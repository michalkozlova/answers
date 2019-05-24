package michal.edu.answers.Models;

public class Customer {

    private String customerID;
    private String customerName;
    private String customerPhone;

    public Customer() {
    }

    public Customer(String customerID, String customerName, String customerPhone) {
        this.customerID = customerID;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
    }

    public String getCurtomerID() {
        return customerID;
    }

    public void setCurtomerID(String curtomerID) {
        this.customerID = curtomerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customerID='" + customerID + '\'' +
                ", customerName='" + customerName + '\'' +
                ", customerPhone='" + customerPhone + '\'' +
                '}';
    }
}
