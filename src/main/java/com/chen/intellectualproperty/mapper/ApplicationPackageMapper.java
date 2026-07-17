package com.chen.intellectualproperty.mapper;

import com.chen.intellectualproperty.model.entity.ApplicationPackage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ApplicationPackageMapper {
    int insert(ApplicationPackage record);
    int updateById(ApplicationPackage record);
    int markNotCurrent(@Param("disclosureId") Long disclosureId, @Param("packageType") String packageType);
    ApplicationPackage selectById(@Param("id") Long id);
    ApplicationPackage selectCurrent(@Param("disclosureId") Long disclosureId, @Param("packageType") String packageType);
    List<ApplicationPackage> selectCurrentByDisclosureId(@Param("disclosureId") Long disclosureId);
    List<ApplicationPackage> selectByDisclosureId(@Param("disclosureId") Long disclosureId);
}
