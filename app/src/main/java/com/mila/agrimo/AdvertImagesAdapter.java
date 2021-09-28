package com.mila.agrimo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;


import java.util.List;

public class AdvertImagesAdapter extends RecyclerView.Adapter<AdvertImagesAdapter.AdvertImagesViewHolder>{
    private List<String> images;
    private List<String> imageAdvert;
    private Context context;
    private int uploadCount;



    public AdvertImagesAdapter(List<String> images, Context context, int uploadCount) {
        this.images = images;
        this.context = context;
        this.uploadCount = uploadCount;
        this.imageAdvert = imageAdvert;
    }

    @Override
    public AdvertImagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sell_items_row, parent, false);
        return new AdvertImagesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdvertImagesAdapter.AdvertImagesViewHolder holder, int position) {
        String image = images.get(position);
        try {
            Glide.with(context).load(image).
                    into(holder.advertImage);

        } catch (Exception e) {
        }



        holder.removeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //imageAdvert.remove(position);
               images.remove(position);
               uploadCount = images.size();
               //uploadCount = uri.size();

                notifyDataSetChanged();
                //Toast.makeText(context, ""+uploadCount, Toast.LENGTH_SHORT).show();
               // Toast.makeText(context, "image advert"+ imageAdvert.size(), Toast.LENGTH_SHORT).show();



            }
        });




    }

    @Override
    public int getItemCount() {
        if(images.size()>4){

            Toast.makeText(context.getApplicationContext(), "Images should be not more than 4", Toast.LENGTH_SHORT).show();
            images.remove(4);
        }
        return images.size();
    }


    public class AdvertImagesViewHolder extends RecyclerView.ViewHolder{
        ImageView advertImage, removeImg;


        public AdvertImagesViewHolder(@NonNull View itemView) {
            super(itemView);

            advertImage = itemView.findViewById(R.id.advertImage);
            removeImg = itemView.findViewById(R.id.removeImg);
        }


    }


    public interface Onclick {
        void onEvent(String image,int pos);
    }


}
