package com.bm.wanma.ui.activity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bm.wanma.R;
import com.bm.wanma.broadcast.BroadcastUtil;
import com.bm.wanma.dialog.CustomCommonDialog;
import com.bm.wanma.dialog.CustomCommonDialog.CustomDialogCallback;
import com.bm.wanma.dialog.CustomTipInsertGunDialog;
import com.bm.wanma.dialog.WalletWarningDialog;
import com.bm.wanma.entity.BanlanceBean;
import com.bm.wanma.entity.IntegralEventBean;
import com.bm.wanma.entity.IntegralProportionBean;
import com.bm.wanma.entity.ScanInfoBean;
import com.bm.wanma.net.GetDataPost;
import com.bm.wanma.net.Protocol;
import com.bm.wanma.popup.CustomTipAwaitPopuWindow;
import com.bm.wanma.popup.CustomTipAwaitPopuWindow.AwaitChange;
import com.bm.wanma.socket.SocketConstant;
import com.bm.wanma.socket.StreamUtil;
import com.bm.wanma.socket.TCPSocketManager;
import com.bm.wanma.utils.LogUtil;
import com.bm.wanma.utils.PreferencesUtil;
import com.bm.wanma.utils.Tools;
import com.umeng.analytics.MobclickAgent;

/**
 * @author cm 扫码后桩信息界面
 */
public class PileDetailActivity extends BaseActivity implements
		OnClickListener, ITcpCallBack, CustomDialogCallback ,AwaitChange{

	private ImageButton ib_back, ib_question;
	private TextView tv_name, tv_power, tv_electric, tv_price, tv_estimate,tv_pile_detail_await,tv_pile_detail_ac_tip,tv_precharge_money,tv_jifen;
	private TextView tv_park_price, tv_balance, tv_recharge;
	private Button bt_forty, bt_thirty, bt_twenty, bt_start_recharge;
	private EditText et_precharge;
	private ScanInfoBean bean;
	private int type,awaitType;
	private String balance;
	private String power, price, serprice;
	private TCPSocketManager mTcpSocketManager;
	private CustomCommonDialog customDialog;
	private CustomTipInsertGunDialog tipInsertGunDialog;
	private WalletWarningDialog mFinishChargeD;
	private String pileNum;
	private String zhuangtai="0";
	private byte headnum;
	private boolean isCloseTCP = true;
	private boolean isStartCharge = false;
//	private boolean isStartAwait = false;
	private LinearLayout ll_charge_switch,rl_jifen;
	private RelativeLayout rl_await_switch;
	private CustomTipAwaitPopuWindow popupWindow;
	private String pkUserId, provincecode, citycode;
	private String proportion="0";
	private String fixationproportion="0";
	private boolean isres = false;
	private String rtion;
	private static final int DECIMAL_DIGITS = 2;//小数的位数
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pile_detail);
		mPageName = "PileDetailActivity";
		CustomTipAwaitPopuWindow.setCouponChangeSize(this);
		type = getIntent().getIntExtra("type", 14);
		awaitType = getIntent().getIntExtra("awaitType", 0);
		
		bean = (ScanInfoBean) getIntent().getSerializableExtra("scanInfo");
		mTcpSocketManager = TCPSocketManager.getInstance(PileDetailActivity.this);
		mTcpSocketManager.setTcpCallback(this);
		if (isNetConnection()) {
			pkUserId = PreferencesUtil.getStringPreferences(
					PileDetailActivity.this, "pkUserinfo");
			GetDataPost.getInstance(this).getBalance(handler, pkUserId);
			
			provincecode = PreferencesUtil.getStringPreferences(this,"provincecode");
			citycode = PreferencesUtil.getStringPreferences(this, "citycode");
//			Toast.makeText(this, "  "+provincecode+"   "+citycode , 0).show();
			if (Tools.isEmptyString(provincecode)||Tools.isEmptyString(citycode)) {
				provincecode = "330000";
				citycode = "330100";
			}
			if (!Tools.isEmptyString(citycode)&&!Tools.isEmptyString(provincecode)) {		
			GetDataPost.getInstance(this).getIntegralProportion(handler, pkUserId,provincecode, citycode,"2");
			
			GetDataPost.getInstance(getActivity()).getIntegralEvent(handler, pkUserId, provincecode, citycode, "2");
			}
		}
		initView(bean);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(mContext);
		MobclickAgent.onPageStart(mPageName);
//		mTcpSocketManager.reopen();
if (isres) {	
		initBalance();
}
isres = true;
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause(); 
		MobclickAgent.onPause(mContext);
		MobclickAgent.onPageEnd(mPageName);
	}
	private void initBalance(){
		balance = PreferencesUtil.getStringPreferences(this,
				"usinAccountbalance");
		tv_balance.setText(String.format(
				getResources().getString(R.string.current_balance), balance));
		if (!Tools.isEmptyString(balance) && Float.valueOf(balance) >= 50) {
			et_precharge.setText("50");
			tv_precharge_money.setText("预充金额：50 元");
			bt_forty.setOnClickListener(this);
			bt_thirty.setOnClickListener(this);
			bt_twenty.setOnClickListener(this);
		} else if (!Tools.isEmptyString(balance) && Float.valueOf(balance) >= 40
				&& Float.valueOf(balance) < 50) {
			et_precharge.setText(balance);
			tv_precharge_money.setText("预充金额："+balance+" 元");
			bt_forty.setOnClickListener(this);
			bt_thirty.setOnClickListener(this);
			bt_twenty.setOnClickListener(this);
		} else if (!Tools.isEmptyString(balance) && Float.valueOf(balance) >= 30
				&& Float.valueOf(balance) < 40) {
			et_precharge.setText(balance);
			tv_precharge_money.setText("预充金额："+balance+" 元");
			bt_thirty.setOnClickListener(this);
			bt_twenty.setOnClickListener(this);
			bt_forty.setClickable(false);
		} else if (!Tools.isEmptyString(balance) && Float.valueOf(balance) >= 20
				&& Float.valueOf(balance) < 30) {
			et_precharge.setText(balance);
			tv_precharge_money.setText("预充金额："+balance+" 元");
			bt_twenty.setOnClickListener(this);
			bt_forty.setClickable(false);
			bt_thirty.setClickable(false);
		} else if (!Tools.isEmptyString(balance) && Float.valueOf(balance) < 20) {
			et_precharge.setText(balance);
			tv_precharge_money.setText("预充金额："+balance+" 元");
			bt_forty.setClickable(false);
			bt_thirty.setClickable(false);
			bt_twenty.setClickable(false);
		}
//		if (Tools.isEmptyString(et_precharge.getText().toString().trim())) {
//			et_precharge.setSelection(et_precharge.getText().toString().trim().length());
//		}
	}

	private void initView(ScanInfoBean v) {
		price = v.getCurrentRate();
		try {
			serprice = v.getSerPrice();			
		} catch (Exception e) {
		}
		// price = v.getpPrice();
		power = v.getElPiPowerSize();
		tv_pile_detail_await=  (TextView) findViewById(R.id.pile_detail_await);//等待充电
		tv_pile_detail_await.setOnClickListener(this);
		tv_pile_detail_ac_tip = (TextView) findViewById(R.id.pile_detail_ac_tip);
		ll_charge_switch = (LinearLayout) findViewById(R.id.charge_switch);
		rl_await_switch = (RelativeLayout) findViewById(R.id.await_switch);
		tv_precharge_money=  (TextView) findViewById(R.id.precharge_money);//等待充电
		
		rl_jifen = (LinearLayout) findViewById(R.id.rl_jifen);
		tv_jifen = (TextView) findViewById(R.id.jifen);
		
		ib_back = (ImageButton) findViewById(R.id.pile_detail_back);
		ib_back.setOnClickListener(this);
		ib_question = (ImageButton) findViewById(R.id.pile_detail_price_help_icon);
		ib_question.setOnClickListener(this);
		tv_name = (TextView) findViewById(R.id.pile_detail_name);
		tv_name.setText(v.getElpiElectricpilename() + "");
		tv_power = (TextView) findViewById(R.id.pile_detail_power);
		try {
			power = power.substring(0, power.length() - 2);
			tv_power.setText(power);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tv_electric = (TextView) findViewById(R.id.pile_detail_electric);
		tv_electric.setText(v.getoCurrent() + "");
		tv_price = (TextView) findViewById(R.id.pile_detail_price);
		if (!Tools.isEmptyString(serprice)) {
			BigDecimal bigDecimal = new BigDecimal(Double.parseDouble(price)+Double.parseDouble(serprice)).setScale(4,BigDecimal.ROUND_HALF_UP);
			tv_price.setText(bigDecimal.toString() + "");
		}else {
			tv_price.setText(price + "");
		}
		tv_park_price = (TextView) findViewById(R.id.pile_detail_park_price);
		tv_estimate = (TextView) findViewById(R.id.pile_detail_estimate);
		if (TextUtils.isEmpty(v.getParkFee())) {
			tv_park_price.setVisibility(View.GONE);
		} else {
			tv_park_price.setText("停车费      " + v.getParkFee());
		}

		if (14 == type) {
			tv_pile_detail_ac_tip.setVisibility(View.VISIBLE);
			tv_estimate.setText(String.format(
					getResources().getString(R.string.estimate_money), "--",
					"--"));
		}

		tv_balance = (TextView) findViewById(R.id.pile_detail_current_balance);

		tv_recharge = (TextView) findViewById(R.id.pile_detail_recharge);
		tv_recharge.setOnClickListener(this);

		et_precharge = (EditText) findViewById(R.id.pile_detail_precharge);
		if (price != null
				&& Double.valueOf(price).compareTo(Double.valueOf(0)) != 0) {
			et_precharge.addTextChangedListener(new MyTextChangedListener());
		}
		bt_forty = (Button) findViewById(R.id.pile_detail_forty);

		bt_thirty = (Button) findViewById(R.id.pile_detail_thirty);

		bt_twenty = (Button) findViewById(R.id.pile_detail_twenty);

		bt_start_recharge = (Button) findViewById(R.id.pile_detail_charge);
		bt_start_recharge.setOnClickListener(PileDetailActivity.this);
		/*
		if (awaitType==1) {
			zhuangtai = "2";
			bt_start_recharge.setText("取消等待充电");
			ll_charge_switch.setVisibility(View.GONE);
			rl_await_switch.setVisibility(View.VISIBLE);
			tv_pile_detail_ac_tip.setVisibility(View.GONE);
			tv_pile_detail_await.setVisibility(View.VISIBLE);
			isStartAwait = false;
		}*/
		initBalance();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (isCloseTCP) {
			mTcpSocketManager.close();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.pile_detail_back:
			
			finish();
			break;
		case R.id.pile_detail_await:
			if (popupWindow == null) {
				popupWindow = new CustomTipAwaitPopuWindow(this,"知道");
			}
			popupWindow.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss() {
					popupWindow = null;
				}
			});
			popupWindow.showAtLocation(this.findViewById(R.id.rl_main),
					Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
			break;
		case R.id.pile_detail_charge:
			
			/*if (bt_start_recharge.getText().toString().trim().equals("取消等待充电")) {
				zhuangtai = "2";
				sendStopChargeCMD();
//				bt_start_recharge.setText("开始等待充电");
//				ll_charge_switch.setVisibility(View.VISIBLE);
//				rl_await_switch.setVisibility(View.GONE);
//				tv_pile_detail_ac_tip.setVisibility(View.VISIBLE);
//				tv_pile_detail_await.setVisibility(View.GONE);
//				isStartAwait = false;
			}else if (bt_start_recharge.getText().toString().trim().equals("开始充电")) {		
//				isStartAwait = true;
 */
				if (mTcpSocketManager.hasTcpConnection()) {
					zhuangtai = "0";
					showPD("正在发送充电请求...");
					bt_start_recharge.setEnabled(false);
					bt_start_recharge.setBackgroundResource(R.drawable.common_button_shape);
					bean.setPrechargeMoney(""+et_precharge.getText().toString().trim());
					tv_precharge_money.setText("预充金额："+et_precharge.getText()
							.toString().trim()+" 元");
					mTcpSocketManager.sendStartChargeCMD(et_precharge.getText()
							.toString().trim());
				} else {
					showCustomDialog("通讯连接异常，请稍后重试...");
					mTcpSocketManager.reopen();
				}
		/*	}
			else if (bt_start_recharge.getText().toString().trim().equals("开始等待充电")){
				isStartAwait = false;
				if (mTcpSocketManager.hasTcpConnection()) {
					zhuangtai = "2";
					showPD("正在发送充电请求...");
					bt_start_recharge.setEnabled(false);
					bt_start_recharge.setBackgroundResource(R.drawable.common_button_shape);
					bean.setPrechargeMoney(""+et_precharge.getText().toString().trim());
					tv_precharge_money.setText("预充金额："+et_precharge.getText()
							.toString().trim()+" 元");
					mTcpSocketManager.sendStartChargeCMD(et_precharge.getText()
							.toString().trim());
				} else {
					showCustomDialog("通讯连接异常，请稍后重试...");
					mTcpSocketManager.reopen();
				}
			}*/

			break;
		case R.id.pile_detail_recharge:
			// 充值
			Intent in = new Intent();
			in.setClass(PileDetailActivity.this, RechargeActivity.class);
			startActivity(in);

			break;
		case R.id.pile_detail_price_help_icon:
			Intent priceIn = new Intent();
			priceIn.putExtra("priceId", bean.getRateId());
			priceIn.setClass(PileDetailActivity.this, AboutPriceActivity.class);
			startActivity(priceIn);

			break;
		case R.id.pile_detail_forty:
			et_precharge.setText("40");
			tv_precharge_money.setText("预充金额：40 元");
			break;
		case R.id.pile_detail_thirty:
			et_precharge.setText("30");
			tv_precharge_money.setText("预充金额：30 元");
			break;
		case R.id.pile_detail_twenty:
			et_precharge.setText("20");
			tv_precharge_money.setText("预充金额：20 元");
			break;
		default:
			break;
		}

	}

	private void AwaitPopuWindow() {
		dismissAllDialog();
		if (popupWindow == null) {
			popupWindow = new CustomTipAwaitPopuWindow(this,"two");
		}
		popupWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				popupWindow = null;
			}
		});
		if (!popupWindow.isShowing()) {			
			popupWindow.showAtLocation(this.findViewById(R.id.rl_main),
					Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
		}
	}

	@Override
	protected void getData() {

	}

	@Override
	public void onSuccess(String sign, Bundle bundle) {
		// 获取余额
		if (bundle != null) {
			if (sign.equals(Protocol.BANLANCE)) {				
				BanlanceBean mBanlanceBean = (BanlanceBean) bundle
						.getSerializable(Protocol.DATA);
				if (mBanlanceBean != null) {
					PreferencesUtil.setPreferences(this, "usinAccountbalance",
							mBanlanceBean.getUserAB());
					initBalance();
					if (awaitType == 1) {
						tv_precharge_money.setText("预充金额："+bean.getPrechargeMoney()+" 元");
					}
				}
			}else if (sign.equals(Protocol.INTEGRAL_PROPORTION)) {
				if(bundle != null){
					@SuppressWarnings("unchecked")
					ArrayList<IntegralProportionBean> integralProportionBeans =  (ArrayList<IntegralProportionBean>) bundle
						.getSerializable(Protocol.DATA);
				if (integralProportionBeans.size()>0) {
					IntegralProportionBean proportionBean = integralProportionBeans.get(0);
					if (!Tools.isEmptyString(proportionBean.getRatio_integral_value())&&!proportionBean.getRatio_integral_value().equals("0")) {					
						proportion = proportionBean.getRatio_integral_value();
//						rl_jifen.setVisibility(View.VISIBLE);
					}else {
						if (!Tools.isEmptyString(proportionBean.getFixed_integral_value())&&!proportionBean.getFixed_integral_value().equals("0")) {
							fixationproportion = proportionBean.getFixed_integral_value();
//							rl_jifen.setVisibility(View.VISIBLE);
						}else {
//							rl_jifen.setVisibility(View.GONE);
						}
					}
					String  str = et_precharge.getText().toString().trim();
					if (!proportion.equals("0")&&str.length()>0) {					
						tv_jifen.setText(""+(Integer.parseInt(str)*Integer.parseInt(proportion)));
					}else {
						if (!fixationproportion.equals("0")&&str.length()>0) {		
							tv_jifen.setText(""+fixationproportion);
						}
						
					}
				}
			  }
			}else if (sign.equals(Protocol.INTEGRAL_EVENT)) {
				//用户详情信息 
				if (bundle != null) {
					ArrayList<IntegralEventBean> integralEventBeans = (ArrayList<IntegralEventBean>) bundle.getSerializable(Protocol.DATA);
				if (integralEventBeans.size()>0) {
					IntegralEventBean integralEventBean = integralEventBeans.get(0);
					if(integralEventBean!= null){
						rl_jifen.setVisibility(View.VISIBLE);
						
					}
				}else {
					rl_jifen.setVisibility(View.GONE);
				}
				}
			}
		}
	}

	@Override
	public void onFaile(String sign, Bundle bundle) {

	}

	private class MyTextChangedListener implements TextWatcher {

		@Override
		public void afterTextChanged(Editable s) {
			// et_precharge
			String str = s.toString().trim();
			et_precharge.setSelection(str.length());
			if (str.length() > 0) {
				if (str.contains(".")&&str.indexOf(".") == 0) {
					et_precharge.setText(str.replaceAll(".", ""));
					return;
				}
				
				
				
				// 时间= 60*precharge/(power*price)=15.625分钟
				float ftime = (Float.valueOf(str) * 60f)
						/ (Float.valueOf(power) * Float.valueOf(price));
				float fdistance = (Float.valueOf(str) * 5.58f)
						/ Float.valueOf(price);
				BigDecimal time = new BigDecimal(ftime).setScale(0,
						RoundingMode.HALF_UP);
				BigDecimal distance = new BigDecimal(fdistance).setScale(0,
						RoundingMode.HALF_UP);
				tv_estimate.setText(String.format(
						getResources().getString(R.string.estimate_money),
						time.toString(), distance.toString()));
				if (str.contains(".")) {
                    if (str.length() - 1 - str.indexOf(".") > DECIMAL_DIGITS) {
                    	str =  str.substring(0,str.indexOf(".") + DECIMAL_DIGITS+1);
                        et_precharge.setText(str);
//                        et_precharge.setSelection(str.length());
                    }
                }
				BigDecimal a = new BigDecimal(str);
				BigDecimal b = new BigDecimal(balance);
				BigDecimal c = new BigDecimal(1);
				if (a.compareTo(b) <= 0) {
					bt_start_recharge.setEnabled(true);
					bt_start_recharge
							.setBackgroundResource(R.drawable.popup_select_shape_confirm);
				} else {
					showToast("输入金额不能大于账号余额");
					bt_start_recharge.setEnabled(false);
					bt_start_recharge
							.setBackgroundResource(R.drawable.common_button_shape);
					return;
				}
				if (a.compareTo(c) >= 0) {
					bt_start_recharge.setEnabled(true);
					bt_start_recharge
							.setBackgroundResource(R.drawable.popup_select_shape_confirm);
				} else {
					showToast("输入金额不能低于1元");
					bt_start_recharge.setEnabled(false);
					bt_start_recharge
							.setBackgroundResource(R.drawable.common_button_shape);
					return;
				}

				if (!proportion.equals("0")) {	
					rtion = String.valueOf((Float.parseFloat(str)/Float.parseFloat(proportion)));
					if (rtion.contains(".")) {
	                    if (rtion.length() - 1 - rtion.indexOf(".") > 0) {
	                    	rtion =  rtion.substring(0,rtion.indexOf("."));

	                    }
	                }
					tv_jifen.setText(""+rtion);
				}else {
					if (!fixationproportion.equals("0")) {		
						tv_jifen.setText(""+fixationproportion);
					}
					
				}
			} else {
				bt_start_recharge.setEnabled(false);
				bt_start_recharge
						.setBackgroundResource(R.drawable.common_button_shape);
				tv_estimate.setText(String.format(
						getResources().getString(R.string.estimate_money),
						"--", "--"));
				tv_jifen.setText("0");
			}
			

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}
	}

	// 处理返回的tcp报文
	@Override
	public void handleTcpPacket(final ByteArrayInputStream result) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				try {
					cancelPD();
					StreamUtil.readByte(result);// int reason =
					short cmdtype = StreamUtil.readShort(result);
					LogUtil.i("cm_netPost", "000000000000000==" + cmdtype);
					switch (cmdtype) {
					case SocketConstant.CMD_TYPE_CONNECT:
						
						int successflag = StreamUtil.readByte(result);
						LogUtil.i("cm_netPost", "++++++++++++++++++++==" + type);
						short errorcode = StreamUtil.readShort(result);
						LogUtil.i("cm_netPost", "------------------------==" + type);
						int headState = StreamUtil.readByte(result);
						int type = 1;
						try {
							type = StreamUtil.readByte(result);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						LogUtil.i("cm_netPost", "--------666666666666666666666==" + type+"  headState   ="+headState);
						mTcpSocketManager.setPileType(type);
						if (successflag == 0) {
							showErrorDialog(errorcode);
						} 
//						if (17 == headState) {//等待充电
//							ll_charge_switch.setVisibility(View.GONE);
//							rl_await_switch.setVisibility(View.VISIBLE);
//							tv_pile_detail_ac_tip.setVisibility(View.GONE);
//							tv_pile_detail_await.setVisibility(View.VISIBLE);
//							bt_start_recharge.setText("取消等待充电");
//							// 开始充电按钮重新变亮
//							bt_start_recharge.setOnClickListener(PileDetailActivity.this);
//							bt_start_recharge.setBackgroundResource(R.drawable.popup_select_shape_confirm);
//						}
						break;
					case SocketConstant.CMD_TYPE_START_CHARGE:
						// 开始充电响应
						int startflag = StreamUtil.readByte(result);
						short errorCode = StreamUtil.readShort(result);
						handleStartChargeEvent(startflag, errorCode);
						break;
				case SocketConstant.CMD_TYPE_CONNECT_AWAIT:
						// 开始等待
						int awaitState = StreamUtil.readByte(result);
						int time = StreamUtil.readUB3(result);
//						if (popupWindow!=null && popupWindow.isShowing()) {
//							popupWindow.dismiss();
//						}
//						if (17 == awaitState&&isStartAwait) {//等待充电
//							zhuangtai = "2";
//							AwaitPopuWindow();
//							isStartAwait = false;
//						}else if (17 == awaitState&&!isStartAwait){
//							ll_charge_switch.setVisibility(View.GONE);
//							rl_await_switch.setVisibility(View.VISIBLE);
//							tv_pile_detail_ac_tip.setVisibility(View.GONE);
//							tv_pile_detail_await.setVisibility(View.VISIBLE);
//							bt_start_recharge.setText("取消等待充电");
//							// 开始充电按钮重新变亮
//							bt_start_recharge.setEnabled(true);
//							bt_start_recharge.setBackgroundResource(R.drawable.popup_select_shape_confirm);
//						}else 
							if(2 == awaitState) {							
//								bt_start_recharge.setText("开始等待充电");
//								ll_charge_switch.setVisibility(View.VISIBLE);
//								rl_await_switch.setVisibility(View.GONE);
//								tv_pile_detail_ac_tip.setVisibility(View.VISIBLE);
//								tv_pile_detail_await.setVisibility(View.GONE);
//								isStartAwait = false;
								mTcpSocketManager.close();
								// 开始充电按钮重新变亮
								bt_start_recharge.setEnabled(true);
								bt_start_recharge.setBackgroundResource(R.drawable.popup_select_shape_confirm);
						}else if(12 == awaitState) {							
//							bt_start_recharge.setText("开始等待充电");
//							ll_charge_switch.setVisibility(View.VISIBLE);
//							rl_await_switch.setVisibility(View.GONE);
//							tv_pile_detail_ac_tip.setVisibility(View.VISIBLE);
//							tv_pile_detail_await.setVisibility(View.GONE);
//							isStartAwait = false;
							mTcpSocketManager.close();
							// 开始充电按钮重新变亮
							bt_start_recharge.setEnabled(true);
							bt_start_recharge.setBackgroundResource(R.drawable.popup_select_shape_confirm);
					}
						
						break;
					case SocketConstant.CMD_TYPE_CHARGE_EVENT:
						// 充电事件 1:充电开始 0:放弃充电
						int eventcode = StreamUtil.readByte(result);
						
//						if (0 == eventcode&&zhuangtai.equals("2")) {	
							
//							bt_start_recharge.setText("开始等待充电");
//							ll_charge_switch.setVisibility(View.VISIBLE);
//							rl_await_switch.setVisibility(View.GONE);
//							tv_pile_detail_ac_tip.setVisibility(View.VISIBLE);
//							tv_pile_detail_await.setVisibility(View.GONE);
//							isStartAwait = false;
//							mTcpSocketManager.close();
//							// 开始充电按钮重新变亮
//							bt_start_recharge.setEnabled(true);
//							bt_start_recharge
//											.setBackgroundResource(R.drawable.popup_select_shape_confirm);
							
							
//						}else {							
							handleChargeEvent(eventcode);
//						}
						break;
					case SocketConstant.CMD_TYPE_GUN:
						// 枪与车的连接状态  1:枪与车未连接 2:枪与车连接 
		 				int gunState = StreamUtil.readByte(result);
						LogUtil.i("cm_socket", "枪与车的连接状态" + gunState);
						mTcpSocketManager.setGunState(gunState);
						mTcpSocketManager.sendTipGunState(StreamUtil
								.readWithLength(result, 3));
						
						if (isStartCharge) {
							if (gunState == 1) {
								if (tipInsertGunDialog == null) {
									tipInsertGunDialog = new CustomTipInsertGunDialog(
											PileDetailActivity.this);
									tipInsertGunDialog.setCancelable(false);
								}
								if (customDialog != null
										&& customDialog.isShowing()) {
									customDialog.dismiss();
								}
								if (!tipInsertGunDialog.isShowing()) {
									tipInsertGunDialog.show();
								}
							} else if (gunState == 2&&!bt_start_recharge.getText().equals("开始等待充电")) {
								showCustomDialog("准备放电，请稍等");
							}else if (gunState == 2&&!bt_start_recharge.getText().equals("取消等待充电")) {
								showCustomDialog("准备放电，请稍等");
							}
						}

						break;
					case SocketConstant.CMD_TYPE_REAL_DATA:
						
						
						
						// 收到实时数据，进入实时数据界面
						dismissAllDialog();
						// 地图界面显示
						Intent chargeStartIn = new Intent();
						chargeStartIn
								.setAction(BroadcastUtil.BROADCAST_Charge_Ing);
						pileNum = mTcpSocketManager.getPileNum();
						headnum = mTcpSocketManager.getHeadNum();
						chargeStartIn.putExtra("chargepilenum", pileNum);
						chargeStartIn.putExtra("chargeheadnum",
								Byte.toString(headnum));
						sendBroadcast(chargeStartIn);

						int state = StreamUtil.readByte(result);

						
						
						short chargeTime = StreamUtil.readShort(result);
						StreamUtil.readShort(result);// short dianya =
						StreamUtil.readShort(result);// short dianliu =
						int diandu = StreamUtil.readInt(result);
						short feilv = StreamUtil.readShort(result);
						int yuchong = StreamUtil.readInt(result);
						int yichong = StreamUtil.readInt(result);
						int soc = StreamUtil.readByte(result);
						StreamUtil.readInt(result);// int fushu =
						StreamUtil.readInt(result);// int gaojing =
//						if (6 == state) {//等待充电
							Intent realIn = new Intent(PileDetailActivity.this,
									RealTimeChargeActivity.class);
							realIn.putExtra("state", state);
							realIn.putExtra("chargeTime", chargeTime);
							realIn.putExtra("diandu", diandu);
							realIn.putExtra("feilv", feilv);
							realIn.putExtra("yuchong", yuchong);
							realIn.putExtra("yichong", yichong);
							realIn.putExtra("soc", soc);
							
							if (getIntent().getStringExtra("interfacefrom").equals("shaomiao")) {
								realIn.putExtra("interfacefrom", "shaomiao");
							}else if (getIntent().getStringExtra("interfacefrom").equals("person")) {
								realIn.putExtra("interfacefrom", "person");
							}else if (getIntent().getStringExtra("interfacefrom").equals("mapmob")) {
								realIn.putExtra("interfacefrom", "mapmob");
							}else if (getIntent().getStringExtra("interfacefrom").equals("home")) {
								realIn.putExtra("interfacefrom", "home");
							}else{							
								realIn.putExtra("interfacefrom", "pile");
							}
							sendBroadcast(new Intent("scan_start_charge"));
							isCloseTCP = false;
							startActivity(realIn);
							finish(); 
//						}
						
						break;
					case SocketConstant.CMD_TYPE_CONSUME_RECORD:
						ll_charge_switch.setVisibility(View.VISIBLE);
						rl_await_switch.setVisibility(View.GONE);
						tv_pile_detail_ac_tip.setVisibility(View.VISIBLE);
						tv_pile_detail_await.setVisibility(View.GONE);
						et_precharge.setText(bean.getPrechargeMoney());
						bt_start_recharge.setText("开始充电");
						mTcpSocketManager.close();
						break;
					}

				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}
	// 处理开始充电时间
	private void handleStartChargeEvent(int startflag, short errorCode) {
		if (0 == startflag) {
			Log.i("cm_socket", "开始充电失败原因" + errorCode);
			showErrorDialog(errorCode);
		} else if (1 == startflag) {
			isStartCharge = true;
			Log.i("cm_socket", "开始充电响应成功");
			// 充电成功 , 弹框提示插枪--//枪与车的连接状态  1:枪与车未连接 2:枪与车连接 
			if (1 == mTcpSocketManager.getGunState()) {
				if (tipInsertGunDialog == null) {
					tipInsertGunDialog = new CustomTipInsertGunDialog(
							PileDetailActivity.this);
					tipInsertGunDialog.setCancelable(false);
				}
				if (!tipInsertGunDialog.isShowing()) {
					tipInsertGunDialog.show();
				}
			} else {
//				if (bt_start_recharge.getText().equals("开始等待充电")) {
//					isStartAwait = false;
//					if (mTcpSocketManager.hasTcpConnection()) {
//						zhuangtai = "2";
//						showPD("正在发送充电请求...");
//						bt_start_recharge.setEnabled(false);
//						bt_start_recharge.setBackgroundResource(R.drawable.common_button_shape);
//						bean.setPrechargeMoney(""+et_precharge.getText().toString().trim());
//						tv_precharge_money.setText("预充金额："+et_precharge.getText()
//								.toString().trim()+" 元");
//						mTcpSocketManager.sendStartChargeCMD(et_precharge.getText()
//								.toString().trim());
//					} else {
//						showCustomDialog("通讯连接异常，请稍后重试...");
//						mTcpSocketManager.reopen();
//					}
//				}
//				if (!bt_start_recharge.getText().equals("开始等待充电")) {
					showCustomDialog("准备放电，请稍等");
//				}else if (!bt_start_recharge.getText().equals("取消等待充电")) {
//					showCustomDialog("准备放电，请稍等");
//				}
			}
		}
	}

	// 处理充电事件--放弃或开始
	private void handleChargeEvent(int eventcode) {
		if (0 == eventcode) {
			dismissAllDialog();
			mFinishChargeD = new WalletWarningDialog(PileDetailActivity.this,
					"由于长时间未插枪，本次充电自动放弃！");
			mFinishChargeD.setCancelable(false);
			mFinishChargeD.setOnPositiveListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mFinishChargeD.dismiss();
					finish();
				}
			});
			mFinishChargeD.show();
		}
//		else if (2 == eventcode){
//			dismissAllDialog();
//			mFinishChargeD = new WalletWarningDialog(PileDetailActivity.this,
//					"取消成功");
//			mFinishChargeD.setCancelable(false);
//			mFinishChargeD.setOnPositiveListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					mFinishChargeD.dismiss();
//					
//				}
//			});
//			mFinishChargeD.show();
//		}
	}

	private void showCustomDialog(String content) {
		if (customDialog == null) {
			customDialog = new CustomCommonDialog(this);
			customDialog.setCancelable(false);
			customDialog.setCustomDialogListener(this);
		}

		if (tipInsertGunDialog != null && tipInsertGunDialog.isShowing()) {
			tipInsertGunDialog.dismiss();
		}
		customDialog.getContentView().setText(content);
		if (!customDialog.isShowing()) {
			customDialog.show();
		}

	}

	@Override
	public void OnDialogClick() {
		if (customDialog != null && customDialog.isShowing()) {
			customDialog.dismiss();
		}
		// 开始充电按钮重新变亮
//		bt_start_recharge.setOnClickListener(PileDetailActivity.this);
//		bt_start_recharge
//				.setBackgroundResource(R.drawable.popup_select_shape_confirm);
	}

	private void showErrorDialog(short code) {

		switch (code) {
		case 110:
			showCustomDialog(getResources().getString(R.string.tip_error_110));
			break;
		case 1002:
			showCustomDialog(getResources().getString(R.string.tip_error_1002));
			break;
		case 6000:
			showCustomDialog(getResources().getString(R.string.tip_error_6000));
			break;
		case 6001:
			showCustomDialog(getResources().getString(R.string.tip_error_6001));
			break;
		case 6100:
			showCustomDialog(getResources().getString(R.string.tip_error_6100));
			break;
		case 6101:
			showCustomDialog(getResources().getString(R.string.tip_error_6101));
			break;
		case 6102:
			showCustomDialog(getResources().getString(R.string.tip_error_6102));
			break;
		case 6104:
			showCustomDialog(getResources().getString(R.string.tip_error_6104));
			break;
		case 6105:
			showCustomDialog(getResources().getString(R.string.tip_error_6105));
			break;
		case 6200:
			showCustomDialog(getResources().getString(R.string.tip_error_6200));
			break;
		case 6201:
			showCustomDialog(getResources().getString(R.string.tip_error_6201));
			break;
		case 6202:
			showCustomDialog(getResources().getString(R.string.tip_error_6202));
			break;
		case 6203:
			showCustomDialog(getResources().getString(R.string.tip_error_6203));
		case 6300:
			showCustomDialog(getResources().getString(R.string.tip_error_6300));
			break;
		case 6301:
			showCustomDialog(getResources().getString(R.string.tip_error_6301));
			break;
		case 6401:
			showCustomDialog(getResources().getString(R.string.tip_error_6401));
			break;
		case 6402:
			showCustomDialog(getResources().getString(R.string.tip_error_6402));
			break;
		case 6403:
			showCustomDialog(getResources().getString(R.string.tip_error_6403));
			break;
		case 6404:
			showCustomDialog(getResources().getString(R.string.tip_error_6404));
			break;
		case 6405:
			showCustomDialog(getResources().getString(R.string.tip_error_6405));
			break;
		case 6406:
			showCustomDialog(getResources().getString(R.string.tip_error_6406));
			break;
		case 6600:
			showCustomDialog(getResources().getString(R.string.tip_error_6600));
			break;
		case 6601:
			showCustomDialog(getResources().getString(R.string.tip_error_6601));
			break;
		case 6633:
			showCustomDialog(getResources().getString(R.string.tip_error_6633));
			break;
		case 6700:
			showCustomDialog(getResources().getString(R.string.tip_error_6700));
			break;
		case 6701:
			showCustomDialog(getResources().getString(R.string.tip_error_6701));
			break;
		case 6702:
			showCustomDialog(getResources().getString(R.string.tip_error_6702));
			break;
		case 6703:
			showCustomDialog(getResources().getString(R.string.tip_error_6703));
			break;
		case 6704:
			showCustomDialog(getResources().getString(R.string.tip_error_6704));
			break;
		case 6705:
			showCustomDialog(getResources().getString(R.string.tip_error_6705));
			break;
		case 6706:
			showCustomDialog(getResources().getString(R.string.tip_error_6706));
			break;
		case 6800:
			showCustomDialog(getResources().getString(R.string.tip_error_6800));
			break;
		case 6801:
			showCustomDialog(getResources().getString(R.string.tip_error_6801));
			break;
		case 6802:
			showCustomDialog(getResources().getString(R.string.tip_error_6802));
			break;
		case 6803:
			showCustomDialog(getResources().getString(R.string.tip_error_6803));
			break;
		case 6804:
			showCustomDialog(getResources().getString(R.string.tip_error_6804));
			break;
		default:
			showCustomDialog("未知原因");
			break;
		}
	}

	private void dismissAllDialog() {
		if (customDialog != null && customDialog.isShowing()) {
			customDialog.dismiss();
		}
		if (tipInsertGunDialog != null && tipInsertGunDialog.isShowing()) {
			tipInsertGunDialog.dismiss();
		}
		if (popupWindow!=null && popupWindow.isShowing()) {
			popupWindow.dismiss();
		}
	}

	@Override
	public void handleSocketException() {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				showErrorCode(110);
			}
		});
	}

	@Override
	public void know() {
		bt_start_recharge.setText("开始等待充电");
//		zhuangtai = "1";
		// 开始充电按钮重新变亮
		bt_start_recharge.setEnabled(true);
		bt_start_recharge.setBackgroundResource(R.drawable.popup_select_shape_confirm);
		sendStopChargeCMD();
	}

	@Override
	public void await() {
		ll_charge_switch.setVisibility(View.GONE);
		rl_await_switch.setVisibility(View.VISIBLE);
		tv_pile_detail_ac_tip.setVisibility(View.GONE);
		tv_pile_detail_await.setVisibility(View.VISIBLE);
		bt_start_recharge.setText("取消等待充电");
		// 开始充电按钮重新变亮
		bt_start_recharge.setEnabled(true);
		bt_start_recharge
						.setBackgroundResource(R.drawable.popup_select_shape_confirm);
		
	}
	
	/**
	 * 发送停止充电
	 */
	public void sendStopChargeCMD() {
		//发送结束充电命令
		if(mTcpSocketManager.hasTcpConnection()){
//			showPD("正在发送停止充电请求");
			mTcpSocketManager.sendStopChargeCMD();
//			mTcpSocketManager.close();
			bt_start_recharge.setEnabled(false);
			bt_start_recharge
					.setBackgroundResource(R.drawable.common_button_shape);
		}else {
			mTcpSocketManager.reopen();
			//showPD("正在发送停止充电请求");
			//mTcpSocketManager.sendStopChargeCMD();
			showToast("通讯异常，请重试");
			
		}
	}
}
