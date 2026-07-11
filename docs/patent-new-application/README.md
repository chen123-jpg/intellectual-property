# 专利新申请表文档

## 1. 文档说明

本目录用于整理 `专利新申请表` 模块的后端文档，覆盖数据结构、接口定义和核心业务逻辑。

适用范围：

- `PatentNewApplicationController`
- `PatentNewApplicationService`
- `PatentNewApplicationServiceImpl`
- `PatentNewApplicationMapper`
- `PatentNewApplicationMapper.xml`
- `PatentNewApplication`
- `PatentNewApplicationQuery`

## 2. 文档目录

- [数据模型](./data-model.md)
- [接口文档](./api.md)
- [业务逻辑说明](./logic.md)

## 3. 模块概览

模块资源路径：

```text
/patent-new-applications
```

当前提供的能力：

- 新增记录
- 插入或更新
- 删除记录
- 按主键查询
- 分页查询
- 条件查询
- Excel 导出

当前约束与约定：

- 新增时，如未提供 `internalNo`，系统自动生成内部编号
- 条件查询统一使用 `PatentNewApplicationQuery`
- 对外已不再保留 `listByParams(Map<String, Object>)` 这种动态 Map 查询入口
- `orderBy` 仅允许白名单字段
- 分页列表默认按 `id DESC` 返回
