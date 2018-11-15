package com.zgg.hochat.http.presenter;

import com.zgg.hochat.base.BaseResult;
import com.zgg.hochat.bean.FindUserResult;
import com.zgg.hochat.bean.InviteInput;
import com.zgg.hochat.bean.InviteResult;
import com.zgg.hochat.common.MyCallBack;
import com.zgg.hochat.http.contract.FindUserContract;
import com.zgg.hochat.http.contract.FriendRequestContract;
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
public class FriendRequestPresenter extends FriendRequestContract.Presenter {

    public FriendRequestPresenter(FriendRequestContract.View view, FriendShipModel model) {
        super(view, model);
    }


    @Override
    public void invite(InviteInput params) {
        model.invite(params, new MyCallBack<BaseResult<InviteResult>>() {
            @Override
            public void onSuc(Response<BaseResult<InviteResult>> response) {
                if (isAttach) {
                    BaseResult<InviteResult> body = response.body();
                    int code = body.getCode();
                    if (code == 200) {
                        view.showInviteResult(body.getResult());
                    } else view.showError("添加失败" + code);
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
