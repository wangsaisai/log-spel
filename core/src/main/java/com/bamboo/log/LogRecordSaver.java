package com.bamboo.log;

import com.bamboo.log.other.LogRecordEntity;

public interface LogRecordSaver {

  void save(LogRecordEntity logRecord);
}
