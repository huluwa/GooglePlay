package com.lipan.ui.fragment;

import android.view.ViewGroup;
import android.widget.AbsListView;
import com.lipan.bean.CategoryInfo;
import com.lipan.http.protocol.CategoryProtocol;
import com.lipan.ui.adapter.DefaultAdapter;
import com.lipan.ui.holder.BaseHolder;
import com.lipan.ui.holder.CategoryHolder;
import com.lipan.ui.holder.CategoryTitleHolder;
import com.lipan.ui.widget.LoadingPage.LoadResult;
import android.view.View;
import com.lipan.ui.widget.BaseListView;
import com.lipan.utils.UIUtils;

import java.util.List;

public class CategoryFragment extends BaseFragment {
	private BaseListView mListView = null;
	private CategoryAdapter mAdapter = null;
	private List<CategoryInfo> mDatas = null;

	@Override
	protected LoadResult load() {
		CategoryProtocol protocol = new CategoryProtocol();
		mDatas = protocol.load(0);
		return check(mDatas);
	}

	@Override
	protected View createLoadedView() {
		mListView = new BaseListView(UIUtils.getContext());
		mAdapter = new CategoryAdapter(mListView, mDatas);
		mListView.setAdapter(mAdapter);

		return mListView;
	}

	public class CategoryAdapter extends DefaultAdapter<CategoryInfo> {
		private int mCurrentPosition;

		public CategoryAdapter(AbsListView listView, List<CategoryInfo> datas) {
			super(listView, datas);
		}

		@Override
		public boolean hasMore() {
			return false;
		}

		//是告诉listView总共有几种样式的item
		@Override
		public int getViewTypeCount() {
			return super.getViewTypeCount() + 1;
		}

		//告诉ListView每个位置是哪种样式的item
		@Override
		public int getItemViewTypeInner(int position) {
			CategoryInfo groupInfo = getData().get(position);
			if (groupInfo.isTitle()) {
				return super.getItemViewTypeInner(position) + 1;
			} else {
				return super.getItemViewTypeInner(position);
			}
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			mCurrentPosition = position;
			return super.getView(position, convertView, parent);
		}

		public BaseHolder getHolder() {
			CategoryInfo groupInfo = getData().get(mCurrentPosition);
			if (groupInfo.isTitle()) {
				return new CategoryTitleHolder();
			} else {
				return new CategoryHolder();
			}
		}
	}
}
