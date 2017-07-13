package com.yanyan.service.console;

import com.yanyan.data.domain.console.ModuleCodes;
import com.yanyan.data.domain.console.ModuleSetup;

/**
 * 代码生成服务
 * User: Saintcy
 * Date: 2016/10/25
 * Time: 9:50
 */
public interface GenerateService {
    ModuleCodes generate(ModuleSetup setup);
}
