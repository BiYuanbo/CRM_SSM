package com.bjpowernode.crm.workbench.mapper;

import com.bjpowernode.crm.workbench.bean.CustomerRemark;

import java.util.List;

public interface CustomerRemarkMapper {
    //批量保存创建的客户备注
    int insertCustomerRemarkByList(List<CustomerRemark> list);
}
