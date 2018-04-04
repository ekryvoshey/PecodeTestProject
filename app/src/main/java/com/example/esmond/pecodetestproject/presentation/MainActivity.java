package com.example.esmond.pecodetestproject.presentation;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.esmond.pecodetestproject.R;

public class MainActivity extends AppCompatActivity {

	private static final int FRAGMENT_LIMIT = 1;

	private ViewPager viewPager;
	private FloatingActionButton addButton;
	private FloatingActionButton removeButton;

	private MainPagerAdapter mainPagerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initViews();
		setupViewPager();
		loadInitialView();
	}

	private void initViews() {
		initViewpager();
		initAddButton();
		initRemoveButton();
	}

	private void initViewpager() {
		viewPager = findViewById(R.id.activity_main_view_pager);
	}

	private void initAddButton() {
		addButton = findViewById(R.id.activity_main_add_button);
		addButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				final ConstraintLayout targetView = (ConstraintLayout)
						getLayoutInflater().inflate(R.layout.fragment_content, null);
				addView(targetView);
			}
		});
	}

	private void initRemoveButton() {
		removeButton = findViewById(R.id.activity_main_remove_button);
		removeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				removeView();
			}
		});
	}

	private void removeView() {
		View targetView = viewPager.getChildAt(mainPagerAdapter.getCount() -1);
		if (targetView != null) {
			removeView(targetView);
		} else {
			Toast.makeText(MainActivity.this, "No fragments", Toast.LENGTH_SHORT).show();
		}
	}

	private void setupViewPager() {
		mainPagerAdapter = new MainPagerAdapter();
		viewPager.setAdapter(mainPagerAdapter);
	}

	private void loadInitialView() {
		ConstraintLayout view = (ConstraintLayout)getLayoutInflater().inflate(R.layout.fragment_content, null);
		viewPager.setOffscreenPageLimit(FRAGMENT_LIMIT);
		mainPagerAdapter.addView(view);
		mainPagerAdapter.notifyDataSetChanged();
	}

	private void addView(View view) {
		int pageIndex = mainPagerAdapter.addView(view);
		mainPagerAdapter.notifyDataSetChanged();
		int newFragmentLimit = pageIndex ++;
		viewPager.setOffscreenPageLimit(newFragmentLimit);
		viewPager.setCurrentItem(pageIndex);
	}

	private void removeView(View view) {
		int pageIndex = mainPagerAdapter.removeView(viewPager, view);
		if (pageIndex == mainPagerAdapter.getCount()) {
			pageIndex --;
		}
		mainPagerAdapter.notifyDataSetChanged();
		viewPager.setCurrentItem(pageIndex);
	}

	private View getCurrentpage() {
		return mainPagerAdapter.getView(viewPager.getCurrentItem());
	}

	private void setCurrentpage(View view) {
		viewPager.setCurrentItem(mainPagerAdapter.getItemPosition(view), true);
	}
}
