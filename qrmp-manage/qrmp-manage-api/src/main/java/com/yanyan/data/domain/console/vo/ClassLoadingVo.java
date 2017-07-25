package com.yanyan.data.domain.console.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 类加载情况
 * User: Saintcy
 * Date: 2017/4/1
 * Time: 10:54
 */
@Data
public class ClassLoadingVo implements Serializable {
    private Long totalLoadedClassCount;
    private Long unloadedClassCount;
    private Long loadedClassCount;
}
