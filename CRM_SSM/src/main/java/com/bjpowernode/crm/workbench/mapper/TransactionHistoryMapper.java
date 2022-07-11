package com.bjpowernode.crm.workbench.mapper;

import com.bjpowernode.crm.workbench.bean.TransactionHistory;

import java.util.List;

public interface TransactionHistoryMapper {
    //保存创建的交易历史
    int insertTranHistory(TransactionHistory transactionHistory);

    //根据交易id查询交易历史
    List<TransactionHistory> selectTranHistoryByTranId(String id);
}
