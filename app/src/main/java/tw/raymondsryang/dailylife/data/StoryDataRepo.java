package tw.raymondsryang.dailylife.data;

import android.content.Context;

import java.io.File;
import java.io.FileNotFoundException;

import tw.raymondsryang.dailylife.data.localDataSource.StoryLocalDataSource;
import tw.raymondsryang.dailylife.utils.Utils;

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
    public void deleteAllStories(Context context, DeleteStoriesCallback callback) {
        mLocalDataSource.deleteAllStories(context, callback);
    }

    @Override
    public void insertStory(Context context, Story story, InsertStoryCallback callback) {
        mLocalDataSource.insertStory(context, story, callback);
    }

    @Override
    public void insertStory(Context context, int index, Story story, InsertStoryCallback callback) {
        mLocalDataSource.insertStory(context, index, story, callback);
    }

    public void deleteLocalStoriesData(Context context, DeleteStoriesCallback callback){
        /*Delete Images*/
        File imagesDir = new File(Utils.getStoryImageDirPath(context));
        if (imagesDir.exists()){
            imagesDir.delete();
        } else {
            callback.onFailed(new FileNotFoundException());
            return;
        }

        mLocalDataSource.deleteAllStories(context, callback);

    }
}
