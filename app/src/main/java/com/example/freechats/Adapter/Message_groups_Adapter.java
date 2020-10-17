package com.example.freechats.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.freechats.Groups_Message_Activity;
import com.example.freechats.Model.Chats_group;
import com.example.freechats.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class Message_groups_Adapter extends RecyclerView.Adapter<Message_groups_Adapter.ViewHolder> {

    public static  final int MSG_TYPE_LEFT = 0;
    public static  final int MSG_TYPE_RIGHT = 1;

    private Context mContext;
    private List<Chats_group> mChat;

    private int coutDate = 1;
    FirebaseUser fuser;

    public Message_groups_Adapter(Context mContext, List<Chats_group> mChat){
        this.mChat = mChat;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public Message_groups_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_right_group, parent, false);
            return new Message_groups_Adapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_left_group, parent, false);
            return new Message_groups_Adapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull Message_groups_Adapter.ViewHolder holder, int position) {

        Chats_group chat = mChat.get(position);

        holder.daTe3R.setVisibility(View.GONE);
        holder.show_message.setVisibility(View.GONE);
        holder.daTe.setVisibility(View.GONE);
        try {
            holder.nameuser.setVisibility(View.GONE);
        } catch (Exception e) {}


        if (coutDate == 1) {
            Groups_Message_Activity.newdateGroup = chat.getDaTe3();
            coutDate = 2;
        }

        try {
            holder.nameuser.setText(chat.getUsername());
        } catch (Exception e) {}
        holder.show_message.setText(chat.getMessage());
        holder.daTe.setText(chat.getDaTe());

        try {
            if (chat.getDate16().equals("nodate")) {
                holder.show_message.setVisibility(View.VISIBLE);
                holder.daTe.setVisibility(View.VISIBLE);
                try {
                    holder.nameuser.setVisibility(View.VISIBLE);
                } catch (Exception e) {}

            } else {
                try {
                    holder.nameuser.setVisibility(View.GONE);
                } catch (Exception e) {}
                holder.show_message.setVisibility(View.GONE);
                holder.daTe.setVisibility(View.GONE);
                holder.daTe3R.setText(chat.getDaTe3());
                holder.daTe3R.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {}
    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView daTe, daTe3R;
        TextView nameuser;
        TextView show_message;
        ImageView profile_image;
        TextView txt_seen;

        public ViewHolder(View itemView) {
            super(itemView);

            daTe3R = itemView.findViewById(R.id.daTe3R);
            daTe = itemView.findViewById(R.id.daTe);
            nameuser = itemView.findViewById(R.id.nameuser);
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
