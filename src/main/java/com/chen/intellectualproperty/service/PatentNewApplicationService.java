package com.chen.intellectualproperty.service;

import com.chen.intellectualproperty.model.entity.PatentNewApplication;
import com.chen.intellectualproperty.model.query.PatentNewApplicationQuery;

import java.util.List;

/**
 * 专利新申请表 Service
 *
 * @author 
 */
public interface PatentNewApplicationService {

    /**
     * 插入记录
     */
    int insert(PatentNewApplication record);

    /**
     * 插入或更新记录
     */
    int insertOrUpdate(PatentNewApplication record);

    /**
     * 根据主键删除
     */
    int deleteById(Long id);

    /**
     * 更新记录
     */
    int updateById(PatentNewApplication record);

    /**
     * 根据主键查询
     */
    PatentNewApplication selectById(Long id);

    /**
     * 查询所有记录
     */
    List<PatentNewApplication> selectAll();

    /**
     * 分页查询
     * @param pageNum 页码（从1开始）
     * @param pageSize 每页数量
     */
    List<PatentNewApplication> selectPage(int pageNum, int pageSize);

    /**
     * 查询总记录数
     */
    long count();

    /**
     * 根据参数条件查询列表
     * @param param 查询参数
     */
    List<PatentNewApplication> findListByParam(PatentNewApplicationQuery param);

    /**
     * 根据参数条件查询总数
     * @param param 查询参数
     */
    long selectCount(PatentNewApplicationQuery param);

}
