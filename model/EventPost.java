package model;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import jdk.jfr.Event;

import java.io.Serializable;


public class EventPost extends Post implements Serializable {
    private static final long serialVersionUID = 1L;

    private String imagePath;
    private String date;
    private String venue;
    private int maxCapacity;

    public void setAttendees(String attendees) {
        this.attendees = this.attendees + attendees + ",";
    }

    public void setTotalAttendees() {
        this.totalAttendees++;
    }

    private String attendees;
    private ValidateDate vd;
    private int totalAttendees;
    private boolean checkDate = true;
    private DataBase db = new DataBase();

    public EventPost() {
        super();
    }

    public int getTotalAttendees() {
        return totalAttendees;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getDate() {
        return date;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(int s) {
        this.maxCapacity = s;
    }

    public String getAttendees() {
        return attendees;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ValidateDate getVd() {
        return vd;
    }

    public boolean isCheckDate() {
        return checkDate;
    }


    public EventPost(String id, String title, String description, String date, String creator_id,
                     String status, String venue, int maxCapacity, String attendees, int totalattendees, String imagePath) {
        super(id, title, description, creator_id, status);
        this.totalAttendees = totalattendees;
        this.date = date;
        this.maxCapacity = maxCapacity;
        this.attendees = attendees;
        this.venue = venue;
        if (imagePath == null) {
            this.imagePath = "";
        } else
            this.imagePath = imagePath;
        vd = new ValidateDate();
    }


    public boolean validateDate() {
        checkDate = vd.validateDate(date);
        if (!checkDate) {
            return false;
        }
        return true;
    }

    public boolean saveEvent() {
//        if (checkDate) {
        String q = "insert into EVENTDETAILS values (" + getPostDetails() + "," + "'" + venue + "'" + ","
                + "'" + date + "'" + "," + "'" + maxCapacity + "'" + "," + "'" + getAttendees() + "'" + "," + "'" + getTotalAttendees() + "'"
                + "," + "'" + this.imagePath + "'" + ")";
        DataBase.addEvent(q);
        return true;
//        }
//        return false;
    }

    @Override
    public StringBuffer getPostTypeDetails() {
        StringBuffer sb = new StringBuffer();
        sb.append("Date: " + getDate()).append("\nMaximum Capacity: " + getMaxCapacity())
                .append("\nVenue: " + getVenue()).append("\nAttendees: " + getTotalAttendees());

        return sb;
    }

    public void handleReply(Post p, String studentId) throws ClosedPostException {

        if (((EventPost) p).getStatus().equalsIgnoreCase("Open")) {
            if (((EventPost) p).getMaxCapacity() > ((EventPost) p).getTotalAttendees()) {
                if (!(((EventPost) p).getAttendees().contains(studentId))) {
                    if (AlertBox.display()) {
                        ReplyPost r = new ReplyPost(p.getId(), studentId, "1");
                        ((EventPost) p).setTotalAttendees();
                        ((EventPost) p).setAttendees(studentId);
                        if (((EventPost) p).getMaxCapacity() == ((EventPost) p).getTotalAttendees()) {

                            db.updatePost("update eventdetails set eventattendees = " + "'" + ((EventPost) p).getAttendees() + "'" + "," +
                                    " totalattendee= " + ((EventPost) p).getTotalAttendees() + "," + " status= " + "'Closed'" +
                                    " where postid =" + "'" + p.getId() + "'");
                            r.addEventReply(p);
                        } else {
                            db.updatePost("update eventdetails set eventattendees = " + "'" + ((EventPost) p).getAttendees() + "'" + "," +
                                    " totalattendee= " + ((EventPost) p).getTotalAttendees() +
                                    " where postid =" + "'" + p.getId() + "'");
                            r.addEventReply(p);
                        }
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "You have Already Replied to this Post", ButtonType.OK);
                    alert.showAndWait();
                    if (alert.getResult() == ButtonType.OK) {
                        alert.close();
                    }
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Event is Full", ButtonType.OK);
                alert.showAndWait();
                if (alert.getResult() == ButtonType.OK) {
                    alert.close();
                }
            }

        }else{
            throw new ClosedPostException("Closed");
        }
    }
}



