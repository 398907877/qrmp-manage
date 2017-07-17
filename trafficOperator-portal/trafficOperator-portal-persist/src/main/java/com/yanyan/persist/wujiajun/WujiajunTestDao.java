/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.yanyan.persist.wujiajun;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.yanyan.core.lang.Page;
import com.yanyan.data.domain.system.Staff;
import com.yanyan.data.domain.system.vo.RoleVo;
import com.yanyan.data.domain.wujiajun.WujiajunTest;
import com.yanyan.data.query.system.StaffQuery;
import com.yanyan.data.query.system.WujiajunQuery;


/**
 * 测试代码DAO接口
 * @author wujiajun
 * @version 2017-07-13
 */

public interface WujiajunTestDao {
	
	
//add
    void insertWujiajunTest(WujiajunTest wujiajuntest);

    //modify
    void updateWujiajunTest(WujiajunTest wujiajuntest);

    //delete
    void deleteWujiajunTest(Long id);


//get

    WujiajunTest getWujiajunTest(Long id);
    
    
    
    
    Page<WujiajunTest> findWujiajunTest(WujiajunQuery query);
    
    List<RoleVo> getAllWujiajunTest(Long wujiajun_id);
    


   

	
	
}