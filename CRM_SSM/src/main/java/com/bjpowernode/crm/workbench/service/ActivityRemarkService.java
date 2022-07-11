package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.bean.ActivityRemark;

import java.util.List;

public interface ActivityRemarkService {
    List<ActivityRemark> selectActivityRemarkByActivityId(String activityId);

    int insertActivityRemark(ActivityRemark activityRemark);

    int deleteActivityRemarkById(String id);

    int updateActivityRemark(ActivityRemark activityRemark);
}
