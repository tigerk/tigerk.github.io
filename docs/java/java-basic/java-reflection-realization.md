### 反射有啥用？

Java 反射机制可以动态地创建对象并调用其属性，主要是实现了在**运行时**加载不同的对象和类。

比如：

**在运行时**可以根据配置文件加载不同的对象和类，如sping的bean加载等

再比如：

Tomcat根据不同的请求加载初始化不同的对象。



### 那反射具体怎么实现呢？

```java
import java.lang.reflect.Method;

public class DumpMethods {
    public static void main(String[] args) {
        try {
            Class c = Class.forName("java.util.Stack");
            Method m[] = c.getDeclaredMethods();
            for (int i = 0; i < m.length; i++) {
                System.out.println(m[i].toString());

            }
        } catch (Throwable e) {
            System.err.println(e);
        }
    }
}

// Output:
public java.lang.Object java.util.Stack.push(java.lang.Object)
public synchronized java.lang.Object java.util.Stack.pop()
public synchronized java.lang.Object java.util.Stack.peek()
public boolean java.util.Stack.empty()
public synchronized int java.util.Stack.search(java.lang.Object)
```

以上就是`java.util.Stack`的所有方法，包括完整的参数和返回值。