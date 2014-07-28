package com.lipan.ui.holder;

import android.view.View;
import android.widget.TextView;
import com.lipan.R;
import com.lipan.bean.CategoryInfo;
import com.lipan.ui.activity.BaseActivity;
import com.lipan.utils.UIUtils;

/**
 * Created by lipan on 2014/6/7.
 */
public class CategoryTitleHolder extends BaseHolder<CategoryInfo> {
	private TextView mTextView;

	@Override
	protected View initView() {
		View view = UIUtils.inflate(R.layout.category_item_title);
		mTextView = (TextView) view.findViewById(R.id.tv_title);
		return view;
	}

	@Override
	public void refreshView() {
		mTextView.setText(getData().getTitle());
	}
}
