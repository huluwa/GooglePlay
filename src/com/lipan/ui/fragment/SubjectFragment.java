package com.lipan.ui.fragment;

import java.util.List;

import com.lipan.bean.AppInfo;
import com.lipan.bean.SubjectInfo;
import com.lipan.http.protocol.HomeProtocol;
import com.lipan.http.protocol.SubjectProtocol;
import com.lipan.ui.adapter.DefaultAdapter;
import com.lipan.ui.holder.BaseHolder;
import com.lipan.ui.holder.MoreHolder;
import com.lipan.ui.holder.SubjectHolder;
import com.lipan.ui.widget.LoadingPage.LoadResult;
import com.lipan.ui.widget.BaseListView;
import android.view.View;
import android.widget.AbsListView;
import com.lipan.utils.UIUtils;

public class SubjectFragment extends BaseFragment {
	private BaseListView mListView;
	private List<SubjectInfo> mDatas;
	private SubjectAdapter mAdapter;

	@Override
	protected LoadResult load() {
		SubjectProtocol protocol = new SubjectProtocol();
		mDatas = protocol.load(0);
		return check(mDatas);
	}

	@Override
	protected View createLoadedView() {
		mListView = new BaseListView(UIUtils.getContext());
		mAdapter = new SubjectAdapter(mListView, mDatas);
		mListView.setAdapter(mAdapter);
		return mListView;
	}

	class SubjectAdapter extends DefaultAdapter<SubjectInfo> {

		public SubjectAdapter(AbsListView listView, List<SubjectInfo> datas) {
			super(listView, datas);
		}

		@Override
		public boolean hasMore() {
			return false;
		}

		@Override
		public BaseHolder getHolder() {
			return new SubjectHolder();
		}

		/** 加载更多 */
		@Override
		public List<SubjectInfo> onLoadMore() {
			SubjectProtocol protocol = new SubjectProtocol();
			return protocol.load(getData().size());
		}

		@Override
		public void onItemClickInner(int position) {
			UIUtils.showToastSafe(getItem(position).getDes());
		}
	}
}
