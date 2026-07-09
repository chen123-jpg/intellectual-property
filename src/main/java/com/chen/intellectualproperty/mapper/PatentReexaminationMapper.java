package com.chen.intellectualproperty.mapper;

import org.apache.ibatis.annotations.Mapper;
import com.chen.intellectualproperty.model.entity.PatentReexamination;
import com.chen.intellectualproperty.model.query.PatentReexaminationQuery;

/**
 * 复审无效专利表 Mapper
 * <p>继承BaseMapper，E=PatentReexamination（返回结果），Q=PatentReexaminationQuery（查询参数）</p>
 *
 * @author 
 */
@Mapper
public interface PatentReexaminationMapper extends BaseMapper<PatentReexamination, PatentReexaminationQuery> {

    /**
     * 根据主键删除
     */
    int deleteById(Long id);

    /**
     * 更新记录
     */
    int updateById(PatentReexamination record);

    /**
     * 插入或更新记录
     */
    int insertOrUpdate(PatentReexamination record);

    /**
     * 根据主键查询
     */
    PatentReexamination selectById(Long id);

}
