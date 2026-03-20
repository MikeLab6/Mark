package com.example.bean;

import cn.hutool.core.util.NumberUtil;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 应用模块名称:
 * 代码描述:
 * copyright: YUNDASYS ALL RIGHTS RESERVED
 * company: YUNDA
 *
 * @author: 杨洪飞
 * @date: 2022/03/17 14:15:40
 */
@Data
public class Planet {
    public static final BigDecimal g = new BigDecimal("6.67");


    String engNm;
    String chiNm;
    BigDecimal M;
    BigDecimal R;

    public Planet(BigDecimal m, BigDecimal r) {
        this.M = m;
        this.R = r;
    }

    public BigDecimal calF(BigDecimal m) {
        return NumberUtil.div(NumberUtil.mul(m, g, this.getM()), NumberUtil.pow(this.getR(), 2)).divide(NumberUtil.pow(10, 11), 2, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getMByPlanet(String gravity) {
        return NumberUtil.div(NumberUtil.mul(new BigDecimal(gravity), this.getR(), this.getR(), NumberUtil.pow(10, 11)), NumberUtil.mul(g, this.getM()));
    }


}
