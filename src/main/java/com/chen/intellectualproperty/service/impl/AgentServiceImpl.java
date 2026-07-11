package com.chen.intellectualproperty.service.impl;

import com.chen.intellectualproperty.mapper.AgentMapper;
import com.chen.intellectualproperty.model.entity.Agent;
import com.chen.intellectualproperty.service.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class
AgentServiceImpl implements AgentService {
    @Autowired
    private AgentMapper agentMapper;

    @Override
    @Transactional
    public int insert(Agent record) {
        return agentMapper.insert(record);
    }

    @Override
    @Transactional
    public int deleteById(Long id) {
        return agentMapper.deleteById(id);
    }

    @Override
    @Transactional
    public int updateById(Agent record) {
        return agentMapper.updateById(record);
    }

    @Override
    public Agent selectById(Long id) {
        return agentMapper.selectById(id);
    }

    @Override
    public List<Agent> selectAll() {
        return agentMapper.selectAll();
    }

}
