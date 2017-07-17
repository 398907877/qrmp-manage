package com.yanyan.data.domain.system;

import com.yanyan.Configs;
import com.yanyan.core.serialize.exclusion.annotation.InputOnly;
import com.yanyan.core.serialize.exclusion.annotation.OutputOnly;
import com.yanyan.core.spring.validator.group.Create;
import com.yanyan.core.spring.validator.group.Update;
import com.yanyan.core.lang.BaseDomain;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 企业信息
 * User: Saintcy
 * Date: 2015/8/21
 * Time: 23:56
 */
@Data
public class Corporation extends BaseDomain {
    @NotNull(message = "id不能为空", groups = Update.class)
    private Long id;
    @NotNull(message = "所属门户不能为空", groups = Create.class)
    private Long portal_id;
    @NotBlank(message = "企业名称不能为空")
    @Length(max = 100, message = "企业名称长度必须小于{max}")
    private String name;
    @Length(max = 200, message = "企业英文名称长度必须小于{max}")
    private String english_name;
    private Attachment logo;
    @NotNull(message = "所属省份不能为空")
    private Long province_id;
    @NotNull(message = "所属地市不能为空")
    private Long city_id;
    @NotNull(message = "所属县市不能为空")
    private Long county_id;
    @NotNull(message = "所属街道不能为空")
    private Long township_id;
    private String contact_man;
    @NotBlank(message = "手机号码不能为空")
    @Length(max = 20, message = "手机号码长度必须小于{max}")
    @Pattern(regexp = Configs.MOBILE_REGEXP, message = "手机号码格式不正确")
    private String contact_phone;
    private String address;
    private String postcode;
    private String fax;
    //@NotBlank(message = "电子邮箱不能为空")
    //@Length(max = 50, message = "电子邮箱长度必须小于{max}")
    //@Email(regexp = Configs.EMAIL_REGEXP, message = "电子邮箱格式不正确")
    private String email;
    private String website;
    @Length(max = 1000, message = "企业简介长度必须小于{max}")
    private String introduction;
    @NotBlank(message = "管理员账号不能为空", groups = Create.class)
    @Length(max = 100, message = "管理员账号长度必须小于{max}", groups = Create.class)
    private String admin_account;

    /**Input Only Parameters*/
    /***/
    @InputOnly
    @NotBlank(message = "管理员密码不能为空", groups = Create.class)
    @Length(max = 20, message = "管理员密码长度必须小于{max}", groups = Create.class)
    private String admin_password;

    /**Output Only Parameters*/
    /***/
    @OutputOnly
    private Long admin_id;
    @OutputOnly
    private String province_name;
    @OutputOnly
    private String city_name;
    @OutputOnly
    private String county_name;
    @OutputOnly
    private String township_name;
    @OutputOnly
    private String portal_name;
    @OutputOnly
    private Integer is_del;

    /**Transient(Not Input and Output) Parameters*/
    /***/
    //No fields
}
