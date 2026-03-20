package com.example.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.example.bean.Planet;
import com.example.bean.User;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;

/**
 * 应用模块名称:
 * 代码描述:
 * copyright: YUNDASYS ALL RIGHTS RESERVED
 * company: YUNDA
 *
 * @author: 杨洪飞
 * @date: 2021/08/09 12:52:19
 */
@RestController
public class TestController {
    //    数据分段每段数量
    public static final int PAGE_NUM = 10000;


    @RequestMapping(value = "/test", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public BigDecimal cal(String gravity, HttpServletResponse response) {


//f*R*R/(G*m)
        Planet earth = new Planet(NumberUtil.mul(5.975, NumberUtil.pow(10, 24)), NumberUtil.mul(6.378, NumberUtil.pow(10, 6)));
        BigDecimal m = earth.getMByPlanet(gravity);


        Planet water = new Planet(NumberUtil.mul(3.302, NumberUtil.pow(10, 23)), NumberUtil.mul(2.439, NumberUtil.pow(10, 6)));


        return water.calF(m);


    }

    @GetMapping(value = "/getAgeNumByRange")
    public int getAgeNumByRange(User user) {
        ExcelReader excelReader = ExcelUtil.getReader("C:\\Users\\YHF\\Desktop\\111.csv");
//        假设年龄0到150
        int[] ages = new int[150];
        int total = excelReader.getRowCount();
        int segNum = total / PAGE_NUM;
        for (int i = 0, l = segNum; i <= l; i++) {
            List<List<Object>> data = excelReader.read(i * PAGE_NUM, NumberUtil.min((i + 1) * PAGE_NUM - 1, total - 1));
            if (CollUtil.isNotEmpty(data)) {
                for (List<Object> item : data) {
                    if (item.get(3) != null) {
                        ages[Integer.valueOf(item.get(3) + "")]++;
                    }
                }
            }

        }
        int result = 0;
        for (int i = user.getAgeMin(), l = user.getAgeMax(); i <= l; i++) {
            result += ages[i];
        }
        return result;
    }


}
