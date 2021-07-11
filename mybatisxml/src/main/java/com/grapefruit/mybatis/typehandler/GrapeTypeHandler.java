/*
 *Copyright @2021 Grapefruit. All rights reserved.
 */

package com.grapefruit.mybatis.typehandler;

import com.alibaba.fastjson.JSON;
import com.grapefruit.mybatis.Model.E;
import com.grapefruit.mybatis.Model.Grape;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 自定义类处理器--将属性转化为json字符串保存在数据库中,获取的时候将json字符串转化为java对象
 * (              springboot方式:
 *                      1:在自定义处理器上加@MappedTypes(E.class)
 *                      2:在application.properties中注册处理器
 *                          mybatis.type-handlers-package=com.grapefruit.mybatis.MyTypeHandler
 *
 *                mybatis配置文件方式注册:
 *                      <typeHandlers>
 *                          <typeHandler handler="com.grapefruit.mybatis.MyTypeHandler.GrapeTypeHandler" jdbcType="VARCHAR"
 *                              javaType="com.grapefruit.mybatis.Model.E"/>
 *                      </typeHandlers>
 *                          )
 *
 * @author zhihuangzhang
 * @version 1.0
 * @date 2021-06-27 11:21 上午
 */
@MappedTypes({E.class, Grape.class})
public class GrapeTypeHandler<T> implements TypeHandler<T> {

    private final Class<T> clazz;

    public GrapeTypeHandler(Class<T> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        }
        this.clazz = clazz;
    }

    @Override
    public void setParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
        String jsonString = JSON.toJSONString(parameter);
        ps.setString(i, jsonString);
    }

    @Override
    public T getResult(ResultSet rs, String columnName) throws SQLException {
        String string = rs.getString(columnName);
        return JSON.parseObject(string, clazz);
    }

    @Override
    public T getResult(ResultSet rs, int columnIndex) throws SQLException {
        String string = rs.getString(columnIndex);
        return JSON.parseObject(string, clazz);
    }

    @Override
    public T getResult(CallableStatement cs, int columnIndex) throws SQLException {
        String string = cs.getString(columnIndex);
        return JSON.parseObject(string, clazz);
    }
}
