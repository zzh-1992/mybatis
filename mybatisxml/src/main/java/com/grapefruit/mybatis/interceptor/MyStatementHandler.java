package com.grapefruit.mybatis.interceptor;

import org.apache.ibatis.executor.resultset.DefaultResultSetHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ResultMap;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

/**
 * ResultSetHandler 拦截器
 *
 * @author zhihuangzhang
 * @version 1.0
 * @date 2022-03-03 21:35 下午
 */
@Intercepts({@Signature(
        type = ResultSetHandler.class,
        method = "handleResultSets",
        args = {Statement.class})})
public class MyStatementHandler implements Interceptor {
    private Properties properties = new Properties();

    /**
     * this method is modified according to the original method
     *
     * @param invocation invocation
     * @return result
     * @throws Throwable Throwable
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        //获取target
        DefaultResultSetHandler handler = (DefaultResultSetHandler) invocation.getTarget();
        Field field = handler.getClass().getDeclaredField("mappedStatement");
        field.setAccessible(true);
        MappedStatement mappedStatement = (MappedStatement) field.get(handler);

        // 1.1 获取字段定义
        List<ResultMap> maps = mappedStatement.getResultMaps();

        //获取args
        Object[] args = invocation.getArgs();
        Statement ms = (Statement) args[0];

        // 1.2 获取值
        ResultSet rs = ms.getResultSet();

        System.out.println("============================");
        while (rs.next()) {
            // 处理结果集
            System.out.println("uid:" + rs.getString("uid"));
            System.out.println("name:" + rs.getString("name"));
            System.out.println("password:" + rs.getString("password"));
            System.out.println("email:" + rs.getString("email"));
            System.out.println("phone:" + rs.getString("phone"));
            System.out.println("device:" + rs.getString("device"));
        }
        System.out.println("============================");

        // 返回结果集
        return null;
    }

    public <T> T castParameter(Object arg, Class<T> tClass) {
        return tClass.cast(arg);
    }

    @Override
    public void setProperties(Properties properties) {
        this.properties = properties;
    }
}
