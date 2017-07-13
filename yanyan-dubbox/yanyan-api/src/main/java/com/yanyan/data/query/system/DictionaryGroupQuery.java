package com.yanyan.data.query.system;

import com.yanyan.core.lang.PageQuery;
import lombok.Data;

/**
 * User: Saintcy
 * Date: 2016/8/12
 * Time: 14:48
 */
@Data
public class DictionaryGroupQuery extends PageQuery {
    private String code;
    private String keyword;
}
