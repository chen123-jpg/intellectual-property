package com.chen.intellectualproperty.service;

import com.chen.intellectualproperty.entity.PatentSupplementary;
import com.chen.intellectualproperty.dto.PatentSupplementaryDTO;
import com.chen.intellectualproperty.query.PatentSupplementaryQuery;

import java.util.List;
import java.util.Map;

/**
 * 补漏专利表 Service
 *
 * @author 
 */
public interface PatentSupplementaryService {

    /**
     * 插入记录
     */
    int insert(PatentSupplementary record);

    /**
     * 插入或更新记录
     */
    int insertOrUpdate(PatentSupplementary record);

    /**
     * 根据主键删除
     */
    int deleteById(Long id);

    /**
     * 更新记录
     */
    int updateById(PatentSupplementary record);

    /**
     * 根据主键查询
     */
    PatentSupplementary selectById(Long id);

    /**
     * 查询所有记录
     */
    List<PatentSupplementary> selectAll();

    /**
     * 分页查询
     * @param pageNum 页码（从1开始）
     * @param pageSize 每页数量
     */
    List<PatentSupplementary> selectPage(int pageNum, int pageSize);

    /**
     * 查询总记录数
     */
    long count();

    /**
     * 根据参数条件查询列表
     * @param param 查询参数
     */
    List<PatentSupplementary> findListByParam(PatentSupplementaryQuery param);

    /**
     * 根据Map参数动态查询列表
     * @param params 查询参数Map
     */
    List<PatentSupplementary> findListByParams(Map<String, Object> params);

    /**
     * 根据参数条件查询总数
     * @param param 查询参数
     */
    long selectCount(PatentSupplementaryQuery param);

}
