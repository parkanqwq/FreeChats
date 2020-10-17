package com.example.freechats.okna;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.freechats.Adapter.My_users_Adapter;
import com.example.freechats.Model.User;
import com.example.freechats.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class My_Activity extends AppCompatActivity {

    public Context mContext;
    private TextView username;
    private List<User> mUsers;
    public EditText search_users;
    private FirebaseUser firebaseUser;
    private RecyclerView recyclerView;
    private DatabaseReference reference;
    private Animation anim, anim2, anim3;
    private CircleImageView profile_image;
    private My_users_Adapter user_Adapter;
    private LinearLayout barfirst, menu_niz;
    private Handler handler = new Handler();
    public ImageView add_new_users, my_groups_users,my_message_users,me_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_);

        add_new_users = findViewById(R.id.add_new_users);
        my_groups_users = findViewById(R.id.my_groups_users);
        my_message_users = findViewById(R.id.my_message_users);
        me_profile = findViewById(R.id.me_profile);

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
        search_users = findViewById(R.id.search_users);
        recyclerView = findViewById(R.id.recycler_view);
        profile_image = findViewById(R.id.profile_image);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(llm);

        readUsers();
        useImage();

        search_users.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchUsers(charSequence.toString().toLowerCase());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        colorMenu();
        AnimBar();
    }

    private Runnable mSTARtFirst = new Runnable() {
        @Override
        public void run() {
            recyclerView.setVisibility(View.INVISIBLE);
            Intent intent = new Intent(My_Activity.this, My_Activity.class);
            startActivity(intent);
            finish();
        }
    };
    private Runnable mSTARtGroup = new Runnable() {
        @Override
        public void run() {
            recyclerView.setVisibility(View.INVISIBLE);
            Intent intent = new Intent(My_Activity.this, Groups_Activity.class);
            startActivity(intent);
            finish();
        }
    };
    private Runnable mSTARtMSG = new Runnable() {
        @Override
        public void run() {
            recyclerView.setVisibility(View.INVISIBLE);
            Intent intent1 = new Intent(My_Activity.this, First_Activity.class);
            startActivity(intent1);
            finish();
        }
    };
    private Runnable mSTARtProfile = new Runnable() {
        @Override
        public void run() {
            recyclerView.setVisibility(View.INVISIBLE);
            Intent intent1 = new Intent(My_Activity.this, Profile_Activity.class);
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
        recyclerView.startAnimation(anim2);
        menu_niz.startAnimation(anim3);
    }

    private void colorMenu(){
        add_new_users.setImageResource(R.drawable.ic_person_add_blue);
    }

    private void searchUsers(String s) {

        final FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("search")
                .startAt(s)
                .endAt(s+"\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);

                    assert user != null;
                    assert fuser != null;
                    if (!user.getId().equals(fuser.getUid())){
                        mUsers.add(user);
                    }
                }

                user_Adapter = new My_users_Adapter(mContext, mUsers, true);
                recyclerView.setAdapter(user_Adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void useImage(){
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
                } else {
                    Glide.with(getApplicationContext()).load(user.getImageURL()).into(profile_image);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void readUsers() {
        mUsers = new ArrayList<>();
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUsers.clear();
                for (DataSnapshot u :snapshot.getChildren()){
                    User user = u.getValue(User.class);
                    if (!user.getId().equals(firebaseUser.getUid())) {
                        mUsers.add(user);
                    }
                }
                user_Adapter = new My_users_Adapter(mContext, mUsers, true);
                recyclerView.setAdapter(user_Adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
