package tw.raymondsryang.life.data;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class Story implements Serializable{

    private String id = UUID.randomUUID().toString();
    private String date;
    private String title;
    private String content;

    public String getDate() {
        return date;
    }

    public Story setDate(String date) {
        this.date = date;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Story setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getContent() {
        return content;
    }

    public Story setContent(String content) {
        this.content = content;
        return this;
    }

    public String getId() {
        return id;
    }
}
