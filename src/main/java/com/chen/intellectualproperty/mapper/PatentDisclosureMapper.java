package com.chen.intellectualproperty.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.chen.intellectualproperty.model.entity.PatentDisclosure;
import com.chen.intellectualproperty.model.query.PatentDisclosureQuery;

/**
 * 专利交底信息表（T表） Mapper
 * <p>继承BaseMapper，E=PatentDisclosure（返回结果），Q=PatentDisclosureQuery（查询参数）</p>
 *
 * @author 
 */
@Mapper
public interface PatentDisclosureMapper extends BaseMapper<PatentDisclosure, PatentDisclosureQuery> {

    /**
     * 根据主键删除
     */
    int deleteById(Long id);

    /**
     * 更新记录
     */
    int updateById(PatentDisclosure record);

    /**
     * 插入或更新记录
     */
    int insertOrUpdate(PatentDisclosure record);

    /**
     * 根据主键查询
     */
    PatentDisclosure selectById(Long id);

}
