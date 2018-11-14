package com.zgg.hochat.http.contract;


import com.zgg.hochat.bean.LoginInput;
import com.zgg.hochat.bean.LoginResult;
import com.zgg.hochat.bean.TokenResult;
import com.zgg.hochat.http.model.LoginModel;
import com.zgg.hochat.base.BasePresenter;
import com.zgg.hochat.base.BaseView;

import java.util.Map;

import io.rong.imageloader.utils.L;


/**
 * 类描述：地区
 * 创建人：liufei
 * 创建时间：2018/2/23 11:41
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public interface LoginContract {

    interface View extends BaseView {

        void showToken(TokenResult data);

        void showLoginResult(LoginResult result);
    }

    abstract class Presenter extends BasePresenter<View, LoginModel> {

        public Presenter(View view, LoginModel model) {
            super(view, model);
        }

        /**
         * 获取token
         */
        public abstract void getToken(Map<String, Object> params);

        public abstract void login(LoginInput params);

    }

}
