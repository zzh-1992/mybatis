package com.grapefruit.mybatis.interceptor;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.Properties;

/**
 * 自定义slq日志拦截器
 *
 * @author zhihuangzhang
 * @version 1.0
 * @date 2021-07-11 12:04 下午
 */
@Intercepts({@Signature(
        type = StatementHandler.class,
        method = "prepare",
        args = {Connection.class, Integer.class})})
public class SqlLogInterceptor implements Interceptor {
    private Properties properties = new Properties();

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        //被代理对象
        Object target = invocation.getTarget();
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        BoundSql boundSql = statementHandler.getBoundSql();
        String sql = boundSql.getSql();

        //代理方法
        Method method = invocation.getMethod();

        //方法参数
        Object[] args = invocation.getArgs();


        // implement pre-processing if needed(前置处理)
        Object returnObject = invocation.proceed();
        // implement post-processing if needed(后置处理)
        return returnObject;
    }

    @Override
    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}