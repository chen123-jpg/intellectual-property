-- =============================================================================
-- 员工分类体系（可扩展）
-- 说明:
--   employee_type     员工类型字典（录入/主办/流程/联系人/管理员…后续可加）
--   employee          员工档案（可关联登录账号 user，也可仅档案无账号如外部联系人）
--   employee_type_rel 员工-类型多对多（一人可兼多类）
-- =============================================================================

SET NAMES utf8mb4;

-- -----------------------------------------------------------------------------
-- 1. 员工类型字典
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS employee_type (
    id              BIGINT          NOT NULL AUTO_INCREMENT COMMENT '主键',
    type_code       VARCHAR(50)     NOT NULL COMMENT '类型编码，如 ENTRY/SPONSOR/PROCESS/CONTACT/ADMIN',
    type_name       VARCHAR(100)    NOT NULL COMMENT '类型名称',
    description     VARCHAR(500)    NULL COMMENT '说明',
    need_login      TINYINT         NOT NULL DEFAULT 1 COMMENT '是否需要登录账号: 1是 0否(如外部联系人)',
    is_system       TINYINT         NOT NULL DEFAULT 0 COMMENT '系统预置: 1不可删',
    enabled         TINYINT         NOT NULL DEFAULT 1 COMMENT '是否启用',
    sort_no         INT             NOT NULL DEFAULT 0 COMMENT '排序，小在前',
    create_time     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_et_code (type_code),
    KEY idx_et_enabled (enabled)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='员工类型字典（可扩展）';

-- 预置类型（可重复执行时忽略冲突）
INSERT INTO employee_type (type_code, type_name, description, need_login, is_system, enabled, sort_no)
VALUES
    ('ENTRY',   '录入人员', '录入交底信息、上传交底书与附件、复制交底', 1, 1, 1, 10),
    ('SPONSOR', '主办人',   '处理本人主办交底：改状态、发邮件、定稿上传申请包', 1, 1, 1, 20),
    ('PROCESS', '流程人员', '确认申请包、定稿待报并同步申请专利表', 1, 1, 1, 30),
    ('CONTACT', '联系人',   '专利联系人等外部/内部联系对象，后续业务扩展', 0, 1, 1, 40),
    ('ADMIN',   '管理员',   '员工与类型维护、拥有全部业务权限', 1, 1, 1, 1)
ON DUPLICATE KEY UPDATE
    type_name = VALUES(type_name),
    description = VALUES(description),
    need_login = VALUES(need_login),
    is_system = VALUES(is_system),
    enabled = VALUES(enabled),
    sort_no = VALUES(sort_no);

-- -----------------------------------------------------------------------------
-- 2. 员工档案
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS employee (
    id              BIGINT          NOT NULL AUTO_INCREMENT COMMENT '主键',
    user_id         INT             NULL COMMENT '关联登录账号 user.id，可空',
    emp_no          VARCHAR(50)     NULL COMMENT '工号',
    name            VARCHAR(100)    NOT NULL COMMENT '姓名',
    email           VARCHAR(200)    NULL COMMENT '邮箱',
    phone           VARCHAR(50)     NULL COMMENT '电话',
    department      VARCHAR(100)    NULL COMMENT '部门',
    status          VARCHAR(20)     NOT NULL DEFAULT 'ACTIVE' COMMENT 'ACTIVE启用 / DISABLED停用',
    remark          VARCHAR(500)    NULL COMMENT '备注',
    create_time     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_emp_user_id (user_id),
    KEY idx_emp_email (email),
    KEY idx_emp_name (name),
    KEY idx_emp_status (status),
    KEY idx_emp_emp_no (emp_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='员工档案表';

-- -----------------------------------------------------------------------------
-- 3. 员工-类型关系（多对多）
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS employee_type_rel (
    id              BIGINT          NOT NULL AUTO_INCREMENT COMMENT '主键',
    employee_id     BIGINT          NOT NULL COMMENT '员工ID',
    type_code       VARCHAR(50)     NOT NULL COMMENT '类型编码',
    create_time     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_etr_emp_type (employee_id, type_code),
    KEY idx_etr_type (type_code),
    KEY idx_etr_employee (employee_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='员工类型关系表';
