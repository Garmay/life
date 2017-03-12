package tw.raymondsryang.dailylife.utils;

import android.content.Context;
import android.support.annotation.NonNull;

import tw.raymondsryang.dailylife.config.Config;

public class Utils {

    public static String getStoryImagePath(@NonNull final Context context, @NonNull final String storyId){
        return getStoryImageDirPath(context)+"/"+storyId;
    }

    public static String getStoryImageDirPath(@NonNull final Context context){
        return context.getFilesDir()+"/"+ Config.STORY_PHOTO_DIR;
    }
}
