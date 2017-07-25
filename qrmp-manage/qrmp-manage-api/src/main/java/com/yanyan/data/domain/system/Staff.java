package com.yanyan.data.domain.system;

import com.yanyan.Configs;
import com.yanyan.core.serialize.exclusion.annotation.InputOnly;
import com.yanyan.core.serialize.exclusion.annotation.NoTransfer;
import com.yanyan.core.serialize.exclusion.annotation.OutputOnly;
import com.yanyan.core.spring.validator.group.*;
import com.yanyan.core.lang.BaseDomain;
import com.yanyan.data.domain.system.vo.RoleVo;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * 人员信息
 * User: Saintcy
 * Date: 2015/8/21
 * Time: 16:44
 */
@Data
public class Staff extends BaseDomain {
    @NotNull(message = "id不能为空", groups = Update.class)
    private Long id;
    @NotBlank(message = "用户名不能为空")
    @Length(max = 50, message = "用户名长度必须小于{max}")
    private String account;
    @NotBlank(message = "真实姓名不能为空")
    @Length(max = 50, message = "真实姓名长度必须小于{max}")
    private String name;
    @Length(max = 200, message = "拼音全拼长度必须小于{max}")
    private String pinyin;
    @Length(max = 50, message = "拼音简码长度必须小于{max}")
    private String pyabbr;
    @NotBlank(message = "手机号码不能为空")
    @Length(max = 20, message = "手机号码长度必须小于{max}")
    @Pattern(regexp = Configs.MOBILE_REGEXP, message = "手机号码格式不正确")
    private String cellphone;
    //@NotBlank(message = "电子邮箱不能为空")
    @Length(max = 50, message = "电子邮箱长度必须小于{max}")
    //@Email(regexp = Configs.EMAIL_REGEXP, message = "电子邮箱格式不正确")
    private String email;
    @NotNull(message = "企业id不能为空", groups = Create.class)
    private Long corp_id;
    private Integer is_admin;
    @NotNull(message = "所属部门不能为空")
    private Long dept_id;
    @Length(max = 50, message = "职务长度必须小于{max}")
    private String duty;
    private Integer priority;

    /**Input Only Parameters*/
    @InputOnly
    @NotBlank(message = "密码不能为空", groups = Create.class)
    @Length(max = 20, message = "密码长度必须小于{max}", groups = Create.class)
    private String password;
    @InputOnly
    @NotEmpty(message = "角色不能为空")
    private List<Long> role_id;

    /**Output Only Parameters*/
    @OutputOnly
    private Integer is_lock;//是否锁定，1: 是 0:否
    @OutputOnly
    private String corp_name;
    @OutputOnly
    private Long portal_id;
    @OutputOnly
    private String portal_code;
    @OutputOnly
    private String dept_name;
    @OutputOnly
    private List<RoleVo> roles;

    /**Transient(Not Input and Output) Parameters*/
    @NoTransfer
    private String salt;
}
