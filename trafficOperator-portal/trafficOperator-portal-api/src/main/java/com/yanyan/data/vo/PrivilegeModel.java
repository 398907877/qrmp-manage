package com.yanyan.data.vo;

import com.yanyan.data.domain.system.Resource;
import lombok.Data;

/**
 * 权限模型
 * User: Saintcy
 * Date: 2016/8/15
 * Time: 16:30
 */
@Data
public class PrivilegeModel extends Resource {
    private boolean hasRight;
}
