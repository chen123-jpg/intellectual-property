package com.chen.intellectualproperty.service;

import com.chen.intellectualproperty.model.dto.EmployeeSaveDTO;
import com.chen.intellectualproperty.model.dto.EmployeeTypeSaveDTO;
import com.chen.intellectualproperty.model.entity.Employee;
import com.chen.intellectualproperty.model.entity.EmployeeType;
import com.chen.intellectualproperty.model.vo.SessionUserVO;

import java.util.List;

public interface EmployeeService {

    List<EmployeeType> listTypes(boolean onlyEnabled);

    EmployeeType saveType(EmployeeTypeSaveDTO dto);

    List<Employee> listEmployees(String keyword, String status, String typeCode);

    Employee getById(Long id);

    Employee getByUserId(Integer userId);

    Employee saveEmployee(EmployeeSaveDTO dto);

    /** 按类型查启用员工（如下拉选主办人） */
    List<Employee> listActiveByType(String typeCode);

    /**
     * 注册/登录后确保有员工档案；无则按角色创建。
     */
    Employee ensureEmployeeForUser(Integer userId, String name, String email, List<String> typeCodes);

    SessionUserVO buildSession(Integer userId, String email, String nickName, String token);

    void fillTypeInfo(Employee employee);
}
