package com.bjpowernode.crm.workbench.mapper;

import com.bjpowernode.crm.workbench.bean.ContactsActivityRelation;

import java.util.List;

public interface ContactsActivityRelationMapper {
    //批量保存创建的联系人和市场活动的关联关系
    int insertContactActivityRelationByList(List<ContactsActivityRelation> list);
}
