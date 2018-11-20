package com.zgg.hochat.http.presenter;

import com.zgg.hochat.base.BaseResult;
import com.zgg.hochat.bean.AllFriendsResult;
import com.zgg.hochat.bean.FindUserResult;
import com.zgg.hochat.bean.SetDisplayNameInput;
import com.zgg.hochat.common.MyCallBack;
import com.zgg.hochat.http.contract.AllFriendsContract;
import com.zgg.hochat.http.contract.FindUserContract;
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
public class AllFriendsPresenter extends AllFriendsContract.Presenter {

    public AllFriendsPresenter(AllFriendsContract.View view, FriendShipModel model) {
        super(view, model);
    }


    @Override
    public void getAllFriends() {
        model.getAllFriends(new MyCallBack<BaseResult<List<AllFriendsResult>>>() {
            @Override
            public void onSuc(Response<BaseResult<List<AllFriendsResult>>> response) {
                if (isAttach) {
                    BaseResult<List<AllFriendsResult>> body = response.body();
                    int code = body.getCode();
                    if (code == 200) {
                        view.showAllFriendsResult(body.getResult());
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
    public void setDisplayName(SetDisplayNameInput input) {
        model.setDisplayName(input, new MyCallBack<BaseResult<String>>() {
            @Override
            public void onSuc(Response<BaseResult<String>> response) {
                if (isAttach) {
                    BaseResult<String> body = response.body();
                    int code = body.getCode();
                    if (code == 200) {
                        view.showSetDisplayNameResult("修改成功");
                    } else view.showError("修改备注失败：" + code);
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
