package com.baidumap.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.location.ad;
import com.baidumap.activity.R;
import com.baidumap.adapter.RentListAdapter;
import com.baidumap.api.LBSCloudSearch;
import com.baidumap.fragment.FragmentRentList;
import com.baidumap.fragment.FragmentRentMap;
import com.baidumap.model.ContentModel;
import com.baidumap.model.RentInfoModel;
import com.baidumap.util.Constant;
import com.baidumap.util.DataUtil;
import com.baidumap.util.Utility;
import com.baidumap.views.ExpandTabView;
import com.baidumap.views.ViewLeft;
import com.baidumap.views.ViewMiddle;
import com.baidumap.views.xlistview.XListView;
import com.baidumap.views.xlistview.XListView.IXListViewListener;

import android.content.res.Resources.NotFoundException;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MyLBSRentActivity extends FragmentActivity {
	private ExpandTabView expandTabView;
	private ArrayList<View> viewList = new ArrayList<View>();
	
	private ViewMiddle view_area;
	private ViewLeft view_price;
	private ViewLeft view_distance;
	private ViewLeft view_rent_type;
	
	private ArrayList<String> areaGroupList = new ArrayList<String>();
	private SparseArray<LinkedList<String>> areaChildList = new SparseArray<LinkedList<String>>();
	private ArrayList<LinkedList<String>> childItemList = new ArrayList<LinkedList<String>>();
	
	private ArrayList<String> priceList = new ArrayList<String>();
	private ArrayList<String> distanceList = new ArrayList<String>();
	private ArrayList<String> typeList = new ArrayList<String>();
	
	ArrayList<String> titleList;
	
	private LinearLayout ll_main_content;	
	private Button btn_list, btn_map;
	private ProgressBar progressBar;
	
//	private ListView lv_rent_list;
	
	private XListView lv_rent_xllist;
	private List<RentInfoModel> rentList;
	private RentListAdapter adapter;
	private int pageCount = 1;
	
	double latitude;
	double longtitude;
	String address;
	String city;
	
	//检索参数
	HashMap<String, String> map;
	
	private String area, price, distance, type;
	
	private boolean initSearchFlag = false;
	
	private Handler mHandler;
	
	private FragmentRentList fragmentRentList;
	private FragmentRentMap fragmentRentMap;
	
	
	/*
	 * 处理网络请求
	 */
/*	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (progressBar != null) {
				progressBar.setVisibility(View.GONE);
			}
			switch (msg.what) {
			case Constant.MSG_NET_TIMEOUT:
				break;
			case Constant.MSG_NET_STATUS_ERROR:
				break;
			case Constant.MSG_NET_SUCC:
				initSearchFlag = true;
				String result = msg.obj.toString();
				try {
					JSONObject json = new JSONObject(result);
					Log.e("json", json.toString());
					parser(json);

					if (rentList != null) {
						adapter = new RentListAdapter(MyLBSRentActivity.this, rentList);
						//lv_rent_list.setAdapter(adapter);						
						lv_rent_xllist.setAdapter(adapter);
						lv_rent_xllist.setPullLoadEnable(true);
						
						lv_rent_xllist.setXListViewListener(new IXListViewListener() {
							
							@Override
							public void onRefresh() {
								search();
								
//								for(RentInfoModel transListModels1: rentList){
//			                        addTransListModel(transListModels1);
//			                    }
			                    adapter.notifyDataSetChanged();
			                    lv_rent_xllist.stopRefresh();					
							}
							
							@Override
							public void onLoadMore() {
								if (rentList != null && rentList.size() < 10) {
									lv_rent_xllist.setPullLoadEnable(false);					
								} else {						
									
									pageCount ++;
									// search type 为 -1，将保持当前的搜索类型
									map.put("page_index", pageCount + "");  //(rentList.size()/10 + 1)
									LBSCloudSearch.request(-1, setRequestHttpParams(), mHandler, MyApplication.setNetworkType());
									
//			                        for(RentInfoModel transListModels1: rentList){
//			                            addTransListModel(transListModels1);
//			                        }
			                        adapter.notifyDataSetChanged();
			                        lv_rent_xllist.stopLoadMore();
				
								}							
							}
						}, lv_rent_xllist.getId());    
					}                                
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;

			}
		}
	};
	*/
	
/*	//版本二
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (progressBar != null) {
				progressBar.setVisibility(View.GONE);
			}
			switch (msg.what) {
			case Constant.MSG_NET_TIMEOUT:
				break;
			case Constant.MSG_NET_STATUS_ERROR:
				break;
			case Constant.MSG_NET_SUCC:
				initSearchFlag = true;
				String result = msg.obj.toString();
				try {
					JSONObject json = new JSONObject(result);
					Log.e("json", json.toString());
					parser(json);

					if (rentList != null) {
						showContent();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;

			}
		}
	};   */
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lbs_rent);
		setTitle("新房");
		//initLeftButton();
		
		if (getIntent() != null) {
			latitude = getIntent().getDoubleExtra("latitude", 0);
			longtitude = getIntent().getDoubleExtra("longtitude", 0);
			city = getIntent().getStringExtra("city");
		}
		
		initView();
		initData();
		
		// 发起搜索请求
		search();
	}
	
	protected void showContent() {
		fragmentRentList = new FragmentRentList();
		mHandler = fragmentRentList.getmHandler();
		Bundle b = new Bundle();
		b.putDouble("latitude", latitude);
		b.putDouble("longtitude", longtitude);
		fragmentRentList.setArguments(b);
		getSupportFragmentManager().beginTransaction().replace(R.id.ll_main_content, fragmentRentList, null).commit();
	}

	
	protected void initView() {
		
		//填充ExpandableView数据源
		DataUtil dataUtil = new DataUtil(areaGroupList, areaChildList, priceList, distanceList, typeList);
		dataUtil.initSeaarchData();
				
		expandTabView = (ExpandTabView)findViewById(R.id.expandTabView);
		view_area = new ViewMiddle(this, areaGroupList, areaChildList);
		view_price = new ViewLeft(this, priceList);
		view_distance = new ViewLeft(this, distanceList);
		view_rent_type = new ViewLeft(this, typeList);
		
		ll_main_content = (LinearLayout)findViewById(R.id.ll_main_content);  //替换Fragment
		showContent();
		
		btn_list = (Button)findViewById(R.id.btn_list);
		btn_map = (Button)findViewById(R.id.btn_map);
		btn_list.setOnClickListener(l);
		btn_map.setOnClickListener(l);
		
//		progressBar = (ProgressBar)findViewById(R.id.progressBar);
		
//		lv_rent_list = (ListView)findViewById(R.id.lv_rent_list);
		
/*		lv_rent_xllist = (XListView)findViewById(R.id.lv_rent_xllist);
		
		lv_rent_xllist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int pos, long id) {
				
			}
		});  */
	}
	
	/**
     *  添加数据到数据源
     * @param temp
     */
   private void addTransListModel(RentInfoModel temp){
       if(temp == null){
           return;
       }
           for(RentInfoModel transListModel1: rentList){
               if(temp.getUid().equals(transListModel1.getUid())){
                   int index = rentList.indexOf(transListModel1);
                   rentList.remove(index);
                   rentList.add(index,temp);
                   return;
               }
           }
           rentList.add(temp);
   }
   
   OnClickListener l = new OnClickListener() {
	
		@Override
		public void onClick(View v) {
			Bundle b = new Bundle();
			b.putDouble("latitude", latitude);
			b.putDouble("longtitude", longtitude);
			if (v.getId() == R.id.btn_list) {	
				//search();
				fragmentRentList = new FragmentRentList();				
				fragmentRentList.setArguments(b);
				mHandler = fragmentRentList.getmHandler();
				getSupportFragmentManager().beginTransaction().replace(R.id.ll_main_content, fragmentRentList, null).commit();
			} else if (v.getId() == R.id.btn_map) {
				//search();
				fragmentRentMap = new FragmentRentMap();
				fragmentRentMap.setArguments(b);
				mHandler = fragmentRentMap.getmHandler();
				getSupportFragmentManager().beginTransaction().replace(R.id.ll_main_content, fragmentRentMap, null).commit();
			}
		}
   };

	protected void initData() {
		viewList.add(view_area);
		viewList.add(view_price);
		viewList.add(view_distance);
		viewList.add(view_rent_type);
		
		titleList = new ArrayList<String>();
		titleList.add("区域");
		titleList.add("价格");
		titleList.add("距离");
		titleList.add("类型");
		
		expandTabView.setValue(titleList, viewList);
		expandTabView.setTitle(titleList.get(0), 0);   //设置第一个tab的标题
		expandTabView.setTitle(titleList.get(1), 1);
		expandTabView.setTitle(titleList.get(2), 2);
		expandTabView.setTitle(titleList.get(3), 3);
		
		initListener();
	}
	
	private void initListener() {
		view_area.setOnSelectListener(new ViewMiddle.OnSelectListener() {

			@Override
			public void getValue(String groupText, String childText) {
				String showText = "";
				if (groupText.contains("不限")) {
					showText = titleList.get(0);
					area = city;
				} else {
					if (childText.contains("不限")) {
						childText = "";
					}
					showText = groupText + childText;
					onRefresh(view_area, showText);
					
					area = showText;
				}
				
				search();  //本地检索
			}
		});
		//选中某个item
		view_price.setOnSelectListener(new ViewLeft.OnSelectListener() {

			@Override
			public void getValue(String distance, String showText) {
				onRefresh(view_price, showText);   
				
				if ("不限".equals(showText)) {
					price = "";
				} else {
					price = showText;
				}
				
				search();   //本地检索
			}
		});
		view_distance.setOnSelectListener(new ViewLeft.OnSelectListener() {

			@Override
			public void getValue(String distance, String showText) {
				onRefresh(view_distance, showText);   
				
				if ("不限".equals(showText)) {
					distance = "";
				} else {
					distance = showText;
				}
				
				search();   //本地检索
			}
		});
		view_rent_type.setOnSelectListener(new ViewLeft.OnSelectListener() {

			@Override
			public void getValue(String distance, String showText) {
				onRefresh(view_rent_type, showText);  
				
				if ("不限".equals(showText)) {
					type = "";
				} else {
					type = showText;
				}
								
				search(); 
			}
		});
	}
	
	private void onRefresh(View view, String showText) {
		
		expandTabView.onPressBack();
		int position = getPositon(view);
		if (position >= 0 && !expandTabView.getTitle(position).equals(showText)) {
			if ("不限".equals(showText)) {
				expandTabView.setTitle(titleList.get(position), position);
			}
			expandTabView.setTitle(showText, position);
		}
		Toast.makeText(MyLBSRentActivity.this, showText , Toast.LENGTH_SHORT).show();

	}
	
	private int getPositon(View tView) {
		for (int i = 0; i < viewList.size(); i++) {
			if (viewList.get(i) == tView) {
				return i;
			}
		}
		return -1;
	}
	
	
	/*
	 * 云检索发起
	 */
	private void search() {
		search(LBSCloudSearch.SEARCH_TYPE_LOCAL);  //本地检索
	}
	
	/*
	 * 根据搜索类型发起检索
	 */
	private void search(int searchType){
		//progressBar.setVisibility(View.VISIBLE);
		
		// 云检索发起
		LBSCloudSearch.request(searchType, setRequestHttpParams(), mHandler, MyApplication.setNetworkType());
	}
	
	//设置云检索参数
	protected HashMap<String, String> setRequestHttpParams() {
		map = new HashMap<String, String>();
		String filter = "";
		try {
			map.put("mcode", "0A:56:DA:99:35:63:89:47:FA:05:8B:55:6A:81:2D:AB:5F:6F:26:51;com.baidumap.activity");  //必须要
			if (city != null) {
				map.put("region", URLEncoder.encode(city, "utf-8"));  //默认为全国范围，所以一般加上该参数最好
			}			
			if (!"".equals(area) && area != null) {
				map.put("q", URLEncoder.encode(area, "utf-8"));       //检索关键字
			}
			map.put("rent_type", "整租");
			filter += "rent_type:整租";
			if (!"".equals(filter)) {
				//map.put("filter", filter); 
			}			  
			Log.e("hh", map + "");
			
//			map.put("price", "150");
//			map.put("location", "114.059388,22.528244");              //检索的中心点
//			map.put("radius", "3000");
			
/*			if (!"".equals(price) && price != null) { 
				String p = Utility.getPrice(price);
				if (p.contains("-")) {
					String[] arr = p.split("-");
					filter += "price:" + arr[0] + "," + arr[1];
					map.put("price", arr[0] + "," + arr[1]);
				} else {
					map.put("price", p);   Log.e("hh", "filter = "+filter);
				}
			}
			
			
			if (!"".equals(distance) && distance != null) {    //该字段是根据当前距离和检索中心点的距离得到
				map.put("distance", distance);
			}
			
			if (!"".equals(type) && type != null) {
				map.put("rent_type", type);
			}
*/			

/*			
			if (!"".equals(price) && price != null) {
				if ("不限".equals(price)) {
					price = ""; 
				} else {
					String p = Utility.getPrice(price);
					if (p.contains("-")) {
						String[] arr = p.split("-");
						filter += "price:" + arr[0] + URLEncoder.encode(",", "utf-8") + arr[1];
						
					} else {
						filter += "price:" + p;   Log.e("hh", "filter = "+filter);
					}
				}								
			}
			
			
			if (!"".equals(distance) && distance != null) {    //该字段是根据当前距离和检索中心点的距离得到
				if ("不限".equals(distance)) {
					distance = "";
				} else {
					if (!"".equals(filter)) {
						filter += URLEncoder.encode("|", "utf-8") + "distance:" + distance;
					} else {
						filter += "distance:" + distance;
					}
				}
				
			}
			
			if (!"".equals(type) && type != null) {
				if ("不限".equals(type)) {
					type = "";
				} else {
					if (!"".equals(filter)) {
						filter += URLEncoder.encode("|", "utf-8") + "rent_type:" + type;
					} else {
						filter += "rent_type:" + type;
					}
				}				
			}
*/		
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (NotFoundException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	/*
	 * 解析返回数据
	 */
	private void parser(JSONObject json) {
//		DemoApplication app = (DemoApplication) getApplication();
//		List<ContentModel> list = app.getList();
		rentList = new ArrayList<RentInfoModel>();

		try {
			//app.getListActivity().totalItem = json.getInt("total");

			JSONArray jsonArray = json.getJSONArray("contents");      Log.e("hh", json.toString());
			if (jsonArray != null && jsonArray.length() <= 0) {
				Toast.makeText(this, "没有符合要求的数据", Toast.LENGTH_SHORT).show();
			} else {
				rentList.clear();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject2 = (JSONObject) jsonArray.opt(i);
					RentInfoModel content = new RentInfoModel();
					content.setUid(jsonObject2.getString("uid"));
					content.setTitle(jsonObject2.getString("title"));
					content.setAddress(jsonObject2.getString("address"));
					content.setPrice(jsonObject2.getString("price"));

					JSONArray locArray = jsonObject2.getJSONArray("location");
					double latitude = locArray.getDouble(1);
					double longitude = locArray.getDouble(0);
					content.setLatitude(latitude);
					content.setLongitude(longitude);

					float results[] = new float[1];

//					if (app.currlocation != null) {
						Location.distanceBetween(
								this.latitude,
								this.longtitude, latitude,
								longitude, results);
//					}
					content.setDistance((int) results[0]+"");					
					content.setRent_type(jsonObject2.getString("rent_type"));
					content.setImg_logo(jsonObject2.getString("img_url"));
					content.setHouse_area(jsonObject2.getString("house_area"));
					
					rentList.add(content);

//					JSONObject jsonObject3 = jsonObject2.getJSONObject("ext");
//					content.setPrice(jsonObject3.getString("dayprice"));
//					content.setImageurl(jsonObject3.getString("mainimage"));
//					content.setWebUrl(jsonObject3.getString("roomurl"));
//					int leasetype = jsonObject3.getInt("leasetype");
//					content.setPrice(jsonObject2.getString("dayprice"));
//					content.setImageurl(jsonObject2.getString("mainimage"));
//					content.setWebUrl(jsonObject2.getString("roomurl"));
//					int leasetype = jsonObject2.getInt("leasetype");
//					
//					if(leasetype > typeList.size() -1){
//						leasetype = 1;
//					}
//					content.setLeaseType(typeList.get(leasetype));
					//list.add(content);
					
				}
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected void initAreaList() {
		areaGroupList.add("不限");
		areaGroupList.add("福田");
		areaGroupList.add("南山");
		areaGroupList.add("宝安");
		
		LinkedList<String> linklist1 = new LinkedList<String>();
//		linklist1.add("");
//		LinkedList<String> linklist2 = new LinkedList<String>();
//		linklist2.add("1公里内");
//		linklist2.add("3公里内");
//		linklist2.add("5公里内");
		LinkedList<String> linklist3 = new LinkedList<String>();
		linklist3.add("不限");
		linklist3.add("皇岗");
		linklist3.add("景田");
		linklist3.add("梅林");
		linklist3.add("华强");
		LinkedList<String> linklist4 = new LinkedList<String>();
		linklist4.add("不限");
		linklist4.add("前海");
		linklist4.add("蛇口");
		LinkedList<String> linklist5 = new LinkedList<String>();
		linklist5.add("不限");
		linklist5.add("西乡");
		linklist5.add("宝安中心区");
		linklist5.add("新安");
		linklist5.add("福永");
		
		childItemList.add(linklist1);
//		childItemList.add(linklist2);
		childItemList.add(linklist3);
		childItemList.add(linklist4);
		childItemList.add(linklist5);
		
		for (int i = 0; i < areaGroupList.size(); i++) {
			areaChildList.put(i, childItemList.get(i));
		}
	}

	protected void initPriceList() {
		priceList.add("不限");
		priceList.add("100元以下");
		priceList.add("100-150元");
		priceList.add("150-200元");
		priceList.add("200-250元");
		priceList.add("250-300元");
		priceList.add("300元以上");
	}
	
	protected void initDistanceList() {
		distanceList.add("不限");
		distanceList.add("1000米");
		distanceList.add("2000米");
		distanceList.add("3000米");
	}
	
	protected void initTypeList() {
		typeList.add("不限");
		typeList.add("整租");
		typeList.add("单间出租");
		typeList.add("单间出租(隔断)");
		typeList.add("床位出租");
	}

}
