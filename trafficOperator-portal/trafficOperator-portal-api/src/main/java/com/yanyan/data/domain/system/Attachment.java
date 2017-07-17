package com.yanyan.data.domain.system;

import com.yanyan.Configs;
import com.yanyan.core.serialize.exclusion.annotation.NoTransfer;
import com.yanyan.core.serialize.exclusion.annotation.OutputOnly;
import com.yanyan.core.lang.BaseDomain;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * 附件
 * User: Saintcy
 * Date: 2016/9/1
 * Time: 10:23
 */
@Data
public class Attachment extends BaseDomain {
    private Long id;
    @NotBlank(message = "文件名称不能为空")
    @Length(max = 256, message = "文件名称长度必须小于{max}")
    private String name;
    @NotNull(message = "文件大小不能为空")
    private Integer size;
    @Length(max = 100, message = "文件类型长度必须小于{max}")
    private String type;
    @Length(max = 256, message = "文件地址称长度必须小于{max}")
    private String url;
    private Long create_staff_id;
    private Integer priority;

    /**Input Only Parameters*/
    /***/
    //No fields

    /**Output Only Parameters*/
    /***/
    //No fields

    /**Transient(Not Input and Output) Parameters*/
    /***/
    @NoTransfer
    @NotNull(message = "业务类型不能为空")
    private Integer ref_type;
    @NoTransfer
    @NotNull(message = "业务子类型不能为空")
    private Integer ref_sub_type;
    @NoTransfer
    @NotNull(message = "业务ID不能为空")
    private Long ref_id;

    @Setter(AccessLevel.NONE)
    @OutputOnly
    private String fileUrl;
    @Setter(AccessLevel.NONE)
    @OutputOnly
    private String thumbnailUrl;

    //********以下基本能满足日常使用需要，如果从gson直接反射设置进来，再直接序列化，fileUrl是没有值的，此时需要手动设置下url值
    public void setUrl(String url) {
        if (StringUtils.isEmpty(url)) {
            this.url = "";
            fileUrl = "";
            thumbnailUrl = "";
        } else {
            this.url = url;
            fileUrl = Configs.FILE_URL_PREFIX + url;
            thumbnailUrl = Configs.THUMBNAIL_URL_PREFIX + url;
        }
    }

    public String getFileUrl() {
        return StringUtils.isEmpty(url) ? "" : (Configs.FILE_URL_PREFIX + url);
    }

    public String getThumbnailUrl() {
        return StringUtils.isEmpty(url) ? "" : (Configs.THUMBNAIL_URL_PREFIX + url);
    }

    public String getMetaJson() {
        return "{" +
                "\"name\":\"" + name + "\"" +
                ", \"size\":" + size +
                ", \"type\":\"" + type + "\"" +
                ", \"url\":\"" + url + "\"" +
                ", \"fileUrl\":\"" + getFileUrl() + "\"" +
                ", \"thumbnailUrl\":\"" + getThumbnailUrl() + "\"" +
                '}';
    }
}
