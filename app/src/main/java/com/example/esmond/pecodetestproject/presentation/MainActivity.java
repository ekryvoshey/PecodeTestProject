package com.example.esmond.pecodetestproject.presentation;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.esmond.pecodetestproject.R;

import static com.example.esmond.pecodetestproject.presentation.ContentFragment.FRAGMENT_NUMBER_KEY;

public class MainActivity extends AppCompatActivity {

	private static final int INITIAL_FRAGMENT_LIMIT = 1;

	private int currentFragmentLimit;

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
				Bundle bundle = new Bundle();
				currentFragmentLimit++;
				bundle.putInt(FRAGMENT_NUMBER_KEY, currentFragmentLimit);
				ContentFragment contentFragment = new ContentFragment();
				contentFragment.setArguments(bundle);
				addFragment(contentFragment, currentFragmentLimit);
			}
		});
	}

	private void initRemoveButton() {
		removeButton = findViewById(R.id.activity_main_remove_button);
		removeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				removeFragment();
			}
		});
	}

	private void removeFragment() {
		if (!getSupportFragmentManager().getFragments().isEmpty()) {
			ContentFragment targetFragment = (ContentFragment)getSupportFragmentManager()
					.getFragments().get(mainPagerAdapter.getCount() -1);
			removeFragment(targetFragment);
		} else {
			Toast.makeText(MainActivity.this, "No fragments", Toast.LENGTH_SHORT).show();
		}
	}

	private void setupViewPager() {
		mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
		viewPager.setAdapter(mainPagerAdapter);
	}

	private void loadInitialView() {
		Bundle bundle = new Bundle();
		bundle.putInt(FRAGMENT_NUMBER_KEY, INITIAL_FRAGMENT_LIMIT);
		ContentFragment fragment = new ContentFragment();
		fragment.setArguments(bundle);
		viewPager.setOffscreenPageLimit(INITIAL_FRAGMENT_LIMIT);
		mainPagerAdapter.addFragment(fragment, getFragmentTitle(INITIAL_FRAGMENT_LIMIT));
		currentFragmentLimit = INITIAL_FRAGMENT_LIMIT;
		mainPagerAdapter.notifyDataSetChanged();
	}

	private void addFragment(ContentFragment fragment, int number) {
		int pageIndex = mainPagerAdapter.addFragment(fragment, getFragmentTitle(number));
		mainPagerAdapter.notifyDataSetChanged();
		viewPager.setOffscreenPageLimit(number);
		viewPager.setCurrentItem(pageIndex);
	}

	private void removeFragment(ContentFragment fragment) {
		int pageIndex = mainPagerAdapter.removeFragment(viewPager, fragment);
		if (pageIndex == mainPagerAdapter.getCount()) {
			pageIndex --;
		}
		mainPagerAdapter.notifyDataSetChanged();
		viewPager.setCurrentItem(pageIndex);
	}

	private String getFragmentTitle(int number) {
		return ContentFragment.class.getSimpleName() + " #" + number;
	}
}
