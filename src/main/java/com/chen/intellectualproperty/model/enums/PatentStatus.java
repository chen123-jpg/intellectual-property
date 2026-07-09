package com.chen.intellectualproperty.model.enums;

public enum PatentStatus {
    RECEIVE_DISCLOSURE_DOC(0, "接收交底书"),
    CONTACT_INVENTOR(1, "联系发明人"),
    SEARCH_DISCLOSURE_DOC(2, "检索交底书"),
    WITHDRAW(3, "撤回"),
    SEND_BACK(4, "打回"),
    REVISE_DISCLOSURE(5, "修改交底"),
    FIRST_DRAFT_WRITING(6, "一稿撰写中"),
    FIRST_DRAFT_PENDING_FEEDBACK(7, "一稿待反馈"),
    NTH_DRAFT_WRITING(8, "N稿撰写中"),
    NTH_DRAFT_PENDING_FEEDBACK(9, "N稿待反馈"),
    PENDING_FINALIZATION(10, "待定稿"),
    FINALIZED_PENDING_SUBMIT(11, "定稿待提交"),
    SUBMITTED_PENDING_ACCEPTANCE(12, "提交待受理"),
    ACCEPTED(13, "受理");

    private Integer status;
    private String desc;

    PatentStatus(Integer status,String desc){
        this.status=status;
        this.desc=desc;
    }

    public String getDesc() {
        return desc;
    }

    public Integer getStatus() {
        return status;
    }

}
