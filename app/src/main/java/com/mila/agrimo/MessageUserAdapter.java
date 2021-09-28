package com.mila.agrimo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageUserAdapter extends RecyclerView.Adapter<MessageUserAdapter.MessageUserHolding> {
    private Context context;
    private List<MyMessageUser> myMessageUsers;

    public MessageUserAdapter(Context context, List<MyMessageUser> myMessageUsers) {
        this.context = context;
        this.myMessageUsers = myMessageUsers;
    }

    @NonNull
    @NotNull
    @Override
    public MessageUserHolding onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_users, parent, false);
        return new MessageUserHolding(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MessageUserAdapter.MessageUserHolding holder, int position) {

        if (myMessageUsers.get(position).getImage().isEmpty()){

        }
        else {
            Glide.with(context).load(myMessageUsers.get(position).getImage()).
                    into(holder.chatProfile);
        }

        holder.messageUserName.setText(myMessageUsers.get(position).getMessageUsername());
        holder.lastMessage.setText(myMessageUsers.get(position).getLastMessage());
        holder.lastTime.setText(myMessageUsers.get(position).getLastTime());

        holder.myMessageUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("id", myMessageUsers.get(position).getId());
                context.startActivity(intent);
                ((Activity)context).finish();
            }
        });

    }

    @Override
    public int getItemCount() {
        return myMessageUsers.size();
    }

  class MessageUserHolding extends RecyclerView.ViewHolder{
        TextView messageUserName, lastMessage, lastTime;
        CardView myMessageUsers;
        CircleImageView chatProfile;

        public MessageUserHolding(@NonNull @NotNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            chatProfile = itemView.findViewById(R.id.chatProfile);
            messageUserName = itemView.findViewById(R.id.messageUser);
            lastMessage = itemView.findViewById(R.id.lastMessage);
            lastTime = itemView.findViewById(R.id.messageTime);
            myMessageUsers = itemView.findViewById(R.id.myMessageUsers);

        }
    }
}
