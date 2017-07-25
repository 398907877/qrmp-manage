package com.yanyan.data.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * User: Saintcy
 * Date: 2016/8/26
 * Time: 16:32
 */
@Data
@AllArgsConstructor
public class FileMeta implements Serializable {
    private String name;//文件原始名称
    private Integer size;//文件大小
    private String type;//文件类型。如：image/jpeg
    private String url;//保存的路径
    private String fileUrl;//文件地址
    private String thumbnailUrl;//缩略图地址

    public String getJson() {
        return "{" +
                "\"name\"='" + name + '\'' +
                ", \"size\"=" + size +
                ", \"type\"='" + type + '\'' +
                ", \"url\"='" + url + '\'' +
                ", \"thumbnailUrl\"='" + thumbnailUrl + '\'' +
                '}';
    }
}
