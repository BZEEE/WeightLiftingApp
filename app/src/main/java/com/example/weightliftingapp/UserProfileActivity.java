package com.example.weightliftingapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;

import android.util.Log;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;

import com.example.weightliftingapp.IPF.IPFCalculatorActivity;
import com.example.weightliftingapp.OneRepMax.RepMaxCalculatorActivity;
import com.example.weightliftingapp.Wilks.WilksCalculatorActivity;
import com.github.mikephil.charting.charts.LineChart;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class UserProfileActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "debugging";
    private String email;
    private String password;
    private LineChart chart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


//        // let user set profile image from phone gallery, then store it in firebase
//        // make a circular image as the profile picture
//        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(),R.drawable.userProfileImageResource);
//        Bitmap circularBitmap = ImageConverter.getRoundedCornerBitmap(bitmap, 100);
//        ImageView circularImageView = (ImageView)findViewById(R.id.userProfileImageView);
//        circularImageView.setImageBitmap(circularBitmap);

        // create user profile chart if data exists
        chart = findViewById(R.id.user_bench_squat_deadlift_progression_chart);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();

        // update UI function retreives users specific data and populates the profile
        // ad acheivements activities with that data
        // updateUI(currentUser);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Intent intent;

        if (id == R.id.nav_log_out) {
            //have the user sign out
            FirebaseAuth.getInstance().signOut();
            intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_achievements) {
            // navigate to Achievements activity
            intent = new Intent(this, AchievementsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_one_rep_max_calculator) {
            // navigate to one rep max calculator activity
            intent = new Intent(this, RepMaxCalculatorActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_wilks_calculator) {
            // navigate to wilks calculator activity
            intent = new Intent(this, WilksCalculatorActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_ipf_calculator) {
            // navigate to ipf calculator activity
            intent = new Intent(this, IPFCalculatorActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void updateUI(FirebaseUser user) {
        // update the UI elements of the user's profile page
        // update whatever contents of the profile we want

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName("Jane Q. User")
                .setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated.");
                        } else {
                            Log.d(TAG, "User profile not able to update.");
                        }
                    }
                });
    }

//    private void getUserProfile() {
//        // get user specific profile information
//    }
//
//    private GraphData[] ReadChartDataFromFirebase() {
//
//    }
//
//    private void ReadWilksScoreFromFirebase() {
//
//    }
//
//    private void ReadIPFScoreFromFirebase() {
//
//    }
//
//    private void WriteNewChartEntryToFirebase() {
//
//    }
//
//    private void WriteNewWilksScoreToFirebase() {
//
//    }
//
//    private void WriteNewIPFScoreToFirebase() {
//
//    }
//
//    private void PopulateChartWithData(GraphData[] data) {
//        // https://weeklycoding.com/mpandroidchart-documentation/getting-started/
//        // read in data from cloud
//        // populate UI with data
//        List<Entry> entries = new ArrayList<>();
//
//        for (GraphData dataInstance : data) {
//            entries.add(new Entry(data.getValueX))
//        }
//
//
//    }
}
