package com.chen.intellectualproperty.service;

import com.chen.intellectualproperty.entity.PatentReexamination;
import com.chen.intellectualproperty.dto.PatentReexaminationDTO;
import com.chen.intellectualproperty.query.PatentReexaminationQuery;

import java.util.List;
import java.util.Map;

/**
 * 复审无效专利表 Service
 *
 * @author 
 */
public interface PatentReexaminationService {

    /**
     * 插入记录
     */
    int insert(PatentReexamination record);

    /**
     * 插入或更新记录
     */
    int insertOrUpdate(PatentReexamination record);

    /**
     * 根据主键删除
     */
    int deleteById(Long id);

    /**
     * 更新记录
     */
    int updateById(PatentReexamination record);

    /**
     * 根据主键查询
     */
    PatentReexamination selectById(Long id);

    /**
     * 查询所有记录
     */
    List<PatentReexamination> selectAll();

    /**
     * 分页查询
     * @param pageNum 页码（从1开始）
     * @param pageSize 每页数量
     */
    List<PatentReexamination> selectPage(int pageNum, int pageSize);

    /**
     * 查询总记录数
     */
    long count();

    /**
     * 根据参数条件查询列表
     * @param param 查询参数
     */
    List<PatentReexamination> findListByParam(PatentReexaminationQuery param);

    /**
     * 根据Map参数动态查询列表
     * @param params 查询参数Map
     */
    List<PatentReexamination> findListByParams(Map<String, Object> params);

    /**
     * 根据参数条件查询总数
     * @param param 查询参数
     */
    long selectCount(PatentReexaminationQuery param);

}
