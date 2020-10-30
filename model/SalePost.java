package model;

import java.io.Serializable;

public class SalePost extends Post implements Serializable {
    private static final long serialVersionUID = 1L;
    public String getSaleMinimumRaise() {
        return saleMinimumRaise;
    }
    public void setSaleMinimumRaise(String minimumRaise){
        this.saleMinimumRaise=minimumRaise;
    }

    public void setSaleAskingPrice(String askingPrice){
        this.saleAskingPrice=askingPrice;
    }

    public String getSaleAskingPrice() {
        return saleAskingPrice;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getHighestPrice() {
        return highestPrice;
    }
    public void setHighestPrice(String highestPrice) {
        this.highestPrice = highestPrice;
    }



    private String saleMinimumRaise;
    private String saleAskingPrice;
    private String imagePath;


    private String highestPrice;

    public SalePost(String Id, String title, String description, String status,
                    String creatorId, String askingPrice, String minimumRaise,String highestPrice, String imagepath) {
        super(Id, title, description, creatorId, status);
        this.saleAskingPrice = askingPrice;
        this.saleMinimumRaise = minimumRaise;
        this.imagePath = imagepath;
        this.highestPrice=highestPrice;
    }


    public boolean validateAskingPrice() {
        try {
            double askingPrice = Double.parseDouble(saleAskingPrice);
            if (askingPrice > 0) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public boolean validateMinRaise() {
        try {
            double minimumRaise = Double.parseDouble(saleMinimumRaise);
            if (minimumRaise > 0) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public boolean saveSale() {
        String q = "insert into SALEDETAILS values (" + getPostDetails() + "," + "'" +
                saleMinimumRaise + "'" +","+"'"+highestPrice+"'"+ "," + "'" + saleAskingPrice + "'" + "," + "'" + imagePath + "'" + ")";
        DataBase.addSale(q);
        return true;
    }


    @Override
    public StringBuffer getPostTypeDetails() {
        StringBuffer sb = new StringBuffer();
        sb.append("Minimum Raise: " + getSaleMinimumRaise()).append("\nHighest Offer: "+getHighestPrice());
        return sb;
    }
}

