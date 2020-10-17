package com.example.freechats.okna;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.freechats.Adapter.Groups_adapter;
import com.example.freechats.Model.Groups;
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
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Groups_Activity extends AppCompatActivity {

    public TextView username;
    public Context mContext;
    public List<User> mUsers;
    public FirebaseUser fuser;
    public Button create_group;
    private List<Groups> mGroups;
    public TextView only_my_group;
    public EditText search_groups;
    public static String usernameMy;
    public RecyclerView recyclerView;
    public FirebaseUser firebaseUser;
    public DatabaseReference reference;
    private Animation anim, anim2, anim3;
    public CircleImageView profile_image;
    private Groups_adapter groups_adapter;
    private LinearLayout barfirst, menu_niz;
    public MaterialEditText Name_groups, You_name;
    public ImageView add_new_users, my_groups_users, my_message_users, me_profile;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.groups_);

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

        fuser = FirebaseAuth.getInstance().getCurrentUser();

        username = findViewById(R.id.username);
        barfirst = findViewById(R.id.barfirst);
        menu_niz = findViewById(R.id.menu_niz);
        profile_image = findViewById(R.id.profile_image);
        recyclerView = findViewById(R.id.recycler_view);

        create_group = findViewById(R.id.create_group);
        only_my_group = findViewById(R.id.only_my_group);
        only_my_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Groups");
               reference.addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot snapshot) {
                       mGroups.clear();
                       for (DataSnapshot snapshot1 : snapshot.getChildren()){
                           Groups groups = snapshot1.getValue(Groups.class);
                           if (groups.getId().equals(fuser.getUid()))
                               mGroups.add(0, groups);
                       }
                       groups_adapter = new Groups_adapter(mContext, mGroups);
                       recyclerView.setAdapter(groups_adapter);
                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError error) {

                   }
               });
            }
        });

        search_groups = findViewById(R.id.search_groups);
        search_groups.addTextChangedListener(new TextWatcher() {
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

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(llm);
        AnimBar();
        useImage();
        grouspAll();
        colorMenu();
    }

    private Runnable mSTARtFirst = new Runnable() {
        @Override
        public void run() {
            recyclerView.setVisibility(View.INVISIBLE);
            Intent intent = new Intent(Groups_Activity.this, My_Activity.class);
            startActivity(intent);
            finish();
        }
    };
    private Runnable mSTARtGroup = new Runnable() {
        @Override
        public void run() {
            recyclerView.setVisibility(View.INVISIBLE);
            Intent intent = new Intent(Groups_Activity.this, Groups_Activity.class);
            startActivity(intent);
            finish();
        }
    };
    private Runnable mSTARtMSG = new Runnable() {
        @Override
        public void run() {
            recyclerView.setVisibility(View.INVISIBLE);
            Intent intent1 = new Intent(Groups_Activity.this, First_Activity.class);
            startActivity(intent1);
            finish();
        }
    };
    private Runnable mSTARtProfile = new Runnable() {
        @Override
        public void run() {
            recyclerView.setVisibility(View.INVISIBLE);
            Intent intent1 = new Intent(Groups_Activity.this, Profile_Activity.class);
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

    private void searchUsers(String s) {

        final FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        Query query = FirebaseDatabase.getInstance().getReference("Groups").orderByChild("search")
                .startAt(s)
                .endAt(s+"\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mGroups.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Groups groups = snapshot.getValue(Groups.class);

                        mGroups.add(0, groups);
                }

                groups_adapter = new Groups_adapter(mContext, mGroups);
                recyclerView.setAdapter(groups_adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void Create_Group(View view){
        final AlertDialog.Builder registerDialog = new AlertDialog.Builder(Groups_Activity.this);
        LayoutInflater inflater = LayoutInflater.from(Groups_Activity.this);
        View registr_window = inflater.inflate(R.layout.grousp_create_activity, null);
        registerDialog.setView(registr_window);

        Name_groups = registr_window.findViewById(R.id.name_groups);
        You_name = registr_window.findViewById(R.id.hou_create);

        registerDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogCansel, int which) {
                dialogCansel.dismiss();
            }
        });
        registerDialog.setPositiveButton("Finish", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogFinish, int which) {

                        FirebaseUser fuser2 = FirebaseAuth.getInstance().getCurrentUser();
                Date date = new Date();
                String date_for_group = date.toString();
                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("id", fuser2.getUid());
                                hashMap.put("username", Name_groups.getText().toString());
                                hashMap.put("imageURL", "default");
                                hashMap.put("hou_create", You_name.getText().toString());
                                hashMap.put("data_time", date_for_group + " : " + fuser2.getUid());
                                hashMap.put("search", Name_groups.getText().toString().toLowerCase());
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Groups")
                                        .child(date_for_group + " : " + fuser2.getUid());
                                reference.setValue(hashMap);
            }
        });
        registerDialog.show();
    }

    public void grouspAll(){

        mGroups = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Groups");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mGroups.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Groups groups = snapshot.getValue(Groups.class);
                            mGroups.add(0, groups);
                }
                groups_adapter = new Groups_adapter(mContext, mGroups);
                recyclerView.setAdapter(groups_adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void colorMenu() {
        my_groups_users.setImageResource(R.drawable.ic_group_blue);

    }

    public void useImage(){

        final ProgressDialog pd = new ProgressDialog(Groups_Activity.this);
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
                usernameMy = user.getUsername();
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