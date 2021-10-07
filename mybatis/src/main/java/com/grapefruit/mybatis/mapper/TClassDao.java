/*
 *Copyright @2021 Grapefruit. All rights reserved.
 */

package com.grapefruit.mybatis.mapper;

import com.grapefruit.mybatis.entity.TClass;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * TClassDao
 *
 * @author zhihuangzhang
 * @version 1.0
 * @date 2021-10-07 11:48 上午
 */
@Mapper
public interface TClassDao {

    List<TClass> selectAll();

    TClass selectById(Integer id);
}