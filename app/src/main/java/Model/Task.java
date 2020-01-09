package Model;

import com.google.firebase.Timestamp;

public class Task {

    private String  taskDocumentId,
                    title,
                    description,
                    userName,
                    userId,
                    imageUrl;
    private Timestamp timeAdded;

    // empty constructor mandatory for firebase
    public Task() {}

    public Task(String taskDocumentId, String title, String description, String userName, String userId, String imageUrl, Timestamp timeAdded)
    {
        this.taskDocumentId = taskDocumentId;
        this.title = title;
        this.description = description;
        this.userName = userName;
        this.userId = userId;
        this.imageUrl = imageUrl;
        this.timeAdded = timeAdded;
    }

    public String getTaskDocumentId() { return taskDocumentId; }
    public void setTaskDocumentId(String taskDocumentId) { this.taskDocumentId = taskDocumentId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getUserId() {return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public Timestamp getTimeAdded() { return timeAdded; }
    public void setTimeAdded(Timestamp timeAdded) { this.timeAdded = timeAdded; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }


}
