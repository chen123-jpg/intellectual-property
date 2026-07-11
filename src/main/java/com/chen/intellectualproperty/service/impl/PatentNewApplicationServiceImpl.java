package com.chen.intellectualproperty.service.impl;

import com.chen.intellectualproperty.service.GenerateNoService;
import com.chen.intellectualproperty.service.PatentNewApplicationService;
import com.chen.intellectualproperty.mapper.PatentNewApplicationMapper;
import com.chen.intellectualproperty.model.entity.PatentNewApplication;
import com.chen.intellectualproperty.model.query.PatentNewApplicationQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * 专利新申请表 ServiceImpl
 *
 * @author
 */
@Service
public class PatentNewApplicationServiceImpl implements PatentNewApplicationService {

    private static final Set<String> ORDER_BY_WHITE_LIST = Collections.unmodifiableSet(Set.of(
            "id desc",
            "id asc",
            "seq_no desc",
            "seq_no asc",
            "create_time desc",
            "create_time asc",
            "update_time desc",
            "update_time asc"
    ));

    @Autowired
    private PatentNewApplicationMapper patentNewApplicationMapper;

    // 注入编号生成工具Service
    @Autowired
    private GenerateNoService generateNoService;

    @Override
    @Transactional
    public int insert(PatentNewApplication record) {
        if(record.getInternalNo() == null || record.getInternalNo().isEmpty()){
            // 自动生成内部编号，覆盖前端传入值
            String internalNo = generateNoService.generateNo("patent_new_application","internal_no", "P");
            record.setInternalNo(internalNo);
        }
        return patentNewApplicationMapper.insert(record);
    }

    @Override
    @Transactional
    public int insertOrUpdate(PatentNewApplication record) {
        return patentNewApplicationMapper.insertOrUpdate(record);
    }

    @Override
    @Transactional
    public int deleteById(Long id) {
        return patentNewApplicationMapper.deleteById(id);
    }

    @Override
    @Transactional
    public int updateById(PatentNewApplication record) {
        return patentNewApplicationMapper.updateById(record);
    }

    @Override
    public PatentNewApplication selectById(Long id) {
        return patentNewApplicationMapper.selectById(id);
    }

    @Override
    public List<PatentNewApplication> selectAll() {
        return patentNewApplicationMapper.selectAll();
    }

    @Override
    public List<PatentNewApplication> selectPage(int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        return patentNewApplicationMapper.selectPage(offset, pageSize);
    }

    @Override
    public long count() {
        return patentNewApplicationMapper.count();
    }

    @Override
    public List<PatentNewApplication> findListByParam(PatentNewApplicationQuery param) {
        sanitizeOrderBy(param);
        return patentNewApplicationMapper.findListByParam(param);
    }

    @Override
    public long selectCount(PatentNewApplicationQuery param) {
        return patentNewApplicationMapper.selectCount(param);
    }

    private void sanitizeOrderBy(PatentNewApplicationQuery param) {
        if (param == null || param.getOrderBy() == null) {
            return;
        }
        String normalized = param.getOrderBy().trim().toLowerCase();
        if (!ORDER_BY_WHITE_LIST.contains(normalized)) {
            param.setOrderBy(null);
            return;
        }
        param.setOrderBy(normalized);
    }

}
