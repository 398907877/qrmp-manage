package com.yanyan.data.query.system;

import com.yanyan.core.lang.PageQuery;
import lombok.Data;

import java.util.Date;

/**
 * 人员查询过滤器
 * User: Saintcy
 * Date: 2015/8/21
 * Time: 17:04
 */
@Data
public class WujiajunQuery extends PageQuery {

    private Long id; //id
	private String name;		// name
	private String sex;		// sex
	
}
