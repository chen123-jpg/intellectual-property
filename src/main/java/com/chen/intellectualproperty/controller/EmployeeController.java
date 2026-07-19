package com.chen.intellectualproperty.controller;

import com.chen.intellectualproperty.exception.BusinessException;
import com.chen.intellectualproperty.model.dto.EmployeeSaveDTO;
import com.chen.intellectualproperty.model.dto.EmployeeTypeSaveDTO;
import com.chen.intellectualproperty.model.dto.Result;
import com.chen.intellectualproperty.model.entity.Employee;
import com.chen.intellectualproperty.model.entity.EmployeeType;
import com.chen.intellectualproperty.model.enums.EmployeeRoleCode;
import com.chen.intellectualproperty.model.vo.SessionUserVO;
import com.chen.intellectualproperty.service.AuthSessionService;
import com.chen.intellectualproperty.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 员工档案与类型字典。
 */
@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    private final AuthSessionService authSessionService;

    /** 启用的类型字典（注册/表单用，登录后即可） */
    @GetMapping("/types")
    public Result<List<EmployeeType>> types(@RequestParam(required = false) String token,
                                            @RequestParam(defaultValue = "true") boolean onlyEnabled) {
        try {
            if (token != null && !token.isBlank()) {
                authSessionService.requireSession(token);
            }
            return Result.success(employeeService.listTypes(onlyEnabled));
        } catch (BusinessException e) {
            return Result.fail(e.getMessage());
        }
    }

    /** 新增/修改类型（管理员） */
    @PostMapping("/types")
    public Result<EmployeeType> saveType(@RequestParam String token, @RequestBody EmployeeTypeSaveDTO dto) {
        try {
            authSessionService.requireAnyRole(token, EmployeeRoleCode.ADMIN);
            return Result.success(employeeService.saveType(dto));
        } catch (BusinessException | IllegalArgumentException e) {
            return Result.fail(e.getMessage());
        }
    }

    /** 员工列表 */
    @GetMapping
    public Result<List<Employee>> list(@RequestParam String token,
                                       @RequestParam(required = false) String keyword,
                                       @RequestParam(required = false) String status,
                                       @RequestParam(required = false) String typeCode) {
        try {
            // 录入/流程/管理可看列表；主办也可看主办人下拉
            authSessionService.requireAnyRole(token,
                    EmployeeRoleCode.ADMIN, EmployeeRoleCode.ENTRY,
                    EmployeeRoleCode.PROCESS, EmployeeRoleCode.SPONSOR);
            return Result.success(employeeService.listEmployees(keyword, status, typeCode));
        } catch (BusinessException e) {
            return Result.fail(e.getMessage());
        }
    }

    /** 按类型查启用员工（下拉：主办人等） */
    @GetMapping("/by-type/{typeCode}")
    public Result<List<Employee>> byType(@RequestParam String token, @PathVariable String typeCode) {
        try {
            authSessionService.requireAnyRole(token,
                    EmployeeRoleCode.ADMIN, EmployeeRoleCode.ENTRY,
                    EmployeeRoleCode.PROCESS, EmployeeRoleCode.SPONSOR);
            return Result.success(employeeService.listActiveByType(typeCode));
        } catch (BusinessException e) {
            return Result.fail(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Result<Employee> detail(@RequestParam String token, @PathVariable Long id) {
        try {
            authSessionService.requireAnyRole(token,
                    EmployeeRoleCode.ADMIN, EmployeeRoleCode.ENTRY, EmployeeRoleCode.PROCESS, EmployeeRoleCode.SPONSOR);
            Employee e = employeeService.getById(id);
            if (e == null) {
                return Result.fail("员工不存在");
            }
            return Result.success(e);
        } catch (BusinessException e) {
            return Result.fail(e.getMessage());
        }
    }

    /** 新建/更新员工（管理员；或本人信息有限——当前仅管理员全量维护） */
    @PostMapping
    public Result<Employee> save(@RequestParam String token, @RequestBody EmployeeSaveDTO dto) {
        try {
            authSessionService.requireAnyRole(token, EmployeeRoleCode.ADMIN);
            return Result.success(employeeService.saveEmployee(dto));
        } catch (BusinessException | IllegalArgumentException e) {
            return Result.fail(e.getMessage());
        }
    }

    /** 当前登录人的员工视图 */
    @GetMapping("/me")
    public Result<SessionUserVO> me(@RequestParam String token) {
        try {
            return Result.success(authSessionService.requireSession(token));
        } catch (BusinessException e) {
            return Result.fail(e.getMessage());
        }
    }
}
