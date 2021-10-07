/*
 *Copyright @2021 Grapefruit. All rights reserved.
 */

package com.grapefruit.mybatis.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * t_student
 *
 * @author zhihuangzhang
 * @version 1.0
 * @date 2021-10-07 11:48 上午
 */
@Getter
@Setter
public class TStudent {
    private Integer id;

    private Integer age;

    private String classId;

    private String name;

    private String favourite;

    private String upLimit;

    private List<TClass> classList;

    private TClass tClass;
}