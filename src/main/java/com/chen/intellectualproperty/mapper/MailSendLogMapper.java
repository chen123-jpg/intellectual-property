package com.chen.intellectualproperty.mapper;

import com.chen.intellectualproperty.model.entity.MailSendLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MailSendLogMapper {
    int insert(MailSendLog record);
    int updateById(MailSendLog record);
    MailSendLog selectById(@Param("id") Long id);
    List<MailSendLog> selectByDisclosureId(@Param("disclosureId") Long disclosureId);
}
