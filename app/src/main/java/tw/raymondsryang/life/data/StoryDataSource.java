package tw.raymondsryang.life.data;

import android.content.Context;

import java.util.List;

public interface StoryDataSource {

    void loadStory(Context context, String id, LoadStoryCallback callback);
    void loadStories(Context context, LoadStoriesCallback callback);
    void updateStory(Context context, String id, Story story, UpdateStoryCallback callback);
    void deleteStories(Context context, String[] id, DeleteStoriesCallback callback);
    void insertStory(Context context,  Story story, InsertStoryCallback callback);

    interface LoadStoryCallback{
        void onStoryLoad(Story story);
        void onFailed(Error error);
    }

    interface LoadStoriesCallback{
        void onStoriesLoad(List<Story> stories);
        void onFailed(Error error);
    }

    interface UpdateStoryCallback{
        void onStoryUpdate(Story story);
        void onFailed(Error error);
    }

    interface DeleteStoriesCallback{
        void onStoriesDelete(List<Story> stories);
        void onFailed(Error error);
    }

    interface InsertStoryCallback{
        void onInsertCallback(Story story);
        void onFailed(Error error);
    }
}
