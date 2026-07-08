package com.chen.intellectualproperty.service;

public interface GenerateNoService {
    /**
     * 生成业务编号
     * @param tableName 数据库表名
     * @param prefix 编号前缀 P/PTS
     * @return 完整编号 P2026001
     */
    String generateNo(String tableName, String prefix);
}
