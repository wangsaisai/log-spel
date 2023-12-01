package com.bamboo.log.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bamboo.log.LogBizEnum;
import com.bamboo.log.LogRecord;
import com.bamboo.log.ThreadLocalLogVariable;
import com.bamboo.log.service.LogRecordService;

@RestController
@RequestMapping("/api/v1/log")
public class BizController {

  @Autowired private LogRecordService logRecordService;

  // 执行成功；resultExpr默认为空，若未跑出异常，则认为成功
  @LogRecord(associateIdExpr = "#id", type = LogBizEnum.BIZ1, operation = "ADD")
  @PostMapping("/add")
  public String add(@RequestParam(name = "id") String id) {
    return "hello " + id;
  }

  // 执行失败；返回null，导致resultExpr结果为false
  @LogRecord(
      associateIdExpr = "#id",
      type = LogBizEnum.BIZ1,
      operation = "GET",
      resultExpr = "#_ret != null")
  @GetMapping("/get")
  public String get(@RequestParam(name = "id") String id) {
    return null;
  }

  // 执行成功；resultExpr结果为true
  @LogRecord(
      associateIdExpr = "#id",
      type = LogBizEnum.BIZ1,
      operation = "UPDATE",
      extraExpr = "'id: ' + #id + ', old_name: ' + #old_name + ', new_name: ' + #updateName",
      resultExpr = "#_ret != null")
  @PostMapping("/update")
  public String update(
      @RequestParam(name = "id") String id, @RequestParam(name = "update_name") String updateName) {
    ThreadLocalLogVariable.put("old_name", "world");
    return "hello " + updateName;
  }

  // 执行失败；抛出了异常
  @PostMapping("/delete")
  @LogRecord(associateIdExpr = "#id", type = LogBizEnum.BIZ1, operation = "DELETE")
  public String delete(@RequestParam(name = "id") String id) {
    throw new RuntimeException("can not delete");
  }
}
