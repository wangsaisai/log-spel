package com.bamboo.log.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bamboo.log.LogBizEnum;
import com.bamboo.log.LogRecordSaver;
import com.bamboo.log.mapper.LogRecordMapper;
import com.bamboo.log.other.LogRecordEntity;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class LogRecordService implements LogRecordSaver {

  @Autowired private LogRecordMapper logRecordMapper;

  @Override
  public void save(LogRecordEntity logRecord) {
    try {
      logRecordMapper.insert(logRecord);
    } catch (Exception e) {
      log.error("save log record error", e);
    }
  }

  public List<LogRecordEntity> list(
      Long associateId, LogBizEnum logType, String operation, Boolean execResult) {
    return logRecordMapper.selectList(
        new LambdaQueryWrapper<LogRecordEntity>()
            .eq(LogRecordEntity::getAssociateId, associateId)
            .eq(LogRecordEntity::getLogType, logType)
            .eq(Objects.nonNull(operation), LogRecordEntity::getOperation, operation)
            .eq(Objects.nonNull(execResult), LogRecordEntity::isExecResult, execResult));
  }
}
