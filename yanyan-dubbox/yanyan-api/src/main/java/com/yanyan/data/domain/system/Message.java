package com.yanyan.data.domain.system;

import com.yanyan.core.serialize.exclusion.annotation.OutputOnly;
import com.yanyan.core.spring.validator.group.Update;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * 消息
 * User: Saintcy
 * Date: 2015/3/6
 * Time: 14:36
 */
@Data
public class Message implements Serializable {
    @NotNull(message = "id不能为空", groups = Update.class)
    private Long id;
    @NotNull
    private String business_code;
    private Long business_id;
    @NotNull
    private Long from_staff_id;
    @NotNull
    private Long to_staff_id;
    @NotNull
    private String content;
    private Date post_time;
    private Date read_time;
    @NotNull
    private Long corp_id;

    /**Input Only Parameters*/
    /***/
    //No fields

    /**Output Only Parameters*/
    /***/
    @OutputOnly
    private String from_staff_name;
    @OutputOnly
    private String to_staff_name;
    @OutputOnly
    private String corp_name;

    /**Transient(Not Input and Output) Parameters*/
    /***/
    //No fields
}
