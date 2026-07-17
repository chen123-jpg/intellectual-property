package com.chen.intellectualproperty.mapper;

import com.chen.intellectualproperty.model.entity.DisclosureStatusLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DisclosureStatusLogMapper {
    int insert(DisclosureStatusLog record);
    List<DisclosureStatusLog> selectByDisclosureId(@Param("disclosureId") Long disclosureId);
}
