# 实现原理说明

总的来说就是利用 TaskDecorator 和 动态代理提供的功能实现在方法执行前后添加拦截器，在方法执行成功或失败之后添加成功或失败处理函数。
具体来说就是利用 TaskDecorator 的 decorate 方法，传入一个 Runnable，并返回一个 Runnable 代理。Proxy.newProxyInstance 的第三个参数是
一个InvocationHandler对象（我们传入的是 TaskProxy 对象，因为它实现了 InvocationHandler 接口），表示的是当动态代理对象调用方法的时候（也就是调用 Runnable 代理对象的 run 方法时）
会关联到那一个InvocationHandler对象上（此时就是我们的 TaskProxy对象），并最终由其（TaskProxy）调用（先调用 InvocationHandler 的 invoke 方法，invoke 方法调用真正 Runnable 的 run 方法）.

也就是当我们通过动态代理对象调用一个方法时候，这个方法的调用就会被转发到实现了 InvocationHandler 接口类的invoke方法来调用
举例就是：当我们调用 Runnable 代理对象的 run 方法时就会调用 TaskProxy 对象的 invoke 方法（因为我们在创建 Runnable 代理的时候关联了 InvocationHandler 的实现类 TaskProxy）， invoke 方法被调用就会调用原 Runnable 的 run 方法。
执行流程就是：执行代理对象的方法就会执行代理对象关联的 InvocationHandler 对象中的  invoke 方法，执行 InvocationHandler 对象中的  invoke 方法就会调用真实对象的方法。
这也许就是 AOP 的实现原理。