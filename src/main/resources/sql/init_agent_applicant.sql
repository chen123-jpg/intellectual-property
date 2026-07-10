-- 代理人表
CREATE TABLE IF NOT EXISTS agent (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    name VARCHAR(255) NOT NULL COMMENT '代理人姓名',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='代理人表';

-- 申请人表
CREATE TABLE IF NOT EXISTS applicant (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    name VARCHAR(255) NOT NULL COMMENT '申请人姓名/名称',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='申请人表';

-- 从现有专利表中导入唯一的代理人姓名
INSERT INTO agent (name)
SELECT DISTINCT agent FROM patent_new_application WHERE agent IS NOT NULL AND agent != ''
UNION
SELECT DISTINCT agent FROM patent_disclosure WHERE agent IS NOT NULL AND agent != ''
UNION
SELECT DISTINCT agent FROM patent_pct WHERE agent IS NOT NULL AND agent != ''
UNION
SELECT DISTINCT agent FROM patent_supplementary WHERE agent IS NOT NULL AND agent != ''
UNION
SELECT DISTINCT agent FROM patent_reexamination WHERE agent IS NOT NULL AND agent != ''
UNION
SELECT DISTINCT agent FROM patent_intermediate_change WHERE agent IS NOT NULL AND agent != '';

-- 从现有专利表中导入唯一的申请人姓名
INSERT INTO applicant (name)
SELECT DISTINCT applicant FROM patent_new_application WHERE applicant IS NOT NULL AND applicant != ''
UNION
SELECT DISTINCT applicant FROM patent_disclosure WHERE applicant IS NOT NULL AND applicant != ''
UNION
SELECT DISTINCT applicant FROM patent_pct WHERE applicant IS NOT NULL AND applicant != ''
UNION
SELECT DISTINCT applicant FROM patent_supplementary WHERE applicant IS NOT NULL AND applicant != ''
UNION
SELECT DISTINCT applicant FROM patent_reexamination WHERE applicant IS NOT NULL AND applicant != ''
UNION
SELECT DISTINCT applicant FROM patent_intermediate_change WHERE applicant IS NOT NULL AND applicant != '';
