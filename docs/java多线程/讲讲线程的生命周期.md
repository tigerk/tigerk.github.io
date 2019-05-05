线程有6种状态，Thread.State定义了线程状态，在某个时间点，线程只能有以下一种状态：

- [`NEW`](https://docs.oracle.com/javase/7/docs/api/java/lang/Thread.State.html#NEW)
  新创建的线程尚未执行

- [`RUNNABLE`](https://docs.oracle.com/javase/7/docs/api/java/lang/Thread.State.html#RUNNABLE)
  线程在JVM中的可执行状态，具体执行与否要看系统的其他资源是否就绪，如CPU

  ```java
  Runnable runnable = new NewState();
  Thread t = new Thread(runnable);
  t.start();
  Log.info(t.getState());
  ```

  线程执行`start()`后就进入到可执行态。

- [`BLOCKED`](https://docs.oracle.com/javase/7/docs/api/java/lang/Thread.State.html#BLOCKED)
  阻塞状态，线程需要等待锁进到Synchronized锁

- [`WAITING`](https://docs.oracle.com/javase/7/docs/api/java/lang/Thread.State.html#WAITING)
  线程等待状态，等待其他线程完成操作，一般来说线程等待由于以下几个方法

  1. [`Object.wait`](https://docs.oracle.com/javase/7/docs/api/java/lang/Object.html#wait()) with no timeout
  2. [`Thread.join`](https://docs.oracle.com/javase/7/docs/api/java/lang/Thread.html#join()) with no timeout
  3. [`LockSupport.park`](https://docs.oracle.com/javase/7/docs/api/java/util/concurrent/locks/LockSupport.html#park())

- [`TIMED_WAITING`](https://docs.oracle.com/javase/7/docs/api/java/lang/Thread.State.html#TIMED_WAITING)
  有限时间等待状态，等待固定时间后重新进入到可执行态

- [`TERMINATED`](https://docs.oracle.com/javase/7/docs/api/java/lang/Thread.State.html#TERMINATED)
  执行结束状态



![lifetime of thread](images/Life_cycle_of_a_Thread_in_Java.jpg)



## sleep(long millis)

在指定时间内睡眠，进入到阻塞状态，线程不会释放当前资源。

> sleep是静态方法，只会对当前运行的线程起作用。当睡眠结束后，会重新进入到可执行状态，是否运行是由系统控制的，所以如果调用Thread.sleep(1000)使得线程睡眠1秒，可能结果会大于1秒。



## join()、join(long millis)、join(long millis, int nanos)

让调用线程等待指定的时间后进入到可执行态

**例子代码，如下**：

```java
/**
 * 在主线程中调用thread.join(); 就是将主线程加入到thread子线程后面等待执行。不过有时间限制，为1毫秒。
 */
public class Test1 {  
    public static void main(String[] args) throws InterruptedException {  
        MyThread t=new MyThread();  
        t.start();  
        t.join(1);//将主线程加入到子线程后面，不过如果子线程在1毫秒时间内没执行完，则主线程便不再等待它执行完，进入就绪状态，等待cpu调度  
        for(int i=0;i<30;i++){  
            System.out.println(Thread.currentThread().getName() + "线程第" + i + "次执行！");  
        }  
    }  
}  
  
class MyThread extends Thread {  
    @Override  
    public void run() {  
        for (int i = 0; i < 1000; i++) {  
            System.out.println(this.getName() + "线程第" + i + "次执行！");  
        }  
    }  
}
```

**在JDK中join方法的源码，如下：**

```java
public final synchronized void join(long millis)    throws InterruptedException {  
    long base = System.currentTimeMillis();  
    long now = 0;  
  
    if (millis < 0) {  
        throw new IllegalArgumentException("timeout value is negative");  
    }  
          
    if (millis == 0) {  
        while (isAlive()) {  
           wait(0);  
        }  
    } else {  
        while (isAlive()) {  
            long delay = millis - now;  
            if (delay <= 0) {  
                break;  
            }  
            wait(delay);  
            now = System.currentTimeMillis() - base;  
        }  
    }  
}
```



## wait & notify/notifyAll

wait & notify/notifyAll这三个都是Object类的方法。使用 wait ，notify 和 notifyAll **前提是先获得调用对象的锁**。

> 1. 调用 wait 方法后，释放持有的对象锁，**线程状态有 Running 变为 Waiting**，并将当前线程放置到对象的 **等待队列**；
> 2. 调用notify 或者 notifyAll 方法后，**等待线程依旧不会从 wait 返回，需要调用 noitfy 的线程释放锁之后，等待线程才有机会从 wait 返回**；
> 3. notify 方法：**将等待队列的一个等待线程从等待队列种移到同步队列中** ，而 notifyAll 方法：**将等待队列种所有的线程全部移到同步队列，被移动的线程状态由 Waiting 变为 Blocked**。



**等待队列（等待池），同步队列（锁池）**，这两者是不一样的。具体如下：

> **同步队列（锁池）** 线程等待其他线程的该对象的锁时，就进入到了该对象的同步队列，线程状态为**Blocked**。准备争夺锁的拥有权
>
> **等待队列（等待池）**：假设一个线程A调用了某个对象的wait()方法，线程A就会释放该对象的锁（因为wait()方法必须出现在synchronized中，这样自然在执行wait()方法之前线程A就已经拥有了该对象的锁），同时 **线程A就进入到了该对象的等待队列（等待池）中，此时线程A状态为Waiting**。
>
> 如果另外的一个线程调用了相同对象的notifyAll()方法，那么 **处于该对象的等待池中的线程就会全部进入该对象的同步队列（锁池）中，准备争夺锁的拥有权**。
>
> 如果另外的一个线程调用了相同对象的notify()方法，那么 **仅仅有一个处于该对象的等待池中的线程（随机）会进入该对象的同步队列（锁池）**。

**被notify或notifyAll唤起的线程是有规律的，具体如下：**

> 1. 如果是通过notify来唤起的线程，那 **先进入wait的线程会先被唤起来**；
> 2. 如果是通过nootifyAll唤起的线程，默认情况是 **最后进入的会先被唤起来**，即LIFO的策略；