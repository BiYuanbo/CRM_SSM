package com.bjpowernode.crm.workbench.bean;

public class ClueActivityRelation {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_clue_activity_relation.id
     *
     * @mbggenerated Mon Jul 04 11:18:59 CST 2022
     */
    private String id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_clue_activity_relation.clueId
     *
     * @mbggenerated Mon Jul 04 11:18:59 CST 2022
     */
    private String clueId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_clue_activity_relation.activityId
     *
     * @mbggenerated Mon Jul 04 11:18:59 CST 2022
     */
    private String activityId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClueId() {
        return clueId;
    }

    public void setClueId(String clueId) {
        this.clueId = clueId;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }
}
