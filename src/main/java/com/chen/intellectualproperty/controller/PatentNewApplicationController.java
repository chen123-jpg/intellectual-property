package com.chen.intellectualproperty.controller;

import com.chen.intellectualproperty.service.PatentNewApplicationService;
import com.chen.intellectualproperty.model.entity.PatentNewApplication;
import com.chen.intellectualproperty.model.query.PatentNewApplicationQuery;
import com.chen.intellectualproperty.util.ExcelExportUtil;
import com.chen.intellectualproperty.model.Result;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/patent-new-applications")
public class PatentNewApplicationController {

    @Autowired
    private PatentNewApplicationService service;

    @PostMapping
    public Result<?> insert(@RequestBody PatentNewApplication record) {
        service.insert(record);
        return Result.success(null);
    }

    @PostMapping("/insertOrUpdate")
    public Result<?> insertOrUpdate(@RequestBody PatentNewApplication record) {
        service.insertOrUpdate(record);
        return Result.success(null);
    }

    @DeleteMapping("/{id}")
    public Result<?> deleteById(@PathVariable Long id) {
        service.deleteById(id);
        return Result.success(null);
    }

    @PutMapping("/{id}")
    public Result<?> updateById(@PathVariable Long id, @RequestBody PatentNewApplication record) {
        record.setId(id);
        service.updateById(record);
        return Result.success(null);
    }

    @GetMapping("/{id}")
    public Result<PatentNewApplication> selectById(@PathVariable Long id) {
        return Result.success(service.selectById(id));
    }

    @GetMapping
    public Result<Map<String, Object>> selectAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "100") int page_size) {
        Map<String, Object> data = new HashMap<>();
        data.put("list", service.selectPage(page, page_size));
        data.put("total", service.count());
        return Result.success(data);
    }

    @PostMapping("/list")
    public Result<List<PatentNewApplication>> findListByParam(@RequestBody PatentNewApplicationQuery param) {
        return Result.success(service.findListByParam(param));
    }

    @PostMapping("/listByParams")
    public Result<List<PatentNewApplication>> findListByParams(@RequestBody Map<String, Object> params) {
        return Result.success(service.findListByParams(params));
    }

    @GetMapping("/export")
    public void exportExcel(HttpServletResponse response) throws IOException {
        List<PatentNewApplication> list = service.selectAll();
        ExcelExportUtil.exportToExcel(list, "专利新申请", response);
    }
}
