package com.bjpowernode.crm.workbench.bean;

public class TransactionRemark {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_tran_remark.id
     *
     * @mbggenerated Wed Jul 06 16:20:00 CST 2022
     */
    private String id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_tran_remark.noteContent
     *
     * @mbggenerated Wed Jul 06 16:20:00 CST 2022
     */
    private String noteContent;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_tran_remark.createBy
     *
     * @mbggenerated Wed Jul 06 16:20:00 CST 2022
     */
    private String createBy;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_tran_remark.createTime
     *
     * @mbggenerated Wed Jul 06 16:20:00 CST 2022
     */
    private String createTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_tran_remark.editBy
     *
     * @mbggenerated Wed Jul 06 16:20:00 CST 2022
     */
    private String editBy;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_tran_remark.editTime
     *
     * @mbggenerated Wed Jul 06 16:20:00 CST 2022
     */
    private String editTime;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_tran_remark.editFlag
     *
     * @mbggenerated Wed Jul 06 16:20:00 CST 2022
     */
    private String editFlag;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tbl_tran_remark.tranId
     *
     * @mbggenerated Wed Jul 06 16:20:00 CST 2022
     */
    private String tranId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNoteContent() {
        return noteContent;
    }

    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getEditBy() {
        return editBy;
    }

    public void setEditBy(String editBy) {
        this.editBy = editBy;
    }

    public String getEditTime() {
        return editTime;
    }

    public void setEditTime(String editTime) {
        this.editTime = editTime;
    }

    public String getEditFlag() {
        return editFlag;
    }

    public void setEditFlag(String editFlag) {
        this.editFlag = editFlag;
    }

    public String getTranId() {
        return tranId;
    }

    public void setTranId(String tranId) {
        this.tranId = tranId;
    }
}
