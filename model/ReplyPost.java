package model;


import java.io.Serializable;

public class ReplyPost implements Serializable {
    private static final long serialVersionUID = 1L;

    public String getPostid() {
        return postid;
    }

    private String postid;
    private String attendees;
    private String value;
    private DataBase db;

    public String getValue() {
        return value;
    }

    public String getAttendees() {
        return attendees;
    }

    public ReplyPost(String postid, String attendee, String value) {
        this.postid = postid;
        this.attendees = attendee;
        this.value = value;
    }

    public void addEventReply(Post p) {
        ((EventPost) p).setAttendees(attendees);
        String query = "Insert into REPLYDETAILS values(" + "'" + p.getId() + "'" + "," + "'" + attendees + "'" + "," + "'" + value + "'" + " )";
        db.addReply(query);
    }

    public void addSaleReply(Post p) {
        String q = "Insert into REPLYDETAILS values(" + "'" + p.getId() + "'" + "," + "'" + this.attendees + "'" + "," + "'" + value + "'" + " )";
        db.addReply(q);
    }

    public void addJobReply(Post p) {
        String q = "Insert into REPLYDETAILS values(" + "'" + p.getId() + "'" + "," + "'" + this.attendees + "'" + "," + "'" + value + "'" + " )";
        db.addReply(q);
    }
}
