package com.example.freechats;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.freechats.Adapter.Message_groups_Adapter;
import com.example.freechats.Model.Chats_group;
import com.example.freechats.Model.Groups;
import com.example.freechats.Notifications.Client;
import com.example.freechats.Notifications.Token;
import com.example.freechats.myNoti.APIService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.freechats.okna.Groups_Activity.usernameMy;
import static com.example.freechats.Message_Activity.userid;

public class Groups_Message_Activity extends AppCompatActivity {

    APIService apiService;
    private TextView username;
    private EditText text_send;
    private FirebaseUser fuser;
    public ImageButton btn_send;
    public ImageView buttonmenu;
    public Intent intent , intent2;
    private String GroupID, userID;
    public boolean notify = false;
    private List<Chats_group> mchat;
    private RecyclerView recyclerView;
    private DatabaseReference reference;
    private CircleImageView profile_image;
    public static String newdateGroup = " ";
    private Message_groups_Adapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.groups_message_);

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        buttonmenu = findViewById(R.id.buttonmenu);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        profile_image = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);
        btn_send = findViewById(R.id.btn_send);
        text_send = findViewById(R.id.text_send);
        text_send.setMaxLines(5);

        intent = getIntent();
        GroupID = intent.getStringExtra("groupsid");
        intent2 = getIntent();
        userID = intent2.getStringExtra("userid");

        buttonmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Groups_Message_Activity.this, Group_Menu.class);
                intent.putExtra("groupsid", GroupID);
                intent.putExtra("userid", userID);
                startActivity(intent);
            }
        });

        fuser = FirebaseAuth.getInstance().getCurrentUser();

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                notify = true;
                String msg = text_send.getText().toString();
                if (!msg.equals("")){
                    sendMessage(fuser.getUid(), msg);
                } else {
                    Toast.makeText(Groups_Message_Activity.this, "You can't send empty message", Toast.LENGTH_SHORT).show();
                }
                text_send.setText("");
            }
        });

        reference = FirebaseDatabase.getInstance().getReference("Groups").child(GroupID);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Groups groups = dataSnapshot.getValue(Groups.class);
                username.setText(groups.getUsername());
                if (groups.getImageURL().equals("default")){
                    profile_image.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Glide.with(getApplicationContext()).load(groups.getImageURL()).into(profile_image);
                }
                readMesagges();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        updateToken(FirebaseInstanceId.getInstance().getToken());
    }

    private void updateToken(String token){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(fuser.getUid()).setValue(token1);
    }

    private void sendMessage(final String sender, final String message){

       DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

                Date date1 = new Date();
                Date date4 = new Date();
                SimpleDateFormat date = new SimpleDateFormat("HH:mm");
                SimpleDateFormat date3 = new SimpleDateFormat("yyyy, d MMM, EEE");

                String daTe = date.format(date1);
                String daTe3 = date3.format(date4);

                if (newdateGroup.equals(daTe3)){
                } else {
                    sendMessageOnlyDate(sender, message, daTe, daTe3, usernameMy);
                }

                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("sender", sender);
                hashMap.put("username", usernameMy);
                hashMap.put("message", message);
                hashMap.put("isseen", false);
                hashMap.put("daTe", daTe);
                hashMap.put("daTe3", daTe3);
                hashMap.put("date16", "nodate");

                reference.child("GroupsChats").child(GroupID).push().setValue(hashMap);
            }

    public void sendMessageOnlyDate(String sender, String message,String daTe, String daTe3, String user){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("username", user);
        hashMap.put("message", message);
        hashMap.put("isseen", false);
        hashMap.put("daTe", daTe);
        hashMap.put("daTe3", daTe3);
        hashMap.put("date16", "date");
        reference.child("GroupsChats").child(GroupID).push().setValue(hashMap);
    }

    private void readMesagges(){
        mchat = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("GroupsChats").child(GroupID);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mchat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chats_group chats_group = snapshot.getValue(Chats_group.class);
                        mchat.add(chats_group);

                    messageAdapter = new Message_groups_Adapter(Groups_Message_Activity.this, mchat);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void currentUser(String userid){
        SharedPreferences.Editor editor = getSharedPreferences("PREFS", MODE_PRIVATE).edit();
        editor.putString("currentuser", userid);
        editor.apply();
    }

    private void status(String status){
        reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);

        reference.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
        currentUser(userid);
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
        currentUser("none");
    }

}
