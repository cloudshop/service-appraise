package com.eyun.appraise.service.dto;


import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Appraise entity.
 */
public class AppraiseDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 500)
    private String comment;

    private Integer productLevel;

    private Integer packageLevel;

    private Integer deliveryLevel;

    private Integer deliverymanLevel;

    private String images;

    @NotNull
    private Long userid;

    @NotNull
    private Long shopId;

    @NotNull
    private Long proId;

    @NotNull
    private Long orderId;

    @NotNull
    private Instant buyTime;

    private Instant createdTime;

    private Instant updatedTime;

    private Integer deleted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getProductLevel() {
        return productLevel;
    }

    public void setProductLevel(Integer productLevel) {
        this.productLevel = productLevel;
    }

    public Integer getPackageLevel() {
        return packageLevel;
    }

    public void setPackageLevel(Integer packageLevel) {
        this.packageLevel = packageLevel;
    }

    public Integer getDeliveryLevel() {
        return deliveryLevel;
    }

    public void setDeliveryLevel(Integer deliveryLevel) {
        this.deliveryLevel = deliveryLevel;
    }

    public Integer getDeliverymanLevel() {
        return deliverymanLevel;
    }

    public void setDeliverymanLevel(Integer deliverymanLevel) {
        this.deliverymanLevel = deliverymanLevel;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getProId() {
        return proId;
    }

    public void setProId(Long proId) {
        this.proId = proId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Instant getBuyTime() {
        return buyTime;
    }

    public void setBuyTime(Instant buyTime) {
        this.buyTime = buyTime;
    }

    public Instant getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Instant createdTime) {
        this.createdTime = createdTime;
    }

    public Instant getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Instant updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AppraiseDTO appraiseDTO = (AppraiseDTO) o;
        if(appraiseDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), appraiseDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AppraiseDTO{" +
            "id=" + getId() +
            ", comment='" + getComment() + "'" +
            ", productLevel=" + getProductLevel() +
            ", packageLevel=" + getPackageLevel() +
            ", deliveryLevel=" + getDeliveryLevel() +
            ", deliverymanLevel=" + getDeliverymanLevel() +
            ", images='" + getImages() + "'" +
            ", userid=" + getUserid() +
            ", shopId=" + getShopId() +
            ", proId=" + getProId() +
            ", orderId=" + getOrderId() +
            ", buyTime='" + getBuyTime() + "'" +
            ", createdTime='" + getCreatedTime() + "'" +
            ", updatedTime='" + getUpdatedTime() + "'" +
            ", deleted=" + getDeleted() +
            "}";
    }
}
