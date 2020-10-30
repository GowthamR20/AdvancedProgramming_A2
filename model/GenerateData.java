package model;

public class GenerateData {
    private DataBase db = new DataBase();

    public void generateTableData() {

        db.setConnection();

        db.creatable();

        EventPost e1 = new EventPost("EVE001", "Movie Time", "Mission Impossible:II", "10/06/2020", "s1",
                "Open", "Village Cinemas", 3, "", 0, "");
        EventPost e2 = new EventPost("EVE002", "Friday Night Party", "Party with dinner an wine", "12/06/2020", "s1",
                "Open", "Docklands", 5, "", 0, "");
        e1.saveEvent();
        e2.saveEvent();

        SalePost s1 = new SalePost("SAL001", "Samsung Galaxy Note 10", "Used for 5 months", "Open",
                "s2", "1200", "100", "0", "");
        SalePost s2 = new SalePost("SAL002", "Hp Pavillion Laptop i5", "Brand New Student offer", "Open",
                "s2", "1000", "100", "0", "");
        s1.saveSale();
        s2.saveSale();

        JobPost j1 = new JobPost("JOB001", "Web App developement", "2 weeks Project", "Open",
                "s1", "600", "0", "");
        JobPost j2 = new JobPost("JOB002", "House Cleaning", "House Cleaner to Clean the house", "Open",
                "s1", "200", "0", "");
        j1.saveJob();
        j2.saveJob();








    }


}
