package com.example.esmond.pecodetestproject.presentation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.esmond.pecodetestproject.R;

public class ContentFragment extends Fragment {

	public static final String FRAGMENT_NUMBER_KEY = "fragmentNumberKey";
	private int fragmentNumber;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			fragmentNumber = getArguments().getInt(FRAGMENT_NUMBER_KEY, 1);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_content, container, false);
		setFragmentTextView(view);
		return view;
	}

	private void setFragmentTextView(View view) {
		TextView textView = view.findViewById(R.id.textView);
		String string = getString(R.string.fragment_text) + " #" + fragmentNumber;
		textView.setText(string);
	}

	public int getFragmentNumber() {
		return fragmentNumber;
	}
}
