package model;


import java.io.Serializable;

public abstract class Post implements Serializable {
    private static final long serialVersionUID = 1L;
    private String creator_id;
    private String title;
    private String description;
    private String id;
    private String status;

    public Post() {

    }


    public  String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public  String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public  String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public  String getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(String creator_id) {
        this.creator_id = creator_id;
    }

    public  String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public StringBuffer getDetails() {
        StringBuffer sb = new StringBuffer();
        sb.append("Post-ID: " + getId()).append("\nCreator-Id: " + getCreator_id()).append("\nTitle: " + getTitle())
                .append("\nDescription: " + getDescription()).append("\nStatus: " + getStatus());
        return sb;
    }

    public  String getPostDetails() {
        return "'" + getId() + "'" + "," + "'" + getCreator_id() + "'" + "," + "'" + getTitle() + "'"
                + "," + "'" + getDescription() + "'" + "," + "'" + getStatus() + "'";
    }

    public Post(String id, String title, String description, String creator_id, String status) {
        setId(id);
        setTitle(title);
        setDescription(description);
        setCreator_id(creator_id);
        setStatus(status);
    }


    public abstract StringBuffer getPostTypeDetails();

    public abstract String getImagePath();


}
