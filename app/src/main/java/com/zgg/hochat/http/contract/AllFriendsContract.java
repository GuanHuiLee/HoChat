package com.zgg.hochat.http.contract;


import com.zgg.hochat.base.BasePresenter;
import com.zgg.hochat.base.BaseView;
import com.zgg.hochat.bean.AllFriendsResult;
import com.zgg.hochat.bean.SetDisplayNameInput;
import com.zgg.hochat.http.model.FriendShipModel;

import java.util.List;


/**
 * 修改时间：
 * 修改备注：
 */
public interface AllFriendsContract {

    interface View extends BaseView {

        void showAllFriendsResult(List<AllFriendsResult> result);

        void showSetDisplayNameResult(String str);
    }

    abstract class Presenter extends BasePresenter<View, FriendShipModel> {

        public Presenter(View view, FriendShipModel model) {
            super(view, model);
        }


        public abstract void getAllFriends();

        public abstract void setDisplayName(SetDisplayNameInput input);

    }

}
