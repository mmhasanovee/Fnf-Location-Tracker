package xyz.mmhasanovee.fnflocationtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;

import io.paperdb.Paper;
import xyz.mmhasanovee.fnflocationtracker.Utils.Commonx;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        final ImageView splash_image;
        splash_image=(ImageView)findViewById(R.id.splash_image);
        final Animation aniSlide = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_in);
        splash_image.startAnimation(aniSlide);

        new Handler().postDelayed(new Runnable() {
            @Override public void run() {


                if(Commonx.loggedUser==null){
               Intent i = new Intent(SplashActivity.this, FinalMainActivity.class);
                    startActivity(i);}
                else{
                    Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);}




                finish(); } }, 2000);
    }
}
