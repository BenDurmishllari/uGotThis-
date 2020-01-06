package Model;

import com.google.firebase.Timestamp;

public class Task {

    private String  title;
    private String task;
    private String imageUrl;
    private String userId;
    private Timestamp timeAdded;
    private String userName;

    public Task() {}

    public Task(String title, String task, String imageUrl, String userId, Timestamp timeAdded, String userName)
    {
        this.title = title;
        this.task = task;
        this.imageUrl = imageUrl;
        this.userId = userId;
        this.timeAdded = timeAdded;
        this.userName = userName;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getTask() { return task; }
    public void setTask(String task) { this.task = task; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getUserId() {return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public Timestamp getTimeAdded() { return timeAdded; }
    public void setTimeAdded(Timestamp timeAdded) { this.timeAdded = timeAdded; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

}
