package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.commons.contants.Contants;
import com.bjpowernode.crm.commons.utils.DateTimeUtil;
import com.bjpowernode.crm.commons.utils.UUIDUtil;
import com.bjpowernode.crm.settings.bean.User;
import com.bjpowernode.crm.workbench.bean.*;
import com.bjpowernode.crm.workbench.mapper.CustomerMapper;
import com.bjpowernode.crm.workbench.mapper.TransactionHistoryMapper;
import com.bjpowernode.crm.workbench.mapper.TransactionMapper;
import com.bjpowernode.crm.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.plugin.util.UIUtil;

import java.util.List;
import java.util.Map;

@Service
public class TranServiceImpl implements TranService {

    @Autowired
    private TransactionMapper transactionMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private TransactionHistoryMapper transactionHistoryMapper;

    @Override
    public List<Transaction> selectTranByConditionForPage(Map<String, Object> map) {
        return transactionMapper.selectTranByConditionForPage(map);
    }

    @Override
    public int selectCountOfTranByCondition(Map<String, Object> map) {
        return transactionMapper.selectCountOfTranByCondition(map);
    }

    @Override
    public Transaction selectTranForDetailById(String id) {
        return transactionMapper.selectTranForDetailById(id);
    }

    @Override
    public void insertTran(Map<String,Object> map) {
        User user = (User) map.get(Contants.SESSION_USER);
        String cusName = (String) map.get("cusName");
        //根据name精确查询客户
        Customer customer = customerMapper.selectCustomerByName(cusName);
        //如果客户不存在则创建
        if (customer==null){
            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setOwner(user.getId());
            customer.setName(cusName);
            customer.setCreateTime(DateTimeUtil.getSysTime());
            customer.setCreateBy(user.getName());

            customerMapper.insertCustomer(customer);
        }

        Transaction tran = new Transaction();
        tran.setId(UUIDUtil.getUUID());
        tran.setOwner((String) map.get("owner"));
        tran.setMoney((String) map.get("money"));
        tran.setName((String) map.get("name"));
        tran.setExpectedDate((String) map.get("expectedDate"));
        tran.setCustomerId(customer.getId());
        tran.setStage((String) map.get("stage"));
        tran.setType((String) map.get("type"));
        tran.setSource((String) map.get("source"));
        tran.setActivityId((String) map.get("activityId"));
        tran.setContactsId((String) map.get("contactsId"));
        tran.setCreateBy(user.getName());
        tran.setCreateTime(DateTimeUtil.getSysTime());
        tran.setDescription((String) map.get("description"));
        tran.setContactSummary((String) map.get("contactSummary"));
        tran.setNextContactTime((String) map.get("nextContactTime"));

        transactionMapper.insertTran(tran);

        TransactionHistory tranHistory = new TransactionHistory();
        tranHistory.setId(UUIDUtil.getUUID());
        tranHistory.setCreateBy(user.getName());
        tranHistory.setCreateTime(DateTimeUtil.getSysTime());
        tranHistory.setStage(tran.getStage());
        tranHistory.setMoney(tran.getMoney());
        tranHistory.setTranId(tran.getId());
        tranHistory.setExpectedDate(tran.getExpectedDate());

        transactionHistoryMapper.insertTranHistory(tranHistory);
    }

    @Override
    public List<FunnelVO> selectCountOfTranGroupByStage() {
        return transactionMapper.selectCountOfTranGroupByStage();
    }
}
