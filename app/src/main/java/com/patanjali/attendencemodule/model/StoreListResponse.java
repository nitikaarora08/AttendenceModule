
package com.patanjali.attendencemodule.model;

import java.util.List;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class StoreListResponse {

    @SerializedName("status")
    private Boolean mStatus;
    @SerializedName("user_store_list")
    private List<UserStoreList> mUserStoreList;

    public Boolean getStatus() {
        return mStatus;
    }

    public void setStatus(Boolean status) {
        mStatus = status;
    }

    public List<UserStoreList> getUserStoreList() {
        return mUserStoreList;
    }

    public void setUserStoreList(List<UserStoreList> userStoreList) {
        mUserStoreList = userStoreList;
    }

}
