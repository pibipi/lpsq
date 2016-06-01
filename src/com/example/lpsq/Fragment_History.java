package com.example.lpsq;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class Fragment_History extends Fragment implements
		OnCheckedChangeListener {
	private FragmentManager fragmentManager;
	private RadioGroup radioGroup;
	private ViewPager pager;
	private List<Fragment> fragments;
	private RadioButton btn_chat;
	private RadioButton btn_sms;
	private RadioButton btn_phone;
	private RadioButton btn_loc;

	private int page_2;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_history, null);
		init(view);
		return view;
	}

	private void init(View view) {
		Bundle bundle = getArguments();
		if (bundle != null) {
			page_2 = bundle.getInt("page_2");
			System.out.println("page2" + page_2);
		}
		fragmentManager = getChildFragmentManager();
		radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
		radioGroup.setOnCheckedChangeListener(this);
		btn_chat = (RadioButton) view.findViewById(R.id.main1);
		btn_sms = (RadioButton) view.findViewById(R.id.main2);
		btn_phone = (RadioButton) view.findViewById(R.id.main3);
		btn_loc = (RadioButton) view.findViewById(R.id.main4);
		pager = (ViewPager) view.findViewById(R.id.pager);
		initViewPager(view);
	}

	@SuppressWarnings("deprecation")
	private void initViewPager(final View view) {
		fragments = new ArrayList<Fragment>();
		fragments.add(new Fragment_History_Chat());
		fragments.add(new Fragment_History_SMS());
		fragments.add(new Fragment_History_Phone());
		fragments.add(new Fragment_History_Location());
		pager.setAdapter(new MyPagerAdapter(fragmentManager, fragments));
		pager.setCurrentItem(page_2);
		switch (page_2) {
		case 0:
			btn_chat.setChecked(true);
			break;
		case 1:
			btn_sms.setChecked(true);
			break;
		case 2:
			btn_phone.setChecked(true);
			break;
		case 3:
			btn_loc.setChecked(true);
			break;
		default:
			break;
		}
		System.out.println("Fragment_History");
		pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int index) {
				System.out.println(index + "index");
				switch (index) {
				case 0:
					btn_chat.setChecked(true);
					break;
				case 1:
					btn_sms.setChecked(true);
					break;
				case 2:
					btn_phone.setChecked(true);
					break;
				case 3:
					btn_loc.setChecked(true);
					break;
				default:
					break;
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// System.out.println(arg0 + "``" + arg1 + "``" + arg2);
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});

	}

	class MyPagerAdapter extends FragmentPagerAdapter {
		private List<Fragment> list = new ArrayList<Fragment>();

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		public MyPagerAdapter(FragmentManager fm, List<Fragment> list) {
			super(fm);
			this.list = list;
		}

		@Override
		public Fragment getItem(int index) {
			return list.get(index);
		}

		@Override
		public int getCount() {
			return list.size();
		}

	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.main1:
			pager.setCurrentItem(0);
			break;
		case R.id.main2:
			pager.setCurrentItem(1);
			break;
		case R.id.main3:
			pager.setCurrentItem(2);
			break;
		case R.id.main4:
			pager.setCurrentItem(3);
			break;
		default:
			break;
		}
	}
}
