package com.example.freechats.okna;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.freechats.Adapter.My_users_Adapter;
import com.example.freechats.Model.Chatlist;
import com.example.freechats.Model.User;
import com.example.freechats.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class First_Activity extends AppCompatActivity {

    public Context mContext;
    private TextView username;
    private List<User> mUsers;
    private FirebaseUser fuser;
    private List<Chatlist> usersList;
    private RecyclerView recyclerView;
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;
    private Animation anim, anim2, anim3;
    private My_users_Adapter userAdapter;
    private CircleImageView profile_image;
    private LinearLayout barfirst, menu_niz;

    public ImageView add_new_users, my_groups_users,my_message_users,me_profile;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_);

        me_profile = findViewById(R.id.me_profile);
        add_new_users = findViewById(R.id.add_new_users);
        my_groups_users = findViewById(R.id.my_groups_users);
        my_message_users = findViewById(R.id.my_message_users);

        add_new_users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimBarExit();
                handler.postDelayed(mSTARtFirst, 200);

            }
        });
        my_groups_users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimBarExit();
                handler.postDelayed(mSTARtGroup, 200);

            }
        });
        my_message_users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimBarExit();
                handler.postDelayed(mSTARtMSG, 200);

            }
        });
        me_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimBarExit();
                handler.postDelayed(mSTARtProfile, 200);
            }
        });

        username = findViewById(R.id.username);
        barfirst = findViewById(R.id.barfirst);
        menu_niz = findViewById(R.id.menu_niz);
        recyclerView = findViewById(R.id.recycler_view);
        profile_image = findViewById(R.id.profile_image);
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        AnimBar();
        useImage();

        usersList = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("ChatIDlist")
                .child(fuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chatlist chatlist = snapshot.getValue(Chatlist.class);
                    usersList.add(0, chatlist);
                }
                chatList();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(llm);

        colorMenu();
    }

    private Runnable mSTARtFirst = new Runnable() {
        @Override
        public void run() {
            recyclerView.setVisibility(View.INVISIBLE);
            Intent intent = new Intent(First_Activity.this, My_Activity.class);
            startActivity(intent);
            finish();
        }
    };
    private Runnable mSTARtGroup = new Runnable() {
        @Override
        public void run() {
            recyclerView.setVisibility(View.INVISIBLE);
            Intent intent = new Intent(First_Activity.this, Groups_Activity.class);
            startActivity(intent);
            finish();
        }
    };
    private Runnable mSTARtMSG = new Runnable() {
        @Override
        public void run() {
            recyclerView.setVisibility(View.INVISIBLE);
            Intent intent1 = new Intent(First_Activity.this, First_Activity.class);
            startActivity(intent1);
            finish();
        }
    };
    private Runnable mSTARtProfile = new Runnable() {
        @Override
        public void run() {
            recyclerView.setVisibility(View.INVISIBLE);
            Intent intent1 = new Intent(First_Activity.this, Profile_Activity.class);
            startActivity(intent1);
            finish();
        }
    };

    private void AnimBar(){
        anim = AnimationUtils.loadAnimation(this, R.anim.bar_anim_first);
        anim2 = AnimationUtils.loadAnimation(this, R.anim.recacle_first);
        anim3 = AnimationUtils.loadAnimation(this, R.anim.menu_niz_first);
        barfirst.startAnimation(anim);
        recyclerView.startAnimation(anim2);
        menu_niz.startAnimation(anim3);
    }

    private void AnimBarExit(){
        anim = AnimationUtils.loadAnimation(this, R.anim.bar_anim_two);
        anim2 = AnimationUtils.loadAnimation(this, R.anim.recacler_two);
        anim3 = AnimationUtils.loadAnimation(this, R.anim.menu_niz_two);
        barfirst.startAnimation(anim);
        menu_niz.startAnimation(anim3);
        recyclerView.startAnimation(anim2);
    }

    private void colorMenu(){
        my_message_users.setImageResource(R.drawable.ic_chats_blue);
    }

    private void chatList() {

        mUsers = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                fuser = FirebaseAuth.getInstance().getCurrentUser();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);

                  for (Chatlist chatlist : usersList) {
                           if (user.getId().equals(chatlist.getId())) {
                               mUsers.add(user);
                           }
                  }
                }
                userAdapter = new My_users_Adapter(mContext, mUsers, true);
                recyclerView.setAdapter(userAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void useImage(){

        final ProgressDialog pd = new ProgressDialog(First_Activity.this);
        pd.setMessage("Uploading");
        pd.show();

        mUsers = new ArrayList<>();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                username.setText(user.getUsername());
                if (user.getImageURL().equals("default")){
                    profile_image.setImageResource(R.mipmap.ic_launcher);
                    pd.dismiss();

                } else {
                    Glide.with(getApplicationContext()).load(user.getImageURL()).into(profile_image);
                    pd.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void status(String status){
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);

        reference.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
    }
}
