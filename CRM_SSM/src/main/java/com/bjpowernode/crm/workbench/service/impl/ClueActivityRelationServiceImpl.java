package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.workbench.bean.ClueActivityRelation;
import com.bjpowernode.crm.workbench.mapper.ClueActivityRelationMapper;
import com.bjpowernode.crm.workbench.service.ClueActivityRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClueActivityRelationServiceImpl implements ClueActivityRelationService {
    @Autowired
    private ClueActivityRelationMapper clueActivityRelationMapper;

    @Override
    public int insertClueActivityRelation(List<ClueActivityRelation> list) {
        return clueActivityRelationMapper.insertClueActivityRelation(list);
    }

    @Override
    public int unbund(ClueActivityRelation clueActivityRelation) {
        return clueActivityRelationMapper.unbund(clueActivityRelation);
    }
}
