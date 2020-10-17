package com.example.freechats.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.freechats.Message_Activity;
import com.example.freechats.Model.Chat;
import com.example.freechats.Model.User;
import com.example.freechats.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class My_users_Adapter extends RecyclerView.Adapter<My_users_Adapter.UserViewHolder>{

    private FirebaseUser fuser;
    private List<User> mUsers;
    private Context mContext;
    private String theLastMessage;
    private boolean ischat;

    public My_users_Adapter(Context mContext, List<User> mUsers, boolean ischat) {
        this.mContext = mContext;
        this.mUsers = mUsers;
        this.ischat = ischat;

    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new My_users_Adapter.UserViewHolder(view);    }

    @Override
    public void onBindViewHolder(@NonNull final UserViewHolder holder, final int position) {
       final User user = mUsers.get(position);
       fuser = FirebaseAuth.getInstance().getCurrentUser();

        holder.username.setText(user.getUsername());
        if (user.getImageURL().equals("default")){
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(holder.profile_image).load(user.getImageURL()).into(holder.profile_image);
        }

        if (ischat){
            lastMessage(user.getId(), holder.last_msg);
        } else {
            holder.last_msg.setVisibility(View.GONE);
        }

        if (ischat){
            if (user.getStatus().equals("online")){
                holder.img_on.setVisibility(View.VISIBLE);
                holder.img_off.setVisibility(View.GONE);
            } else {
                holder.img_on.setVisibility(View.GONE);
                holder.img_off.setVisibility(View.VISIBLE);
            }
        } else {
            holder.img_on.setVisibility(View.GONE);
            holder.img_off.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), Message_Activity.class);
                intent.putExtra("userid", user.getId());
                intent.putExtra("username", user.getUsername());
                intent.putExtra("imageurl", user.getImageURL());
                intent.putExtra("status", user.getStatus());
                intent.putExtra("search", user.getSearch());
                view.getContext().startActivity(intent);
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View view) {

                AlertDialog.Builder registerDialog = new AlertDialog.Builder(view.getContext())
                        .setCancelable(false);
                LayoutInflater inflater = LayoutInflater.from(view.getContext());
                View registr_window = inflater.inflate(R.layout.act, null);
                registerDialog.setView(registr_window);

                holder.loautItemUsers.setBackgroundColor(Color.parseColor("#D7DADF"));

                registerDialog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogCansel, int which) {

                        holder.loautItemUsers.setBackgroundColor(Color.parseColor("#FAFAFA"));
                        dialogCansel.dismiss();
                    }
                });

                registerDialog.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogFinish, int which) {

                        final AlertDialog.Builder registerDialog = new AlertDialog.Builder(view.getContext())
                                .setCancelable(false);
                        LayoutInflater inflater = LayoutInflater.from(view.getContext());
                        View registr_window = inflater.inflate(R.layout.delet_group_, null);
                        registerDialog.setView(registr_window);
                        registerDialog.setNegativeButton("No!!!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogCansel, int which) {

                                holder.loautItemUsers.setBackgroundColor(Color.parseColor("#FAFAFA"));
                                dialogCansel.dismiss();
                            }
                        });
                        registerDialog.setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogFinish, int which) {

                                    holder.loautItemUsers.setBackgroundColor(Color.parseColor("#FAFAFA"));

                                final DatabaseReference reference = FirebaseDatabase.getInstance()
                                        .getReference("ChatIDlist")
                                        .child(fuser.getUid())
                                        .child(user.getId());
                                    reference.removeValue();

                                Toast.makeText(view.getContext(), "Message users delete", Toast.LENGTH_SHORT).show();

                            }
                        });
                        registerDialog.show();
                    }
                });
                registerDialog.show();

                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder{

        RelativeLayout loautItemUsers;
        TextView username;
        CircleImageView profile_image;
        ImageView img_on;
        ImageView img_off;
        TextView last_msg;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            loautItemUsers = itemView.findViewById(R.id.loautItemUsers);
            img_on = itemView.findViewById(R.id.img_on);
            img_off = itemView.findViewById(R.id.img_off);
            last_msg = itemView.findViewById(R.id.last_msg);
            username = itemView.findViewById(R.id.username);
            profile_image = itemView.findViewById(R.id.profile_image);
        }
    }

    private void lastMessage(final String userid, final TextView last_msg){
        theLastMessage = "default";
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ChatIDuser")
                .child(firebaseUser.getUid())
                .child(userid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);
                    if (firebaseUser != null && chat != null) {
                        if (chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid) ||
                                chat.getReceiver().equals(userid) && chat.getSender().equals(firebaseUser.getUid())) {
                            theLastMessage = chat.getMessage();

                        }
                    }
                }

                switch (theLastMessage){
                    case  "default":
                        last_msg.setText("No Message");
                        break;

                    default:
                        last_msg.setText(theLastMessage);

                        break;
                }

                theLastMessage = "default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
