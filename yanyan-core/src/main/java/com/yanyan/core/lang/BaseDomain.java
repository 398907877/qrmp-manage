package com.yanyan.core.lang;

import com.yanyan.core.serialize.exclusion.annotation.OutputOnly;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 实体基类
 * User: Saintcy
 * Date: 2016/3/30
 * Time: 17:52
 */
@Data
public class BaseDomain implements Serializable {
    @OutputOnly
    protected Date create_time = new Date();//创建时间
    @OutputOnly
    protected Date update_time = new Date();//更新时间
}
