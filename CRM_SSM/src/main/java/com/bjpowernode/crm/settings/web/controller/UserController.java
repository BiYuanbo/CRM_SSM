package com.bjpowernode.crm.settings.web.controller;

import com.bjpowernode.crm.commons.bean.ReturnObject;
import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.utils.DateTimeUtil;
import com.bjpowernode.crm.commons.utils.MD5Util;
import com.bjpowernode.crm.settings.bean.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.sun.deploy.net.HttpResponse;
import org.apache.poi.ss.usermodel.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    //启动服务器后跳转到登陆界面
    @RequestMapping("/settings/qx/user/toLogin.do")
    public String toLogin(){
        //使用请求转发，因为若有数据，重定向不能传数据，而且重定向不能访问/WEB-INF资源
        //已经配置视图解析器，省去前缀后缀
        return "settings/qx/user/login";
    }

    //输入账号密码实现登录操作
    @RequestMapping("/settings/qx/user/login.do")
    @ResponseBody
    public Object login(String loginAct, String loginPwd, String isRemPwd, HttpServletResponse response,HttpServletRequest request, HttpSession session){
        //获取当前用户的ip地址
        String ip = request.getRemoteAddr();

        //封装参数
        Map<String,Object> map = new HashMap<>();
        map.put("loginAct",loginAct);
        map.put("loginPwd", MD5Util.getMD5(loginPwd));

        //调用Service层方法，查询用户
        User user = userService.selectUserByLoginActAndPwd(map);
        //根据查询结果生成响应信息
        ReturnObject returnObject = new ReturnObject();
        if (user==null){
            //未查询到用户
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("用户名或密码错误");
        }else if (DateTimeUtil.getSysTime().compareTo(user.getExpiretime())>0){
            //当前账号过期
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("账号已过期");
        }else if ("0".equals(user.getLockstate())){
            //当前账号锁定
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("账号已锁定");
        }else if (!user.getAllowips().contains(ip)){
            //ip地址错误
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("ip地址错误");
        }else {
            //登录成功
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            //将user相关信息保存在会话作用域(session)
            session.setAttribute(Contants.SESSION_USER,user);

            //如果需要记住密码，则往外写cookie
            if("true".equals(isRemPwd)){
                Cookie c1 = new Cookie("loginAct",loginAct);
                c1.setMaxAge(10*24*60*60);
                response.addCookie(c1);
                Cookie c2 = new Cookie("loginPwd",loginPwd);
                c2.setMaxAge(10*24*60*60);
                response.addCookie(c2);
            }else {
                Cookie c1 = new Cookie("loginAct","1");
                c1.setMaxAge(0);
                response.addCookie(c1);
                Cookie c2 = new Cookie("loginPwd","1");
                c2.setMaxAge(0);
                response.addCookie(c2);
            }
        }

        //返回该对象
        return returnObject;
    }

    @RequestMapping("/settings/qx/user/logout.do")
    public String logout(HttpServletResponse response,HttpSession session){
        //清除cookie
        Cookie c1 = new Cookie("loginAct","1");
        c1.setMaxAge(0);
        response.addCookie(c1);
        Cookie c2 = new Cookie("loginPwd","1");
        c2.setMaxAge(0);
        response.addCookie(c2);
        //清除session
        session.invalidate();
        //通过重定向返回登录页面
        return "redirect:/";
    }


}
