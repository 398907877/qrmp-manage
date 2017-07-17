package com.yanyan.data.domain.system;

import com.yanyan.core.lang.BaseDomain;
import com.yanyan.core.spring.validator.group.Update;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * 字典分组
 * User: Saintcy
 * Date: 2016/8/12
 * Time: 11:34
 */
@Data
public class DictionaryGroup extends BaseDomain {
    @NotNull(message = "id不能为空", groups = Update.class)
    private Long id;

    /**
     * 编码
     */
    @NotBlank(message = "编码不能为空")
    @Length(max = 64, message = "编码长度必须小于{max}")
    private String code;
    /**
     * 编码
     */
    @NotBlank(message = "名称不能为空")
    @Length(max = 64, message = "名称长度必须小于{max}")
    private String name;

    /**
     * 备注
     */
    @Length(max = 1000, message = "备注长度必须小于{max}")
    private String remarks;

    /**
     * 优先级
     */
    private Integer priority;

    /**Input Only Parameters*/
    //No fields

    /**Output Only Parameters*/
    //No fields

    /**Transient(Not Input and Output) Parameters*/
    //No fields
}
