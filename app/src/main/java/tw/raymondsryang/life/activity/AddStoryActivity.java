package tw.raymondsryang.life.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.sromku.simple.storage.SimpleStorage;
import com.sromku.simple.storage.Storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import pl.aprilapps.easyphotopicker.EasyImage;
import tw.raymondsryang.life.R;
import tw.raymondsryang.life.config.Config;
import tw.raymondsryang.life.data.Error;
import tw.raymondsryang.life.data.Story;
import tw.raymondsryang.life.data.StoryDataRepo;
import tw.raymondsryang.life.data.StoryDataSource;

import static android.R.attr.data;
import static tw.raymondsryang.life.R.id.image;
import static tw.raymondsryang.life.R.id.photo;

public class AddStoryActivity extends AppCompatActivity {

    private ImageView mPhotoView;
    private Story mStory;
    private File mPhoto = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_story);

        mStory = new Story();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mPhotoView = (ImageView) findViewById(photo);
        mPhotoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EasyImage.openChooserWithGallery(AddStoryActivity.this, "Pick source", 0);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_story, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                saveStory();
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveStory(){
        final EditText title = (EditText) findViewById(R.id.title);
        final EditText content = (EditText) findViewById(R.id.content);
        mStory.setTitle(title.getText().toString());
        mStory.setContent(content.getText().toString());
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        mStory.setDate(cal.get(Calendar.YEAR)+"-"+cal.get(Calendar.MONTH)+"-"+Calendar.DAY_OF_MONTH);

        StoryDataRepo.getInstance().insertStory(getApplicationContext(), mStory, new StoryDataSource.InsertStoryCallback() {
            @Override
            public void onInsertCallback(Story story) {
                if(mPhoto!=null) {
                    saveTmpPhotoIntoFile(story.getId());
                }
                finish();
            }

            @Override
            public void onFailed(Error error) {
                Toast.makeText(AddStoryActivity.this, error.msg, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new EasyImage.Callbacks() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {

            }

            @Override
            public void onImagesPicked(@NonNull List<File> imageFiles, EasyImage.ImageSource source, int type) {
                if (imageFiles.size() > 0) {

                    Picasso.with(getApplicationContext())
                            .load(imageFiles.get(0))
                            .fit()
                            .centerInside()
                            .into(mPhotoView);

                    mPhoto = imageFiles.get(0);
                }
            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {

            }
        });
    }

    private void saveTmpPhotoIntoFile(final String fileName){
        String newFilePath = getFilesDir().getPath()+"/"+Config.STORY_PHOTO_DIR;
        File file = new File(newFilePath);
        if(!file.exists() || !file.isDirectory()){
            file.mkdir();
        }

        try {
            FileOutputStream outputStream = new FileOutputStream(new File(newFilePath+"/"+fileName));
            FileInputStream inputStream = new FileInputStream(mPhoto);

            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = inputStream.read(bytes)) > 0) {
                outputStream.write(bytes, 0, len);
            }

            inputStream.close();
            outputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
            //TODO exception handling
        }
    }
}
