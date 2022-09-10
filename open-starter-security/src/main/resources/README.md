# 安全插件

认证、鉴权拦截插件

## 功能

- [x] 配置路由白名单功能，白名单内的路由不会被拦截，即请求头不需要携带 token 就能访问

- [x] 默认提供基于注解进行权限拦截校验，如果方法上加了 PreAuthorization 注解则会进行权限校验，如果有相应权限才能通过，否则将被拒绝访问

- [x] 开发者无感知获取用户信息

## 快速开始

### 1. 添加 Maven 依赖

```xml
<dependency>
    <groupId>com.saucesubfresh</groupId>
    <artifactId>open-starter-security</artifactId>
    <version>1.0.2</version>
</dependency>
```

### 2. 启用安全拦截插件

启用 EnableSecurity 注解后会注入我们的拦截器
```java
@EnableSecurity
@EnableLightRpcServer
@SpringBootApplication
public class JobAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(JobAdminApplication.class, args);
    }
}
```

### 3. 配置白名单及其他参数

```yaml
com:
  saucesubfresh:
    security:
      # 配置白名单
      ignore-paths: "/login"
      # 配置 jwt token 的 secret-key， 须注意长度，且要与 open-starter-oauth 配置的一致
      secret-key: "ThisIsKeyThisIsKeyThisIsKeyThisIsKeyThisIsKeyThisIsKey"
```

### 4. 获取当前用户的用户信息

```java
@Service
public class OpenJobServiceImpl extends ServiceImpl<OpenJobMapper, OpenJobDO> implements OpenJobService {

    private final ScheduleTaskExecutor scheduleTaskExecutor;
    private final ScheduleTaskManage scheduleTaskManage;
    private final OpenJobMapper openJobMapper;

    public OpenJobServiceImpl(ScheduleTaskExecutor scheduleTaskExecutor, ScheduleTaskManage scheduleTaskManage, OpenJobMapper openJobMapper) {
        this.scheduleTaskExecutor = scheduleTaskExecutor;
        this.scheduleTaskManage = scheduleTaskManage;
        this.openJobMapper = openJobMapper;
    }

    @Override
    public boolean save(OpenJobCreateDTO openJobCreateDTO) {
        String cronExpression = openJobCreateDTO.getCronExpression();
        if (!CronExpression.isValidExpression(cronExpression)){
            throw new ServiceException("Invalid cronExpression");
        }
        openJobCreateDTO.setCreateTime(LocalDateTime.now());
        // UserSecurityContextHolder.getUserId() 获取当前用户 userId
        openJobCreateDTO.setCreateUser(UserSecurityContextHolder.getUserId());
        int insert = openJobMapper.insert(OpenJobConvert.INSTANCE.convert(openJobCreateDTO));
        return insert != 0;
    }
}

```

### 高阶使用-用户鉴权功能

常见的权限访问控制做法有如下两种

#### 1、路径比对

一般的系统在权限设计上，都会分为角色、权限（RBAC），复杂一点的可能会有用户组、组织之类的概念。

用户的权限是写死的，对应于后台的接口或者资源，是没办法改变的，一般不对用户开放修改权限。

管理员用户可以通过给角色分配权限的方式，来实现访问控制。

即分析当前访问路径所需要的权限，检查当前用户是否具有该权限，做一个对比，根据对比结果来决定当前用户是否可以访问该资源。

这种做法的好处是代码的入侵性不高，不需要再每个接口上加注解。但相对来说，显得不那么直观。

具体实现可以参考 Open-Admin

#### 2、使用注解的方式

有些小型的系统或许压根就不需要权限，只需要给用户分配角色，没有给角色分配权限这一过程。这样的话，角色也是不可变的，就可以根据角色来做访问控制了。

在需要某个角色才能访问的方法上添加 PreAuthorization 注解，在该 starter 中已提供该实现。

参考 Open-Crawler

```java
@Validated
@RestController
@RequestMapping("/user")
public class CrawlerUserController {

  @Autowired
  private CrawlerUserService crawlerUserService;

  @GetMapping("/currentUser")
  public Result<CrawlerUserRespDTO> getCurrentUser() {
    Long userId = UserSecurityContextHolder.getUserId();
    return Result.succeed(crawlerUserService.loadUserByUserId(userId));
  }

  @GetMapping("/page")
  public Result<PageResult<CrawlerUserRespDTO>> page(CrawlerUserReqDTO crawlerUserReqDTO) {
    return Result.succeed(crawlerUserService.selectPage(crawlerUserReqDTO));
  }

  @GetMapping("/info/{id}")
  public Result<CrawlerUserRespDTO> info(@PathVariable("id") Long id) {
    return Result.succeed(crawlerUserService.getById(id));
  }

  @PostMapping("/save")
  @PreAuthorization(value = "admin")
  public Result<Boolean> save(@RequestBody @Valid CrawlerUserCreateDTO crawlerUserCreateDTO) {
    return Result.succeed(crawlerUserService.save(crawlerUserCreateDTO));
  }

  @PutMapping("/update")
  @PreAuthorization(value = "admin")
  public Result<Boolean> update(@RequestBody @Valid CrawlerUserUpdateDTO crawlerUserUpdateDTO) {
    return Result.succeed(crawlerUserService.updateById(crawlerUserUpdateDTO));
  }

  @DeleteMapping("/delete")
  @PreAuthorization(value = "admin")
  public Result<Boolean> delete(@RequestBody @Valid DeleteDTO deleteDTO) {
    return Result.succeed(crawlerUserService.deleteById(deleteDTO));
  }

}
```

注意：

与 HandlerInterceptor 搭配使用的注解只能用在 Controller 中, 也就是说我们的自定义注解 PreAuthorization 只能用在 Controller 中

## 1.0.1 版本更新说明

1. 对拦截器拦截方法进行了重大重构，拆分出了 401（认证） 处理策略和 403（授权） 处理策略
2. 认证、授权接口支持自定义
3. 优化了鉴权相关逻辑，提供简单的基于注解鉴权能力，同时给开发者预留了基于“路径比对”的接口能力

## 1.0.2 版本更新说明

1. 增加角色列表字段

## 1.0.3 版本更新说明

1. 修复了 DefaultAccessDeniedHandler#handler 方法逻辑判断 fix a bug with issues#6