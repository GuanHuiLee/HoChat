package com.zgg.hochat.http.contract;


import com.zgg.hochat.base.BasePresenter;
import com.zgg.hochat.base.BaseView;
import com.zgg.hochat.bean.AllFriendsResult;
import com.zgg.hochat.bean.GetUserInfoByIdResult;
import com.zgg.hochat.bean.NickNameInput;
import com.zgg.hochat.http.model.AccountModel;
import com.zgg.hochat.http.model.FriendShipModel;

import java.util.List;


/**
 * 类描述：地区
 * 创建人：liufei
 * 创建时间：2018/2/23 11:41
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public interface AccountContract {

    interface View extends BaseView {

        void showSetNickNameResult(String result);

        void showUserInfoResult(GetUserInfoByIdResult result);
    }

    abstract class Presenter extends BasePresenter<View, AccountModel> {

        public Presenter(View view, AccountModel model) {
            super(view, model);
        }


        public abstract void setNickName(NickNameInput input);

        public abstract void getUserInfoById(String id);

    }

}
