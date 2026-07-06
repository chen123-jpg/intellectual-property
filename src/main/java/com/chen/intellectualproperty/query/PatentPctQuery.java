package com.chen.intellectualproperty.query;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;

/**
 * PCT国际申请表 查询对象
 *
 * @author 
 */
@Data
public class PatentPctQuery {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 序号
     */
    private Integer seqNo;

    /**
     * PCT内部编号
     */
    private String pctInternalNo;

    /**
     * 状态
     */
    private String status;

    /**
     * 发文日
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date issueDate;

    /**
     * 在先内部编号
     */
    private String priorInternalNo;

    /**
     * 在先申请号
     */
    private String priorApplicationNo;

    /**
     * 在先申请日
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date priorApplicationDate;

    /**
     * PCT申请日
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date pctApplicationDate;

    /**
     * 申请名称
     */
    private String applicationName;

    /**
     * 申请号（PCT号）
     */
    private String applicationNo;

    /**
     * 申请人
     */
    private String applicant;

    /**
     * 发明人
     */
    private String inventor;

    /**
     * 主办人
     */
    private String sponsor;

    /**
     * 委托书代理人
     */
    private String agent;

    /**
     * 初检结论
     */
    private String preliminaryConclusion;

    /**
     * 备注
     */
    private String remark;

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
