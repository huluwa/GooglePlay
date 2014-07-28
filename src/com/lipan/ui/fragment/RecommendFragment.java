package com.lipan.ui.fragment;

import android.view.View;
import android.widget.TextView;
import com.lipan.R;
import com.lipan.http.protocol.RecommendProtocol;
import com.lipan.ui.widget.LoadingPage.LoadResult;
import com.lipan.ui.widget.randomLayout.ShakeListener;
import com.lipan.ui.widget.randomLayout.StellarMap;
import com.lipan.utils.UIUtils;

import java.util.List;
import java.util.Random;

public class RecommendFragment extends BaseFragment {
	private List<String> mDatas;
	private StellarMap mStellarMap;
	private ShakeListener mShakeListener;

	@Override
	protected LoadResult load() {
		RecommendProtocol protocol = new RecommendProtocol();
		mDatas = protocol.load(0);
		return check(mDatas);
	}

	@Override
	protected View createLoadedView() {
		mStellarMap = new StellarMap(UIUtils.getContext());
		mStellarMap.setInnerPadding(20, 20, 20, 20);
		mStellarMap.setRegularity(6, 9);
		mStellarMap.setAdapter(new StellarMapAdapter());
		mShakeListener = new ShakeListener(UIUtils.getContext());
		mShakeListener.setOnShakeListener(new ShakeListener.OnShakeListener() {
			@Override
			public void onShake() {
				int currentGroup = mStellarMap.getCurrentGroup();
				mStellarMap.setGroup((currentGroup + 1) % 2, true);
			}
		});
		mStellarMap.setGroup(0, true);
		mStellarMap.setBackgroundResource(R.drawable.grid_item_bg_normal);
		return mStellarMap;
	}

	class StellarMapAdapter implements StellarMap.Adapter {

		private Random mRdm;

		public StellarMapAdapter() {
			mRdm = new Random();
		}

		@Override
		public int getGroupCount() {
			return 2;
		}

		@Override
		public int getCount(int group) {
			return 15;
		}

		@Override
		public View getView(int group, int position, View convertView) {
			TextView txtKeyword;
			if (convertView instanceof TextView) {
				txtKeyword = (TextView) convertView;
			} else {
				txtKeyword = new TextView(UIUtils.getContext());
			}
			final String keyword = mDatas.get(group * 15 + position);
			// 随机颜色的范围0x202020~0xefefef
			int red = 32 + mRdm.nextInt(208);
			int green = 32 + mRdm.nextInt(208);
			int blue = 32 + mRdm.nextInt(208);
			int color = 0xff000000 | (red << 16) | (green << 8) | blue;
			txtKeyword.setTextColor(color);
			txtKeyword.setTextSize(16 + mRdm.nextInt(6));
			txtKeyword.setText(keyword);
			txtKeyword.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					UIUtils.showToastSafe(UIUtils.getString(R.string.recommend_toast) + ((TextView) v).getText());
				}
			});
			return txtKeyword;
		}

		@Override
		public int getNextGroupOnPan(int group, float degree) {
			return (group + 1) % 2;
		}

		@Override
		public int getNextGroupOnZoom(int group, boolean isZoomIn) {
			return (group + 1) % 2;
		}
	}
}
