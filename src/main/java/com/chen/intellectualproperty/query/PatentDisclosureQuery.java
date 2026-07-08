package com.chen.intellectualproperty.query;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

/**
 * 专利交底信息表（T表） 查询对象
 *
 * @author 
 */
@Data
public class PatentDisclosureQuery {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 临时编号（如T250101）
     */
    private String tempNo;

    /**
     * 内部编号（与P表关联键）
     */
    private String internalNo;

    /**
     * 专利状态（如受理、N稿撰写中等）
     */
    private String patentStatus;

    /**
     * 要求（如一周内提交、追求授权等）
     */
    private String requirement;

    /**
     * 专利交底名称
     */
    private String disclosureName;

    /**
     * 申请人（可多个，用顿号或逗号分隔）
     */
    private String applicant;

    /**
     * 是否邀请进群（0-否，1-是）
     */
    private Boolean invitedToGroup;

    /**
     * 联系人姓名
     */
    private String contactPerson;

    /**
     * 管理人姓名
     */
    private String manager;

    /**
     * 代理人姓名
     */
    private String agent;

    /**
     * 交底日
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date disclosureDate;

    /**
     * 交底天数（可为空）
     */
    private Integer disclosureDays;

    /**
     * 备注
     */
    private String remark;

    /**
     * 联系人信息（含电话、邮箱、QQ等）
     */
    private String contactInfo;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 排序参数（例如："create_time DESC"、"name ASC, age DESC"）
     */
    private String orderBy;

}
