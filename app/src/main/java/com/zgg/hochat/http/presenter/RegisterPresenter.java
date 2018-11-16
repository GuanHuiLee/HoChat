package com.zgg.hochat.http.presenter;

import com.zgg.hochat.base.BaseResult;
import com.zgg.hochat.bean.LoginInput;
import com.zgg.hochat.bean.LoginResult;
import com.zgg.hochat.bean.RegisterInput;
import com.zgg.hochat.bean.RegisterResult;
import com.zgg.hochat.bean.RegisterZggInput;
import com.zgg.hochat.bean.TokenResult;
import com.zgg.hochat.common.MyCallBack;
import com.zgg.hochat.http.contract.LoginContract;
import com.zgg.hochat.http.contract.RegisterContract;
import com.zgg.hochat.http.model.AccountModel;

import java.util.Map;

import retrofit2.Response;


/**
 * 类描述：地区
 * 创建人：liufei
 * 创建时间：2018/2/23 11:41
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public class RegisterPresenter extends RegisterContract.Presenter {


    public RegisterPresenter(RegisterContract.View view, AccountModel model) {
        super(view, model);
    }


    @Override
    public void register(RegisterInput params) {
        model.register(params, new MyCallBack<BaseResult<RegisterResult>>() {
            @Override
            public void onSuc(Response<BaseResult<RegisterResult>> response) {
                if (isAttach) {
                    BaseResult<RegisterResult> body = response.body();
                    int code = body.getCode();
                    if (code == 200) {
                        view.showRegisterResult(body.getResult());
                    } else view.showError("注册失败" + code);
                }
            }

            @Override
            public void onFail(String message) {
                if (isAttach)
                    view.showError(message);
            }
        });
    }

    @Override
    public void registerZgg(RegisterZggInput params) {
        model.registerZgg(params, new MyCallBack<BaseResult<String>>() {
            @Override
            public void onSuc(Response<BaseResult<String>> response) {
                if (isAttach) {
                    BaseResult<String> body = response.body();
                    int code = body.getCode();
                    if (code == 200) {
                        view.showRegisterZggResult(body.getMsg());
                    } else view.showError("注册失败" + code);
                }
            }

            @Override
            public void onFail(String message) {
                if (isAttach)
                    view.showError(message);
            }
        });
    }
}
