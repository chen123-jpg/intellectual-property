package com.chen.intellectualproperty.service.impl;

import com.chen.intellectualproperty.service.PatentReexaminationService;
import com.chen.intellectualproperty.mapper.PatentReexaminationMapper;
import com.chen.intellectualproperty.model.entity.PatentReexamination;
import com.chen.intellectualproperty.model.query.PatentReexaminationQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 复审无效专利表 ServiceImpl
 *
 * @author
 */
@Service
public class PatentReexaminationServiceImpl implements PatentReexaminationService {

    @Autowired
    private PatentReexaminationMapper patentReexaminationMapper;

    @Override
    @Transactional
    public int insert(PatentReexamination record) {
        return patentReexaminationMapper.insert(record);
    }

    @Override
    @Transactional
    public int insertOrUpdate(PatentReexamination record) {
        return patentReexaminationMapper.insertOrUpdate(record);
    }

    @Override
    @Transactional
    public int deleteById(Long id) {
        return patentReexaminationMapper.deleteById(id);
    }

    @Override
    @Transactional
    public int updateById(PatentReexamination record) {
        return patentReexaminationMapper.updateById(record);
    }

    @Override
    public PatentReexamination selectById(Long id) {
        return patentReexaminationMapper.selectById(id);
    }

    @Override
    public List<PatentReexamination> selectAll() {
        return patentReexaminationMapper.selectAll();
    }

    @Override
    public List<PatentReexamination> selectPage(int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        return patentReexaminationMapper.selectPage(offset, pageSize);
    }

    @Override
    public long count() {
        return patentReexaminationMapper.count();
    }

    @Override
    public List<PatentReexamination> findListByParam(PatentReexaminationQuery param) {
        return patentReexaminationMapper.findListByParam(param);
    }

    @Override
    public List<PatentReexamination> findListByParams(Map<String, Object> params) {
        return patentReexaminationMapper.findListByParams(params);
    }

    @Override
    public long selectCount(PatentReexaminationQuery param) {
        return patentReexaminationMapper.selectCount(param);
    }

}
