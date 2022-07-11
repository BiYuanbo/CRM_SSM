package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.bean.ReturnObject;
import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.utils.DateTimeUtil;
import com.bjpowernode.crm.commons.utils.UUIDUtil;
import com.bjpowernode.crm.settings.bean.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.workbench.bean.*;
import com.bjpowernode.crm.workbench.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ClueController {
    @Autowired
    private UserService userService;

    @Autowired
    private DicValueService dicValueService;

    @Autowired
    private ClueService clueService;

    @Autowired
    private ClueRemarkService clueRemarkService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ClueActivityRelationService clueActivityRelationService;

    //跳转到线索主页面,并获取数据字典数据
    @RequestMapping("/workbench/clue/index.do")
    public String index(HttpServletRequest request){
        List<User> userList = userService.selectAllUsers();

        request.setAttribute("userList",userList);

        List<DicValue> appellationList = dicValueService.selectDicValueByTypeCode("appellation");
        List<DicValue> clueStateList = dicValueService.selectDicValueByTypeCode("clueState");
        List<DicValue> sourceList = dicValueService.selectDicValueByTypeCode("source");

        request.setAttribute("appellationList",appellationList);
        request.setAttribute("clueStateList",clueStateList);
        request.setAttribute("sourceList",sourceList);

        return "workbench/clue/index";
    }

    //添加线索列表
    @RequestMapping("/workbench/clue/insertClue.do")
    @ResponseBody
    public Object insertClue(Clue clue, HttpSession session){
        User user = (User) session.getAttribute(Contants.SESSION_USER);

        clue.setId(UUIDUtil.getUUID());
        clue.setCreateTime(DateTimeUtil.getSysTime());
        clue.setCreateBy(user.getName());

        ReturnObject ro = new ReturnObject();

        try {
            int result = clueService.insertClue(clue);
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

    //条件查询线索列表
    @RequestMapping("/workbench/clue/selectClueByConditionForPage.do")
    @ResponseBody
    public Object selectClueByConditionForPage(String fullname,String company,String owner,String phone,String source,String state,
                                               int pageNo,int pageSize){
        Map<String,Object> map = new HashMap<>();
        map.put("fullname",fullname);
        map.put("owner",owner);
        map.put("company",company);
        map.put("phone",phone);
        map.put("source",source);
        map.put("state",state);
        map.put("pageNo",(pageNo-1)*pageSize);
        map.put("pageSize",pageSize);

        List<Clue> clueList = clueService.selectClueByConditionForPage(map);
        int total = clueService.selectCountOfClueByCondition(map);

        Map<String,Object> retMap = new HashMap<>();
        retMap.put("clueList",clueList);
        retMap.put("total",total);

        return retMap;
    }

    //跳转到详情信息页面
    @RequestMapping("/workbench/clue/detail.do")
    public Object detail(String id,HttpServletRequest request){
        Clue clue = clueService.selectClueForDetailById(id);

        List<ClueRemark> clueRemarkList = clueRemarkService.selectClueRemarkByClueId(id);

        List<Activity> activityList = activityService.selectActivityForDetailByClueId(id);

        request.setAttribute("clue",clue);
        request.setAttribute("clueRemarkList",clueRemarkList);
        request.setAttribute("activityList",activityList);

        return "workbench/clue/detail";
    }

    //根据名称模糊查询市场活动并且排除以及关联过的
    @RequestMapping("/workbench/clue/getActivityListByNameAndNotByClueId.do")
    @ResponseBody
    public Object getActivityListByNameAndNotByClueId(String aname,String clueId){
        //封装参数
        Map<String,Object> map = new HashMap<>();
        map.put("aname",aname);
        map.put("clueId",clueId);

        List<Activity> activityList = activityService.getActivityListByNameAndNotByClueId(map);

        return activityList;
    }

    //给关联按钮绑定事件
    @RequestMapping("/workbench/clue/insertClueActivityRelation.do")
    @ResponseBody
    public Object insertClueActivityRelation(String[] activityId,String clueId){
        ClueActivityRelation clueActivityRelation = null;
        List<ClueActivityRelation> list = new ArrayList<>();
        for (String ai : activityId) {
            clueActivityRelation = new ClueActivityRelation();

            clueActivityRelation.setId(UUIDUtil.getUUID());
            clueActivityRelation.setActivityId(ai);
            clueActivityRelation.setClueId(clueId);

            list.add(clueActivityRelation);
        }

        ReturnObject ro = new ReturnObject();
        try {
            int num = clueActivityRelationService.insertClueActivityRelation(list);
            if (num>0){
                ro.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);

                List<Activity> activityList = activityService.selectActivityForDetailByIds(activityId);

                ro.setOretData(activityList);
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

    //解除关联
    @RequestMapping("/workbench/clue/unbund.do")
    @ResponseBody
    public Object unbund(ClueActivityRelation clueActivityRelation){
        ReturnObject ro = new ReturnObject();
        try {
            int num = clueActivityRelationService.unbund(clueActivityRelation);
            if (num == 1){
                ro.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                ro.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                ro.setMessage("系统忙碌，请稍后");
            }
        }catch (Exception e){
            e.printStackTrace();
            ro.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            ro.setMessage("系统忙碌，请稍后");
        }
        return ro;
    }

    //跳转到转化界面
    @RequestMapping("/workbench/clue/convert.do")
    public String convert(String id,HttpServletRequest request){
        Clue clue = clueService.selectClueForDetailById(id);
        List<DicValue> dicValueList = dicValueService.selectDicValueByTypeCode("stage");
        request.setAttribute("clue",clue);
        request.setAttribute("dicValueList",dicValueList);
        return "workbench/clue/convert";
    }

    //根据name模糊查询市场活动，并且查询那些跟clueId关联过的市场活动
    @RequestMapping("/workbench/clue/getActivityListByNameAndAndClueId.do")
    @ResponseBody
    public Object getActivityListByNameAndAndClueId(String aname,String clueId){
        //封装参数
        Map<String,Object> map = new HashMap<>();
        map.put("aname",aname);
        map.put("clueId",clueId);

        List<Activity> activityList = activityService.getActivityListByNameAndAndClueId(map);

        return activityList;
    }

    //保存线索转换
    @RequestMapping("/workbench/clue/saveConvert.do")
    @ResponseBody
    public Object saveConvert(String clueId,String money,String name,String expectedDate,String stage,String activityId,String isCreateTran,HttpSession session){
        Map<String,Object> map = new HashMap<>();
        map.put("clueId",clueId);
        map.put("money",money);
        map.put("name",name);
        map.put("expectedDate",expectedDate);
        map.put("stage",stage);
        map.put("activityId",activityId);
        map.put("isCreateTran",isCreateTran);
        map.put(Contants.SESSION_USER,session.getAttribute(Contants.SESSION_USER));

        ReturnObject ro = new ReturnObject();
        try {
            clueService.saveConvert(map);

            ro.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            ro.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            ro.setMessage("系统忙，请稍后。。。");
        }

        return ro;
    }
}
