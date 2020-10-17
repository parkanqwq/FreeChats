package com.example.freechats;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.freechats.Adapter.MessageAdapter;
import com.example.freechats.Model.Chat;
import com.example.freechats.Model.User;
import com.example.freechats.Notifications.Client;
import com.example.freechats.Notifications.Data;
import com.example.freechats.Notifications.MyResponse;
import com.example.freechats.Notifications.Sender;
import com.example.freechats.Notifications.Token;
import com.example.freechats.myNoti.APIService;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Message_Activity extends AppCompatActivity {

    private Uri imageUri;
    APIService apiService;
    public Context mContext;
    public List<Chat> mchat;
    public Button buttonDate;
    public ImageView add_foto;
    private TextView username;
    private EditText text_send;
    private FirebaseUser fuser;
    public ImageButton btn_send;
    public static String userid;
    public ImageView menuMessage;
    private StorageTask uploadTask;
    private RecyclerView recyclerView;
    private DatabaseReference reference;
    public CircleImageView image_profile;
    public MessageAdapter messageAdapter;
    private CircleImageView profile_image;
    public static CardView menuMessageCard;
    public ValueEventListener seenListener;
    private DatabaseReference referenceSeen;
    private static final int IMAGE_REQUEST = 999;
    public String  userName, imageurl, status, searche;
    public Intent intent, intent2, intent3, intent4, intent5;
    public static StorageReference storageReference = FirebaseStorage.getInstance().getReference("uploads");

    public static String newdate = " ";
    public static boolean dateShow = false;
    boolean menuMesCard = false;
    boolean notify = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_);

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        menuMessageCard = findViewById(R.id.menuMessageCard);
        menuMessage = findViewById(R.id.menuMessage);
        profile_image = findViewById(R.id.profile_image);
        image_profile = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);
        btn_send = findViewById(R.id.btn_send);
        text_send = findViewById(R.id.text_send);
        text_send.setMaxLines(5);
        add_foto = findViewById(R.id.add_foto);

        intent = getIntent();
        intent2 = getIntent();
        intent3 = getIntent();
        intent4 = getIntent();
        intent5 = getIntent();
        userid = intent.getStringExtra("userid");
        userName = intent.getStringExtra("username");
        imageurl = intent.getStringExtra("imageurl");
        status = intent.getStringExtra("status");
        searche = intent.getStringExtra("search");

        fuser = FirebaseAuth.getInstance().getCurrentUser();

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notify = true;
                String msg = text_send.getText().toString();
                if (!msg.equals("")){
                    sendMessage(fuser.getUid(), userid, msg, "default");
                } else {
                    Toast.makeText(Message_Activity.this, "You can't send empty message", Toast.LENGTH_SHORT).show();
                }
                text_send.setText("");
            }
        });

        reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

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
                readMesagges(fuser.getUid(), userid, user.getImageURL());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        buttonDate = findViewById(R.id.buttonDate);
        buttonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        menuMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (menuMesCard == false){
                    menuMessageCard.setVisibility(View.VISIBLE);
                    menuMesCard = true;
                } else {
                    menuMessageCard.setVisibility(View.GONE);
                    menuMesCard = false;

                }
            }
        });
        seenMessage(userid);
        updateToken(FirebaseInstanceId.getInstance().getToken());
        add_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImage();
            }
        });


    }

    private void updateToken(String token){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(fuser.getUid()).setValue(token1);
    }

    private void seenMessage(final String userid){
        referenceSeen = FirebaseDatabase.getInstance().getReference("ChatIDuser")
                .child(userid)
                .child(fuser.getUid());
       seenListener = referenceSeen.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(fuser.getUid()) && chat.getSender().equals(userid)){
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("isseen", true);
                        snapshot.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void sendMessage(String sender, final String receiver, String message, String fotoURL){

        Date date1 = new Date();
        Date date0 = new Date();
        Date date4 = new Date();
        SimpleDateFormat date = new SimpleDateFormat("HH:mm");
        SimpleDateFormat date2 = new SimpleDateFormat("dd.MM");
        SimpleDateFormat date3 = new SimpleDateFormat("yyyy, d MMM, EEE");

        String daTe = date.format(date1);
        String daTe2 = date2.format(date0);
        String daTe3 = date3.format(date4);

        if (newdate.equals(daTe3)){

        } else {

            sendMessageOnlyDate(sender, receiver, fotoURL, daTe, daTe2 , daTe3);
        }

        HashMap<String, Object> hashMapChatRef = new HashMap<>();
        hashMapChatRef.put("sender", sender);
        hashMapChatRef.put("receiver", receiver);
        hashMapChatRef.put("message", message);
        hashMapChatRef.put("isseen", false);
        hashMapChatRef.put("daTe", daTe);
        hashMapChatRef.put("daTe2", daTe2 + "  ");
        hashMapChatRef.put("daTe3", daTe3);
        hashMapChatRef.put("fotoURL", fotoURL);
        hashMapChatRef.put("date16", "nodate");


        DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("ChatIDuser")
                .child(fuser.getUid())
                .child(userid)
                .push();
        chatRef.setValue(hashMapChatRef);

        DatabaseReference chatRefReceiver = FirebaseDatabase.getInstance().getReference("ChatIDuser")
                .child(userid)
                .child(fuser.getUid())
                .push();
        chatRefReceiver.setValue(hashMapChatRef);

        HashMap<String, Object> hashMapIDlist = new HashMap<>();
        hashMapIDlist.put("id", userid);

        DatabaseReference chatIdlist = FirebaseDatabase.getInstance().getReference("ChatIDlist")
                .child(fuser.getUid())
                .child(userid);
        chatIdlist.setValue(hashMapIDlist);

        HashMap<String, Object> hashMapIDlist2 = new HashMap<>();
        hashMapIDlist2.put("id", fuser.getUid());

        DatabaseReference chatIdlistReceiver = FirebaseDatabase.getInstance().getReference("ChatIDlist")
                .child(userid)
                .child(fuser.getUid());
        chatIdlistReceiver.setValue(hashMapIDlist2);

        final String msg = message;
        reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (notify) {

                    assert user != null;

                    sendNotifiaction(receiver, user.getUsername(), msg , user.getImageURL());
                }
                notify = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void sendMessageOnlyDate(String sender, String receiver, String fotoURL,
    String daTe, String daTe2 , String daTe3){

        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", "datMSGHere");
        hashMap.put("isseen", false);
        hashMap.put("daTe", daTe);
        hashMap.put("daTe2", daTe2 + "  ");
        hashMap.put("daTe3", daTe3);
        hashMap.put("fotoURL", fotoURL);
        hashMap.put("date16", "date");

        DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("ChatIDuser")
                .child(fuser.getUid())
                .child(userid)
                .push();
        chatRef.setValue(hashMap);

        DatabaseReference chatRefReceiver = FirebaseDatabase.getInstance().getReference("ChatIDuser")
                .child(userid)
                .child(fuser.getUid())
                .push();
        chatRefReceiver.setValue(hashMap);

    }

//new herr
    private void sendNotifiaction(String receiver, final String username, final String message, final String iconl){
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(fuser.getUid(), R.mipmap.ic_launcher, "MSG: " + message, "Name: " + username,
                            userid, iconl);

                    Sender sender = new Sender(data, token.getToken());

                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if (response.code() == 200){
                                        if (response.body().success != 1){
                                            Toast.makeText(Message_Activity.this, "Failed!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {

                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readMesagges(final String myid, final String userid, final String imageurl){
        mchat = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("ChatIDuser")
                .child(myid)
                .child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mchat.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                        mchat.add(chat);

                    messageAdapter = new MessageAdapter(Message_Activity.this, mchat, imageurl);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void openImage() {
        Intent intent =  new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getApplicationContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage(){
        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Uploading");
        pd.show();

        if (imageUri != null){
            final  StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    +"."+getFileExtension(imageUri));

            uploadTask = fileReference.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw  task.getException();
                    }

                    return  fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        String mUri = downloadUri.toString();

                       // отправка Фото в виде сообщения
                       sendMessage(fuser.getUid(), userid, "Photo", ""+mUri);

                        pd.dismiss();
                    } else {
                        Toast.makeText(Message_Activity.this, "Failed!", Toast.LENGTH_SHORT).show();
                        pd.dismiss();

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Message_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        } else {
            Toast.makeText(Message_Activity.this, "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null){
            imageUri = data.getData();

            if (uploadTask != null && uploadTask.isInProgress()){
            } else {
                uploadImage();
            }
        }
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
      referenceSeen.removeEventListener(seenListener);
        status("offline");
        currentUser("none");

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
