# 交底流程 API

基础路径：`/api/disclosure-workflow`  
统一响应：`{ "code": 200, "message": "success", "data": ... }`

## 交底

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/disclosures` | 录入交底，同步写缴费表+开票表 |
| POST | `/disclosures/copy` | 复制历史交底 |
| PUT | `/disclosures/{id}` | 更新交底 |
| POST | `/disclosures/search` | 组合查询 |
| GET | `/disclosures/by-sponsor/{sponsorUserId}` | 主办人交底列表 |
| GET | `/disclosures/{id}` | 详情（附件/包/缴费/开票/日志） |
| POST | `/disclosures/{id}/status` | 改状态；目标 12/定稿待报 时走同步 |
| POST | `/disclosures/{id}/pending-report` | 定稿待报并同步 P 表 |

### 录入示例

```json
POST /api/disclosure-workflow/disclosures
{
  "autoGenerateNo": true,
  "disclosure": {
    "disclosureName": "一种测试方法",
    "applicant": "某某公司",
    "inventor": "张三",
    "sponsor": "李主办",
    "sponsorUserId": 1,
    "contactPerson": "王联系",
    "contactEmail": "a@b.com",
    "agent": "代理人A",
    "patentType": "发明"
  }
}
```

### 改状态

```json
POST /api/disclosure-workflow/disclosures/1/status
{ "toStatus": "11", "operatorName": "主办", "remark": "定稿" }
```

状态码：`11=定稿`（可上传申请包），`12=定稿待报`（需 XML+五书齐全，同步 `patent_new_application`）。

## 附件

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/disclosures/{id}/attachments` | multipart：`file` + `bizType`=`DISCLOSURE_DOC`/`DISCLOSURE_OTHER` |
| GET | `/disclosures/{id}/attachments` | 列表 |
| DELETE | `/attachments/{attachmentId}` | 逻辑删除 |

交底书 `DISCLOSURE_DOC` 必须 `.doc/.docx`。

## 申请包

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/disclosures/{id}/packages` | multipart：`file` + `packageType`=`XML_PACKAGE` 或 `FIVE_BOOKS_WORD` |
| GET | `/disclosures/{id}/packages` | 列表（含历史版本） |
| POST | `/packages/{packageId}/confirm` | 流程确认 |

## 邮件

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/mail-templates` | 启用模板列表 |
| GET | `/mail-templates/{code}/preview?disclosureId=` | 渲染占位符预览 |
| POST | `/mail/send` | 发送（需登录，使用用户 SMTP） |
| GET | `/disclosures/{id}/mail-logs` | 发送记录 |

```json
POST /api/disclosure-workflow/mail/send
{
  "disclosureId": 1,
  "templateCode": "DISCLOSURE_CONTACT",
  "toEmails": "a@b.com",
  "subject": "可改主题",
  "content": "可改正文",
  "attachmentIds": [1, 2]
}
```

## 查询附属

| GET | `/disclosures/{id}/fees` |
| GET | `/disclosures/{id}/invoices` |
| GET | `/disclosures/{id}/status-logs` |
