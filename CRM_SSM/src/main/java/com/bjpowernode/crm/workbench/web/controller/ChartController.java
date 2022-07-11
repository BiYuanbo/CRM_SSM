package com.bjpowernode.crm.workbench.web.controller;

import com.bjpowernode.crm.workbench.bean.FunnelVO;
import com.bjpowernode.crm.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ChartController {
    @Autowired
    private TranService tranService;

    @RequestMapping("/workbench/chart/transaction/index.do")
    public String index(){
        return "workbench/chart/transaction/index";
    }

    @RequestMapping("/workbench/chart/transaction/selectCountOfTranGroupByStage.do")
    @ResponseBody
    public Object selectCountOfTranGroupByStage(){
        List<FunnelVO> funnelVOList = tranService.selectCountOfTranGroupByStage();

        return funnelVOList;
    }
}
