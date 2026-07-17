package com.chen.intellectualproperty.mapper;

import com.chen.intellectualproperty.model.entity.Invoice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface InvoiceMapper {
    int insert(Invoice record);
    int updateById(Invoice record);
    Invoice selectById(@Param("id") Long id);
    List<Invoice> selectByDisclosureId(@Param("disclosureId") Long disclosureId);
}
