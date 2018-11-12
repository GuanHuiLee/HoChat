package com.zgg.hochat.bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.rong.imlib.model.CSEvaluateItem;


/**
 * Created by yuejunhong on 17/9/20.
 */

public class CSEvaluateInfo {
    List<CSEvaluateItem> sealCSEvaluateInfoList = new ArrayList<>();

    public CSEvaluateInfo(JSONObject jsonObj) {
        try {
            JSONObject evaluateJsonObj = jsonObj.optJSONObject("evaluation");
            JSONArray jsonArray = evaluateJsonObj.getJSONArray("satisfaction");
            sealCSEvaluateInfoList.clear();
            if (jsonArray != null && jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    CSEvaluateItem sealCSEvaluateItem = new CSEvaluateItem();
                    JSONObject jsonObject = jsonArray.optJSONObject(i);
                    sealCSEvaluateItem.setConfigId(jsonObject.optString("configId"));
                    sealCSEvaluateItem.setCompanyId(jsonObject.optString("companyId"));
                    sealCSEvaluateItem.setGroupId(jsonObject.optString("groupId"));
                    sealCSEvaluateItem.setGroupName(jsonObject.optString("groupName"));
                    sealCSEvaluateItem.setLabelId(jsonObject.optString("labelId"));
                    String labelNames = jsonObject.optString("labelName");
                    sealCSEvaluateItem.setLabelNameList(Arrays.asList(labelNames.split(",")));
                    sealCSEvaluateItem.setQuestionFlag(jsonObject.optInt("isQuestionFlag", 0) == 1 ? true : false);
                    sealCSEvaluateItem.setScore(jsonObject.optInt("score"));
                    sealCSEvaluateItem.setScoreExplain(jsonObject.optString("scoreExplain"));
                    sealCSEvaluateItem.setTagMust(jsonObject.optInt("isTagMust", 0) == 1 ? true : false);
                    sealCSEvaluateItem.setInputMust(jsonObject.optInt("isInputMust", 0) == 1 ? true : false);
                    sealCSEvaluateItem.setInputLanguage(jsonObject.optString("inputLanguage"));
                    sealCSEvaluateItem.setCreateTime(jsonObject.optLong("createTime", 0));
                    sealCSEvaluateItem.setSettingMode(jsonObject.optInt("settingMode"));
                    sealCSEvaluateItem.setUpdateTime(jsonObject.optLong("updateTime", 0));
                    sealCSEvaluateItem.setOperateType(jsonObject.optInt("operateType"));
                    sealCSEvaluateInfoList.add(sealCSEvaluateItem);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public List<CSEvaluateItem> getSealCSEvaluateInfoList() {
        return sealCSEvaluateInfoList;
    }
}
