package com.zgg.hochat.http.model;


import com.zgg.hochat.api.ApiFactory;
import com.zgg.hochat.base.BaseModel;
import com.zgg.hochat.bean.AgreeInput;
import com.zgg.hochat.bean.InviteInput;
import com.zgg.hochat.bean.LoginInput;
import com.zgg.hochat.bean.RegisterInput;
import com.zgg.hochat.bean.SetDisplayNameInput;
import com.zgg.hochat.common.MyCallBack;
import com.zgg.hochat.utils.Constant;

import java.util.Map;

/**
 * 类描述：好友关系
 * 创建人：lgh
 * 创建时间：2018/2/23 11:36
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public class FriendShipModel extends BaseModel {

    private static FriendShipModel model;

    private FriendShipModel() {
    }

    public synchronized static FriendShipModel newInstance() {
        if (model == null) {
            model = new FriendShipModel();
        }
        return model;
    }


    public void findUserByPhone(String params, MyCallBack callBack) {
        ApiFactory.getService().findUser("86", params).enqueue(callBack);
    }

    public void invite(InviteInput inviteInput, MyCallBack callBack) {
        ApiFactory.getService().invite(inviteInput).enqueue(callBack);
    }

    public void agree(AgreeInput inviteInput, MyCallBack callBack) {
        ApiFactory.getService().agree(inviteInput).enqueue(callBack);
    }


    public void getAllFriends(MyCallBack callBack) {
        ApiFactory.getService().getAllFriends().enqueue(callBack);
    }

    public void setDisplayName(SetDisplayNameInput input, MyCallBack callBack) {
        ApiFactory.getService().setDisplayName(input).enqueue(callBack);
    }

    public void getUserInfoById(String id, MyCallBack callBack) {
        ApiFactory.getService().getUserInfoById(id).enqueue(callBack);
    }
}
