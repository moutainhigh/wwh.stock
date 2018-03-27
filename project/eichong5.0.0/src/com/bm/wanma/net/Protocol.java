package com.bm.wanma.net;

/**
 *  接口地址
 */
public class Protocol {
	public static final String MSG = "msg";
	public static final String DATA = "data";
	public static final String CODE = "code";
	public static final String DATABASE_NAME = "eichong.db";
	public static final int dbNumer = 3;
	
	/**李军旺     */
//	/* */public static final String SERVER_ADDRESS = "http://yfbcs.api.eichong.com/api";
//	public static final String SERVER_ADDRESS_HTML = "http://yfbcs.html.eichong.com/html";//html外网地址
//	public static final String HOST = "yfbcs.ep.eichong.com"; 
//	public static final int PORT = 8001;
	 
	/** 开发地址内网      */ 
	//public static final String SERVER_ADDRESS = "http://10.9.3.116:80/api";//胡飞地址
//	/**/public static final String SERVER_ADDRESS = "http://10.9.2.109/api";//109内网地址
//	public static final String SERVER_ADDRESS_HTML = "http://10.9.2.109/html";//html
//	public static final String HOST = "10.9.2.115"; 
//	public static final int PORT = 8001;     
	
	/**测试域名地址  */
	/*    public static final String SERVER_ADDRESS = "http://cs.ep.eichong.com:88/api";//外网地址
	public static final String SERVER_ADDRESS_HTML = "http://cs.ep.eichong.com:84/html";//html外网地址
	public static final String HOST = "cs.ep.eichong.com"; 
	public static final int PORT = 8008;*/ 
	/**开发地址映射外网 新账户  */
//	public static final String SERVER_ADDRESS = "http://cs.ep.eichong.com:2381/api";//外网地址
//	public static final String SERVER_ADDRESS_HTML = "http://cs.ep.eichong.com:2382/html/";//html外网地址
//	public static final String HOST = "cs.ep.eichong.com"; 
//	public static final int PORT = 23301;   
	/**测试域名地址 新账户  */
	/*    */public static final String SERVER_ADDRESS = "http://cs.ep.eichong.com:2482/api";//外网地址
	public static final String SERVER_ADDRESS_HTML = SERVER_ADDRESS;//html外网地址
	public static final String HOST = "cs.ep.eichong.com"; 
	public static final int PORT = 24401; 
	
	/**开发地址映射外网    */
	/* */
//	public static final String SERVER_ADDRESS = "http://cs.ep.eichong.com:70/api";//外网地址
//	public static final String SERVER_ADDRESS = "http://10.9.2.231/api";//外网地址
//	public static final String SERVER_ADDRESS_HTML = "http://cs.ep.eichong.com:2381/api";//html外网地址
//	public static final String HOST = "cs.ep.eichong.com"; 
//	public static final int PORT = 8007;  
	
//	/* */public static final String SERVER_ADDRESS = "http://10.8.3.192:8080/api";//外网地址
//	public static final String SERVER_ADDRESS_HTML = "http://cs.ep.eichong.com:70/html";//html外网地址
//	public static final String HOST = "cs.ep.eichong.com"; 
//	public static final int PORT = 8007;  

	/* public static final String SERVER_ADDRESS = "http://101.69.243.94:83/api";//外网地址
	public static final String SERVER_ADDRESS_HTML = "http://101.69.243.94:73/html";//html外网地址
	public static final String HOST = "101.69.243.94"; 
	public static final int PORT = 8003;  */
	/**测试地址     */
/*	public static final String SERVER_ADDRESS = "http://115.236.3.66:88/api";
	public static final String SERVER_ADDRESS_HTML = "http://115.236.3.66:88/html";//html外网地址
	public static final String HOST = "115.236.3.66"; 
	public static final int PORT = 8008; */
	 
	/**阿里云    */
/*	public static final String SERVER_ADDRESS = "http://139.196.235.28/api";
	public static final String SERVER_ADDRESS_HTML = "http://139.224.67.215/html";//html外网地址
	public static final String HOST = "124.42.117.53"; 
	public static final int PORT = 8001; */
	
	/** IES开发测试地址 */
	/*public static final String SERVER_ADDRESS = "http://115.236.3.66:76/api";
	public static final String SERVER_ADDRESS_HTML = "http://115.236.3.66:76/html";
	public static final String HOST = "115.236.3.66";    
	public static final int PORT = 8100;*/
	
	/** 预发布地址 */         
	/*public static final String SERVER_ADDRESS = "http://10.9.2.102/api";
	public static final String SERVER_ADDRESS_HTML = "http://10.9.2.102/html";
	public static final String HOST = "10.9.2.102"; 
	public static final int PORT = 8001; */

	/** 演示环境 */ 
	/* public static final String SERVER_ADDRESS = "http://115.236.3.66:99/api";
	 public static final String SERVER_ADDRESS_HTML = "http://115.236.3.66:99/html/aichong/map_index.html";//html
	 public static final String HOST = "115.236.3.66";
	 public static final int PORT = 8109;*/
	

	 /**正式环境--app上线*/
//	 public static final String SERVER_ADDRESS = "http://m.api.eichong.com/api";
//	 public static final String SERVER_ADDRESS_HTML = "http://html.eichong.com/html";
//	 public static final String HOST = "link.eichong.com";
//	 public static final int PORT = 8001; 
	    
	  
	/** 获取短信验证码 */
	//public static final String GET_AUTH_CODE = SERVER_ADDRESS + "/app/common/getAuthCode.do";
	public static final String GET_AUTH_CODE = SERVER_ADDRESS + "/app/auth/getAuthCode.do";
	/** 电站、电桩查找-列表 */
	public static final String GET_STATION_ELECTRIC_LIST = SERVER_ADDRESS + "/app/electricPileList/getElectricPileListN.do";
	/** 电站、电桩查找-地图 */   
	public static final String GET_MAP_LIST = SERVER_ADDRESS + "/app/seamap/map.do";
	/** 获取市级充电点聚合*/   
	public static final String GET_MARKER_CITY = SERVER_ADDRESS + "/app/electricPileMap/getCtyPoints.do";
	/**获取省级充电点聚合*/   
	public static final String GET_MARKER_PROVINCE = SERVER_ADDRESS + "/app/electricPileMap/getProPoints.do";
	/** 电站、电桩--锚点 信息 */
	public static final String GET_ANCHOR_SUMMARY = SERVER_ADDRESS + "/app/electricPileMap/getAnchorSummary.do";
	/** 检查手机是否已注册 */
	public static final String CHECK_PHONE = SERVER_ADDRESS + "/app/user/checkphone.do"; 
	/** 登录 */
	public static final String TO_LOGIN = SERVER_ADDRESS + "/app/user/login.do"; 
	/** 判断验证码是否正确 */
	public static final String CHECK_CODE = SERVER_ADDRESS + "/app/user/checkAuthCode.do";
	/** 注册 */
	public static final String TO_REGIST = SERVER_ADDRESS + "/app/user/regist.do";
	/** 找回密码 */
	public static final String RESET_PWD = SERVER_ADDRESS + "/app/user/resetPasswrod.do";
	/** 修改密码 */
	public static final String MODIFY_PWD = SERVER_ADDRESS + "/app/user/modPassword.do";
	/** 检查支付密码 */
	public static final String CHECK_PAY_PWD = SERVER_ADDRESS + "/app/user/checkPayPwd.do";
	/** 设置支付密码 */
	public static final String SET_PAY_PWD = SERVER_ADDRESS + "/app/user/setPayPwd.do";
	/** 修改支付密码 */
	public static final String MODIFY_PAY_PWD = SERVER_ADDRESS + "/app/user/modPayPwd.do";
	/** 获取api通用token */
	public static final String GET_API_TOKEN = SERVER_ADDRESS + "/app/common/getToken.do";
	/** 获取版本信息（版本更新） */
	public static final String GET_APP_VERSION_INFO = SERVER_ADDRESS + "/app/other/getVersionInfo.do";
	/** 注销退出账号 */
	public static final String TO_LOGOUT = SERVER_ADDRESS + "/app/user/logout.do";
	/** 电桩详情 */
	public static final String POWER_Pile_DETAIL = SERVER_ADDRESS + "/app/powerStationDetail/getPointDetail.do";
	/** 电桩评价添加*/
	public static final String COMMIT_PILE_COMMENT = SERVER_ADDRESS + "/app/epComment/insertEpCommnet.do";   
	/** 电桩评分添加 */
	public static final String COMMIT_PILE_STAR = SERVER_ADDRESS+ "/app/epStar/insertEpStar.do";
	/** 电站详情 */
	public static final String POWER_STATION_DETAIL = SERVER_ADDRESS + "/app/powerStationDetail/getPowerStationDetail.do";
	/** 收藏电站，电桩 */
	public static final String COLLECT_STATION_PILE = SERVER_ADDRESS + "/app/usercollect/userFavorites.do";
	/** 删除我的收藏 */
	public static final String REMOVE_COLLECTED = SERVER_ADDRESS + "/app/favorite/removeFavorite.do";
	/**电站评价列表（对所属桩评论的汇总） */
	public static final String GET_STATION_COMMENT = SERVER_ADDRESS + "/app/psComment/findPsComments.do";
	/**获取电桩评价列表 */
	public static final String GET_PILE_COMMENT = SERVER_ADDRESS + "/app/epComment/findEpComments.do";
	/** 获取设备保修类型 */
	public static final String GET_EQUIPMENT_TYPE = SERVER_ADDRESS + "/app/paraconfig/findConfigContentList.do";
	/** 添加设备报修 */
	public static final String COMMIT_EQUIPMENT = SERVER_ADDRESS + "/app/other/addTblEquipmentrepair.do";
	/** 获取用户信息 */
	public static final String GET_USER_INFO = SERVER_ADDRESS + "/app/user/getUserInfo.do";
	/** 修改用户信息 */
	public static final String MODIFY_USER_INFO = SERVER_ADDRESS + "/app/user/modifyUser.do";
	/** 降地锁 */
	public static final String DOWN_PARKLOCK = SERVER_ADDRESS + "/app/net/downParkLock.do";
	/** LED开关 */
	public static final String LED = SERVER_ADDRESS + "/app/net/ledControl.do";
	/** 账号余额 */
	public static final String BANLANCE = SERVER_ADDRESS + "/app/user/getUserAB.do";
	/** aliPay */ 
	public static final String AliPayURL = SERVER_ADDRESS+"/app/pay/aliSign.do";
	/** weixin */ 
	public static final String WeiXinPayURL = SERVER_ADDRESS+"/app/pay/wxTempOrder.do";
	/***品牌*/
	public static final String FIND_CAR_INFO = SERVER_ADDRESS + "/app/paraconfig/findCarinfoList.do";
	/**车型号 */
	public static final String FIND_PARACONFIG_LIST = SERVER_ADDRESS + "/app/paraconfig/findParaconfigList.do";
	/**我的收藏 */
	public static final String GET_MYCOLLECT_LIST = SERVER_ADDRESS + "/app/favorite/getFavoriteListN.do";
	/**系统消息*/
	public static final String GET_MYNEWS_SYSTEM_LIST = SERVER_ADDRESS +"/app/usermessge/mylist.do";
	/**系统消息详情*/
	public static final String GET_MYNEWS_SYSTEM_DETAIL = SERVER_ADDRESS +"/app/usemessage/myMessageContent.do";
	/**我的反馈*/
	public static final String GET_MYNEWS_FEEDBACK_LIST = SERVER_ADDRESS +"/app/other/getMyFB.do";
	/**活动列表页--动态*/
	public static final String GET_MYDYNAMIC_LIST = SERVER_ADDRESS +"/app/advert/dynamicList.do";
	/**意见反馈*/
	public static final String COMMIT_MYFEEDBACK = SERVER_ADDRESS +"/app/other/addTblFeedBack.do";
	/**急救电话*/
	public static final String GET_EMERGENCY_CALL = SERVER_ADDRESS +"/app/rescue/list.do";
	/**车俩维修*/
	public static final String GET_CAR_REPAIR= SERVER_ADDRESS +"/app/carGarage/list.do";
	/** 分享电桩*/
	public static final String SHARE_PILE= SERVER_ADDRESS +"/app/publishEp/add.do";
	/** 获取二维码信息*/
	public static final String GET_SCAN_INFO= SERVER_ADDRESS +"/app/electricPileDetail/selectPileInfo.do";
	/** 获取数字码信息*/
	public static final String GET_ZNum_PILE_INFO= SERVER_ADDRESS +"/app/electricPileDetail/zNumSelPileInfo.do";
	/** 获取我的充电卡*/
	public static final String GET_MY_CARDLIST= SERVER_ADDRESS +"/app/chargeCard/cardList.do";
	/** 获取我的充电卡申请列表*/
	public static final String GET_MY_CARD_APPLYLIST= SERVER_ADDRESS +"/app/chargeCard/applyList.do";
	/** 充电卡挂失*/
	public static final String REPORTLOSS_CARD= SERVER_ADDRESS +"/app/chargeCard/reportLoss.do";
	/** 充电卡申请*/
	public static final String APPLY_MY_CARD= SERVER_ADDRESS +"/app/chargeCard/applyCard.do";
	/** 我的账单 */
	public static final String MY_BILL = SERVER_ADDRESS + "/app/user/getMtBills.do";
	/** 月账单详情 */
	public static final String MY_MOTH_BILL = SERVER_ADDRESS + "/app/user/getMtBillsDetail.do";
	/**可开发票列表*/
	public static final String MY_ENABLE_INVOICE = SERVER_ADDRESS + "/app/invoice/enableInvoiceList.do";
	/**发票配置*/
	public static final String MY_INVOICE_CONFIG = SERVER_ADDRESS + "/app/invoice/invoiceConfig.do";
	/**开发票*/
	public static final String COMMIT_INVOICE = SERVER_ADDRESS + "/app/invoice/saveInvoice.do";
	/**个人开过的发票记录*/
	public static final String MY_INVOICE_RECORD = SERVER_ADDRESS + "/app/invoice/invoicedList.do";
	/**发票详情*/
	public static final String MY_INVOICE_RECORD_DETAIL = SERVER_ADDRESS + "/app/invoice/invoiceDetail.do";
	/**发票所包含的消费记录*/
	public static final String MY_INVOICE_RECORD_PUR = SERVER_ADDRESS + "/app/invoice/invoicePurList.do";
	/**检查用户是否看过开票流程*/
	public static final String GET_INVOICE_CHECK = SERVER_ADDRESS + "/app/invoice/getInvoiceCheck.do";
	/**添加用户看过开票流程标志*/
	public static final String ADD_INVOICE_CHECK = SERVER_ADDRESS + "/app/invoice/addInvoiceCheck.do";
	/**获取我的优惠信息（4.0.0）*/
	public static final String GET_COUPON_INFO = SERVER_ADDRESS + "/app/user/myCpInfo.do";
	/**获取我的优惠数量（4.0.0）*/
	public static final String GET_COUPON_SIZE = SERVER_ADDRESS + "/app/coupon/count.do";
	/**获取优惠券列表*/
	public static final String GET_COUPON_LIST = SERVER_ADDRESS + "/app/coupon/list.do";
	/**兑换优惠券*/
	public static final String COUPON_EXCHANGE = SERVER_ADDRESS + "/app/coupon/exchange.do";
	/**广告内容*/
	public static final String ADVERTISEMENT = SERVER_ADDRESS + "/app/advert/advertList.do";
	/**广告有效期*/
	public static final String ADVERTISEMENT_VALIDITY = SERVER_ADDRESS + "/app/coupon/exchange.do";
	/**我的充电*/
	public static final String INDENT_PARTICULARS =SERVER_ADDRESS + "/app/chargeShow/chargeOrderList.do";
	/**充值记录*/
	public static final String RECHARGERECOR =SERVER_ADDRESS +  "/app/user/getCzDetail.do";
	/** 充电订单详情 */
	public static final String MYCHARGE_ORDER_DETAILS = SERVER_ADDRESS + "/app/chargeShow/chargeOrderDetail.do";
	/** 图片轮播 */
	public static final String IMAGE_CAROUSEL = SERVER_ADDRESS + "/app/vfour/banner.do";
	/** 文字轮播 */
	public static final String TEXT_CAROUSEL = SERVER_ADDRESS + "/app/vfour/rollMessage.do";
	/** 功能按钮 */
	public static final String FUNCTION_BUTTON = SERVER_ADDRESS + "/app/vfour/button.do";
	/** 资讯 */
	public static final String INFORMATION_LIST = SERVER_ADDRESS + "/app/vfour/information.do";
	/** 故障 */
	public static final String FAULT_LIST = SERVER_ADDRESS + "/app/vfour/messageInfo.do";
	
	public static final String GET_SEARCH_LIST = SERVER_ADDRESS + "/app/seamap/search.do";
	/** 每日领取 */
	public static final String EVERY_DAY_GET_INTEGRAL = SERVER_ADDRESS + "/app/point/dailysign.do";
	/** 签到列表 */
	public static final String SIGN_IN_GET = SERVER_ADDRESS + "/app/point/signablelist.do";
	/** 积分列表 */
	public static final String INTEGRAL_DETAILS_LIST = SERVER_ADDRESS + "/app/point/getUserPoint.do";
	/** 个人积分 */
	public static final String INTEGRAL_PERSONAGE = SERVER_ADDRESS + "/app/point/getuserpointByUserId.do";
	/** 充值充电送积分比例 */
	public static final String INTEGRAL_PROPORTION = SERVER_ADDRESS + "/app/point/getpointpresentrate.do";
	/** 充值送积分 */
	public static final String INTEGRAL_RECHARGE = SERVER_ADDRESS + "/app/point/rechargepresentpoint.do";
	/** 充电送积分 */
	public static final String INTEGRAL_CHARGE = SERVER_ADDRESS + "/app/point/chargingpresentpoint.do";
	/** 分享送积分 */
	public static final String SHARE_INTEGRAL = SERVER_ADDRESS + "/app/point/getpointbyshare.do";
	/** 充值送积分 */
	public static final String INTEGRAL_EVENT = SERVER_ADDRESS + "/app/point/findactivitylist.do";
	//以下为html 地址
	/**活动详情页*/
	public static final String GET_MYDYNAMIC_DETAIL = SERVER_ADDRESS_HTML +"/html/news/detail.html";
	/**帮助向导页*/
	public static final String INSTRUCTION = SERVER_ADDRESS_HTML +"/html/help/index.html";
	/**费率详情*/
	public static final String ABOUT_PRICE = SERVER_ADDRESS_HTML +"/html/rateinfo/detail.html";
	/**申请建桩*/
	public static final String ApplyBuilder = SERVER_ADDRESS_HTML +"/aichong/applyBuilder.html";
	
	public static final String CHARGESTATE = SERVER_ADDRESS_HTML +"/html/statusDescription/statusDescription.html";
	/**积分规则*/
	public static final String INTEGRALRULES = SERVER_ADDRESS_HTML +"/html/integralRules/integralRules.html";
}
