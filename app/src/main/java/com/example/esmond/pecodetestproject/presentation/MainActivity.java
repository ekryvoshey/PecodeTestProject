package com.example.esmond.pecodetestproject.presentation;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.example.esmond.pecodetestproject.R;

import java.util.UUID;

import static com.example.esmond.pecodetestproject.presentation.ContentFragment.FRAGMENT_NUMBER_KEY;

public class MainActivity extends AppCompatActivity {

	private static final int INITIAL_FRAGMENT_LIMIT = 1;
	private static final String NOTIFICATION_CHANNEL_ID = "testAppNotificationChannel";
	private static final String NOTIFICATION_EXTRA_KEY = "notificationExtraKey";

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

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if (intent != null) {
			if (intent.getExtras() != null) {
				if (intent.getExtras().get(NOTIFICATION_EXTRA_KEY) != null) {
					int fragmentNumberToLoad = intent.getExtras().getInt(NOTIFICATION_EXTRA_KEY, 0);
					viewPager.setCurrentItem(fragmentNumberToLoad);
				}
			}
		}
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
			removeFragment(viewPager.getCurrentItem());
		} else {
			cancelAllNotifications();
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
		showNotification();
	}

	private void addFragment(ContentFragment fragment, int number) {
		int pageIndex = mainPagerAdapter.addFragment(fragment, getFragmentTitle(number));
		mainPagerAdapter.notifyDataSetChanged();
		viewPager.setOffscreenPageLimit(number);
		viewPager.setCurrentItem(pageIndex);
		showNotification();
	}

	private void removeFragment(int position) {
		int pageIndex = mainPagerAdapter.removeFragment(viewPager, position);
		if (pageIndex == mainPagerAdapter.getCount()) {
			pageIndex--;
		}
		mainPagerAdapter.notifyDataSetChanged();
		viewPager.setCurrentItem(pageIndex);
		cancelNotification();
		currentFragmentLimit--;
	}

	private void showNotification() {
		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra(NOTIFICATION_EXTRA_KEY, currentFragmentLimit);

		PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		int iconResource = R.drawable.ic_notification;
		Bitmap largeIcon = BitmapFactory.decodeResource(getApplicationContext().getResources(), iconResource);

		NotificationCompat.Builder notificationBuilder =
				new NotificationCompat.Builder(getApplicationContext(), NOTIFICATION_CHANNEL_ID)
						.setSmallIcon(iconResource)
						.setLargeIcon(largeIcon)
						.setContentTitle("Chat heads active")
						.setContentText("Notification " + currentFragmentLimit)
						.setContentIntent(pendingIntent);

		NotificationManagerCompat notificationManager =
				NotificationManagerCompat.from(getApplicationContext());

		notificationManager.notify(currentFragmentLimit, notificationBuilder.build());
	}

	public void cancelNotification() {
		NotificationManagerCompat notificationManager =
				NotificationManagerCompat.from(getApplicationContext());
		notificationManager.cancel(viewPager.getCurrentItem() + 1);
	}

	private void cancelAllNotifications() {
		NotificationManagerCompat notificationManager =
				NotificationManagerCompat.from(getApplicationContext());
		notificationManager.cancelAll();
	}

	private String getFragmentTitle(int number) {
		return ContentFragment.class.getSimpleName() + " #" + number;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
