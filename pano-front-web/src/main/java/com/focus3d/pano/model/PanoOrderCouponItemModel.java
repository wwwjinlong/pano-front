package com.focus3d.pano.model;

import com.focus3d.pano.common.model.CommonModel;
import com.focus3d.pano.model.ibator.PanoOrderCouponItem;
import com.focus3d.pano.model.ibator.PanoOrderCouponItemCriteria;
/**
 * 
 * *
 * @author lihaijun
 *
 */
public class PanoOrderCouponItemModel extends PanoOrderCouponItem<PanoOrderCouponItemModel, PanoOrderCouponItemCriteria> implements CommonModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private PanoOrderCouponModel coupon;
	
	private int status;//0-可用 1-未生效 2-过期 3-已被使用过 4-优惠券不是该项目下的

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public PanoOrderCouponModel getCoupon() {
		return coupon;
	}

	public void setCoupon(PanoOrderCouponModel coupon) {
		this.coupon = coupon;
	}
	
}
