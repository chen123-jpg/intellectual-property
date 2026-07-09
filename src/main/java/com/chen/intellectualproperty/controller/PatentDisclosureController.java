package com.chen.intellectualproperty.controller;

import com.chen.intellectualproperty.service.PatentDisclosureService;
import com.chen.intellectualproperty.model.entity.PatentDisclosure;
import com.chen.intellectualproperty.model.query.PatentDisclosureQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * 专利交底信息表（T表） Controller
 *
 * @author 
 */
@RestController
@RequestMapping("/patent-disclosure")
public class PatentDisclosureController {

    @Autowired
    private PatentDisclosureService service;

    /**
     * 插入记录
     */
    @PostMapping
    public int insert(@RequestBody PatentDisclosure record) {
        return service.insert(record);
    }

    /**
     * 插入或更新记录
     */
    @PostMapping("/insertOrUpdate")
    public int insertOrUpdate(@RequestBody PatentDisclosure record) {
        return service.insertOrUpdate(record);
    }

    /**
     * 根据主键删除
     */
    @DeleteMapping("/{id}")
    public int deleteById(@PathVariable Long id) {
        return service.deleteById(id);
    }

    /**
     * 更新记录
     */
    @PutMapping
    public int updateById(@RequestBody PatentDisclosure record) {
        return service.updateById(record);
    }

    /**
     * 根据主键查询
     */
    @GetMapping("/{id}")
    public PatentDisclosure selectById(@PathVariable Long id) {
        return service.selectById(id);
    }

    /**
     * 查询所有记录
     */
    @GetMapping
    public List<PatentDisclosure> selectAll() {
        return service.selectAll();
    }

    /**
     * 分页查询
     * @param pageNum 页码（从1开始）
     * @param pageSize 每页数量
     */
    @GetMapping("/page")
    public Map<String, Object> selectPage(@RequestParam(defaultValue = "1") int pageNum,
                                         @RequestParam(defaultValue = "10") int pageSize) {
        Map<String, Object> result = new HashMap<>();
        result.put("list", service.selectPage(pageNum, pageSize));
        result.put("total", service.count());
        result.put("pageNum", pageNum);
        result.put("pageSize", pageSize);
        return result;
    }

    /**
     * 根据参数条件查询列表
     * @param param 查询参数
     */
    @PostMapping("/list")
    public List<PatentDisclosure> findListByParam(@RequestBody PatentDisclosureQuery param) {
        return service.findListByParam(param);
    }

    /**
     * 根据Map参数动态查询列表
     * @param params 查询参数Map
     */
    @PostMapping("/listByParams")
    public List<PatentDisclosure> findListByParams(@RequestBody Map<String, Object> params) {
        return service.findListByParams(params);
    }

    /**
     * 根据参数条件查询总数
     * @param param 查询参数
     */
    @PostMapping("/count")
    public long selectCount(@RequestBody PatentDisclosureQuery param) {
        return service.selectCount(param);
    }

}
