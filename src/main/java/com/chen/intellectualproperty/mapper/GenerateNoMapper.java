package com.chen.intellectualproperty.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface GenerateNoMapper {

    /**
     * 查询指定表、指定前缀、当前年份最大编号
     * @param tableName 表名
     * @param prefix 编号前缀 P/PTS
     * @param year 当前4位年份
     * @return 最大完整编号
     */
    @Select("SELECT MAX(internal_no) FROM ${tableName} " +
            "WHERE internal_no LIKE CONCAT(#{prefix}, #{year}, '%')")
    String getMaxBusinessNo(@Param("tableName") String tableName,
                            @Param("prefix") String prefix,
                            @Param("year") String year);
}
