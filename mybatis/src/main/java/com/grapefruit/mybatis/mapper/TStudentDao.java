/*
 *Copyright @2021 Grapefruit. All rights reserved.
 */

package com.grapefruit.mybatis.mapper;

import com.grapefruit.mybatis.entity.TStudent;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * TStudentDao
 *
 * @author zhihuangzhang
 * @version 1.0
 * @date 2021-10-07 11:48 上午
 */
@Mapper
public interface TStudentDao {
    List<TStudent> selectByPrimaryKey(Integer id);

    List<TStudent> selectAll();
}