package com.chen.intellectualproperty.controller;

import com.chen.intellectualproperty.model.dto.Result;
import com.chen.intellectualproperty.model.entity.Agent;
import com.chen.intellectualproperty.service.AgentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/agents")
public class AgentController {

    @Autowired
    private AgentService agentService;

    @GetMapping
    public Result<List<Agent>> selectAll() {
        return Result.success(agentService.selectAll());
    }

    @GetMapping("/{id}")
    public Result<Agent> selectById(@PathVariable Long id) {
        return Result.success(agentService.selectById(id));
    }

    @PostMapping
    public Result<?> insert(@RequestBody Agent record) {
        agentService.insert(record);
        return Result.success(null);
    }

    @PutMapping("/{id}")
    public Result<?> updateById(@PathVariable Long id, @RequestBody Agent record) {
        record.setId(id);
        agentService.updateById(record);
        return Result.success(null);
    }

    @DeleteMapping("/{id}")
    public Result<?> deleteById(@PathVariable Long id) {
        agentService.deleteById(id);
        return Result.success(null);
    }

}
