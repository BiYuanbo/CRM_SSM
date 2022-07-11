package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.bean.TransactionHistory;

import java.util.List;

public interface TranHistoryService {
    //根据交易id查询交易历史
    List<TransactionHistory> selectTranHistoryByTranId(String id);
}
