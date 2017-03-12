package tw.raymondsryang.dailylife.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import tw.raymondsryang.dailylife.R;
import tw.raymondsryang.dailylife.data.Story;
import tw.raymondsryang.dailylife.data.StoryDataRepo;
import tw.raymondsryang.dailylife.data.StoryDataSource;

public class AccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        CircleImageView avatar = (CircleImageView) findViewById(R.id.avatar);
        TextView userName = (TextView) findViewById(R.id.user_name);
        TextView email = (TextView) findViewById(R.id.email);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Picasso.with(getApplicationContext())
                    .load(user.getPhotoUrl())
                    .fit()
                    .centerInside()
                    .into(avatar);

            userName.setText(user.getDisplayName());
            email.setText(user.getEmail());
        }

        Button logout = (Button) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

    }

    private void logout(){
        FirebaseAuth.getInstance().signOut();
        StoryDataRepo.getInstance().deleteLocalStoriesData(this, new StoryDataSource.DeleteStoriesCallback() {
            @Override
            public void onStoriesDelete(List<Story> stories) {
                Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailed(Exception e) {
                Log.w("Logout", "Delete stories data failed!", e);
                Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }
}
