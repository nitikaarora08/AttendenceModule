
package com.patanjali.attendencemodule.model;

import java.util.List;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class ItemListResponse {

    @SerializedName("status")
    private Boolean mStatus;
    @SerializedName("store_material_list")
    private List<StoreMaterialList> mStoreMaterialList;

    public Boolean getStatus() {
        return mStatus;
    }

    public void setStatus(Boolean status) {
        mStatus = status;
    }

    public List<StoreMaterialList> getStoreMaterialList() {
        return mStoreMaterialList;
    }

    public void setStoreMaterialList(List<StoreMaterialList> storeMaterialList) {

        mStoreMaterialList = storeMaterialList;

    }

}
