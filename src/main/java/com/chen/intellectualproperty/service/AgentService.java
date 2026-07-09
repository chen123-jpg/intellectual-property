package com.chen.intellectualproperty.service;

import com.chen.intellectualproperty.model.entity.Agent;
import java.util.List;

public interface AgentService {

    int insert(Agent record);

    int deleteById(Long id);

    int updateById(Agent record);

    Agent selectById(Long id);

    List<Agent> selectAll();

}
