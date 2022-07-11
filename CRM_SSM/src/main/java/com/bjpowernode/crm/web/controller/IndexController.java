package com.bjpowernode.crm.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
    @RequestMapping("/")
    public String index(){
        //使用请求转发，因为若有数据，重定向不能传数据，而且重定向不能访问/WEB-INF资源
        //已经配置视图解析器，省去前缀后缀
        return "index";
    }
}
