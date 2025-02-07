package com.fullstack.mall.model.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;;


public class AddCategoryReq {
    @Size(min = 2, max = 5, message = "name length should be between 2 and 5")
    @NotNull(message = "name can not be null")
    private String name;

    @NotNull(message = "type can not be null")
    @Max(value=3, message = "type should be less than 3")
    private Integer type;

    @NotNull(message = "parentId can not be null")
    private Integer parentId;

    @NotNull(message = "orderNum can not be null")
    private Integer orderNum;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "AddCategoryReq{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", parentId=" + parentId +
                ", orderNum=" + orderNum +
                '}';
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }
}
