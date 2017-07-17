package com.yanyan.web.controller.console;

import com.yanyan.data.domain.console.vo.LoggerVo;
import com.yanyan.service.console.LoggerService;
import com.yanyan.core.web.DataResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 日志设置
 * User: Saintcy
 * Date: 2017/3/31
 * Time: 14:26
 */
@Slf4j
@Controller
@RequestMapping("/console/logger")
public class LoggerController {
    @Autowired
    private LoggerService loggerService;

    @RequestMapping("/index")
    public String index(Model model) {
        List<LoggerVo> loggers = loggerService.getLoggerList("");
        model.addAttribute("loggers", loggers);
        return "/console/logger/index";
    }

    @RequestMapping("/find")
    @ResponseBody
    public Model find() {
        try {
            return DataResponse.success("loggers", loggerService.getLoggerList(""));
        } catch (Exception e) {
            log.error("", e);
            return DataResponse.failure(e);
        }
    }

    @RequestMapping("/set")
    @ResponseBody
    public Model set(@RequestBody LoggerVo logger){
        try {
            loggerService.setLogger(logger.getName(), logger.getLevel(), logger.getFiles().get(0));
            return DataResponse.success();
        } catch (Exception e) {
            log.error("", e);
            return DataResponse.failure(e);
        }
    }
}
