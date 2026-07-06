package com.chen.intellectualproperty.service.impl;

import com.chen.intellectualproperty.service.PatentIntermediateChangeService;
import com.chen.intellectualproperty.mapper.PatentIntermediateChangeMapper;
import com.chen.intellectualproperty.entity.PatentIntermediateChange;
import com.chen.intellectualproperty.dto.PatentIntermediateChangeDTO;
import com.chen.intellectualproperty.query.PatentIntermediateChangeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 中间著变专利表（有重复） ServiceImpl
 *
 * @author 
 */
@Service
public class PatentIntermediateChangeServiceImpl implements PatentIntermediateChangeService {

    @Autowired
    private PatentIntermediateChangeMapper patentIntermediateChangeMapper;

    @Override
    public int insert(PatentIntermediateChange record) {
        return patentIntermediateChangeMapper.insert(record);
    }

    @Override
    public int insertOrUpdate(PatentIntermediateChange record) {
        return patentIntermediateChangeMapper.insertOrUpdate(record);
    }

    @Override
    public int deleteById(Long id) {
        return patentIntermediateChangeMapper.deleteById(id);
    }

    @Override
    public int updateById(PatentIntermediateChange record) {
        return patentIntermediateChangeMapper.updateById(record);
    }

    @Override
    public PatentIntermediateChange selectById(Long id) {
        return patentIntermediateChangeMapper.selectById(id);
    }

    @Override
    public List<PatentIntermediateChange> selectAll() {
        return patentIntermediateChangeMapper.selectAll();
    }

    @Override
    public List<PatentIntermediateChange> selectPage(int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        return patentIntermediateChangeMapper.selectPage(offset, pageSize);
    }

    @Override
    public long count() {
        return patentIntermediateChangeMapper.count();
    }

    @Override
    public List<PatentIntermediateChange> findListByParam(PatentIntermediateChangeQuery param) {
        return patentIntermediateChangeMapper.findListByParam(param);
    }

    @Override
    public List<PatentIntermediateChange> findListByParams(Map<String, Object> params) {
        return patentIntermediateChangeMapper.findListByParams(params);
    }

    @Override
    public long selectCount(PatentIntermediateChangeQuery param) {
        return patentIntermediateChangeMapper.selectCount(param);
    }

}
