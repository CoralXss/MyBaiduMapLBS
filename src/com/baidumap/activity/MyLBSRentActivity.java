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
	
	//��������
	HashMap<String, String> map;
	
	private String area, price, distance, type;
	
	private boolean initSearchFlag = false;
	
	private Handler mHandler;
	
	private FragmentRentList fragmentRentList;
	private FragmentRentMap fragmentRentMap;
	
	
	/*
	 * ������������
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
									// search type Ϊ -1�������ֵ�ǰ����������
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
	
/*	//�汾��
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
		setTitle("�·�");
		//initLeftButton();
		
		if (getIntent() != null) {
			latitude = getIntent().getDoubleExtra("latitude", 0);
			longtitude = getIntent().getDoubleExtra("longtitude", 0);
			city = getIntent().getStringExtra("city");
		}
		
		initView();
		initData();
		
		// ������������
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
		
		//���ExpandableView����Դ
		DataUtil dataUtil = new DataUtil(areaGroupList, areaChildList, priceList, distanceList, typeList);
		dataUtil.initSeaarchData();
				
		expandTabView = (ExpandTabView)findViewById(R.id.expandTabView);
		view_area = new ViewMiddle(this, areaGroupList, areaChildList);
		view_price = new ViewLeft(this, priceList);
		view_distance = new ViewLeft(this, distanceList);
		view_rent_type = new ViewLeft(this, typeList);
		
		ll_main_content = (LinearLayout)findViewById(R.id.ll_main_content);  //�滻Fragment
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
     *  ������ݵ�����Դ
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
		titleList.add("����");
		titleList.add("�۸�");
		titleList.add("����");
		titleList.add("����");
		
		expandTabView.setValue(titleList, viewList);
		expandTabView.setTitle(titleList.get(0), 0);   //���õ�һ��tab�ı���
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
				if (groupText.contains("����")) {
					showText = titleList.get(0);
					area = city;
				} else {
					if (childText.contains("����")) {
						childText = "";
					}
					showText = groupText + childText;
					onRefresh(view_area, showText);
					
					area = showText;
				}
				
				search();  //���ؼ���
			}
		});
		//ѡ��ĳ��item
		view_price.setOnSelectListener(new ViewLeft.OnSelectListener() {

			@Override
			public void getValue(String distance, String showText) {
				onRefresh(view_price, showText);   
				
				if ("����".equals(showText)) {
					price = "";
				} else {
					price = showText;
				}
				
				search();   //���ؼ���
			}
		});
		view_distance.setOnSelectListener(new ViewLeft.OnSelectListener() {

			@Override
			public void getValue(String distance, String showText) {
				onRefresh(view_distance, showText);   
				
				if ("����".equals(showText)) {
					distance = "";
				} else {
					distance = showText;
				}
				
				search();   //���ؼ���
			}
		});
		view_rent_type.setOnSelectListener(new ViewLeft.OnSelectListener() {

			@Override
			public void getValue(String distance, String showText) {
				onRefresh(view_rent_type, showText);  
				
				if ("����".equals(showText)) {
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
			if ("����".equals(showText)) {
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
	 * �Ƽ�������
	 */
	private void search() {
		search(LBSCloudSearch.SEARCH_TYPE_LOCAL);  //���ؼ���
	}
	
	/*
	 * �����������ͷ������
	 */
	private void search(int searchType){
		//progressBar.setVisibility(View.VISIBLE);
		
		// �Ƽ�������
		LBSCloudSearch.request(searchType, setRequestHttpParams(), mHandler, MyApplication.setNetworkType());
	}
	
	//�����Ƽ�������
	protected HashMap<String, String> setRequestHttpParams() {
		map = new HashMap<String, String>();
		String filter = "";
		try {
			map.put("mcode", "0A:56:DA:99:35:63:89:47:FA:05:8B:55:6A:81:2D:AB:5F:6F:26:51;com.baidumap.activity");  //����Ҫ
			if (city != null) {
				map.put("region", URLEncoder.encode(city, "utf-8"));  //Ĭ��Ϊȫ����Χ������һ����ϸò������
			}			
			if (!"".equals(area) && area != null) {
				map.put("q", URLEncoder.encode(area, "utf-8"));       //�����ؼ���
			}
			map.put("rent_type", "����");
			filter += "rent_type:����";
			if (!"".equals(filter)) {
				//map.put("filter", filter); 
			}			  
			Log.e("hh", map + "");
			
//			map.put("price", "150");
//			map.put("location", "114.059388,22.528244");              //���������ĵ�
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
			
			
			if (!"".equals(distance) && distance != null) {    //���ֶ��Ǹ��ݵ�ǰ����ͼ������ĵ�ľ���õ�
				map.put("distance", distance);
			}
			
			if (!"".equals(type) && type != null) {
				map.put("rent_type", type);
			}
*/			

/*			
			if (!"".equals(price) && price != null) {
				if ("����".equals(price)) {
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
			
			
			if (!"".equals(distance) && distance != null) {    //���ֶ��Ǹ��ݵ�ǰ����ͼ������ĵ�ľ���õ�
				if ("����".equals(distance)) {
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
				if ("����".equals(type)) {
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
	 * ������������
	 */
	private void parser(JSONObject json) {
//		DemoApplication app = (DemoApplication) getApplication();
//		List<ContentModel> list = app.getList();
		rentList = new ArrayList<RentInfoModel>();

		try {
			//app.getListActivity().totalItem = json.getInt("total");

			JSONArray jsonArray = json.getJSONArray("contents");      Log.e("hh", json.toString());
			if (jsonArray != null && jsonArray.length() <= 0) {
				Toast.makeText(this, "û�з���Ҫ�������", Toast.LENGTH_SHORT).show();
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
		areaGroupList.add("����");
		areaGroupList.add("����");
		areaGroupList.add("��ɽ");
		areaGroupList.add("����");
		
		LinkedList<String> linklist1 = new LinkedList<String>();
//		linklist1.add("");
//		LinkedList<String> linklist2 = new LinkedList<String>();
//		linklist2.add("1������");
//		linklist2.add("3������");
//		linklist2.add("5������");
		LinkedList<String> linklist3 = new LinkedList<String>();
		linklist3.add("����");
		linklist3.add("�ʸ�");
		linklist3.add("����");
		linklist3.add("÷��");
		linklist3.add("��ǿ");
		LinkedList<String> linklist4 = new LinkedList<String>();
		linklist4.add("����");
		linklist4.add("ǰ��");
		linklist4.add("�߿�");
		LinkedList<String> linklist5 = new LinkedList<String>();
		linklist5.add("����");
		linklist5.add("����");
		linklist5.add("����������");
		linklist5.add("�°�");
		linklist5.add("����");
		
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
		priceList.add("����");
		priceList.add("100Ԫ����");
		priceList.add("100-150Ԫ");
		priceList.add("150-200Ԫ");
		priceList.add("200-250Ԫ");
		priceList.add("250-300Ԫ");
		priceList.add("300Ԫ����");
	}
	
	protected void initDistanceList() {
		distanceList.add("����");
		distanceList.add("1000��");
		distanceList.add("2000��");
		distanceList.add("3000��");
	}
	
	protected void initTypeList() {
		typeList.add("����");
		typeList.add("����");
		typeList.add("�������");
		typeList.add("�������(����)");
		typeList.add("��λ����");
	}

}
