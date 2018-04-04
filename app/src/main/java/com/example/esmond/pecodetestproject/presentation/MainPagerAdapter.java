package com.example.esmond.pecodetestproject.presentation;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class MainPagerAdapter extends PagerAdapter{

	private List<View> viewList = new ArrayList<>();

	@Override
	public int getItemPosition(Object object) {
		int index = viewList.indexOf(object);
		if (index == -1) {
			return POSITION_NONE;
		}
		return index;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		View view = viewList.get(position);
		container.addView(view);
		return view;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(viewList.get(position));
	}


	@Override
	public int getCount() {
		return viewList.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	public int addView(View view) {
		return addView(view, viewList.size());
	}

	public int addView(View view, int position) {
		viewList.add(view);
		return position;
	}

	public int removeView(ViewPager viewPager, View view) {
		return removeView(viewPager, viewList.indexOf(view));
	}

	public int removeView(ViewPager viewPager, int position) {
		viewPager.setAdapter(null);
		viewList.remove(position);
		viewPager.setAdapter(this);
		return position;
	}

	public View getView(int position) {
		return viewList.get(position);
	}
}
