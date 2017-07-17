package com.yanyan.data.query.system;

import com.yanyan.core.lang.PageQuery;
import lombok.Data;

/**
 * 附件查询条件
 * User: Saintcy
 * Date: 2016/9/1
 * Time: 10:55
 */
@Data
public class AttachmentQuery extends PageQuery {
    private Long ref_id;
    private Integer ref_type;
    private Integer ref_sub_type;
}
