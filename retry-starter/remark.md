# 重试框架之Spring-Retry
Spring Retry 为 Spring 应用程序提供了声明性重试支持。它用于Spring批处理、Spring集成、Apache Hadoop(等等)。它主要是针对可能抛出异常的一些调用操作，进行有策略的重试
## Spring-Retry的普通使用方式
### 1.准备工作
我们只需要加上依赖:
```xml
 <dependency>
    <groupId>org.springframework.retry</groupId>
    <artifactId>spring-retry</artifactId>
    <version>1.2.2.RELEASE</version>
 </dependency>
```
准备一个任务方法，我这里是采用一个随机整数，根据不同的条件返回不同的值，或者抛出异常

FixedBackOffPolicy
在继续之前暂停一段固定时间的BackOffPolicy的实现。使用sleep .sleep(long)实现暂停。setBackOffPeriod(long)是线程安全的，在多个线程执行过程中调用setBackOffPeriod是安全的，但是这可能会导致单个重试操作具有不同间隔的暂停。