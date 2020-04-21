package org.zyx.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by SunShine on 2020/4/21.
 */
@Controller
public class WxHandler {


    @RequestMapping("/")
    public String index(){

        System.out.println("测试");

        return "index";
    }


}
