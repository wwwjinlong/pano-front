package com.focus3d.pano.order.controller;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import wxdev.pay.WXPay;
import wxdev.pay.common.Configure;
import wxdev.pay.common.Signature;
import wxdev.pay.protocol.pay_protocol.ScanPayResData;
import wxdev.pay.protocol.unified_order_protocol.UnifiedOrderReqData;
import wxdev.pay.protocol.unified_order_protocol.UnifiedOrderResData;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.focus3d.pano.filter.LoginThreadLocal;
import com.focus3d.pano.login.service.PanoMemLoginService;
import com.focus3d.pano.member.service.PanoUserBankcardService;
import com.focus3d.pano.member.service.PanoUserReceiveAddressService;
import com.focus3d.pano.model.PanoMemLoginModel;
import com.focus3d.pano.model.PanoMemUserModel;
import com.focus3d.pano.model.PanoOrderCouponItemModel;
import com.focus3d.pano.model.PanoOrderCouponModel;
import com.focus3d.pano.model.PanoOrderLogtcModel;
import com.focus3d.pano.model.PanoOrderModel;
import com.focus3d.pano.model.PanoOrderPackageDetailModel;
import com.focus3d.pano.model.PanoOrderPackageModel;
import com.focus3d.pano.model.PanoOrderShopcartDetailModel;
import com.focus3d.pano.model.PanoOrderShopcartModel;
import com.focus3d.pano.model.PanoOrderTransModel;
import com.focus3d.pano.model.PanoProjectHousePackageModel;
import com.focus3d.pano.model.PanoProjectModel;
import com.focus3d.pano.model.PanoUserBankcardModel;
import com.focus3d.pano.model.PanoUserReceiveAddressModel;
import com.focus3d.pano.model.PanoValidateModel;
import com.focus3d.pano.order.pay.config.LianAuthPayConfig;
import com.focus3d.pano.order.pay.config.LianQuickPayConfig;
import com.focus3d.pano.order.pay.config.WxPayConfig;
import com.focus3d.pano.order.service.PanoOrderCouponItemService;
import com.focus3d.pano.order.service.PanoOrderLogtcService;
import com.focus3d.pano.order.service.PanoOrderPackageDetailService;
import com.focus3d.pano.order.service.PanoOrderPackageService;
import com.focus3d.pano.order.service.PanoOrderService;
import com.focus3d.pano.order.service.PanoOrderTransService;
import com.focus3d.pano.pay.lianlian.utils.YinTongUtil;
import com.focus3d.pano.project.service.PanoProjectHousePackageService;
import com.focus3d.pano.project.service.PanoProjectService;
import com.focus3d.pano.pub.controller.AbstractPanoController;
import com.focus3d.pano.shopcart.service.PanoOrderShopCartService;
import com.focus3d.pano.sms.service.SmsValidateService;
import com.focus3d.pano.user.service.PanoMemUserService;
import com.focus3d.pano.utils.Override;
import com.focus3d.pano.utils.PayUtils;
import com.focustech.common.utils.EncryptUtil;
import com.focustech.common.utils.HttpUtil;
import com.focustech.common.utils.TCUtil;
import com.lianpay.share.security.Md5Algorithm;
import com.lianpay.share.util.DateUtil;
import com.llpay.client.vo.PayDataBean;
import com.llpay.client.vo.RetBean;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.yintong.paywap.domain.PaymentInfo;

/**
 * 
 * 
 * @author lihaijun
 * 
 */
@Controller
@RequestMapping(value = "/order")
public class PanoOrderController extends AbstractPanoController {
	private static Logger logger = Logger.getLogger(PanoOrderController.class);
	@Autowired
	private PanoProjectHousePackageService<PanoProjectHousePackageModel> housePackageService;
	@Autowired
	private PanoUserReceiveAddressService<PanoUserReceiveAddressModel> receiveAddressService;
	@Autowired
	private PanoOrderCouponItemService<PanoOrderCouponItemModel> panoOrderCouponItemService;
	@Autowired
	private PanoOrderPackageService<PanoOrderPackageModel> panoOrderPackageService;
	@Autowired
	private PanoOrderService<PanoOrderModel> orderService;
	@Autowired
	private PanoOrderPackageDetailService<PanoOrderPackageDetailModel> panoOrderPackageDetailService;
	@Autowired
	private PanoMemUserService<PanoMemUserModel> panoMemUserService;
	@Autowired
	private PanoMemLoginService<PanoMemLoginModel> panoMemLoginService;
	@Autowired
	private PanoOrderTransService<PanoOrderTransModel> panoOrderTransService;
	@Autowired
	private SmsValidateService smsValidateService;
	@Autowired
	private PanoOrderShopCartService<PanoOrderShopcartModel> shopCartService;
	@Autowired
	private PanoUserBankcardService<PanoUserBankcardModel> userBankcardService;
	@Autowired
	private PanoOrderLogtcService<PanoOrderLogtcModel> orderLogtcService;
	@Autowired
	private PanoProjectService<PanoProjectModel> projectService;

	/**
	 * 跳转到支付页面
	 * @param request
	 * @param map
	 * @return
	 * @throws NumberFormatException
	 * @throws SQLException
	 */
	@RequestMapping(value = "/topaypage")
	public String toPayPage(HttpServletRequest request, ModelMap map) throws NumberFormatException, SQLException {
		Long userSn = LoginThreadLocal.getLoginInfo().getUserSn();
		String orderSn = request.getParameter("order_sn");
		List<PanoUserBankcardModel> userBankcards = userBankcardService.listByUser(userSn);
		if (userBankcards != null && !userBankcards.isEmpty()) {
			PanoUserBankcardModel bankcard = userBankcards.get(0);
			String maskName = Override.getMaskCharWay(bankcard.getUserName(), 1, 1);
			String cardNo = bankcard.getCardNo();
			String maskCardNo = Override.getMaskCharWay(cardNo, cardNo.length() / 2, cardNo.length() - 1);
			String certNo = bankcard.getCertNo();
			String maskCertNo = Override.getMaskCharWay(certNo, certNo.length() / 2, certNo.length() - 2);
			bankcard.setMaskName(maskName);
			bankcard.setMaskCardNo(maskCardNo);
			bankcard.setMaskCertNo(maskCertNo);
			map.put("userBankcard", bankcard);
		}
		PanoOrderModel order = orderService.getOrderDetail(Long.parseLong(orderSn));
		map.put("order", order);
		String orderNum = order.getOrderNum();
		PanoOrderModel parentOrder = order.getParentOrder();
		if(parentOrder != null){
			orderNum = parentOrder.getOrderNum();
		}
		map.put("orderNum", orderNum);
		return "/member/order/pay";
	}

	/**
	 * 跳转到订单确认页面
	 * @param request
	 * @param modelMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/confirmpage")
	public String orderConfirmPage(HttpServletRequest request, ModelMap modelMap)throws Exception {
		Long userSn = LoginThreadLocal.getLoginInfo().getUserSn();
		String packageSnsParam = request.getParameter("packageSns");
		String projectEncryptSn = request.getParameter("projectId");
		String[] shopcarts = packageSnsParam.split(",");
		// find packages
		BigDecimal payAmount = BigDecimal.ZERO;
		List<PanoOrderShopcartModel> shopcartList = new ArrayList<PanoOrderShopcartModel>();
		for (String shopcartEncryptSn : shopcarts) {
			PanoOrderShopcartModel shopcart = shopCartService.getBySn(EncryptUtil.decode(shopcartEncryptSn));
			shopCartService.setShopcartDetail(shopcart);
			shopcartList.add(shopcart);
			payAmount = payAmount.add(shopcart.getHousePackage().getPackagePrice());
		}
		// find address and default address
		List<PanoUserReceiveAddressModel> address = receiveAddressService.listByUser(userSn);
		PanoUserReceiveAddressModel defaultAddress = null;
		if (address != null && !address.isEmpty()) {
			for (PanoUserReceiveAddressModel addressTmp : address) {
				if (addressTmp.getDefaultFirst() == 1) {
					defaultAddress = addressTmp;
					break;
				}
			}
			if (defaultAddress == null){
				defaultAddress = address.get(0);
			}
		}
		modelMap.put("shopcartList", shopcartList);
		modelMap.put("address", address);
		modelMap.put("defaultAddress", defaultAddress);
		modelMap.put("payAmount", payAmount);
		PanoMemUserModel memUser = panoMemUserService.getBySn(userSn);
		modelMap.put("regMobile", com.focustech.common.utils.StringUtils.isNotEmpty(memUser.getMobile()));
		modelMap.put("packageSns", packageSnsParam);
		PanoProjectModel projectModel = projectService.getBySn(EncryptUtil.decode(projectEncryptSn));
		//优惠折扣
		double discount = projectModel.getDiscount() == null ? 1 : projectModel.getDiscount().doubleValue();
		modelMap.put("discount", discount);
		String discountName = TCUtil.sv((int)(discount * 100));
		modelMap.put("discountName", discountName.replace("0", ""));
		//分期付
		double percent = projectModel.getPercent() == null ? 1 : projectModel.getPercent().doubleValue();
		modelMap.put("percent", percent);
		String percentName = TCUtil.sv((int)(percent * 10));
		modelMap.put("percentName", percentName.replace("0", ""));
		
		modelMap.put("projectId", projectModel.getEncryptSn());
		return "/member/order/confirm";
	}

	/**
	 * 获取优惠券信息
	 * @param request
	 * @param response
	 * @throws Exception 
	 */
	@RequestMapping(value = "/coupon")
	public void coupon(HttpServletRequest request, HttpServletResponse response)throws Exception {
		String code = request.getParameter("code");
		String projectId = HttpUtil.sv(request, "projectId");
		PanoOrderCouponItemModel panoOrderCouponItemModel = panoOrderCouponItemService.getByCode(EncryptUtil.decode(projectId), code);
		JSONObject jo = new JSONObject();
		if (panoOrderCouponItemModel != null) {
			jo.put("status", panoOrderCouponItemModel.getStatus());
			PanoOrderCouponModel coupon = panoOrderCouponItemModel.getCoupon();
			jo.put("discountAmount", coupon.getPriceDiscount());
			jo.put("useCondition", coupon.getUseCondition());
		}
		ajaxOutput(response, jo.toString());
	}

	@RequestMapping(value = "/phoneexist")
	public void phoneExist(HttpServletRequest request,
			HttpServletResponse response, ModelMap map) throws Exception {
		JSONObject data = new JSONObject();
		try {
			String phone = StringUtils
					.trimToNull(request.getParameter("phone"));

			// if (panoUserService.getByMobile(phone) == null) {
			// data.put("exist", 0);
			// } else {
			// data.put("exist", 1);
			// }
			data.put("exist", 1);
			data.put("status", 0);
		} catch (Exception e) {
			logger.debug(ExceptionUtils.getStackTrace(e));
			data.put("statusMsg", e.getMessage());
			data.put("status", 1);
		}

		ajaxOutput(response, data.toString());
	}

	/**
	 * 支付
	 * @param request
	 * @param response
	 * @param map
	 * @throws Exception
	 */
	@RequestMapping(value = "/pay")
	public void pay(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {
		Long userSn = LoginThreadLocal.getLoginInfo().getUserSn();
		String orderSn = StringUtils.trimToNull(request.getParameter("order_sn"));
		String payType = StringUtils.trimToNull(request.getParameter("pay_type"));
		JSONObject data = new JSONObject();
		try {
			PanoOrderModel orderModel = orderService.getBySn(Long.parseLong(orderSn));
			if (orderModel == null){
				throw new RuntimeException("订单不存在");
			}
			if (userSn.compareTo(orderModel.getUserSn()) != 0){
				throw new RuntimeException("不是您的订单");
			}
			if (orderModel.getStatus() == 2){
				throw new RuntimeException("订单已经支付");
			}
			if (orderModel.getPayMoney().compareTo(BigDecimal.ZERO) != 1){
				throw new RuntimeException("订单金额有误");
			}
			if (orderModel.getParentOrderSn() != null && orderModel.getParentOrderSn() != -1) {
				PanoOrderModel parentOrder = orderService.getBySn(orderModel.getParentOrderSn());
				if (parentOrder.getStatus() != 2){
					throw new RuntimeException("必须先完成定金支付，再支付尾款");
				}
			}
			//data.put("unPayedParentOrderSn", orderModel.getParentOrderSn().toString());
			PanoMemUserModel panoMemUserModel = panoMemUserService.getBySn(orderModel.getUserSn());
			// 根据支付类型组装相应参数
			if ("LIANPAY_AUTH".equals(payType)) {
				//连连认证支付
				String acctName = StringUtils.trimToNull(request.getParameter("acct_name"));
				String cardNo = StringUtils.trimToNull(request.getParameter("card_no"));
				String idNo = StringUtils.trimToNull(request.getParameter("id_no"));
				String userBankcardSnParam = StringUtils.trimToNull(request.getParameter("user_bankcard_sn"));
				Long userBankcardSn = TCUtil.lv(userBankcardSnParam);
				if (acctName == null){
					throw new RuntimeException("请输入姓名");
				}
				if (cardNo == null){
					throw new RuntimeException("请输入卡号");
				} else if(cardNo.length() < 10){
					throw new RuntimeException("卡号输入有误");
				}
				if (idNo == null){
					throw new RuntimeException("请输入身份证");
				} else if(idNo.length() < 10){
					throw new RuntimeException("身份证号码输入有误");
				}
				// 保存银行卡信息
				PanoUserBankcardModel userBankcard = null;
				if (TCUtil.lv(userBankcardSn) > 0) {
					userBankcard = userBankcardService.getBySn(userBankcardSn);
				} else {
					if(userBankcard == null){
						userBankcard = userBankcardService.getByCardNo(userSn, cardNo);
						if(userBankcard != null){
							userBankcardService.delete(userBankcard);
						}
					}
					userBankcard = new PanoUserBankcardModel();
					userBankcard.setUserSn(userSn);
					userBankcard.setCardNo(cardNo);
					userBankcard.setUserName(acctName);
					userBankcard.setCertNo(idNo);
					userBankcardService.insert(userBankcard);
				}
				//组装连连支付报文
				StringBuffer strBuf = new StringBuffer();
				PaymentInfo payInfo = new PaymentInfo();
				payInfo.setAcct_name(userBankcard.getUserName());
				strBuf.append("&acct_name=").append(payInfo.getAcct_name());
				// 请求应用标识 1-Android 2-ios 3-WAP
				payInfo.setApp_request("3");
				strBuf.append("&app_request=").append(payInfo.getApp_request());
				// payInfo.setBg_color(request.getParameter("bg_color"));
				// 商户业务类型 虚拟商品销售：101001实物商品销售：109001
				payInfo.setBusi_partner(LianAuthPayConfig.BUSI_PARTNER);
				strBuf.append("&busi_partner=").append(payInfo.getBusi_partner());

				payInfo.setCard_no(userBankcard.getCardNo());
				strBuf.append("&card_no=").append(payInfo.getCard_no());

				// payInfo.setCard_no(request.getParameter("card_no"));
				payInfo.setDt_order(DateUtil.getCurrentDateTimeStr1());
				strBuf.append("&dt_order=").append(payInfo.getDt_order());

				payInfo.setId_no(userBankcard.getCertNo());
				strBuf.append("&id_no=").append(payInfo.getId_no());
				// 订单描述变(255)
				// payInfo.setInfo_order(request.getParameter("info_order"));
				// 交易金额该笔订单的资金总额，单位为RMB-元。大于 0的数字，精确到小数点后两位。如：49.65
				payInfo.setMoney_order(orderModel.getPayMoney().toString());
				strBuf.append("&money_order=").append(payInfo.getMoney_order());
				// 商品名称
				payInfo.setName_goods("产品套餐");
				strBuf.append("&name_goods=").append(payInfo.getName_goods());
				// payInfo.setNo_agree(request.getParameter("no_agree"));签约协议号
				// 商户唯一订单号
				payInfo.setNo_order(orderModel.getOrderNum() + "");
				strBuf.append("&no_order=").append(payInfo.getNo_order());
				payInfo.setNotify_url(LianAuthPayConfig.NOTIFY_URL);
				strBuf.append("&notify_url=").append(payInfo.getNotify_url());
				// 商户编号
				payInfo.setOid_partner(LianAuthPayConfig.OID_PARTNER);
				strBuf.append("&oid_partner=").append(payInfo.getOid_partner());
				// payInfo.setAcct_name(request.getParameter("acct_name"));
				// 风险控制参数
				JSONObject riskItem = new JSONObject();
				riskItem.put("frms_ware_category", "2031");//产品类目
				riskItem.put("user_info_mercht_userno", TCUtil.sv(orderModel.getUserSn()));//平台用户id
				riskItem.put("user_info_dt_register", DateUtil.getCurrentDateTimeStr1(panoMemUserModel.getAddTime()));//平台用户注册时间
				riskItem.put("user_info_bind_phone", TCUtil.sv(panoMemUserModel.getMobile()));//平台用户绑定的手机号
				riskItem.put("user_info_full_name", userBankcard.getUserName());//平台用户实名
				riskItem.put("user_info_id_no", userBankcard.getCertNo());//平台用户身份证号
				riskItem.put("user_info_identify_state", 0);//是否实名
				payInfo.setRisk_item(riskItem.toJSONString());
				strBuf.append("&risk_item=").append(payInfo.getRisk_item());

				payInfo.setSign_type("MD5");
				strBuf.append("&sign_type=").append(payInfo.getSign_type());

				payInfo.setUrl_return(LianAuthPayConfig.RETURN_URL);
				strBuf.append("&url_return=").append(payInfo.getUrl_return());

				payInfo.setUser_id(orderModel.getUserSn() + "");
				strBuf.append("&user_id=").append(payInfo.getUser_id());
				// payInfo.setValid_order(request.getParameter("valid_order"));

				String sign_src = strBuf.toString();
				if (sign_src.startsWith("&")) {
					sign_src = sign_src.substring(1);
				}
				sign_src += "&key=" + LianAuthPayConfig.MD5_KEY;
				String sign = Md5Algorithm.getInstance().md5Digest(sign_src.getBytes("utf-8"));
				payInfo.setSign(sign);
				String req_data = JSON.toJSONString(payInfo);
				logger.debug(req_data);
				data.put("linkString", req_data);
				data.put("outGateway", LianAuthPayConfig.PAY_URL);

			} 
			else if("LIANPAY_QUICK".equals(payType)){
				//连连快捷支付
				String acctName = StringUtils.trimToNull(request.getParameter("acct_name"));
				String cardNo = StringUtils.trimToNull(request.getParameter("card_no"));
				String idNo = StringUtils.trimToNull(request.getParameter("id_no"));
				String userBankcardSnParam = StringUtils.trimToNull(request.getParameter("user_bankcard_sn"));
				Long userBankcardSn = TCUtil.lv(userBankcardSnParam);
				if (acctName == null){
					throw new RuntimeException("请输入姓名");
				}
				if (cardNo == null){
					throw new RuntimeException("请输入卡号");
				} else if(cardNo.length() < 10){
					throw new RuntimeException("卡号输入有误");
				}
				if (idNo == null){
					throw new RuntimeException("请输入身份证");
				} else if(idNo.length() < 10){
					throw new RuntimeException("身份证号码输入有误");
				}
				// 保存银行卡信息
				PanoUserBankcardModel userBankcard = null;
				if (TCUtil.lv(userBankcardSn) > 0) {
					userBankcard = userBankcardService.getBySn(userBankcardSn);
				} else {
					if(userBankcard == null){
						userBankcard = userBankcardService.getByCardNo(userSn, cardNo);
						if(userBankcard != null){
							userBankcardService.delete(userBankcard);
						}
					}
					userBankcard = new PanoUserBankcardModel();
					userBankcard.setUserSn(userSn);
					userBankcard.setCardNo(cardNo);
					userBankcard.setUserName(acctName);
					userBankcard.setCertNo(idNo);
					userBankcardService.insert(userBankcard);
				}
				//组装连连支付报文
				StringBuffer strBuf = new StringBuffer();
				PaymentInfo payInfo = new PaymentInfo();
				payInfo.setAcct_name(userBankcard.getUserName());
				strBuf.append("&acct_name=").append(payInfo.getAcct_name());
				// 请求应用标识 1-Android 2-ios 3-WAP
				payInfo.setApp_request("3");
				strBuf.append("&app_request=").append(payInfo.getApp_request());
				// payInfo.setBg_color(request.getParameter("bg_color"));
				// 商户业务类型 虚拟商品销售：101001实物商品销售：109001
				payInfo.setBusi_partner(LianQuickPayConfig.BUSI_PARTNER);
				strBuf.append("&busi_partner=").append(payInfo.getBusi_partner());

				payInfo.setCard_no(userBankcard.getCardNo());
				strBuf.append("&card_no=").append(payInfo.getCard_no());

				// payInfo.setCard_no(request.getParameter("card_no"));
				payInfo.setDt_order(DateUtil.getCurrentDateTimeStr1());
				strBuf.append("&dt_order=").append(payInfo.getDt_order());

				payInfo.setId_no(userBankcard.getCertNo());
				strBuf.append("&id_no=").append(payInfo.getId_no());
				// 订单描述变(255)
				// payInfo.setInfo_order(request.getParameter("info_order"));
				// 交易金额该笔订单的资金总额，单位为RMB-元。大于 0的数字，精确到小数点后两位。如：49.65
				payInfo.setMoney_order(orderModel.getPayMoney().toString());
				strBuf.append("&money_order=").append(payInfo.getMoney_order());
				// 商品名称
				payInfo.setName_goods("产品套餐");
				strBuf.append("&name_goods=").append(payInfo.getName_goods());
				// payInfo.setNo_agree(request.getParameter("no_agree"));签约协议号
				// 商户唯一订单号
				payInfo.setNo_order(orderModel.getOrderNum() + "");
				strBuf.append("&no_order=").append(payInfo.getNo_order());
				payInfo.setNotify_url(LianQuickPayConfig.NOTIFY_URL);
				strBuf.append("&notify_url=").append(payInfo.getNotify_url());
				// 商户编号
				payInfo.setOid_partner(LianQuickPayConfig.OID_PARTNER);
				strBuf.append("&oid_partner=").append(payInfo.getOid_partner());
				// payInfo.setAcct_name(request.getParameter("acct_name"));
				// 风险控制参数
				JSONObject riskItem = new JSONObject();
				riskItem.put("frms_ware_category", "2031");//产品类目
				riskItem.put("user_info_mercht_userno", TCUtil.sv(orderModel.getUserSn()));//平台用户id
				riskItem.put("user_info_dt_register", DateUtil.getCurrentDateTimeStr1(panoMemUserModel.getAddTime()));//平台用户注册时间
				riskItem.put("user_info_bind_phone", TCUtil.sv(panoMemUserModel.getMobile()));//平台用户绑定的手机号
				riskItem.put("user_info_full_name", userBankcard.getUserName());//平台用户实名
				riskItem.put("user_info_id_no", userBankcard.getCertNo());//平台用户身份证号
				riskItem.put("user_info_identify_state", 0);//是否实名
				payInfo.setRisk_item(riskItem.toJSONString());
				strBuf.append("&risk_item=").append(payInfo.getRisk_item());

				payInfo.setSign_type("MD5");
				strBuf.append("&sign_type=").append(payInfo.getSign_type());

				payInfo.setUrl_return(LianQuickPayConfig.RETURN_URL);
				strBuf.append("&url_return=").append(payInfo.getUrl_return());

				payInfo.setUser_id(orderModel.getUserSn() + "");
				strBuf.append("&user_id=").append(payInfo.getUser_id());
				// payInfo.setValid_order(request.getParameter("valid_order"));

				String sign_src = strBuf.toString();
				if (sign_src.startsWith("&")) {
					sign_src = sign_src.substring(1);
				}
				sign_src += "&key=" + LianQuickPayConfig.MD5_KEY;
				String sign = Md5Algorithm.getInstance().md5Digest(sign_src.getBytes("utf-8"));
				payInfo.setSign(sign);
				String req_data = JSON.toJSONString(payInfo);
				logger.debug(req_data);
				data.put("linkString", req_data);
				data.put("outGateway", LianQuickPayConfig.PAY_URL);
			}
			else if ("WX_H5".equals(payType)) {
				//微信公众号支付
				//验证微信登录 获取微信openid
				PanoMemLoginModel memLogin = panoMemLoginService
						.getBySn(LoginThreadLocal.getLoginInfo().getSn());
				if (memLogin.getType() != 1 || memLogin.getStatus() != 1) {
					throw new RuntimeException("请用微信公众号登录");
				}
				String out_trade_no = orderModel.getOrderNum() + "";
				String notify_url = WxPayConfig.NOTIFY_URL;
				//组装微信公众号支付报文
				Configure configure = new Configure();
				configure.setAppID(WxPayConfig.APPID);
				configure.setMchID(WxPayConfig.MCHID);
				configure.setKey(WxPayConfig.MCHKEY);
				String attach = payType;
				String openId = memLogin.getLoginName();
				//统一下单接口
				UnifiedOrderReqData unifiedOrderReqData = new UnifiedOrderReqData(
						"body", 
						out_trade_no,
						TCUtil.iv(PayUtils.convertYuan2Fen(orderModel.getPayMoney())),
						getRemoteIp(request), 
						notify_url,
						"JSAPI", 
						openId, 
						attach, 
						configure);
				UnifiedOrderResData resData = WXPay.requestUnifiedOrderService(unifiedOrderReqData, configure);
				if (!resData.getIsSuccess()){
					throw new RuntimeException(resData.getWorkedMsg());
				}
				String timestamp = new Date().getTime() / 1000l + "";
				data.put("appId", resData.getAppid());
				data.put("package", "prepay_id=" + resData.getPrepay_id());
				data.put("nonceStr", resData.getNonce_str());
				data.put("timeStamp", timestamp);
				data.put("signType", "MD5");

				String sign = Signature.getSign(data, configure);
				data.put("paySign", sign);
				data.remove("package");
				data.put("pack", "prepay_id=" + resData.getPrepay_id());
				data.put("returnUrl", WxPayConfig.RETURN_URL);
				data.put("orderSn", orderModel.getEncryptSn());
			} else {
				throw new RuntimeException("不支持的支付方式");
			}
			data.put("status", 0);
			data.put("payType", payType);
		} catch (Exception e) {
			logger.debug(ExceptionUtils.getStackTrace(e));
			data.put("statusMsg", e.getMessage());
			data.put("status", 1);
		}
		ajaxOutput(response, data.toString());
	}

	/**
	 * 下单
	 * @param request
	 * @param response
	 * @param map
	 * @throws Exception
	 */
	@RequestMapping(value = "/create")
	public void createOrder(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {
		String projectEncryptSn = HttpUtil.sv(request, "projectId");
		PanoProjectModel project = projectService.getBySn(EncryptUtil.decode(projectEncryptSn));
		PanoMemLoginModel loginInfo = LoginThreadLocal.getLoginInfo();
		Long userSn = loginInfo.getUserSn();
		JSONObject data = new JSONObject();
		PanoMemUserModel member = panoMemUserService.getBySn(userSn);
		try {
			if(StringUtils.isEmpty(member.getMobile())){
				String mobilePhone = request.getParameter("mobile_phone");
				String verifycode = request.getParameter("verifycode");
				if(StringUtils.isNotEmpty(mobilePhone) && StringUtils.isNotEmpty(verifycode)) {
					PanoValidateModel messageValidate = smsValidateService.selectByMobilePhone(mobilePhone, verifycode);
					if (messageValidate != null && messageValidate.getStatus() == 1) {
						smsValidateService.setStatus(messageValidate, 0);
						//更新用户信息
						member.setMobile(mobilePhone);
						panoMemUserService.update(member);
					} else {
						throw new RuntimeException("验证码错误");
					}
				} else {
					throw new RuntimeException("请输入手机号和短信验证码");
				}
			}
			String[] packageSns = request.getParameterValues("package_sns[]");
			String[] packageCounts = request.getParameterValues("package_counts[]");
			String couponCode = StringUtils.trimToNull(request.getParameter("coupon_code"));
			String payScheme = StringUtils.trimToNull(request.getParameter("pay_scheme"));
			String addressSnParam = StringUtils.trimToNull(request.getParameter("address_sn"));
			Long addressSn = Long.parseLong(addressSnParam);
			if (receiveAddressService.getBySn(addressSn) == null) {
				throw new RuntimeException("收货地址不存在");
			}
			float actualAmount = 0;
			float dueAmount = 0;
			float payAmount = 0;
			float discountAmount = 0;
			int totalPackageCount = 0;
			// 校验套餐，计算总金额
			List<PanoProjectHousePackageModel> packages = new ArrayList<PanoProjectHousePackageModel>();
			for (int i = 0; i < packageSns.length - 1; i++) {
				PanoProjectHousePackageModel pack = housePackageService.getBySn(Long.parseLong(packageSns[i]));
				if (pack == null){
					throw new RuntimeException("套餐不存在");
				}
				packages.add(pack);
				int packageCount = Integer.parseInt(packageCounts[i]);
				actualAmount = actualAmount + pack.getDiscountPrice().floatValue() * packageCount;
				totalPackageCount = totalPackageCount + packageCount;
			}
			dueAmount = actualAmount;
			// 校验优惠券，计算金额
			PanoOrderCouponItemModel coupon = null;
			if (couponCode != null) {
				coupon = panoOrderCouponItemService.getByCode(project.getSn(), couponCode);
				if (coupon == null) {
					throw new RuntimeException("优惠券不存在");
				} else if (coupon.getStatus() != 0) {
					throw new RuntimeException("优惠券不可用");
				}
				PanoOrderCouponModel cp = coupon.getCoupon();
				discountAmount = cp.getPriceDiscount().floatValue();
				dueAmount = dueAmount - discountAmount;
			}
			// 分支付方案处理
			PanoOrderModel orderModel = null;
			//优惠
			float discount = 1.00f;
			float percent = 1.00f;
			if ("FULL".equals(payScheme)) {
				discount = project.getDiscount().floatValue();
				// 全款支付
				payAmount = dueAmount * discount;
				orderModel = new PanoOrderModel();
				orderModel.setOrderNum(generateOrderNum());
				orderModel.setOrderTime(new Date());
				orderModel.setStatus(1);
				orderModel.setAddressSn(addressSn);
				orderModel.setUserSn(userSn);
				orderModel.setSumMoney(new BigDecimal(dueAmount));
				orderModel.setDiscountMoney(new BigDecimal(discountAmount));
				orderModel.setPayMoney(new BigDecimal(payAmount));
				orderModel.setLeftMoney(BigDecimal.ZERO);
				orderModel.setLeftPay(0);
				orderModel.setAgreeClause(1);
				orderModel.setParentOrderSn(-1l);
				orderModel.setDiscount(new BigDecimal(discount));
				orderService.insert(orderModel);
			} else if ("STAGES".equals(payScheme)) {
				percent = project.getPercent().floatValue();
				// 分期付款
				float stage1Amount = dueAmount * percent;
				float stage2Amount = dueAmount - stage1Amount;
				payAmount = stage1Amount;
				orderModel = new PanoOrderModel();
				orderModel.setOrderNum(generateOrderNum());
				orderModel.setOrderTime(new Date());
				orderModel.setStatus(1);
				orderModel.setAddressSn(addressSn);
				orderModel.setUserSn(userSn);
				orderModel.setSumMoney(new BigDecimal(dueAmount));
				orderModel.setDiscountMoney(new BigDecimal(discountAmount));
				orderModel.setPayMoney(new BigDecimal(payAmount));
				orderModel.setLeftMoney(new BigDecimal(stage2Amount));
				orderModel.setLeftPay(1);
				orderModel.setAgreeClause(1);
				orderModel.setParentOrderSn(-1l);
				orderModel.setDiscount(new BigDecimal(discount));
				orderService.insert(orderModel);
				//子订单
				PanoOrderModel stage2OrderModel = new PanoOrderModel();
				stage2OrderModel.setOrderNum(generateOrderNum());
				stage2OrderModel.setOrderTime(new Date());
				stage2OrderModel.setStatus(1);
				stage2OrderModel.setAddressSn(addressSn);
				stage2OrderModel.setUserSn(userSn);
				stage2OrderModel.setSumMoney(new BigDecimal(stage2Amount));
				stage2OrderModel.setDiscountMoney(BigDecimal.ZERO);
				stage2OrderModel.setPayMoney(new BigDecimal(stage2Amount));
				stage2OrderModel.setLeftMoney(BigDecimal.ZERO);
				stage2OrderModel.setLeftPay(0);
				stage2OrderModel.setAgreeClause(1);
				stage2OrderModel.setParentOrderSn(orderModel.getSn());
				stage2OrderModel.setDiscount(new BigDecimal(discount));
				orderService.insert(stage2OrderModel);
			} else {
				throw new RuntimeException("不支持的支付方案");
			}
			// 保存优惠券使用者信息
			if (coupon != null) {
				coupon.setUserSn(userSn);
				coupon.setOrderSn(orderModel.getSn());
				panoOrderCouponItemService.update(coupon);
			}
			//保存物流信息
			saveOrderLogtcInfo(orderModel);
			// 保存订单中的套餐信息
			for (int i = 0; i < packages.size(); i++) {
				PanoOrderPackageModel orderPackageModel = new PanoOrderPackageModel();
				orderPackageModel.setOrderSn(orderModel.getSn());
				orderPackageModel.setHousePackageSn(packages.get(i).getSn());
				orderPackageModel.setPurchaseNum(Integer.parseInt(packageCounts[i]));
				orderPackageModel.setPrice(packages.get(i).getPackagePrice());
				panoOrderPackageService.insert(orderPackageModel);
				PanoOrderShopcartModel shopcart = shopCartService.getUserShopcartPackage(userSn, packages.get(i).getSn());
				if (shopcart != null) {
					for (PanoOrderShopcartDetailModel shopcartPackageDetail : shopcart.getDetails()) {
						PanoOrderPackageDetailModel orderPackageDetailModel = new PanoOrderPackageDetailModel();
						orderPackageDetailModel.setOrderPackageSn(orderPackageModel.getSn());
						orderPackageDetailModel.setPackageTypeSn(shopcartPackageDetail.getPackageTypeSn());
						orderPackageDetailModel.setPackageProductSn(shopcartPackageDetail.getPackageProductSn());
						panoOrderPackageDetailService.insert(orderPackageDetailModel);
					}
				}
			}
			data.put("status", 0);
			data.put("orderSn", orderModel.getSn());

		} catch (Exception e) {
			logger.debug(ExceptionUtils.getStackTrace(e));
			data.put("statusMsg", e.getMessage());
			data.put("status", 1);
		}

		ajaxOutput(response, data.toString());
	}
	/**
	 * 保存订单物流信息
	 * *
	 * @param orderModel
	 */
	private void saveOrderLogtcInfo(PanoOrderModel orderModel) {
		Long orderSn = orderModel.getSn();
		int type = 1;//物流类型 1-快递 2-物流
		int send = 0;//是否发货 0-未发货 1-发货
		int receive = 0;//是否已经收到货 0-未收货 1-已收货
		Long addressSn = orderModel.getAddressSn();
		PanoUserReceiveAddressModel userReceiveAddressModel = receiveAddressService.getBySn(addressSn);
		String userName = userReceiveAddressModel.getUserName();
		String mobile = userReceiveAddressModel.getMobile();
		String province = TCUtil.sv(userReceiveAddressModel.getProvince());
		String city = TCUtil.sv(userReceiveAddressModel.getCity());
		String area = TCUtil.sv(userReceiveAddressModel.getArea());
		String street = TCUtil.sv(userReceiveAddressModel.getStreet());
		PanoOrderLogtcModel orderLogtcModel = new PanoOrderLogtcModel();
		orderLogtcModel.setType(type);
		orderLogtcModel.setSend(send);
		orderLogtcModel.setReceive(receive);
		orderLogtcModel.setUserName(userName);
		orderLogtcModel.setUserPhone(mobile);
		orderLogtcModel.setOrderSn(orderSn);
		orderLogtcModel.setUserAddress(province + " " + city + " " + area + " " + street);
	    orderLogtcService.insert(orderLogtcModel);
	}

	/**
	 * 订单列表页
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/orderspage")
	public String ordersPage(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {
		Long userSn = LoginThreadLocal.getLoginInfo().getUserSn();
		String statusParam = StringUtils.trimToNull(request.getParameter("status"));
		Integer status = statusParam == null ? null : Integer.parseInt(statusParam);
		
		List<PanoOrderModel> orders = orderService.listByUser(userSn, status);
		//分组显示
 		Map<Long, PanoOrderVo> projectGroupMap = new HashMap<Long, PanoOrderVo>();
		for(PanoOrderModel order : orders){
			List<PanoOrderPackageModel> packages = order.getOrderPackages();
			for (PanoOrderPackageModel pk : packages) {
				PanoProjectHousePackageModel housePackage = pk.getHousePackage();
				if(housePackage == null){
					continue;
				}
				PanoProjectModel project = housePackage.getProject();
				if(project == null){
					continue;
				}
				//按项目分组
				Long projectSn = project.getSn();
				if(projectGroupMap.containsKey(projectSn)){
					List<PanoOrderModel> ords = projectGroupMap.get(projectSn).getOrders();
					if(!ords.contains(order)){
						ords.add(order);
					}
				} else {
					PanoOrderVo v = new PanoOrderVo();
					v.setProject(project);
					v.getOrders().add(order);
					projectGroupMap.put(projectSn, v);
				}
			}
		}
		map.put("orderGroupMap", projectGroupMap);
		map.put("status", StringUtils.isEmpty(statusParam) ? "0" : statusParam);
		return "/member/order/orderAll";
	}

	/**
	 * 订单页面
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/orderpage")
	public String orderPage(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {
		String orderSnParam = StringUtils.trimToNull(request.getParameter("order_sn"));
		Long orderSn = Long.parseLong(orderSnParam);
		PanoOrderModel order = orderService.getOrderDetail(orderSn);
		map.put("order", order);
		try {
			Double percentValue = order.getPercentValue();
			map.put("dueStage1Money", (int) (order.getSumMoney().floatValue() * percentValue * 100) / 100.0);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/member/order/order_detail";
	}
	/**
	 * 取消订单
	 * *
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/cancel")
	public String delete(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {
		String orderSnParam = StringUtils.trimToNull(request.getParameter("order_sn"));
		Long orderSn = Long.parseLong(orderSnParam);
		PanoOrderModel order = orderService.getOrderDetail(orderSn);
		PanoOrderModel childrenOrder = order.getChildrenOrder();
		deleteOrder(childrenOrder);
		deleteOrder(order);
		return redirect("/order/orderspage");
	}
	/**
	 * 
	 * *
	 * @param order
	 */
	private void deleteOrder(PanoOrderModel order) {
		if(order != null){
			List<PanoOrderPackageModel> childrenOrderPackages = order.getOrderPackages();
			for (PanoOrderPackageModel panoOrderPackageModel : childrenOrderPackages) {
				List<PanoOrderPackageDetailModel> childrenOrderDetails = panoOrderPackageModel.getDetails();
				for (PanoOrderPackageDetailModel panoOrderPackageDetailModel : childrenOrderDetails) {
					panoOrderPackageDetailService.delete(panoOrderPackageDetailModel);
				}
				panoOrderPackageService.delete(panoOrderPackageModel);
			}
		}
	}

	/**
	 * 连连支付异步通知
	 * @param req
	 * @param resp
	 * @param map
	 * @throws Exceptiono
	 */
	@RequestMapping(value = "/lianpaynotify")
	public void lianpayNotify(HttpServletRequest req, HttpServletResponse resp, ModelMap map) throws Exception {
		resp.setCharacterEncoding("UTF-8");
		System.out.println("进入支付异步通知数据接收处理");
		RetBean retBean = new RetBean();
		String reqStr = YinTongUtil.readReqStr(req);
		logger.debug(reqStr);
		if (YinTongUtil.isnull(reqStr)) {
			retBean.setRet_code("9999");
			retBean.setRet_msg("交易失败");
			resp.getWriter().write(JSON.toJSONString(retBean));
			resp.getWriter().flush();
			return;
		}
		System.out.println("接收支付异步通知数据：【" + reqStr + "】");
		PayDataBean payDataBean = JSON.parseObject(reqStr, PayDataBean.class);
		String lianOderNum = payDataBean.getOid_paybill();
		String payDateStr = payDataBean.getDt_order();
		Date payDate = getPayTime(payDateStr);
		int transPlatformType = 0;
		try {
			//
			String oidPartner = payDataBean.getOid_partner();
			String pubKey = "";
			String md5Key = "";
			if(LianAuthPayConfig.OID_PARTNER.equals(oidPartner)){
				transPlatformType = 1;
				pubKey = LianAuthPayConfig.PUB_KEY;
				md5Key = LianAuthPayConfig.MD5_KEY;
			} else if(LianQuickPayConfig.OID_PARTNER.equals(oidPartner)){
				transPlatformType = 3;
				pubKey = LianQuickPayConfig.PUB_KEY;
				md5Key = LianQuickPayConfig.MD5_KEY;
			}
			if (!YinTongUtil.checkSign(reqStr, pubKey, md5Key)) {
				retBean.setRet_code("9999");
				retBean.setRet_msg("交易失败");
				resp.getWriter().write(JSON.toJSONString(retBean));
				resp.getWriter().flush();
				System.out.println("支付异步通知验签失败");
				return;
			}
		} catch (Exception e) {
			System.out.println("异步通知报文解析异常：" + e);
			retBean.setRet_code("9999");
			retBean.setRet_msg("交易失败");
			resp.getWriter().write(JSON.toJSONString(retBean));
			resp.getWriter().flush();
			return;
		}
		// 解析异步通知对象
		try {
			if ("SUCCESS".equals(payDataBean.getResult_pay())) {
				BigDecimal payAmount = new BigDecimal(payDataBean.getMoney_order());
				long orderId = Long.parseLong(payDataBean.getNo_order());
				String orderNum = payDataBean.getNo_order();
				PanoOrderModel orderModel = orderService.getOrderByNum(orderNum);
				if(orderModel == null){
					return;
				}
				if (payAmount.compareTo(orderModel.getPayMoney()) != 0) {
					throw new RuntimeException("支付金额不对");
				}
				if (orderModel.getStatus().compareTo(2) == 0) {
					retBean.setRet_code("0000");
					retBean.setRet_msg("交易成功");
					resp.getWriter().write(JSON.toJSONString(retBean));
					resp.getWriter().flush();
					return;
				}
				updateOrderWhenNotify(payDate, orderModel);
				//保存交易记录
				PanoOrderTransModel orderTransModel = new PanoOrderTransModel();
				orderTransModel.setOrderId(TCUtil.sv(orderId));
				orderTransModel.setTransDate(payDate);
				orderTransModel.setTransType("001");
				orderTransModel.setTransPlatformType(transPlatformType);
				orderTransModel.setTransMoney(payAmount);
				orderTransModel.setTransId(lianOderNum);
				orderTransModel.setTransStatus("SUCCESS");
				panoOrderTransService.insert(orderTransModel);
				//标记优惠券使用
				updateCouponItem2Use(orderModel.getSn());
			}
			retBean.setRet_code("0000");
			retBean.setRet_msg("交易成功");
			resp.getWriter().write(JSON.toJSONString(retBean));
			resp.getWriter().flush();
			System.out.println("支付异步通知数据接收处理成功");
		} catch (Exception e) {
			System.out.println(ExceptionUtils.getStackTrace(e));
			retBean.setRet_code("9999");
			retBean.setRet_msg(e.getMessage());
			resp.getWriter().write(JSON.toJSONString(retBean));
			resp.getWriter().flush();
		}
		return;
	}
	/**
	 * 获取支付时间
	 * *
	 * @param payDateStr
	 * @return
	 * @throws ParseException
	 */
	private Date getPayTime(String payDateStr) throws ParseException {
		try {
			DateFormat df = new SimpleDateFormat("yyyyMMddHHssmm");
			Date payDate = df.parse(payDateStr);
			return payDate;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new Date();
	}
	/**
	 * 
	 * *
	 * @param payDate 
	 * @param orderModel
	 */
	private void updateOrderWhenNotify(Date payDate, PanoOrderModel orderModel) {
		orderModel.setPayTime(payDate);
		orderModel.setStatus(2);
		Long parentOrderSn = orderModel.getParentOrderSn();
		if(parentOrderSn != -1){
			PanoOrderModel parentOrder = orderService.getBySn(parentOrderSn);
			parentOrder.setLeftMoney(new BigDecimal(0));
			parentOrder.setLeftPay(0);
			orderService.update(parentOrder);
		}
		orderService.update(orderModel);
	}
	/**
	 * 连连支付同步回调
	 * @param request
	 * @param response
	 * @param map
	 * @throws Exception
	 */
	@RequestMapping(value = "/lianpayreturn")
	public void lianpayReturn(HttpServletRequest request,HttpServletResponse response, ModelMap map) throws Exception {
		String resDataStr = request.getParameter("res_data");
		if(StringUtils.isEmpty(resDataStr)){
			response.sendRedirect("/order/orderspage");
			return;
		}
		if (!YinTongUtil.checkSign(resDataStr, LianAuthPayConfig.PUB_KEY, LianAuthPayConfig.MD5_KEY)) {

		}
		JSONObject resData = JSON.parseObject(resDataStr);
		String resultPay = resData.getString("result_pay");
		String moneyOrder = resData.getString("money_order");
		String noOrder = resData.getString("no_order");
		PayDataBean payDataBean = JSON.parseObject(resDataStr, PayDataBean.class);
		String lianOrderNum = payDataBean.getOid_paybill();
		int transPlatformType = 0;
		String oidPartner = payDataBean.getOid_partner();
		String payDateStr = payDataBean.getDt_order();
		Date payDate = getPayTime(payDateStr);
		if(LianAuthPayConfig.OID_PARTNER.equals(oidPartner)){
			transPlatformType = 1;
		} else if(LianQuickPayConfig.OID_PARTNER.equals(oidPartner)){
			transPlatformType = 3;
		}
		String orderSn = "";
		if ("SUCCESS".equals(resultPay)) {
			BigDecimal payAmount = new BigDecimal(moneyOrder);
			PanoOrderModel orderModel = orderService.getOrderByNum(noOrder);
			if (payAmount.compareTo(orderModel.getPayMoney()) != 0) {
				throw new RuntimeException("支付金额不对");
			} else {
				if (orderModel.getStatus().compareTo(1) == 0) {
					updateOrderWhenNotify(payDate, orderModel);
					PanoOrderTransModel orderTransModel = new PanoOrderTransModel();
					orderTransModel.setOrderId(noOrder);
					orderTransModel.setTransDate(payDate);
					orderTransModel.setTransType("001");
					orderTransModel.setTransPlatformType(transPlatformType);
					orderTransModel.setTransMoney(payAmount);
					orderTransModel.setTransId(lianOrderNum);
					orderTransModel.setTransStatus(resultPay);
					panoOrderTransService.insert(orderTransModel);
				}
			}
			if(orderModel != null){
				orderSn = EncryptUtil.encode(orderModel.getSn());
			}
		}
		response.sendRedirect("/order/pay/complete?orderSn=" + orderSn);
	}

	/**
	 * 微信异步通知
	 * @param request
	 * @param response
	 * @param map
	 * @throws Exception
	 */
	@RequestMapping(value = "/wxpaynotify")
	public void wxpayNotify(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {
		try {
			String responseString = IOUtils.toString(request.getInputStream(), request.getCharacterEncoding());
			XStream xstream = new XStream(new DomDriver());
			xstream.processAnnotations(ScanPayResData.class);
			ScanPayResData resData = (ScanPayResData) xstream.fromXML(responseString);
			String payType = resData.getAttach().split(",")[0];
			Configure configure = new Configure();
			if ("WX_H5".equals(payType)) {
				configure.setAppID(WxPayConfig.APPID);
				configure.setMchID(WxPayConfig.MCHID);
				configure.setKey(WxPayConfig.MCHKEY);
			} else {
				throw new RuntimeException("未知支付类型");
			}

			resData = WXPay.payAsyncNotify(responseString, configure);

			if (!resData.getIsSuccess())
				throw new RuntimeException(resData.getWorkedMsg());

			PanoOrderModel orderModel = orderService.getOrderByNum(resData.getOut_trade_no());
			String payAmount = PayUtils.convertFen2Yuan(new BigDecimal(resData.getTotal_fee()));
			/*
			if (!payAmount.equals(TCUtil.sv(orderModel.getPayMoney()))) {
				throw new RuntimeException("支付金额不对");
			}*/
			if (orderModel.getStatus().compareTo(2) == 0) {
				throw new RuntimeException("订单已经支付");
			}
			String payTimeStr = resData.getTime_end();
			Date payDate = getPayTime(payTimeStr);
			updateOrderWhenNotify(payDate, orderModel);
			PanoOrderTransModel orderTransModel = new PanoOrderTransModel();
			orderTransModel.setOrderId(resData.getOut_trade_no());
			orderTransModel.setTransDate(payDate);
			orderTransModel.setTransType("001");
			orderTransModel.setTransPlatformType(1);
			orderTransModel.setTransMoney(new BigDecimal(payAmount));
			orderTransModel.setTransId(resData.getTransaction_id());
			orderTransModel.setTransStatus("SUCCESS");
			panoOrderTransService.insert(orderTransModel);
			//优惠券使用
			updateCouponItem2Use(orderModel.getSn());
			response.getWriter()
					.write("<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>");
		} catch (Exception e) {
			logger.debug(ExceptionUtils.getStackTrace(e));
			response.getWriter().write("failure");
			return;
		} finally {

		}
	}
	/**
	 * 微信异步通知
	 * @param request
	 * @param response
	 * @param map
	 * @throws Exception
	 */
	@RequestMapping(value = "/wxpayreturn")
	public String wxpayReturn(String orderSn, HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {
		return redirect("/order/pay/complete?orderSn=" + orderSn);
	}
	/**
	 * 
	 * *
	 * @param orderSn
	 */
	public void updateCouponItem2Use(long orderSn){
		PanoOrderCouponItemModel couponItemModel = panoOrderCouponItemService.getByOrderSn(orderSn);
		if(couponItemModel != null){
			couponItemModel.setCodeStatus(1);
			panoOrderCouponItemService.update(couponItemModel);
		}
	}
	
	/**
	 * 
	 * *
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value = "pay/complete", method = RequestMethod.GET)
	public String payComplete(String orderSn, ModelMap modelMap) throws Exception{
		if(StringUtils.isNotEmpty(orderSn)){
			long orderSnD = EncryptUtil.decode(orderSn);
			PanoOrderModel order = orderService.getBySn(orderSnD);
			String orderNum = order.getOrderNum();
			if(order != null){
				Long parentOrderSn = order.getParentOrderSn();
				if(parentOrderSn != -1){
					order = orderService.getOrderDetail(parentOrderSn);
					orderNum = order.getOrderNum();
				}
				modelMap.put("order", order);
			}
			Long addressSn = order.getAddressSn();
			PanoUserReceiveAddressModel address = receiveAddressService.getBySn(addressSn);
			if(address != null){
				modelMap.put("address", address);
			}
			modelMap.put("orderNum", orderNum);
		}
		return "/member/order/pay_complete";
	}

	/**
	 * 获取远程客户端IP
	 * @param httpRequest
	 * @return
	 */
	public static String getRemoteIp(HttpServletRequest httpRequest) {
		String remoteAddr = null;
		String xForwardedFor = httpRequest.getHeader("x-forwarded-for");
		if (StringUtils.isEmpty(xForwardedFor)
				|| xForwardedFor.split(",").length == 0) {
			remoteAddr = httpRequest.getRemoteAddr();
		} else {
			remoteAddr = StringUtils.trim(xForwardedFor.split(",")[0]);
		}
		return remoteAddr;
	}
	
	public static String generateOrderNum() {
		return new SimpleDateFormat("yyyyMMDDHHmmss").format(new Date())+RandomStringUtils.randomNumeric(4);
	}

}
