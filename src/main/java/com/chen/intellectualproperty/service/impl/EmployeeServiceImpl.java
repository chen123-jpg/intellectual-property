package com.chen.intellectualproperty.service.impl;

import com.chen.intellectualproperty.mapper.EmployeeMapper;
import com.chen.intellectualproperty.mapper.EmployeeTypeMapper;
import com.chen.intellectualproperty.mapper.EmployeeTypeRelMapper;
import com.chen.intellectualproperty.model.dto.EmployeeSaveDTO;
import com.chen.intellectualproperty.model.dto.EmployeeTypeSaveDTO;
import com.chen.intellectualproperty.model.entity.Employee;
import com.chen.intellectualproperty.model.entity.EmployeeType;
import com.chen.intellectualproperty.model.entity.EmployeeTypeRel;
import com.chen.intellectualproperty.model.enums.EmployeeRoleCode;
import com.chen.intellectualproperty.model.vo.SessionUserVO;
import com.chen.intellectualproperty.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeMapper employeeMapper;
    private final EmployeeTypeMapper employeeTypeMapper;
    private final EmployeeTypeRelMapper employeeTypeRelMapper;

    @Override
    public List<EmployeeType> listTypes(boolean onlyEnabled) {
        return onlyEnabled ? employeeTypeMapper.selectAllEnabled() : employeeTypeMapper.selectAll();
    }

    @Override
    @Transactional
    public EmployeeType saveType(EmployeeTypeSaveDTO dto) {
        if (dto == null || !StringUtils.hasText(dto.getTypeCode()) || !StringUtils.hasText(dto.getTypeName())) {
            throw new IllegalArgumentException("类型编码和名称不能为空");
        }
        String code = dto.getTypeCode().trim().toUpperCase(Locale.ROOT);
        if (dto.getId() == null) {
            if (employeeTypeMapper.selectByCode(code) != null) {
                throw new IllegalArgumentException("类型编码已存在: " + code);
            }
            EmployeeType t = new EmployeeType();
            t.setTypeCode(code);
            t.setTypeName(dto.getTypeName().trim());
            t.setDescription(dto.getDescription());
            t.setNeedLogin(dto.getNeedLogin() == null ? 1 : dto.getNeedLogin());
            t.setIsSystem(0);
            t.setEnabled(dto.getEnabled() == null ? 1 : dto.getEnabled());
            t.setSortNo(dto.getSortNo() == null ? 100 : dto.getSortNo());
            employeeTypeMapper.insert(t);
            return employeeTypeMapper.selectById(t.getId());
        }
        EmployeeType exist = employeeTypeMapper.selectById(dto.getId());
        if (exist == null) {
            throw new IllegalArgumentException("类型不存在");
        }
        EmployeeType patch = new EmployeeType();
        patch.setId(dto.getId());
        patch.setTypeName(dto.getTypeName().trim());
        patch.setDescription(dto.getDescription());
        patch.setNeedLogin(dto.getNeedLogin());
        patch.setEnabled(dto.getEnabled());
        patch.setSortNo(dto.getSortNo());
        employeeTypeMapper.updateById(patch);
        return employeeTypeMapper.selectById(dto.getId());
    }

    @Override
    public List<Employee> listEmployees(String keyword, String status, String typeCode) {
        List<Employee> list = employeeMapper.selectList(keyword, status, typeCode);
        for (Employee e : list) {
            fillTypeInfo(e);
        }
        return list;
    }

    @Override
    public Employee getById(Long id) {
        Employee e = employeeMapper.selectById(id);
        if (e != null) {
            fillTypeInfo(e);
        }
        return e;
    }

    @Override
    public Employee getByUserId(Integer userId) {
        if (userId == null) {
            return null;
        }
        Employee e = employeeMapper.selectByUserId(userId);
        if (e != null) {
            fillTypeInfo(e);
        }
        return e;
    }

    @Override
    @Transactional
    public Employee saveEmployee(EmployeeSaveDTO dto) {
        if (dto == null || !StringUtils.hasText(dto.getName())) {
            throw new IllegalArgumentException("员工姓名不能为空");
        }
        List<String> codes = normalizeTypeCodes(dto.getTypeCodes());
        if (codes.isEmpty()) {
            throw new IllegalArgumentException("至少选择一种员工类型");
        }
        validateTypeCodesExist(codes);

        if (dto.getId() == null) {
            if (dto.getUserId() != null && employeeMapper.selectByUserId(dto.getUserId()) != null) {
                throw new IllegalArgumentException("该登录账号已绑定其他员工");
            }
            Employee e = new Employee();
            e.setUserId(dto.getUserId());
            e.setEmpNo(dto.getEmpNo());
            e.setName(dto.getName().trim());
            e.setEmail(dto.getEmail());
            e.setPhone(dto.getPhone());
            e.setDepartment(dto.getDepartment());
            e.setStatus(StringUtils.hasText(dto.getStatus()) ? dto.getStatus() : "ACTIVE");
            e.setRemark(dto.getRemark());
            employeeMapper.insert(e);
            replaceTypes(e.getId(), codes);
            return getById(e.getId());
        }

        Employee exist = employeeMapper.selectById(dto.getId());
        if (exist == null) {
            throw new IllegalArgumentException("员工不存在");
        }
        if (dto.getUserId() != null) {
            Employee byUser = employeeMapper.selectByUserId(dto.getUserId());
            if (byUser != null && !byUser.getId().equals(dto.getId())) {
                throw new IllegalArgumentException("该登录账号已绑定其他员工");
            }
        }
        Employee patch = new Employee();
        patch.setId(dto.getId());
        patch.setUserId(dto.getUserId());
        patch.setEmpNo(dto.getEmpNo());
        patch.setName(dto.getName().trim());
        patch.setEmail(dto.getEmail());
        patch.setPhone(dto.getPhone());
        patch.setDepartment(dto.getDepartment());
        patch.setStatus(dto.getStatus());
        patch.setRemark(dto.getRemark());
        employeeMapper.updateById(patch);
        replaceTypes(dto.getId(), codes);
        return getById(dto.getId());
    }

    @Override
    public List<Employee> listActiveByType(String typeCode) {
        if (!StringUtils.hasText(typeCode)) {
            return Collections.emptyList();
        }
        List<Employee> list = employeeMapper.selectByTypeCode(typeCode.trim().toUpperCase(Locale.ROOT));
        for (Employee e : list) {
            fillTypeInfo(e);
        }
        return list;
    }

    @Override
    @Transactional
    public Employee ensureEmployeeForUser(Integer userId, String name, String email, List<String> typeCodes) {
        if (userId == null) {
            throw new IllegalArgumentException("userId 不能为空");
        }
        Employee exist = employeeMapper.selectByUserId(userId);
        if (exist != null) {
            fillTypeInfo(exist);
            // 若已有档案但无任何类型，补默认
            if (exist.getTypeCodes() == null || exist.getTypeCodes().isEmpty()) {
                List<String> codes = normalizeTypeCodes(typeCodes);
                if (codes.isEmpty()) {
                    codes = List.of(EmployeeRoleCode.ENTRY);
                }
                validateTypeCodesExist(codes);
                replaceTypes(exist.getId(), codes);
                fillTypeInfo(exist);
            }
            return exist;
        }
        List<String> codes = normalizeTypeCodes(typeCodes);
        if (codes.isEmpty()) {
            codes = List.of(EmployeeRoleCode.ENTRY);
        }
        validateTypeCodesExist(codes);

        Employee e = new Employee();
        e.setUserId(userId);
        e.setName(StringUtils.hasText(name) ? name.trim() : (email != null ? email : "员工" + userId));
        e.setEmail(email);
        e.setStatus("ACTIVE");
        employeeMapper.insert(e);
        replaceTypes(e.getId(), codes);
        return getById(e.getId());
    }

    @Override
    public SessionUserVO buildSession(Integer userId, String email, String nickName, String token) {
        SessionUserVO vo = new SessionUserVO();
        vo.setUserId(userId);
        vo.setEmail(email);
        vo.setNickName(nickName);
        vo.setToken(token);

        Employee emp = getByUserId(userId);
        if (emp != null) {
            vo.setEmployeeId(emp.getId());
            vo.setEmployeeName(emp.getName());
            vo.setEmpNo(emp.getEmpNo());
            vo.setDepartment(emp.getDepartment());
            vo.setEmployeeStatus(emp.getStatus());
            vo.setRoles(emp.getTypeCodes() != null ? emp.getTypeCodes() : new ArrayList<>());
            vo.setRoleNames(emp.getTypeNames() != null ? emp.getTypeNames() : new ArrayList<>());
        } else {
            vo.setRoles(new ArrayList<>());
            vo.setRoleNames(new ArrayList<>());
        }
        return vo;
    }

    @Override
    public void fillTypeInfo(Employee employee) {
        if (employee == null || employee.getId() == null) {
            return;
        }
        List<String> codes = employeeTypeRelMapper.selectTypeCodesByEmployeeId(employee.getId());
        employee.setTypeCodes(codes != null ? codes : new ArrayList<>());
        if (codes == null || codes.isEmpty()) {
            employee.setTypeNames(new ArrayList<>());
            return;
        }
        List<EmployeeType> types = employeeTypeMapper.selectByCodes(codes);
        employee.setTypeNames(types.stream().map(EmployeeType::getTypeName).collect(Collectors.toList()));
    }

    private void replaceTypes(Long employeeId, List<String> codes) {
        employeeTypeRelMapper.deleteByEmployeeId(employeeId);
        for (String code : codes) {
            EmployeeTypeRel rel = new EmployeeTypeRel();
            rel.setEmployeeId(employeeId);
            rel.setTypeCode(code);
            employeeTypeRelMapper.insert(rel);
        }
    }

    private List<String> normalizeTypeCodes(List<String> raw) {
        if (raw == null || raw.isEmpty()) {
            return new ArrayList<>();
        }
        return raw.stream()
                .filter(StringUtils::hasText)
                .map(s -> s.trim().toUpperCase(Locale.ROOT))
                .distinct()
                .collect(Collectors.toList());
    }

    private void validateTypeCodesExist(List<String> codes) {
        for (String code : codes) {
            EmployeeType t = employeeTypeMapper.selectByCode(code);
            if (t == null || t.getEnabled() == null || t.getEnabled() != 1) {
                throw new IllegalArgumentException("员工类型不存在或未启用: " + code);
            }
        }
    }
}
