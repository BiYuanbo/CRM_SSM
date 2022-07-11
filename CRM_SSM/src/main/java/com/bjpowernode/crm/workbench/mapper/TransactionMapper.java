package com.bjpowernode.crm.workbench.mapper;

import com.bjpowernode.crm.workbench.bean.Clue;
import com.bjpowernode.crm.workbench.bean.FunnelVO;
import com.bjpowernode.crm.workbench.bean.Transaction;

import java.util.List;
import java.util.Map;

public interface TransactionMapper {
    //保存创建的交易
    int insertTran(Transaction transaction);

    //根据条件分页查询市场列表
    List<Transaction> selectTranByConditionForPage(Map<String, Object> map);

    //根据条件查询市场列表总条数
    int selectCountOfTranByCondition(Map<String, Object> map);

    //根据id查询交易信息
    Transaction selectTranForDetailById(String id);

    //查询交易表中各个阶段的数据量
    List<FunnelVO> selectCountOfTranGroupByStage();
}
