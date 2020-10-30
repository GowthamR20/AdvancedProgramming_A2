package model;


import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;

public class DataBase implements Serializable {
    private static final long serialVersionUID = 1L;
    static String DB_NAME = "UNILINK";
    static String TABLE_NAME;

    private static ArrayList<Post> postList = new ArrayList<>();
    private ArrayList<ReplyPost> replyList = new ArrayList<>();


    public void setConnection() {
        try (Connection con = getConnection(DB_NAME)) {
            System.out.println("Connection to database " + DB_NAME + " created successfully");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Data Base was not connected");
        }
    }

    public static Connection getConnection(String dbName)
            throws SQLException, ClassNotFoundException {
        Class.forName("org.hsqldb.jdbc.JDBCDriver");
        Connection con = DriverManager.getConnection
                ("jdbc:hsqldb:file:database/" + dbName, "SA", "");
        return con;
    }

    public void creatable() {
        try (Connection con = getConnection(DB_NAME);
             Statement stmt = con.createStatement();
        ) {
            int eventResult = 0;
            int saleResult = 0;
            int jobResult = 0;
            int replies = 0;
            if (!(checkEventTableExist("eventdetails"))) {
                eventResult = stmt.executeUpdate("CREATE TABLE EVENTDETAILS ("
                        + "postid varchar(6) NOT NULL,"
                        + "creatorid VARCHAR(8) NOT NULL,"
                        + "postname VARCHAR(200) NOT NULL,"
                        + "postdescription VARCHAR(200) NOT NULL,"
                        + "status VARCHAR(200) NOT NULL,"
                        + "eventvenue VARCHAR(200) NOT NULL,"
                        + "eventdate VARCHAR(200) NOT NULL,"
                        + "eventmaxlimit VARCHAR(200) NOT NULL,"
                        + "eventattendees VARCHAR(200) NOT NULL,"
                        + "totalattendee INTEGER  Not NUll,"
                        + "postimage VARCHAR (200) NOT NULL,"
                        + "PRIMARY KEY (postid))");

                if (eventResult == 0) {
                    System.out.println("Table EVENTDETAILS has been created successfully");
                } else {
                    System.out.println("Table EVENTDETAILS is not created");
                }
            }
            if (!(checkEventTableExist("saledetails"))) {
                saleResult = stmt.executeUpdate("CREATE TABLE SALEDETAILS ("
                        + "postid varchar(6) NOT NULL,"
                        + "creatorid VARCHAR(8) NOT NULL,"
                        + "postname VARCHAR(200) NOT NULL,"
                        + "postdescription VARCHAR(200) NOT NULL,"
                        + "status VARCHAR(200) NOT NULL,"
                        + "minimumprice INTEGER  NOT NULL,"
                        + "highestprice INTEGER  NOT NULL,"
                        + "askingprice INTEGER NOT NULL,"
                        + "postimage Varchar(200) NOT NULL,"
                        + "PRIMARY KEY (postid))");

                if (saleResult == 0) {
                    System.out.println("Table SALEDETAILS has been created successfully");
                } else {
                    System.out.println("Table SALEDETAILS is not created");
                }
            }
            if (!(checkEventTableExist("jobdetails"))) {
                jobResult = stmt.executeUpdate("CREATE TABLE JOBDETAILS ("
                        + "postid varchar(6) NOT NULL,"
                        + "creatorid VARCHAR(8) NOT NULL,"
                        + "postname VARCHAR(200) NOT NULL,"
                        + "postdescription VARCHAR(200) NOT NULL,"
                        + "status VARCHAR(200) NOT NULL,"
                        + "proposedprice VARCHAR (200) NOT NULL,"
                        + "lowestprice VARCHAR (200) NOT NULL,"
                        + "postimage varchar(200) NOT NULL,"
                        + "PRIMARY KEY (postid))");
                if (jobResult == 0) {
                    System.out.println("Table JOBDETAILS has been created successfully");
                } else {
                    System.out.println("Table JOBDETAILS is not created");
                }
            }
            if (!(checkEventTableExist("replydetails"))) {
                replies = stmt.executeUpdate("CREATE TABLE REPLYDETAILS ("
                        + "postid varchar(6) NOT NULL,"
                        + "attendees VARCHAR(1000) NOT NULL," +
                        "   value VARCHAR(200) not null)");
                if (replies == 0) {
                    System.out.println("Table REPLIES has been created successfully ");
                } else {
                    System.out.println("Table REPLIES is not created");
                }
            }


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void addEvent(String q) {
        TABLE_NAME = "EVENTDETAILS";
        try (Connection con = getConnection(DB_NAME);
             Statement stmt = con.createStatement();
        ) {
            int result = stmt.executeUpdate(q);
            con.commit();
            System.out.println("Insert into table  " + TABLE_NAME + " executed successfully");
            System.out.println(result + " row(s) affected");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public static void addSale(String q) {
        TABLE_NAME = "SALEDETAILS";
        try (Connection con = getConnection(DB_NAME);
             Statement stmt = con.createStatement();
        ) {
            int result = stmt.executeUpdate(q);
            con.commit();
            System.out.println("Insert into table  " + TABLE_NAME + " executed successfully");
            System.out.println(result + " row(s) affected");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public static void addJob(String q) {
        TABLE_NAME = "JOBDETAILS";
        try (Connection con = getConnection(DB_NAME);
             Statement stmt = con.createStatement();
        ) {
            int result = stmt.executeUpdate(q);
            con.commit();
            System.out.println("Insert into table  " + TABLE_NAME + " executed successfully");
            System.out.println(result + " row(s) affected");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }


    }

    public static ArrayList getEventRecords(String fetchQuery) {

        try (Connection con = getConnection(DB_NAME);
             Statement stmt = con.createStatement();
        ) {
            try (ResultSet resultSet = stmt.executeQuery(fetchQuery)) {

                while (resultSet.next()) {
                    Post p = new EventPost(resultSet.getString("postid"),
                            resultSet.getString("postname"),
                            resultSet.getString("postdescription"),
                            resultSet.getString("eventdate"),
                            resultSet.getString("creatorid"),
                            resultSet.getString("status"),
                            resultSet.getString("eventvenue"),
                            resultSet.getInt("eventmaxlimit"),
                            resultSet.getString("eventattendees"),
                            resultSet.getInt("totalattendee"),
                            resultSet.getString("postimage"));

                    postList.add(p);

                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return postList;
    }

    public static ArrayList getSaleRecords(String fetchQuery) {
        try (Connection con = getConnection(DB_NAME);
             Statement stmt = con.createStatement();
        ) {
            try (ResultSet resultSet = stmt.executeQuery(fetchQuery)) {
                while (resultSet.next()) {
                    Post p = new SalePost(resultSet.getString("postid"),
                            resultSet.getString("postname"),
                            resultSet.getString("postdescription"),
                            resultSet.getString("status"),
                            resultSet.getString("creatorid"),
                            resultSet.getString("askingprice"),
                            resultSet.getString("minimumprice"),
                            resultSet.getString("highestprice"),
                            resultSet.getString("postimage"));

                    postList.add(p);
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return postList;
    }

    public static ArrayList getJobRecords(String fetchQuery) {
        try (Connection con = getConnection(DB_NAME);
             Statement stmt = con.createStatement();
        ) {
            try (ResultSet resultSet = stmt.executeQuery(fetchQuery)) {
                while (resultSet.next()) {
                    Post p = new JobPost(resultSet.getString("postid"),
                            resultSet.getString("postname"),
                            resultSet.getString("postdescription"),
                            resultSet.getString("status"),
                            resultSet.getString("creatorid"),
                            resultSet.getString("proposedprice"),
                            resultSet.getString("lowestprice"),
                            resultSet.getString("postimage"));

                    postList.add(p);
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return postList;
    }


    public boolean checkEventTableExist(String tableName) {
        boolean isTable = false;
        final String DB_NAME = "UNILINK";
        try (Connection con = getConnection(DB_NAME)) {
            DatabaseMetaData dbm = con.getMetaData();
            ResultSet tables = dbm.getTables(null, null, tableName.toUpperCase(), null);

            if (tables != null) {
                if (tables.next()) {
//                    System.out.println("Table " + tableName + " exists.");
                    isTable = true;
                } else {
//                    System.out.println("Table " + tableName + " does not exist.");
                    isTable = false;
                }
                tables.close();
            } else {
                System.out.println(("Problem with retrieving database metadata"));
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return isTable;
    }


    public static String checkEventIds(String postName) {
        String idString = "";
        try (Connection con = getConnection(DB_NAME);
             Statement stmt = con.createStatement();
        ) {
            if (postName.equalsIgnoreCase("Event")) {
                try (ResultSet resultSet = stmt.executeQuery("SELECT postid FROM eventdetails ")) {
                    while (resultSet.next()) {
                        idString = resultSet.getString("postid");
                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            } else if (postName.equalsIgnoreCase("Sale")) {
                try (ResultSet resultSet = stmt.executeQuery("SELECT postid FROM saledetails ")) {
                    while (resultSet.next()) {
                        idString = resultSet.getString("postid");
                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            } else if (postName.equalsIgnoreCase("Job")) {
                try (ResultSet resultSet = stmt.executeQuery("SELECT postid FROM jobDetails ")) {
                    while (resultSet.next()) {
                        idString = resultSet.getString("postid");
                    }
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return idString;
    }

    public static int updatePost(String query) {
        int result = 0;
        try (Connection con = getConnection(DB_NAME);
             Statement stmt = con.createStatement();
        ) {
            result = stmt.executeUpdate(query);
            System.out.println(result + " row(s) affected");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    public static void deletePost(String query) {
        try (Connection con = getConnection(DB_NAME);
             Statement stmt = con.createStatement();
        ) {
            int result = stmt.executeUpdate(query);
            System.out.println(result + " row(s) affected");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void addReply(String query) {
        TABLE_NAME = "REPLYDETAILS";
        try (Connection con = getConnection(DB_NAME);
             Statement stmt = con.createStatement();
        ) {
            int result = stmt.executeUpdate(query);
            con.commit();
            System.out.println("Insert into table  " + TABLE_NAME + " executed successfully");
            System.out.println(result + " row(s) affected");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }


    public ArrayList getReplyDetails(String fetchQuery) {
        try (Connection con = getConnection(DB_NAME);
             Statement stmt = con.createStatement();
        ) {
            try (ResultSet resultSet = stmt.executeQuery(fetchQuery)) {
                while (resultSet.next()) {
                    ReplyPost rp = new ReplyPost(resultSet.getString("postid"),
                            resultSet.getString("attendees"),
                            resultSet.getString("value"));

                    replyList.add(rp);
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return replyList;
    }
}