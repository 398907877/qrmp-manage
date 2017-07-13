package com.yanyan.service.console.impl;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import ch.qos.logback.core.util.FileSize;
import com.yanyan.data.domain.console.vo.LoggerVo;
import com.yanyan.service.console.LoggerService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * User: Saintcy
 * Date: 2017/3/31
 * Time: 14:13
 */
@Service
public class LoggerServiceImpl implements LoggerService {
    public void setLogger(String packageName, String level, String logFileName) {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

        // if logfilename not set, then set to default
        if (StringUtils.isEmpty(logFileName)) {
            logFileName = "application.log";
        }

        Logger logger = loggerContext.getLogger(packageName);

        // Remove all previously added appenders from this logger instance.
        logger.detachAndStopAllAppenders();

        // define appender
        RollingFileAppender<ILoggingEvent> appender = new RollingFileAppender<ILoggingEvent>();
        //Must not be empty from 1.1.7, or else appenders can't be start again because of FILE-NAME-PATTERN COLLISION, because of FILE-NAME-PATTERN not removed when name is empty.
        appender.setName(logFileName.replace(".log", "_FILE").toUpperCase());
        appender.setFile(logFileName);

        // rolling policy
        TimeBasedRollingPolicy<ILoggingEvent> rollingPolicy = new TimeBasedRollingPolicy<ILoggingEvent>();
        rollingPolicy.setContext(loggerContext);
        rollingPolicy.setMaxHistory(30);
        rollingPolicy.setFileNamePattern(logFileName.replace(".log", ".%d{yyyy-MM-dd}.%i.log.zip"));
        rollingPolicy.setParent(appender);
        SizeAndTimeBasedFNATP<ILoggingEvent> triggeringPolicy = new SizeAndTimeBasedFNATP<ILoggingEvent>();
        triggeringPolicy.setContext(loggerContext);
        triggeringPolicy.setMaxFileSize(FileSize.valueOf("50MB"));
        rollingPolicy.setTimeBasedFileNamingAndTriggeringPolicy(triggeringPolicy);
        rollingPolicy.start();

        // encoder
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(loggerContext);
        encoder.setPattern("%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{50} - %msg%n");
        encoder.start();

        // start appender
        appender.setRollingPolicy(rollingPolicy);
        appender.setContext(loggerContext);
        appender.setEncoder(encoder);
        //Compress not support Prudent mode
        //appender.setPrudent(true); //support that multiple JVMs can safely write to the same file.
        appender.start();

        // add appender
        logger.addAppender(appender);

        // setup level
        logger.setLevel(Level.toLevel(level));

        // remove the appenders that inherited 'ROOT'.
        logger.setAdditive(false);

        System.out.println("+++" + logger.getName() + "[" + level + "]" + ":" + appender.getFile());
    }

    /**
     * 获取日志配置列表
     *
     * @param name 日志类包名
     * @return 日志配置列表
     */
    public List<LoggerVo> getLoggerList(String name) {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        List<Logger> loggerList = loggerContext.getLoggerList();
        List<LoggerVo> loggerVoList = new ArrayList<LoggerVo>();

        if (loggerList != null && loggerList.size() > 0) {
            Appender<ILoggingEvent> appender;

            for (int i = 0; i < loggerList.size(); i++) {
                Logger logger = loggerList.get(i);

                if (name != null && !"".equals(name) && !(logger.getName().startsWith(name))) {
                    continue;
                }

                LoggerVo loggerVo = new LoggerVo();
                loggerVo.setName(logger.getName());

                // 添加到Vector对象中
                if (logger.getLevel() != null) {
                    loggerVo.setLevel(logger.getLevel().toString());
                } else {
                    loggerVo.setLevel("");
                }


                List files = new ArrayList();
                Iterator<Appender<ILoggingEvent>> it = logger.iteratorForAppenders();
                while (it.hasNext()) {
                    appender = it.next();

                    if (ConsoleAppender.class.isInstance(appender)) {
                        files.add("<CONSOLE>");
                    } else if (FileAppender.class.isInstance(appender)) {
                        files.add(((FileAppender<ILoggingEvent>) appender).getFile());
                    }
                }
                loggerVo.setFiles(files);
                loggerVoList.add(loggerVo);
            }
        }

        return loggerVoList;
    }
}
