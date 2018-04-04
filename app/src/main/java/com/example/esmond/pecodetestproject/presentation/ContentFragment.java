package com.example.esmond.pecodetestproject.presentation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.esmond.pecodetestproject.R;

public class ContentFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_content, container, false);
		return view;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if (getView() != null) {
			ViewGroup parent = (ViewGroup) getView().getParent();
			if (parent != null) {
				parent.removeAllViews();
			}
		}
	}
}
