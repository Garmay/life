package tw.raymondsryang.dailylife.data;

import android.content.Context;

import java.util.List;

public interface StoryDataSource {

    void loadStory(Context context, String id, LoadStoryCallback callback);
    void loadStories(Context context, LoadStoriesCallback callback);
    void updateStory(Context context, String id, Story story, UpdateStoryCallback callback);
    void deleteAllStories(Context context, DeleteStoriesCallback callback);
    void deleteStories(Context context, String[] id, DeleteStoriesCallback callback);
    void insertStory(Context context,  Story story, InsertStoryCallback callback);
    void insertStory(Context context,  int index, Story story, InsertStoryCallback callback);

    interface LoadStoryCallback{
        void onStoryLoad(Story story);
        void onFailed(Exception exception);
    }

    interface LoadStoriesCallback{
        void onStoriesLoad(List<Story> stories);
        void onFailed(Exception exception);
    }

    interface UpdateStoryCallback{
        void onStoryUpdate(Story story);
        void onFailed(Exception exception);
    }

    interface DeleteStoriesCallback{
        void onStoriesDelete(List<Story> stories);
        void onFailed(Exception exception);
    }

    interface InsertStoryCallback{
        void onInsertCallback(Story story);
        void onFailed(Exception exception);
    }
}
