package com.chen.intellectualproperty.mapper;

import com.chen.intellectualproperty.model.entity.Agent;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AgentMapper {

    int insert(Agent record);

    int deleteById(Long id);

    int updateById(Agent record);

    Agent selectById(Long id);

    List<Agent> selectAll();

    List<Agent> selectByName(String name);

}
