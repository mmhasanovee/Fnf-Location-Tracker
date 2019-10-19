package xyz.mmhasanovee.fnflocationtracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import xyz.mmhasanovee.fnflocationtracker.Interface.IFirebaseLoadDone;
import xyz.mmhasanovee.fnflocationtracker.Model.User;
import xyz.mmhasanovee.fnflocationtracker.Utils.Commonx;
import xyz.mmhasanovee.fnflocationtracker.ViewHolder.FriendRequestViewHolder;
import xyz.mmhasanovee.fnflocationtracker.ViewHolder.UserViewHolder;

public class FriendRequestActivity extends AppCompatActivity implements IFirebaseLoadDone {


    FirebaseRecyclerAdapter<User, FriendRequestViewHolder> adapter,searchAdapter;
    RecyclerView recycler_all_user;
    IFirebaseLoadDone firebaseLoadDone;
    TextView friend_request_empty;



    MaterialSearchBar searchBar;
    List<String> suggestList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_request);

        /*searchBar = (MaterialSearchBar) findViewById(R.id.m_search_bar);
        searchBar.setCardViewElevation(10);
        searchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                List<String> suggest = new ArrayList<>();
                for(String search:suggestList){

                    if(search.toLowerCase().contains(searchBar.getText().toLowerCase()))
                        suggest.add(search);
                }

                searchBar.setLastSuggestions(suggest);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }


        });
        searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                if(!enabled){

                    if(adapter !=null){
                        recycler_all_user.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {

                startSearch(text,toString());

            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });*/

        recycler_all_user = (RecyclerView) findViewById(R.id.recycler_all_people);
        recycler_all_user.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recycler_all_user.setLayoutManager(layoutManager);
        recycler_all_user.addItemDecoration(new DividerItemDecoration(this,((LinearLayoutManager) layoutManager).getOrientation()));

        friend_request_empty=(TextView)findViewById(R.id.friend_request_list_is_empty);
        firebaseLoadDone = this;
        loadFriendRequestList();

        //loadSearchData();
    }



    private void loadFriendRequestList() {



        Query query = FirebaseDatabase.getInstance().getReference().child(Commonx.USER_INFORMATION)
                .child(Commonx.loggedUser.getUid())
                .child(Commonx.FRIEND_REQUEST);
        FirebaseRecyclerOptions<User> options = new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(query,User.class)
                .build();



        adapter = new FirebaseRecyclerAdapter<User, FriendRequestViewHolder>(options) {



            @Override
            protected void onBindViewHolder(@NonNull final FriendRequestViewHolder friendRequestViewHolder, int i, @NonNull final User user) {


                if(getItemCount()>0){
                    friend_request_empty.setText("You have new friend requests");
                }


                DatabaseReference referencexo = FirebaseDatabase.getInstance().getReference().child(Commonx.USER_INFORMATION);

                final Query newxo = referencexo.orderByChild("uid").equalTo(user.getUid());

                newxo.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot userx : dataSnapshot.getChildren()) {

                            if (userx.child("image").exists()) {
                                Picasso.get().load(userx.child("image").getValue().toString()).into(friendRequestViewHolder.friend_request_image);
                            }


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                friendRequestViewHolder.txt_user_email.setText(user.getEmail());
                friendRequestViewHolder.btn_accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(FriendRequestActivity.this, "User "+user.getEmail()+" added to your friend list", Toast.LENGTH_SHORT).show();
                        deleteFriendRequest(user,true);
                        addToAcceptList(user);
                        addUserToFriendContact(user);
                    }
                });

                friendRequestViewHolder.btn_decline.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //remove friend request
                        //Toast.makeText(FriendRequestActivity.this, "Friend request declined", Toast.LENGTH_SHORT).show();

                        deleteFriendRequest_2(user,true);
                        startActivity(new Intent(FriendRequestActivity.this,HomeActivity.class
                        ));
                    }
                });



            }

            @NonNull
            @Override
            public FriendRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_friend_request,parent,false);

                return new FriendRequestViewHolder(itemView);
            }


        };
        
        adapter.startListening();
        recycler_all_user.setAdapter(adapter);




    }

    private void addUserToFriendContact(User user) {
        //request sent info

        DatabaseReference acceptList = FirebaseDatabase.getInstance()
                .getReference(Commonx.USER_INFORMATION)
                .child(user.getUid())
                .child(Commonx.ACCEPT_LIST);

        acceptList.child(Commonx.loggedUser.getUid()).setValue(Commonx.loggedUser); //Commonx.loggedUser.getUid()--->user.getUid()


    }

    private void addToAcceptList(User user) {

        //request got info

        DatabaseReference acceptList = FirebaseDatabase.getInstance()
                .getReference(Commonx.USER_INFORMATION)
                .child(Commonx.loggedUser.getUid())
                .child(Commonx.ACCEPT_LIST);

        acceptList.child(user.getUid()).setValue(user);
    }

    private void deleteFriendRequest(final User user,final boolean isShowMessage) {



        DatabaseReference friendRequest= FirebaseDatabase.getInstance()
                .getReference(Commonx.USER_INFORMATION)
                .child(Commonx.loggedUser.getUid())
                .child(Commonx.FRIEND_REQUEST);
        friendRequest.child(user.getUid()).removeValue();

        DatabaseReference friendRequest_remove_from_both= FirebaseDatabase.getInstance()
                .getReference(Commonx.USER_INFORMATION)
                .child(user.getUid())
                .child(Commonx.FRIEND_REQUEST);
        friendRequest_remove_from_both.child(Commonx.loggedUser.getUid()).removeValue();


    }

    private void deleteFriendRequest_2(final User user,final boolean isShowMessage) {



        DatabaseReference friendRequest= FirebaseDatabase.getInstance()
                .getReference(Commonx.USER_INFORMATION)
                .child(Commonx.loggedUser.getUid())
                .child(Commonx.FRIEND_REQUEST);
        friendRequest.child(user.getUid()).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        if(isShowMessage)
                            Toast.makeText(FriendRequestActivity.this, "Friend request has been removed!", Toast.LENGTH_SHORT).show();
                    }
                });


    }



    @Override
    protected void onStop() {

        if(adapter!=null)
            adapter.stopListening();
        if(searchAdapter!=null)
            searchAdapter.stopListening();
        super.onStop();
    }

    private void loadSearchData() {


        final List<String> lsUserEmail = new ArrayList<>();
        DatabaseReference userList =FirebaseDatabase.getInstance().getReference().child(Commonx.USER_INFORMATION)
                .child(Commonx.loggedUser.getUid())
                .child(Commonx.FRIEND_REQUEST);
        userList.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot userSnapShot:dataSnapshot.getChildren()){
                    User user=userSnapShot.getValue(User.class);
                    lsUserEmail.add(user.getEmail());
                }
                firebaseLoadDone.onFirebaseLoadUserNameDone(lsUserEmail);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                firebaseLoadDone.onFirebaseLoadFailed(databaseError.getMessage());

            }
        });

        
    }

    @Override
    public void onFirebaseLoadUserNameDone(List<String> lstEmail) {

        searchBar.setLastSuggestions((lstEmail));

    }

    @Override
    public void onFirebaseLoadFailed(String message) {

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

    }
}
