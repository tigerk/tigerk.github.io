### Java Memory Model

Java内存模型定义了多线程之间共享变量的可见性以及如何在需要的时候对共享变量进行同步。



### Java内存模型

在JMM中，将内存分为线程栈（`Thread Stack`）和堆（`Heap`）。

1. Thread Stack 保存了当前线程的局部变量（`local variable`），线程仅能访问自己的线程栈，无法访问其他线程所创建的局部变量。即使两个线程正在执行同一段代码，它们会在各自的线程栈中创建自己的局部变量。

2. Heap保存了java应用的所有对象，所有线程的对象都在Heap中。

![](images/java-memory-model-2.png)

java基本类型：`boolean`, `byte`, `short`, `char`, `int`, `long`, `float`, `double`

局部变量是8个类型，保存在Thread Stack。如果是对象，则引用保存在Thread Stack，对象保存在Heap中。

对象的成员变量都保存在Heap中。

静态类变量和类定义一起存储在Heap中。

线程通过引用可以访问Heap中的对象，也可以访问对象的成员变量，多线程可以同时访问Heap中的同一个对象，但是每个线程都有各自的成员变量副本。可以参考下图：

![](images/java-memory-model-3.png)

上图的两个线程都各自有`Object 3`的引用；看`Object 3`的箭头指向`Object2` 和 `Object4`，俩线程都可以访问。



举个栗子，说明在代码注释中

```java
public class MyRunnable implements Runnable {
    public void run() {
        methodOne();
    }
  
    public void methodOne() {
        // localVariable1保存在各自的Thread Stack中，
        int localVariable1 = 98;

        // localVariable2对象保存在Heap中，每个线程保存了localVariable2的引用。
        // localVariable2 指向的是一个静态变量引用的对象。静态变量保存在堆中，仅创建一个。
        MySharedObject localVariable2 = MySharedObject.sharedInstance;

        //... do more with local variables.

        methodTwo();
    }

    public void methodTwo() {
        // 保存在Heap中，每个线程都指向了各自在Heap中创建的对象。
        Integer localVariable1 = new Integer(99);

        //... do more with local variable.
    }

    public static void main(String args[]) {
        // 创建了两个线程
        MyRunnable r1 = new MyRunnable();
        MyRunnable r2 = new MyRunnable();

        new Thread(r1).start();
        new Thread(r2).start();
    }
}

public class MySharedObject {

    public static final MySharedObject sharedInstance = new MySharedObject();

    //member variables pointing to two objects on the heap
    // 成员变量指向Heap中的两个对象
    public Integer object2 = new Integer(22);
    public Integer object4 = new Integer(44);

    // 以下两个变量和对象一起保存在Heap中
    public long member1 = 12345L;
    public long member2 = 67890L;
}
```



### 硬件内存架构

![](images/java-memory-model-4.png)

现在的计算机，通常提供2个（或以上）的CPU，甚至一个CPU还是多个核心的。这些多核的电脑，就能够处理多个线程同时运行。每个CPU在特定的时间内都能够运行一个线程。这将意味着如果编写的Java程序是多线程的话，那么每个CPU将会运行一个线程。

每个CPU存在一系列的寄存器。CPU从这些寄存器上读写数据要比在主内存上读写数据快得多。

每个CPU也拥有自己的一定容量的缓存内存。而CPU从这些内存上读写数据的熟读又要比从主内存中读写要快得多，但还是比寄存器慢。所以CPU缓存是介于寄存器以及内存之间的。一些CPU还拥有多级缓存（一级缓存、二级缓存），但这跟JMM的抽象没太大的关系。我们只需要指定CPU拥有这一些内存就可以了。

一个计算机还包含了主内存（RAM，后面称主存）。所有的CPU都能够访问主存中的数据。主存的大小也要比CPU中的缓存大得多。

所以实际上，为了提升访问速度，当CPU需要从主存中读取数据的时候，会先将一部分数据读取到CPU缓存中去，然后再从缓存中读取到CPU的寄存器中。然后再进行一系列的操作。当CPU需要将数据刷新到主存时，会先将数据从寄存器刷新到缓存，再讲缓存中的数据刷新到主存里面。

在CPU缓存中，CPU在缓存中写完数据，然后再将数据写到主存。每次读写都不会整个缓存进行刷新。缓存中每次刷新都只会刷新最小的单位称为“缓存行”。每次读写数据，都是以“缓存行”作为单位进行读写。



### JMM以及硬件内存结构的对应关系

正如刚刚提到的，JMM模型与现实中计算机的模型有一些不同。硬件中的内存并不会将**堆**和**栈**分开来。在硬件中，堆和栈中的数据都存在于主存中。栈和堆中的数据，都将可能被读取到CPU缓存以及CPU寄存器当中。下图是两个模型的对应关系：

![](images/java-memory-model-5.png)

很多时候，对象和变量存储于这几个不同的区域中时，问题就会变得很复杂。两个主要的问题就是：
– 线程更新共享的变量
– 当线程读、写、确认数据的时候的竞争情况



- 共享对象的可见性

如果两（多）个线程同时访问堆中的一个共享对象，若没有使用`volatile`关键字进行修饰变量或者对修改变量进行同步，一个线程去更新对象中的值，另外的线程将不能同时感知。

试想一下，一个共享对象在主存中。一个CPU的上线程将对象读取到他们各自的CPU缓存中，并且做出一些修改。但是这个CPU并不会立即把缓存中的修改刷新到主存中，这时候其他CPU读取到的主存中的数据依然是旧的版本，那么这时候多线程将会造成数据的错误。

接下来的图片将简单描述这个情况。左边的CPU将共享对象读取到缓存中并且修改对象中的数据`count = 2`，这时候数据还没刷新到主存当中，那右边的CPU依然只能读到旧的值（也就是`1`）。

![](images/java-memory-model-6.png)

解决这个问题的方法就是使用Java中的`volatile`关键字。这个关键字能够让每个线程直接从主存中读取数据，并且实时的将修改后的数据写回主存。

- 线程竞争情况

如果多个线程对同一个共享对象进行写操作，那么将会出现线程安全问题。

想象一下如果线程A从共享对象中读取`count`变量到CPU缓存中。另外线程B也做同样的操作。当线程A和B同时修改`count`变量时。那这时候`count`变量在两个不同的CPU中都进行了操作。

如果这两个操作是顺序执行的，那么这个值在主存中的情况应该是**加上2**的情况。但是因为这个操作并不是同步的，所以其实在主存中的情况应该只是**加上1**，并不是我们预想中的**加上2**的情况。

操作如图所示：

![](images/java-memory-model-7.png)

解决这个问题的方法是使用Java的`synchronized`代码块。代码块中的代码保证数据只能被一个线程读写（无论有没有被`volatile`关键字修饰），并且在需要的时候读取到CPU缓存，而线程运行结束时将数据写回主存。



[Java Memory Model]: http://tutorials.jenkov.com/java-concurrency/java-memory-model.html#bridging-the-gap-between-the-java-memory-model-and-the-hardware-memory-architectur