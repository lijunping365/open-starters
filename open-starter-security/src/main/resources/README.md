# 安全插件

安全认证拦截插件

## 功能

- [x] 配置路由白名单功能，白名单内的路由不会被拦截，即请求头不需要携带 token 就能访问

- [x] 基于注解拦截进行权限校验，如果方法上加了 PreAuthorization 注解则会进行拦截并校验权限，如果有相应权限才能通过，否则将被拒绝访问

- [x] 开发者无感知获取用户信息

## 快速开始

### 1. 添加 Maven 依赖

```xml
<dependency>
    <groupId>com.lightcode</groupId>
    <artifactId>open-starter-security</artifactId>
    <version>1.0-SNAPSHOT</version>
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
  lightcode:
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

### 高级使用-用户权限拦截

通常在后台管理系统中会使用此功能

> 1.在需要某个角色才能访问的方法上添加 PreAuthorization 注解
> 2.需要注入 AuthorityService 的实现


