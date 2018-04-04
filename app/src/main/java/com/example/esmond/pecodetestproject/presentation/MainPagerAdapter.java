package com.example.esmond.pecodetestproject.presentation;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.List;

public class MainPagerAdapter extends FragmentPagerAdapter {

	private List<ContentFragment> fragmentList = new ArrayList<>();
	private List<String> pageTitles = new ArrayList<>();

	public MainPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		return fragmentList.get(position);
	}

	@Override
	public int getCount() {
		return fragmentList.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return pageTitles.get(position);
	}

	public int addFragment(ContentFragment fragment, String title) {
		return addFragment(fragment, title, fragmentList.size());
	}

	public int addFragment(ContentFragment fragment, String title, int position) {
		fragmentList.add(fragment);
		pageTitles.add(title);

		return position;
	}

	public int removeFragment(ViewPager viewPager, ContentFragment fragment) {
		return removeFragment(viewPager, fragmentList.indexOf(fragment));
	}

	public int removeFragment(ViewPager viewPager, int position) {
		viewPager.setAdapter(null);
		fragmentList.remove(position);
		pageTitles.remove(position);
		viewPager.setAdapter(this);

		return position;
	}
}
