package com.lipan.ui.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.lipan.R;
import com.lipan.bean.SubjectInfo;
import com.lipan.http.image.ImageLoader;
import com.lipan.ui.activity.BaseActivity;
import com.lipan.utils.UIUtils;

/**
 * Created by lipan on 2014/6/7.
 */
public class SubjectHolder extends BaseHolder<SubjectInfo> {

	private ImageView iv;
	private TextView tv;

	@Override
	protected View initView() {
		View view = UIUtils.inflate(R.layout.subject_item);
		iv = (ImageView) view.findViewById(R.id.item_icon);
		tv = (TextView) view.findViewById(R.id.item_txt);
		return view;
	}

	@Override
	public void refreshView() {
		SubjectInfo data = getData();
		String des = data.getDes();
		String url = data.getUrl();
		tv.setText(des);
		iv.setTag(url);
		ImageLoader.load(iv, url);
	}

	@Override
	public void recycle() {
		recycleImageView(iv);
	}
}
