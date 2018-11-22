package com.zgg.hochat.http.contract;


import com.zgg.hochat.base.BasePresenter;
import com.zgg.hochat.base.BaseView;
import com.zgg.hochat.bean.AgreeInput;
import com.zgg.hochat.bean.InviteInput;
import com.zgg.hochat.bean.ActionResult;
import com.zgg.hochat.http.model.FriendShipModel;


/**
 * 类描述：地区
 * 创建人：liufei
 * 创建时间：2018/2/23 11:41
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public interface FriendRequestContract {

    interface View extends BaseView {

        void showInviteResult(ActionResult result);

        void showAgreeResult(ActionResult result);

        void showDeleteFriendResult(String string);
    }

    abstract class Presenter extends BasePresenter<View, FriendShipModel> {

        public Presenter(View view, FriendShipModel model) {
            super(view, model);
        }


        public abstract void invite(InviteInput params);

        public abstract void agree(AgreeInput params);

        public abstract void deleteFriend(AgreeInput params);

    }

}
