package com.chen.intellectualproperty.service.impl;

import com.chen.intellectualproperty.service.GenerateNoService;
import com.chen.intellectualproperty.service.PatentNewApplicationService;
import com.chen.intellectualproperty.mapper.PatentNewApplicationMapper;
import com.chen.intellectualproperty.entity.PatentNewApplication;
import com.chen.intellectualproperty.dto.PatentNewApplicationDTO;
import com.chen.intellectualproperty.query.PatentNewApplicationQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 专利新申请表 ServiceImpl
 *
 * @author 
 */
@Service
public class PatentNewApplicationServiceImpl implements PatentNewApplicationService {

    @Autowired
    private PatentNewApplicationMapper patentNewApplicationMapper;

    // 注入编号生成工具Service
    @Autowired
    private GenerateNoService generateNoService;

    @Override
    public int insert(PatentNewApplication record) {
        // 自动生成内部编号，覆盖前端传入值
        String internalNo = generateNoService.generateNo("patent_new_application", "P");
        record.setInternalNo(internalNo);
        return patentNewApplicationMapper.insert(record);
    }

    @Override
    public int insertOrUpdate(PatentNewApplication record) {
        return patentNewApplicationMapper.insertOrUpdate(record);
    }

    @Override
    public int deleteById(Long id) {
        return patentNewApplicationMapper.deleteById(id);
    }

    @Override
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
        return patentNewApplicationMapper.findListByParam(param);
    }

    @Override
    public List<PatentNewApplication> findListByParams(Map<String, Object> params) {
        return patentNewApplicationMapper.findListByParams(params);
    }

    @Override
    public long selectCount(PatentNewApplicationQuery param) {
        return patentNewApplicationMapper.selectCount(param);
    }

}
