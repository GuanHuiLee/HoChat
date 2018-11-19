package com.zgg.hochat.http.presenter;

import com.zgg.hochat.base.BaseResult;
import com.zgg.hochat.bean.AddGroupMemberInput;
import com.zgg.hochat.bean.AllFriendsResult;
import com.zgg.hochat.bean.GetGroupMembersResult;
import com.zgg.hochat.common.MyCallBack;
import com.zgg.hochat.http.contract.GroupMemberContract;
import com.zgg.hochat.http.model.GroupModel;

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
public class GroupMemberPresenter extends GroupMemberContract.Presenter {

    public GroupMemberPresenter(GroupMemberContract.View view, GroupModel model) {
        super(view, model);
    }


    @Override
    public void addGroupMembers(AddGroupMemberInput input) {
        model.addGroupMembers(input, new MyCallBack<BaseResult<String>>() {
            @Override
            public void onSuc(Response<BaseResult<String>> response) {
                if (isAttach) {
                    BaseResult<String> body = response.body();
                    int code = body.getCode();
                    if (code == 200) {
                        view.showAddGroupMembersResult("添加成功");
                    } else view.showError("添加失败：" + code);
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
    public void kickGroupMembers(AddGroupMemberInput input) {
        model.kickGroupMembers(input, new MyCallBack<BaseResult<String>>() {
            @Override
            public void onSuc(Response<BaseResult<String>> response) {
                if (isAttach) {
                    BaseResult<String> body = response.body();
                    int code = body.getCode();
                    if (code == 200) {
                        view.showKickGroupMembersResult("移除成功");
                    } else view.showError("移除失败：" + code);
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
    public void getGroupMembers(String id) {
        model.getGroupMembers(id, new MyCallBack<BaseResult<List<GetGroupMembersResult>>>() {
            @Override
            public void onSuc(Response<BaseResult<List<GetGroupMembersResult>>> response) {
                if (isAttach) {
                    BaseResult<List<GetGroupMembersResult>> body = response.body();
                    int code = body.getCode();
                    if (code == 200) {
                        view.showGetGroupMembersResult(body.getResult());
                    } else view.showError("获取失败：" + code);
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
