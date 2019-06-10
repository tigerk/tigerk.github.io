### 什么是线程安全

线程安全是出现在多线程下的概念，

如果一段代码在多线程同时执行期间运行正常，能达到预期效果，就是线程安全的。

在多个线程访问共享数据时，在给定时间只有一个线程能访问共享数据，保证数据一致性。



常见问题：

## i++是不是线程安全的？

`i++`需要三个独立的步骤：

1. 读取当前值
2. 获取要更新的值
3. 赋值

所以`i++` 不是原子操作，不是线程安全的。



举个栗子

```java

public class ThreadSafety {

    public static void main(String[] args) throws InterruptedException {

        ProcessingThread pt = new ProcessingThread();
        Thread t1 = new Thread(pt, "t1");
        t1.start();
        Thread t2 = new Thread(pt, "t2");
        t2.start();
        //wait for threads to finish processing
        t1.join();
        t2.join();
        System.out.println("Processing count=" + pt.getCount());
    }

}

class ProcessingThread implements Runnable {
    private int count;

    @Override
    public void run() {
        for (int i = 1; i < 5; i++) {
            processSomething(i);
//            synchronized (this) {
                count++;
//            }
        }
    }

    int getCount() {
        return this.count;
    }

    private void processSomething(int i) {
        // processing some job
        try {
            Thread.sleep(i * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
```

以上代码运行后，每次循环count增加1，期望结果应该是8，但是你发现结果是在6，7，8。 这是因为count++不是原子操作。



#### Synchronized是最常用、最简单的方式

```java
synchronized (this) {
		count++;
}
```

结果：

```
Processing count=8
```



还有哪些方法？

- Use of Atomic Wrapper classes from *java.util.concurrent.atomic* package. For example AtomicInteger
- Use of locks from *java.util.concurrent.locks* package.
- Using thread safe collection classes, check this post for usage of [ConcurrentHashMap](https://www.journaldev.com/122/concurrenthashmap-in-java) for thread safety.
- Using volatile keyword with variables to make every thread read the data from memory, not read from thread cache.

