package michal.edu.answers.Branch;

public class Branch{

    private String branchNameEng;
    private String branchNameHeb;
    private String branchPhone;
    private Address branchAddress;

    public Branch() {
    }

    public Branch(String branchNameEng, String branchNameHeb, String branchPhone, Address branchAddress) {
        this.branchNameEng = branchNameEng;
        this.branchNameHeb = branchNameHeb;
        this.branchPhone = branchPhone;
        this.branchAddress = branchAddress;
    }

    public String getBranchNameEng() {
        return branchNameEng;
    }

    public void setBranchNameEng(String branchNameEng) {
        this.branchNameEng = branchNameEng;
    }

    public String getBranchNameHeb() {
        return branchNameHeb;
    }

    public void setBranchNameHeb(String branchNameHeb) {
        this.branchNameHeb = branchNameHeb;
    }

    public String getBranchPhone() {
        return branchPhone;
    }

    public void setBranchPhone(String branchPhone) {
        this.branchPhone = branchPhone;
    }

    public Address getBranchAddress() {
        return branchAddress;
    }

    public void setBranchAddress(Address branchAddress) {
        this.branchAddress = branchAddress;
    }

    @Override
    public String toString() {
        return "Branch{" +
                "branchNameEng='" + branchNameEng + '\'' +
                ", branchNameHeb='" + branchNameHeb + '\'' +
                ", branchPhone='" + branchPhone + '\'' +
                ", branchAddress=" + branchAddress +
                '}';
    }
}

