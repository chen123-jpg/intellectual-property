package com.chen.intellectualproperty.mapper;

import com.chen.intellectualproperty.model.entity.DisclosureAttachment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DisclosureAttachmentMapper {
    int insert(DisclosureAttachment record);
    int logicalDelete(@Param("id") Long id);
    DisclosureAttachment selectById(@Param("id") Long id);
    List<DisclosureAttachment> selectByDisclosureId(@Param("disclosureId") Long disclosureId);
    List<DisclosureAttachment> selectByIds(@Param("ids") List<Long> ids);
}
