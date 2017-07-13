package com.yanyan.core.db;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import javax.sql.DataSource;

/**
 * 扩展Spring原生的JdbcDaoSupport以支持扩展的NamedParameterJdbcTemplate
 * User: Saintcy
 * Date: 2015/6/29
 * Time: 16:06
 */
public class NamedParameterJdbcDaoSupport extends JdbcDaoSupport {
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    protected void initTemplateConfig() {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(this.getJdbcTemplate());
    }

    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
        return this.namedParameterJdbcTemplate;
    }

    @Autowired
    public void setDs(DataSource dataSource){
        super.setDataSource(dataSource);
    }
}
