package com.chen.intellectualproperty.service;

import com.chen.intellectualproperty.model.entity.Applicant;
import java.util.List;

public interface ApplicantService {

    int insert(Applicant record);

    int deleteById(Long id);

    int updateById(Applicant record);

    Applicant selectById(Long id);

    List<Applicant> selectAll();

}
