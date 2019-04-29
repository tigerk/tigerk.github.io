### JVM里的有几种classloader，为什么会有多种？

ClassLoader的具体作用就是将class文件加载到jvm虚拟机中去，程序就可以正确运行了。

JDK 默认提供了如下三种`Class Loader`

1.  **Bootstrap loader**  最顶层的加载类，主要加载核心类库，%JRE_HOME%\lib下的rt.jar、resources.jar、charsets.jar和class等。另外需要注意的是可以通过启动jvm时指定-Xbootclasspath和路径来改变Bootstrap ClassLoader的加载目录。比如java -Xbootclasspath/a:path被指定的文件追加到默认的bootstrap路径中。

2. **Extention ClassLoader** 扩展的类加载器，加载目录%JRE_HOME%\lib\ext目录下的jar包和class文件。还可以加载-D java.ext.dirs选项指定的目录。
3. **Appclass Loader**也称为SystemAppClass 加载当前应用的classpath的所有类。



加载顺序：

```mermaid
graph LR
A[Bootstrap Loader] --> B[Extension ClassLoader]
B --> C(Appclass Loader)
```



可以看sun.misc.Launcher源码，它是一个java虚拟机的入口应用。

```java
public class Launcher {
    private static URLStreamHandlerFactory factory = new Launcher.Factory();
    private static Launcher launcher = new Launcher();
    private static String bootClassPath = System.getProperty("sun.boot.class.path");
    private ClassLoader loader;
    private static URLStreamHandler fileHandler;

    public static Launcher getLauncher() {
        return launcher;
    }

    public Launcher() {
        Launcher.ExtClassLoader var1;
        try {
            var1 = Launcher.ExtClassLoader.getExtClassLoader();
        } catch (IOException var10) {
            throw new InternalError("Could not create extension class loader", var10);
        }

        try {
            this.loader = Launcher.AppClassLoader.getAppClassLoader(var1);
        } catch (IOException var9) {
            throw new InternalError("Could not create application class loader", var9);
        }

        ...

    }
}
```

Launcher初始化了ExtClassLoader和AppClassLoader。

虽然没有看见BootstrapClassLoader，但通过System.getProperty("sun.boot.class.path") 得到了字符串bootClassPath。

这个应该就是BootstrapClassLoader加载的jar包路径。

```java
// 这些打印出来的都是JRE目录下的jar包或者是class文件。
System.out.println(System.getProperty("sun.boot.class.path"));
```

结果：

```
/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/jre/lib/resources.jar:
/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/jre/lib/rt.jar:
/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/jre/lib/sunrsasign.jar:
/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/jre/lib/jsse.jar:
/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/jre/lib/jce.jar:
/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/jre/lib/charsets.jar:
/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/jre/lib/jfr.jar:
/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/jre/classes
```

BootstrapClassLoader、ExtClassLoader、AppClassLoader实际是查阅相应的环境属性`sun.boot.class.path`、`java.ext.dirs`和`java.class.path`来加载资源文件的。