package com.zgg.hochat.http.presenter;

import com.zgg.hochat.base.BaseResult;
import com.zgg.hochat.bean.AllFriendsResult;
import com.zgg.hochat.bean.GetUserInfoByIdResult;
import com.zgg.hochat.bean.NickNameInput;
import com.zgg.hochat.common.MyCallBack;
import com.zgg.hochat.http.contract.AccountContract;
import com.zgg.hochat.http.contract.AllFriendsContract;
import com.zgg.hochat.http.model.AccountModel;
import com.zgg.hochat.http.model.FriendShipModel;

import java.util.List;

import retrofit2.Response;


/**
 * 类描述：地区
 * 创建人：liufei
 * 创建时间：2018/2/23 11:41
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public class AccountPresenter extends AccountContract.Presenter {

    public AccountPresenter(AccountContract.View view, AccountModel model) {
        super(view, model);
    }


    @Override
    public void setNickName(NickNameInput input) {
        model.setNickName(input, new MyCallBack<BaseResult<String>>() {
            @Override
            public void onSuc(Response<BaseResult<String>> response) {
                if (isAttach) {
                    BaseResult<String> body = response.body();
                    int code = body.getCode();
                    if (code == 200) {
                        view.showSetNickNameResult("昵称更改成功");
                    } else view.showError("修改昵称失败:" + code);
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
