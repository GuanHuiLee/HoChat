package com.zgg.hochat.http.contract;


import com.zgg.hochat.base.BasePresenter;
import com.zgg.hochat.base.BaseView;
import com.zgg.hochat.bean.FindUserResult;
import com.zgg.hochat.bean.GetUserInfoByIdResult;
import com.zgg.hochat.bean.RegisterInput;
import com.zgg.hochat.bean.RegisterResult;
import com.zgg.hochat.http.model.AccountModel;
import com.zgg.hochat.http.model.FriendShipModel;

import io.rong.imlib.model.UserInfo;


/**
 * 类描述：地区
 * 创建人：liufei
 * 创建时间：2018/2/23 11:41
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public interface FindUserContract {

    interface View extends BaseView {

        void showFindUserByPhoneResult(FindUserResult result);

        void showFindUserInfoByIdResult(GetUserInfoByIdResult result);

    }

    abstract class Presenter extends BasePresenter<View, FriendShipModel> {

        public Presenter(View view, FriendShipModel model) {
            super(view, model);
        }


        public abstract void findUserByPhone(String params);

        public abstract void findUserById(String params);

    }

}
