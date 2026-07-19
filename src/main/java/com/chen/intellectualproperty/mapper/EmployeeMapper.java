package com.chen.intellectualproperty.mapper;

import com.chen.intellectualproperty.model.entity.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EmployeeMapper {
    int insert(Employee record);

    int updateById(Employee record);

    Employee selectById(@Param("id") Long id);

    Employee selectByUserId(@Param("userId") Integer userId);

    Employee selectByEmail(@Param("email") String email);

    List<Employee> selectList(@Param("keyword") String keyword,
                              @Param("status") String status,
                              @Param("typeCode") String typeCode);

    List<Employee> selectByTypeCode(@Param("typeCode") String typeCode);
}
