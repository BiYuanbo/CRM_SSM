package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.bean.ReturnObject;
import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.utils.DateTimeUtil;
import com.bjpowernode.crm.commons.utils.UUIDUtil;
import com.bjpowernode.crm.settings.bean.User;
import com.bjpowernode.crm.workbench.bean.ActivityRemark;
import com.bjpowernode.crm.workbench.service.ActivityRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
public class ActivityRemarkController {
    @Autowired
    private ActivityRemarkService activityRemarkService;

    //添加市场备注
    @RequestMapping("/workbench/activity/insertActivityRemark.do")
    @ResponseBody
    public Object insertActivityRemark(ActivityRemark activityRemark, HttpSession session){
        User user = (User) session.getAttribute(Contants.SESSION_USER);

        activityRemark.setId(UUIDUtil.getUUID());
        activityRemark.setCreateTime(DateTimeUtil.getSysTime());
        activityRemark.setCreateBy(user.getName());
        activityRemark.setEditFlag(Contants.REMARK_EDIT_FLAG_NEDIT);

        ReturnObject ro = new ReturnObject();

        try {
            int result = activityRemarkService.insertActivityRemark(activityRemark);

            if (result==1){
                ro.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                ro.setOretData(activityRemark);
            }else {
                ro.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                ro.setMessage("系统忙，请稍后重试");
            }
        }catch (Exception e){
            e.printStackTrace();

            ro.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            ro.setMessage("系统忙，请稍后重试");
        }

        return ro;
    }

    //删除市场备注
    @RequestMapping("/workbench/activity/deleteActivityRemarkById.do")
    @ResponseBody
    public Object deleteActivityRemarkById(String id){
        ReturnObject ro = new ReturnObject();
        try {
            int result = activityRemarkService.deleteActivityRemarkById(id);
            if (result == 1){
                ro.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                ro.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                ro.setMessage("系统忙碌，请稍后...");
            }
        }catch (Exception e){
            e.printStackTrace();
            ro.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            ro.setMessage("系统忙碌，请稍后...");
        }
        return ro;
    }

    //更改市场备注
    @RequestMapping("/workbench/activity/updateActivityRemark.do")
    @ResponseBody
    public Object updateActivityRemark(ActivityRemark activityRemark,HttpSession session){
        User user = (User) session.getAttribute(Contants.SESSION_USER);

        activityRemark.setEditFlag(Contants.REMARK_EDIT_FLAG_YEDIT);
        activityRemark.setEditTime(DateTimeUtil.getSysTime());
        activityRemark.setEditBy(user.getName());
        
        ReturnObject ro = new ReturnObject();

        try {
            int result = activityRemarkService.updateActivityRemark(activityRemark);

            if (result == 1){
                ro.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                ro.setOretData(activityRemark);
            }else {
                ro.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                ro.setMessage("系统繁忙，请稍后...");
            }
        }catch (Exception e){
            e.printStackTrace();
            ro.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            ro.setMessage("系统繁忙，请稍后");
        }

        return ro;
    }
}
