package com.bjpowernode.crm.workbench.mapper;

import com.bjpowernode.crm.workbench.bean.Customer;

import java.util.List;

public interface CustomerMapper {
    //保存创建的客户
    int insertCustomer(Customer customer);

    //模糊查询客户名字
    List<String> getCustomerName(String name);

    //精确查询客户
    Customer selectCustomerByName(String name);
}
