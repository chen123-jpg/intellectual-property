package com.chen.intellectualproperty.model.dto;

import com.chen.intellectualproperty.model.entity.PatentDisclosure;
import lombok.Data;

/**
 * 录入交底：主表字段 + 是否自动编号。
 * 附件通过独立上传接口绑定。
 */
@Data
public class DisclosureCreateDTO {
    private PatentDisclosure disclosure;
    /** true=自动生成 tempNo/internalNo；false=使用传入值 */
    private Boolean autoGenerateNo = true;
}
