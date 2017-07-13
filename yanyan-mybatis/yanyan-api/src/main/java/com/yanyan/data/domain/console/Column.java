package com.yanyan.data.domain.console;

import lombok.Data;

import java.io.Serializable;

/**
 * 列
 * User: Saintcy
 * Date: 2016/10/24
 * Time: 23:07
 */
@Data
public class Column implements Serializable {
    private String name;
    private String type;
    private Integer maxlen;
    private String comment;
}
