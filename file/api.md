好的，已将 HTML 文档完整转换为 Markdown 格式，保留了所有接口信息、表格、代码示例和层级结构，便于阅读和维护。

---

# 知识产权专利管理系统 — API 接口文档

**Base URL:** `http://localhost:8080`  
**版本:** 1.0  
**共 55 个接口，6 个业务模块，5 个 Excel 导出功能**

---

## 1. 项目概述

| 项目名称 | intellectual-property |
|---------|----------------------|
| 技术栈   | Spring Boot 4.1.0 + Java 17 + MyBatis |
| 接口文档 | Swagger UI: `/swagger-ui/index.html` &nbsp;\|&nbsp; OpenAPI: `/v3/api-docs` |
| 认证方式 | 无认证（开发阶段，所有接口允许匿名访问） |
| 跨域     | 允许所有来源（*） |

---

## 2. 通用说明

### 2.1 统一响应格式 `Result<T>`

除 **专利交底信息** 模块外，其余模块均使用统一响应包装：

```json
{
  "code": 200,
  "message": "success",
  "data": { ... }
}
```

| 字段      | 类型   | 说明                                      |
|-----------|--------|-------------------------------------------|
| `code`    | int    | 200 = 成功，500 = 失败                    |
| `message` | String | 提示信息，成功默认 "success"              |
| `data`    | T      | 响应数据，可为 null、对象、数组或 Map     |

### 2.2 日期格式

所有日期字段序列化为 `yyyy-MM-dd HH:mm:ss` 格式，时区 GMT+8。  
请求传入时同样使用该格式字符串或符合 ISO 8601 的日期字符串。

### 2.3 分页参数

| 参数名                    | 默认值 | 说明                                    |
|---------------------------|--------|-----------------------------------------|
| `page` / `pageNum`        | 1      | 页码，从 1 开始                         |
| `page_size` / `pageSize`  | 10/100 | 每页条数（交底模块默认10，其余默认100） |

> **注意：** 专利交底模块使用 `pageNum` + `pageSize`，其他模块使用 `page` + `page_size`。

### 2.4 查询参数说明

所有字段均为 **可选**。非空字段会作为 `=` 等值条件拼接；可通过 `orderBy` 字段指定排序（如 `"create_time DESC, id ASC"`，直接拼入 SQL，请确保输入来源可信）。

---

## 3. 专利交底信息（T表）

**Base URL:** `/patent-disclosure`  
**接口数：10**  
**响应格式：不使用 Result 包装，直接返回原始数据**

### 3.1 字段说明

| 字段名             | 类型      | 说明                                            |
|--------------------|-----------|-------------------------------------------------|
| `id`               | Long      | 主键ID（新增时为空，系统自增）                  |
| `tempNo`           | String    | 临时编号（如 T250101）                          |
| `internalNo`       | String    | 内部编号（与P表关联键）                         |
| `patentStatus`     | String    | 专利状态（如受理、N稿撰写中等）                  |
| `requirement`      | String    | 要求（如一周内提交、追求授权等）                 |
| `disclosureName`   | String    | 专利交底名称                                    |
| `applicant`        | String    | 申请人（多个用顿号或逗号分隔）                  |
| `invitedToGroup`   | Boolean   | 是否邀请进群（false-否，true-是）               |
| `contactPerson`    | String    | 联系人姓名                                      |
| `manager`          | String    | 管理人姓名                                      |
| `agent`            | String    | 代理人姓名                                      |
| `disclosureDate`   | Date      | 交底日（格式 yyyy-MM-dd HH:mm:ss）              |
| `disclosureDays`   | Integer   | 交底天数（可为空）                              |
| `remark`           | String    | 备注                                            |
| `contactInfo`      | String    | 联系人信息（含电话、邮箱、QQ等）                |
| `createTime`       | Date      | 创建时间（自动填充）                            |
| `updateTime`       | Date      | 更新时间（自动填充）                            |

### 3.2 接口列表

#### 3.2.1 新增一条交底记录
- **URL:** `POST /patent-disclosure`
- **Request Body:**
```json
{
  "tempNo": "T250101",
  "internalNo": "P2025001",
  "patentStatus": "受理",
  "disclosureName": "一种XXX方法",
  "applicant": "张三、李四",
  "invitedToGroup": true,
  "contactPerson": "张三",
  "manager": "王五",
  "agent": "赵六",
  "disclosureDate": "2025-01-15 10:30:00",
  "remark": "备注内容"
}
```
- **Response:** `1` （影响行数，int）

#### 3.2.2 新增或更新
- **URL:** `POST /patent-disclosure/insertOrUpdate`
- **Request Body:** 包含 `id` 则更新，否则插入
- **Response:** `1`

#### 3.2.3 根据主键删除
- **URL:** `DELETE /patent-disclosure/{id}`
- **Path Parameter:** `id` (Long)
- **Response:** `1`

#### 3.2.4 更新记录
- **URL:** `PUT /patent-disclosure`
- **Request Body:** 必须包含 `id`
- **Response:** `1`

#### 3.2.5 根据主键查询单条
- **URL:** `GET /patent-disclosure/{id}`
- **Response:**
```json
{
  "id": 1,
  "tempNo": "T250101",
  "internalNo": "P2025001",
  "patentStatus": "受理",
  "disclosureName": "一种XXX方法",
  "applicant": "张三、李四",
  "invitedToGroup": true,
  "contactPerson": "张三",
  "manager": "王五",
  "agent": "赵六",
  "disclosureDate": "2025-01-15 10:30:00",
  "remark": "备注内容",
  "createTime": "2025-01-15 10:30:00",
  "updateTime": "2025-01-20 14:00:00"
}
```

#### 3.2.6 查询所有记录（不分页）
- **URL:** `GET /patent-disclosure`
- **Response:** `[ {...}, {...} ]`

#### 3.2.7 分页查询
- **URL:** `GET /patent-disclosure/page`
- **Query Parameters:**
    - `pageNum` (int, 默认1)
    - `pageSize` (int, 默认10)
- **Response:**
```json
{
  "list": [ {...} ],
  "total": 100,
  "pageNum": 1,
  "pageSize": 10
}
```

#### 3.2.8 条件筛选列表
- **URL:** `POST /patent-disclosure/list`
- **Request Body:** 所有字段可选，支持 `orderBy`
```json
{
  "patentStatus": "受理",
  "applicant": "张三",
  "orderBy": "create_time DESC"
}
```
- **Response:** `[ {...} ]`

#### 3.2.9 自由 Map 参数动态查询
- **URL:** `POST /patent-disclosure/listByParams`
- **Request Body:** 任意键值对
- **Response:** `[ {...} ]`

#### 3.2.10 条件统计记录数
- **URL:** `POST /patent-disclosure/count`
- **Request Body:** 同 list 接口
- **Response:** `42` (int)

---

## 4. 中间著变专利

**Base URL:** `/patent-intermediate-changes`  
**接口数：9**  
**响应格式：Result 包装**  
**支持 Excel 导出**

### 4.1 字段说明

| 字段名             | 类型      | 说明                              |
|--------------------|-----------|-----------------------------------|
| `id`               | Long      | 主键ID                            |
| `seqNo`            | Integer   | 序号                              |
| `internalNo`       | String    | 内部编号                          |
| `businessType`     | String    | 业务类型（转让/转我所/著录变更等）|
| `applicationNo`    | String    | 申请号                            |
| `patentName`       | String    | 发明创造名称                      |
| `applicant`        | String    | 申请人                            |
| `inventor`         | String    | 发明人                            |
| `sponsor`          | String    | 主办人                            |
| `agent`            | String    | 委托书代理人                      |
| `applicationDate`  | Date      | 申请日                            |
| `status`           | String    | 状态                              |
| `issueDate`        | Date      | 发文日                            |
| `feeReductionInfo` | String    | 非正标-费减情况                   |
| `submitDate`       | Date      | 提交日期                          |
| `paymentDeadline`  | Date      | 缴费止期                          |
| `feeAmount`        | Double    | 费用金额                          |
| `paymentStatus`    | String    | 缴费状态                          |
| `remark1`          | String    | 备注1                             |
| `remark2`          | String    | 备注2                             |
| `createTime`       | Date      | 创建时间                          |
| `updateTime`       | Date      | 更新时间                          |

### 4.2 接口列表

| 方法 | 路径                                         | 说明                                    |
|------|----------------------------------------------|-----------------------------------------|
| POST | `/patent-intermediate-changes`               | 新增记录                                |
| POST | `/patent-intermediate-changes/insertOrUpdate`| 新增或更新（id 存在则更新）              |
| DELETE | `/patent-intermediate-changes/{id}`        | 根据主键删除                            |
| PUT  | `/patent-intermediate-changes/{id}`          | 根据主键更新（id 也从 path 设置）       |
| GET  | `/patent-intermediate-changes/{id}`          | 根据主键查询                            |
| GET  | `/patent-intermediate-changes`               | 分页查询（参数 `page`, `page_size`，默认100） |
| POST | `/patent-intermediate-changes/list`          | 条件查询（传入 PatentIntermediateChangeQuery） |
| POST | `/patent-intermediate-changes/listByParams`  | 自由 Map 参数动态查询                   |
| GET  | `/patent-intermediate-changes/export`        | 导出全部数据为 Excel（文件名"中间著变专利.xls"） |

> 查询字段（均可选）：`id`, `seqNo`, `internalNo`, `businessType`, `applicationNo`, `patentName`, `applicant`, `inventor`, `sponsor`, `agent`, `applicationDate`, `status`, `issueDate`, `feeReductionInfo`, `submitDate`, `paymentDeadline`, `feeAmount`, `paymentStatus`, `remark1`, `remark2`, `createTime`, `updateTime`, `orderBy`

---

## 5. 专利新申请

**Base URL:** `/patent-new-applications`  
**接口数：9**  
**响应格式：Result 包装**  
**支持 Excel 导出**

### 5.1 字段说明

| 字段名             | 类型      | 说明                                      |
|--------------------|-----------|-------------------------------------------|
| `id`               | Long      | 主键ID                                    |
| `seqNo`            | Integer   | 序号                                      |
| `internalNo`       | String    | 内部编号                                  |
| `patentName`       | String    | 发明创造名称                              |
| `applicationNo`    | String    | 申请号/专利号                             |
| `patentType`       | String    | 类型（发明/实用新型/外观设计）            |
| `applicant`        | String    | 申请人                                    |
| `inventor`         | String    | 发明人                                    |
| `sponsor`          | String    | 主办人                                    |
| `agent`            | String    | 委托书代理人                              |
| `applicationDate`  | Date      | 申请日                                    |
| `notification`     | String    | 通知书                                    |
| `issueDate`        | Date      | 发文日                                    |
| `preExamMark`      | String    | 非正标-预审标                             |
| `paymentDeadline`  | Date      | 缴费止期                                  |
| `feeAmount`        | Double    | 费用金额                                  |
| `paymentDate`      | String    | 缴费时间                                  |
| `dasCode`          | String    | DAS码                                     |
| `createTime`       | Date      | 创建时间                                  |
| `updateTime`       | Date      | 更新时间                                  |

### 5.2 接口列表

| 方法 | 路径                                       | 说明                                    |
|------|--------------------------------------------|-----------------------------------------|
| POST | `/patent-new-applications`                 | 新增记录                                |
| POST | `/patent-new-applications/insertOrUpdate`  | 新增或更新                              |
| DELETE | `/patent-new-applications/{id}`          | 根据主键删除                            |
| PUT  | `/patent-new-applications/{id}`            | 根据主键更新                            |
| GET  | `/patent-new-applications/{id}`            | 根据主键查询                            |
| GET  | `/patent-new-applications`                 | 分页查询（参数 `page`, `page_size`，默认100） |
| POST | `/patent-new-applications/list`            | 条件查询（传入 PatentNewApplicationQuery） |
| POST | `/patent-new-applications/listByParams`    | 自由 Map 参数动态查询                   |
| GET  | `/patent-new-applications/export`          | 导出全部数据为 Excel（文件名"专利新申请.xls"） |

> 查询字段（均可选）：`id`, `internalNo`, `patentName`, `applicationNo`, `applicant`, `inventor`, `sponsor`, `agent`, `applicationDate`, `notification`, `issueDate`, `preExamMark`, `paymentDeadline`, `feeAmount`, `paymentDate`, `seqNo`, `patentType`, `dasCode`, `createTime`, `updateTime`, `orderBy`

---

## 6. PCT国际申请

**Base URL:** `/patent-pcts`  
**接口数：9**  
**响应格式：Result 包装**  
**支持 Excel 导出**

### 6.1 字段说明

| 字段名                   | 类型      | 说明                        |
|--------------------------|-----------|-----------------------------|
| `id`                     | Long      | 主键ID                      |
| `seqNo`                  | Integer   | 序号                        |
| `pctInternalNo`          | String    | PCT内部编号                 |
| `status`                 | String    | 状态                        |
| `issueDate`              | Date      | 发文日                      |
| `priorInternalNo`        | String    | 在先内部编号                |
| `priorApplicationNo`     | String    | 在先申请号                  |
| `priorApplicationDate`   | Date      | 在先申请日                  |
| `pctApplicationDate`     | Date      | PCT申请日                   |
| `applicationName`        | String    | 申请名称                    |
| `applicationNo`          | String    | 申请号（PCT号）             |
| `applicant`              | String    | 申请人                      |
| `inventor`               | String    | 发明人                      |
| `sponsor`                | String    | 主办人                      |
| `agent`                  | String    | 委托书代理人                |
| `preliminaryConclusion`  | String    | 初检结论                    |
| `remark`                 | String    | 备注                        |
| `createTime`             | Date      | 创建时间                    |
| `updateTime`             | Date      | 更新时间                    |

### 6.2 接口列表

| 方法 | 路径                               | 说明                                    |
|------|------------------------------------|-----------------------------------------|
| POST | `/patent-pcts`                     | 新增记录                                |
| POST | `/patent-pcts/insertOrUpdate`      | 新增或更新                              |
| DELETE | `/patent-pcts/{id}`              | 根据主键删除                            |
| PUT  | `/patent-pcts/{id}`                | 根据主键更新                            |
| GET  | `/patent-pcts/{id}`                | 根据主键查询                            |
| GET  | `/patent-pcts`                     | 分页查询（参数 `page`, `page_size`，默认100） |
| POST | `/patent-pcts/list`                | 条件查询（传入 PatentPctQuery）         |
| POST | `/patent-pcts/listByParams`        | 自由 Map 参数动态查询                   |
| GET  | `/patent-pcts/export`              | 导出全部数据为 Excel（文件名"PCT国际申请.xls"） |

> 查询字段（均可选）：`id`, `seqNo`, `pctInternalNo`, `status`, `issueDate`, `priorInternalNo`, `priorApplicationNo`, `priorApplicationDate`, `pctApplicationDate`, `applicationName`, `applicationNo`, `applicant`, `inventor`, `sponsor`, `agent`, `preliminaryConclusion`, `remark`, `createTime`, `updateTime`, `orderBy`

---

## 7. 复审无效专利

**Base URL:** `/patent-reexaminations`  
**接口数：9**  
**响应格式：Result 包装**  
**支持 Excel 导出**

### 7.1 字段说明

| 字段名           | 类型      | 说明                                |
|------------------|-----------|-------------------------------------|
| `id`             | Long      | 主键ID                              |
| `seqNo`          | Integer   | 序号                                |
| `patentType`     | String    | 类型（发明/实用新型/外观设计）      |
| `category`       | String    | 分类（复审/无效）                   |
| `caseNo`         | String    | 案件编号                            |
| `internalNo`     | String    | 内部编号                            |
| `applicationNo`  | String    | 申请号                              |
| `patentName`     | String    | 专利名称                            |
| `applicant`      | String    | 申请人                              |
| `sponsor`        | String    | 主办人                              |
| `agent`          | String    | 委托书代理人                        |
| `notification`   | String    | 通知书                              |
| `issueDate`      | Date      | 发文日                              |
| `submitDate`     | Date      | 提交日期                            |
| `queryInfo`      | String    | 25.6.12查询                         |
| `officialFee`    | Double    | 规费                                |
| `paymentDate`    | String    | 缴费时间                            |
| `note1`          | String    | 附注1                               |
| `createTime`     | Date      | 创建时间                            |
| `updateTime`     | Date      | 更新时间                            |

### 7.2 接口列表

| 方法 | 路径                                   | 说明                                    |
|------|----------------------------------------|-----------------------------------------|
| POST | `/patent-reexaminations`               | 新增记录                                |
| POST | `/patent-reexaminations/insertOrUpdate`| 新增或更新                              |
| DELETE | `/patent-reexaminations/{id}`        | 根据主键删除                            |
| PUT  | `/patent-reexaminations/{id}`          | 根据主键更新                            |
| GET  | `/patent-reexaminations/{id}`          | 根据主键查询                            |
| GET  | `/patent-reexaminations`               | 分页查询（参数 `page`, `page_size`，默认100） |
| POST | `/patent-reexaminations/list`          | 条件查询（传入 PatentReexaminationQuery）|
| POST | `/patent-reexaminations/listByParams`  | 自由 Map 参数动态查询                   |
| GET  | `/patent-reexaminations/export`        | 导出全部数据为 Excel（文件名"复审无效专利.xls"） |

> 查询字段（均可选）：`id`, `seqNo`, `patentType`, `category`, `caseNo`, `internalNo`, `applicationNo`, `patentName`, `applicant`, `sponsor`, `agent`, `notification`, `issueDate`, `submitDate`, `queryInfo`, `officialFee`, `paymentDate`, `note1`, `createTime`, `updateTime`, `orderBy`

---

## 8. 补漏专利

**Base URL:** `/patent-supplementaries`  
**接口数：9**  
**响应格式：Result 包装**  
**支持 Excel 导出**

### 8.1 字段说明

| 字段名            | 类型      | 说明                        |
|-------------------|-----------|-----------------------------|
| `id`              | Long      | 主键ID                      |
| `seqNo`           | Integer   | 序号                        |
| `applicationNo`   | String    | 申请号/专利号               |
| `patentName`      | String    | 发明创造名称                |
| `applicant`       | String    | 申请人                      |
| `inventor`        | String    | 发明人                      |
| `sponsor`         | String    | 主办人                      |
| `agent`           | String    | 委托书代理人                |
| `applicationDate` | Date      | 申请日                      |
| `notification`    | String    | 通知书                      |
| `issueDate`       | Date      | 发文日                      |
| `feeReduction`    | String    | 费减                        |
| `remark`          | String    | 备注                        |
| `createTime`      | Date      | 创建时间                    |
| `updateTime`      | Date      | 更新时间                    |

### 8.2 接口列表

| 方法 | 路径                                     | 说明                                    |
|------|------------------------------------------|-----------------------------------------|
| POST | `/patent-supplementaries`                | 新增记录                                |
| POST | `/patent-supplementaries/insertOrUpdate` | 新增或更新                              |
| DELETE | `/patent-supplementaries/{id}`          | 根据主键删除                            |
| PUT  | `/patent-supplementaries/{id}`            | 根据主键更新                            |
| GET  | `/patent-supplementaries/{id}`            | 根据主键查询                            |
| GET  | `/patent-supplementaries`                 | 分页查询（参数 `page`, `page_size`，默认100） |
| POST | `/patent-supplementaries/list`            | 条件查询（传入 PatentSupplementaryQuery）|
| POST | `/patent-supplementaries/listByParams`    | 自由 Map 参数动态查询                   |
| GET  | `/patent-supplementaries/export`          | 导出全部数据为 Excel（文件名"补漏专利.xls"） |

> 查询字段（均可选）：`id`, `seqNo`, `applicationNo`, `patentName`, `applicant`, `inventor`, `sponsor`, `agent`, `applicationDate`, `notification`, `issueDate`, `feeReduction`, `remark`, `createTime`, `updateTime`, `orderBy`

---

## 9. 接口汇总

| 模块         | 新增 | 新增/更新 | 删除 | 更新 | 查单个 | 分页 | 条件查 | Map查 | 统计 | 导出 |
|--------------|------|-----------|------|------|--------|------|--------|-------|------|------|
| 专利交底     | POST / | POST /insertOrUpdate | DELETE /{id} | PUT / | GET /{id} | GET /page | POST /list | POST /listByParams | POST /count | — |
| 中间著变     | POST / | POST /insertOrUpdate | DELETE /{id} | PUT /{id} | GET /{id} | GET / | POST /list | POST /listByParams | — | GET /export |
| 专利新申请   | POST / | POST /insertOrUpdate | DELETE /{id} | PUT /{id} | GET /{id} | GET / | POST /list | POST /listByParams | — | GET /export |
| PCT申请      | POST / | POST /insertOrUpdate | DELETE /{id} | PUT /{id} | GET /{id} | GET / | POST /list | POST /listByParams | — | GET /export |
| 复审无效     | POST / | POST /insertOrUpdate | DELETE /{id} | PUT /{id} | GET /{id} | GET / | POST /list | POST /listByParams | — | GET /export |
| 补漏专利     | POST / | POST /insertOrUpdate | DELETE /{id} | PUT /{id} | GET /{id} | GET / | POST /list | POST /listByParams | — | GET /export |

---

**文档生成日期：** 2026-07-08  
**适用范围：** 知识产权专利管理系统 API 开发与测试