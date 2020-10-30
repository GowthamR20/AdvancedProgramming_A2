package model;

import java.io.Serializable;

public class JobPost extends Post implements Serializable {
    private static final long serialVersionUID = 1L;

    public String getProposedPrice() {
        return proposedPrice;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setProposedPrice(String proposedPrice) {
        this.proposedPrice = proposedPrice;
    }

    private String proposedPrice;
    private String imagePath;

    public String getLowestprice() {
        return lowestprice;
    }

    public void setLowestprice(String lowestprice) {
        this.lowestprice = lowestprice;
    }

    private String lowestprice;

    public JobPost(String id, String title, String description, String status, String creator_id,
                   String proposedPrice, String lowestprice, String imagePath) {
        super(id, title, description, creator_id, status);
        this.proposedPrice = proposedPrice;
        this.imagePath = imagePath;
        this.lowestprice = lowestprice;
    }

    public boolean saveJob() {
        String q = "insert into JOBDETAILS values (" + getPostDetails() + "," + "'" +
                this.proposedPrice + "'" + "," + "'" + this.lowestprice + "'" + "," + "'" + this.imagePath + "'" + ")";
        DataBase.addJob(q);
        return true;
    }

    @Override
    public StringBuffer getPostTypeDetails() {

        StringBuffer sb = new StringBuffer();
        sb.append("Proposed Price: " + getProposedPrice()).append("\nLowest Price: "+getLowestprice());

        return sb;
    }
}
