# 专利新申请表接口文档

## 1. 基本信息

- 模块名称：专利新申请表
- 资源路径：`/patent-new-applications`
- 响应包装：`Result<T>`

统一响应示例：

```json
{
  "code": 200,
  "message": "success",
  "data": {}
}
```

## 2. 接口列表

### 2.1 新增记录

- 方法：`POST`
- 路径：`/patent-new-applications`

请求体示例：

```json
{
  "seqNo": 1,
  "patentName": "一种专利申请方法",
  "applicationNo": "CN2026000001",
  "patentType": "发明",
  "applicant": "某科技公司",
  "inventor": "张三",
  "sponsor": "李四",
  "agent": "王五",
  "applicationDate": "2026-07-11 10:00:00",
  "notification": "受理通知书",
  "issueDate": "2026-07-12 10:00:00",
  "preExamMark": "预审通过",
  "paymentDeadline": "2026-08-11 10:00:00",
  "feeAmount": 500.0,
  "paymentDate": "2026-07-15",
  "dasCode": "DAS-001"
}
```

说明：

- 如果未传 `internalNo`，系统会自动生成

### 2.2 插入或更新

- 方法：`POST`
- 路径：`/patent-new-applications/insertOrUpdate`

说明：

- 依赖数据库唯一键命中 `ON DUPLICATE KEY UPDATE`
- 若数据库未命中重复键，则表现为插入

### 2.3 删除记录

- 方法：`DELETE`
- 路径：`/patent-new-applications/{id}`

路径参数：

| 参数名 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | `Long` | 是 | 主键 ID |

### 2.4 按 ID 更新

- 方法：`PUT`
- 路径：`/patent-new-applications/{id}`

说明：

- 请求体中的 `id` 以路径参数为准
- 当前更新逻辑仅更新非空字段

### 2.5 按 ID 查询

- 方法：`GET`
- 路径：`/patent-new-applications/{id}`

### 2.6 分页查询

- 方法：`GET`
- 路径：`/patent-new-applications`

查询参数：

| 参数名 | 类型 | 默认值 | 说明 |
| --- | --- | --- | --- |
| `page` | `int` | `1` | 页码，从 1 开始 |
| `page_size` | `int` | `100` | 每页条数 |

响应 `data` 结构：

```json
{
  "list": [],
  "total": 0
}
```

当前排序规则：

```text
ORDER BY id DESC
```

### 2.7 条件查询

- 方法：`POST`
- 路径：`/patent-new-applications/list`

请求体类型：

```json
{
  "internalNo": "P2026001",
  "patentName": "一种专利申请方法",
  "applicationNo": "CN2026000001",
  "applicant": "某科技公司",
  "inventor": "张三",
  "sponsor": "李四",
  "agent": "王五",
  "patentType": "发明",
  "seqNo": 1,
  "orderBy": "create_time desc"
}
```

说明：

- 所有查询字段均为可选
- 当前为精确匹配查询，不支持模糊匹配
- `orderBy` 仅允许以下值：

```text
id desc
id asc
seq_no desc
seq_no asc
create_time desc
create_time asc
update_time desc
update_time asc
```

不在白名单中的排序值会被忽略。

### 2.8 Excel 导出

- 方法：`GET`
- 路径：`/patent-new-applications/export`

说明：

- 导出全部记录
- 返回 Excel 文件

## 3. 已移除接口

以下接口已不再作为“专利新申请表”对外查询入口保留：

```text
POST /patent-new-applications/listByParams
```

原因：

- 动态 Map 查询会带来字段名直拼风险
- 查询条件不可控，不利于接口规范化
- 已统一收口到 `PatentNewApplicationQuery`
