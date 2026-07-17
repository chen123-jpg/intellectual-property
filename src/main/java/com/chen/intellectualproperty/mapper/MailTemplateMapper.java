package com.chen.intellectualproperty.mapper;

import com.chen.intellectualproperty.model.entity.MailTemplate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MailTemplateMapper {
    int insert(MailTemplate record);
    int updateById(MailTemplate record);
    MailTemplate selectById(@Param("id") Long id);
    MailTemplate selectByCode(@Param("templateCode") String templateCode);
    List<MailTemplate> selectEnabled();
    List<MailTemplate> selectAll();
}
