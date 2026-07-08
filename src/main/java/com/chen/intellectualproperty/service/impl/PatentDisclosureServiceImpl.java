package com.chen.intellectualproperty.service.impl;

import com.chen.intellectualproperty.service.GenerateNoService;
import com.chen.intellectualproperty.service.PatentDisclosureService;
import com.chen.intellectualproperty.mapper.PatentDisclosureMapper;
import com.chen.intellectualproperty.entity.PatentDisclosure;
import com.chen.intellectualproperty.dto.PatentDisclosureDTO;
import com.chen.intellectualproperty.query.PatentDisclosureQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 专利交底信息表（T表） ServiceImpl
 *
 * @author 
 */
@Service
public class PatentDisclosureServiceImpl implements PatentDisclosureService {

    @Autowired
    private PatentDisclosureMapper patentDisclosureMapper;

    @Autowired
    private GenerateNoService generateNoService;

    @Override
    public int insert(PatentDisclosure record) {
        String tempNo = generateNoService.generateNo("patent_disclosure","temp_no","T");
        String internalNo = generateNoService.generateNo("patent_disclosure","internal_no","P");
        return patentDisclosureMapper.insert(record);
    }

    @Override
    public int insertOrUpdate(PatentDisclosure record) {
        return patentDisclosureMapper.insertOrUpdate(record);
    }

    @Override
    public int deleteById(Long id) {
        return patentDisclosureMapper.deleteById(id);
    }

    @Override
    public int updateById(PatentDisclosure record) {
        return patentDisclosureMapper.updateById(record);
    }

    @Override
    public PatentDisclosure selectById(Long id) {
        return patentDisclosureMapper.selectById(id);
    }

    @Override
    public List<PatentDisclosure> selectAll() {
        return patentDisclosureMapper.selectAll();
    }

    @Override
    public List<PatentDisclosure> selectPage(int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        return patentDisclosureMapper.selectPage(offset, pageSize);
    }

    @Override
    public long count() {
        return patentDisclosureMapper.count();
    }

    @Override
    public List<PatentDisclosure> findListByParam(PatentDisclosureQuery param) {
        return patentDisclosureMapper.findListByParam(param);
    }

    @Override
    public List<PatentDisclosure> findListByParams(Map<String, Object> params) {
        return patentDisclosureMapper.findListByParams(params);
    }

    @Override
    public long selectCount(PatentDisclosureQuery param) {
        return patentDisclosureMapper.selectCount(param);
    }

}
