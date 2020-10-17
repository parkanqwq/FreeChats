package com.example.freechats.okna;


import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.freechats.MainActivity;
import com.example.freechats.Model.User;
import com.example.freechats.R;
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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile_Activity extends AppCompatActivity {

    private Uri imageUri;
    public Context mContext;
    private FirebaseUser fuser;
    private StorageTask uploadTask;
    private LinearLayout recycler_view;
    private DatabaseReference reference;
    private Animation anim, anim2, anim3;
    private CircleImageView image_profile;
    public TextView username, exit_profile;
    private Handler handler = new Handler();
    private LinearLayout barfirst, menu_niz;
    public StorageReference storageReference;
    private static final int IMAGE_REQUEST = 999;
    public ImageView add_new_users, my_groups_users,my_message_users,me_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_);

        exit_profile = findViewById(R.id.exit_profile);
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

        exit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Profile_Activity.this, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                finish();
            }
        });

        barfirst = findViewById(R.id.barfirst);
        menu_niz = findViewById(R.id.menu_niz);
        recycler_view = findViewById(R.id.recycler_view);
        image_profile = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);

        storageReference = FirebaseStorage.getInstance().getReference("uploads");

        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                username.setText(user.getUsername());
                if (user.getImageURL().equals("default")){
                    image_profile.setImageResource(R.mipmap.ic_launcher);
                } else {
                    Glide.with(getApplicationContext()).load(user.getImageURL()).into(image_profile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        image_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImage();
            }
        });

        colorMenu();
        AnimBar();
    }

    private Runnable mSTARtFirst = new Runnable() {
        @Override
        public void run() {
            recycler_view.setVisibility(View.INVISIBLE);
            Intent intent = new Intent(Profile_Activity.this, My_Activity.class);
            startActivity(intent);
            finish();
        }
    };
    private Runnable mSTARtGroup = new Runnable() {
        @Override
        public void run() {
            recycler_view.setVisibility(View.INVISIBLE);
            Intent intent = new Intent(Profile_Activity.this, Groups_Activity.class);
            startActivity(intent);
            finish();
        }
    };
    private Runnable mSTARtMSG = new Runnable() {
        @Override
        public void run() {
            recycler_view.setVisibility(View.INVISIBLE);
            Intent intent1 = new Intent(Profile_Activity.this, First_Activity.class);
            startActivity(intent1);
            finish();
        }
    };
    private Runnable mSTARtProfile = new Runnable() {
        @Override
        public void run() {
            recycler_view.setVisibility(View.INVISIBLE);
            Intent intent1 = new Intent(Profile_Activity.this, Profile_Activity.class);
            startActivity(intent1);
            finish();
        }
    };

    private void AnimBar(){
        anim = AnimationUtils.loadAnimation(this, R.anim.bar_anim_first);
        anim2 = AnimationUtils.loadAnimation(this, R.anim.recacle_first);
        anim3 = AnimationUtils.loadAnimation(this, R.anim.menu_niz_first);
        barfirst.startAnimation(anim);
        recycler_view.startAnimation(anim2);
        menu_niz.startAnimation(anim3);
    }

    private void AnimBarExit(){
        anim = AnimationUtils.loadAnimation(this, R.anim.bar_anim_two);
        anim2 = AnimationUtils.loadAnimation(this, R.anim.recacler_two);
        anim3 = AnimationUtils.loadAnimation(this, R.anim.menu_niz_two);
        barfirst.startAnimation(anim);
        recycler_view.startAnimation(anim2);
        menu_niz.startAnimation(anim3);
    }

    private void colorMenu() {
        me_profile.setImageResource(R.drawable.ic_profile_blue);

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

                        reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("imageURL", ""+mUri);
                        reference.updateChildren(map);

                        pd.dismiss();
                    } else {
                        Toast.makeText(Profile_Activity.this, "Failed!", Toast.LENGTH_SHORT).show();
                        pd.dismiss();

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Profile_Activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        } else {
            Toast.makeText(Profile_Activity.this, "No image selected", Toast.LENGTH_SHORT).show();
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
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
    }
}


