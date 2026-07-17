package com.chen.intellectualproperty.mapper;

import com.chen.intellectualproperty.model.entity.MailSendAttachment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MailSendAttachmentMapper {
    int insert(MailSendAttachment record);
    List<MailSendAttachment> selectByLogId(@Param("mailSendLogId") Long mailSendLogId);
}
