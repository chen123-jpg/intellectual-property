package com.chen.intellectualproperty.service;

import com.chen.intellectualproperty.model.entity.PatentIntermediateChange;
import com.chen.intellectualproperty.model.query.PatentIntermediateChangeQuery;

import java.util.List;
import java.util.Map;

/**
 * 中间著变专利表（有重复） Service
 *
 * @author 
 */
public interface PatentIntermediateChangeService {

    /**
     * 插入记录
     */
    int insert(PatentIntermediateChange record);

    /**
     * 插入或更新记录
     */
    int insertOrUpdate(PatentIntermediateChange record);

    /**
     * 根据主键删除
     */
    int deleteById(Long id);

    /**
     * 更新记录
     */
    int updateById(PatentIntermediateChange record);

    /**
     * 根据主键查询
     */
    PatentIntermediateChange selectById(Long id);

    /**
     * 查询所有记录
     */
    List<PatentIntermediateChange> selectAll();

    /**
     * 分页查询
     * @param pageNum 页码（从1开始）
     * @param pageSize 每页数量
     */
    List<PatentIntermediateChange> selectPage(int pageNum, int pageSize);

    /**
     * 查询总记录数
     */
    long count();

    /**
     * 根据参数条件查询列表
     * @param param 查询参数
     */
    List<PatentIntermediateChange> findListByParam(PatentIntermediateChangeQuery param);

    /**
     * 根据Map参数动态查询列表
     * @param params 查询参数Map
     */
    List<PatentIntermediateChange> findListByParams(Map<String, Object> params);

    /**
     * 根据参数条件查询总数
     * @param param 查询参数
     */
    long selectCount(PatentIntermediateChangeQuery param);

}
