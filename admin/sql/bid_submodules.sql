-- ========================================================================
-- 招标系统子模块建表SQL
-- ========================================================================

-- 1. 供应商报名表
CREATE TABLE IF NOT EXISTS bid_registration (
    id              BIGINT(20)   NOT NULL AUTO_INCREMENT COMMENT '主键',
    project_id      BIGINT(20)   NOT NULL                COMMENT '关联招标项目ID',
    vendor_name     VARCHAR(200) NOT NULL                COMMENT '供应商名称',
    contact_name    VARCHAR(100)                         COMMENT '联系人姓名',
    contact_phone   VARCHAR(50)                          COMMENT '联系电话',
    email           VARCHAR(100)                         COMMENT '邮箱',
    qualification_files TEXT                             COMMENT '资质材料文件路径(JSON数组)',
    status          TINYINT      NOT NULL DEFAULT 0      COMMENT '状态 0待审核 1已通过 2已拒绝',
    reject_reason   VARCHAR(500)                         COMMENT '拒绝原因',
    user_id         BIGINT(20)                           COMMENT '报名用户ID',
    create_time     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP                 COMMENT '创建时间',
    update_time     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by       VARCHAR(100)                         COMMENT '创建人',
    update_by       VARCHAR(100)                         COMMENT '更新人',
    deleted         TINYINT      NOT NULL DEFAULT 0      COMMENT '是否删除1-是、0-否',
    PRIMARY KEY (id),
    KEY idx_project_id (project_id),
    KEY idx_user_id (user_id),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='供应商报名表';

-- 2. 投标书表
CREATE TABLE IF NOT EXISTS bid_tender (
    id                  BIGINT(20)     NOT NULL AUTO_INCREMENT COMMENT '主键',
    project_id          BIGINT(20)     NOT NULL                COMMENT '关联招标项目ID',
    registration_id     BIGINT(20)                             COMMENT '关联报名ID',
    vendor_name         VARCHAR(200)                           COMMENT '投标人名称',
    total_price         DECIMAL(14,2)                          COMMENT '总报价(万元)',
    include_tax         TINYINT        NOT NULL DEFAULT 1      COMMENT '是否含税 1是 0否',
    tech_file_path      VARCHAR(500)                           COMMENT '技术标文件路径',
    commercial_file_path VARCHAR(500)                          COMMENT '商务标文件路径',
    price_file_path     VARCHAR(500)                           COMMENT '报价文件路径',
    remark              VARCHAR(500)                           COMMENT '备注',
    status              TINYINT        NOT NULL DEFAULT 0      COMMENT '状态 0草稿 1已提交 2已开标 3资格审查中 4通过 5淘汰',
    user_id             BIGINT(20)                             COMMENT '投标用户ID',
    create_time         DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP                 COMMENT '创建时间',
    update_time         DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by           VARCHAR(100)                           COMMENT '创建人',
    update_by           VARCHAR(100)                           COMMENT '更新人',
    deleted             TINYINT        NOT NULL DEFAULT 0      COMMENT '是否删除1-是、0-否',
    PRIMARY KEY (id),
    KEY idx_project_id (project_id),
    KEY idx_user_id (user_id),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='投标书表';

-- 3. 评标专家表
CREATE TABLE IF NOT EXISTS bid_expert (
    id              BIGINT(20)   NOT NULL AUTO_INCREMENT COMMENT '主键',
    name            VARCHAR(100) NOT NULL                COMMENT '姓名',
    specialty       VARCHAR(200)                         COMMENT '专业方向',
    title           VARCHAR(100)                         COMMENT '职称',
    organization    VARCHAR(200)                         COMMENT '单位',
    phone           VARCHAR(50)                          COMMENT '联系电话',
    email           VARCHAR(100)                         COMMENT '邮箱',
    remark          VARCHAR(500)                         COMMENT '备注',
    blacklisted     TINYINT      NOT NULL DEFAULT 0      COMMENT '是否黑名单 0否 1是',
    blacklist_reason VARCHAR(500)                        COMMENT '加入黑名单原因',
    create_time     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP                 COMMENT '创建时间',
    update_time     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by       VARCHAR(100)                         COMMENT '创建人',
    update_by       VARCHAR(100)                         COMMENT '更新人',
    deleted         TINYINT      NOT NULL DEFAULT 0      COMMENT '是否删除1-是、0-否',
    PRIMARY KEY (id),
    KEY idx_name (name),
    KEY idx_blacklisted (blacklisted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评标专家表';

-- 4. 评标记录表
CREATE TABLE IF NOT EXISTS bid_evaluation (
    id              BIGINT(20)     NOT NULL AUTO_INCREMENT COMMENT '主键',
    project_id      BIGINT(20)     NOT NULL                COMMENT '关联招标项目ID',
    tender_id       BIGINT(20)     NOT NULL                COMMENT '关联投标书ID',
    expert_id       BIGINT(20)     NOT NULL                COMMENT '评标专家ID',
    total_score     DECIMAL(6,1)                           COMMENT '总分',
    comment         TEXT                                   COMMENT '综合评价',
    items           TEXT                                   COMMENT '评分明细(JSON)',
    submitted       TINYINT        NOT NULL DEFAULT 0      COMMENT '是否已提交 0否 1是',
    create_time     DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP                 COMMENT '创建时间',
    update_time     DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by       VARCHAR(100)                           COMMENT '创建人',
    update_by       VARCHAR(100)                           COMMENT '更新人',
    deleted         TINYINT        NOT NULL DEFAULT 0      COMMENT '是否删除1-是、0-否',
    PRIMARY KEY (id),
    KEY idx_project_id (project_id),
    KEY idx_tender_id (tender_id),
    KEY idx_expert_id (expert_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评标记录表';

-- 5. 质疑投诉表
CREATE TABLE IF NOT EXISTS bid_complaint (
    id              BIGINT(20)   NOT NULL AUTO_INCREMENT COMMENT '主键',
    project_id      BIGINT(20)   NOT NULL                COMMENT '关联招标项目ID',
    project_name    VARCHAR(200)                         COMMENT '项目名称(冗余)',
    type            TINYINT      NOT NULL DEFAULT 1      COMMENT '类型 1质疑 2投诉',
    title           VARCHAR(200) NOT NULL                COMMENT '标题',
    content         TEXT         NOT NULL                COMMENT '质疑/投诉内容',
    status          TINYINT      NOT NULL DEFAULT 0      COMMENT '状态 0待处理 1处理中 2已回复 3已关闭 4已升级',
    reply_content   TEXT                                 COMMENT '回复内容',
    submitter_id    BIGINT(20)                           COMMENT '提交人ID',
    submitter_name  VARCHAR(100)                         COMMENT '提交人姓名',
    create_time     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP                 COMMENT '创建时间',
    update_time     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by       VARCHAR(100)                         COMMENT '创建人',
    update_by       VARCHAR(100)                         COMMENT '更新人',
    deleted         TINYINT      NOT NULL DEFAULT 0      COMMENT '是否删除1-是、0-否',
    PRIMARY KEY (id),
    KEY idx_project_id (project_id),
    KEY idx_type (type),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='质疑投诉表';

-- 6. 合同表
CREATE TABLE IF NOT EXISTS bid_contract (
    id              BIGINT(20)     NOT NULL AUTO_INCREMENT COMMENT '主键',
    contract_name   VARCHAR(200)   NOT NULL                COMMENT '合同名称',
    project_id      BIGINT(20)                             COMMENT '关联招标项目ID',
    project_name    VARCHAR(200)                           COMMENT '项目名称(冗余)',
    party_a         VARCHAR(200)                           COMMENT '甲方',
    party_b         VARCHAR(200)                           COMMENT '乙方',
    amount          DECIMAL(14,2)                          COMMENT '合同金额(万元)',
    start_date      DATE                                   COMMENT '合同开始日期',
    end_date        DATE                                   COMMENT '合同结束日期',
    content         TEXT                                   COMMENT '合同正文',
    status          TINYINT        NOT NULL DEFAULT 0      COMMENT '状态 0草稿 1待签署 2已签署 3履行中 4已完结 5已解除',
    sign_date       DATE                                   COMMENT '签署日期',
    create_time     DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP                 COMMENT '创建时间',
    update_time     DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    create_by       VARCHAR(100)                           COMMENT '创建人',
    update_by       VARCHAR(100)                           COMMENT '更新人',
    deleted         TINYINT        NOT NULL DEFAULT 0      COMMENT '是否删除1-是、0-否',
    PRIMARY KEY (id),
    KEY idx_project_id (project_id),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='合同表';
