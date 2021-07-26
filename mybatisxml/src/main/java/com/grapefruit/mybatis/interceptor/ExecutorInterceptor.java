package com.grapefruit.mybatis.interceptor;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

/**
 * Executor 拦截器(modify sql)
 *
 * @author zhihuangzhang
 * @version 1.0
 * @date 2021-07-11 12:04 下午
 */
@Intercepts({@Signature(
        type = Executor.class,
        method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
public class ExecutorInterceptor implements Interceptor {
    private Properties properties = new Properties();

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        //获取target
        Executor executor = (Executor) invocation.getTarget();

        //获取args
        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        Integer parameter = (Integer) args[1];
        RowBounds rowBounds = (RowBounds)args[2];
        ResultHandler resultHandler = (ResultHandler)args[3];

        // get boundSql
        BoundSql boundSql = ms.getBoundSql(parameter);
        // get baseSql
        String sql = boundSql.getSql();

        // modify sql
        String newSql = "select id from grape where id = ?";

        // update newSql to boundSql
        Class<? extends BoundSql> aClass = boundSql.getClass();
        Field sqlField = aClass.getDeclaredField("sql");
        sqlField.setAccessible(true);
        sqlField.set(boundSql, newSql);

        CacheKey key = executor.createCacheKey(ms, parameter, rowBounds, boundSql);

        // execute method
        return executor.query(ms, parameter, rowBounds, resultHandler, key, boundSql);
    }

    @Override
    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}