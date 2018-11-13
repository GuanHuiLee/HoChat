package com.zgg.hochat.http.model;


import com.zgg.hochat.api.ApiFactory;
import com.zgg.hochat.common.MyCallBack;
import com.zgg.hochat.base.BaseModel;

import java.util.Map;

/**
 * 类描述：地区
 * 创建人：liufei
 * 创建时间：2018/2/23 11:36
 * 修改人：
 * 修改时间：
 * 修改备注：
 */
public class LoginModel extends BaseModel {

    private static LoginModel model;

    private LoginModel() {
    }

    public synchronized static LoginModel newInstance() {
        if (model == null) {
            model = new LoginModel();
        }
        return model;
    }

    private final static String AREA_URL = "projInfo/getAreaList";

    /**
     * 获token
     */
    public void getToken(Map<String, Object> params, MyCallBack callBack) {
        ApiFactory.getService().getToken(params).enqueue(callBack);
    }
}
