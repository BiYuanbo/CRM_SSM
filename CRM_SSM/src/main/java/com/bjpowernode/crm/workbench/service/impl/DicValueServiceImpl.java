package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.workbench.bean.DicValue;
import com.bjpowernode.crm.workbench.mapper.DicValueMapper;
import com.bjpowernode.crm.workbench.service.DicValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DicValueServiceImpl implements DicValueService {
    @Autowired
    private DicValueMapper dicValueMapper;

    @Override
    public List<DicValue> selectDicValueByTypeCode(String typeCode) {
        return dicValueMapper.selectDicValueByTypeCode(typeCode);
    }
}
