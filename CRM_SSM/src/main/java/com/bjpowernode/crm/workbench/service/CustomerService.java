package com.bjpowernode.crm.workbench.service;

import java.util.List;

public interface CustomerService {
    //模糊查询客户名字
    List<String> getCustomerName(String name);
}
