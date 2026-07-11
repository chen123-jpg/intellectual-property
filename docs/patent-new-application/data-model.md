# 专利新申请表数据模型

## 1. 实体对象

实体类：`PatentNewApplication`

对应数据表：`patent_new_application`

## 2. 字段说明

| 字段名 | 类型 | 说明 |
| --- | --- | --- |
| `id` | `Long` | 主键 ID |
| `seqNo` | `Integer` | 序号 |
| `internalNo` | `String` | 内部编号 |
| `patentName` | `String` | 发明创造名称 |
| `applicationNo` | `String` | 申请号/专利号 |
| `patentType` | `String` | 类型，通常为发明/实用新型/外观设计 |
| `applicant` | `String` | 申请人 |
| `inventor` | `String` | 发明人 |
| `sponsor` | `String` | 主办人 |
| `agent` | `String` | 委托书代理人 |
| `applicationDate` | `Date` | 申请日 |
| `notification` | `String` | 通知书 |
| `issueDate` | `Date` | 发文日 |
| `preExamMark` | `String` | 非正标-预审标 |
| `paymentDeadline` | `Date` | 缴费止期 |
| `feeAmount` | `Double` | 费用金额 |
| `paymentDate` | `String` | 缴费时间 |
| `dasCode` | `String` | DAS 码 |
| `createTime` | `Date` | 创建时间 |
| `updateTime` | `Date` | 更新时间 |

## 3. 查询对象

查询类：`PatentNewApplicationQuery`

用途：

- 用于 `/patent-new-applications/list`
- 用于条件统计 `selectCount`

查询对象字段基本与实体一致，额外包含：

| 字段名 | 类型 | 说明 |
| --- | --- | --- |
| `orderBy` | `String` | 排序字段，仅允许白名单值 |

## 4. 日期格式

模块中的日期字段使用以下格式序列化：

```text
yyyy-MM-dd HH:mm:ss
```

时区设置为：

```text
GMT+8
```

## 5. Excel 导出字段

导出顺序由 `@ExcelField` 决定，当前导出列顺序如下：

1. 序号
2. 内部编号
3. 发明创造名称
4. 申请号/专利号
5. 类型
6. 申请人
7. 发明人
8. 主办人
9. 委托书代理人
10. 申请日
11. 通知书
12. 发文日
13. 非正标-预审标
14. 缴费止期
15. 费用金额
16. 缴费时间
17. DAS码
