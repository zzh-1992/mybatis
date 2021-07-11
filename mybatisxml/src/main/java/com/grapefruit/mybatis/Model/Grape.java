/*
 *Copyright @2021 Grapefruit. All rights reserved.
 */

package com.grapefruit.mybatis.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

/**
 * 实体
 *
 * @author zhihuangzhang
 * @version 1.0
 * @date 2021-05-28 10:27 下午
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Grape {
    private int id;
    private String name;
    private BigInteger num;
    private E content;

    @Override
    public String toString() {
        return "Grape{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", num=" + num +
                ", content='" + content + '\'' +
                '}';
    }
}
