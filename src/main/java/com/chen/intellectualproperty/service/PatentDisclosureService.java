package com.chen.intellectualproperty.service;

import com.chen.intellectualproperty.model.entity.PatentDisclosure;
import com.chen.intellectualproperty.model.query.PatentDisclosureQuery;

import java.util.List;
import java.util.Map;

/**
 * 专利交底信息表（T表） Service
 *
 * @author 
 */
public interface PatentDisclosureService {

    /**
     * 插入记录
     */
    int insert(PatentDisclosure record);

    /**
     * 插入或更新记录
     */
    int insertOrUpdate(PatentDisclosure record);

    /**
     * 根据主键删除
     */
    int deleteById(Long id);

    /**
     * 更新记录
     */
    int updateById(PatentDisclosure record);

    /**
     * 根据主键查询
     */
    PatentDisclosure selectById(Long id);

    /**
     * 查询所有记录
     */
    List<PatentDisclosure> selectAll();

    /**
     * 分页查询
     * @param pageNum 页码（从1开始）
     * @param pageSize 每页数量
     */
    List<PatentDisclosure> selectPage(int pageNum, int pageSize);

    /**
     * 查询总记录数
     */
    long count();

    /**
     * 根据参数条件查询列表
     * @param param 查询参数
     */
    List<PatentDisclosure> findListByParam(PatentDisclosureQuery param);

    /**
     * 根据Map参数动态查询列表
     * @param params 查询参数Map
     */
    List<PatentDisclosure> findListByParams(Map<String, Object> params);

    /**
     * 根据参数条件查询总数
     * @param param 查询参数
     */
    long selectCount(PatentDisclosureQuery param);

}
