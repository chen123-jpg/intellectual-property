package com.chen.intellectualproperty.controller;

import com.chen.intellectualproperty.model.dto.Result;
import com.chen.intellectualproperty.model.entity.Applicant;
import com.chen.intellectualproperty.service.ApplicantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/applicants")
public class ApplicantController {

    @Autowired
    private ApplicantService applicantService;

    @GetMapping
    public Result<List<Applicant>> selectAll() {
        return Result.success(applicantService.selectAll());
    }

    @GetMapping("/{id}")
    public Result<Applicant> selectById(@PathVariable Long id) {
        return Result.success(applicantService.selectById(id));
    }

    @PostMapping
    public Result<?> insert(@RequestBody Applicant record) {
        applicantService.insert(record);
        return Result.success(null);
    }

    @PutMapping("/{id}")
    public Result<?> updateById(@PathVariable Long id, @RequestBody Applicant record) {
        record.setId(id);
        applicantService.updateById(record);
        return Result.success(null);
    }

    @DeleteMapping("/{id}")
    public Result<?> deleteById(@PathVariable Long id) {
        applicantService.deleteById(id);
        return Result.success(null);
    }

}
