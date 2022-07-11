package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.bean.Contacts;

import java.util.List;

public interface ContactsService {

    List<Contacts> getContactsListByName(String name);
}
