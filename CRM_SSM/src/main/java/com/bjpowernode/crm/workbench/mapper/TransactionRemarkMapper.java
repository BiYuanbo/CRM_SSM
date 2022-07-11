package com.bjpowernode.crm.workbench.mapper;

import com.bjpowernode.crm.workbench.bean.TransactionRemark;

import java.util.List;

public interface TransactionRemarkMapper {
    //批量保存创建的交易备注
    int insertTranRemarkByList(List<TransactionRemark> list);

    //根据交易id查询交易备注
    List<TransactionRemark> selectTranRemarkByTranId(String id);
}
