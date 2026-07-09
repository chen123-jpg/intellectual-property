package com.chen.intellectualproperty.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.chen.intellectualproperty.model.entity.PatentNewApplication;
import com.chen.intellectualproperty.model.query.PatentNewApplicationQuery;

/**
 * 专利新申请表 Mapper
 * <p>继承BaseMapper，E=PatentNewApplication（返回结果），Q=PatentNewApplicationQuery（查询参数）</p>
 *
 * @author 
 */
@Mapper
public interface PatentNewApplicationMapper extends BaseMapper<PatentNewApplication, PatentNewApplicationQuery> {

    /**
     * 根据主键删除
     */
    int deleteById(Long id);

    /**
     * 更新记录
     */
    int updateById(PatentNewApplication record);

    /**
     * 插入或更新记录
     */
    int insertOrUpdate(PatentNewApplication record);

    /**
     * 根据主键查询
     */
    PatentNewApplication selectById(Long id);

}
