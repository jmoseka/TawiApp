package com.mila.agrimo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder> {

    private List<ProductModel> productModels;
    private Context context;


    public ProductAdapter(List<ProductModel> productModels, Context context) {
        this.productModels = productModels;
        this.context = context;


    }

    @Override
    public @NotNull ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.products_list, parent, false);
        return new ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductAdapter.ProductHolder holder, final int position) {
       Glide.with(context).load(productModels.get(position).getImage()).
               into(holder.productImage);

        holder.productTitle.setText(productModels.get(position).getProductTitle());
        holder.productLocation.setText(productModels.get(position).getProductLocation());
        holder.productCurrency.setText(productModels.get(position).getProductCurrency());
        holder.productAdditionalAddress.setText(productModels.get(position).getAdditionalInfo());
        
        //String timeAgo = calculateTimeAgo(productModels.get(position).getProductTime());

        //holder.productTime.setText(timeAgo);
         //holder.randomKey.setText(productModels.get(position).getProductRandom());
        // random = productModels.get(position).getProductRandom();
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

    private String calculateTimeAgo(String startTime) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm:ss", Locale.US);

        try {
            long time = sdf.parse(startTime).getTime();
            long now = System.currentTimeMillis();
            CharSequence ago =
                    DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS);
            return ago+"";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return startTime;
    }


    @Override
    public int getItemCount() {
        return productModels.size();
    }

    public  class ProductHolder extends RecyclerView.ViewHolder{
        ImageView productImage;
        TextView productTitle, productLocation, productCurrency, productTime,productAdditionalAddress, randomKey;
        CardView itemCardView;

        public ProductHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            context = itemView.getContext();

            productImage  = itemView.findViewById(R.id.ivProductImage);
            productTitle = itemView.findViewById(R.id.tvProductTitle);
            productLocation = itemView.findViewById(R.id.tvProductLocation);
            productCurrency = itemView.findViewById(R.id.tvProductCurrency);
            productTime= itemView.findViewById(R.id.tvProductTime);
            itemCardView = itemView.findViewById(R.id.itemCardView);
            productAdditionalAddress = itemView.findViewById(R.id.productAdditionalAddress);
           // randomKey = itemView.findViewById(R.id.tvRandomKey);

        }
    }
}
