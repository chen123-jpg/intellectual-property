package com.chen.intellectualproperty.service.impl;

import com.chen.intellectualproperty.mapper.ApplicantMapper;
import com.chen.intellectualproperty.model.entity.Applicant;
import com.chen.intellectualproperty.service.ApplicantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ApplicantServiceImpl implements ApplicantService {

    @Autowired
    private ApplicantMapper applicantMapper;

    @Override
    @Transactional
    public int insert(Applicant record) {
        return applicantMapper.insert(record);
    }

    @Override
    @Transactional
    public int deleteById(Long id) {
        return applicantMapper.deleteById(id);
    }

    @Override
    @Transactional
    public int updateById(Applicant record) {
        return applicantMapper.updateById(record);
    }

    @Override
    public Applicant selectById(Long id) {
        return applicantMapper.selectById(id);
    }

    @Override
    public List<Applicant> selectAll() {
        return applicantMapper.selectAll();
    }

}
