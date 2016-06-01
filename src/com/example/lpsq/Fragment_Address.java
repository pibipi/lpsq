package com.example.lpsq;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;

import com.example.db.DBHelper;
import com.example.db.GeneralData;
import com.example.sortlistview.CharacterParser;
import com.example.sortlistview.ClearEditText;
import com.example.sortlistview.GroupMemberBean;
import com.example.sortlistview.PinyinComparator;
import com.example.sortlistview.SideBar;
import com.example.sortlistview.SideBar.OnTouchingLetterChangedListener;
import com.example.sortlistview.SortGroupMemberAdapter;
import com.example.thread.SendMsgThread;
import com.example.utils.SharedpreferencesUtil;

public class Fragment_Address extends Fragment implements SectionIndexer,
		OnClickListener {
	private ListView sortListView;
	private SideBar sideBar;
	private TextView dialog;
	private SortGroupMemberAdapter adapter;
	private ClearEditText mClearEditText;

	private LinearLayout titleLayout;
	private TextView title;
	private TextView tvNofriends;
	//
	private ImageView address_refresh;
	private Handler mHandler;
	//
	/**
	 * 上次第一个可见元素，用于滚动时记录标识。
	 */
	private int lastFirstVisibleItem = -1;
	/**
	 * 汉字转换成拼音的类
	 */
	private CharacterParser characterParser;
	private List<GroupMemberBean> SourceDateList;

	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private PinyinComparator pinyinComparator;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_address, null);
		initViews(view);
		return view;
	}

	private void initViews(View view) {
		mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case 200:
					Toast.makeText(getContext(), "发送成功", 0).show();
					break;
				case 400:
					Toast.makeText(getContext(), "发送失败", 0).show();
					break;
				default:
					break;
				}
			}
		};
		address_refresh = (ImageView) view.findViewById(R.id.address_refresh);
		address_refresh.setOnClickListener(this);
		titleLayout = (LinearLayout) view.findViewById(R.id.title_layout);
		title = (TextView) view.findViewById(R.id.title_layout_catalog);
		tvNofriends = (TextView) view
				.findViewById(R.id.title_layout_no_friends);
		// 实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();

		pinyinComparator = new PinyinComparator();

		sideBar = (SideBar) view.findViewById(R.id.sidrbar);
		dialog = (TextView) view.findViewById(R.id.dialog);
		sideBar.setTextView(dialog);

		// 设置右侧触摸监听
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				// 该字母首次出现的位置
				int position = adapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					sortListView.setSelection(position);
				}

			}
		});

		sortListView = (ListView) view.findViewById(R.id.country_lvcountry);
		sortListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 这里要利用adapter.getItem(position)来获取当前position所对应的对象
//				Toast.makeText(
//						getContext(),
//						((GroupMemberBean) adapter.getItem(position)).getName(),
//						Toast.LENGTH_SHORT).show();
			}
		});

		// SourceDateList =
		// filledData(getResources().getStringArray(R.array.date));
		SourceDateList = filledData(getSourceDateList());

		// 根据a-z进行排序源数据
		Collections.sort(SourceDateList, pinyinComparator);
		adapter = new SortGroupMemberAdapter(getContext(), SourceDateList);
		sortListView.setAdapter(adapter);
		sortListView.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				int section = getSectionForPosition(firstVisibleItem);
				int nextSection = getSectionForPosition(firstVisibleItem + 1);
				int nextSecPosition = getPositionForSection(+nextSection);
				if (firstVisibleItem != lastFirstVisibleItem) {
					MarginLayoutParams params = (MarginLayoutParams) titleLayout
							.getLayoutParams();
					params.topMargin = 0;
					titleLayout.setLayoutParams(params);
					title.setText(SourceDateList.get(
							getPositionForSection(section)).getSortLetters());
				}
				if (nextSecPosition == firstVisibleItem + 1) {
					View childView = view.getChildAt(0);
					if (childView != null) {
						int titleHeight = titleLayout.getHeight();
						int bottom = childView.getBottom();
						MarginLayoutParams params = (MarginLayoutParams) titleLayout
								.getLayoutParams();
						if (bottom < titleHeight) {
							float pushedDistance = bottom - titleHeight;
							params.topMargin = (int) pushedDistance;
							titleLayout.setLayoutParams(params);
						} else {
							if (params.topMargin != 0) {
								params.topMargin = 0;
								titleLayout.setLayoutParams(params);
							}
						}
					}
				}
				lastFirstVisibleItem = firstVisibleItem;
			}
		});
		mClearEditText = (ClearEditText) view.findViewById(R.id.filter_edit);

		// 根据输入框输入值的改变来过滤搜索
		mClearEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// 这个时候不需要挤压效果 就把他隐藏掉
				titleLayout.setVisibility(View.GONE);
				// 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
				filterData(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}

	private String[] getSourceDateList() {
		ArrayList<String> list = new ArrayList<String>();
		DBHelper dbHelper = new DBHelper(getContext(), "lpsq", null, 1);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from ADDRESSDATA ", null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			list.add(cursor.getString(cursor.getColumnIndex("name"))
					+ "    " + cursor.getString(cursor.getColumnIndex("phone")));
			cursor.moveToNext();
		}
		cursor.close();
		if(list.size()<=1){
			list.add("点击右上角按钮获取通讯录");
			list.add("暂无数据");
		}
		String[] s =  list.toArray(new String[list.size()]);
		
		return s;
	}

	/**
	 * 为ListView填充数据
	 * 
	 * @param date
	 * @return
	 */
	private List<GroupMemberBean> filledData(String[] date) {
		List<GroupMemberBean> mSortList = new ArrayList<GroupMemberBean>();

		for (int i = 0; i < date.length; i++) {
			GroupMemberBean sortModel = new GroupMemberBean();
			sortModel.setName(date[i]);
			// 汉字转换成拼音
			String pinyin = characterParser.getSelling(date[i]);
			String sortString = pinyin.substring(0, 1).toUpperCase();

			// 正则表达式，判断首字母是否是英文字母
			if (sortString.matches("[A-Z]")) {
				sortModel.setSortLetters(sortString.toUpperCase());
			} else {
				sortModel.setSortLetters("#");
			}

			mSortList.add(sortModel);
		}
		return mSortList;

	}

	/**
	 * 根据输入框中的值来过滤数据并更新ListView
	 * 
	 * @param filterStr
	 */
	private void filterData(String filterStr) {
		List<GroupMemberBean> filterDateList = new ArrayList<GroupMemberBean>();

		if (TextUtils.isEmpty(filterStr)) {
			filterDateList = SourceDateList;
			tvNofriends.setVisibility(View.GONE);
		} else {
			filterDateList.clear();
			for (GroupMemberBean sortModel : SourceDateList) {
				String name = sortModel.getName();
				if (name.indexOf(filterStr.toString()) != -1
						|| characterParser.getSelling(name).startsWith(
								filterStr.toString())) {
					filterDateList.add(sortModel);
				}
			}
		}

		// 根据a-z进行排序
		Collections.sort(filterDateList, pinyinComparator);
		adapter.updateListView(filterDateList);
		if (filterDateList.size() == 0) {
			tvNofriends.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public Object[] getSections() {
		return null;
	}

	/**
	 * 根据ListView的当前位置获取分类的首字母的Char ascii值
	 */
	public int getSectionForPosition(int position) {
		return SourceDateList.get(position).getSortLetters().charAt(0);
	}

	/**
	 * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
	 */
	public int getPositionForSection(int section) {
		for (int i = 0; i < SourceDateList.size(); i++) {
			String sortStr = SourceDateList.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.address_refresh:
			GeneralData gd = new GeneralData("", "请求通讯录",
					GeneralData.TYPE_REQUEST_ADDRESS,
					new SharedpreferencesUtil(getContext()).getTargetId());
			new SendMsgThread(gd, getContext(), mHandler).start();
			break;

		default:
			break;
		}
	}
}
