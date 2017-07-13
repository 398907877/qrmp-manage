package com.yanyan.core.db.mybatis.spring;

import com.yanyan.core.db.mybatis.binding.CustomMapperRegistry;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * User: Saintcy
 * Date: 2017/3/30
 * Time: 14:02
 */
public class CustomSqlSessionFactoryBean extends SqlSessionFactoryBean {
    private static final Log logger = LogFactory.getLog(SqlSessionFactoryBean.class);

    @Override
    protected SqlSessionFactory buildSqlSessionFactory() throws IOException {
        SqlSessionFactory sqlSessionFactory = super.buildSqlSessionFactory();
        try {
            Configuration config = sqlSessionFactory.getConfiguration();
            Class<?> classConfig = Configuration.class;
            Field field = classConfig.getDeclaredField("mapperRegistry");
            field.setAccessible(true);
            field.set(config, new CustomMapperRegistry(config));
        } catch ( Exception e ) {
            logger.error(e.getMessage(), e);
        }

        return sqlSessionFactory;
    }
}
