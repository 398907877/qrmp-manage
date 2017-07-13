package com.yanyan.data.query.system;

import com.yanyan.core.lang.PageQuery;
import lombok.Data;

/**
 * User: Saintcy
 * Date: 2016/5/3
 * Time: 14:36
 */
@Data
public class DictionaryEntryQuery extends PageQuery {
    private String code;
    private Long group_id;
    private String keyword;
}
