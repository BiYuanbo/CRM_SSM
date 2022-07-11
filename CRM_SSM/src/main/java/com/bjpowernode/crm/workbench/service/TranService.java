package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.bean.Clue;
import com.bjpowernode.crm.workbench.bean.FunnelVO;
import com.bjpowernode.crm.workbench.bean.Transaction;

import java.util.List;
import java.util.Map;

public interface TranService {
    //根据条件分页查询市场列表
    List<Transaction> selectTranByConditionForPage(Map<String, Object> map);

    //根据条件查询市场列表总条数
    int selectCountOfTranByCondition(Map<String, Object> map);

    //根据id查询交易信息
    Transaction selectTranForDetailById(String id);

    //保存创建的交易
    void insertTran(Map<String,Object> map);

    //查询交易表中各个阶段的数据量
    List<FunnelVO> selectCountOfTranGroupByStage();
}
