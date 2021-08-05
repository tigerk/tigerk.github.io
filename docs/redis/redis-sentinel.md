#Redis哨兵模式

sentinel，中文名是哨兵。哨兵是 Redis 集群架构中非常重要的一个组件，主要有以下功能：

1. 集群监控：负责监控 Redis master 和 slave 进程是否正常工作。
2. 消息通知：如果某个 Redis 实例有故障，那么哨兵负责发送消息作为报警通知给管理员。
3. 故障转移：如果 master node 挂掉了，会自动转移到 slave node 上。
4. 配置中心：如果故障转移发生了，通知 client 客户端新的 master 地址



## 哨兵模式的选举过程是什么样的？
### slave -> master 选举算法
如果一个 master 被认为 odown 了，而且 majority 数量的哨兵都允许主备切换，那么某个哨兵就会执行主备切换操作，

此时首先要选举一个 slave 来，会考虑 slave 的一些信息：

1. 跟 master 断开连接的时长
2. slave 优先级
3. 复制 offset
4. run id

如果一个 slave 跟 master 断开连接的时间已经超过了 down-after-milliseconds 的 10 倍，外加 master 宕机的时长，那么 slave 就被认为不适合选举为 master。

```script
# 判断 slave 能否选举为 master
(down-after-milliseconds * 10) + milliseconds_since_master_is_in_SDOWN_state
```

接下来会对 slave 进行排序：

1. 按照 slave 优先级进行排序，slave priority 越低，优先级就越高。
2. 如果 slave priority 相同，那么看 replica offset，哪个 slave 复制了越多的数据，offset 越靠后，优先级就越高。
3. 如果上面两个条件都相同，那么选择一个 run id 比较小的那个 slave。

### Raft算法

**优点**
相比于Paxos算法更容易理解，⽽且更容易工程化实现。

**缺点**
Raft有一个很强的假设是主（leader）和备（follower）都按顺序投票，为了便于阐述，
以数据库事务为例：
* 主库按事务顺序发送事务日志
* 备库按事务顺序持久化事务和应答主库
* 主库按事务顺序提交事务

由于不同的事务可能被不同工作线程处理，事务日志可能被不同的网络线程发送和接收，因为网络抖动和Linux线程调度等原因，一个备库可能会出现接收到了事务日志#5-#9，但没有接收到事务#4，因此#5-#9的所有事务都需要hold住（在内存），不能持久化，也不能应答主库：<img src="images/v2-14e861befc23473f7234e5eda52f6e09_720w.png"/>#1-#3为已经持久化和应答的事务日志#5-#9为已经收到但却不能持久化和应答的事务日志#4为未收到的事务日志。

###什么是脑裂？
**脑裂**，也就是说，某个 master 所在机器突然脱离了正常的网络，跟其他 slave 机器不能连接，但是实际上 master 还运行着。

此时哨兵可能就会认为 master 宕机了，然后开启选举，将其他 slave 切换成了 master。

这个时候，集群里就会有两个 master ，也就是所谓的脑裂。

###脑裂有什么影响？
1. slave 切换成 master，但是 Client 还没有切换到新的 master，继续想老的 master写数据。

2. 老 master 恢复时作为 slave 挂到新 master，自己的数据清空，重新从新 master 复制数据。

3. 如上所述，新 master 上没有刚才的client写入的数据，所以，这部分数据丢失了。

