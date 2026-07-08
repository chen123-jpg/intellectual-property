package com.chen.intellectualproperty.service.impl;

import com.chen.intellectualproperty.service.PatentSupplementaryService;
import com.chen.intellectualproperty.mapper.PatentSupplementaryMapper;
import com.chen.intellectualproperty.entity.PatentSupplementary;
import com.chen.intellectualproperty.dto.PatentSupplementaryDTO;
import com.chen.intellectualproperty.query.PatentSupplementaryQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 补漏专利表 ServiceImpl
 *
 * @author
 */
@Service
public class PatentSupplementaryServiceImpl implements PatentSupplementaryService {

    @Autowired
    private PatentSupplementaryMapper patentSupplementaryMapper;

    @Override
    @Transactional
    public int insert(PatentSupplementary record) {
        return patentSupplementaryMapper.insert(record);
    }

    @Override
    @Transactional
    public int insertOrUpdate(PatentSupplementary record) {
        return patentSupplementaryMapper.insertOrUpdate(record);
    }

    @Override
    @Transactional
    public int deleteById(Long id) {
        return patentSupplementaryMapper.deleteById(id);
    }

    @Override
    @Transactional
    public int updateById(PatentSupplementary record) {
        return patentSupplementaryMapper.updateById(record);
    }

    @Override
    public PatentSupplementary selectById(Long id) {
        return patentSupplementaryMapper.selectById(id);
    }

    @Override
    public List<PatentSupplementary> selectAll() {
        return patentSupplementaryMapper.selectAll();
    }

    @Override
    public List<PatentSupplementary> selectPage(int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        return patentSupplementaryMapper.selectPage(offset, pageSize);
    }

    @Override
    public long count() {
        return patentSupplementaryMapper.count();
    }

    @Override
    public List<PatentSupplementary> findListByParam(PatentSupplementaryQuery param) {
        return patentSupplementaryMapper.findListByParam(param);
    }

    @Override
    public List<PatentSupplementary> findListByParams(Map<String, Object> params) {
        return patentSupplementaryMapper.findListByParams(params);
    }

    @Override
    public long selectCount(PatentSupplementaryQuery param) {
        return patentSupplementaryMapper.selectCount(param);
    }

}
