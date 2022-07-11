package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.workbench.bean.TransactionRemark;
import com.bjpowernode.crm.workbench.mapper.TransactionRemarkMapper;
import com.bjpowernode.crm.workbench.service.TranRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TranRemarkServiceImpl implements TranRemarkService {
    @Autowired
    private TransactionRemarkMapper transactionRemarkMapper;
    @Override
    public List<TransactionRemark> selectTranRemarkByTranId(String id) {
        return transactionRemarkMapper.selectTranRemarkByTranId(id);
    }
}
