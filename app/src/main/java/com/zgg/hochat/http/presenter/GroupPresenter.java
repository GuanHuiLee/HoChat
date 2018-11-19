package com.zgg.hochat.http.presenter;

import com.zgg.hochat.base.BaseResult;
import com.zgg.hochat.bean.CreateCroupInput;
import com.zgg.hochat.bean.CreateGroupResult;
import com.zgg.hochat.bean.GetGroupDetailResult;
import com.zgg.hochat.bean.GetGroupsResult;
import com.zgg.hochat.bean.QuitGroupInput;
import com.zgg.hochat.common.MyCallBack;
import com.zgg.hochat.http.contract.GroupContract;
import com.zgg.hochat.http.model.GroupModel;

import java.util.List;

import retrofit2.Response;


/**
 * 修改时间：
 * 修改备注：
 */
public class GroupPresenter extends GroupContract.Presenter {

    public GroupPresenter(GroupContract.View view, GroupModel model) {
        super(view, model);
    }


    @Override
    public void createGroup(CreateCroupInput input) {
        model.createGroup(input, new MyCallBack<BaseResult<CreateGroupResult>>() {
            @Override
            public void onSuc(Response<BaseResult<CreateGroupResult>> response) {
                if (isAttach) {
                    BaseResult<CreateGroupResult> body = response.body();
                    int code = body.getCode();
                    if (code == 200) {
                        view.showCreateGroupResult(body.getResult());
                    } else view.showError("创建失败：" + code);
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
    public void getGroups() {
        model.getGroups(new MyCallBack<BaseResult<List<GetGroupsResult>>>() {
            @Override
            public void onSuc(Response<BaseResult<List<GetGroupsResult>>> response) {
                if (isAttach) {
                    BaseResult<List<GetGroupsResult>> body = response.body();
                    int code = body.getCode();
                    if (code == 200) {
                        view.showGetGroupsResult(body.getResult());
                    } else view.showError("获取群失败：" + code);
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
    public void quitGroup(QuitGroupInput input) {
        model.quitGroup(input, new MyCallBack<BaseResult<String>>() {
            @Override
            public void onSuc(Response<BaseResult<String>> response) {
                BaseResult<String> body = response.body();
                if (isAttach) {
                    if (body.getCode() == 200) {
                        view.showQuitGroupResult("退群成功");
                    } else view.showError("退群失败：" + body.getCode());
                }
            }

            @Override
            public void onFail(String message) {
                if (isAttach) {
                    view.showError(message);
                }
            }
        });
    }

    @Override
    public void dismissGroup(QuitGroupInput input) {
        model.dismissGroup(input, new MyCallBack<BaseResult<String>>() {
            @Override
            public void onSuc(Response<BaseResult<String>> response) {
                BaseResult<String> body = response.body();
                if (isAttach) {
                    if (body.getCode() == 200) {
                        view.showDismissGroupResult("解散群组成功");
                    } else view.showError("解散群组失败：" + body.getCode());
                }
            }

            @Override
            public void onFail(String message) {
                if (isAttach) {
                    view.showError(message);
                }
            }
        });
    }

    @Override
    public void getGroupDetail(String id) {
        model.getGroupDetail(id, new MyCallBack<BaseResult<GetGroupDetailResult>>() {
            @Override
            public void onSuc(Response<BaseResult<GetGroupDetailResult>> response) {
                BaseResult<GetGroupDetailResult> body = response.body();
                if (isAttach) {
                    if (body.getCode() == 200) {
                        view.showGetGroupDetailResult(body.getResult());
                    } else view.showError("获取群信息失败：" + body.getCode());
                }
            }

            @Override
            public void onFail(String message) {
                if (isAttach) {
                    view.showError(message);
                }
            }
        });
    }
}
