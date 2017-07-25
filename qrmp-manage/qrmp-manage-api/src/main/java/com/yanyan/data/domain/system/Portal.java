package com.yanyan.data.domain.system;

import com.yanyan.core.serialize.exclusion.annotation.OutputOnly;
import com.yanyan.core.spring.validator.group.Update;
import com.yanyan.core.lang.BaseDomain;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * 门户
 * User: Saintcy
 * Date: 2016/5/3
 * Time: 15:44
 */
@Data
public class Portal extends BaseDomain {
    @NotNull(message = "id不能为空", groups = Update.class)
    private Long id;
    @NotBlank(message = "门户编码不能为空")
    @Length(max = 100, message = "门户编码长度必须小于{max}")
    private String code;
    @NotBlank(message = "门户名称不能为空")
    @Length(max = 50, message = "门户名称长度必须小于{max}")
    private String name;
    @Length(max = 1000, message = "门户备注长度必须小于{max}")
    private String remarks;
    private Integer priority;

    @Length(max = 32, message = "应用key长度必须小于{max}")
    private String app_key;
    @Length(max = 128, message = "应用安全码长度必须小于{max}")
    private String app_secret;
    @Length(max = 1024, message = "应用私钥长度必须小于{max}")
    private String private_secret;

    /**Input Only Parameters*/
    /***/
    //No fields

    /**Output Only Parameters*/
    /***/
    @OutputOnly
    private Integer status;

    /**Transient(Not Input and Output) Parameters*/
    /***/
    //No fields
}
