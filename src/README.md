# 项目结构

### 1. 项目整体架构
这是一个基于 Spring Boot 的后端项目，采用了经典的分层架构设计。项目的主要结构如下：

```
src/main/java/com/example/learndemo/
├── LearnDemoApplication.java    # 应用程序入口类
├── config/                      # 配置类目录
├── controller/                  # 控制器层
├── service/                     # 服务层
├── mapper/                      # 数据访问层
├── domain/                      # 实体类
├── dto/                         # 数据传输对象
├── common/                      # 公共组件
├── security/                    # 安全相关
├── util/                        # 工具类
└── impl/                        # 接口实现类
```

### 2. 各层详细说明

#### 2.1 入口类 (LearnDemoApplication.java)
这是整个 Spring Boot 应用的启动入口，包含 `main` 方法。它使用 `@SpringBootApplication` 注解标记，这是 Spring Boot 的核心注解，它包含了：
- `@Configuration`：标记这是一个配置类
- `@EnableAutoConfiguration`：启用 Spring Boot 的自动配置机制
- `@ComponentScan`：自动扫描当前包及其子包下的所有组件

#### 2.2 配置层 (config/)
- 存放各种配置类
- 通常包含：
    - 数据库配置
    - 安全配置
    - 跨域配置
    - 其他框架配置

#### 2.3 控制器层 (controller/)
- 处理 HTTP 请求的入口
- 使用 `@RestController` 或 `@Controller` 注解
- 负责：
    - 接收前端请求
    - 参数校验
    - 调用服务层
    - 返回响应结果

#### 2.4 服务层 (service/)
- 包含业务逻辑
- 使用 `@Service` 注解
- 主要职责：
    - 实现具体的业务逻辑
    - 事务管理
    - 调用数据访问层

#### 2.5 数据访问层 (mapper/)
- 负责与数据库交互
- 使用 MyBatis 的 `@Mapper` 注解
- 包含 SQL 映射文件
- 主要职责：
    - 执行数据库操作
    - 定义 SQL 语句
    - 处理数据持久化

#### 2.6 实体层 (domain/)
- 存放数据库实体类
- 对应数据库表结构
- 使用 JPA 注解或 MyBatis 注解
- 包含：
    - 实体类属性
    - 表关系映射
    - 字段约束

#### 2.7 数据传输对象 (dto/)
- 用于层间数据传输
- 包含：
    - 请求对象（Request DTO）
    - 响应对象（Response DTO）
- 用于：
    - 数据封装
    - 参数传递
    - 结果返回

#### 2.8 公共组件 (common/)
- 存放公共代码
- 包含：
    - 常量定义
    - 公共工具类
    - 通用异常处理
    - 统一响应对象

#### 2.9 安全相关 (security/)
- 处理认证授权
- 包含：
    - 安全配置
    - 用户认证
    - 权限控制
    - JWT 相关

#### 2.10 工具类 (util/)
- 存放各种工具类
- 包含：
    - 字符串处理
    - 日期处理
    - 加密解密
    - 其他通用工具

#### 2.11 接口实现 (impl/)
- 存放接口的具体实现类
- 实现 service 层定义的接口
- 包含具体的业务逻辑实现

### 3. 数据流转过程
1. 客户端发送请求 → Controller 层
2. Controller 层接收请求，进行参数校验
3. 调用 Service 层处理业务逻辑
4. Service 层调用 Mapper 层访问数据库
5. Mapper 层执行数据库操作
6. 数据通过各层返回给客户端

### 4. 开发建议
1. 遵循分层原则，各层之间通过接口通信
2. 使用统一的异常处理机制
3. 做好日志记录
4. 注意代码复用，将公共代码抽取到 common 包
5. 合理使用设计模式
6. 保持代码整洁，做好注释

这个项目结构清晰，遵循了 Spring Boot 的最佳实践，适合作为学习 Spring Boot 的参考项目。每一层都有其明确的职责，便于维护和扩展。



--------------------------------------------------



### 1. 配置类
#### 1.1 CorsConfig.java
- 作用：配置跨域资源共享（CORS）
- 主要功能：
    - 允许来自 localhost 的跨域请求
    - 配置允许的 HTTP 方法、请求头
    - 设置凭证支持和缓存时间

### 2. 工具类
#### 2.1 JwtUtil.java
- 作用：JWT（JSON Web Token）工具类
- 主要功能：
    - 生成 JWT token
    - 验证 token 有效性
    - 从 token 中提取用户信息
    - 检查 token 是否过期

### 3. 数据传输对象（DTO）
#### 3.1 BookDto.java
- 作用：图书数据传输对象
- 包含字段：
    - id：图书ID
    - title：书名
    - author：作者
    - category：分类
    - publisher：出版社
    - published_year：出版年份
    - isbn：国际标准书号
    - total_copies：总副本数
    - available_copies：可用副本数
    - status：状态

#### 3.2 LoginRequestDto.java
- 作用：登录请求数据传输对象
- 包含字段：
    - username：用户名
    - password：密码

#### 3.3 LoginResponseDto.java
- 作用：登录响应数据传输对象
- 包含字段：
    - token：JWT token

#### 3.4 BookStatsDTO.java
- 作用：图书统计数据传输对象
- 包含字段：
    - bookId：图书ID
    - bookTitle：书名
    - author：作者
    - genre：类型
    - borrowCount：借阅次数

#### 3.5 PointsSummaryDto.java
- 作用：积分汇总数据传输对象
- 包含字段：
    - userId：用户ID
    - totalPoints：总积分
    - rank：排名
    - nextLevelPoints：下一级所需积分
    - borrowedBooks：已借图书数
    - returnedOnTime：按时归还数
    - overdueBooks：逾期图书数

### 4. 实体类（Domain）
#### 4.1 Book.java
- 作用：图书实体类
- 主要字段：
    - bookId：图书ID
    - title：书名
    - author：作者
    - category：分类
    - publisher：出版社
    - publishYear：出版年份
    - isbn：国际标准书号
    - totalCopies：总副本数
    - availableCopies：可用副本数
    - status：状态
    - createdAt：创建时间
    - updatedAt：更新时间

#### 4.2 Members.java
- 作用：会员实体类
- 主要字段：
    - memberId：会员ID
    - userId：用户ID
    - maxBooks：最大借书数
    - freeDays：免费天数
    - deposit：押金
    - rentPay：租金
    - overdueFine：逾期罚款

#### 4.3 UserPoints.java
- 作用：用户积分实体类
- 主要字段：
    - id：ID
    - username：用户名
    - points：积分
    - stars：星级
    - version：版本号

#### 4.4 FinancialRecords.java
- 作用：财务记录实体类
- 主要字段：
    - id：记录ID
    - username：用户名
    - type：类型
    - amount：金额
    - operation：操作
    - description：描述
    - createTime：创建时间

### 5. 服务接口
#### 5.1 PointsService.java
- 作用：积分服务接口
- 主要方法：
    - getUserPoints：获取用户积分
    - calculatePointsSummary：计算积分汇总
    - updateUserPoints：更新用户积分

#### 5.2 FinService.java
- 作用：财务服务接口
- 主要方法：
    - depositOperation：存款操作
    - prepaymentOperation：预付款操作
    - fineOperation：罚款操作
    - getUserRecords：获取用户记录
    - getRecordsByType：按类型获取记录
    - getUserPrepaymentBalance：获取用户预付款余额

### 6. 控制器
#### 6.1 PointsController.java
- 作用：积分控制器
- 主要接口：
    - GET /api/points/my：获取我的积分
    - GET /api/points/summary：获取积分汇总
    - POST /api/points/update：更新积分

### 7. 公共组件
#### 7.1 LibraryException.java
- 作用：自定义异常类
- 主要字段：
    - code：错误码
    - message：错误信息

#### 7.2 PlainResult.java
- 作用：统一响应结果类
- 主要字段：
    - code：状态码
    - message：消息
    - data：数据
- 主要方法：
    - success：成功响应
    - error：错误响应

### 8. 应用程序入口
#### 8.1 LearnDemoApplication.java
- 作用：Spring Boot 应用程序入口类
- 主要功能：
    - 启动 Spring Boot 应用
    - 配置自动扫描
    - 启用自动配置

这个项目是一个图书管理系统，包含了用户管理、图书管理、积分管理、财务管理等功能模块。采用了标准的三层架构（控制器层、服务层、数据访问层），并使用了 DTO 模式进行数据传输，实现了良好的代码组织和功能分离。