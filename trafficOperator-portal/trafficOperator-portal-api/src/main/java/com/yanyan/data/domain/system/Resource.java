package com.yanyan.data.domain.system;

import com.yanyan.core.lang.BaseDomain;
import com.yanyan.core.serialize.exclusion.annotation.OutputOnly;
import com.yanyan.core.spring.validator.group.Create;
import com.yanyan.core.spring.validator.group.Update;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 资源信息
 * User: Saintcy
 * Date: 2015/8/22
 * Time: 0:12
 */
@Data
public class Resource extends BaseDomain {
    @NotNull(message = "id不能为空", groups = Update.class)
    protected Long id;
    @NotNull(message = "上级资源不能为空")
    protected Long parent_id;
    @NotNull(message = "所属门户不能为空", groups = Create.class)
    private Long portal_id;
    @NotBlank(message = "资源编码不能为空")
    @Length(max = 50, message = "资源编码长度必须小于{max}")
    private String code;
    @Length(max = 50, message = "资源名称长度必须小于{max}")
    private String name;
    @Length(max = 100, message = "资源地址长度必须小于{max}")
    private String url;
    @Length(max = 100, message = "目标窗口长度必须小于{max}")
    private String target;
    @Length(max = 100, message = "资源图标长度必须小于{max}")
    private String icon;
    @Length(max = 1000, message = "资源备注长度必须小于{max}")
    private String remarks;
    private Integer priority;
    private Integer is_show;
    private List<Permission> permissions;

    /**Input Only Parameters*/
    //No fields

    /**Output Only Parameters*/
    @OutputOnly
    private String parent_name;
    @OutputOnly
    private String portal_name;
    @OutputOnly
    private boolean hasChildren;
    @OutputOnly
    private String path;
    @OutputOnly
    private String level;

    /**Transient(Not Input and Output) Parameters*/
    //No fields
}
