package com.zgg.hochat.http.contract;


import com.zgg.hochat.base.BasePresenter;
import com.zgg.hochat.base.BaseView;
import com.zgg.hochat.bean.CreateCroupInput;
import com.zgg.hochat.bean.CreateGroupResult;
import com.zgg.hochat.bean.GetGroupDetailResult;
import com.zgg.hochat.bean.GetGroupsResult;
import com.zgg.hochat.bean.QuitGroupInput;
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
public interface GroupContract {

    interface View extends BaseView {

        void showCreateGroupResult(CreateGroupResult result);

        void showGetGroupsResult(List<GetGroupsResult> result);

        void showQuitGroupResult(String result);

        void showDismissGroupResult(String result);

        void showGetGroupDetailResult(GetGroupDetailResult groupDetail);
    }

    abstract class Presenter extends BasePresenter<View, GroupModel> {

        public Presenter(View view, GroupModel model) {
            super(view, model);
        }


        public abstract void createGroup(CreateCroupInput input);

        public abstract void getGroups();

        public abstract void quitGroup(QuitGroupInput input);

        public abstract void dismissGroup(QuitGroupInput input);

        public abstract void getGroupDetail(String id);

    }

}
