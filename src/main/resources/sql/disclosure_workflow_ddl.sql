-- =============================================================================
-- 交底处理流程 DDL
-- 数据库: MySQL 8.x / 5.7+  (utf8mb4)
-- 说明:
--   1. 扩展现有 patent_disclosure（T表）
--   2. 新建附件、缴费、开票、邮件模板/发送记录、申请包等表
--   3. 定稿待报时同步写入现有 patent_new_application（P表-新申请），本脚本不改其结构
-- 执行前请备份；若列已存在，对应 ALTER 会报错，可按需注释后重跑
-- =============================================================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- -----------------------------------------------------------------------------
-- 0. 交底状态约定（应用层枚举，不强制建字典表）
--    0  接收交底书
--    1  联系发明人
--    2  检索交底书
--    3  撤回
--    4  打回
--    5  修改交底
--    6  一稿撰写中
--    7  一稿待反馈
--    8  N稿撰写中
--    9  N稿待反馈
--   10  待定稿
--   11  定稿              -- 主办人可上传 XML包 / 五书WORD
--   12  定稿待报          -- 流程确认后；同步写入 patent_new_application
--   13  提交待受理
--   14  受理
--  注: 与旧版「11=定稿待提交」相比，拆成「定稿」与「定稿待报」两态，应用需同步改枚举
-- -----------------------------------------------------------------------------


-- =============================================================================
-- 1. 扩展交底主表 patent_disclosure
-- =============================================================================

-- 1.1 业务扩展字段（若不存在则添加；已存在请跳过对应语句）
ALTER TABLE patent_disclosure
    ADD COLUMN inventor            VARCHAR(500)  NULL COMMENT '发明人' AFTER applicant,
    ADD COLUMN patent_type         VARCHAR(50)   NULL COMMENT '专利类型：发明/实用新型/外观等' AFTER inventor,
    ADD COLUMN sponsor             VARCHAR(100)  NULL COMMENT '主办人姓名' AFTER agent,
    ADD COLUMN sponsor_user_id     BIGINT        NULL COMMENT '主办人用户ID' AFTER sponsor,
    ADD COLUMN contact_email       VARCHAR(200)  NULL COMMENT '联系人邮箱(发邮件主收件人)' AFTER contact_info,
    ADD COLUMN contact_phone       VARCHAR(50)   NULL COMMENT '联系人电话' AFTER contact_email,
    ADD COLUMN entry_user_id       BIGINT        NULL COMMENT '录入人用户ID' AFTER contact_phone,
    ADD COLUMN entry_user_name     VARCHAR(100)  NULL COMMENT '录入人姓名' AFTER entry_user_id,
    ADD COLUMN copy_from_id        BIGINT        NULL COMMENT '复制来源交底ID' AFTER entry_user_name,
    ADD COLUMN no_generate_mode    VARCHAR(20)   NULL DEFAULT 'AUTO' COMMENT '编号方式: AUTO/MANUAL' AFTER copy_from_id,
    ADD COLUMN finalized_at        DATETIME      NULL COMMENT '进入定稿时间' AFTER no_generate_mode,
    ADD COLUMN pending_report_at   DATETIME      NULL COMMENT '进入定稿待报时间' AFTER finalized_at,
    ADD COLUMN synced_to_patent    TINYINT       NOT NULL DEFAULT 0 COMMENT '是否已同步申请专利表: 0否 1是' AFTER pending_report_at,
    ADD COLUMN patent_application_id BIGINT      NULL COMMENT '同步后的patent_new_application.id' AFTER synced_to_patent;

-- 1.2 索引（按需；重复创建会报错可忽略）
-- 内部编号：业务主关联键，建议唯一（历史脏数据时请先清洗再加 UNIQUE）
-- ALTER TABLE patent_disclosure ADD UNIQUE INDEX uk_pd_internal_no (internal_no);

CREATE INDEX idx_pd_temp_no            ON patent_disclosure (temp_no);
CREATE INDEX idx_pd_internal_no        ON patent_disclosure (internal_no);
CREATE INDEX idx_pd_patent_status      ON patent_disclosure (patent_status);
CREATE INDEX idx_pd_sponsor            ON patent_disclosure (sponsor);
CREATE INDEX idx_pd_sponsor_user_id    ON patent_disclosure (sponsor_user_id);
CREATE INDEX idx_pd_entry_user_id      ON patent_disclosure (entry_user_id);
CREATE INDEX idx_pd_disclosure_date    ON patent_disclosure (disclosure_date);
CREATE INDEX idx_pd_copy_from_id       ON patent_disclosure (copy_from_id);
CREATE INDEX idx_pd_synced_to_patent   ON patent_disclosure (synced_to_patent);
CREATE INDEX idx_pd_status_sponsor     ON patent_disclosure (patent_status, sponsor_user_id);
CREATE INDEX idx_pd_applicant          ON patent_disclosure (applicant(100));
CREATE INDEX idx_pd_agent              ON patent_disclosure (agent);


-- =============================================================================
-- 2. 交底附件表 disclosure_attachment
--    biz_type: DISCLOSURE_DOC=交底书(须Word) | DISCLOSURE_OTHER=其他 | MAIL_EXTRA=发信附加
-- =============================================================================

DROP TABLE IF EXISTS disclosure_attachment;
CREATE TABLE disclosure_attachment (
    id                      BIGINT          NOT NULL AUTO_INCREMENT COMMENT '主键',
    disclosure_id           BIGINT          NOT NULL COMMENT '交底ID',
    internal_no             VARCHAR(32)     NULL COMMENT '内部编号(冗余)',
    biz_type                VARCHAR(32)     NOT NULL COMMENT 'DISCLOSURE_DOC / DISCLOSURE_OTHER / MAIL_EXTRA',
    mail_send_log_id        BIGINT          NULL COMMENT '关联发信记录(仅MAIL_EXTRA时用)',
    file_name               VARCHAR(255)    NOT NULL COMMENT '原始文件名',
    file_ext                VARCHAR(20)     NULL COMMENT '扩展名，不含点',
    file_path               VARCHAR(500)    NOT NULL COMMENT '存储相对/绝对路径',
    file_url                VARCHAR(500)    NULL COMMENT '访问URL，如 /files/xxx.docx?name=...',
    file_size               BIGINT          NULL DEFAULT 0 COMMENT '字节数',
    content_type            VARCHAR(100)    NULL COMMENT 'MIME',
    is_required             TINYINT         NOT NULL DEFAULT 0 COMMENT '是否必填类: 交底书=1',
    sort_no                 INT             NOT NULL DEFAULT 0 COMMENT '排序，小在前',
    upload_user_id          BIGINT          NULL COMMENT '上传人ID',
    upload_user_name        VARCHAR(100)    NULL COMMENT '上传人姓名',
    deleted                 TINYINT         NOT NULL DEFAULT 0 COMMENT '逻辑删除: 0否 1是',
    create_time             DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time             DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    KEY idx_da_disclosure_id   (disclosure_id),
    KEY idx_da_internal_no     (internal_no),
    KEY idx_da_biz_type        (biz_type),
    KEY idx_da_mail_log        (mail_send_log_id),
    KEY idx_da_deleted         (deleted),
    KEY idx_da_disc_type_del   (disclosure_id, biz_type, deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='交底附件表';


-- =============================================================================
-- 3. 缴费表 fee_payment（录入交底时同步建档）
-- =============================================================================

DROP TABLE IF EXISTS fee_payment;
CREATE TABLE fee_payment (
    id                      BIGINT          NOT NULL AUTO_INCREMENT COMMENT '主键',
    disclosure_id           BIGINT          NOT NULL COMMENT '交底ID',
    internal_no             VARCHAR(32)     NULL COMMENT '内部编号',
    temp_no                 VARCHAR(32)     NULL COMMENT '临时编号',
    disclosure_name         VARCHAR(500)    NULL COMMENT '交底/专利名称(冗余)',
    applicant               VARCHAR(500)    NULL COMMENT '申请人/缴费主体',
    fee_type                VARCHAR(50)     NULL COMMENT '费用类型: 官费/代理费等',
    fee_amount              DECIMAL(12,2)   NULL COMMENT '金额',
    payment_deadline        DATE            NULL COMMENT '缴费止期',
    payment_date            DATE            NULL COMMENT '实缴日期',
    payment_status          VARCHAR(30)     NOT NULL DEFAULT 'PENDING' COMMENT 'PENDING待缴/PAID已缴/PARTIAL部分/VOID作废',
    payer                   VARCHAR(100)    NULL COMMENT '付款方',
    remark                  VARCHAR(500)    NULL COMMENT '备注',
    source                  VARCHAR(30)     NOT NULL DEFAULT 'DISCLOSURE_SYNC' COMMENT '来源: DISCLOSURE_SYNC等',
    create_time             DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time             DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    KEY idx_fp_disclosure_id   (disclosure_id),
    KEY idx_fp_internal_no     (internal_no),
    KEY idx_fp_payment_status  (payment_status),
    KEY idx_fp_deadline        (payment_deadline),
    -- 同一交底默认同步一条主缴费记录；若允许多费用项可去掉该唯一约束
    UNIQUE KEY uk_fp_disclosure_source (disclosure_id, source, fee_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='缴费表';


-- =============================================================================
-- 4. 开票表 invoice（录入交底时同步建档）
-- =============================================================================

DROP TABLE IF EXISTS invoice;
CREATE TABLE invoice (
    id                      BIGINT          NOT NULL AUTO_INCREMENT COMMENT '主键',
    disclosure_id           BIGINT          NOT NULL COMMENT '交底ID',
    internal_no             VARCHAR(32)     NULL COMMENT '内部编号',
    temp_no                 VARCHAR(32)     NULL COMMENT '临时编号',
    disclosure_name         VARCHAR(500)    NULL COMMENT '交底/专利名称(冗余)',
    applicant               VARCHAR(500)    NULL COMMENT '申请人',
    invoice_title           VARCHAR(200)    NULL COMMENT '发票抬头',
    tax_no                  VARCHAR(50)     NULL COMMENT '税号',
    invoice_type            VARCHAR(30)     NULL COMMENT '发票类型: 普票/专票等',
    invoice_amount          DECIMAL(12,2)   NULL COMMENT '开票金额',
    invoice_status          VARCHAR(30)     NOT NULL DEFAULT 'PENDING' COMMENT 'PENDING待开/ISSUED已开/VOID作废',
    invoice_no              VARCHAR(50)     NULL COMMENT '发票号码',
    invoice_date            DATE            NULL COMMENT '开票日期',
    remark                  VARCHAR(500)    NULL COMMENT '备注',
    source                  VARCHAR(30)     NOT NULL DEFAULT 'DISCLOSURE_SYNC' COMMENT '来源: DISCLOSURE_SYNC等',
    create_time             DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time             DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    KEY idx_inv_disclosure_id  (disclosure_id),
    KEY idx_inv_internal_no    (internal_no),
    KEY idx_inv_status         (invoice_status),
    KEY idx_inv_invoice_no     (invoice_no),
    UNIQUE KEY uk_inv_disclosure_source (disclosure_id, source)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='开票表';


-- =============================================================================
-- 5. 邮件模板 mail_template
-- =============================================================================

DROP TABLE IF EXISTS mail_template;
CREATE TABLE mail_template (
    id                      BIGINT          NOT NULL AUTO_INCREMENT COMMENT '主键',
    template_code           VARCHAR(50)     NOT NULL COMMENT '模板编码',
    template_name           VARCHAR(100)    NOT NULL COMMENT '模板名称',
    subject                 VARCHAR(500)    NOT NULL COMMENT '主题模板，支持占位符',
    content                 TEXT            NOT NULL COMMENT '正文模板，支持占位符',
    default_attach_types    VARCHAR(200)    NULL COMMENT '默认附带附件类型，逗号分隔如 DISCLOSURE_DOC',
    enabled                 TINYINT         NOT NULL DEFAULT 1 COMMENT '是否启用: 0否 1是',
    create_time             DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time             DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_mt_code (template_code),
    KEY idx_mt_enabled (enabled)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='邮件模板表';

-- 预置：联系发明人/交底处理通知
INSERT INTO mail_template (template_code, template_name, subject, content, default_attach_types, enabled)
VALUES (
    'DISCLOSURE_CONTACT',
    '交底联系通知',
    '关于专利交底「${disclosureName}」的沟通（内部编号：${internalNo}）',
    '尊敬的 ${contactPerson}：\n\n您好！\n\n关于交底「${disclosureName}」（临时编号：${tempNo}，内部编号：${internalNo}），我司主办人 ${sponsor} 希望与您进一步沟通。\n\n如有材料请查收附件。\n\n此致\n敬礼',
    'DISCLOSURE_DOC',
    1
);


-- =============================================================================
-- 6. 发信记录 mail_send_log
-- =============================================================================

DROP TABLE IF EXISTS mail_send_log;
CREATE TABLE mail_send_log (
    id                      BIGINT          NOT NULL AUTO_INCREMENT COMMENT '主键',
    disclosure_id           BIGINT          NOT NULL COMMENT '交底ID',
    internal_no             VARCHAR(32)     NULL COMMENT '内部编号',
    template_id             BIGINT          NULL COMMENT '所用模板ID',
    template_code           VARCHAR(50)     NULL COMMENT '模板编码(冗余)',
    from_email              VARCHAR(200)    NULL COMMENT '发件人邮箱',
    to_emails               VARCHAR(1000)   NOT NULL COMMENT '收件人，逗号分隔',
    cc_emails               VARCHAR(1000)   NULL COMMENT '抄送，逗号分隔',
    subject                 VARCHAR(500)    NOT NULL COMMENT '实际发送主题',
    content                 TEXT            NOT NULL COMMENT '实际发送正文',
    send_status             VARCHAR(20)     NOT NULL DEFAULT 'PENDING' COMMENT 'PENDING/SUCCESS/FAILED',
    error_message           VARCHAR(1000)   NULL COMMENT '失败原因',
    sender_user_id          BIGINT          NULL COMMENT '发送人用户ID',
    sender_name             VARCHAR(100)    NULL COMMENT '发送人姓名',
    sent_at                 DATETIME        NULL COMMENT '实际发送时间',
    create_time             DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    KEY idx_msl_disclosure_id  (disclosure_id),
    KEY idx_msl_internal_no    (internal_no),
    KEY idx_msl_send_status    (send_status),
    KEY idx_msl_sender_user    (sender_user_id),
    KEY idx_msl_sent_at        (sent_at),
    KEY idx_msl_template_id    (template_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='邮件发送记录表';


-- =============================================================================
-- 7. 发信附件 mail_send_attachment（一次发送的最终附件清单）
-- =============================================================================

DROP TABLE IF EXISTS mail_send_attachment;
CREATE TABLE mail_send_attachment (
    id                          BIGINT          NOT NULL AUTO_INCREMENT COMMENT '主键',
    mail_send_log_id            BIGINT          NOT NULL COMMENT '发信记录ID',
    disclosure_attachment_id    BIGINT          NULL COMMENT '来源交底附件ID，可空(用户临时上传)',
    file_name                   VARCHAR(255)    NOT NULL COMMENT '文件名',
    file_path                   VARCHAR(500)    NOT NULL COMMENT '路径',
    file_url                    VARCHAR(500)    NULL COMMENT '访问URL',
    file_size                   BIGINT          NULL DEFAULT 0 COMMENT '字节数',
    create_time                 DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    KEY idx_msa_log_id (mail_send_log_id),
    KEY idx_msa_da_id  (disclosure_attachment_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='邮件发送附件表';


-- =============================================================================
-- 8. 申请包 application_package
--    package_type: XML_PACKAGE | FIVE_BOOKS_WORD  （两个条目分开）
--    仅状态>=定稿时由主办人上传；流程确认后可改定稿待报
-- =============================================================================

DROP TABLE IF EXISTS application_package;
CREATE TABLE application_package (
    id                      BIGINT          NOT NULL AUTO_INCREMENT COMMENT '主键',
    disclosure_id           BIGINT          NOT NULL COMMENT '交底ID',
    internal_no             VARCHAR(32)     NULL COMMENT '内部编号',
    package_type            VARCHAR(30)     NOT NULL COMMENT 'XML_PACKAGE / FIVE_BOOKS_WORD',
    file_name               VARCHAR(255)    NOT NULL COMMENT '原始文件名',
    file_ext                VARCHAR(20)     NULL COMMENT '扩展名',
    file_path               VARCHAR(500)    NOT NULL COMMENT '存储路径',
    file_url                VARCHAR(500)    NULL COMMENT '访问URL',
    file_size               BIGINT          NULL DEFAULT 0 COMMENT '字节数',
    content_type            VARCHAR(100)    NULL COMMENT 'MIME',
    version_no              INT             NOT NULL DEFAULT 1 COMMENT '版本号，覆盖上传+1',
    is_current              TINYINT         NOT NULL DEFAULT 1 COMMENT '是否当前有效版本: 0否 1是',
    upload_user_id          BIGINT          NULL COMMENT '上传人ID(主办)',
    upload_user_name        VARCHAR(100)    NULL COMMENT '上传人姓名',
    upload_time             DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '上传时间',
    confirm_status          VARCHAR(30)     NOT NULL DEFAULT 'UNCONFIRMED'
                            COMMENT 'UNCONFIRMED未确认 / CONFIRMED可提交 / SUBMITTED已交国知局',
    confirm_user_id         BIGINT          NULL COMMENT '确认人ID(流程)',
    confirm_user_name       VARCHAR(100)    NULL COMMENT '确认人姓名',
    confirm_time            DATETIME        NULL COMMENT '确认时间',
    remark                  VARCHAR(500)    NULL COMMENT '备注',
    create_time             DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time             DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    KEY idx_ap_disclosure_id   (disclosure_id),
    KEY idx_ap_internal_no     (internal_no),
    KEY idx_ap_package_type    (package_type),
    KEY idx_ap_is_current      (is_current),
    KEY idx_ap_confirm_status  (confirm_status),
    KEY idx_ap_disc_type       (disclosure_id, package_type),
    -- 同一交底 + 类型，仅允许一条「当前」记录（MySQL 8.0.13+ 函数索引；
    -- 低版本可改为应用层保证，并注释本句）
    UNIQUE KEY uk_ap_current_type (disclosure_id, package_type, is_current)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='申请包表(XML包与五书WORD分条目)';

-- 说明: uk_ap_current_type 在 is_current 只能为 0/1 时，
-- 历史版本请统一将 is_current 置 0；若同一类型存在多条 is_current=0，
-- 唯一约束会冲突。历史版本建议 is_current 用 0，且仅保留一条 0？
-- 更稳妥做法：历史版本 is_current=0 时不走该唯一键——
-- MySQL 可用生成列实现「仅约束 is_current=1」：

ALTER TABLE application_package
    DROP INDEX uk_ap_current_type;

ALTER TABLE application_package
    ADD COLUMN current_type_key VARCHAR(64)
        GENERATED ALWAYS AS (
            IF(is_current = 1, CONCAT(disclosure_id, ':', package_type), NULL)
        ) VIRTUAL COMMENT '仅当前版本参与唯一' AFTER is_current,
    ADD UNIQUE KEY uk_ap_current_type_key (current_type_key);


-- =============================================================================
-- 9. （可选）交底状态变更日志 disclosure_status_log
-- =============================================================================

DROP TABLE IF EXISTS disclosure_status_log;
CREATE TABLE disclosure_status_log (
    id                      BIGINT          NOT NULL AUTO_INCREMENT COMMENT '主键',
    disclosure_id           BIGINT          NOT NULL COMMENT '交底ID',
    from_status             VARCHAR(50)     NULL COMMENT '原状态(码或文案)',
    to_status               VARCHAR(50)     NOT NULL COMMENT '新状态',
    operator_user_id        BIGINT          NULL COMMENT '操作人ID',
    operator_name           VARCHAR(100)    NULL COMMENT '操作人姓名',
    remark                  VARCHAR(500)    NULL COMMENT '备注/原因',
    create_time             DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    KEY idx_dsl_disclosure_id (disclosure_id),
    KEY idx_dsl_create_time   (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='交底状态变更日志';


-- =============================================================================
-- 10. P表索引增强（定稿待报同步依赖 internal_no，不改表结构）
-- =============================================================================

-- 若尚未存在可执行：
-- CREATE INDEX idx_pna_internal_no ON patent_new_application (internal_no);


SET FOREIGN_KEY_CHECKS = 1;

-- =============================================================================
-- 附: 应用层校验备忘（非SQL）
-- 1. DISCLOSURE_DOC 扩展名仅 doc/docx；同一交底未删除的交底书建议仅 1 份
-- 2. 录入成功同事务写 fee_payment + invoice
-- 3. package 上传仅状态=定稿(11)；定稿待报(12) 前校验两类 is_current=1 均存在
-- 4. 进入定稿待报: synced_to_patent=0 时插入 patent_new_application 并回写 id
-- 5. 主办人列表: sponsor_user_id = 当前用户
-- 6. 复制交底: 复制主表字段 + 可选附件；新 temp_no/internal_no；copy_from_id=源id
-- =============================================================================
