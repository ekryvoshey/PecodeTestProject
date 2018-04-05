package com.example.esmond.pecodetestproject.presentation;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.esmond.pecodetestproject.R;

import static android.support.v4.app.NotificationCompat.DEFAULT_ALL;
import static android.view.View.VISIBLE;
import static com.example.esmond.pecodetestproject.presentation.ContentFragment.FRAGMENT_NUMBER_KEY;

public class MainActivity extends AppCompatActivity {

	private static final int FRAGMENTS_COUNT_MIN = 1;
	private static final int FRAGMENT_COUNT_MAX = 3;
	private static final String NOTIFICATION_CHANNEL_ID = "testAppNotificationChannel";
	private static final String NOTIFICATION_EXTRA_KEY = "notificationExtraKey";
	private static final String SHARED_PREFERENCES_NAME = "testSharedPreferences";
	private static final String PREFERENCE_TOTAL_FRAGMENTS_KEY = "preferenceTotalFragments";

	private int currentFragmentLimit;

	private ViewPager viewPager;
	private FloatingActionButton addButton;
	private FloatingActionButton removeButton;
	private Button createNotificationButton;
	private TextView fragmentsCounter;

	private SharedPreferences sharedPreferences;
	private MainPagerAdapter mainPagerAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initPreferences();
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

	private void initPreferences() {
		if (sharedPreferences == null) {
			sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE);
		}
	}

	private void initViews() {
		initViewpager();
		initCreateButton();
		initAddButton();
		initRemoveButton();
		initFragmentsCounter();
	}

	private void initViewpager() {
		viewPager = findViewById(R.id.activity_main_view_pager);
	}

	private void initCreateButton() {
		createNotificationButton = findViewById(R.id.activity_main_button_create);
		createNotificationButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				showNotification();
			}
		});
	}

	private void initAddButton() {
		addButton = findViewById(R.id.fragment_controls_add_button);
		addButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Bundle bundle = new Bundle();
				currentFragmentLimit++;
				if (currentFragmentLimit == FRAGMENT_COUNT_MAX) {
					addButton.setVisibility(View.GONE);
				}
				if (currentFragmentLimit > FRAGMENTS_COUNT_MIN) {
					removeButton.setVisibility(VISIBLE);
				}
				bundle.putInt(FRAGMENT_NUMBER_KEY, currentFragmentLimit);
				ContentFragment contentFragment = new ContentFragment();
				contentFragment.setArguments(bundle);
				addFragment(contentFragment, currentFragmentLimit);
				sharedPreferences.edit().putInt(PREFERENCE_TOTAL_FRAGMENTS_KEY, currentFragmentLimit).apply();
				fragmentsCounter.setText(String.valueOf(currentFragmentLimit));
			}
		});
	}

	private void initRemoveButton() {
		removeButton = findViewById(R.id.fragment_controls_remove_button);
		removeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				removeFragment();
				sharedPreferences.edit().putInt(PREFERENCE_TOTAL_FRAGMENTS_KEY, currentFragmentLimit).apply();
				fragmentsCounter.setText(String.valueOf(currentFragmentLimit));
			}
		});
	}

	private void initFragmentsCounter() {
		fragmentsCounter = findViewById(R.id.fragment_controls_counter);
		fragmentsCounter.setText(String.valueOf(FRAGMENTS_COUNT_MIN));
	}

	private void removeFragment() {
		if (!getSupportFragmentManager().getFragments().isEmpty()) {
			if (getSupportFragmentManager().getFragments().size() > FRAGMENTS_COUNT_MIN) {
				removeFragment(viewPager.getCurrentItem());
			}
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
		int totalFragments = sharedPreferences.getInt(PREFERENCE_TOTAL_FRAGMENTS_KEY, 1);
		for (int i = 1; i <= totalFragments; i++) {
			Bundle bundle = new Bundle();
			bundle.putInt(FRAGMENT_NUMBER_KEY, i);
			ContentFragment fragment = new ContentFragment();
			fragment.setArguments(bundle);
			viewPager.setOffscreenPageLimit(i);
			mainPagerAdapter.addFragment(fragment, getFragmentTitle(i));
			currentFragmentLimit = i;
			mainPagerAdapter.notifyDataSetChanged();
			removeButton.setVisibility(View.GONE);
			fragmentsCounter.setText(String.valueOf(i));
		}
		if (currentFragmentLimit < FRAGMENT_COUNT_MAX) {
			addButton.setVisibility(VISIBLE);
		} else {
			addButton.setVisibility(View.GONE);
		}
		if (currentFragmentLimit > FRAGMENTS_COUNT_MIN) {
			removeButton.setVisibility(VISIBLE);
		} else {
			removeButton.setVisibility(View.GONE);
		}
	}

	private void addFragment(ContentFragment fragment, int number) {
		int pageIndex = mainPagerAdapter.addFragment(fragment, getFragmentTitle(number));
		mainPagerAdapter.notifyDataSetChanged();
		viewPager.setOffscreenPageLimit(number);
		viewPager.setCurrentItem(pageIndex);
	}

	private void removeFragment(int position) {
		cancelNotification();
		int pageIndex = mainPagerAdapter.removeFragment(viewPager, position);
		if (pageIndex == mainPagerAdapter.getCount()) {
			pageIndex--;
		}
		mainPagerAdapter.notifyDataSetChanged();
		viewPager.setCurrentItem(pageIndex);
		currentFragmentLimit--;

		if (currentFragmentLimit <= FRAGMENTS_COUNT_MIN) {
			removeButton.setVisibility(View.GONE);
		}
		if (currentFragmentLimit < FRAGMENT_COUNT_MAX) {
			addButton.setVisibility(VISIBLE);
		}
		sharedPreferences.edit().putInt(PREFERENCE_TOTAL_FRAGMENTS_KEY, currentFragmentLimit).apply();
	}

	private void showNotification() {
		int position = viewPager.getCurrentItem();
		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra(NOTIFICATION_EXTRA_KEY, position);

		PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		int iconResource = R.drawable.ic_notification;
		Bitmap largeIcon = BitmapFactory.decodeResource(getApplicationContext().getResources(), iconResource);

		NotificationCompat.Builder notificationBuilder =
				new NotificationCompat.Builder(getApplicationContext(), NOTIFICATION_CHANNEL_ID)
						.setSmallIcon(iconResource)
						.setLargeIcon(largeIcon)
						.setContentTitle("Chat heads active")
						.setContentText("Notification " + (position + 1))
						.setPriority(NotificationManagerCompat.IMPORTANCE_HIGH)
						.setDefaults(DEFAULT_ALL)
						.setContentIntent(pendingIntent);

		NotificationManagerCompat notificationManager =
				NotificationManagerCompat.from(getApplicationContext());

		notificationManager.notify((position + 1), notificationBuilder.build());
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
		return "Page number" + " #" + number;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
