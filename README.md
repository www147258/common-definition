# common-definition

`common-definition` 是一个基础定义模块，用于沉淀项目中的公共对象、公共响应结构、分页结构、数据库基础实体、异常定义和常用工具类。

该模块只使用 JDK 标准库，不引入任何第三方依赖，方便被其他业务模块直接引用。

## 环境要求

- JDK 21
- Maven 编译配置使用 `maven.compiler.release=21`

## 模块内容

### 公共响应

包路径：`com.weiwei.wang.common.domain.response`

- `CommonResponse<T>`：统一接口响应对象，包含 `code`、`message`、`data`。
- `PageResponse<T>`：统一分页响应对象，包含 `totalRecords`、`rows`、`pageIndex`、`pageSize`。

示例：

```java
CommonResponse<String> response = CommonResponse.success("success");
CommonResponse<Void> fail = CommonResponse.fail(ResponseCodeAndMessageEnum.SYSTEM_ERROR);
```

### 公共请求

包路径：`com.weiwei.wang.common.domain.request`

- `PageRequest`：分页请求对象，包含 `pageIndex`、`pageSize`。

### 数据库基础实体

包路径：`com.weiwei.wang.common.domain.entity`

- `BaseEntity`：数据库基础实体，包含主键、创建人、创建时间、更新人、更新时间等字段。

### 异常定义

包路径：`com.weiwei.wang.common.exception`

- `ExceptionResponse`：异常响应接口，定义 `getCode()` 和 `getMessage()`。
- `BusinessException`：业务异常，携带业务错误码和错误消息。

示例：

```java
throw new BusinessException(ResponseCodeAndMessageEnum.PARAM_ERROR);
```

### 响应码枚举

包路径：`com.weiwei.wang.common.enums`

- `ResponseCodeAndMessageEnum`：公共响应码和响应消息定义。

当前内置：

- `SUCCESS`：操作成功
- `FAIL`：操作失败
- `PARAM_ERROR`：请求参数错误
- `UNAUTHORIZED`：未认证
- `FORBIDDEN`：无权限
- `NOT_FOUND`：资源不存在
- `SYSTEM_ERROR`：系统异常

### 常量定义

包路径：`com.weiwei.wang.common.constant`

- `NumberConstant`：常用数字常量。
- `CharacterConstant`：常用字符常量。

### 工具类

包路径：`com.weiwei.wang.common.util`

#### SnowflakeIdUtil

雪花算法 ID 生成工具。

特点：

- 支持 JDK 21。
- 默认从当前机器 IPv4 推导 `workerId` 和 `dataCenterId`。
- 支持传入 `conflictFactor` 冲突因子，用于同一 IP 下多个应用实例区分。

示例：

```java
SnowflakeIdUtil snowflakeIdUtil = SnowflakeIdUtil.createDefault(1);
long id = snowflakeIdUtil.nextId();
long workId = snowflakeIdUtil.getWorkId();
long dataId = snowflakeIdUtil.getDataId();
String localIp = SnowflakeIdUtil.getLocalIp();
```

#### AesUtil

AES 对称加密工具。

特点：

- 使用 `AES/GCM/NoPadding`。
- 密钥使用 Base64 字符串传递。
- 加密结果包含随机 IV，并整体返回 Base64 字符串。

示例：

```java
String key = AesUtil.generateBase64Key();
String cipherText = AesUtil.encrypt("hello", key);
String plainText = AesUtil.decrypt(cipherText, key);
```

#### RsaUtil

RSA 非对称加密和签名工具。

特点：

- 默认生成 2048 位 RSA 密钥对。
- 加密使用 `RSA/ECB/OAEPWithSHA-256AndMGF1Padding`。
- 签名使用 `SHA256withRSA`。
- 公钥和私钥均使用 Base64 字符串传递。

示例：

```java
RsaUtil.RsaKeyPair keyPair = RsaUtil.generateKeyPair();
String cipherText = RsaUtil.encryptByPublicKey("hello", keyPair.publicKey());
String plainText = RsaUtil.decryptByPrivateKey(cipherText, keyPair.privateKey());

String signature = RsaUtil.sign("hello", keyPair.privateKey());
boolean verified = RsaUtil.verify("hello", signature, keyPair.publicKey());
```

#### Md5SignUtil

MD5 签名工具。

特点：

- 支持字符串 MD5。
- 支持字符串加密钥签名。
- 支持 Map 参数按 key 排序后签名。
- Map 签名时会忽略 `null` key、`null` value 和 `sign` 字段。

示例：

```java
String md5 = Md5SignUtil.md5("hello");
String sign = Md5SignUtil.sign("hello", "secret");
boolean verified = Md5SignUtil.verify("hello", "secret", sign);
```

## 依赖约束

该模块定位为基础模块，应保持轻量和稳定：

- 不引入第三方依赖。
- 不依赖 Web、数据库、缓存、消息队列等具体技术栈。
- 不放业务逻辑。
- 只定义跨模块可复用的公共对象、异常、枚举、常量和工具类。

如需新增内容，优先保证 API 简洁、职责单一，并避免影响已有业务模块。
