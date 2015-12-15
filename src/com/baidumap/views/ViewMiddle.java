package com.baidumap.views;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.baidumap.activity.R;
import com.baidumap.adapter.TextAdapter;

import android.content.Context;
import android.graphics.Region;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

public class ViewMiddle extends LinearLayout implements ViewBaseAction {

	private ListView regionListView;
	private ListView plateListView;

	private TextAdapter earaListViewAdapter;
	private TextAdapter plateListViewAdapter;

	private OnSelectListener mOnSelectListener;
	private int tEaraPosition = 0;
	private int tBlockPosition = 0;

	private String showString;

	// private StringBuffer showString;

	private ArrayList<String> areaGroupList; // ��ListView����Դ
	private SparseArray<LinkedList<String>> areaChildList; // ��ListView����Դlist
	private LinkedList<String> childrenItem = new LinkedList<String>(); // ������ĳ����Item��Ӧ����ListView����Դ

	public ViewMiddle(Context context) {
		super(context);
		init(context);
	}

	public ViewMiddle(Context context, ArrayList<String> areaGroupList,
			SparseArray<LinkedList<String>> areaChildList) {
		super(context);
		this.areaGroupList = areaGroupList;
		this.areaChildList = areaChildList;
		init(context);
	}

	public ViewMiddle(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_region, this, true);
		regionListView = (ListView) findViewById(R.id.listView);
		plateListView = (ListView) findViewById(R.id.listView2);
		// setBackgroundDrawable(getResources().getDrawable(R.drawable.choosearea_bg_left));

		earaListViewAdapter = new TextAdapter(context, areaGroupList,
				R.drawable.choose_item_selected,
				R.drawable.choose_eara_item_selector);
		earaListViewAdapter.setTextSize(16);
		earaListViewAdapter.setSelectedPositionNoNotify(tEaraPosition);
		regionListView.setAdapter(earaListViewAdapter);
		earaListViewAdapter.setOnItemClickListener(new TextAdapter.OnItemClickListener() {

			@Override
			public void onItemClick(View view, int position) {
				if (position < areaChildList.size()) {
					childrenItem.clear();
					tEaraPosition = position; // ����ѡ�еĸ��б�position

					childrenItem.addAll(areaChildList.get(position)); // ����areaChildList�еĵ�position��list����Դ
					plateListViewAdapter.notifyDataSetChanged(); // �����븸�ؼ���ص���ListView����Դ
				}
			}
		});
		
		if (tEaraPosition < areaChildList.size()) { // �����븸�ؼ���ص���ListView����Դ
			childrenItem.addAll(areaChildList.get(tEaraPosition));
		}
		
		plateListViewAdapter = new TextAdapter(context, childrenItem,
				R.drawable.choose_item_right,
				R.drawable.choose_plate_item_selector);
		plateListViewAdapter.setTextSize(14);
		plateListViewAdapter.setSelectedPositionNoNotify(tBlockPosition);
		plateListView.setAdapter(plateListViewAdapter);
		plateListViewAdapter.setOnItemClickListener(new TextAdapter.OnItemClickListener() {

			@Override
			public void onItemClick(View view, final int position) {
				tBlockPosition = position; // ����ѡ�е����б�position
//				String childItem = childrenItem.get(tBlockPosition);
//				if (childrenItem.get(tBlockPosition).contains("����")) {
//					childItem.replace("����", "");
//				}
//				showString = areaGroupList.get(tEaraPosition) + childItem;
				
				if (mOnSelectListener != null) {
					mOnSelectListener.getValue(areaGroupList.get(tEaraPosition), childrenItem.get(tBlockPosition));
				}
			}
		});

		setDefaultSelect(); // ����ѡ�е�item�ı���
	}

	public void setDefaultSelect() {
		regionListView.setSelection(tEaraPosition);
		plateListView.setSelection(tBlockPosition);
	}

	public String getShowText() {
		return showString;
	}

	public void setOnSelectListener(OnSelectListener onSelectListener) {
		mOnSelectListener = onSelectListener;
	}

	public interface OnSelectListener {
		//public void getValue(String showText);
		
		public void getValue(String groupText, String childText);
	}

	@Override
	public void hide() {

	}

	@Override
	public void show() {

	}
}
