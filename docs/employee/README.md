# 员工分类与权限

## 表结构（可扩展）

| 表 | 说明 |
|----|------|
| `employee_type` | 类型字典：`ENTRY`/`SPONSOR`/`PROCESS`/`CONTACT`/`ADMIN`，后续可加新编码 |
| `employee` | 员工档案，可关联 `user.id`（外部联系人可无账号） |
| `employee_type_rel` | 员工-类型多对多 |

DDL：`src/main/resources/sql/employee_ddl.sql`

## 交底流程权限矩阵

| 能力 | 录入 ENTRY | 主办 SPONSOR | 流程 PROCESS | 管理 ADMIN |
|------|:---:|:---:|:---:|:---:|
| 录入/复制交底 | ✓ | | | ✓ |
| 查全部交底 | ✓ | 仅本人 | ✓ | ✓ |
| 改状态（处理中/定稿） | | 本人 | | ✓ |
| 发邮件给联系人 | | 本人 | | ✓ |
| 上传申请包 | | 本人 | | ✓ |
| 确认申请包 | | | ✓ | ✓ |
| 定稿待报同步 P 表 | | | ✓ | ✓ |
| 员工管理 | | | | ✓ |

## 登录/注册

- 注册可选员工类型（默认 ENTRY），同步写员工档案
- 登录返回 `roles` / `roleNames` / `employeeId`
- 历史账号无档案时登录自动补 ENTRY

## 主要 API

- `GET /api/employees/types`
- `GET /api/employees`、`POST /api/employees`（ADMIN）
- `GET /api/employees/by-type/{typeCode}`
- 交底工作流接口均需 `token`，并按角色鉴权
