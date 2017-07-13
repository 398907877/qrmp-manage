package com.yanyan.data.query.system;

import com.yanyan.core.lang.PageQuery;
import lombok.Data;

/**
 * 门户查询条件
 * User: Saintcy
 * Date: 2016/5/3
 * Time: 15:44
 */
@Data
public class PortalQuery extends PageQuery {
    private String code;
    private String app_key;
    private String keyword;
    private String status;
}


