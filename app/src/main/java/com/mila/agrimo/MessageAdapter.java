package com.mila.agrimo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter {

    //RecyclerView.Adapter
    Context context;
    ArrayList<MessageModel> messageList;
    int ITEM_SEND = 1;
    int ITEM_RECEIVER = 2;

    public MessageAdapter(Context context, ArrayList<MessageModel> messageList) {
        this.context = context;
        this.messageList = messageList;
    }

    @NonNull
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_SEND) {
            View view = LayoutInflater.from(context).inflate(R.layout.sender_layout_item, parent, false);
            return new SenderViewHolder(view);

        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.receiver_layout_item, parent, false);
            return new ReceiverViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull RecyclerView.ViewHolder holder, int position) {

        MessageModel messages = messageList.get(position);

        if (holder.getClass() == SenderViewHolder.class) {
            SenderViewHolder viewHolder = (SenderViewHolder) holder;
            viewHolder.txtMessage.setText(messages.getMessage());
            viewHolder.senderTime.setText(messages.getTime());

            //

           /* if (position == messageList.size() - 1) {
                if (messageList.get(position).isSeen) {
                    viewHolder.isSeen.setText("seen");
                } else {
                    viewHolder.isSeen.setText("Delivered");
                }
            }*/

        } else {
            ReceiverViewHolder viewHolder = (ReceiverViewHolder) holder;
            viewHolder.txtMessage.setText(messages.getMessage());
            viewHolder.receiverTime.setText(messages.time);
            //viewHolder.isSeen.setText(messages.getReceiverSeen());

           /* if (position == messageList.size() - 1) {
                if (messageList.get(position).isSeen) {
                    viewHolder.isSeen.setText("seen");
                } else {
                    viewHolder.isSeen.setText("Delivered");
                }
            }*/
        }



        //set seen/delivered




    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }


    @Override
    public int getItemViewType(int position) {
        MessageModel messages = messageList.get(position);
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messages.getSenderId())){
            return ITEM_SEND;
        }
        else {
            return ITEM_RECEIVER;
        }

    }

    class SenderViewHolder extends RecyclerView.ViewHolder{
        CircleImageView circleImageView;
        TextView txtMessage, senderTime, isSeen;

        public SenderViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            txtMessage = itemView.findViewById(R.id.senderMessage);
            senderTime = itemView.findViewById(R.id.senderTime);
            isSeen = itemView.findViewById(R.id.senderSeen);


        }
    }

    class ReceiverViewHolder extends RecyclerView.ViewHolder{
        CircleImageView circleImageView;
        TextView txtMessage, receiverTime, isSeen;

        public ReceiverViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
           
            txtMessage = itemView.findViewById(R.id.receiverMessage);
            receiverTime = itemView.findViewById(R.id.receiverTime);
            isSeen = itemView.findViewById(R.id.receiverSeen);


        }
    }


}
