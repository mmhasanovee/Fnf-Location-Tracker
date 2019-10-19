package xyz.mmhasanovee.fnflocationtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import xyz.mmhasanovee.fnflocationtracker.Model.User;
import xyz.mmhasanovee.fnflocationtracker.Utils.Commonx;

public class UserProfileActivity extends AppCompatActivity {

    DatabaseReference userProfile;

    CircleImageView imageView;
    TextView friend_profile_name,friend_profile_address,friend_profile_phone,friend_profile_email;
    Button friend_remove_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        imageView=(CircleImageView) findViewById(R.id.friend_profile_image_view);
        friend_profile_address=(TextView)findViewById(R.id.friend_profile_address);
        friend_profile_name=(TextView)findViewById(R.id.friend_profile_name);
        friend_profile_phone=(TextView)findViewById(R.id.friend_profile_phone);
        friend_profile_email=(TextView)findViewById(R.id.friend_profile_email);

        friend_remove_btn=(Button)findViewById(R.id.friend_remove_btn);

        userProfile = FirebaseDatabase.getInstance().getReference(Commonx.USER_INFORMATION).child(Commonx.userProfile.getUid());


        // userProfile fetches the user profile data node(image/name/email), user profile contains UID form the previous activity

        userProfile.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    //Toast.makeText(UserProfileActivity.this, dataSnapshot.child("image").getValue().toString(), Toast.LENGTH_LONG).show();
                    if(dataSnapshot.child("image").exists()){
                    Picasso.get().load(dataSnapshot.child("image").getValue().toString()).into(imageView);}

                    if(dataSnapshot.child("address").exists()){

                        friend_profile_address.setText(dataSnapshot.child("address").getValue().toString());

                    }
                    if(dataSnapshot.child("name").exists()){

                        friend_profile_name.setText(dataSnapshot.child("name").getValue().toString());

                    }
                    if(dataSnapshot.child("phoneNumber").exists()){

                        friend_profile_phone.setText(dataSnapshot.child("phoneNumber").getValue().toString());

                    }

                    if(dataSnapshot.child("email").exists()){

                        friend_profile_email.setText(dataSnapshot.child("email").getValue().toString());

                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        friend_remove_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDialogRequest(Commonx.userProfile);

            }
        });




    }

    private void showDialogRequest(final User user) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this,R.style.ThemeOverlay_MaterialComponents_Dialog_Alert);


        alertDialog.setTitle("Remove friend");
        alertDialog.setMessage("Are you sure about removing your friend "+user.getEmail()+"?");


        alertDialog.setIcon(R.drawable.ic_close_pink_24dp);

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

            }
        });

        alertDialog.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                DatabaseReference friendList= FirebaseDatabase.getInstance()
                        .getReference(Commonx.USER_INFORMATION)
                        .child(Commonx.loggedUser.getUid())
                        .child(Commonx.ACCEPT_LIST);
                friendList.child(user.getUid()).removeValue();

                DatabaseReference friendList_remove_from_both= FirebaseDatabase.getInstance()
                        .getReference(Commonx.USER_INFORMATION)
                        .child(user.getUid())
                        .child(Commonx.ACCEPT_LIST);
                friendList_remove_from_both.child(Commonx.loggedUser.getUid()).removeValue();


                Toast.makeText(UserProfileActivity.this, "Removed successfully!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(UserProfileActivity.this,HomeActivity.class));



            }
        });

        alertDialog.show();

    }
}
