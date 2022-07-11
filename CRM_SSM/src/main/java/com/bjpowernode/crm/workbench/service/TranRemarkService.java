package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.bean.TransactionRemark;

import java.util.List;

public interface TranRemarkService {
    //根据交易id查询交易备注
    List<TransactionRemark> selectTranRemarkByTranId(String id);
}
