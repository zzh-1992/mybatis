/*
 *Copyright @2021 Grapefruit. All rights reserved.
 */

package com.grapefruit.mybatis.mapper;

import com.grapefruit.mybatis.Model.Grape;
import org.apache.ibatis.annotations.Mapper;

/**
 * 相关描述
 *
 * @author zhihuangzhang
 * @version 1.0
 * @date 2021-06-24 11:38 下午
 */
@Mapper
public interface GrapeMapper {

    String column = "id,name,num,content";

    Grape selectGrape(int id);

    //@Select("select " + column + " from grape where id = #{id}")
    //Grape selectGrape(int id);
}
