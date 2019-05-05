package michal.edu.answers;

import java.io.Serializable;

public class Store implements Serializable {

    public static final int STORE_RETAIL = 0;
    public static final int STORE_RESTAURANT = 1;

    private Integer storeType;
    private String StoreNameEng;
    private String StoreNameHeb;

    public Store() {
    }

    public Store(Integer storeType, String storeNameEng, String storeNameHeb) {
        this.storeType = storeType;
        StoreNameEng = storeNameEng;
        StoreNameHeb = storeNameHeb;
    }

    public Integer getStoreType() {
        return storeType;
    }

    public String getStoreNameEng() {
        return StoreNameEng;
    }

    public void setStoreNameEng(String storeNameEng) {
        StoreNameEng = storeNameEng;
    }

    public String getStoreNameHeb() {
        return StoreNameHeb;
    }

    public void setStoreNameHeb(String storeNameHeb) {
        StoreNameHeb = storeNameHeb;
    }

    public void setStoreType(Integer storeType) {
        this.storeType = storeType;
    }

    @Override
    public String toString() {
        return "Store{" +
                "storeType=" + storeType +
                ", StoreNameEng='" + StoreNameEng + '\'' +
                ", StoreNameHeb='" + StoreNameHeb + '\'' +
                '}';
    }

}
