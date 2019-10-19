package xyz.mmhasanovee.fnflocationtracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.LabeledIntent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;

import com.google.android.gms.common.internal.service.Common;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import io.paperdb.Paper;
import xyz.mmhasanovee.fnflocationtracker.Model.User;
import xyz.mmhasanovee.fnflocationtracker.Utils.Commonx;



public class MainActivity extends AppCompatActivity {




    DatabaseReference user_informations;
    private static final int MY_REQUEST_CODE=1506;
    private MyInternetConnectionReceiver myInternetConnectionReceiver;
    List<AuthUI.IdpConfig> providers;
    ProgressBar progressBar;



    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Paper.init(this);

        //animate


        progressBar=(ProgressBar)findViewById(R.id.progress_bar_main);
        progressBar.setVisibility(View.VISIBLE);

        //Firebase initialization

        user_informations= FirebaseDatabase.getInstance().getReference(Commonx.USER_INFORMATION);

        //provider initialization

        providers = Arrays.asList(

                new AuthUI.IdpConfig.GoogleBuilder().build()
        );

        //internet connection checker

        myInternetConnectionReceiver = new MyInternetConnectionReceiver();


        //request location permission

        /*Dexter.withActivity(this).withPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        showSignInOPtions();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        Toast.makeText(MainActivity.this, "You have to allow permission", Toast.LENGTH_SHORT).show();

                        finishAndRemoveTask();

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();

                    }
                }).check();*/

        Dexter.withActivity(this).withPermissions(Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {

                if( report.areAllPermissionsGranted()){
                showSignInOPtions();}

                else {

                    Toast.makeText(MainActivity.this, "Please allow all the permissions", Toast.LENGTH_SHORT).show();

                    finishAndRemoveTask();
                }

            }


            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                token.continuePermissionRequest();

            }


        }).check();


    }



    private void showSignInOPtions() {


        startActivityForResult(AuthUI.getInstance()
        .createSignInIntentBuilder().setAvailableProviders(providers)
                .build(),MY_REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==MY_REQUEST_CODE){


            IdpResponse response = IdpResponse.fromResultIntent(data);

            if(resultCode == RESULT_OK){
                final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                //checks whether user exist
                user_informations.orderByKey()
                        .equalTo(firebaseUser.getUid())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.getValue()==null){

                                    if(!dataSnapshot.child(firebaseUser.getUid()).exists()){

                                        Commonx.loggedUser= new User(firebaseUser.getUid(),firebaseUser.getEmail());
                                        //add to firebase
                                        user_informations.child(Commonx.loggedUser.getUid())
                                                .setValue(Commonx.loggedUser);
                                    }
                                }
                                else //if user is available
                                {
                                    Commonx.loggedUser = dataSnapshot.child(firebaseUser.getUid()).getValue(User.class);
                                }

                                //saving UID to storage to update location background

                                Paper.book().write(Commonx.USER_UID_SAVE_KEY,Commonx.loggedUser.getUid());
                                updateToken(firebaseUser);
                                setupUI();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

            }
        }
    }

    private void setupUI() {

        //Go to HOme
        startActivity(new Intent(MainActivity.this,HomeActivity.class));
        finish();
    }

    private void updateToken(final FirebaseUser firebaseUser) {

        final DatabaseReference tokens=FirebaseDatabase.getInstance()
                .getReference(Commonx.TOKENS);

        //get Toekn
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                    @Override
                    public void onSuccess(InstanceIdResult instanceIdResult) {
                        tokens.child(firebaseUser.getUid())
                                .setValue(instanceIdResult.getToken());
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

//internet connection checker

    @Override
    protected void onResume() {
        super.onResume();
        // Create an IntentFilter instance.
        IntentFilter intentFilter = new IntentFilter();

        // Add network connectivity change action.
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");

        // Register the broadcast receiver with the intent filter object.
        registerReceiver(myInternetConnectionReceiver, intentFilter);

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(myInternetConnectionReceiver);
    }

    public class MyInternetConnectionReceiver extends BroadcastReceiver {


        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            //should check null because in airplane mode it will be null
            showMsgBar(netInfo != null && netInfo.isConnected());
        }
    }

    private void showMsgBar(boolean isConnected) {

        if (isConnected) {
            /*Snackbar snackbar = Snackbar.make(
                    getWindow().getDecorView().getRootView(),
                    "Connected to internet",
                    Snackbar.LENGTH_LONG);
            snackbar.setActionTextColor(Color.WHITE);
            View snackbarView = snackbar.getView();
            snackbarView.setBackgroundColor(Color.GREEN);
            snackbar.show();*/



  //Snackbar.make(getWindow().getDecorView().getRootView(), "Connected to internet", Snackbar.LENGTH_LONG).show();

        }
        else { Snackbar snackbar = Snackbar.make(
                    getWindow().getDecorView().getRootView(),
                    "No internet connection",
                    Snackbar.LENGTH_INDEFINITE);
            snackbar.setActionTextColor(Color.WHITE);
            View snackbarView = snackbar.getView();
            snackbarView.setBackgroundColor(Color.RED);
            snackbar.show();

            //Snackbar.make(getWindow().getDecorView().getRootView(), "No internet connection", Snackbar.LENGTH_INDEFINITE).show();


        }

        }



}
