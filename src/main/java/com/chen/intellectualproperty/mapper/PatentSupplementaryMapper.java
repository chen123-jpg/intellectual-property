package com.chen.intellectualproperty.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.chen.intellectualproperty.model.entity.PatentSupplementary;
import com.chen.intellectualproperty.model.query.PatentSupplementaryQuery;

/**
 * 补漏专利表 Mapper
 * <p>继承BaseMapper，E=PatentSupplementary（返回结果），Q=PatentSupplementaryQuery（查询参数）</p>
 *
 * @author 
 */
@Mapper
public interface PatentSupplementaryMapper extends BaseMapper<PatentSupplementary, PatentSupplementaryQuery> {

    /**
     * 根据主键删除
     */
    int deleteById(Long id);

    /**
     * 更新记录
     */
    int updateById(PatentSupplementary record);

    /**
     * 插入或更新记录
     */
    int insertOrUpdate(PatentSupplementary record);

    /**
     * 根据主键查询
     */
    PatentSupplementary selectById(Long id);

}
