# 分布式锁

## 分布式锁实现方式

想要实现分布式锁，必须借助一个外部系统，所有进程都去这个系统上申请「加锁」。

而这个外部系统，必须要实现「互斥」的能力，即两个请求同时进来，只会给一个进程返回成功，另一个返回失败（或等待）。

这个外部系统，可以是 MySQL，也可以是 Redis 或 Zookeeper。但为了追求更好的性能，我们通常会选择使用 Redis 或 Zookeeper 来做。

## 常用的分布式锁都有什么？

>1.基于数据库实现分布式锁

>2.基于Redisson实现分布式锁（系统默认提供）

>3.基于Zookeeper实现分布式锁（频繁创建节点，系统未提供）

## Redisson提供的分布式锁

### 1.分布式可重入锁

Redisson的分布式可重入锁RLock 实现了java.util.concurrent.locks.Lock接口，同时还支持自动过期解锁。下面是RLock的基本使用方法：

```
RLock lock = redisson.getLock("anyLock");

lock.lock();// 最常见的使用方法

lock.lock(10, TimeUnit.SECONDS);//支持过期解锁功能，10秒钟以后自动解锁，无需调用unlock方法手动解锁
 
boolean res = lock.tryLock(100, 10, TimeUnit.SECONDS);// 尝试加锁，最多等待100秒，上锁以后10秒自动解锁
```

Redisson同时还为分布式锁提供了异步执行的相关方法：

``` java
RLock lock = redisson.getLock("anyLock");

lock.lockAsync();

lock.lockAsync(10, TimeUnit.SECONDS);

Future<Boolean> res = lock.tryLockAsync(100, 10, TimeUnit.SECONDS);
```

### 公平锁

Redisson分布式可重入公平锁也是实现了java.util.concurrent.locks.Lock接口的一种RLock对象。在提供了自动过期解锁功能的同时，保证了当多个Redisson客户端线程同时请求加锁时，优先分配给先发出请求的线程。

``` java

RLock fairLock = redisson.getFairLock("anyLock");
// 最常见的使用方法
fairLock.lock();
 
// 支持过期解锁功能
// 10秒钟以后自动解锁
// 无需调用unlock方法手动解锁
fairLock.lock(10, TimeUnit.SECONDS);
 
// 尝试加锁，最多等待100秒，上锁以后10秒自动解锁
boolean res = fairLock.tryLock(100, 10, TimeUnit.SECONDS);
...
fairLock.unlock();
```

### 其他锁

Redisson还提供了其他机制的锁，如联锁(MultiLock)、红锁(RedLock)等。详细可参考：分布式锁和同步器 (https://github.com/redisson/redisson/wiki/8.-%E5%88%86%E5%B8%83%E5%BC%8F%E9%94%81%E5%92%8C%E5%90%8C%E6%AD%A5%E5%99%A8)

## 分布式锁的条件

>1.排他性：同一时间，只能有一个客户端获取锁，其他客户端不能同事获取锁。
>2.避免死锁：这把锁在一定的时间后需要释放，否则会产生死锁，这里面包括正常释放锁和非正常释放锁，比如即使一个客户端在持锁期间发生故障而没有释放锁也要保证后续的客户端能枷锁。
>3.自己解锁：加锁和解锁都应该是同一个客户端去完成，不能去解别人的锁。
>4.高可用：获取和释放锁必须高可用且优秀。

## 1.数据库分布式锁？

在数据库中创建一个表，并在字段上创建唯一索引，想要执行某个方法，就使用这个方法名向表中插入数据，成功插入则获取锁，执行完成后删除对应的行数据释放锁。

## 2.Zookeeper分布式锁？

### Zookeeper的节点类型和watch机制

> zookeeper的节点类型：

```
PERSISTENT 持久化节点
PERSISTENT_SEQUENTIAL 顺序自动编号持久化节点，这种节点会根据当前已存在的节点数自动加 1
EPHEMERAL 临时节点， 客户端session超时这类节点就会被自动删除
EPHEMERAL_SEQUENTIAL 临时自动编号节点
```
> zookeeper的watch机制：

```
Znode发生变化（Znode本身的增加，删除，修改，以及子Znode的变化）可以通过Watch机制通知到客户端。那么要实现Watch，就必须实现org.apache.zookeeper.Watcher接口，并且将实现类的对象传入到可以Watch的方法中。
Zookeeper中所有读操作（getData()，getChildren()，exists()）都可以设置Watch选项。Watch事件具有one-time trigger（一次性触发）的特性，如果Watch监视的Znode有变化，那么就会通知设置该Watch的客户端。

```

### 实现思路

> 定义锁：

在通常的java并发编程中，有两种常见的方式可以用来定义锁，分别是synchronized机制和JDK5提供的ReetrantLock。然而，在zookeeper中，
没有类似于这样的API可以直接使用，而是通过Zookeeper上的数据节点来表示一个锁，例如/exclusive_lock/lock节点就可以定义为一个锁。

> 获取锁:

在需要获取锁的时候，所有的客户端都会试图通过调用create()接口，在/exclusive_lock节点下创建临时子节点/exclusive_lock/lock。zookeeper会保证在所有客户端中，最终只有一个客户端能够创建成功，
那么就可以认为该客户端获得了锁。同时，所有没有获得锁的客户端就需要到/exclusive_lock节点上注册一个子节点变更的Watcher监听，以便实时监听到lock节点的变更情况。

> 释放锁:

在定义锁部分，我们已经提到，/exclusive_lock/lock是一个临时节点，因此在以下两种情况下，都有可能释放锁。

1.当前获取锁的客户端发生宕机，那么Zookeeper上的这个临时节点就会被移除。

2.正常执行完业务逻辑之后，客户端就会主动将自己创建的临时节点删除


### 参考示例代码

参考了ReentrantLock的设计思路，使用了模板方法设计模式。

### 小结 zookeeper 实现分布式锁的过程

zookeeper 实现分布式锁流程

> 1.客户端 1 和 2 都尝试创建「临时节点」，例如 /lock
> 2.假设客户端 1 先到达，则加锁成功，客户端 2 加锁失败
> 3.客户端 1 操作共享资源
> 4.客户端 1 删除 /lock 节点，释放锁

你应该也看到了，Zookeeper 不像 Redis 那样，需要考虑锁的过期时间问题，它是采用了「临时节点」，保证客户端 1 拿到锁后，只要连接不断，就可以一直持有锁。
而且，如果客户端 1 异常崩溃了，那么这个临时节点会自动删除，保证了锁一定会被释放。 不错，没有锁过期的烦恼，还能在异常时自动释放锁，是不是觉得很完美？ 其实不然。
思考一下，客户端 1 创建临时节点后，Zookeeper 是如何保证让这个客户端一直持有锁呢？
原因就在于，客户端 1 此时会与 Zookeeper 服务器维护一个 Session，这个 Session 会依赖客户端「定时心跳」来维持连接。
如果 Zookeeper 长时间收不到客户端的心跳，就认为这个 Session 过期了，也会把这个临时节点删除。
图见：[zookeeper实现分布式锁原理图.png]

同样地，基于此问题，我们也讨论一下 GC 问题对 Zookeeper 的锁有何影响：

> 1.客户端 1 创建临时节点 /lock 成功，拿到了锁
> 2.客户端 1 发生长时间 GC
> 3.客户端 1 无法给 Zookeeper 发送心跳，Zookeeper 把临时节点「删除」
> 4.客户端 2 创建临时节点 /lock 成功，拿到了锁
> 5.客户端 1 GC 结束，它仍然认为自己持有锁（冲突）

可见，即使是使用 Zookeeper，也无法保证进程 GC、网络延迟异常场景下的安全性。

Zookeeper 的优点：

> 1.不需要考虑锁的过期时间
> 2.watch 机制，加锁失败，可以 watch 等待锁释放，实现乐观锁

但它的劣势是：

> 1.性能不如 Redis
> 2.部署和运维成本高
> 3.客户端与 Zookeeper 的长时间失联，锁被释放问题

## 3.Redis分布式锁？

### 1.最初 SETNX 命令实现分布式锁
想要实现分布式锁，必须要求 Redis 有「互斥」的能力，我们可以使用 SETNX 命令，这个命令表示SET if Not eXists，即如果 key 不存在，才会设置它的值，否则什么也不做。
两个客户端进程可以执行这个命令，达到互斥，就可以实现一个分布式锁。
客户端 1 申请加锁，加锁成功：
```
127.0.0.1:6379> SETNX lock 1
(integer) 1     // 客户端1，加锁成功
```
客户端 2 申请加锁，因为它后到达，加锁失败：
```
127.0.0.1:6379> SETNX lock 1
(integer) 0     // 客户端2，加锁失败
```
此时，加锁成功的客户端，就可以去操作「共享资源」，例如，修改 MySQL 的某一行数据，或者调用一个 API 请求。
操作完成后，还要及时释放锁，给后来者让出操作共享资源的机会。如何释放锁呢？
也很简单，直接使用 DEL 命令删除这个 key 即可：
```
127.0.0.1:6379> DEL lock // 释放锁
(integer) 1
```
这个逻辑非常简单，整体的路程就是这样，图见：[setnx实现分布式锁流程.png]
但是，它存在一个很大的问题，当客户端 1 拿到锁后，如果发生下面的场景，就会造成「死锁」：

> 1.程序处理业务逻辑异常，没及时释放锁
> 2.进程挂了，没机会释放锁

这时，这个客户端就会一直占用这个锁，而其它客户端就「永远」拿不到这把锁了。 怎么解决这个问题呢？

### 2.升级使用 redis 新版的 SET 命令实现同时加锁和设置过期时间避免死锁问题

我们很容易想到的方案是，在申请锁时，给这把锁设置一个「租期」。
在 Redis 中实现时，就是给这个 key 设置一个「过期时间」。这里我们假设，操作共享资源的时间不会超过 10s，那么在加锁时，给这个 key 设置 10s 过期即可：
```
127.0.0.1:6379> SETNX lock 1    // 加锁
(integer) 1
127.0.0.1:6379> EXPIRE lock 10  // 10s后自动过期
(integer) 1
```
这样一来，无论客户端是否异常，这个锁都可以在 10s 后被「自动释放」，其它客户端依旧可以拿到锁。
但这样真的没问题吗？ 还是有问题。 现在的操作，加锁、设置过期是 2 条命令，有没有可能只执行了第一条，第二条却「来不及」执行的情况发生呢？例如：

> 1.SETNX 执行成功，执行 EXPIRE 时由于网络问题，执行失败
> 2.SETNX 执行成功，Redis 异常宕机，EXPIRE 没有机会执行
> 3.SETNX 执行成功，客户端异常崩溃，EXPIRE 也没有机会执行

总之，这两条命令不能保证是原子操作（一起成功），就有潜在的风险导致过期时间设置失败，依旧发生「死锁」问题。
怎么办？ 在 Redis 2.6.12 版本之前，我们需要想尽办法，保证 SETNX 和 EXPIRE 原子性执行，还要考虑各种异常情况如何处理。
但在 Redis 2.6.12 之后，Redis 扩展了 SET 命令的参数，用这一条命令就可以了：
```
// 一条命令保证原子性执行
127.0.0.1:6379> SET lock 1 EX 10 NX
OK
```
这样就解决了死锁问题，也比较简单。 我们再来看分析下，它还有什么问题？ 试想这样一种场景：
> 1.客户端 1 加锁成功，开始操作共享资源
> 2.客户端 1 操作共享资源的时间，「超过」了锁的过期时间，锁被「自动释放」
> 3.客户端 2 加锁成功，开始操作共享资源
> 4.客户端 1 操作共享资源完成，释放锁（但释放的是客户端 2 的锁）

看到了么，这里存在两个严重的问题：

> 1.锁过期：客户端 1 操作共享资源耗时太久，导致锁被自动释放，之后被客户端 2 持有
> 2.释放别人的锁：客户端 1 操作共享资源完成后，却又释放了客户端 2 的锁（自动释放锁是为了避免死锁而设计的，但是别忘了我们还有主动释放锁，这里释放客户端 2 的锁就是由于客户端 1 主动释放锁导致的）

导致这两个问题的原因是什么？我们一个个来看。
> 1.第一个问题，可能是我们评估操作共享资源的时间不准确导致的。

例如，操作共享资源的时间「最慢」可能需要 15s，而我们却只设置了 10s 过期，那这就存在锁提前过期的风险。
过期时间太短，那增大冗余时间，例如设置过期时间为 20s，这样总可以了吧？ 这样确实可以「缓解」这个问题，降低出问题的概率，但依旧无法「彻底解决」问题。
为什么？ 原因在于，客户端在拿到锁之后，在操作共享资源时，遇到的场景有可能是很复杂的，例如，程序内部发生异常、网络请求超时等等。
既然是「预估」时间，也只能是大致计算，除非你能预料并覆盖到所有导致耗时变长的场景，但这其实很难。 有什么更好的解决方案吗？ 别急，关于这个问题，我会在后面详细来讲对应的解决方案。我们继续来看第二个问题。

> 2.第二个问题在于，一个客户端释放了其它客户端持有的锁。

想一下，导致这个问题的关键点在哪？重点在于，每个客户端在释放锁时，都是「无脑」操作，并没有检查这把锁是否还「归自己持有」，所以就会发生释放别人锁的风险，这样的解锁流程，很不「严谨」！
如何解决这个问题呢？解决办法是：客户端在加锁时，设置一个只有自己知道的「唯一标识」进去。 例如，可以是自己的线程 ID，也可以是一个 UUID（随机且唯一），这里我们以 UUID 举例：
```
// 锁的VALUE设置为UUID,这里假设 20s 操作共享时间完全足够，先不考虑锁自动过期的问题。
127.0.0.1:6379> SET lock $uuid EX 20 NX
OK
```
之后，在释放锁时，要先判断这把锁是否还归自己持有，伪代码可以这么写：
```
// 锁是自己的，才释放
if redis.get("lock") == $uuid:
    redis.del("lock")
```

这里释放锁使用的是 GET + DEL 两条命令，这时，又会遇到我们前面讲的原子性问题了。

> 1.客户端 1 执行 GET，判断锁是自己的
> 2.客户端 2 执行了 SET 命令，强制获取到锁（虽然发生概率比较低，但我们需要严谨地考虑锁的安全性模型）
> 3.客户端 1 执行 DEL，却释放了客户端 2 的锁

由此可见，这两个命令还是必须要原子执行才行。 怎样原子执行呢？Lua 脚本。
我们可以把这个逻辑，写成 Lua 脚本，让 Redis 来执行。
因为 Redis 处理每一个请求是「单线程」执行的，在执行一个 Lua 脚本时，其它请求必须等待，直到这个 Lua 脚本处理完成，这样一来，GET + DEL 之间就不会插入其它命令了。
安全释放锁的 Lua 脚本如下：
```lua
// 判断锁是自己的，才释放
if redis.call("GET",KEYS[1]) == ARGV[1]
then
    return redis.call("DEL",KEYS[1])
else
    return 0
end
```
好了，这样一路优化，整个的加锁、解锁的流程就更「严谨」了。
这里我们先小结一下，基于 Redis 实现的分布式锁，一个严谨的的流程如下：

> 1.加锁：SET lock_key $unique_id EX $expire_time NX
> 2.操作共享资源
> 3.释放锁：Lua 脚本，先 GET 判断锁是否归属自己，再 DEL 释放锁

见图：[set命令+lua脚本删除实现分布式锁流程.png]

### 3.王者方案使用 redisson 提供的分布式锁解决预估失效时间问题

前面我们提到，锁的过期时间如果评估不好，这个锁就会有「提前」过期的风险。 当时给的妥协方案是，尽量「冗余」过期时间，降低锁提前过期的概率。 这个方案其实也不能完美解决问题，那怎么办呢？

是否可以设计这样的方案：加锁时，先设置一个过期时间，然后我们开启一个「守护线程」，定时去检测这个锁的失效时间，如果锁快要过期了，操作共享资源还未完成，那么就自动对锁进行「续期」，重新设置过期时间。
这确实一种比较好的方案。
如果你是 Java 技术栈，幸运的是，已经有一个库把这些工作都封装好了：Redisson。
Redisson 是一个 Java 语言实现的 Redis SDK 客户端，在使用分布式锁时，它就采用了「自动续期」的方案来避免锁过期，这个守护线程我们一般也把它叫做「看门狗」线程。
图见：[redisson自动续期原理图.png]

除此之外，这个 SDK 还封装了很多易用的功能：

> 可重入锁
> 乐观锁
> 公平锁
> 读写锁
> 红锁（RedLock）

这个 SDK 提供的 API 非常友好，它可以像操作本地锁的方式，操作分布式锁。如果你是 Java 技术栈，可以直接把它用起来。

到这里我们再小结一下，基于 Redis 的实现分布式锁，前面遇到的问题，以及对应的解决方案：
> 1.死锁：设置过期时间
> 2.过期时间评估不好，锁提前过期：守护线程，自动续期
> 3.锁被别人释放：锁写入唯一标识，释放锁先检查标识，再释放

# 什么是SpEL

SpEL(Spring Expression Language)，即Spring表达式语言。它与JSP2的EL表达式功能类似，可以在运行时查询和操作对象。

与JSP2的EL相比，SpEL功能更加强大，它甚至支持方法调用和基本字符串模板函数。

SpEL可以独立于Spring容器使用——只是当成简单的表达式语言来使用；也可以在Annotation或XML配置中使用SpEL，这样可以充分利用SpEL简化Spring的Bean配置。

# 为什么使用SpEL

自定义注解的属性声明，一般只能使用字符串，但是如果需要使用方法的入参作为注解的值，我们就需要使用SpEL表达式。

当然要实现这种目的还有很多方法，只是使用SpEL是最简单和灵活的。

# @Cacheable key的使用

key属性是用来指定Spring缓存方法的返回结果时对应的key的。该属性支持SpringEL表达式。当我们没有指定该属性时，Spring将使用默认策略生成key。我们这里先来看看自定义策略，至于默认策略会在后文单独介绍。

自定义策略是指我们可以通过Spring的EL表达式来指定我们的key。这里的EL表达式可以使用方法参数及它们对应的属性。使用方法参数时我们可以直接使用“#参数名”或者“#p参数index”。下面是几个使用参数作为key的示例。

```
@Cacheable(value="users", key="#id")
public User find(Integer id) {
  returnnull;

}

@Cacheable(value="users", key="#p0")
public User find(Integer id) {
  returnnull;

}

@Cacheable(value="users", key="#user.id")
public User find(User user) {
  returnnull;

}

@Cacheable(value="users", key="#p0.id")
public User find(User user) {
  returnnull;

}
```

除了上述使用方法参数作为key之外，Spring还为我们提供了一个root对象可以用来生成key。通过该root对象我们可以获取到以下信息。

![](img/spel.png)

当我们要使用root对象的属性作为key时我们也可以将“#root”省略，因为Spring默认使用的就是root对象的属性。如：

```
@Cacheable(value={"users", "xxx"}, key="caches[1].name")
public User find(User user) {
  returnnull;

}
```

如果要调用当前类里面的方法

```
@Override
@Cacheable(value={"TeacherAnalysis_public_chart"}, key="#root.target.getDictTableName() + '_' + #root.target.getFieldName()")
public List<Map<String, Object>> getChartList(Map<String, Object> paramMap) {
}
public String getDictTableName(){
    return "";
}
public String getFieldName(){
    return "";
}
```

## 使用分布式锁需要注意的点

### 看门狗

它是干啥用的呢？

好的，如果你回答不上来这个问题。那当你遇到下面这个面试题的时候肯定懵逼。

面试官：请问你用 Redis 做分布式锁的时候，如果指定过期时间到了，把锁给释放了。但是任务还未执行完成，导致任务再次被执行，这种情况你会怎么处理呢？

这个时候，99% 的面试官想得到的回答都是看门狗，或者一种类似于看门狗的机制。

如果你说：这个问题我遇到过，但是我就是把过期时间设置的长一点。

时间到底设置多长，是你一个非常主观的判断，设置的长一点，能一定程度上解决这个问题，但是不能完全解决。

所以，请回去等通知吧。

或者你回答：这个问题我遇到过，我不设置过期时间，由程序调用 unlock 来保证。

好的，程序保证调用 unlock 方法没毛病，这是在程序层面可控、可保证的。但是如果你程序运行的服务器刚好还没来得及执行 unlock 就宕机了呢，这个你不能打包票吧？

这个锁是不是就死锁了？

所以，请回去等通知吧。

为了解决前面提到的过期时间不好设置，以及一不小心死锁的问题，Redisson 内部基于时间轮，针对每一个锁都搞了一个定时任务，这个定时任务，就是看门狗。

在 Redisson 实例被关闭前，这个狗子可以通过定时任务不断的延长锁的有效期。

因为你根本就不需要设置过期时间，这样就从根本上解决了“过期时间不好设置”的问题。默认情况下，看门狗的检查锁的超时时间是 30 秒钟，也可以通过修改参数来另行指定。

如果很不幸，节点宕机了导致没有执行 unlock，那么在默认的配置下最长 30s 的时间后，这个锁就自动释放了。

那么问题来了，面试官紧接着来一个追问：怎么自动释放呢？

这个时候，你只需要来一个战术后仰：程序都没了，你觉得定时任务还在吗？定时任务都不在了，定时任务不在了就不会给锁自动续期了，锁就会在一定时间后失效，所以也不会存在死锁的问题。

### 看门狗什么情况下才会工作

分析源码一看便知（RedissonLock）

```
private RFuture<Boolean> tryAcquireOnceAsync(long waitTime, long leaseTime, TimeUnit unit, long threadId) {
    RFuture<Boolean> ttlRemainingFuture;
    if (leaseTime != -1) {
        ttlRemainingFuture = tryLockInnerAsync(waitTime, leaseTime, unit, threadId, RedisCommands.EVAL_NULL_BOOLEAN);
    } else {
        ttlRemainingFuture = tryLockInnerAsync(waitTime, internalLockLeaseTime,
                TimeUnit.MILLISECONDS, threadId, RedisCommands.EVAL_NULL_BOOLEAN);
    }

    ttlRemainingFuture.onComplete((ttlRemaining, e) -> {
        if (e != null) {
            return;
        }

        // lock acquired
        if (ttlRemaining) {
            if (leaseTime != -1) {
                internalLockLeaseTime = unit.toMillis(leaseTime);
            } else {
                scheduleExpirationRenewal(threadId);
            }
        }
    });
    return ttlRemainingFuture;
}
```

这里的 leaseTime 是 -1，所以触发的是 else 分支中的 scheduleExpirationRenewal 代码。

而这个代码就是启动看门狗的代码。换句话说，如果这里的 leaseTime 不是 -1，那么就不会启动看门狗。

那么怎么让 leaseTime 不是 -1 呢？自己指定加锁时间为其他值，且不是 -1.

### 看门狗工作原理

首先，我们知道看门狗其实就是一个定时任务，接下来我们来扒扒源码

```
protected void scheduleExpirationRenewal(long threadId) {
    ExpirationEntry entry = new ExpirationEntry();
    ExpirationEntry oldEntry = EXPIRATION_RENEWAL_MAP.putIfAbsent(getEntryName(), entry);
    // 重入加锁
    if (oldEntry != null) {
        oldEntry.addThreadId(threadId);
    } else { // 第一次加锁，触发定时任务
        entry.addThreadId(threadId);
        renewExpiration();
    }
}
```

里面就是把当前线程封装成了一个对象，然后维护到一个 MAP 中。

你只要记住这个 MAP 的 key 是当前线程，value 是 ExpirationEntry 对象，这个对象维护的是当前线程的加锁次数。

然后，我们先看 scheduleExpirationRenewal 方法里面，调用 MAP 的 putIfAbsent 方法后，返回的 oldEntry 不为空的情况。

这种情况说明是第一次加锁，会触发 renewExpiration 方法，这个方法里面就是看门狗的核心逻辑。

而在 scheduleExpirationRenewal 方法里面，不管前面提到的 oldEntry 是否为空，都会触发 addThreadId 方法：

从源码中可以看出来，这里仅仅对当前线程的加锁次数进行一个维护。

这个维护很好理解，因为要支持锁的重入嘛，就得记录到底重入了几次。

加锁一次，次数加一。解锁一次，次数减一。

接着看 renewExpiration 方法，这就是看门狗的真面目了：

```
private void renewExpiration() {
    ExpirationEntry ee = EXPIRATION_RENEWAL_MAP.get(getEntryName());
    if (ee == null) {
        return;
    }
    
    Timeout task = commandExecutor.getConnectionManager().newTimeout(new TimerTask() {
        @Override
        public void run(Timeout timeout) throws Exception {
            ExpirationEntry ent = EXPIRATION_RENEWAL_MAP.get(getEntryName());
            if (ent == null) {
                return;
            }
            Long threadId = ent.getFirstThreadId();
            if (threadId == null) {
                return;
            }
            
            RFuture<Boolean> future = renewExpirationAsync(threadId);
            future.onComplete((res, e) -> {
                if (e != null) {
                    log.error("Can't update lock " + getName() + " expiration", e);
                    EXPIRATION_RENEWAL_MAP.remove(getEntryName());
                    return;
                }
                
                if (res) {
                    // reschedule itself
                    renewExpiration();
                }
            });
        }
    }, internalLockLeaseTime / 3, TimeUnit.MILLISECONDS);
    
    ee.setTimeout(task);
}
```

这段代码主要逻辑是一个基于时间轮的定时任务
