package com.yanyan.web.controller.console;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * User: Saintcy
 * Date: 2017/3/31
 * Time: 23:30
 */
@Controller
@RequestMapping("/console/tool")
public class ToolController {
    @RequestMapping("/telnet")
    public String telnet() {
        return "/console/tool/Telnet";
    }

    @RequestMapping("/browser")
    public String browser() {
        return "/console/tool/Browser";
    }

    @RequestMapping("/classpath")
    public String classpath() {
        return "/console/tool/classpath";
    }
}
