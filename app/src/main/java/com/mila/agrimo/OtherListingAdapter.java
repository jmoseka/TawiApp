package com.mila.agrimo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class OtherListingAdapter extends RecyclerView.Adapter<OtherListingAdapter.OtherListingHolder>  {

    private Context context;
    private List<ProductModel> productModels;


    public OtherListingAdapter(Context context, List<ProductModel> productModels) {
        this.context = context;
        this.productModels = productModels;
    }

    @NonNull
    @NotNull
    @Override
    public OtherListingHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.other_listing_layout, parent, false);
        return new OtherListingAdapter.OtherListingHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull OtherListingAdapter.OtherListingHolder holder, int position) {

        Glide.with(context).load(productModels.get(position).getImage()).
                into(holder.productImage);

        holder.productTitle.setText(productModels.get(position).getProductTitle());
        holder.productLocation.setText(productModels.get(position).getProductLocation());
        holder.productCurrency.setText(productModels.get(position).getProductCurrency());
        //holder.randomKey.setText(productModels.get(position).getProductRandom());
        holder.productTime.setText(productModels.get(position).getProductTime());

        holder.itemCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ProductInfoActivity.class);
                // intent.putExtra("image", productModels.get(position).getImage());
                intent.putExtra("randomKey", productModels.get(position).getProductRandom());
                intent.putExtra("section", productModels.get(position).getSection());
                intent.putExtra("userId", productModels.get(position).getUserId());
                context.startActivity(intent);
                ((Activity)context).finish();

            }
        });

    }

    @Override
    public int getItemCount() {
        return productModels.size();
    }


    public  class OtherListingHolder extends RecyclerView.ViewHolder{
        ImageView productImage;
        TextView productTitle, productLocation, productCurrency, productTime, randomKey;
        CardView itemCardView;

        public OtherListingHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            context = itemView.getContext();

            productImage  = itemView.findViewById(R.id.otherListingProductImage);
            productTitle = itemView.findViewById(R.id.otherListingProductTitle);
            productLocation = itemView.findViewById(R.id.otherListingProductLocation);
            productCurrency = itemView.findViewById(R.id.otherListingProductCurrency);
            productTime= itemView.findViewById(R.id.otherListingProductTime);
            itemCardView = itemView.findViewById(R.id.otherListingCardView);
            // randomKey = itemView.findViewById(R.id.listingRandomKey);

        }
    }

}
