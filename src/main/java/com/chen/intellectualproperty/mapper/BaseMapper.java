package com.chen.intellectualproperty.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 通用Mapper基类
 * @param <E> 实体类型（用于返回结果）
 * @param <Q> 查询参数类型（用于传递查询条件）
 *
 * @author 
 */
public interface BaseMapper<E, Q> {

    /**
     * 插入记录
     */
    int insert(E record);

    /**
     * 查询所有记录
     */
    List<E> selectAll();

    /**
     * 分页查询
     * @param offset 偏移量
     * @param limit 每页数量
     */
    List<E> selectPage(@Param("offset") int offset, @Param("limit") int limit);

    /**
     * 查询总记录数
     */
    long count();

    /**
     * 根据参数条件查询列表
     * @param param 查询参数
     */
    List<E> findListByParam(@Param("param") Q param);

    /**
     * 根据Map参数动态查询列表
     * @param params 查询参数Map
     */
    List<E> findListByParams(@Param("params") Map<String, Object> params);

    /**
     * 根据参数条件查询总数
     * @param param 查询参数
     */
    long selectCount(@Param("param") Q param);

}
