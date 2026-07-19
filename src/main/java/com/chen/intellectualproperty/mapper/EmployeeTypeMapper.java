package com.chen.intellectualproperty.mapper;

import com.chen.intellectualproperty.model.entity.EmployeeType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EmployeeTypeMapper {
    int insert(EmployeeType record);

    int updateById(EmployeeType record);

    EmployeeType selectById(@Param("id") Long id);

    EmployeeType selectByCode(@Param("typeCode") String typeCode);

    List<EmployeeType> selectAllEnabled();

    List<EmployeeType> selectAll();

    List<EmployeeType> selectByCodes(@Param("codes") List<String> codes);
}
