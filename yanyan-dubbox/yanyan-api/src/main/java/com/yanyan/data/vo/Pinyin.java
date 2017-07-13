package com.yanyan.data.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 拼音
 * User: Saintcy
 * Date: 2016/8/18
 * Time: 16:40
 */
@Data
public class Pinyin implements Serializable {
    //全拼
    private String complete;
    //首字母缩写
    private String simple;
}
