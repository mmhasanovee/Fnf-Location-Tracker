package xyz.mmhasanovee.fnflocationtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.shobhitpuri.custombuttons.GoogleSignInButton;

public class FinalMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_main);

        GoogleSignInButton signInButtonx;

        signInButtonx=(GoogleSignInButton) findViewById(R.id.google_connect);

        signInButtonx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(FinalMainActivity.this,MainActivity.class));
            }
        });



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}
