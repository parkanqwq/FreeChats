package com.example.freechats.Adapter;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.freechats.ImageMessage;
import com.example.freechats.Message_Activity;
import com.example.freechats.Model.Chat;
import com.example.freechats.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static  final int MSG_TYPE_LEFT = 0;
    public static  final int MSG_TYPE_RIGHT = 1;

    private Context mContext;
    private List<Chat> mChat;
    private String imageurl;
    private int coutDate = 1;
    private FirebaseUser fuser;

    public MessageAdapter(Context mContext, List<Chat> mChat, String imageurl){
        this.mChat = mChat;
        this.mContext = mContext;
        this.imageurl = imageurl;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_right, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }  else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_left, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final MessageAdapter.ViewHolder holder, final int position) {

        final Chat chat = mChat.get(position);

        holder.show_message.setVisibility(View.GONE);
        holder.daTe.setVisibility(View.GONE);
        holder.daTeImage.setVisibility(View.GONE);
        holder.add_loat_foto.setVisibility(View.GONE);
        holder.add_foto.setVisibility(View.GONE);

        holder.show_message.setText(chat.getMessage());
        holder.daTeImage.setText(chat.getDaTe());

        holder.daTe3R.setVisibility(View.GONE);

             if (coutDate == 1) {

                    Message_Activity.newdate = chat.getDaTe3();
                    coutDate = 2;
                }

            if (chat.getDate16().equals("nodate")) {
                holder.show_message.setVisibility(View.VISIBLE);
                holder.daTe.setVisibility(View.VISIBLE);
            } else {
                holder.show_message.setVisibility(View.GONE);
                holder.daTe.setVisibility(View.GONE);
                holder.daTe3R.setText(chat.getDaTe3());
                holder.daTe3R.setVisibility(View.VISIBLE);
            }


                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        if (chat.getDate16().equals("nodate")) {
                            final AlertDialog.Builder registerDialog = new AlertDialog.Builder(mContext)
                                    .setCancelable(false);
                            LayoutInflater inflater = LayoutInflater.from(mContext);
                            View registr_window = inflater.inflate(R.layout.act, null);
                            registerDialog.setView(registr_window);
                            try {
                                holder.chatItemLeft.setBackgroundColor(Color.parseColor("#D7DADF"));
                            } catch (Exception e) {
                            }
                            try {
                                holder.chatItemRight.setBackgroundColor(Color.parseColor("#D7DADF"));
                            } catch (Exception e) {
                            }
                            registerDialog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogCansel, int which) {
                                    try {
                                        holder.chatItemLeft.setBackgroundColor(Color.parseColor("#FAFAFA"));
                                    } catch (Exception e) {
                                    }
                                    try {
                                        holder.chatItemRight.setBackgroundColor(Color.parseColor("#FAFAFA"));
                                    } catch (Exception e) {
                                    }

                                    dialogCansel.dismiss();

                                }
                            });

                            registerDialog.setNegativeButton("Copy", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogCansel, int which) {
                                    try {
                                        holder.chatItemLeft.setBackgroundColor(Color.parseColor("#FAFAFA"));
                                    } catch (Exception e) {
                                    }
                                    try {
                                        holder.chatItemRight.setBackgroundColor(Color.parseColor("#FAFAFA"));
                                    } catch (Exception e) {
                                    }

                                    Toast.makeText(mContext, "Copy: " + chat.getMessage(), Toast.LENGTH_SHORT).show();

                                    ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                                    ClipData clip = ClipData.newPlainText("", chat.getMessage());
                                    clipboard.setPrimaryClip(clip);

                                    dialogCansel.dismiss();

                                }
                            });

                            if (chat.getSender().equals(fuser.getUid())) {
                                registerDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogFinish, int which) {

                                        final AlertDialog.Builder registerDialog = new AlertDialog.Builder(mContext)
                                                .setCancelable(false);
                                        LayoutInflater inflater = LayoutInflater.from(mContext);
                                        View registr_window = inflater.inflate(R.layout.delet_group_, null);
                                        registerDialog.setView(registr_window);
                                        registerDialog.setNegativeButton("No!!!", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogCansel, int which) {
                                                try {
                                                    holder.chatItemLeft.setBackgroundColor(Color.parseColor("#FAFAFA"));
                                                } catch (Exception e) {
                                                }
                                                try {
                                                    holder.chatItemRight.setBackgroundColor(Color.parseColor("#FAFAFA"));
                                                } catch (Exception e) {
                                                }
                                                dialogCansel.dismiss();
                                            }
                                        });
                                        registerDialog.setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogFinish, int which) {
                                                try {
                                                    holder.chatItemLeft.setBackgroundColor(Color.parseColor("#FAFAFA"));
                                                } catch (Exception e) {
                                                }
                                                try {
                                                    holder.chatItemRight.setBackgroundColor(Color.parseColor("#FAFAFA"));
                                                } catch (Exception e) {
                                                }

                                                try {
                                                    FirebaseStorage mSorage;
                                                    mSorage = FirebaseStorage.getInstance();
                                                    StorageReference storageRef = mSorage.getReferenceFromUrl(chat.getFotoURL());
                                                    storageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {

                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception exception) {

                                                        }
                                                    });
                                                } catch (Exception e) {
                                                }

                                                final DatabaseReference reference = FirebaseDatabase.getInstance()
                                                        .getReference("ChatIDuser")
                                                        .child(fuser.getUid())
                                                        .child(Message_Activity.userid);
                                                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                        for (DataSnapshot ds : snapshot.getChildren()) {
                                                            String keyDelete = ds.getKey();
                                                            Chat item = ds.getValue(Chat.class);
                                                            if (chat.getMessage().equals(item.getMessage()) && chat.getDaTe().equals(item.getDaTe())
                                                                    && chat.getDaTe2().equals(item.getDaTe2()) && chat.getSender().equals(fuser.getUid())) {
                                                                reference.child(keyDelete).removeValue();
                                                                break;
                                                            }
                                                            if (chat.getMessage().equals(item.getMessage() + " ") && chat.getDaTe().equals(item.getDaTe())
                                                                    && chat.getDaTe2().equals(item.getDaTe2()) && chat.getSender().equals(fuser.getUid())) {
                                                                reference.child(keyDelete).removeValue();
                                                                break;
                                                            }

                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });

                                                final DatabaseReference reference2 = FirebaseDatabase.getInstance()
                                                        .getReference("ChatIDuser")
                                                        .child(Message_Activity.userid)
                                                        .child(fuser.getUid());
                                                reference2.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                        for (DataSnapshot ds : snapshot.getChildren()) {
                                                            String keyDelete = ds.getKey();
                                                            Chat item = ds.getValue(Chat.class);
                                                            if (chat.getMessage().equals(item.getMessage()) && chat.getDaTe().equals(item.getDaTe())
                                                                    && chat.getDaTe2().equals(item.getDaTe2()) && chat.getSender().equals(fuser.getUid())) {
                                                                reference2.child(keyDelete).removeValue();
                                                                break;
                                                            }
                                                            if (chat.getMessage().equals(item.getMessage() + " ") && chat.getDaTe().equals(item.getDaTe())
                                                                    && chat.getDaTe2().equals(item.getDaTe2()) && chat.getSender().equals(fuser.getUid())) {
                                                                reference2.child(keyDelete).removeValue();
                                                                break;
                                                            }

                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });

                                                Toast.makeText(mContext, "Message deleted", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        registerDialog.show();
                                    }
                                });
                            }
                            registerDialog.show();

                        }
                        return true;
                    }
                });


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (chat.getFotoURL().equals("default")){
                    } else {
                        Intent intent = new Intent(mContext, ImageMessage.class);
                        intent.putExtra("imageURL", chat.getFotoURL());
                        mContext.startActivity(intent);
                    }

                }
            });

        try {
            holder.dateRight.setText(chat.getDaTe2());
            holder.dateRightImage.setText(chat.getDaTe2());
        } catch (Exception e){}

        try {
            holder.dateLeft.setText(chat.getDaTe2());
            holder.dateLeftImage.setText(chat.getDaTe2());
        } catch (Exception e){}


        if (chat.getFotoURL().equals("default")){

        } else {
            holder.add_loat_foto.setVisibility(View.VISIBLE);
            holder.add_foto.setVisibility(View.VISIBLE);
            holder.daTeImage.setVisibility(View.VISIBLE);
            chat.setMessage(chat.getMessage()+" ");
            Glide.with(mContext.getApplicationContext()).load(chat.getFotoURL()).into(holder.add_foto);
        }


        holder.daTe.setText(chat.getDaTe());


        if (imageurl.equals("default")){
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        } else {
           Glide.with(mContext.getApplicationContext()).load(imageurl).into(holder.profile_image);
        }

        try {
            if (chat.getFotoURL().equals("default")) {
                if (position == mChat.size() - 1) {
                    if (chat.isIsseen()) {
                        holder.txt_seen.setText("Seen");
                    } else {
                        holder.txt_seen.setText("Delivered");
                    }
                } else {
                    holder.txt_seen.setVisibility(View.GONE);
                }
            } else {
                if (position == mChat.size() - 1) {
                    if (chat.isIsseen()) {
                        holder.txt_seen_Image.setText("Seen");
                    } else {
                        holder.txt_seen_Image.setText("Delivered");
                    }
                } else {
                    holder.txt_seen_Image.setVisibility(View.GONE);
                }
            }
        } catch (Exception e){}

    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

     class ViewHolder extends RecyclerView.ViewHolder{

        TextView daTe3R, daTeImage;
        ImageView add_foto;
        LinearLayout add_loat_foto;
        RelativeLayout chatItemRight, chatItemLeft;
        TextView daTe, dateLeft, dateLeftImage, dateRight, dateRightImage;
        TextView show_message;
        ImageView profile_image;
        TextView txt_seen, txt_seen_Image;

        public ViewHolder(View itemView) {
            super(itemView);

            daTeImage = itemView.findViewById(R.id.daTeImage);
            daTe3R = itemView.findViewById(R.id.daTe3R);
            dateLeftImage = itemView.findViewById(R.id.dateLeftImage);
            txt_seen_Image = itemView.findViewById(R.id.txt_seen_Image);
            dateRightImage = itemView.findViewById(R.id.dateRightImage);
            add_loat_foto = itemView.findViewById(R.id.add_loat_foto);
            add_foto = itemView.findViewById(R.id.add_foto);
            chatItemRight = itemView.findViewById(R.id.chatItemRight);
            chatItemLeft = itemView.findViewById(R.id.chatItemLeft);
            dateLeft = itemView.findViewById(R.id.dateLeft);
            dateRight = itemView.findViewById(R.id.dateRight);
            daTe = itemView.findViewById(R.id.daTe);
            show_message = itemView.findViewById(R.id.show_message);
            profile_image = itemView.findViewById(R.id.profile_image);
            txt_seen = itemView.findViewById(R.id.txt_seen);
        }
    }

    @Override
    public int getItemViewType(int position) {

            fuser = FirebaseAuth.getInstance().getCurrentUser();
            if (mChat.get(position).getSender().equals(fuser.getUid())){
                return MSG_TYPE_RIGHT;
            } else {
                return MSG_TYPE_LEFT;
            }

    }
}