/*
 *Copyright @2021 Grapefruit. All rights reserved.
 */

package com.grapefruit.mybatis.Run;

import com.grapefruit.mybatis.Model.Grape;
import com.grapefruit.mybatis.mapper.GrapeMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;

/**
 * 相关描述
 *
 * @author zhihuangzhang
 * @version 1.0
 * @date 2021-06-27 10:52 上午
 */
public class MybatisXmlRunTest {

    // 原生配置方式(手动实现DataSourceFactory接口)
    public static void main(String[] args) throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        SqlSession session = sqlSessionFactory.openSession();
        GrapeMapper mapper = session.getMapper(GrapeMapper.class);

        Grape grape = mapper.selectGrape(1);
        Grape grape1 = mapper.selectGrape(1);

        //Grape grape = session.selectOne("selectBlog", 1);
        //Grape grape2 = session.selectOne("selectBlog", 1);

        System.out.println(grape);
        System.out.println(grape1);

        session.close();
    }

    @PostConstruct
    public void initTest() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        SqlSession session = sqlSessionFactory.openSession();
        //GrapeMapper mapper = session.getMapper(GrapeMapper.class);
        //Grape grape = mapper.selectGrape(1);
        //Grape grape1 = mapper.selectGrape(1);

        Grape grape = session.selectOne("selectGrape", 1);
        Grape grape1 = session.selectOne("selectGrape", 1);

        System.out.println(grape);
        System.out.println(grape1);

        session.close();
    }
}
