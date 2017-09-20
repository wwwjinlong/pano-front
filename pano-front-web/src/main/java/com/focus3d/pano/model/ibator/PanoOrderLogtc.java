package com.focus3d.pano.model.ibator;

import com.focustech.cief.ibatis.annotation.Column;
import com.focustech.cief.ibatis.annotation.PrimaryKey;
import com.focustech.cief.ibatis.annotation.SqlMap;
import com.focustech.cief.ibatis.annotation.Xss;
import java.math.BigDecimal;
import java.util.Date;

@Xss
@SqlMap(Name = "pano_order_logtc", Class = PanoOrderLogtc.class)
public class PanoOrderLogtc<T, U> {
    /**
     * This field was generated by Apache iBATIS ibator.
     * This field corresponds to the database column pano_order_logtc.SN
     *
     * @ibatorgenerated Wed Sep 20 17:23:34 CST 2017
     */
    @PrimaryKey
    @Column
    private Long sn;

    /**
     * This field was generated by Apache iBATIS ibator.
     * This field corresponds to the database column pano_order_logtc.ID
     *
     * @ibatorgenerated Wed Sep 20 17:23:34 CST 2017
     */
    @Column
    private String id;

    /**
     * This field was generated by Apache iBATIS ibator.
     * This field corresponds to the database column pano_order_logtc.NAME
     *
     * @ibatorgenerated Wed Sep 20 17:23:34 CST 2017
     */
    @Column
    private String name;

    /**
     * This field was generated by Apache iBATIS ibator.
     * This field corresponds to the database column pano_order_logtc.TYPE
     *
     * @ibatorgenerated Wed Sep 20 17:23:34 CST 2017
     */
    @Column
    private Integer type;

    /**
     * This field was generated by Apache iBATIS ibator.
     * This field corresponds to the database column pano_order_logtc.PRICE
     *
     * @ibatorgenerated Wed Sep 20 17:23:34 CST 2017
     */
    @Column
    private BigDecimal price;

    /**
     * This field was generated by Apache iBATIS ibator.
     * This field corresponds to the database column pano_order_logtc.SEND
     *
     * @ibatorgenerated Wed Sep 20 17:23:34 CST 2017
     */
    @Column
    private Integer send;

    /**
     * This field was generated by Apache iBATIS ibator.
     * This field corresponds to the database column pano_order_logtc.RECEIVE
     *
     * @ibatorgenerated Wed Sep 20 17:23:34 CST 2017
     */
    @Column
    private Integer receive;

    /**
     * This field was generated by Apache iBATIS ibator.
     * This field corresponds to the database column pano_order_logtc.ORDER_SN
     *
     * @ibatorgenerated Wed Sep 20 17:23:34 CST 2017
     */
    @Column
    private Long orderSn;

    /**
     * This field was generated by Apache iBATIS ibator.
     * This field corresponds to the database column pano_order_logtc.USER_NAME
     *
     * @ibatorgenerated Wed Sep 20 17:23:34 CST 2017
     */
    @Column
    private String userName;

    /**
     * This field was generated by Apache iBATIS ibator.
     * This field corresponds to the database column pano_order_logtc.USER_PHONE
     *
     * @ibatorgenerated Wed Sep 20 17:23:34 CST 2017
     */
    @Column
    private String userPhone;

    /**
     * This field was generated by Apache iBATIS ibator.
     * This field corresponds to the database column pano_order_logtc.USER_ADDRESS
     *
     * @ibatorgenerated Wed Sep 20 17:23:34 CST 2017
     */
    @Column
    private String userAddress;

    /**
     * This field was generated by Apache iBATIS ibator.
     * This field corresponds to the database column pano_order_logtc.REMARK
     *
     * @ibatorgenerated Wed Sep 20 17:23:34 CST 2017
     */
    @Column
    private String remark;

    /**
     * This field was generated by Apache iBATIS ibator.
     * This field corresponds to the database column pano_order_logtc.ADDER_SN
     *
     * @ibatorgenerated Wed Sep 20 17:23:34 CST 2017
     */
    @Column
    private Long adderSn;

    /**
     * This field was generated by Apache iBATIS ibator.
     * This field corresponds to the database column pano_order_logtc.ADDER_NAME
     *
     * @ibatorgenerated Wed Sep 20 17:23:34 CST 2017
     */
    @Column
    private String adderName;

    /**
     * This field was generated by Apache iBATIS ibator.
     * This field corresponds to the database column pano_order_logtc.ADD_TIME
     *
     * @ibatorgenerated Wed Sep 20 17:23:34 CST 2017
     */
    @Column
    private Date addTime;

    /**
     * This field was generated by Apache iBATIS ibator.
     * This field corresponds to the database column pano_order_logtc.UPDATER_SN
     *
     * @ibatorgenerated Wed Sep 20 17:23:34 CST 2017
     */
    @Column
    private Long updaterSn;

    /**
     * This field was generated by Apache iBATIS ibator.
     * This field corresponds to the database column pano_order_logtc.UPDATER_NAME
     *
     * @ibatorgenerated Wed Sep 20 17:23:34 CST 2017
     */
    @Column
    private String updaterName;

    /**
     * This field was generated by Apache iBATIS ibator.
     * This field corresponds to the database column pano_order_logtc.UPDATE_TIME
     *
     * @ibatorgenerated Wed Sep 20 17:23:34 CST 2017
     */
    @Column
    private Date updateTime;

    /**
     * This field was generated by Apache iBATIS ibator.
     * This field corresponds to the database column pano_order_logtc.encryptSn
     *
     * @ibatorgenerated Wed Sep 20 17:23:34 CST 2017
     */
    @Column
    private String encryptSn;

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method returns the value of the database column pano_order_logtc.SN
     *
     * @return the value of pano_order_logtc.SN
     *
     * @ibatorgenerated Wed Sep 20 17:23:34 CST 2017
     */
    public Long getSn() {
        return sn;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method sets the value of the database column pano_order_logtc.SN
     *
     * @param sn the value for pano_order_logtc.SN
     *
     * @ibatorgenerated Wed Sep 20 17:23:34 CST 2017
     */
    public void setSn(Long sn) {
        this.sn = sn;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method returns the value of the database column pano_order_logtc.ID
     *
     * @return the value of pano_order_logtc.ID
     *
     * @ibatorgenerated Wed Sep 20 17:23:34 CST 2017
     */
    public String getId() {
        return id;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method sets the value of the database column pano_order_logtc.ID
     *
     * @param id the value for pano_order_logtc.ID
     *
     * @ibatorgenerated Wed Sep 20 17:23:34 CST 2017
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method returns the value of the database column pano_order_logtc.NAME
     *
     * @return the value of pano_order_logtc.NAME
     *
     * @ibatorgenerated Wed Sep 20 17:23:34 CST 2017
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method sets the value of the database column pano_order_logtc.NAME
     *
     * @param name the value for pano_order_logtc.NAME
     *
     * @ibatorgenerated Wed Sep 20 17:23:34 CST 2017
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method returns the value of the database column pano_order_logtc.TYPE
     *
     * @return the value of pano_order_logtc.TYPE
     *
     * @ibatorgenerated Wed Sep 20 17:23:34 CST 2017
     */
    public Integer getType() {
        return type;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method sets the value of the database column pano_order_logtc.TYPE
     *
     * @param type the value for pano_order_logtc.TYPE
     *
     * @ibatorgenerated Wed Sep 20 17:23:34 CST 2017
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method returns the value of the database column pano_order_logtc.PRICE
     *
     * @return the value of pano_order_logtc.PRICE
     *
     * @ibatorgenerated Wed Sep 20 17:23:34 CST 2017
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method sets the value of the database column pano_order_logtc.PRICE
     *
     * @param price the value for pano_order_logtc.PRICE
     *
     * @ibatorgenerated Wed Sep 20 17:23:34 CST 2017
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method returns the value of the database column pano_order_logtc.SEND
     *
     * @return the value of pano_order_logtc.SEND
     *
     * @ibatorgenerated Wed Sep 20 17:23:34 CST 2017
     */
    public Integer getSend() {
        return send;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method sets the value of the database column pano_order_logtc.SEND
     *
     * @param send the value for pano_order_logtc.SEND
     *
     * @ibatorgenerated Wed Sep 20 17:23:34 CST 2017
     */
    public void setSend(Integer send) {
        this.send = send;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method returns the value of the database column pano_order_logtc.RECEIVE
     *
     * @return the value of pano_order_logtc.RECEIVE
     *
     * @ibatorgenerated Wed Sep 20 17:23:34 CST 2017
     */
    public Integer getReceive() {
        return receive;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method sets the value of the database column pano_order_logtc.RECEIVE
     *
     * @param receive the value for pano_order_logtc.RECEIVE
     *
     * @ibatorgenerated Wed Sep 20 17:23:34 CST 2017
     */
    public void setReceive(Integer receive) {
        this.receive = receive;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method returns the value of the database column pano_order_logtc.ORDER_SN
     *
     * @return the value of pano_order_logtc.ORDER_SN
     *
     * @ibatorgenerated Wed Sep 20 17:23:34 CST 2017
     */
    public Long getOrderSn() {
        return orderSn;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method sets the value of the database column pano_order_logtc.ORDER_SN
     *
     * @param orderSn the value for pano_order_logtc.ORDER_SN
     *
     * @ibatorgenerated Wed Sep 20 17:23:34 CST 2017
     */
    public void setOrderSn(Long orderSn) {
        this.orderSn = orderSn;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method returns the value of the database column pano_order_logtc.USER_NAME
     *
     * @return the value of pano_order_logtc.USER_NAME
     *
     * @ibatorgenerated Wed Sep 20 17:23:34 CST 2017
     */
    public String getUserName() {
        return userName;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method sets the value of the database column pano_order_logtc.USER_NAME
     *
     * @param userName the value for pano_order_logtc.USER_NAME
     *
     * @ibatorgenerated Wed Sep 20 17:23:34 CST 2017
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method returns the value of the database column pano_order_logtc.USER_PHONE
     *
     * @return the value of pano_order_logtc.USER_PHONE
     *
     * @ibatorgenerated Wed Sep 20 17:23:34 CST 2017
     */
    public String getUserPhone() {
        return userPhone;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method sets the value of the database column pano_order_logtc.USER_PHONE
     *
     * @param userPhone the value for pano_order_logtc.USER_PHONE
     *
     * @ibatorgenerated Wed Sep 20 17:23:34 CST 2017
     */
    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method returns the value of the database column pano_order_logtc.USER_ADDRESS
     *
     * @return the value of pano_order_logtc.USER_ADDRESS
     *
     * @ibatorgenerated Wed Sep 20 17:23:34 CST 2017
     */
    public String getUserAddress() {
        return userAddress;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method sets the value of the database column pano_order_logtc.USER_ADDRESS
     *
     * @param userAddress the value for pano_order_logtc.USER_ADDRESS
     *
     * @ibatorgenerated Wed Sep 20 17:23:34 CST 2017
     */
    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method returns the value of the database column pano_order_logtc.REMARK
     *
     * @return the value of pano_order_logtc.REMARK
     *
     * @ibatorgenerated Wed Sep 20 17:23:34 CST 2017
     */
    public String getRemark() {
        return remark;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method sets the value of the database column pano_order_logtc.REMARK
     *
     * @param remark the value for pano_order_logtc.REMARK
     *
     * @ibatorgenerated Wed Sep 20 17:23:34 CST 2017
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method returns the value of the database column pano_order_logtc.ADDER_SN
     *
     * @return the value of pano_order_logtc.ADDER_SN
     *
     * @ibatorgenerated Wed Sep 20 17:23:34 CST 2017
     */
    public Long getAdderSn() {
        return adderSn;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method sets the value of the database column pano_order_logtc.ADDER_SN
     *
     * @param adderSn the value for pano_order_logtc.ADDER_SN
     *
     * @ibatorgenerated Wed Sep 20 17:23:34 CST 2017
     */
    public void setAdderSn(Long adderSn) {
        this.adderSn = adderSn;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method returns the value of the database column pano_order_logtc.ADDER_NAME
     *
     * @return the value of pano_order_logtc.ADDER_NAME
     *
     * @ibatorgenerated Wed Sep 20 17:23:34 CST 2017
     */
    public String getAdderName() {
        return adderName;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method sets the value of the database column pano_order_logtc.ADDER_NAME
     *
     * @param adderName the value for pano_order_logtc.ADDER_NAME
     *
     * @ibatorgenerated Wed Sep 20 17:23:34 CST 2017
     */
    public void setAdderName(String adderName) {
        this.adderName = adderName;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method returns the value of the database column pano_order_logtc.ADD_TIME
     *
     * @return the value of pano_order_logtc.ADD_TIME
     *
     * @ibatorgenerated Wed Sep 20 17:23:34 CST 2017
     */
    public Date getAddTime() {
        return addTime;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method sets the value of the database column pano_order_logtc.ADD_TIME
     *
     * @param addTime the value for pano_order_logtc.ADD_TIME
     *
     * @ibatorgenerated Wed Sep 20 17:23:34 CST 2017
     */
    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method returns the value of the database column pano_order_logtc.UPDATER_SN
     *
     * @return the value of pano_order_logtc.UPDATER_SN
     *
     * @ibatorgenerated Wed Sep 20 17:23:34 CST 2017
     */
    public Long getUpdaterSn() {
        return updaterSn;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method sets the value of the database column pano_order_logtc.UPDATER_SN
     *
     * @param updaterSn the value for pano_order_logtc.UPDATER_SN
     *
     * @ibatorgenerated Wed Sep 20 17:23:34 CST 2017
     */
    public void setUpdaterSn(Long updaterSn) {
        this.updaterSn = updaterSn;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method returns the value of the database column pano_order_logtc.UPDATER_NAME
     *
     * @return the value of pano_order_logtc.UPDATER_NAME
     *
     * @ibatorgenerated Wed Sep 20 17:23:34 CST 2017
     */
    public String getUpdaterName() {
        return updaterName;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method sets the value of the database column pano_order_logtc.UPDATER_NAME
     *
     * @param updaterName the value for pano_order_logtc.UPDATER_NAME
     *
     * @ibatorgenerated Wed Sep 20 17:23:34 CST 2017
     */
    public void setUpdaterName(String updaterName) {
        this.updaterName = updaterName;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method returns the value of the database column pano_order_logtc.UPDATE_TIME
     *
     * @return the value of pano_order_logtc.UPDATE_TIME
     *
     * @ibatorgenerated Wed Sep 20 17:23:34 CST 2017
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method sets the value of the database column pano_order_logtc.UPDATE_TIME
     *
     * @param updateTime the value for pano_order_logtc.UPDATE_TIME
     *
     * @ibatorgenerated Wed Sep 20 17:23:34 CST 2017
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method returns the value of the database column pano_order_logtc.encryptSn
     *
     * @return the value of pano_order_logtc.encryptSn
     *
     * @ibatorgenerated Wed Sep 20 17:23:34 CST 2017
     */
    public String getEncryptSn() {
        return encryptSn;
    }

    /**
     * This method was generated by Apache iBATIS ibator.
     * This method sets the value of the database column pano_order_logtc.encryptSn
     *
     * @param encryptSn the value for pano_order_logtc.encryptSn
     *
     * @ibatorgenerated Wed Sep 20 17:23:34 CST 2017
     */
    public void setEncryptSn(String encryptSn) {
        this.encryptSn = encryptSn;
    }
}