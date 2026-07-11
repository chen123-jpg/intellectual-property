package com.chen.intellectualproperty.mapper;

import com.chen.intellectualproperty.model.entity.Applicant;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ApplicantMapper {

    int insert(Applicant record);

    int deleteById(Long id);

    int updateById(Applicant record);

    Applicant selectById(Long id);

    List<Applicant> selectAll();

    List<Applicant> selectByName(String name);

}
