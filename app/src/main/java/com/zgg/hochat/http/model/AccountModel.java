package com.zgg.hochat.http.model;


import com.zgg.hochat.api.ApiFactory;
import com.zgg.hochat.api.TokenApiFactory;
import com.zgg.hochat.base.BaseModel;
import com.zgg.hochat.bean.LoginInput;
import com.zgg.hochat.common.MyCallBack;

import java.util.Map;

/**
 * 类描述：地区
 * 创建人：liufei
 * 创建时间：2018/2/23 11:36
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public class AccountModel extends BaseModel {

    private static AccountModel model;

    private AccountModel() {
    }

    public synchronized static AccountModel newInstance() {
        if (model == null) {
            model = new AccountModel();
        }
        return model;
    }


    /**
     * 登录
     */
    public void login(LoginInput params, MyCallBack callBack) {
        ApiFactory.getService().login(params).enqueue(callBack);
    }
}
