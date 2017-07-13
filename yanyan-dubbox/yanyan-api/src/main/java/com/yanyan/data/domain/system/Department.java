package com.yanyan.data.domain.system;

import com.yanyan.core.serialize.exclusion.annotation.OutputOnly;
import com.yanyan.core.spring.validator.group.Create;
import com.yanyan.core.spring.validator.group.Update;
import com.yanyan.core.lang.BaseDomain;
import com.yanyan.data.domain.system.vo.StaffVo;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 部门
 * User: Saintcy
 * Date: 2015/8/21
 * Time: 23:15
 */
@Data
public class Department extends BaseDomain {
    @NotNull(message = "id不能为空", groups = Update.class)
    private Long id;
    @NotNull(message = "所属企业不能为空", groups = Create.class)
    private Long corp_id;
    @NotBlank(message = "部门名称不能为空")
    @Length(max = 50, message = "企业名称长度必须小于{max}")
    private String name;
    @Length(max = 200, message = "拼音全拼长度必须小于{max}")
    private String pinyin;
    @Length(max = 50, message = "拼音简码长度必须小于{max}")
    private String pyabbr;
    @NotNull(message = "上级部门不能为空")
    private Long parent_id;
    @Length(max = 1000, message = "备注长度必须小于{max}")
    private String remarks;
    private Integer priority;


    /**Input Only Parameters*/
    /***/
    private List<Long> manager_id;

    /**Output Only Parameters*/
    /***/
    @OutputOnly
    private Integer status;
    @OutputOnly
    private String corp_name;
    @OutputOnly
    private String parent_name;
    @OutputOnly
    private boolean hasChildren;
    @OutputOnly
    private String path;
    @OutputOnly
    private String level;
    @OutputOnly
    private List<StaffVo> managers;

    /**Transient(Not Input and Output) Parameters*/
    /***/
    //No fields

}
