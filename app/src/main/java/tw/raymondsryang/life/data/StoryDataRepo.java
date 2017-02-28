package tw.raymondsryang.life.data;

import android.content.Context;

import tw.raymondsryang.life.data.localDataSource.StoryLocalDataSource;

public class StoryDataRepo implements StoryDataSource{

    private StoryLocalDataSource mLocalDataSource;
    private static StoryDataRepo sInstance;

    public static StoryDataRepo getInstance(){
        if (sInstance==null)
            sInstance = new StoryDataRepo();
        return sInstance;
    }

    private StoryDataRepo() {
        mLocalDataSource = new StoryLocalDataSource();
    }

    @Override
    public void loadStory(Context context, String id, LoadStoryCallback callback) {
        mLocalDataSource.loadStory(context, id, callback);
    }

    @Override
    public void loadStories(Context context, LoadStoriesCallback callback) {
        mLocalDataSource.loadStories(context, callback);
    }

    @Override
    public void updateStory(Context context, String id, Story story, UpdateStoryCallback callback) {
        mLocalDataSource.updateStory(context, id, story, callback);
    }

    @Override
    public void deleteStories(Context context, String[] id, DeleteStoriesCallback callback) {
        mLocalDataSource.deleteStories(context, id, callback);
    }

    @Override
    public void insertStory(Context context, Story story, InsertStoryCallback callback) {
        mLocalDataSource.insertStory(context, story, callback);
    }

    @Override
    public void insertStory(Context context, int index, Story story, InsertStoryCallback callback) {
        mLocalDataSource.insertStory(context, index, story, callback);
    }
}
