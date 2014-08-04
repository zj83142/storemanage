package com.zj.storemanag.service;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;

import com.zj.storemanag.bean.CVersion;

public class AsyncService {

	private static AsyncService asyncService = null;

	private AsyncService() {

	}

	public static AsyncService getInstance() {
		if (asyncService == null) {
			asyncService = new AsyncService();
		}
		return asyncService;
	}

	/** 登录 参数 ：handler ， flag username pwd */
	public class Login extends BaseAsync {
		@Override
		protected Object doInBackground(Object... params) {
			handler = (Handler) params[0];
			what = (Integer) params[1];
			return new LoginService().login((String) params[2], "UserLogin",
					(String) params[3], (String) params[4]);
		}
	}
	
	/** 版本更新 参数： handler ， flag, 接口名称, 当前版本号 */
	public class UpdateVersion extends BaseAsync {
		@Override
		protected Object doInBackground(Object... params) {
			handler = (Handler) params[0];
			what = (Integer) params[1];
			return new GetVersionService().getVersion((String) params[2],
					(String) params[3], (String) params[4], (String) params[5]);
		}
	}
	
	/** 下载最新版本 参数： handler ，flag, 版本CVersion ， ProgressDialog */
	public class DownloadVersion extends BaseAsync {

		@Override
		protected Object doInBackground(Object... params) {
			handler = (Handler) params[0];
			what = (Integer) params[1];
			return new DownLoadVersionService().downLoadVersion(
					(CVersion) params[2], (ProgressDialog) params[3]);
		}
	}

	/** 获取成本中心数据 */
	public class GetCodeTable extends BaseAsync {
		@Override
		protected Object doInBackground(Object... params) {
			handler = (Handler) params[0];
			what = (Integer) params[1];
			return new GetBaseDataService().getBaseData((String) params[2],
					"GetCodeTable", (String) params[4], (Context) params[3]);
		}
	}

	/** 获取基础数据 */
	public class GetBaseData extends BaseAsync {
		@Override
		protected Object doInBackground(Object... params) {
			handler = (Handler) params[0];
			what = (Integer) params[1];
			return new GetBaseDataService().getBaseData((String) params[2], "GetLgortsWerksUnit", (Context) params[3]);
		}
	}

	/** 获取RFID信息 */
	public class GetRFIDInfo extends BaseAsync {

		@Override
		protected Object doInBackground(Object... params) {
			handler = (Handler) params[0];
			what = (Integer) params[1];
			// handler, ParamsUtil.GETRFIDINFO, myApp.getUrl(),
			// myApp.getUser().getUserIdMD5(), rfid
			return new GetRFIDinfoService().getRFIDinfo((String) params[2],
					"GetRFIDinfo", (String) params[3], (String) params[4],
					(Context) params[5]);
		}
	}

	/** 获取日志信息 */
	public class GetLogList extends BaseAsync {
		@Override
		protected Object doInBackground(Object... params) {
			handler = (Handler) params[0];
			what = (Integer) params[1];
			return new GetLogListService().getLogList((String) params[2],
					"GetLogList", (String) params[3], (String) params[4],
					(String) params[5], (String) params[6], (String) params[7]);
		}
	}

	/**
	 * 获取采购订单GetPoOrder public GoodsList[] GetPoOrder(string userID, string
	 * order, ref BAPIEKKOL retHead) 输入参数：加密的用户ID、
	 * 订单号、引用参数：BAPIEKKOL(采购单表头和供应商名称) 输出结果：GoodsList[]集合 Context context
	 * */
	public class GetPoOrder extends BaseAsync {
		@Override
		protected Object doInBackground(Object... params) {
			handler = (Handler) params[0];
			what = (Integer) params[1];
			return new GetPoOrderService().getPoOrder((String) params[2],
					"GetPoOrder", (String) params[3], (String) params[4],
					(Context) params[5]);
		}
	}

	// /**初始化RFID*/
	// public class InitRFID extends BaseAsync {
	// @Override
	// protected Object doInBackground(Object... params) {
	// handler = (Handler) params[0];
	// what = (Integer) params[1];
	// return new InitRFIDService().initRFID((String) params[2],
	// "InitRFID", (String) params[3]);
	// }
	// }

	/** 入库 */
	public class Upload extends BaseAsync {
		@Override
		protected Object doInBackground(Object... params) {
			handler = (Handler) params[0];
			what = (Integer) params[1];
			return new UploadService().upload((String) params[2],
					"GOODSMVT_CREATE", (String) params[3], (String) params[4]);
		}
	}

	/** 获取物料卡片GetWLKP（卡片二、卡片四） */
	public class GetWLKP extends BaseAsync {
		@Override
		protected Object doInBackground(Object... params) {
			handler = (Handler) params[0];
			what = (Integer) params[1];
			return new GetCardService().getCard245((String) params[2],
					"GetWLKP", (String) params[3], (String) params[4],
					(String) params[5], (String) params[6], (String) params[7],
					(Context) params[8]);
		}
	}

	/** 获取物料卡片GetWLKP（卡片一、卡片三） */
	public class GetWLKP2 extends BaseAsync {
		@Override
		protected Object doInBackground(Object... params) {
			handler = (Handler) params[0];
			what = (Integer) params[1];
			return new GetCardService().getCard13((String) params[2],
					"GetWLKP2", (String) params[3], (String) params[4],
					(String) params[5], (String) params[6], (String) params[7],
					(Context) params[8]);
		}
	}

	// /**获取物料详情*/
	public class GetMaterialDetail extends BaseAsync {
		@Override
		protected Object doInBackground(Object... params) {
			handler = (Handler) params[0];
			what = (Integer) params[1];
			return new GetMaterialDetailService()
					.getMaterialDetail((String) params[2], "GetMaterialDetail",
							(String) params[3], (String) params[4],
							(Context) params[5]);
		}
	}

	/** 获取临时RFID集合 */
	public class GetTempRfidLs extends BaseAsync {
		@Override
		protected Object doInBackground(Object... params) {
			handler = (Handler) params[0];
			what = (Integer) params[1];
			return new GetTempRfidLsService().getTempRfidLs((String) params[2],
					"getTempRFIDsInfo", (String) params[3], (String) params[4],(String) params[5],
					(String) params[6],(String) params[7],(Context) params[8]);
		}
	}

	/** 更新RFID */
	public class UpdateRfID extends BaseAsync {
		@Override
		protected Object doInBackground(Object... params) {
			handler = (Handler) params[0];
			what = (Integer) params[1];
			return new UpdateRfIDService().updateRfID((String) params[2],
					"UpdateRfID", (String) params[3]);
		}
	}

	/** 临时RFID */
	public class TempRFID extends BaseAsync {
		@Override
		protected Object doInBackground(Object... params) {
			handler = (Handler) params[0];
			what = (Integer) params[1];
			return new TempRfIDService().tempRfID((String) params[2],
					"TmpRFID", (String) params[3], (String) params[4]);
		}
	}

	/**
	 * 获取维修订单 UserIDEncdoe：加密的用户ID；repairNo：维修订单，最大长度12位数字
	 * 
	 * @author zhoujing 2014-5-15 上午9:40:52
	 */
	public class GetPmOrder extends BaseAsync {
		@Override
		protected Object doInBackground(Object... params) {
			handler = (Handler) params[0];
			what = (Integer) params[1];
			return new GetPmOrderService().getPmOrder((String) params[2],
					"GetPmOrder", (String) params[3], (String) params[4],
					(Context) params[5]);
		}
	}

	/**
	 * 获取库存信息
	 * 
	 * @author zhoujing 2014-5-16 下午5:45:44
	 */
	public class GetKC extends BaseAsync {
		@Override
		protected Object doInBackground(Object... params) {
			handler = (Handler) params[0];
			what = (Integer) params[1];
			return new GetKCService().getKC((String) params[2], "GetKC",
					(String) params[3], (String) params[4], (String) params[5],
					(Context) params[6]);
		}
	}

	/**
	 * 获取项目库存信息
	 * 
	 * @author zhoujing 2014-5-16 下午5:45:44
	 */
	public class GetKC2 extends BaseAsync {
		@Override
		protected Object doInBackground(Object... params) {
			handler = (Handler) params[0];
			what = (Integer) params[1];
			return new GetKCService().getKC2((String) params[2], "GetKC2",
					(String) params[3], (String) params[4], (String) params[5],
					(Context) params[6]);
		}
	}

	/** 打印卡片 */
	public class PrintCard extends BaseAsync {
		@Override
		protected Object doInBackground(Object... params) {
			handler = (Handler) params[0];
			what = (Integer) params[1];
			return new PrintCardService().printCard((String) params[2],
					"PrintCard", (String) params[3], (String) params[4],
					(String) params[5]);
		}
	}

	/** 查询成本中心数据 */
	public class CostCenter extends BaseAsync {
		@Override
		protected Object doInBackground(Object... params) {
			// TODO Auto-generated method stub
			return super.doInBackground(params);
		}
	}

	public class GetCompCo extends BaseAsync {
		@Override
		protected Object doInBackground(Object... params) {
			handler = (Handler) params[0];
			what = (Integer) params[1];
			return new GetCompCoService().getCompCo((String) params[2],
					"GetCompCo", (String) params[3], (String) params[4]);
		}
	}

	public class Card245Ls extends BaseAsync {
		@Override
		protected Object doInBackground(Object... params) {
			handler = (Handler) params[0];
			what = (Integer) params[1];
			return new GetCardLsService().getCard245Ls((String) params[2],
					"GetGM_ITEM", (String) params[3], (String) params[4],
					(String) params[5], (String) params[6], (String) params[7],
					(String) params[8], (Context) params[9]);
		}
	}

	public class Card13Ls extends BaseAsync {
		@Override
		protected Object doInBackground(Object... params) {
			handler = (Handler) params[0];
			what = (Integer) params[1];
			return new GetCardLsService().getCard13Ls((String) params[2],
					"GetMaterials", (String) params[3], (String) params[4],
					(String) params[5], (String) params[6], (String) params[7],
					(Context) params[8]);
		}
	}

	public class GetMaterialImg extends BaseAsync {
		@Override
		protected Object doInBackground(Object... params) {
			handler = (Handler) params[0];
			what = (Integer) params[1];
			return new GetMaterialImgService().getMaterialImg(
					(String) params[2], "GetMaterialImage", (String) params[3],
					(String) params[4], (String) params[5], (String) params[6]);
		}
	}

}
