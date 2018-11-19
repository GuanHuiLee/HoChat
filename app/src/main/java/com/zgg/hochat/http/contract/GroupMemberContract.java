package com.zgg.hochat.http.contract;


import com.zgg.hochat.base.BasePresenter;
import com.zgg.hochat.base.BaseView;
import com.zgg.hochat.bean.AddGroupMemberInput;
import com.zgg.hochat.bean.AllFriendsResult;
import com.zgg.hochat.bean.CreateCroupInput;
import com.zgg.hochat.bean.CreateGroupResult;
import com.zgg.hochat.bean.GetGroupMembersResult;
import com.zgg.hochat.bean.GetGroupsResult;
import com.zgg.hochat.http.model.GroupModel;

import java.util.List;


/**
 * 类描述：地区
 * 创建人：liufei
 * 创建时间：2018/2/23 11:41
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public interface GroupMemberContract {

    interface View extends BaseView {

        void showGetGroupMembersResult(List<GetGroupMembersResult> result);

        void showAddGroupMembersResult(String result);

        void showKickGroupMembersResult(String result);
    }

    abstract class Presenter extends BasePresenter<View, GroupModel> {

        public Presenter(View view, GroupModel model) {
            super(view, model);
        }


        public abstract void getGroupMembers(String input);

        public abstract void addGroupMembers(AddGroupMemberInput input);

        public abstract void kickGroupMembers(AddGroupMemberInput input);

    }

}
