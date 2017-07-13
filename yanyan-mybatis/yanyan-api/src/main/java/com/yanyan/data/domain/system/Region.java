package com.yanyan.data.domain.system;

import com.yanyan.core.lang.BaseDomain;
import com.yanyan.core.serialize.exclusion.annotation.OutputOnly;
import com.yanyan.core.spring.validator.group.Update;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * 地区
 * User: Saintcy
 * Date: 2016/8/23
 * Time: 15:31
 */
@Data
public class Region extends BaseDomain {
    @NotNull(message = "id不能为空", groups = Update.class)
    private Long id;
    @NotBlank(message = "地区名称不能为空")
    @Length(max = 50, message = "地区代码长度必须小于{max}")
    private String code;
    @NotBlank(message = "地区名称不能为空")
    @Length(max = 100, message = "地区名称长度必须小于{max}")
    private String name;
    @Length(max = 200, message = "拼音全拼长度必须小于{max}")
    private String pinyin;
    @Length(max = 50, message = "拼音简码长度必须小于{max}")
    private String pyabbr;
    @NotNull(message = "上级区域不能为空")
    private Long parent_id;
    private Integer priority;


    /**Input Only Parameters*/
    /***/
    //No fields

    /**Output Only Parameters*/
    /***/
    //No fields
    @OutputOnly
    private String parent_name;
    @OutputOnly
    private String path;
    @OutputOnly
    private Short level;
    @OutputOnly
    private Boolean hasChildren;

    /**Transient(Not Input and Output) Parameters*/
    /***/
    //No fields
}
