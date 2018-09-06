/*
 * Copyright (C) 2014 Bluetooth Connection Template
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.mjhwa.ironman.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.mjhwa.ironman.R;
import com.example.mjhwa.ironman.views.LearnActivity;

public class MainFragment extends Fragment implements View.OnClickListener {

	private Context mContext = null;
	private IFragmentListener mFragmentListener = null;
	private Handler mActivityHandler = null;

	ImageButton hand1, hand2, hand3, hand4, hand5, hand6, hand7, hand8;

	Intent intent;

	public MainFragment(Context c, IFragmentListener l, Handler h) {
		mContext = c;
		mFragmentListener = l;
		mActivityHandler = h;
	}

	public MainFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_main, container, false);

		hand1 = (ImageButton) v.findViewById(R.id.hand1);
		hand1.setOnClickListener(this);

		hand2 = (ImageButton) v.findViewById(R.id.hand2);
		hand2.setOnClickListener(this);

		hand3 = (ImageButton) v.findViewById(R.id.hand3);
		hand3.setOnClickListener(this);

		hand4 = (ImageButton) v.findViewById(R.id.hand4);
		hand4.setOnClickListener(this);

		hand5 = (ImageButton) v.findViewById(R.id.hand5);
		hand5.setOnClickListener(this);

		hand6 = (ImageButton) v.findViewById(R.id.hand6);
		hand6.setOnClickListener(this);

		hand7 = (ImageButton) v.findViewById(R.id.hand7);
		hand7.setOnClickListener(this);

		hand8 = (ImageButton) v.findViewById(R.id.hand8);
		hand8.setOnClickListener(this);
		
		return v;
	}
	
	@Override
	public void onClick(View v) {
		int no = 0;
		Intent intent = new Intent(v.getContext(), LearnActivity.class);
		switch (v.getId()) {
			case R.id.hand1:
				no = 1;
				break;
			case R.id.hand2:
				no = 2;
				break;
			case R.id.hand3:
				no = 3;
				break;
			case R.id.hand4:
				no = 4;
				break;
			case R.id.hand5:
				no = 5;
				break;
			case R.id.hand6:
				no = 6;
				break;
			case R.id.hand7:
				no = 7;
				break;
			case R.id.hand8:
				no = 8;
				break;
			default:
				break;
		}

		intent.putExtra("NO",no);
		startActivity(intent);
	}
    
}
