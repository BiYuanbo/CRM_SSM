package com.bjpowernode.crm.workbench.mapper;

import com.bjpowernode.crm.workbench.bean.Contacts;

import java.util.List;

public interface ContactsMapper {
    //保存创建的联系人
    int insertContacts(Contacts contacts);

    //根据名字模糊查询联系人列表
    List<Contacts> getContactsListByName(String name);
}
