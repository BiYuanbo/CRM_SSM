package com.bjpowernode.crm.workbench.mapper;

import com.bjpowernode.crm.workbench.bean.ContactsRemark;
import com.bjpowernode.crm.workbench.bean.CustomerRemark;

import java.util.List;

public interface ContactsRemarkMapper {
    //批量保存创建的联系人备注
    int insertContactsRemarkByList(List<ContactsRemark> list);
}
