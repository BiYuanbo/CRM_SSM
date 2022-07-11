package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.bean.ClueActivityRelation;

import java.util.List;

public interface ClueActivityRelationService {
    //批量保存线索和市场活动列表的关系
    int insertClueActivityRelation(List<ClueActivityRelation> list);

    //解除关联
    int unbund(ClueActivityRelation clueActivityRelation);
}
