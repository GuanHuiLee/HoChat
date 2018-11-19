package com.zgg.hochat.http.model;


import com.huawei.hms.api.Api;
import com.zgg.hochat.api.ApiFactory;
import com.zgg.hochat.base.BaseModel;
import com.zgg.hochat.bean.AddGroupMemberInput;
import com.zgg.hochat.bean.CreateCroupInput;
import com.zgg.hochat.bean.LoginInput;
import com.zgg.hochat.bean.QuitGroupInput;
import com.zgg.hochat.bean.RegisterInput;
import com.zgg.hochat.bean.RegisterZggInput;
import com.zgg.hochat.common.MyCallBack;
import com.zgg.hochat.utils.Constant;

import java.util.Map;

/**
 * 类描述：地区
 * 创建人：liufei
 * 创建时间：2018/2/23 11:36
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public class GroupModel extends BaseModel {

    private static GroupModel model;

    private GroupModel() {
    }

    public synchronized static GroupModel newInstance() {
        if (model == null) {
            model = new GroupModel();
        }
        return model;
    }


    public void createGroup(CreateCroupInput input, MyCallBack callBack) {
        ApiFactory.getService().createCroup(input).enqueue(callBack);
    }

    public void getGroups(MyCallBack callBack) {
        ApiFactory.getService().getGroups().enqueue(callBack);
    }

    public void addGroupMembers(AddGroupMemberInput input, MyCallBack callBack) {
        ApiFactory.getService().addGroupMembers(input).enqueue(callBack);
    }

    public void kickGroupMembers(AddGroupMemberInput input, MyCallBack callBack) {
        ApiFactory.getService().kickGroupMembers(input).enqueue(callBack);
    }

    public void getGroupMembers(String id, MyCallBack callBack) {
        ApiFactory.getService().getGroupMembers(id).enqueue(callBack);
    }

    public void quitGroup(QuitGroupInput input, MyCallBack callBack) {
        ApiFactory.getService().quitGroup(input).enqueue(callBack);
    }

    public void dismissGroup(QuitGroupInput input, MyCallBack callBack) {
        ApiFactory.getService().dismissGroup(input).enqueue(callBack);
    }

    public void getGroupDetail(String input, MyCallBack callBack) {
        ApiFactory.getService().getGroupDetail(input).enqueue(callBack);
    }
}
