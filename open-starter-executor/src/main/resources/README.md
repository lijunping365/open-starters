# 前言

由于我们在实际项目中使用**线程池执行器**的情况比较多，所以这里我们提供一个线程池执行器。

# 该执行器具有的功能

向外提供的接口是 [@see TaskProcessor], 只需要向该接口传入需要执行的任务 [@see Task] 就可以了.

## 组件功能说明：

### 任务执行器：

[@see TaskExecutor]

执行任务，并在任务执行完成后自动关闭线程池


### 任务构建器：

[@see TaskBuilder]

构建任务，并代理当前任务，添加任务执行前后处理器，添加任务执行成功失败处理器，最终返回 Runnable。

### 任务执行前后拦截器：

[@see TaskBeforeInterceptor，TaskAfterInterceptor]

我们利用 TaskBeforeInterceptor #before 完成了任务的装饰，
我们利用 TaskAfterInterceptor #after 完成了任务执行后的处理
类似于 js 中的拦截器，有 requestInterceptor 和 responseInterceptor，提供钩子函数，供开发者自己实现。

### 任务执行成功失败处理器：

[@see TaskExecuteSuccessHandler，TaskExecuteFailureHandler]

我们利用 TaskExecuteSuccessHandler #onTaskExecuteSuccess 完成任务执行成功后的处理逻辑，提供钩子函数，供开发者自己实现。
我们利用 TaskExecuteFailureHandler #onTaskExecuteFailure 完成任务执行失败后的处理逻辑，提供钩子函数，供开发者自己实现。
类似于 oauth2 中的认证成功失败处理器，有 onAuthenticationSuccess 和 onAuthenticationFailure，提供钩子函数，供开发者自己实现。


# 执行流程说明：

（1）构建任务，设置任务执行前后拦截器，设置任务执行成功失败处理器。

（2）执行任务，executor::execute

# 使用方法说明：

以发送消息为例说明：
把要发送的消息列表传入到 TaskProcessor，定义线程池处理器，定义消息处理器 TaskHandler，任务执行前后拦截器和任务执行成功失败处理器按需求定义。

# 第一版实现原理：

总的来说就是利用 TaskDecorator 和 动态代理提供的功能实现在方法执行前后添加拦截器，在方法执行成功或失败之后添加成功或失败处理函数。
具体来说就是利用 TaskDecorator 的 decorate 方法，传入一个 Runnable，并返回一个 Runnable 代理。Proxy.newProxyInstance 的第三个参数是
一个InvocationHandler对象（我们传入的是 TaskProxy 对象，因为它实现了 InvocationHandler 接口），表示的是当动态代理对象调用方法的时候（也就是调用 Runnable 代理对象的 run 方法时）
会关联到那一个InvocationHandler对象上（此时就是我们的 TaskProxy对象），并最终由其（TaskProxy）调用（先调用 InvocationHandler 的 invoke 方法，invoke 方法调用真正 Runnable 的 run 方法）.

也就是当我们通过动态代理对象调用一个方法时候，这个方法的调用就会被转发到实现了 InvocationHandler 接口类的invoke方法来调用
举例就是：当我们调用 Runnable 代理对象的 run 方法时就会调用 TaskProxy 对象的 invoke 方法（因为我们在创建 Runnable 代理的时候关联了 InvocationHandler 的实现类 TaskProxy）， invoke 方法被调用就会调用原 Runnable 的 run 方法。
执行流程就是：执行代理对象的方法就会执行代理对象关联的 InvocationHandler 对象中的  invoke 方法，执行 InvocationHandler 对象中的  invoke 方法就会调用真实对象的方法。
这也许就是 AOP 的实现原理。
# 已实现功能

(1)动态调参：支持线程池参数动态调整、界面化操作；包括修改线程池核心大小、最大核心大小、队列长度等；参数修改后及时生效。
单机版容易实现，集群版写过已删除，较为麻烦。

(2)线程池、队列、拒绝策略支持自定义

# 后续优化点

(3)任务监控：支持应用粒度、线程池粒度、任务粒度的Transaction监控；可以看到线程池的任务执行情况、最大任务执行时间、平均任务执行时间。

(4)队列支持自动扩容，默认队列大小，扩容因子，但是自动扩容需要重点注意一点，要考虑到 cpu 使用率，不能超过 cpu 使用率的 80%；