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

	private ArrayList<String> areaGroupList; // 父ListView数据源
	private SparseArray<LinkedList<String>> areaChildList; // 子ListView数据源list
	private LinkedList<String> childrenItem = new LinkedList<String>(); // 单独的某个父Item对应的子ListView数据源

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
					tEaraPosition = position; // 设置选中的父列表position

					childrenItem.addAll(areaChildList.get(position)); // 复制areaChildList中的第position个list数据源
					plateListViewAdapter.notifyDataSetChanged(); // 加载与父控件相关的子ListView数据源
				}
			}
		});
		
		if (tEaraPosition < areaChildList.size()) { // 加载与父控件相关的子ListView数据源
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
				tBlockPosition = position; // 设置选中的子列表position
//				String childItem = childrenItem.get(tBlockPosition);
//				if (childrenItem.get(tBlockPosition).contains("不限")) {
//					childItem.replace("不限", "");
//				}
//				showString = areaGroupList.get(tEaraPosition) + childItem;
				
				if (mOnSelectListener != null) {
					mOnSelectListener.getValue(areaGroupList.get(tEaraPosition), childrenItem.get(tBlockPosition));
				}
			}
		});

		setDefaultSelect(); // 设置选中的item的背景
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
