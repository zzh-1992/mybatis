/*
 *Copyright @2021 Grapefruit. All rights reserved.
 */

package com.grapefruit.mybatis.test;

import com.grapefruit.mybatis.entity.TClass;
import com.grapefruit.mybatis.entity.TStudent;
import com.grapefruit.mybatis.mapper.TClassDao;
import com.grapefruit.mybatis.mapper.TStudentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * mybatis 多对多查询(association collection)
 *
 * @author zhihuangzhang
 * @version 1.0
 * @date 2021-10-07 11:48 上午
 */
@Service
public class Test {

    @Autowired
    private TClassDao classDao;

    @Autowired
    private TStudentDao studentDao;

    @PostConstruct
    public void init() {
        List<TClass> list = classDao.selectAll();
        TClass myClass = classDao.selectById(2);

        List<TStudent> tStudent = studentDao.selectByPrimaryKey(38);
        List<TStudent> stuList = studentDao.selectAll();

        System.out.println();
    }
}
