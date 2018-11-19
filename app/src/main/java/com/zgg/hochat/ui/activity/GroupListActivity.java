package com.zgg.hochat.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.zgg.hochat.App;
import com.zgg.hochat.R;
import com.zgg.hochat.base.BaseToolbarActivity;
import com.zgg.hochat.bean.CreateGroupResult;
import com.zgg.hochat.bean.GetGroupDetailResult;
import com.zgg.hochat.bean.GetGroupsResult;
import com.zgg.hochat.bean.Groups;
import com.zgg.hochat.http.contract.GroupContract;
import com.zgg.hochat.http.model.GroupModel;
import com.zgg.hochat.http.presenter.GroupPresenter;
import com.zgg.hochat.utils.PortraitUtil;
import com.zgg.hochat.widget.SelectableRoundedImageView;

import java.util.ArrayList;
import java.util.List;

import io.rong.imageloader.core.ImageLoader;
import io.rong.imkit.RongIM;

/**
 * Created by AMing on 16/3/8.
 * Company RongCloud
 */
public class GroupListActivity extends BaseToolbarActivity implements GroupContract.View {

    private ListView mGroupListView;
    private GroupAdapter adapter;
    private TextView mNoGroups;
    private EditText mSearch;
    private List<Groups> mList;
    private TextView mTextView;

    private GroupPresenter presenter;
    private List<Groups> mGroupsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fr_group_list);
        mGroupListView = (ListView) findViewById(R.id.group_listview);
        mNoGroups = (TextView) findViewById(R.id.show_no_group);
        mSearch = (EditText) findViewById(R.id.group_search);
        mTextView = (TextView) findViewById(R.id.foot_group_size);
        initData();
    }

    @Override
    protected void initData() {
        presenter = new GroupPresenter(this, GroupModel.newInstance());
        addPresenter(presenter);
        presenter.getGroups();
    }

    private List<Groups> addGroups(final List<GetGroupsResult> list) {
        if (list != null && list.size() > 0) {
            mGroupsList = new ArrayList<>();
            for (GetGroupsResult groups : list) {
                String portrait = "";
                if (TextUtils.isEmpty(portrait)) {
                    portrait = PortraitUtil.generateDefaultAvatar(groups.getGroup().getName(), groups.getGroup().getId());
                }
                mGroupsList.add(new Groups(groups.getGroup().getId(),
                        groups.getGroup().getName(),
                        portrait,
                        String.valueOf(groups.getRole())
                ));
            }
            if (mGroupsList.size() > 0) {//加入数据库

            }
            return mGroupsList;
        } else {
            return null;
        }
    }

    @Override
    protected void initToolbar(Toolbar toolbar) {
        toolbar.setTitle("我的群组");
    }

    private void filterData(String s) {
        List<Groups> filterDataList = new ArrayList<>();
        if (TextUtils.isEmpty(s)) {
            filterDataList = mList;
        } else {
            for (Groups groups : mList) {
                if (groups.getName().contains(s)) {
                    filterDataList.add(groups);
                }
            }
        }
        adapter.updateListView(filterDataList);
        mTextView.setText(getString(R.string.ac_group_list_group_number, filterDataList.size()));
    }

    @Override
    public void showCreateGroupResult(CreateGroupResult result) {

    }

    @Override
    public void showGetGroupsResult(List<GetGroupsResult> result) {
        mList = addGroups(result);
        if (mList != null && mList.size() > 0) {
            adapter = new GroupAdapter(mContext, mList);
            mGroupListView.setAdapter(adapter);
            mNoGroups.setVisibility(View.GONE);
            mTextView.setVisibility(View.VISIBLE);
            mTextView.setText(getString(R.string.ac_group_list_group_number, mList.size()));
            mGroupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Groups bean = (Groups) adapter.getItem(position);
                    RongIM.getInstance().startGroupChat(GroupListActivity.this, bean.getGroupsId(), bean.getName());
                }
            });
            mSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    filterData(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        } else {
            mNoGroups.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showQuitGroupResult(String result) {

    }

    @Override
    public void showDismissGroupResult(String result) {

    }

    @Override
    public void showGetGroupDetailResult(GetGroupDetailResult groupDetail) {

    }


    class GroupAdapter extends BaseAdapter {

        private Context context;

        private List<Groups> list;

        public GroupAdapter(Context context, List<Groups> list) {
            this.context = context;
            this.list = list;
        }

        /**
         * 传入新的数据 刷新UI的方法
         */
        public void updateListView(List<Groups> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            if (list != null) return list.size();
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (list == null)
                return null;

            if (position >= list.size())
                return null;

            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            final Groups mContent = list.get(position);
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.group_item_new, parent, false);
                viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.groupname);
                viewHolder.mImageView = (SelectableRoundedImageView) convertView.findViewById(R.id.groupuri);
                viewHolder.groupId = (TextView) convertView.findViewById(R.id.group_id);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tvTitle.setText(mContent.getName());
            ImageLoader.getInstance().displayImage(null, viewHolder.mImageView, App.getOptions());
            if (context.getSharedPreferences("config", MODE_PRIVATE).getBoolean("isDebug", false)) {
                viewHolder.groupId.setVisibility(View.VISIBLE);
                viewHolder.groupId.setText(mContent.getGroupsId());
            }
            return convertView;
        }


        class ViewHolder {
            /**
             * 昵称
             */
            TextView tvTitle;
            /**
             * 头像
             */
            SelectableRoundedImageView mImageView;
            /**
             * userId
             */
            TextView groupId;
        }

    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void initUI() {

    }


}
