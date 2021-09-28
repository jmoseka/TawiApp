package com.mila.agrimo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PendingAdapter extends RecyclerView.Adapter<PendingAdapter.MyListingHolder> {
    private Context context;
    private List<MyListingModel> productModels;
    String randomKey, id;

    public PendingAdapter(Context context, List<MyListingModel> productModels) {
        this.context = context;
        this.productModels = productModels;
    }

    @NonNull
    @NotNull
    @Override
    public MyListingHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pending_layout, parent, false);
        return new PendingAdapter.MyListingHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PendingAdapter.MyListingHolder holder, int position) {
        Glide.with(context).load(productModels.get(position).getImage()).
                into(holder.productImage);

        holder.productTitle.setText(productModels.get(position).getProductTitle());
        holder.productLocation.setText(productModels.get(position).getProductLocation());
        holder.productCurrency.setText(productModels.get(position).getProductCurrency());
        randomKey = productModels.get(position).getProductRandom();
        id= productModels.get(position).getUserId();
        holder.productTime.setText(productModels.get(position).getProductTime());

        holder.productDelt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProduct();
            }
        });




    }

    private void deleteProduct() {


        //DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("All Listings");
        DatabaseReference myRef= FirebaseDatabase.getInstance().getReference().child("My Pending Listings Kenya");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();



        assert user != null;
        Query q = myRef.child(user.getUid()).orderByChild("productRandom").equalTo(randomKey);

        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()) {

                    ds.getRef().removeValue();

                }

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });



    }

    @Override
    public int getItemCount() {
        return productModels.size();
    }


    public  class MyListingHolder extends RecyclerView.ViewHolder{
        ImageView productImage, productDelt;
        TextView productTitle, productLocation, productCurrency, productTime, randomKey;
        CardView itemCardView;

        public MyListingHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            context = itemView.getContext();

            productImage  = itemView.findViewById(R.id.listingProductImage);
            productTitle = itemView.findViewById(R.id.listingProductTitle);
            productLocation = itemView.findViewById(R.id.listingProductLocation);
            productCurrency = itemView.findViewById(R.id.listingProductCurrency);
            productTime= itemView.findViewById(R.id.listingProductTime);
            itemCardView = itemView.findViewById(R.id.listingCardView);
            productDelt = itemView.findViewById(R.id.productDelete);
            // randomKey = itemView.findViewById(R.id.listingRandomKey);

        }
    }
}
