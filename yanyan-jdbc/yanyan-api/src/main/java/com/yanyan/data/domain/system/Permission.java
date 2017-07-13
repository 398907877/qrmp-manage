package com.yanyan.data.domain.system;

import com.yanyan.core.serialize.exclusion.annotation.OutputOnly;
import com.yanyan.core.spring.validator.group.Create;
import com.yanyan.core.lang.BaseDomain;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * 权限
 * User: Saintcy
 * Date: 2015/8/21
 * Time: 9:18
 */
@Data
public class Permission extends BaseDomain {
    @NotNull(groups = Create.class)
    private Long id;
    @NotNull(message = "所属资源不能为空")
    private Long resource_id;
    @NotBlank(message = "权限编码不能为空")
    @Length(max = 50, message = "权限编码长度必须小于{max}")
    private String code;
    @NotBlank(message = "权限编码不能为空")
    @Length(max = 50, message = "权限编码长度必须小于{max}")
    private String name;
    private Integer is_show;
    private String remarks;
    private Integer priority;

    /**Input Only Parameters*/
    //No fields

    /**Output Only Parameters*/
    @OutputOnly
    private String resource_name;

    /**Transient(Not Input and Output) Parameters*/
    //No fields
}
