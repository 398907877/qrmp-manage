package com.yanyan.data.domain.console;

import lombok.Data;

import java.io.Serializable;

/**
 * 表
 * User: Saintcy
 * Date: 2016/10/24
 * Time: 23:04
 */
@Data
public class Table implements Serializable {
    private String name;
    private String comment;
}
