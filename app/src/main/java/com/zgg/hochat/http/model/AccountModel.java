package com.zgg.hochat.http.model;


import com.zgg.hochat.api.ApiFactory;
import com.zgg.hochat.base.BaseModel;
import com.zgg.hochat.bean.LoginInput;
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
     * 获token
     */
    public void getToken(Map<String, Object> params, MyCallBack callBack) {
        ApiFactory.getService().getToken(Constant.TOKEN_BASE_IP + "user/getToken.json", params).enqueue(callBack);
    }

    /**
     * 登录
     */
    public void login(LoginInput params, MyCallBack callBack) {
        ApiFactory.getService().login(params).enqueue(callBack);
    }

    /**
     * 登录
     */
    public void register(RegisterInput params, MyCallBack callBack) {
        ApiFactory.getService().register(params).enqueue(callBack);
    }

    public void registerZgg(RegisterZggInput input, MyCallBack callBack) {
        ApiFactory.getService().registerZgg(Constant.BASE_ZGG_IP + "user/user/register", input).enqueue(callBack);
    }
}
