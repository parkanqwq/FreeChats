package com.example.freechats.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.freechats.Groups_Message_Activity;
import com.example.freechats.Model.Groups;
import com.example.freechats.Model.User;
import com.example.freechats.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Groups_adapter extends RecyclerView.Adapter<Groups_adapter.UserViewHolder>{

    public FirebaseUser fuser;
    private List<Groups> mGroups;
    private List<User> mUsers;
    private Context mContext;
    private String theLastMessage;
    private boolean ischat;
    private DatabaseReference reference;
    private boolean NotDel = false;

    public Groups_adapter(Context mContext, List<Groups> mGroups) {
        this.mContext = mContext;
        this.mGroups = mGroups;

    }

    @NonNull
    @Override
    public Groups_adapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_item, parent, false);
        return new Groups_adapter.UserViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final Groups_adapter.UserViewHolder holder, final int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        final Groups groups = mGroups.get(position);
        holder.hou_create.setText("Group create: " + groups.getHou_create());
        holder.username.setText(groups.getUsername());
        if (groups.getImageURL().equals("default")){
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        } else {
            Glide.with(holder.profile_image).load(groups.getImageURL()).into(holder.profile_image);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                Intent intent = new Intent(view.getContext(), Groups_Message_Activity.class);
                intent.putExtra("groupsid", groups.getData_time());
                intent.putExtra("userid", groups.getId());
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mGroups.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder{

        TextView data_time;
        TextView hou_create;
        TextView username;
        ImageView img_on;
        ImageView img_off;
        TextView last_msg;
        CircleImageView profile_image;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            img_on = itemView.findViewById(R.id.img_on);
            img_off = itemView.findViewById(R.id.img_off);
            last_msg = itemView.findViewById(R.id.last_msg);
            username = itemView.findViewById(R.id.username);
            data_time = itemView.findViewById(R.id.data_time);
            hou_create = itemView.findViewById(R.id.hou_create);
            profile_image = itemView.findViewById(R.id.profile_image);
        }
    }
}
