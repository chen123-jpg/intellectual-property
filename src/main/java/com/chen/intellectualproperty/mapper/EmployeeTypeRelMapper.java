package com.chen.intellectualproperty.mapper;

import com.chen.intellectualproperty.model.entity.EmployeeTypeRel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EmployeeTypeRelMapper {
    int insert(EmployeeTypeRel record);

    int deleteByEmployeeId(@Param("employeeId") Long employeeId);

    List<String> selectTypeCodesByEmployeeId(@Param("employeeId") Long employeeId);

    List<EmployeeTypeRel> selectByEmployeeId(@Param("employeeId") Long employeeId);
}
