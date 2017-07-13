package com.yanyan.data.domain.console.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * User: Saintcy
 * Date: 2017/3/31
 * Time: 11:02
 */
@Data
public class LoggerVo implements Serializable {
    private String name;
    private String level;
    private List<String> files;
}
