package com.eyun.appraise.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A Appraise.
 */
@Entity
@Table(name = "appraise")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Appraise implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 500)
    @Column(name = "jhi_comment", length = 500, nullable = false)
    private String comment;

    @Column(name = "product_level")
    private Integer productLevel;

    @Column(name = "package_level")
    private Integer packageLevel;

    @Column(name = "delivery_level")
    private Integer deliveryLevel;

    @Column(name = "deliveryman_level")
    private Integer deliverymanLevel;

    @Column(name = "images")
    private String images;

    @NotNull
    @Column(name = "userid", nullable = false)
    private Long userid;

    @NotNull
    @Column(name = "shop_id", nullable = false)
    private Long shopId;

    @NotNull
    @Column(name = "pro_id", nullable = false)
    private Long proId;

    @NotNull
    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @NotNull
    @Column(name = "buy_time", nullable = false)
    private Instant buyTime;

    @Column(name = "created_time")
    private Instant createdTime;

    @Column(name = "updated_time")
    private Instant updatedTime;

    @Column(name = "deleted")
    private Integer deleted;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public Appraise comment(String comment) {
        this.comment = comment;
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getProductLevel() {
        return productLevel;
    }

    public Appraise productLevel(Integer productLevel) {
        this.productLevel = productLevel;
        return this;
    }

    public void setProductLevel(Integer productLevel) {
        this.productLevel = productLevel;
    }

    public Integer getPackageLevel() {
        return packageLevel;
    }

    public Appraise packageLevel(Integer packageLevel) {
        this.packageLevel = packageLevel;
        return this;
    }

    public void setPackageLevel(Integer packageLevel) {
        this.packageLevel = packageLevel;
    }

    public Integer getDeliveryLevel() {
        return deliveryLevel;
    }

    public Appraise deliveryLevel(Integer deliveryLevel) {
        this.deliveryLevel = deliveryLevel;
        return this;
    }

    public void setDeliveryLevel(Integer deliveryLevel) {
        this.deliveryLevel = deliveryLevel;
    }

    public Integer getDeliverymanLevel() {
        return deliverymanLevel;
    }

    public Appraise deliverymanLevel(Integer deliverymanLevel) {
        this.deliverymanLevel = deliverymanLevel;
        return this;
    }

    public void setDeliverymanLevel(Integer deliverymanLevel) {
        this.deliverymanLevel = deliverymanLevel;
    }

    public String getImages() {
        return images;
    }

    public Appraise images(String images) {
        this.images = images;
        return this;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public Long getUserid() {
        return userid;
    }

    public Appraise userid(Long userid) {
        this.userid = userid;
        return this;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public Long getShopId() {
        return shopId;
    }

    public Appraise shopId(Long shopId) {
        this.shopId = shopId;
        return this;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getProId() {
        return proId;
    }

    public Appraise proId(Long proId) {
        this.proId = proId;
        return this;
    }

    public void setProId(Long proId) {
        this.proId = proId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Appraise orderId(Long orderId) {
        this.orderId = orderId;
        return this;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Instant getBuyTime() {
        return buyTime;
    }

    public Appraise buyTime(Instant buyTime) {
        this.buyTime = buyTime;
        return this;
    }

    public void setBuyTime(Instant buyTime) {
        this.buyTime = buyTime;
    }

    public Instant getCreatedTime() {
        return createdTime;
    }

    public Appraise createdTime(Instant createdTime) {
        this.createdTime = createdTime;
        return this;
    }

    public void setCreatedTime(Instant createdTime) {
        this.createdTime = createdTime;
    }

    public Instant getUpdatedTime() {
        return updatedTime;
    }

    public Appraise updatedTime(Instant updatedTime) {
        this.updatedTime = updatedTime;
        return this;
    }

    public void setUpdatedTime(Instant updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public Appraise deleted(Integer deleted) {
        this.deleted = deleted;
        return this;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Appraise appraise = (Appraise) o;
        if (appraise.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), appraise.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Appraise{" +
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
