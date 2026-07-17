package com.chen.intellectualproperty.mapper;

import com.chen.intellectualproperty.model.entity.FeePayment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FeePaymentMapper {
    int insert(FeePayment record);
    int updateById(FeePayment record);
    FeePayment selectById(@Param("id") Long id);
    List<FeePayment> selectByDisclosureId(@Param("disclosureId") Long disclosureId);
}
