package com.yanyan.service.console;

import com.yanyan.data.domain.console.vo.LoggerVo;

import java.util.List;

/**
 * 日志服务
 * User: Saintcy
 * Date: 2017/3/31
 * Time: 14:17
 */
public interface LoggerService {
    void setLogger(String packageName, String level, String logFileName);
    List<LoggerVo> getLoggerList(String name);
}
