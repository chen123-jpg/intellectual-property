package com.chen.intellectualproperty.service;

import com.chen.intellectualproperty.model.entity.PatentPct;
import com.chen.intellectualproperty.model.query.PatentPctQuery;

import java.util.List;
import java.util.Map;

/**
 * PCT国际申请表 Service
 *
 * @author 
 */
public interface PatentPctService {

    /**
     * 插入记录
     */
    int insert(PatentPct record);

    /**
     * 插入或更新记录
     */
    int insertOrUpdate(PatentPct record);

    /**
     * 根据主键删除
     */
    int deleteById(Long id);

    /**
     * 更新记录
     */
    int updateById(PatentPct record);

    /**
     * 根据主键查询
     */
    PatentPct selectById(Long id);

    /**
     * 查询所有记录
     */
    List<PatentPct> selectAll();

    /**
     * 分页查询
     * @param pageNum 页码（从1开始）
     * @param pageSize 每页数量
     */
    List<PatentPct> selectPage(int pageNum, int pageSize);

    /**
     * 查询总记录数
     */
    long count();

    /**
     * 根据参数条件查询列表
     * @param param 查询参数
     */
    List<PatentPct> findListByParam(PatentPctQuery param);

    /**
     * 根据Map参数动态查询列表
     * @param params 查询参数Map
     */
    List<PatentPct> findListByParams(Map<String, Object> params);

    /**
     * 根据参数条件查询总数
     * @param param 查询参数
     */
    long selectCount(PatentPctQuery param);

}
