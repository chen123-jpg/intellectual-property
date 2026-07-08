package com.chen.intellectualproperty.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.chen.intellectualproperty.model.entity.PatentPct;
import com.chen.intellectualproperty.model.query.PatentPctQuery;

/**
 * PCT国际申请表 Mapper
 * <p>继承BaseMapper，E=PatentPct（返回结果），Q=PatentPctQuery（查询参数）</p>
 *
 * @author 
 */
@Mapper
public interface PatentPctMapper extends BaseMapper<PatentPct, PatentPctQuery> {

    /**
     * 根据主键删除
     */
    int deleteById(Long id);

    /**
     * 更新记录
     */
    int updateById(PatentPct record);

    /**
     * 插入或更新记录
     */
    int insertOrUpdate(PatentPct record);

    /**
     * 根据主键查询
     */
    PatentPct selectById(Long id);

}
