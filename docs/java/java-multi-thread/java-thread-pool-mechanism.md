### 为什么创建多线程

先来举个栗子，每个线程消耗25M内存，如果有10个请求，建立10个线程，消耗250M内存，看着没啥问题，但是来10000个请求，需要建立1万个线程，那1万个线程就消耗会250000M，也就是250G内存，这大大超出了服务器的容量，而且大部分时间并不会需要建立1万个线程，会造成大量的内存浪费，频繁创建和销毁线程也会带来开销。

- 合理利用线程池能够带来三个好处：

* 降低资源消耗。通过重复利用已创建的线程降低线程创建和销毁造成的消耗。
* 提高响应速度。当任务到达时，任务可以不需要的等到线程创建就能立即执行。
* 提高线程的可管理性。线程是稀缺资源，如果无限制的创建，不仅会消耗系统资源，还会降低系统的稳定性，使用线程池可以进行统一的分配，调优和监控。

先来个demo：

```java
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池demo
 */
public class MyPool {

    public static void main(String[] args) {
        /**
         * 在java doc中，并不提倡我们直接使用ThreadPoolExecutor，而是使用Executors类中提供的几个静态方法来创建线程池：
         * Executors.newCachedThreadPool();        //创建一个缓冲池，缓冲池容量大小为Integer.MAX_VALUE
         * Executors.newSingleThreadExecutor();   //创建容量为1的缓冲池
         * Executors.newFixedThreadPool(int);    //创建固定容量大小的缓冲池
         */
        ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 200, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(5));

        for (int i = 1; i <= 15; i++) {
            MyThread myTask = new MyThread(i + "");
            executor.execute(myTask);
            System.out.println("线程池中线程数目：" + executor.getPoolSize() + "，队列中等待执行的任务数目：" + executor.getQueue().size() + "，已执行完别的任务数目：" + executor.getCompletedTaskCount());
        }
        executor.shutdown();
    }
}

class MyThread implements Runnable {
    private String threadName;

    public MyThread(String name) {
        threadName = name;
    }

    @Override
    public void run() {
        System.out.println("正在执行任务中，名称：" + threadName);
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("task " + threadName + "执行完毕");
    }
}
```

ThreadPoolExecutor是线程池的核心类，看下hreadPoolExecutor的构造函数
```java
/**
     * Creates a new {@code ThreadPoolExecutor} with the given initial
     * parameters and default thread factory and rejected execution handler.
     * It may be more convenient to use one of the {@link Executors} factory
     * methods instead of this general purpose constructor.
     *
     * @param corePoolSize the number of threads to keep in the pool, even
     *        if they are idle, unless {@code allowCoreThreadTimeOut} is set
     * @param maximumPoolSize the maximum number of threads to allow in the
     *        pool
     * @param keepAliveTime when the number of threads is greater than
     *        the core, this is the maximum time that excess idle threads
     *        will wait for new tasks before terminating.
     * @param unit the time unit for the {@code keepAliveTime} argument
     * @param workQueue the queue to use for holding tasks before they are
     *        executed.  This queue will hold only the {@code Runnable}
     *        tasks submitted by the {@code execute} method.
     * @throws IllegalArgumentException if one of the following holds:<br>
     *         {@code corePoolSize < 0}<br>
     *         {@code keepAliveTime < 0}<br>
     *         {@code maximumPoolSize <= 0}<br>
     *         {@code maximumPoolSize < corePoolSize}
     * @throws NullPointerException if {@code workQueue} is null
     */
    public ThreadPoolExecutor(int corePoolSize,
                              int maximumPoolSize,
                              long keepAliveTime,
                              TimeUnit unit,
                              BlockingQueue<Runnable> workQueue) {
        this(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue,
             Executors.defaultThreadFactory(), defaultHandler);
    }
```

对参数进行下说明：
* corePoolSize 线程池的大小
* maximumPoolSize 线程池的最大容量
* keepAliveTime 线程存活的时间
* TimeUnit 时间单位
* workQueue 用于保存任务的队列

corePoolSize是在线程池中保持的最少线程数，没有任务也会存在，而maximumPoolSize是线程池能创建的最大线程数

总结一下线程池添加任务的整个流程：

1. 线程池刚刚创建是，线程数量为0；
2. 执行execute添加新的任务时会在线程池创建一个新的线程；
3. 当线程数量达到corePoolSize时，再添加新任务则会将任务放到workQueue队列；
4. 当队列已满放不下新的任务，再添加新任务则会继续创建新线程，但线程数量不超过maximumPoolSize；
5. 当线程数量达到maximumPoolSize时，再添加新任务则会抛出异常。

在java doc中，并不提倡我们直接使用ThreadPoolExecutor，而是使用Executors类中提供的几个静态方法来创建线程池：
Executors有三种方式来创建线程池，如下三种：
* Executors.newCachedThreadPool();        //创建一个缓冲池，缓冲池容量大小为Integer.MAX_VALUE
* Executors.newSingleThreadExecutor();   //创建容量为1的缓冲池
* Executors.newFixedThreadPool(int);    //创建固定容量大小的缓冲池

Executors的底层也是用ThreadPoolExecutor来创建的，只是提供了不同的接口。

在前面提到了任务缓存队列，workQueue，它用来存放等待执行的任务。
workQueue的类型为BlockingQueue<Runnable>，通常可以取下面三种类型：

1. ArrayBlockingQueue：基于数组的先进先出队列，此队列创建时必须指定大小；
2. LinkedBlockingQueue：基于链表的先进先出队列，如果创建时没有指定此队列大小，则默认为Integer.MAX_VALUE；
3. synchronousQueue：这个队列比较特殊，它不会保存提交的任务，而是将直接新建一个线程来执行新来的任务。

### 那如何合理配置线程池大小？
一般来说有两种方式来考量
* 如果是CPU密集型任务，就需要尽量压榨CPU，参考值可以设为 NCPU+1
* 如果是IO密集型任务，参考值可以设置为2*NCPU

当然具体问题还需要具体分析，要查看任务执行情况、系统负载等