package tw.raymondsryang.life.data;

import java.util.Date;
import java.util.UUID;

public class Story {

    private String id = UUID.randomUUID().toString();
    private Date storyDate;

    public Date getStoryDate() {
        return storyDate;
    }

    public Story setStoryDate(Date storyDate) {
        this.storyDate = storyDate;
        return this;
    }

    public String getId() {
        return id;
    }
}
