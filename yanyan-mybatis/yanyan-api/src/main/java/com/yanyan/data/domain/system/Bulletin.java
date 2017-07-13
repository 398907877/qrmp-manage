package com.yanyan.data.domain.system;

import com.yanyan.core.lang.BaseDomain;
import com.yanyan.core.serialize.exclusion.annotation.OutputOnly;
import com.yanyan.core.spring.validator.group.Create;
import com.yanyan.core.spring.validator.group.Update;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 公告
 * User: Saintcy
 * Date: 2015/8/21
 * Time: 23:26
 */
@Data
public class Bulletin extends BaseDomain {
    /**
     * 公告id
     */
    @NotNull(message = "ID不能为空", groups = Update.class)
    private Long id;
    /**
     * 公告标题
     */
    @NotBlank(message = "公告标题不能为空")
    @Length(max = 100, message = "公告标题长度必须小于{max}")
    private String title;
    /**
     * 公告内容
     */
    @NotBlank(message = "公告内容不能为空")
    @Length(max = 1000, message = "公告内容长度必须小于{max}")
    private String content;
    /**
     * 发布时间
     */
    private Date publish_time;
    /**
     * 生效时间
     */
    private Date effective_time;
    /**
     * 失效时间
     */
    private Date expiry_time;
    /**
     * 企业id
     */
    @NotNull(message = "企业id不能为空", groups = Create.class)
    private Long corp_id;

    /**Input Only Parameters*/
    /***/
    //No fields

    /**Output Only Parameters*/
    /***/
    /**
     * 状态。1：正常；0：删除
     */
    @OutputOnly
    private Integer status;

    /**Transient(Not Input and Output) Parameters*/
    /***/
    //No fields
}
