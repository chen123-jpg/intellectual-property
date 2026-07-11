# 专利新申请表业务逻辑说明

## 1. 调用链

当前模块的主要调用链如下：

```text
Controller
  -> Service
    -> Mapper Interface
      -> Mapper XML
        -> patent_new_application
```

对应代码职责：

- `PatentNewApplicationController`：暴露 HTTP 接口
- `PatentNewApplicationService`：定义业务能力
- `PatentNewApplicationServiceImpl`：实现业务规则
- `PatentNewApplicationMapper`：定义数据库操作方法
- `PatentNewApplicationMapper.xml`：SQL 明细

## 2. 新增逻辑

入口：

```text
POST /patent-new-applications
```

执行步骤：

1. Controller 接收 `PatentNewApplication`
2. Service 判断 `internalNo` 是否为空
3. 若为空，调用编号生成服务生成内部编号
4. 调用 Mapper 执行插入
5. XML 自动写入 `create_time` 和 `update_time`

当前规则：

- `internalNo` 未传时自动生成
- `internalNo` 已传时保留调用方提供的值

## 3. 插入或更新逻辑

入口：

```text
POST /patent-new-applications/insertOrUpdate
```

执行方式：

- 使用 `ON DUPLICATE KEY UPDATE`
- 是否走更新，取决于数据库唯一键是否命中

注意：

- 这是数据库层面的 upsert
- 实际命中条件依赖表结构中的唯一索引

## 4. 更新逻辑

入口：

```text
PUT /patent-new-applications/{id}
```

执行步骤：

1. Controller 用路径中的 `id` 覆盖请求体中的 `id`
2. Service 调用 `updateById`
3. XML 仅更新非空字段
4. `update_time` 自动更新为 `NOW()`

当前特征：

- 只更新非空字段
- 不支持把已有字段显式更新为 `null`

## 5. 分页逻辑

入口：

```text
GET /patent-new-applications?page=1&page_size=100
```

执行步骤：

1. Controller 接收分页参数
2. Service 计算偏移量：`(pageNum - 1) * pageSize`
3. Mapper 执行分页查询
4. 同时调用 `count()` 返回总数

当前规则：

- 默认 `page = 1`
- 默认 `page_size = 100`
- 查询结果按 `id DESC` 排序

## 6. 条件查询逻辑

入口：

```text
POST /patent-new-applications/list
```

执行步骤：

1. Controller 接收 `PatentNewApplicationQuery`
2. Service 对 `orderBy` 做白名单校验
3. Mapper XML 根据非空字段拼接 `WHERE`
4. 若 `orderBy` 合法，则拼接排序语句

查询特征：

- 条件查询为精确匹配
- 不支持模糊匹配
- 不支持范围查询

## 7. 导出逻辑

入口：

```text
GET /patent-new-applications/export
```

执行步骤：

1. Controller 调用 `selectAll()`
2. 取出全部记录
3. 交给 `ExcelExportUtil` 导出
4. 列顺序由实体字段上的 `@ExcelField` 控制

## 8. 当前设计约束

### 8.1 保留 Query 查询，移除动态 Map 查询

当前专利新申请表模块已统一使用：

```text
PatentNewApplicationQuery
```

不再保留对外的：

```text
Map<String, Object> params
```

这样做的原因：

- 查询字段边界明确
- 避免字段名动态直拼
- 接口定义更稳定

### 8.2 排序白名单

当前允许的排序值：

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

不在白名单中的排序值会被置空，不参与 SQL 拼接。

## 9. 后续优化建议

建议后续优先关注以下方向：

1. 为新增编号生成补数据库唯一约束兜底
2. 评估是否需要支持字段清空能力
3. 如果业务有搜索需求，可补充模糊查询能力
4. 为查询和新增逻辑补单元测试或接口测试
