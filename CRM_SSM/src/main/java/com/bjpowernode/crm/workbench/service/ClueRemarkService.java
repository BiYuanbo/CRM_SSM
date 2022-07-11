package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.bean.Clue;
import com.bjpowernode.crm.workbench.bean.ClueRemark;

import java.util.List;

public interface ClueRemarkService {
    //通过线索id查询备注列表
    List<ClueRemark> selectClueRemarkByClueId(String id);
}
