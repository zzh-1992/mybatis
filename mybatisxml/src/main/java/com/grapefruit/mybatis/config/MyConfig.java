/*
 *Copyright @2021 Grapefruit. All rights reserved.
 */

package com.grapefruit.mybatis.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.grapefruit.mybatis.interceptor.SqlLogInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * 配置类
 *
 * @author zhihuangzhang
 * @version 1.0
 * @date 2021-06-24 11:24 下午
 */
@Configuration
public class MyConfig {
    @Bean
    public DataSource getDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUsername("root");
        dataSource.setPassword("123456");
        dataSource.setUrl("jdbc:mysql://127.0.0.1:3306/grapefruit?useUnicode=true&characterEncoding=UTF-8&useSSL=false");
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        return dataSource;
    }

    /**
     * 注册插件
     *
     * @return 自定义插件
     */
    @Bean
    public SqlLogInterceptor sqlLogInterceptor() {
        SqlLogInterceptor myPlugin = new SqlLogInterceptor();
        //设置参数，比如阈值等，可以在配置文件中配置，这里直接写死便于测试
        Properties properties = new Properties();
        //这里设置慢查询阈值为1毫秒，便于测试
        properties.setProperty("time", "1");
        myPlugin.setProperties(properties);
        return myPlugin;
    }
}
