package com.yanyan.data.domain.system;

import com.yanyan.core.lang.BaseDomain;
import lombok.Data;

/**
 * 操作
 * User: Saintcy
 * Date: 2016/5/3
 * Time: 15:57
 */
@Data
public class OperationLog extends BaseDomain {
    private String operation_id;
    private String operation_name;
    private String staff_id;
    private String corp_id;
    private String operate_time;
    private String operate_content;
}
