CREATE TABLE IF NOT EXISTS `t_log_record`
(
    `id`           bigint      NOT NULL AUTO_INCREMENT,
    `associate_id` varchar(50) NOT NULL,
    `log_type`     varchar(50) NOT NULL COMMENT '日志对应的业务',
    `operation`    varchar(50) NOT NULL COMMENT '操作类型：add,update,delete...',
    `extra`        text                 DEFAULT NULL COMMENT '额外信息记录',
    `exec_result`  tinyint     NOT NULL DEFAULT true COMMENT '是否执行成功',
    `operator`     varchar(50) NOT NULL COMMENT '操作人',
    `start_time`   datetime    NOT NULL COMMENT '开始时间',
    `end_time`     datetime    NOT NULL COMMENT '结束时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;