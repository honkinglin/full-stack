package com.fullstack.reader.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

@TableName("evaluation")
public class Evaluation {
    @TableId(type = IdType.AUTO)
    private Long evaluationId;
    private Long bookId;
    private Integer score;
    private String content;
    private Long memberId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;

    private Integer enjoy;
    private String state;
    private String disableReason;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime disableTime;

    public Long getEvaluationId() {
        return evaluationId;
    }

    public void setEvaluationId(Long evaluationId) {
        this.evaluationId = evaluationId;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public Integer getEnjoy() {
        return enjoy;
    }

    public void setEnjoy(Integer enjoy) {
        this.enjoy = enjoy;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDisabledReason() {
        return disableReason;
    }

    public void setDisabledReason(String disableReason) {
        this.disableReason = disableReason;
    }

    public LocalDateTime getDisabledTime() {
        return disableTime;
    }

    public void setDisabledTime(LocalDateTime disableTime) {
        this.disableTime = disableTime;
    }
}
