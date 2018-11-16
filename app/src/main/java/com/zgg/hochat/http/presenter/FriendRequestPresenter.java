package com.zgg.hochat.http.presenter;

import com.zgg.hochat.base.BaseResult;
import com.zgg.hochat.bean.AgreeInput;
import com.zgg.hochat.bean.InviteInput;
import com.zgg.hochat.bean.ActionResult;
import com.zgg.hochat.common.MyCallBack;
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
        model.invite(params, new MyCallBack<BaseResult<ActionResult>>() {
            @Override
            public void onSuc(Response<BaseResult<ActionResult>> response) {
                if (isAttach) {
                    BaseResult<ActionResult> body = response.body();
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

    @Override
    public void agree(AgreeInput params) {
        model.agree(params, new MyCallBack<BaseResult<ActionResult>>() {
            @Override
            public void onSuc(Response<BaseResult<ActionResult>> response) {
                if (isAttach) {
                    BaseResult<ActionResult> body = response.body();
                    int code = body.getCode();
                    if (code == 200) {
                        view.showAgreeResult(body.getResult());
                    } else view.showError("同意失败" + code);
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
