package com.yanyan.data.domain.system;

import com.yanyan.core.serialize.exclusion.annotation.OutputOnly;
import com.yanyan.core.spring.validator.group.Update;
import com.yanyan.core.lang.BaseDomain;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * User: Saintcy
 * Date: 2015/8/21
 * Time: 16:30
 */
@Data
public class Role extends BaseDomain {
    @NotNull(message = "id不能为空", groups = Update.class)
    private Long id;
    /**
     * 所属门户
     */
    @NotNull(message = "所属门户不能为空")
    private Long portal_id;
    /**
     * 角色编码
     */
    @NotBlank(message = "角色编码不能为空")
    @Length(max = 100, message = "角色编码长度必须小于{max}")
    private String code;
    /**
     * 角色名称
     */
    @NotBlank(message = "角色名称不能为空")
    @Length(max = 50, message = "角色名称长度必须小于{max}")
    private String name;
    /**
     * 角色备注
     */
    @Length(max = 1000, message = "角色备注长度必须小于{max}")
    private String remarks;
    /**
     * 是否显示
     */
    private Integer is_show;
    /**
     * 是否管理员
     */
    private Integer is_admin;
    /**
     * 顺序
     */
    private Integer priority;

    /**Input Only Parameters*/
    /***/
    //No fields

    /**Output Only Parameters*/
    /***/
    @OutputOnly
    private String portal_name;

    /**Transient(Not Input and Output) Parameters*/
    /***/
    //No fields
}
