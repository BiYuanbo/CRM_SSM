package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.workbench.bean.Contacts;
import com.bjpowernode.crm.workbench.mapper.ContactsMapper;
import com.bjpowernode.crm.workbench.service.ContactsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactsServiceImpl implements ContactsService {
    @Autowired
    private ContactsMapper contactsMapper;

    @Override
    public List<Contacts> getContactsListByName(String name) {
        return contactsMapper.getContactsListByName(name);
    }
}
