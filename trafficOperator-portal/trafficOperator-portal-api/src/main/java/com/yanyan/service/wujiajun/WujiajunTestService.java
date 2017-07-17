package com.yanyan.service.wujiajun;

import java.util.List;



import com.yanyan.core.lang.Page;
import com.yanyan.data.domain.wujiajun.WujiajunTest;

/**
 * 测试代码Service
 * 
 * @author wujiajun
 * @version 2017-07-13
 */

public interface WujiajunTestService {

	WujiajunTest get(String id);

	List<WujiajunTest> findList(WujiajunTest wujiajunTest);

	public Page<WujiajunTest> findPage(Page<WujiajunTest> page, WujiajunTest wujiajunTest);

	public void save(WujiajunTest wujiajunTest);

	public void delete(WujiajunTest wujiajunTest);

}