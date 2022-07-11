package com.bjpowernode.crm.workbench.mapper;

import com.bjpowernode.crm.workbench.bean.Clue;
import com.bjpowernode.crm.workbench.bean.ClueRemark;

import java.util.List;

public interface ClueRemarkMapper {
    //通过线索id查询备注列表
    List<ClueRemark> selectClueRemarkByClueId(String id);

    //根据id删除线索备注
    int deleteClueRemarkByClueId(String clueId);
}
