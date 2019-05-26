package michal.edu.answers.Models;

import java.io.Serializable;
import java.util.ArrayList;

public class Store implements Serializable {

    public static final int STORE_RETAIL = 0;
    public static final int STORE_RESTAURANT = 1;

    private String storeName;
    private Integer storeType;
    private String storeID;
//    private ArrayList<Branch> branches;
    private ArrayList<Section> questionnaire;

    public Store() {
    }

    public Store(String storeName, Integer storeType, String storeID, ArrayList<Section> questionnaire) {
        this.storeName = storeName;
        this.storeType = storeType;
        this.storeID = storeID;
//        this.branches = branches;
        this.questionnaire = questionnaire;
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

//    public ArrayList<Branch> getBranches() {
//        return branches;
//    }

//    public void setBranches(ArrayList<Branch> branches) {
//        this.branches = branches;
//    }

    public ArrayList<Section> getQuestionnaire() {
        return questionnaire;
    }

    public void setQuestionnaire(ArrayList<Section> questionnaire) {
        this.questionnaire = questionnaire;
    }

    @Override
    public String toString() {
        return "Store{" +
                "storeName='" + storeName + '\'' +
                ", storeType=" + storeType +
                ", storeID='" + storeID + '\'' +
//                ", branches=" + branches +
                ", questionnaire=" + questionnaire +
                '}';
    }
}
