package com.yanyan.data.domain.console.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 线程信息
 * User: Saintcy
 * Date: 2017/4/6
 * Time: 12:10
 */
@Data
public class ThreadVo implements Serializable {
    private long id;
    private String name;
    private long startTime;
    private long lastTime;
    private String state;
    private long cpuTime;
    private double cpuPercent;
}
