package com.zj.storemanag.commen;

public class ParamsUtil {
	
	/**显示设备列表界面*/
	public static final String showListMessage = "com.zj.broadcast.receiver.showlist.message";
	
	public static final String key = "37D3DCA3-C077-4644-85E8-C3E7612948E1";// 接口密钥
	
	public static int APP = 1;//0:测试版，1：正式版
	
//	public static String defaultUrl = "http://172.18.28.59:8088/Library/WebInitPage.aspx?Source=HGStockInterfacePage";
//	public static String defaultUrl = "http://192.168.23.1:8088/Library/WebInitPage.aspx?Source=HGStockInterfacePage";
//	public static String defaultIP = "172.18.96.82:8088";
//	public static String imgUrl = "http://172.18.96.82:8088/";
	public static String defaultIP = "223.4.97.56:9005";
	public static String imgUrl = "http://223.4.97.56:9005/";
	public static String defaulServiceName = "/Library/WebInitPage.aspx?Source=HGStockInterfacePage";
	
	public static final int update_list = 1 * 88;
	
	public static final int showKeyboard = 2 * 88;
	public static final int hideKeyboard = 3 * 88;
	
	/** 退出 */
	public static final int EXIT = 1 * 1000;

	/** pop选项 */
	public static final int POP_SELECT = 2 * 1000;

	/** 登录 */
	public static final int LOGIN = 3 * 1000;

	/** getCodeTable获取工厂库位信息 */
	public static final int GETBASEDATA = 4 * 1000;
	
	/** 初始化RFID */
	public static final int INIT_RFID = 5 * 1000;
	
	/**获取RFID信息*/
	public static final int GETRFIDINFO = 6 * 1000;
	
	/**获取操作日志集（集合）*/
	public static final int GETLOGLIST = 7 * 1000;
	
	/**获取采购订单*/
	public static final int GETPOORDER = 8 * 1000;
	
	/**上传数据*/
	public static final int UPLOAD= 9 * 1000;
	
	/**上传数据*/
	public static final int UPLOAD2= 9 * 10000;
	
	/**获取物料卡片GetWLKP（卡片二、卡片四）*/
	public static final int GET_CARD = 10 * 1000;
	
	/**获取物料详情*/
	public static final int GETMATERIALDETAIL = 11 * 1000;
	
	/**更新RFID*/
	public static final int UPDATERFID = 12 * 1000;
	
	/**获取临时Rfid集合*/
	public static final int GETTEMPRFIDLS = 13 * 1000;

	/**获取维修订单集合*/
	public static final int GETPMORDER = 14 * 1000;
	
	/**查询出入库凭证报表*/
	public static final int GETSTOREHISTORY= 15 * 1000;
	
	/**获取库存*/
	public static final int GETKC = 16 * 1000;
	
	/**打印卡片*/
	public static final int PRINT_CARD = 17 * 1000;
	
	/**获取物料卡片GetWLKP2（【旧】卡片一、卡片三）*/
	public static final int GETCARD13 = 18 * 1000;
	
	/**获取物料卡片GetWLKP 卡片（二）、卡片（四）和卡片（五）*/
	public static final int GETCARD245 = 19 * 1000;
	
	/**初始化基础数据*/
	public static final int INITBASICDATE = 20 * 1000;
	
	/**临时rfid*/
	public static final int TEMPRFID = 21 * 1000;
	/**临时rfid*/
	public static final int IsTEMPRFID = 22 * 1000;
	/**出入库查询*/
	public static final int OUTINSTORE = 23 * 1000;
	/**获取公司代码*/
	public static final int GETCOMPCO = 24 * 1000;
	
	/**查询项目库存*/
	public static final int GETKC2 = 25 * 1000;
	
	/** 获取卡片13集合*/
	public static final int CARD13LS = 26 * 1000;
	
	/**获取卡片245集合*/
	public static final int CARD245LS = 27 * 1000;
	
	/**获取物料图片*/
	public static final int load_img = 28 * 1000;
	
	/**是否退出操作*/
	public static final int EXIT_OPERATION = 29 * 1000;
	
	/**检测版本更新*/
	public static final int CHECK_VERSION = 30 * 1000;
	
	/**下载新版本*/
	public static final int DOWNLOAD_NEW_VERSION = 31 * 1000;
	
	/**下载结果*/
	public static final int DOWNLOAD_RESULT = 32 * 1000;
	
	public static final int INSTALL = 33 * 1000;
	
	/**是否继续操作*/
	public static final int IS_GOON = 34 * 1000;
	
	/**查询nfc 对应的物料信息*/
	public static int QUERY_NFC = 1 * 123456;
	
	/** 外部备件入库 */
	public static final int external = 0;
	/** 内部备件入库 */
	public static final int interior = 1;
	/** 工技改处 */
	public static final int project = 2;
	/** 工技改处-分 */
	public static final int project_part = 3;
	/** 寄售备出-移库 */
	public static final int consign_midify = 4;
//	/** 寄售备出-成 */
//	public static final int consign_midify_complete = 5;
	/** 按维修订单 */
	public static final int maintenance_order = 5;
	/** 按成本中心 */
	public static final int cost_center = 6;
	/** 按客号发-内 */
	public static final int guest_no_inside = 7;
	/** 按客号发-外 */
	public static final int guest_no_outside = 8;
	/** 跨公司移库 */
	public static final int cross_company = 9;
	/** 同公司移库 */
	public static final int company = 10;
	/** 内部投料 */
	public static final int internal_feeding = 11;
	
	/**查看设备列表详情*/
	public static final int EQ_DETAIL = 111;
	/**设备列表数据变化*/
	public static final int EQ_CHANGE = 222;
	/**显示toast*/
	public static final int SHOWTOAST= 333;
	
	/**记忆用能标志*/
	public static final String flag_orderNum = "1";//采购订单
	public static final String flag_modifyOrder = "2";//维修订单
	public static final String flag_wbs = "3";//wbs元素
	public static final String flag_materail = "4";//物料号
	public static final String flag_center = "5";//成本中心
	public static final String flag_guest = "6";//客户号
	public static final String flag_supper = "7";//供应商
	public static final String flag_cw = "8";//仓位
	public static final String flag_proof_num = "9";//凭证号
	public static final String flag_proof_year = "10";//凭证年
	

}
