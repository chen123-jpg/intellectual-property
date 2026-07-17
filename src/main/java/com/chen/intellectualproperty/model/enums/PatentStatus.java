package com.chen.intellectualproperty.model.enums;

import java.util.Arrays;
import java.util.Optional;

/**
 * 交底处理状态。
 * <p>11=定稿（可上传申请包），12=定稿待报（同步申请专利表）。</p>
 */
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
    FINALIZED(11, "定稿"),
    FINALIZED_PENDING_REPORT(12, "定稿待报"),
    SUBMITTED_PENDING_ACCEPTANCE(13, "提交待受理"),
    ACCEPTED(14, "受理");

    private final Integer status;
    private final String desc;

    PatentStatus(Integer status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public Integer getStatus() {
        return status;
    }

    public String codeAsString() {
        return String.valueOf(status);
    }

    public static Optional<PatentStatus> fromCode(String code) {
        if (code == null || code.isBlank()) {
            return Optional.empty();
        }
        String trimmed = code.trim();
        return Arrays.stream(values())
                .filter(s -> s.codeAsString().equals(trimmed) || s.desc.equals(trimmed))
                .findFirst();
    }

    public static boolean isFinalizedOrAfter(String code) {
        return fromCode(code)
                .map(s -> s.status >= FINALIZED.status)
                .orElse(false);
    }

    public static boolean isFinalizedPendingReport(String code) {
        return fromCode(code)
                .map(s -> s == FINALIZED_PENDING_REPORT)
                .orElse("定稿待报".equals(code) || "12".equals(code));
    }
}
