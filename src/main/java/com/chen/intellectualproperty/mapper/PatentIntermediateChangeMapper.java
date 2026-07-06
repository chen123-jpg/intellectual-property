package com.chen.intellectualproperty.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.chen.intellectualproperty.entity.PatentIntermediateChange;
import com.chen.intellectualproperty.query.PatentIntermediateChangeQuery;

/**
 * 中间著变专利表（有重复） Mapper
 * <p>继承BaseMapper，E=PatentIntermediateChange（返回结果），Q=PatentIntermediateChangeQuery（查询参数）</p>
 *
 * @author 
 */
@Mapper
public interface PatentIntermediateChangeMapper extends BaseMapper<PatentIntermediateChange, PatentIntermediateChangeQuery> {

    /**
     * 根据主键删除
     */
    int deleteById(Long id);

    /**
     * 更新记录
     */
    int updateById(PatentIntermediateChange record);

    /**
     * 插入或更新记录
     */
    int insertOrUpdate(PatentIntermediateChange record);

    /**
     * 根据主键查询
     */
    PatentIntermediateChange selectById(Long id);

}
