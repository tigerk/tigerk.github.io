本系列知识源自网络以及手动实践，整理的目的是因为想做一个可分享的知识沉淀；

> 俗话说，好记性不如烂笔头。记录下来也方便查看



<directory>

### [java基础](docs/java基础)
- [GC机制和原理；GC分哪两种；什么时候会触发Full GC？](docs/java基础/GC%E6%9C%BA%E5%88%B6%E5%92%8C%E5%8E%9F%E7%90%86%EF%BC%9BGC%E5%88%86%E5%93%AA%E4%B8%A4%E7%A7%8D%EF%BC%9B%E4%BB%80%E4%B9%88%E6%97%B6%E5%80%99%E4%BC%9A%E8%A7%A6%E5%8F%91Full%20GC%EF%BC%9F.md)
- [HashMap内部的数据结构是什么？底层是怎么实现的？](docs/java基础/HashMap%E5%86%85%E9%83%A8%E7%9A%84%E6%95%B0%E6%8D%AE%E7%BB%93%E6%9E%84%E6%98%AF%E4%BB%80%E4%B9%88%EF%BC%9F%E5%BA%95%E5%B1%82%E6%98%AF%E6%80%8E%E4%B9%88%E5%AE%9E%E7%8E%B0%E7%9A%84%EF%BC%9F.md)
- [JVM虚拟机内存划分、类加载器、垃圾收集算法、垃圾收集器、class文件结构是如何解析的](docs/java基础/JVM%E8%99%9A%E6%8B%9F%E6%9C%BA%E5%86%85%E5%AD%98%E5%88%92%E5%88%86%E3%80%81%E7%B1%BB%E5%8A%A0%E8%BD%BD%E5%99%A8%E3%80%81%E5%9E%83%E5%9C%BE%E6%94%B6%E9%9B%86%E7%AE%97%E6%B3%95%E3%80%81%E5%9E%83%E5%9C%BE%E6%94%B6%E9%9B%86%E5%99%A8%E3%80%81class%E6%96%87%E4%BB%B6%E7%BB%93%E6%9E%84%E6%98%AF%E5%A6%82%E4%BD%95%E8%A7%A3%E6%9E%90%E7%9A%84.md)
- [JVM里的有几种classloader，为什么会有多种？](docs/java基础/JVM%E9%87%8C%E7%9A%84%E6%9C%89%E5%87%A0%E7%A7%8Dclassloader%EF%BC%8C%E4%B8%BA%E4%BB%80%E4%B9%88%E4%BC%9A%E6%9C%89%E5%A4%9A%E7%A7%8D%EF%BC%9F.md)
- [Java常用List，Arraylist、LinkedList、Vector以及CopyOnWriteArrayList](docs/java基础/Java%E5%B8%B8%E7%94%A8List%EF%BC%8CArraylist%E3%80%81LinkedList%E3%80%81Vector%E4%BB%A5%E5%8F%8ACopyOnWriteArrayList.md)
- [synchronized、volatile区别、synchronized锁粒度、原子性与可见性](docs/java基础/synchronized%E3%80%81volatile%E5%8C%BA%E5%88%AB%E3%80%81synchronized%E9%94%81%E7%B2%92%E5%BA%A6%E3%80%81%E5%8E%9F%E5%AD%90%E6%80%A7%E4%B8%8E%E5%8F%AF%E8%A7%81%E6%80%A7.md)
- [什么是JVM内存模型](docs/java基础/%E4%BB%80%E4%B9%88%E6%98%AFJVM%E5%86%85%E5%AD%98%E6%A8%A1%E5%9E%8B.md)
- [常见的JVM调优方法有哪些？可以具体到调整哪个参数，调成什么值？](docs/java基础/%E5%B8%B8%E8%A7%81%E7%9A%84JVM%E8%B0%83%E4%BC%98%E6%96%B9%E6%B3%95%E6%9C%89%E5%93%AA%E4%BA%9B%EF%BC%9F%E5%8F%AF%E4%BB%A5%E5%85%B7%E4%BD%93%E5%88%B0%E8%B0%83%E6%95%B4%E5%93%AA%E4%B8%AA%E5%8F%82%E6%95%B0%EF%BC%8C%E8%B0%83%E6%88%90%E4%BB%80%E4%B9%88%E5%80%BC%EF%BC%9F.md)
- [说说反射的用途及实现](docs/java基础/%E8%AF%B4%E8%AF%B4%E5%8F%8D%E5%B0%84%E7%9A%84%E7%94%A8%E9%80%94%E5%8F%8A%E5%AE%9E%E7%8E%B0.md)
### [java多线程](docs/java多线程)
- [什么是线程安全，如何实现线程安全？](docs/java多线程/%E4%BB%80%E4%B9%88%E6%98%AF%E7%BA%BF%E7%A8%8B%E5%AE%89%E5%85%A8%EF%BC%8C%E5%A6%82%E4%BD%95%E5%AE%9E%E7%8E%B0%E7%BA%BF%E7%A8%8B%E5%AE%89%E5%85%A8%EF%BC%9F.md)
- [线程池是什么，为什么创建多线程？](docs/java多线程/%E7%BA%BF%E7%A8%8B%E6%B1%A0%E6%98%AF%E4%BB%80%E4%B9%88%EF%BC%8C%E4%B8%BA%E4%BB%80%E4%B9%88%E5%88%9B%E5%BB%BA%E5%A4%9A%E7%BA%BF%E7%A8%8B%EF%BC%9F.md)
- [讲讲线程的生命周期](docs/java多线程/%E8%AE%B2%E8%AE%B2%E7%BA%BF%E7%A8%8B%E7%9A%84%E7%94%9F%E5%91%BD%E5%91%A8%E6%9C%9F.md)
### [java中间件](docs/java中间件)

- [Dubbo Provider服务提供者要控制执行并发请求上限，具体怎么做？](docs/java中间件/Dubbo%20Provider%E6%9C%8D%E5%8A%A1%E6%8F%90%E4%BE%9B%E8%80%85%E8%A6%81%E6%8E%A7%E5%88%B6%E6%89%A7%E8%A1%8C%E5%B9%B6%E5%8F%91%E8%AF%B7%E6%B1%82%E4%B8%8A%E9%99%90%EF%BC%8C%E5%85%B7%E4%BD%93%E6%80%8E%E4%B9%88%E5%81%9A%EF%BC%9F.md)
- [Spring Cloud对比下Dubbo，什么场景下该使用Spring Cloud？](docs/java中间件/Spring%20Cloud%E5%AF%B9%E6%AF%94%E4%B8%8BDubbo%EF%BC%8C%E4%BB%80%E4%B9%88%E5%9C%BA%E6%99%AF%E4%B8%8B%E8%AF%A5%E4%BD%BF%E7%94%A8Spring%20Cloud%EF%BC%9F.md)
- [消息中间件如何保证消息的一致性和如何进行消息的重试机制？](docs/java中间件/%E6%B6%88%E6%81%AF%E4%B8%AD%E9%97%B4%E4%BB%B6%E5%A6%82%E4%BD%95%E4%BF%9D%E8%AF%81%E6%B6%88%E6%81%AF%E7%9A%84%E4%B8%80%E8%87%B4%E6%80%A7%E5%92%8C%E5%A6%82%E4%BD%95%E8%BF%9B%E8%A1%8C%E6%B6%88%E6%81%AF%E7%9A%84%E9%87%8D%E8%AF%95%E6%9C%BA%E5%88%B6%EF%BC%9F.md)

### [分布式锁](docs/分布式锁)
- [Redis的分布式锁官方算法](docs/分布式锁/Redis%E7%9A%84%E5%88%86%E5%B8%83%E5%BC%8F%E9%94%81%E5%AE%98%E6%96%B9%E7%AE%97%E6%B3%95.md)
- [zookeeper的分布式锁方案](docs/分布式锁/zookeeper%E7%9A%84%E5%88%86%E5%B8%83%E5%BC%8F%E9%94%81%E6%96%B9%E6%A1%88.md)

</directory>
