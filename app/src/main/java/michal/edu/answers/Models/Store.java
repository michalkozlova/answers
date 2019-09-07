package michal.edu.answers.Models;

import java.io.Serializable;
import java.util.ArrayList;

public class Store implements Serializable {

    public static final int STORE_RETAIL = 0;
    public static final int STORE_RESTAURANT = 1;

    private String storeName;
    private Integer storeType;
    private String storeID;
    private ArrayList<Section> questionnaire;
    private Boolean hasBranches;

    public Store() {
    }

    public Store(String storeName, Integer storeType, String storeID, ArrayList<Section> questionnaire, Boolean hasBranches) {
        this.storeName = storeName;
        this.storeType = storeType;
        this.storeID = storeID;
        this.questionnaire = questionnaire;
        this.hasBranches = hasBranches;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public Integer getStoreType() {
        return storeType;
    }

    public void setStoreType(Integer storeType) {
        this.storeType = storeType;
    }

    public String getStoreID() {
        return storeID;
    }

    public void setStoreID(String storeID) {
        this.storeID = storeID;
    }

    public ArrayList<Section> getQuestionnaire() {
        return questionnaire;
    }

    public void setQuestionnaire(ArrayList<Section> questionnaire) {
        this.questionnaire = questionnaire;
    }

    public Boolean getHasBranches() {
        return hasBranches;
    }

    public void setHasBranches(Boolean hasBranches) {
        this.hasBranches = hasBranches;
    }

    @Override
    public String toString() {
        return "Store{" +
                "storeName='" + storeName + '\'' +
//                ", storeType=" + storeType +
//                ", storeID='" + storeID + '\'' +
//                ", questionnaire=" + questionnaire +
                '}';
    }
}
