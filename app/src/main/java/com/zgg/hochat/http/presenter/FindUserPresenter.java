package com.zgg.hochat.http.presenter;

import com.zgg.hochat.base.BaseResult;
import com.zgg.hochat.bean.FindUserResult;
import com.zgg.hochat.bean.GetUserInfoByIdResult;
import com.zgg.hochat.common.MyCallBack;
import com.zgg.hochat.http.contract.FindUserContract;
import com.zgg.hochat.http.model.FriendShipModel;

import retrofit2.Response;


/**
 * 类描述：地区
 * 创建人：liufei
 * 创建时间：2018/2/23 11:41
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public class FindUserPresenter extends FindUserContract.Presenter {

    public FindUserPresenter(FindUserContract.View view, FriendShipModel model) {
        super(view, model);
    }


    @Override
    public void findUserByPhone(String params) {
        model.findUserByPhone(params, new MyCallBack<BaseResult<FindUserResult>>() {
            @Override
            public void onSuc(Response<BaseResult<FindUserResult>> response) {
                if (isAttach) {
                    BaseResult<FindUserResult> body = response.body();
                    int code = body.getCode();
                    if (code == 200) {
                        view.showFindUserByPhoneResult(body.getResult());
                    } else view.showError("查找失败" + code);
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
    public void findUserById(String id) {
        model.getUserInfoById(id, new MyCallBack<BaseResult<GetUserInfoByIdResult>>() {
            @Override
            public void onSuc(Response<BaseResult<GetUserInfoByIdResult>> response) {
                if (isAttach) {
                    BaseResult<GetUserInfoByIdResult> body = response.body();
                    int code = body.getCode();
                    if (code == 200) {
                        view.showFindUserInfoByIdResult(body.getResult());
                    } else view.showError("获取用户信息失败:" + code);
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
