package com.chen.intellectualproperty.controller;

import com.chen.intellectualproperty.service.PatentPctService;
import com.chen.intellectualproperty.model.entity.PatentPct;
import com.chen.intellectualproperty.model.query.PatentPctQuery;
import com.chen.intellectualproperty.util.ExcelExportUtil;
import com.chen.intellectualproperty.model.dto.Result;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/patent-pcts")
public class PatentPctController {

    @Autowired
    private PatentPctService service;

    @PostMapping
    public Result<?> insert(@RequestBody PatentPct record) {
        service.insert(record);
        return Result.success(null);
    }

    @PostMapping("/insertOrUpdate")
    public Result<?> insertOrUpdate(@RequestBody PatentPct record) {
        service.insertOrUpdate(record);
        return Result.success(null);
    }

    @DeleteMapping("/{id}")
    public Result<?> deleteById(@PathVariable Long id) {
        service.deleteById(id);
        return Result.success(null);
    }

    @PutMapping("/{id}")
    public Result<?> updateById(@PathVariable Long id, @RequestBody PatentPct record) {
        record.setId(id);
        service.updateById(record);
        return Result.success(null);
    }

    @GetMapping("/{id}")
    public Result<PatentPct> selectById(@PathVariable Long id) {
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
    public Result<List<PatentPct>> findListByParam(@RequestBody PatentPctQuery param) {
        return Result.success(service.findListByParam(param));
    }

    @PostMapping("/listByParams")
    public Result<List<PatentPct>> findListByParams(@RequestBody Map<String, Object> params) {
        return Result.success(service.findListByParams(params));
    }

    @GetMapping("/export")
    public void exportExcel(HttpServletResponse response) throws IOException {
        List<PatentPct> list = service.selectAll();
        ExcelExportUtil.exportToExcel(list, "PCT国际申请", response);
    }
}
