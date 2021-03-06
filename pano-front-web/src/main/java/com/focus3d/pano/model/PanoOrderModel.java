package com.focus3d.pano.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.focus3d.pano.common.model.CommonModel;
import com.focus3d.pano.model.ibator.PanoOrder;
import com.focus3d.pano.model.ibator.PanoOrderCriteria;

public class PanoOrderModel extends PanoOrder<PanoOrderModel, PanoOrderCriteria> implements CommonModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//订单下面套餐集合
	private List<PanoOrderPackageModel> orderPackages = new ArrayList<PanoOrderPackageModel>();
	//收获地址
	private PanoUserReceiveAddressModel address;
	//优惠券
	private PanoOrderCouponItemModel couponItem;
	//子订单
	private PanoOrderModel childrenOrder;
	
	private PanoOrderModel parentOrder;

	private Double discountValue;
	private String discountName;
	private Double percentValue;
	private String percentName;
	
	public PanoOrderModel getChildrenOrder() {
		return childrenOrder;
	}

	public void setChildrenOrder(PanoOrderModel childrenOrder) {
		this.childrenOrder = childrenOrder;
	}

	public PanoOrderCouponItemModel getCouponItem() {
		return couponItem;
	}

	public void setCouponItem(PanoOrderCouponItemModel couponItem) {
		this.couponItem = couponItem;
	}

	public List<PanoOrderPackageModel> getOrderPackages() {
		return orderPackages;
	}

	public void setOrderPackages(List<PanoOrderPackageModel> orderPackages) {
		this.orderPackages = orderPackages;
	}

	public PanoUserReceiveAddressModel getAddress() {
		return address;
	}

	public void setAddress(PanoUserReceiveAddressModel address) {
		this.address = address;
	}
	/**
	 * 获取订单状态
	 * *
	 * @return
	 */
	public boolean isPayComplete(){
		Integer status = this.getStatus();
		if(status == 2){
			PanoOrderModel cldOrder = getChildrenOrder();
			if(cldOrder != null){
				return cldOrder.getStatus() == 2;
			} 
			return true;
		}
		return false;
	}
	
	/**
	 * 是否需要补款
	 * *
	 * @return
	 */
	public boolean needSecondPay(){
		if(!isPayComplete()){
			//未完成支付情况
			if(getStatus() == 2){
				PanoOrderModel cldOrder = getChildrenOrder();
				return cldOrder.getStatus() == 1;
			}
		}
		return false;
	}

	public String getActPayMoney(){
		BigDecimal payMoney = getPayMoney();
		PanoOrderModel cldOrder = getChildrenOrder();
		if(cldOrder != null){
			BigDecimal cldPayMoney = cldOrder.getPayMoney();
			return payMoney.add(cldPayMoney).toString();
		}
		return payMoney.toString();
	}
	
	@Override
	public int hashCode() {
		return 6 * this.getSn().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof PanoOrderModel){
			PanoOrderModel order = (PanoOrderModel)obj;
			return order.getSn().equals(this.getSn());
		}
		return false;
	}

	public PanoOrderModel getParentOrder() {
		return parentOrder;
	}

	public void setParentOrder(PanoOrderModel parentOrder) {
		this.parentOrder = parentOrder;
	}

	public String getDiscountName() {
		return discountName;
	}

	public void setDiscountName(String discountName) {
		this.discountName = discountName;
	}
	
	public Double getDiscountValue() {
		return discountValue;
	}

	public void setDiscountValue(Double discountValue) {
		this.discountValue = discountValue;
	}

	public Double getPercentValue() {
		return percentValue;
	}

	public void setPercentValue(Double percentValue) {
		this.percentValue = percentValue;
	}

	public String getPercentName() {
		return percentName;
	}

	public void setPercentName(String percentName) {
		this.percentName = percentName;
	}
}
