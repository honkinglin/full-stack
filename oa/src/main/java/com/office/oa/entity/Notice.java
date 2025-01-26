package com.office.oa.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.office.oa.utils.LocalDateTimeDeserializer;
import com.office.oa.utils.LocalDateTimeSerializer;

import java.time.LocalDateTime;

public class Notice {
    private Long noticeId;
    private Long receiverId;
    private String content;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createTime;

    public Notice() {
    }

    public Notice(Long receiverId, String content) {
        this.receiverId = receiverId;
        this.content = content;
        this.createTime = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Notice{" +
                "noticeId=" + noticeId +
                ", receiverId=" + receiverId +
                ", content='" + content + '\'' +
                ", createTime=" + createTime +
                '}';
    }

    public Long getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(Long noticeId) {
        this.noticeId = noticeId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
}
