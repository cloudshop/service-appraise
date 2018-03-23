package com.eyun.appraise.service.dto;

import java.io.Serializable;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

import io.github.jhipster.service.filter.InstantFilter;




/**
 * Criteria class for the Appraise entity. This class is used in AppraiseResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /appraises?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AppraiseCriteria implements Serializable {
    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter comment;

    private IntegerFilter productLevel;

    private IntegerFilter packageLevel;

    private IntegerFilter deliveryLevel;

    private IntegerFilter deliverymanLevel;

    private StringFilter images;

    private LongFilter userid;

    private LongFilter shopId;

    private LongFilter proId;

    private LongFilter orderId;

    private InstantFilter buyTime;

    private InstantFilter createdTime;

    private InstantFilter updatedTime;

    private IntegerFilter deleted;

    public AppraiseCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getComment() {
        return comment;
    }

    public void setComment(StringFilter comment) {
        this.comment = comment;
    }

    public IntegerFilter getProductLevel() {
        return productLevel;
    }

    public void setProductLevel(IntegerFilter productLevel) {
        this.productLevel = productLevel;
    }

    public IntegerFilter getPackageLevel() {
        return packageLevel;
    }

    public void setPackageLevel(IntegerFilter packageLevel) {
        this.packageLevel = packageLevel;
    }

    public IntegerFilter getDeliveryLevel() {
        return deliveryLevel;
    }

    public void setDeliveryLevel(IntegerFilter deliveryLevel) {
        this.deliveryLevel = deliveryLevel;
    }

    public IntegerFilter getDeliverymanLevel() {
        return deliverymanLevel;
    }

    public void setDeliverymanLevel(IntegerFilter deliverymanLevel) {
        this.deliverymanLevel = deliverymanLevel;
    }

    public StringFilter getImages() {
        return images;
    }

    public void setImages(StringFilter images) {
        this.images = images;
    }

    public LongFilter getUserid() {
        return userid;
    }

    public void setUserid(LongFilter userid) {
        this.userid = userid;
    }

    public LongFilter getShopId() {
        return shopId;
    }

    public void setShopId(LongFilter shopId) {
        this.shopId = shopId;
    }

    public LongFilter getProId() {
        return proId;
    }

    public void setProId(LongFilter proId) {
        this.proId = proId;
    }

    public LongFilter getOrderId() {
        return orderId;
    }

    public void setOrderId(LongFilter orderId) {
        this.orderId = orderId;
    }

    public InstantFilter getBuyTime() {
        return buyTime;
    }

    public void setBuyTime(InstantFilter buyTime) {
        this.buyTime = buyTime;
    }

    public InstantFilter getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(InstantFilter createdTime) {
        this.createdTime = createdTime;
    }

    public InstantFilter getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(InstantFilter updatedTime) {
        this.updatedTime = updatedTime;
    }

    public IntegerFilter getDeleted() {
        return deleted;
    }

    public void setDeleted(IntegerFilter deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "AppraiseCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (comment != null ? "comment=" + comment + ", " : "") +
                (productLevel != null ? "productLevel=" + productLevel + ", " : "") +
                (packageLevel != null ? "packageLevel=" + packageLevel + ", " : "") +
                (deliveryLevel != null ? "deliveryLevel=" + deliveryLevel + ", " : "") +
                (deliverymanLevel != null ? "deliverymanLevel=" + deliverymanLevel + ", " : "") +
                (images != null ? "images=" + images + ", " : "") +
                (userid != null ? "userid=" + userid + ", " : "") +
                (shopId != null ? "shopId=" + shopId + ", " : "") +
                (proId != null ? "proId=" + proId + ", " : "") +
                (orderId != null ? "orderId=" + orderId + ", " : "") +
                (buyTime != null ? "buyTime=" + buyTime + ", " : "") +
                (createdTime != null ? "createdTime=" + createdTime + ", " : "") +
                (updatedTime != null ? "updatedTime=" + updatedTime + ", " : "") +
                (deleted != null ? "deleted=" + deleted + ", " : "") +
            "}";
    }

}
