import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author tigerkim
 */
public class SimpleBlockingQueue {
    private List<Object> container = new ArrayList<>();

    private Lock lock = new ReentrantLock();

    private final Condition isEmpty = lock.newCondition();

    private final Condition isFull = lock.newCondition();

    private AtomicInteger size = new AtomicInteger(0);

    private volatile int capacity;

    SimpleBlockingQueue(int capacity) {
        this.capacity = capacity;
    }

    public void add(Object element) {
        lock.lock();
        try {
            try {
                while (size.intValue() >= capacity) {
                    System.out.println("队列已满，释放锁，等待消费者消费数据");
                    isFull.await();
                }
            } catch (InterruptedException e) {
                isFull.signal();
                e.printStackTrace();
            }
            System.out.println("添加元素:" + element);
            size.incrementAndGet();
            container.add(element);
            System.out.println("唤醒阻塞线程...");


            isEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    public Object take() {
        lock.lock();
        try {
            try {
                while (size.intValue() == 0) {
                    System.out.println("阻塞队列空了，进入阻塞");
                    isEmpty.await();
                }
            } catch (InterruptedException e) {
                isEmpty.signal();
                e.printStackTrace();
            }

            size.decrementAndGet();
            Object res = container.get(0);
            container.remove(0);
            isFull.signal();

            return res;
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        SimpleBlockingQueue sbq = new SimpleBlockingQueue(10);
        Thread t1 = new Thread(() -> {
            try {
                sbq.add(1);
                Thread.sleep(1000);
                sbq.add(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Thread t2 = new Thread(() -> {
            while (true) {
                Object element = sbq.take();

                System.out.println("取出阻塞队列的元素:" + element);
            }
        });

        t2.start();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t1.start();

    }
}
