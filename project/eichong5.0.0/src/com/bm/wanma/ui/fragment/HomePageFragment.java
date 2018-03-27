package com.bm.wanma.ui.fragment;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.tsz.afinal.FinalDb;
import net.tsz.afinal.db.table.TableInfo;
import pulltorefresh.ListViewCustom;
import pulltorefresh.PullToRefreshBase;
import pulltorefresh.PullToRefreshBase.OnRefreshListener2;
import pulltorefresh.PullToRefreshScrollView;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.MyLocationStyle;
import com.bm.wanma.R;
import com.bm.wanma.adapter.FunctionButtonAdapter;
import com.bm.wanma.adapter.InformationAdapter;
import com.bm.wanma.broadcast.BroadcastUtil;
import com.bm.wanma.broadcast.NetWorkChangeBroadcastReceiver;
import com.bm.wanma.broadcast.NetWorkChangeBroadcastReceiver.HomeNetWorkChangeCallback;
import com.bm.wanma.entity.AreaBean;
import com.bm.wanma.entity.ChargeFinishAndDoneBean;
import com.bm.wanma.entity.FunctionButtonBean;
import com.bm.wanma.entity.ImageCarouselBean;
import com.bm.wanma.entity.InformationBean;
import com.bm.wanma.entity.ShareToThirdBean;
import com.bm.wanma.entity.TextCarouselBean;
import com.bm.wanma.net.FunctionPictureUpload;
import com.bm.wanma.net.GetDataPost;
import com.bm.wanma.net.ImgBannerPictureUpload;
import com.bm.wanma.net.Protocol;
import com.bm.wanma.socket.SocketConstant;
import com.bm.wanma.socket.StreamUtil;
import com.bm.wanma.socket.TCPSocketManager;
import com.bm.wanma.ui.activity.CommonH5Activity;
import com.bm.wanma.ui.activity.ITcpCallBack;
import com.bm.wanma.ui.activity.LoginActivity;
import com.bm.wanma.ui.activity.MalfunctionActivity;
import com.bm.wanma.ui.activity.MoreActivity;
import com.bm.wanma.ui.activity.MyChargeActivity;
import com.bm.wanma.ui.activity.RealTimeChargeActivity;
import com.bm.wanma.ui.activity.RechargeActivity;
import com.bm.wanma.ui.scan.CaptureActivity;
import com.bm.wanma.ui.shareactivity.ShareDetailActivity;
import com.bm.wanma.utils.LogUtil;
import com.bm.wanma.utils.PreferencesUtil;
import com.bm.wanma.utils.ProjectApplication;
import com.bm.wanma.utils.ToastUtil;
import com.bm.wanma.utils.Tools;
import com.bm.wanma.utils.ViewFactory;
import com.bm.wanma.view.AutoVerticalScrollTextView;
import com.bm.wanma.view.AutoVerticalScrollTextView2;
import com.bm.wanma.view.CycleViewPager;
import com.bm.wanma.view.CycleViewPager.ImageCycleViewListener;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.umeng.analytics.MobclickAgent;

/**
 * 首页
 * @author LYH
 *
 */
public class HomePageFragment extends BaseFragment  implements OnRefreshListener2<ScrollView>,ITcpCallBack ,LocationSource, AMapLocationListener,HomeNetWorkChangeCallback,OnClickListener{
	private boolean isfirst = true;
	private boolean isfirst2 = true;
	private boolean isfirstcharge = true;
	private boolean one = true;
	private static HomeMapCallback mapcallback;
	
	private int flag = 0;
	ListViewCustom iv_information_listview;
	private List<ImageView> views = new ArrayList<ImageView>();
	private ArrayList<ImageCarouselBean> carouselBeans = new ArrayList<ImageCarouselBean>();
	private ArrayList<TextCarouselBean> textCarouselBeans = new ArrayList<TextCarouselBean>();
	private ArrayList<FunctionButtonBean> buttonBeans = new ArrayList<FunctionButtonBean>();
	private ArrayList<FunctionButtonBean> buttonBeans2 = new ArrayList<FunctionButtonBean>();
	private PullToRefreshScrollView pScrollView;
	private TextView tv_help,tv_information_title;
	private String pageNum = "10";
	private int currentPage = 1;
	private boolean isRefresh;
	private String pkuserId;
	private List<InformationBean> rawdata,beans;
	private InformationAdapter informationAdapter;
	private FunctionButtonBean buttonBean;
	private CycleViewPager cycleViewPager;
	private GridView gridView;
	private String userId;
	private LinearLayout ll_information;
	/*上下滚动*/
    private int number =0;
    private boolean isCurrentFragment = true;
    private AutoVerticalScrollTextView verticalScrollTV;
    private AutoVerticalScrollTextView2 tv_fault_title;
    private FinalDb finalDb;
    private AMap aMap;
    private MapView mapView;
    private MyLocationStyle myLocationStyle;
    private LocationManagerProxy mAMapLocationManager;
    private boolean hasLocationSuccess = false;
    private boolean isf = true;
//    private FrameLayout fl_charging_confirm;
    private String provincecode = null;
	private String citycode = null;
	private String countycode = null;
	private String areaName = null;
	/**
	 * 充电
	 */
	private ArrayList<ChargeFinishAndDoneBean> chargeList;
	private ChargeFinishAndDoneBean chargeOrderBean;
	private TextView iv_charging_animation;
	private String pilenum,headnum;
	private TCPSocketManager mTcpSocketManager;
//	private AlphaAnimation animation = new AlphaAnimation(1, 0);
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		mPageName = "HomePageFragment";
		finalDb = FinalDb.create(getActivity(),Protocol.DATABASE_NAME,true,Protocol.dbNumer,null);
		NetWorkChangeBroadcastReceiver.setNetWorkChangecallback(this);
		pkuserId = PreferencesUtil.getStringPreferences(getActivity(),"pkUserinfo");
//		registerBoradcastReceiver();//注册调用户信息接口
//		if (!Tools.isEmptyString(pkuserId)) {
//			GetDataPost.getInstance(getActivity()).getChargeInfoList(handler,pkuserId, "2", "1", "1");
//		}
//		MobclickAgent.onEvent(mContext, "anniu_shouye");
		currentPage = 1;
		isRefresh = true;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
			SystemBarTintManager tintManager = new SystemBarTintManager(getActivity());
			tintManager.setStatusBarTintEnabled(true);
			tintManager.setStatusBarTintResource(R.color.common_orange);
        }
		
	}
	
	@TargetApi(19) 
	private void setTranslucentStatus(boolean on) {
		Window win = getActivity().getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View bespokeOrderFragment = inflater.inflate(
				R.layout.homepage_fragment, container, false);
		mapView = (MapView) bespokeOrderFragment.findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);// 必须要写
		
		initView(bespokeOrderFragment);
		initMap();
		intiData();
		return bespokeOrderFragment;
	} 
	
	
	private void initMap() {
		if (aMap == null) {
			aMap = mapView.getMap();
			
			setUpMap();
		}
	}

	private void intiData() {
		InitBanner();

		if(isNetConnection()){
			showPD("正在加载数据...");
			GetDataPost.getInstance(getActivity()).getMyFunctionButton(handler);
		}
		initbuttondata();
		functionbutton();
		InitInformationData();
		
	}

	private void InitInformationData() {
		if (ProjectApplication.getButtonType()) {	
			if (finalDb.tabIsExistByName("tb_information_data")) {				
				beans = finalDb.findAll(InformationBean.class);
			}else {
				beans = new ArrayList<InformationBean>();				
			}
			if (beans.size()>0) {
				tv_information_title.setVisibility(View.VISIBLE);
			informationface();
			}else{
				tv_information_title.setVisibility(View.GONE);
			}
		}else {
			if (beans == null) {
				beans = new ArrayList<InformationBean>();
			}
			if (isNetConnection()) {		
				NoNetConnectionInitInformation();
				informationface();
				ProjectApplication.setButtonType(true);
			}
		}
		
		
		rawdata = new ArrayList<InformationBean>();
		if(isNetConnection()){
			GetDataPost.getInstance(getActivity()).getMyInformationList(handler,String.valueOf(currentPage), pageNum);
		}else {
			ll_information.setVisibility(View.GONE);
			if (!ProjectApplication.getButtonType()) {				
				NoNetConnectionInitInformation();
				informationface();
			}
		}
	}

	private void InitBanner() {
		
		NoNetConnectionInitBanner();
		if (!isNetConnection()) {
			if (carouselBeans.size()>0) {
				carouselBeans.clear();
			}
			if (finalDb.tabIsExistByName("tb_imagecarousel_data")) {				
				carouselBeans = (ArrayList<ImageCarouselBean>) finalDb.findAll(ImageCarouselBean.class);
			}
			if (carouselBeans.size()>0) {				
				initialize();
			}
		}
	}


	
	/**
	 * 设置一些amap的属性
	 */
	private void setUpMap() {
		myLocationStyle = new MyLocationStyle();
		myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));// 设置圆形的边框颜色
		myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));// 设置圆形的填充颜色
		myLocationStyle.myLocationIcon(BitmapDescriptorFactory
				.fromResource(R.drawable.location_marker));// 设置小蓝点的图标
		aMap.setMyLocationStyle(myLocationStyle);

		aMap.setLocationSource(this);// 设置定位监听
		aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
		aMap.getUiSettings().setZoomControlsEnabled(false);//设置默认缩放按钮是否显示
		aMap.getUiSettings().setRotateGesturesEnabled(false);//禁用手势旋转地图
		aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
		// 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
		aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
		//aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_ROTATE);
	}

	private void initView(View bespokeOrderFragment) {
//		fl_charging_confirm = (FrameLayout) bespokeOrderFragment.findViewById(R.id.charging_confirm);
		tv_help = (TextView) bespokeOrderFragment.findViewById(R.id.help);
		tv_help.setOnClickListener(this);
		tv_information_title = (TextView) bespokeOrderFragment.findViewById(R.id.information_title);
		iv_charging_animation = (TextView) bespokeOrderFragment.findViewById(R.id.fragment_myperson_charging);
		iv_charging_animation.setOnClickListener(this);
		
		gridView = (GridView)bespokeOrderFragment.findViewById(R.id.custom_function);
        verticalScrollTV = (AutoVerticalScrollTextView) bespokeOrderFragment.findViewById(R.id.textview_auto_roll);
        tv_fault_title = (AutoVerticalScrollTextView2) bespokeOrderFragment.findViewById(R.id.fault_title);
        iv_information_listview = (ListViewCustom) bespokeOrderFragment.findViewById(R.id.information_listview);
        ll_information = (LinearLayout) bespokeOrderFragment.findViewById(R.id.information);
        
        pScrollView = (PullToRefreshScrollView) bespokeOrderFragment.findViewById(R.id.charge_done_finish_refresh);
		pScrollView.setOnRefreshListener(this);
		
		cycleViewPager = (CycleViewPager) getFragmentManager().findFragmentById(R.id.fragment_cycle_viewpager_content);
		
		
        
		
    }

    

	private void NoNetConnectionInitInformation() {
		InformationBean informationBean = new InformationBean();
		informationBean.setNeiUpdatedate("                      ");
		informationBean.setNeiName("                                                   ");
		informationBean.setNewsPicUrl("      ");
		informationBean.setNeiUrl("  ");
		informationBean.setNeiCreatedate("                      ");
		beans.add(informationBean);
		InformationBean informationBean2 = new InformationBean();
		informationBean2.setNeiUpdatedate("                      ");
		informationBean2.setNeiName("                                                   ");
		informationBean2.setNewsPicUrl("      ");
		informationBean2.setNeiUrl(" ");
		informationBean.setNeiCreatedate("                      ");
		beans.add(informationBean2);
	}



	private Handler handlerfunction = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 199&&textCarouselBeans.size()>1) {
            	
            	verticalScrollTV.next();
            	number++;
            	try {
            		if (textCarouselBeans.get(number%textCarouselBeans.size()).getMeiType().equals("0")
            				||textCarouselBeans.get(number%textCarouselBeans.size()).getMeiType().equals("1")) {						
            			tv_fault_title.setText("[故障] ");
            		}else {
            			tv_fault_title.setText("[新建] ");
            		}
            		verticalScrollTV.setText(Tools.parseDate(textCarouselBeans.get(number%textCarouselBeans.size()).getMeiBeginTime() ,"yyyy/MM/dd HH:mm", "MM.dd")+"  "+textCarouselBeans.get(number%textCarouselBeans.size()).getMeiName());
				} catch (Exception e) {
				}
            }

        }
    };
	private boolean identical = true;
    
	
	@SuppressWarnings("unchecked")
	@Override
	public void onSuccess(String sign, Bundle bundle) {
		cancelPD();
		if(bundle != null ){
			if (sign.equals(Protocol.SIGN_IN_GET)) {
				//充电中订单列表
				chargeList = (ArrayList<ChargeFinishAndDoneBean>) bundle
						.getSerializable(Protocol.DATA);
				if (chargeList != null && chargeList.size() > 0) {
					chargeOrderBean = chargeList.get(0);
					if (chargeOrderBean != null) {
						Intent intnet = new Intent(BroadcastUtil.BROADCAST_Charge_Ing);
						intnet.putExtra("chargepilenum",chargeOrderBean.getElPi_ElectricPileCode());
						intnet.putExtra("chargeheadnum",chargeOrderBean.getHeadCode());
						getActivity().sendBroadcast(intnet);
						isfirstcharge = false;
					}
				}
			}else if (sign.equals(Protocol.IMAGE_CAROUSEL)) {
				pScrollView.onRefreshComplete();
				hasLocationSuccess = true ;
				if (carouselBeans.size()>0) {
					carouselBeans.clear();
				}
				carouselBeans = (ArrayList<ImageCarouselBean>) bundle.getSerializable(Protocol.DATA);
				for (ImageCarouselBean imageCarouselBean : carouselBeans) {
					List<ImageCarouselBean> carouselBeans  = null;
					if (finalDb.tabIsExistByName("tb_imagecarousel_data")) {							
						carouselBeans = finalDb.findAllByWhere(ImageCarouselBean.class, "pkBlId="+imageCarouselBean.getPkBlId());
					}
					if (carouselBeans != null && carouselBeans.size()>0) {
						if (!carouselBeans.get(0).getBlUpdatedate().equals(imageCarouselBean.getBlUpdatedate())
								||!Tools.isPicture(imageCarouselBean.getPkBlId()+"banner")
								) {
							new ImgBannerPictureUpload(getActivity(), imageCarouselBean.getBannerPicUrl(), imageCarouselBean.getPkBlId())
							.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"");
						}
					}else {
						new ImgBannerPictureUpload(getActivity(), imageCarouselBean.getBannerPicUrl(), imageCarouselBean.getPkBlId())
						.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"");
					}
				}
				if (finalDb.tabIsExistByName("tb_imagecarousel_data")) {					
					finalDb.deleteAll(ImageCarouselBean.class);
				}
				
				if (carouselBeans.size()>0) {					
					initialize();	
				}else {
					NoNetConnectionInitBanner();
				}
				
				for (ImageCarouselBean imageCarouselBean : carouselBeans) {	
					imageCarouselBean.setBannerPicUrl("file:///"+Tools.advertisementpath+imageCarouselBean.getPkBlId()+"banner");
					finalDb.save(imageCarouselBean);
				}
			}else if (sign.equals(Protocol.TEXT_CAROUSEL)) {
				if (textCarouselBeans.size()>0) {
					textCarouselBeans.clear();
				}
				hasLocationSuccess = true ;
				textCarouselBeans =  (ArrayList<TextCarouselBean>) bundle.getSerializable(Protocol.DATA);
				pScrollView.onRefreshComplete();
				if (textCarouselBeans.size()>0) {
					try {
						ll_information.setVisibility(View.VISIBLE);
						if ((!Tools.isEmptyString(textCarouselBeans.get(0).getMeiType())&&textCarouselBeans.get(0).getMeiType().equals("0"))||textCarouselBeans.get(0).getMeiType().equals("1")) {						
							tv_fault_title.setText("[故障] ");
						}else {
							tv_fault_title.setText("[新建] ");
						}
						verticalScrollTV.setText(Tools.parseDate(textCarouselBeans.get(0).getMeiBeginTime() ,"yyyy/MM/dd HH:mm", "MM.dd")+"  "+textCarouselBeans.get(number%textCarouselBeans.size()).getMeiName());
						//故障消息上下滚动点击事件
						ll_information.setOnClickListener(this);
						
						if (one) {
							new Thread(){
								@Override
								public void run() {
									while (isRunning){
										if (textCarouselBeans.size()>1) {									
											SystemClock.sleep(5000);
											handlerfunction.sendEmptyMessage(199);
										}
									}
								}
							}.start();
							one = false; 
						}
					} catch (Exception e) {
						
						if (one) {
							new Thread(){
								@Override
								public void run() {
									while (isRunning){
										if (textCarouselBeans.size()>1) {									
											SystemClock.sleep(5000);
											handlerfunction.sendEmptyMessage(199);
										}
									}
								}
							}.start();
							one = false; 
						}
					}
					
				
				}else {
					ll_information.setVisibility(View.GONE);
				}
			}else if (sign.equals(Protocol.INFORMATION_LIST)) {
				
				pScrollView.onRefreshComplete();
				rawdata = (ArrayList<InformationBean>) bundle.getSerializable(Protocol.DATA);
				if (beans.equals(rawdata)) {
					identical  = false;
				}else{
					identical = true;
				}
				if (rawdata.size()>0&&isfirst) {
					if (beans.size()>0) {
						beans.clear();
						if (finalDb.tabIsExistByName("tb_information_data")) {					
							finalDb.deleteAll(InformationBean.class);
						}
					}
					for (InformationBean informationBean : rawdata) {
						finalDb.save(informationBean);
					}
				}
				
				if(rawdata.size()<10){
					pScrollView.pullsettins = false;
				}else {
					pScrollView.pullsettins = true;
				}

				isfirst = false;
				if(isRefresh){
						beans.clear();
						beans.addAll(rawdata);
				}else {
					
					if(rawdata.size()>0){
						beans.addAll(rawdata);
					}else {
						showToast("暂无更多数据");
					}
				}
				if (beans.size()>0) {
					tv_information_title.setVisibility(View.VISIBLE);
				}else{
					tv_information_title.setVisibility(View.GONE);
				}
				if (identical) {					
					informationface();
				}
			}else if (sign.equals(Protocol.FUNCTION_BUTTON)) {
				pScrollView.onRefreshComplete();
				if (buttonBeans.size()>0) {					
					buttonBeans.clear();
				}
				buttonBeans = (ArrayList<FunctionButtonBean>) bundle.getSerializable(Protocol.DATA);
				isfirst2 = false;
				if (buttonBeans.size()>0) {	
					int i = 0;
					for (FunctionButtonBean buttonBean : buttonBeans) {
						try {

							if (!buttonBean.getAblAction().equals("0")) {
								if (i==0 && finalDb.tabIsExistByName("tb_functionbutton_data")) {
									finalDb.deleteAll(FunctionButtonBean.class);
								}
								if(Tools.isPicture(buttonBean.getPkAblId()+"button")
					            		&&!Tools.isEmptyString(PreferencesUtil.getStringPreferences(getActivity(), buttonBean.getPkAblId()+"Id"))
					            		&&PreferencesUtil.getStringPreferences(getActivity(), buttonBean.getPkAblId()+"Id")
					            		.equals(buttonBean.getPkAblId())
					            		&&!Tools.isEmptyString(PreferencesUtil.getStringPreferences(getActivity(), buttonBean.getPkAblId()+"Update"))
					            		&&PreferencesUtil.getStringPreferences(getActivity(), buttonBean.getPkAblId()+"Update")
					            		.equals(buttonBean.getAblUpdatedate())
					            		){

				    					new FunctionPictureUpload(getActivity(),buttonBean.getButtonPicUrl(),
				    							buttonBean.getAblSort(), buttonBean.getPkAblId(),
				    							buttonBean.getAblName().substring(0, 
				    							buttonBean.getAblName().length()>4?4:buttonBean.getAblName().length())
				    							,buttonBean.getAblUpdatedate()
				    							)
				    					.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,"");
								}
								
								i++;
								finalDb.save(buttonBean);
							}
						} catch (Exception e) {
						}
					}
					
					functionbutton();
				}else {
					initbuttondata();
				}
				functionbutton();
				
			}
			
			

			
		}
	}


	private void informationface() {
		if(beans.size()>0){
			if(informationAdapter == null){
				informationAdapter = new InformationAdapter(getActivity(), beans);
				iv_information_listview.setAdapter(informationAdapter);
				iv_information_listview.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent,
							View view, int position, long id) {
								Intent intent = new Intent();
								intent.setClass(getActivity(),
										CommonH5Activity.class);
								intent.putExtra("h5title", "资讯");
								intent.putExtra("h5url", beans.get(position)
										.getNeiUrl());
								getActivity().startActivity(intent);

					}
				});
			}
			informationAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onFaile(String sign, Bundle bundle) {
		pScrollView.onRefreshComplete();
		cancelPD();
		showToast(bundle.getString(Protocol.MSG));
		if(bundle != null ){
			if (sign.equals(Protocol.IMAGE_CAROUSEL)) {
				if (carouselBeans.size()==0) {				
					NoNetConnectionInitBanner();
				}
//				LogUtil.i("cm_netPost","content//////////////////////////////" + Protocol.IMAGE_CAROUSEL);
			}else if (sign.equals(Protocol.TEXT_CAROUSEL)) {
				if (textCarouselBeans.size()==0) {
					ll_information.setVisibility(View.GONE);
				}
//				LogUtil.i("cm_netPost","content*******************************" + Protocol.TEXT_CAROUSEL);
			}else if (sign.equals(Protocol.INFORMATION_LIST)) {
				if (beans.size() == 0) {
					tv_information_title.setVisibility(View.GONE);
				}
//				LogUtil.i("cm_netPost","content-----------------------------------------" + Protocol.INFORMATION_LIST);
			}else if (sign.equals(Protocol.FUNCTION_BUTTON)) {
				if (buttonBeans.size()==0 && isfirst2) {					
					initbuttondata();
					functionbutton();
				}
//				LogUtil.i("cm_netPost","content+++++++++++++++++++++++++++++"+"    "+buttonBeans.size()+"         " + Protocol.FUNCTION_BUTTON);
			}
			
		
		}
		
	}
	private ImageCycleViewListener mAdCycleViewListener = new ImageCycleViewListener() {

		@Override
		public void onImageClick(ImageCarouselBean bean, int position, View imageView) {
			position = position - 1; 
			if (cycleViewPager.isCycle()&&!Tools.isEmptyString(carouselBeans.get(position).getBlUrl())) {
					MobclickAgent.onEvent(mContext, "zhuye_banner_"+(position+1));
					Intent intent = new Intent();
					intent.setClass(getActivity(), CommonH5Activity.class);
					intent.putExtra("h5url", carouselBeans.get(position).getBlUrl());
					intent.putExtra("h5title", "  ");
					getActivity().startActivity(intent);
			}

		}

	};
	
	
	@SuppressLint("NewApi")
	private void initialize() {
		if (views.size()>0) {			
			views.clear();
		}
		if (carouselBeans.size()!=1) {
			// 将最后一个ImageView添加进来
			try {
				carouselBeans.get(carouselBeans.size() - 1).getBannerPicUrl();
				views.add(ViewFactory.getImageView(getActivity(), carouselBeans.get(carouselBeans.size() - 1).getBannerPicUrl()));	
			} catch (Exception e) {
				views.add(ViewFactory.getImageView(getActivity(), ""));
			}

		}else {
			// 将最后一个ImageView添加进来
			try {
				carouselBeans.get(0).getBannerPicUrl();
				views.add(ViewFactory.getImageView(getActivity(), carouselBeans.get(0).getBannerPicUrl()));
			} catch (Exception e) {
				views.add(ViewFactory.getImageView(getActivity(), ""));
			}
		}
		for (int i = 0; i < carouselBeans.size(); i++) {			
			try {
				carouselBeans.get(i).getBannerPicUrl();
				views.add(ViewFactory.getImageView(getActivity(), carouselBeans.get(i).getBannerPicUrl()));
			} catch (Exception e) {
				views.add(ViewFactory.getImageView(getActivity(), ""));
			}
		}
		
		if (carouselBeans.size()!=1) {

			// 将第一个ImageView添加进来
			try {
				carouselBeans.get(0).getBannerPicUrl();
				views.add(ViewFactory.getImageView(getActivity(), carouselBeans.get(0).getBannerPicUrl()));
			} catch (Exception e) {
				views.add(ViewFactory.getImageView(getActivity(), ""));
			}
			
			// 设置循环，在调用setData方法前调用
			cycleViewPager.setCycle(true);
			// 设置轮播
			cycleViewPager.setWheel(true);
		}else {
			// 将最后一个ImageView添加进来
			try {
				carouselBeans.get(0).getBannerPicUrl();
				views.add(ViewFactory.getImageView(getActivity(), carouselBeans.get(0).getBannerPicUrl()));
			} catch (Exception e) {
				views.add(ViewFactory.getImageView(getActivity(), ""));
			}
			// 设置循环，在调用setData方法前调用
			cycleViewPager.setCycle(false);
			// 设置轮播
			cycleViewPager.setWheel(false);
		}
		
		// 在加载数据前设置是否循环
		cycleViewPager.setData(views, carouselBeans, mAdCycleViewListener);

	    // 设置轮播时间，默认5000ms
//		cycleViewPager.setTime(2000);
		//设置圆点指示图标组居中显示，默认靠右
		if (carouselBeans.size()!=1) {			
			cycleViewPager.setIndicatorCenter(true); 
			cycleViewPager.setScrollable(true);
		}else {
			cycleViewPager.setIndicatorCenter(false);
			cycleViewPager.setScrollable(false);
		}
	}
	
	private void NoNetConnectionInitBanner() {

		if (views.size()>0) {			
			views.clear();
		}
		if (carouselBeans.size()>0) {
			carouselBeans.clear();
		}
		ImageCarouselBean carouselBean = new ImageCarouselBean();
		carouselBean.setBlUrl("     ");
		carouselBeans.add(carouselBean);

		views.add(ViewFactory.getImageView(getActivity(), ""));
		views.add(ViewFactory.getImageView(getActivity(), ""));
		views.add(ViewFactory.getImageView(getActivity(), ""));
		// 设置循环，在调用setData方法前调用
		cycleViewPager.setCycle(false);
		// 设置轮播
		cycleViewPager.setWheel(false);

		// 在加载数据前设置是否循环
		cycleViewPager.setData(views, carouselBeans, mAdCycleViewListener);
		cycleViewPager.setIndicatorCenter(false);
		cycleViewPager.setScrollable(false);
		
	}
	
	private void functionbutton(){
		if (buttonBeans2.size()>0) {
			buttonBeans2.clear();
		}
		ComparatorListFunctionButton listFunctionButton = new ComparatorListFunctionButton();
		Collections.sort(buttonBeans, listFunctionButton);
		if (buttonBeans.size()<8) {
			buttonBeans2.addAll(buttonBeans);
		}else{
			for (int i = 0; i<7; i++) {
				buttonBean = buttonBeans.get(i);
				buttonBeans2.add(buttonBean);
			}
			FunctionButtonBean functionButtonBean = new FunctionButtonBean();
			functionButtonBean.setAblName("全部");//按钮名字
			functionButtonBean.setPkAblId("0");//图片id
			functionButtonBean.setAblAction("5");//跳转位置
			functionButtonBean.setAblSort("8");//按钮所在位置
			functionButtonBean.setButtonPicUrl("url");
			buttonBeans2.add(functionButtonBean);

		}
		if (buttonBeans.size()==0) {
			gridView.setVisibility(View.GONE);
		}else {
			gridView.setVisibility(View.VISIBLE);
		}
		
        FunctionButtonAdapter adapter = new FunctionButtonAdapter(getActivity(), buttonBeans2);
        gridView.setAdapter(adapter);
        //功能按钮点击事件
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {  
            @Override  
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {  
				try {
					redirect(position);
				} catch (Exception e) {
				}
            }

			 
        });  
        adapter.notifyDataSetChanged();
	}
	private void redirect(int position) {
		Intent intent = new Intent();
		if (position==7) {
			MobclickAgent.onEvent(mContext, "zhuye_quanbu");
			intent.putExtra("funtionbutton", buttonBeans);
			intent.setClass(getActivity(), MoreActivity.class);//更多
			getActivity().startActivity(intent);
		}else if (buttonBeans.get(position).getAblAction().equals("1")) {
			MobclickAgent.onEvent(mContext, "zhuye_saomachongdian");
			if(!Tools.isEmptyString(pkuserId)){
				intent.setClass(getActivity(), CaptureActivity.class);
			}else {
				intent.setClass(getActivity(), LoginActivity.class);
				intent.putExtra("source_inteface", "captureactivity");
			}
			getActivity().startActivity(intent);
		}else if (buttonBeans.get(position).getAblAction().equals("2")) {
			MobclickAgent.onEvent(mContext, "zhuye_chongdianditu");
			mapcallback.homemap();
		}else if (buttonBeans.get(position).getAblAction().equals("3")) {
			MobclickAgent.onEvent(mContext, "zhuye_wodechongdian");
			intent.putExtra("defaultpage", "pagedun");
			if(Tools.isEmptyString(pkuserId)){
				intent.putExtra("source_inteface", "myperson_record");
				intent.setClass(getActivity(), LoginActivity.class); 
			}else {
				intent.setClass(getActivity(), MyChargeActivity.class);
			}
			getActivity().startActivity(intent);
		}else if (buttonBeans.get(position).getAblAction().equals("4")) {
			MobclickAgent.onEvent(mContext, "zhuye_chongzhi");
			if(Tools.isEmptyString(pkuserId)){
				intent.putExtra("source_inteface", "myperson_recharge");
				intent.setClass(getActivity(), LoginActivity.class);
			}else {
				intent.setClass(getActivity(), RechargeActivity.class);
			}
			getActivity().startActivity(intent);
		}else {
			intent.putExtra("h5url", buttonBeans.get(position).getAblUrl());
			intent.putExtra("h5title", buttonBeans.get(position).getAblName());
			intent.setClass(getActivity(), CommonH5Activity.class);
			getActivity().startActivity(intent);
		}
		
	} 

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		if (isNetConnection()) {
			isRefresh = true;
			currentPage = 1;
			GetDataPost.getInstance(getActivity()).getMyCarousel(handler, provincecode, citycode);
			GetDataPost.getInstance(getActivity()).getMyTextCarousel(handler, provincecode, citycode);
			GetDataPost.getInstance(getActivity()).getMyFunctionButton(handler);
			GetDataPost.getInstance(getActivity()).getMyInformationList(handler,String.valueOf(currentPage), pageNum);
			if (!Tools.isEmptyString(pkuserId)) {
				GetDataPost.getInstance(getActivity()).getChargeInfoList(handler,pkuserId, "2", "1", "1");
			}
			// 下拉刷新
			String label = (String) DateFormat.format("yyyy-MM-dd HH:mm:ss", System.currentTimeMillis()); 
			refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最后更新："+label);
		}else {			
			pScrollView.onRefreshComplete();
		}

	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		// 上拉加载
		isRefresh = false;
		//获取下一页数据
		currentPage ++;
		if(isNetConnection()){
			GetDataPost.getInstance(getActivity()).getMyInformationList(handler,String.valueOf(currentPage), pageNum);
		}else {
			pScrollView.onRefreshComplete();
			showToast("网络不稳，请稍后再试");
		}
	}
	@Override
	public void onPullUpToRefreshNoData(PullToRefreshBase<ScrollView> refreshView) {
		showToast("暂无更多数据");
	}


	class ComparatorListFunctionButton implements Comparator {
		private int flag = 0;
		@Override
		public int compare(Object lhs, Object rhs) {
			FunctionButtonBean bean1 = (FunctionButtonBean) lhs;
			FunctionButtonBean bean2 = (FunctionButtonBean) rhs;
			String Place1 = bean1.getAblSort();
			String Place2 = bean2.getAblSort();

			if(Tools.isEmptyString(Place1)){
				Place1 = "0.00";
			}
			if(Tools.isEmptyString(Place2)){
				Place2 = "0.00";
			}

			flag = Float.valueOf(Place1).compareTo(
						Float.valueOf(Place2));
			return flag;
		}

	}
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}
	
	
	/**
	 * 暂停定位
	 */
	@SuppressWarnings("deprecation")
	private void stopLocation() {
	    if (mAMapLocationManager != null) {
	    	LogUtil.i("cm_location","停止 定位" );
	    	mAMapLocationManager.removeUpdates(this);
	    	mAMapLocationManager.destory();
	    }
	    mAMapLocationManager = null;
	}
	/**
	 * 重新启动定位
	 * 
	 */
	private void startLocation(){
		if (mAMapLocationManager == null) {
			LogUtil.i("cm_location","重新定位" );
			mAMapLocationManager = LocationManagerProxy
					.getInstance(getActivity().getApplicationContext());
			mAMapLocationManager.requestLocationData(
					LocationProviderProxy.AMapNetwork, 60 * 1000 * 5 , 20, this);
			mAMapLocationManager.setGpsEnable(true);
		}
	}
	
	@Override
	public void onDestroy() {
		mapView.onDestroy();
		deactivate();
		getActivity().unregisterReceiver(mBroadcastReceiver);
		getActivity().unregisterReceiver(broadcastReceiver);
		super.onDestroy();
	}
	/**
	 * 无网络时初始化功能按钮数据
	 */
	private void initbuttondata() {		
		if (buttonBeans.size()>0) {
			buttonBeans.clear();
		}
		
		
		buttonBeans = (ArrayList<FunctionButtonBean>) finalDb.findAll(FunctionButtonBean.class);
		if (buttonBeans.size()>0) {
		}else {
			FunctionButtonBean buttonBean2 = new FunctionButtonBean();

			buttonBean2.setAblName("地图找桩");// 按钮名字
			buttonBean2.setPkAblId("0");// 图片id
			buttonBean2.setAblAction("2");// 跳转位置
			buttonBean2.setAblSort("2");// 按钮所在位置
			buttonBean2.setButtonPicUrl("url");
			buttonBeans.add(buttonBean2);
			
			FunctionButtonBean buttonBean = new FunctionButtonBean();

			buttonBean.setAblName("扫码充电");// 按钮名字
			buttonBean.setPkAblId("0");// 图片id
			buttonBean.setAblAction("1");// 跳转位置
			buttonBean.setAblSort("1");// 按钮所在位置
			buttonBean.setButtonPicUrl("url");
			buttonBeans.add(buttonBean);
			
			
			
			FunctionButtonBean buttonBean3 = new FunctionButtonBean();

			buttonBean3.setAblName("我的充电");// 按钮名字
			buttonBean3.setPkAblId("0");// 图片id
			buttonBean3.setAblAction("3");// 跳转位置
			buttonBean3.setAblSort("3");// 按钮所在位置
			buttonBean3.setButtonPicUrl("url");
			buttonBeans.add(buttonBean3);
			
			FunctionButtonBean buttonBean4 = new FunctionButtonBean();

			buttonBean4.setAblName("余额充值");// 按钮名字
			buttonBean4.setPkAblId("0");// 图片id
			buttonBean4.setAblAction("4");// 跳转位置
			buttonBean4.setAblSort("4");// 按钮所在位置
			buttonBean4.setButtonPicUrl("url");
			buttonBeans.add(buttonBean4);
		}
		
	}
	public static void setcallback(HomeMapCallback callback){
		mapcallback = callback;
	}
	public interface HomeMapCallback{
		void homemap();
	}

	@Override
	public void onLocationChanged(Location location) { }

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) { }

	@Override
	public void onProviderEnabled(String provider) { }

	@Override
	public void onProviderDisabled(String provider) { }

	@Override
	public void onLocationChanged(AMapLocation amapLocation) { 
		if (!hasLocationSuccess && amapLocation != null) {
			if (amapLocation != null
					&& amapLocation.getAMapException().getErrorCode() == 0) {
				List<AreaBean> adcodeBeans = null;
				String adcode = amapLocation.getAdCode();
				if (finalDb.tabIsExistByName("tb_imagecarousel_data")) {					
					adcodeBeans = finalDb.findAllByWhere(AreaBean.class, "AREA_ID="+adcode);
				}
				if (adcodeBeans != null && adcodeBeans.size()!=0) {					
					AreaBean bean = adcodeBeans.get(0);
					provincecode = bean.getPROVINCE_ID();
					citycode = bean.getCITY_ID();
					countycode = bean.getAREA_ID();
					areaName = bean.getAREA_NAME();
				}
				 GetDataPost.getInstance(getActivity()).getMyCarousel(handler, provincecode, citycode);
				 GetDataPost.getInstance(getActivity()).getMyTextCarousel(handler, provincecode, citycode);
				 if (!Tools.isEmptyString(provincecode)) {
					 PreferencesUtil.setPreferences(getActivity(), "provincecode", provincecode);
				 	}
				 if (!Tools.isEmptyString(citycode)) {
					 PreferencesUtil.setPreferences(getActivity(), "citycode", citycode);						
					}
				 PreferencesUtil.setPreferences(getActivity(), "areacode", adcode);
				 PreferencesUtil.setPreferences(getActivity(), "areaName", areaName);
				 deactivate();
			} else {
				 GetDataPost.getInstance(getActivity()).getMyCarousel(handler, provincecode, citycode);
				 GetDataPost.getInstance(getActivity()).getMyTextCarousel(handler, provincecode, citycode);
				Toast.makeText(getActivity(), "定位失败,请打开gps", 0).show();
			}
		}
	}

	@Override
	public void activate(OnLocationChangedListener listener) {
		if (mAMapLocationManager == null) {
			mAMapLocationManager = LocationManagerProxy.getInstance(getActivity());
			// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
			// 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用removeUpdates()方法来取消定位请求
			// 在定位结束后，在合适的生命周期调用destroy()方法
			// 其中如果间隔时间为-1，则定位只定一次
			// 在单次定位情况下，定位无论成功与否，都无需调用removeUpdates()方法移除请求，定位sdk内部会移除
			mAMapLocationManager.requestLocationData(
					LocationProviderProxy.AMapNetwork, 60 * 1000 * 5, 20, this);
			mAMapLocationManager.setGpsEnable(true);
		}
	}

	/**
	 * 停止定位
	 */
	@Override
	public void deactivate() {
		if (mAMapLocationManager != null) {
			mAMapLocationManager.removeUpdates(this);
			mAMapLocationManager.destroy();
		}
		mAMapLocationManager = null;
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.help:
//			Intent intents = new Intent();
//			intents.setClass(getActivity(), ShareDetailActivity.class);
//			getActivity().startActivity(intents);
			MobclickAgent.onEvent(mContext, "zhuye_bangzhu");
			Intent Instruction = new Intent();
			Instruction.setClass(getActivity(), CommonH5Activity.class);
			Instruction.putExtra("h5url", Protocol.INSTRUCTION);
			Instruction.putExtra("h5title", "操作说明");
			getActivity().startActivity(Instruction);
			break;
		case R.id.information:
			try {
				Intent intent = new Intent();
				intent.putExtra("malfunction", textCarouselBeans.get(number%textCarouselBeans.size()));
				intent.setClass(getActivity(), MalfunctionActivity.class);
				getActivity().startActivity(intent);				
			} catch (Exception e) {
			}
			break;
			
		case R.id.fragment_myperson_charging:
			MobclickAgent.onEvent(mContext, "zhuye_zhengzaichongdian");
			//充电中
			showPD("正在获取充电信息...");
			mTcpSocketManager = TCPSocketManager.getInstance(getActivity());
			mTcpSocketManager.setTcpCallback(this);
			if(!mTcpSocketManager.hasTcpConnection()&&!Tools.isEmptyString(pilenum)
					&& !Tools.isEmptyString(headnum)){
				mTcpSocketManager.conn(pilenum, 
						Byte.parseByte(headnum));
			}else {
				cancelPD();
				showErrorCode(110);
			}
		default:
			break;
		}
		
	}

	
	/*
	 * 充电
	 */
	
	public void registerBoradcastReceiver(){  
        IntentFilter myIntentFilter = new IntentFilter();  
        myIntentFilter.addAction(BroadcastUtil.BROADCAST_Modify_UserInfo);  
        myIntentFilter.addAction(BroadcastUtil.BROADCAST_LOGIN); 
        myIntentFilter.addAction(BroadcastUtil.BROADCAST_Charge_Ing);  
        myIntentFilter.addAction(BroadcastUtil.BROADCAST_Charge_CANCLE);  
        //注册广播        
        getActivity().registerReceiver(mBroadcastReceiver, myIntentFilter);  
        IntentFilter myIntentFilter2 = new IntentFilter();  
        myIntentFilter2.addAction(ConnectivityManager.CONNECTIVITY_ACTION);  
        getActivity().registerReceiver(broadcastReceiver, myIntentFilter2);
    } 
	NetWorkChangeBroadcastReceiver broadcastReceiver = new NetWorkChangeBroadcastReceiver();
	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver(){  
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			 if(action.equals(BroadcastUtil.BROADCAST_Modify_UserInfo)){  
					 GetDataPost.getInstance(getActivity()).getUserInfo(handler, pkuserId);
	         } else if( action.equals(BroadcastUtil.BROADCAST_LOGIN)){
	        	 String pkid = intent.getStringExtra("pkUserId");
	        	 GetDataPost.getInstance(getActivity()).getUserInfo(handler, pkid);
	         }  else if(action.equals(BroadcastUtil.BROADCAST_Charge_CANCLE)){
	        	 //充电结束
	        	 PreferencesUtil.setPreferences(getActivity(),"chargepilenum","");
	        	 PreferencesUtil.setPreferences(getActivity(),"chargeheadnum","");
	        	 iv_charging_animation.setVisibility(View.GONE);
//	        	 fl_charging_confirm.setVisibility(View.GONE);
	         } else if(action.equals(BroadcastUtil.BROADCAST_Charge_Ing)){
	        	 //充电进行中
	        	 pilenum = intent.getStringExtra("chargepilenum");
	        	 headnum = intent.getStringExtra("chargeheadnum");
	        	 PreferencesUtil.setPreferences(getActivity(),"chargepilenum",pilenum);
	        	 PreferencesUtil.setPreferences(getActivity(),"chargeheadnum",headnum);
	        	 iv_charging_animation.setVisibility(View.VISIBLE);
//	        	 fl_charging_confirm.setVisibility(View.VISIBLE);
//				 animation.setDuration(3000);
//				 animation.setRepeatCount(-1);
//				 iv_charging_animation.startAnimation(animation);
	        	  
	         }
		}  
		
    };
    
    
    @Override
	public void handleTcpPacket(final ByteArrayInputStream result) {
		//收到实时数据，进入实时数据界面
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						cancelPD();
					    try {
							StreamUtil.readByte(result);//int reason = 
							short cmdtype = StreamUtil.readShort(result);
							switch (cmdtype) {
							case SocketConstant.CMD_TYPE_REAL_DATA:
								handleRealDataEvent(result);
								break;
							case SocketConstant.CMD_TYPE_CONNECT:
								handleConnectEvent(result);
								break;
							default:
								break;
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});
			}
			//处理socket-实时数据事件
			private void handleRealDataEvent(ByteArrayInputStream result) throws IOException{
				int state = StreamUtil.readByte(result);
				short chargeTime = StreamUtil.readShort(result);
				StreamUtil.readShort(result);//short dianya = 
				StreamUtil.readShort(result);//short dianliu = 
				int diandu = StreamUtil.readInt(result);
				short feilv = StreamUtil.readShort(result);
				int yuchong = StreamUtil.readInt(result);
				int yichong = StreamUtil.readInt(result);
				int soc = StreamUtil.readByte(result);
				StreamUtil.readInt(result);//int fushu = 
				StreamUtil.readInt(result);//int gaojing = 
				Intent realIn = new Intent(getActivity(),
						RealTimeChargeActivity.class);
				realIn.putExtra("state", state);
				realIn.putExtra("chargeTime", chargeTime);
				realIn.putExtra("diandu", diandu);
				realIn.putExtra("feilv", feilv);
				realIn.putExtra("yuchong", yuchong);
				realIn.putExtra("yichong", yichong);
				realIn.putExtra("soc", soc);
				realIn.putExtra("interfacefrom", "home");
				//mTcpSocketManager.close();
				startActivity(realIn);
			}
			//处理socket-连接充电桩事件
			private void handleConnectEvent(ByteArrayInputStream result) throws IOException {
				int successflag = StreamUtil.readByte(result);
				short errorcode = StreamUtil.readShort(result);
				int headState = StreamUtil.readByte(result);
				int type = 1;
				 try {
					type = StreamUtil.readByte(result);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mTcpSocketManager.setPileType(type);
				switch (successflag) {
				case 1:
					//连接成功
					if(3 == headState || 0 == headState){
						 showToast("充电已结束");
						 PreferencesUtil.setPreferences(getActivity(),"chargepilenum","");
			        	 PreferencesUtil.setPreferences(getActivity(),"chargeheadnum","");
			        	 iv_charging_animation.setVisibility(View.GONE);
//			        	 fl_charging_confirm.setVisibility(View.GONE);
			        	 mTcpSocketManager.close();
					}
					break;
				case 0:
					showErrorCode(errorcode);
					mTcpSocketManager.close();
					break;
				default:
					break;
				}
			}
			
			@Override
			public void handleSocketException() {
				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						cancelPD();
						showErrorCode(110);
					}
				});
			}
	/**
	 * 网络变化
	 */

			@Override
			public void homenetworkchange() {
				LogUtil.i("cm_netPost","--------------------------88888888==" );
				if(isNetConnection()&&isfirst){
					startLocation();//重新启动定位
					isRefresh = true;
					currentPage = 1;
					pageNum = "10";
					if (isNetConnection()) {
						
						GetDataPost.getInstance(getActivity()).getMyInformationList(handler,String.valueOf(currentPage), pageNum);
					}
				}
			}
			@Override
			public void onHiddenChanged(boolean hidden) {
				super.onHiddenChanged(hidden);
				if(!hidden){
					//重新加载数据
					isCurrentFragment = true;
					onResume();
				}else {
					isCurrentFragment = false;
					onPause();
				}
			}
			@Override
			public void onPause() {
				super.onPause();
				if (isCurrentFragment) {
					return;
				}
				mapView.onPause();
				//友盟统计
//				MobclickAgent.onPause(mContext);
				
//				MobclickAgent.onPageEnd(mPageName);
				stopLocation();//停止定位
			}
			@Override
			public void onResume() {
				super.onResume();
				if (!isCurrentFragment) {
					return;
				}
				mapView.onResume();
				//友盟统计
//				MobclickAgent.onResume(mContext);
//				MobclickAgent.onPageStart(mPageName);
				

				pkuserId = PreferencesUtil.getStringPreferences(getActivity(), "pkUserinfo");
//				if(Tools.isEmptyString(pkuserId)){//未登录
//					iv_charging_animation.setVisibility(View.GONE);
////					fl_charging_confirm.setVisibility(View.GONE);
//				}else {
//					if (isfirstcharge) {
//						GetDataPost.getInstance(getActivity()).getChargeInfoList(handler,pkuserId, "2", "1", "1");
//					}
//					//充电动画
//					pilenum = PreferencesUtil.getStringPreferences(getActivity(),"chargepilenum");
//					headnum = PreferencesUtil.getStringPreferences(getActivity(),"chargeheadnum");
//					if(!Tools.isEmptyString(pilenum) && !Tools.isEmptyString(headnum)){
//						iv_charging_animation.setVisibility(View.VISIBLE);
////						fl_charging_confirm.setVisibility(View.VISIBLE);
////						animation.setDuration(3000);
////						animation.setRepeatCount(-1);
////						iv_charging_animation.startAnimation(animation);
//					}else {
//						iv_charging_animation.setVisibility(View.GONE);
////						fl_charging_confirm.setVisibility(View.GONE);
//					}
//				}
				startLocation();//重新启动定位
			}
}
