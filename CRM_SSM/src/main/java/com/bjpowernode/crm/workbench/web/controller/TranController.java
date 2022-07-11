package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.commons.bean.ReturnObject;
import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.settings.bean.User;
import com.bjpowernode.crm.settings.service.UserService;
import com.bjpowernode.crm.workbench.bean.*;
import com.bjpowernode.crm.workbench.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

@Controller
public class TranController {
    @Autowired
    private UserService userService;

    @Autowired
    private DicValueService dicValueService;

    @Autowired
    private TranService tranService;

    @Autowired
    private TranRemarkService tranRemarkService;

    @Autowired
    private TranHistoryService tranHistoryService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private ContactsService contactsService;

    @Autowired
    private CustomerService customerService;

    //跳转到交易主页面,并获取数据字典数据
    @RequestMapping("/workbench/tran/index.do")
    public String index(HttpServletRequest request){
        List<User> userList = userService.selectAllUsers();

        request.setAttribute("userList",userList);

        List<DicValue> transactionTypeList = dicValueService.selectDicValueByTypeCode("transactionType");
        List<DicValue> tranStageList = dicValueService.selectDicValueByTypeCode("stage");
        List<DicValue> sourceList = dicValueService.selectDicValueByTypeCode("source");

        request.setAttribute("transactionTypeList",transactionTypeList);
        request.setAttribute("tranStageList",tranStageList);
        request.setAttribute("sourceList",sourceList);

        return "workbench/transaction/index";
    }

    //条件查询线索列表
    @RequestMapping("/workbench/tran/selectTranByConditionForPage.do")
    @ResponseBody
    public Object selectTranByConditionForPage(String name,String customerId,String owner,String stage,
                                               String type,String source,String contactsId,
                                               int pageNo,int pageSize){
        Map<String,Object> map = new HashMap<>();
        map.put("name",name);
        map.put("customerId",customerId);
        map.put("owner",owner);
        map.put("stage",stage);
        map.put("type",type);
        map.put("source",source);
        map.put("contactsId",contactsId);
        map.put("pageNo",(pageNo-1)*pageSize);
        map.put("pageSize",pageSize);


        List<Transaction> tranList = tranService.selectTranByConditionForPage(map);
        int total = tranService.selectCountOfTranByCondition(map);

        Map<String,Object> retMap = new HashMap<>();
        retMap.put("tranList",tranList);
        retMap.put("total",total);

        return retMap;
    }

    //跳转到添加交易界面
    @RequestMapping("/workbench/tran/save.do")
    public String save(HttpServletRequest request){
        List<User> userList = userService.selectAllUsers();

        request.setAttribute("userList",userList);

        List<DicValue> transactionTypeList = dicValueService.selectDicValueByTypeCode("transactionType");
        List<DicValue> tranStageList = dicValueService.selectDicValueByTypeCode("stage");
        List<DicValue> sourceList = dicValueService.selectDicValueByTypeCode("source");

        request.setAttribute("transactionTypeList",transactionTypeList);
        request.setAttribute("tranStageList",tranStageList);
        request.setAttribute("sourceList",sourceList);

        return "workbench/transaction/save";
    }

    //通过名字模糊查询市场活动列表
    @RequestMapping("/workbench/tran/getActivityListByName.do")
    @ResponseBody
    public Object getActivityListByName(String name){
        List<Activity> list = activityService.getActivityListByName(name);

        return list;
    }

    //通过名字模糊查询联系人列表
    @RequestMapping("/workbench/tran/getContactsListByName.do")
    @ResponseBody
    public Object getContactsListByName(String name){
        List<Contacts> list = contactsService.getContactsListByName(name);

        return list;
    }

    //根据阶段获取可能性
    @RequestMapping("/workbench/tran/getPossibilityByStage.do")
    @ResponseBody
    public Object getPossibilityByStage(String stageValue){
        //解析properties文件，获取可能性
        ResourceBundle rb = ResourceBundle.getBundle("possibility");
        String possibility = rb.getString(stageValue);

        //返回响应信息
        return possibility;
    }

    //模糊查询客户名字
    @RequestMapping("/workbench/tran/getCustomerName.do")
    @ResponseBody
    public Object getCustomerName(String name){
        List<String> list = customerService.getCustomerName(name);

        return list;
    }

    //添加交易列表
    @RequestMapping("/workbench/tran/insertTran.do")
    @ResponseBody
    public Object insertTran(@RequestParam Map<String,Object> map, HttpSession session){
        map.put(Contants.SESSION_USER,session.getAttribute(Contants.SESSION_USER));

        ReturnObject ro = new ReturnObject();
        try {
            tranService.insertTran(map);
            ro.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            ro.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            ro.setMessage("系统繁忙，请稍后...");
        }

        return ro;
    }

    //跳转到详情界面
    @RequestMapping("/workbench/tran/detail.do")
    public String detail(String id,HttpServletRequest request){
        //查询交易详情信息
        Transaction tranList = tranService.selectTranForDetailById(id);

        //根据交易所处阶段名称查询可能性
        ResourceBundle rb = ResourceBundle.getBundle("possibility");
        String possibility = rb.getString(tranList.getStage());
        tranList.setPossibility(possibility);

        request.setAttribute("tranList",tranList);

        //查询交易备注详情信息
        List<TransactionRemark> trList = tranRemarkService.selectTranRemarkByTranId(id);
        request.setAttribute("trList",trList);

        //查询交易历史信息
        List<TransactionHistory> thList = tranHistoryService.selectTranHistoryByTranId(id);
        request.setAttribute("thList",thList);

        //查询交易所有的阶段
        List<DicValue> stageList = dicValueService.selectDicValueByTypeCode("stage");
        request.setAttribute("stageList",stageList);

        return "workbench/transaction/detail";
    }
}
