package com.mila.agrimo;

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

public class MyListingAdapter extends RecyclerView.Adapter<MyListingAdapter.MyListingHolder> {
    private Context context;
    private List<ProductModel> productModels;
    String randomKey, id, country;
    FirebaseUser user;
    DatabaseReference ref;

    public MyListingAdapter(Context context, List<ProductModel> productModels) {
        this.context = context;
        this.productModels = productModels;
    }

    @NonNull
    @NotNull
    @Override
    public MyListingHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_listing_layout, parent, false);
        return new MyListingAdapter.MyListingHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyListingAdapter.MyListingHolder holder, int position) {
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
                String r = productModels.get(position).getProductRandom();
                String section = productModels.get(position).getSection();
                productModels.remove(position);
                notifyDataSetChanged();


                user = FirebaseAuth.getInstance().getCurrentUser();
                DatabaseReference userRef =  FirebaseDatabase.getInstance().getReference("UserInfo").
                        child(user.getUid());

                userRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        country = "" + snapshot.child("country").getValue();
                        removeItem(r,section);

                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });




            }
        });
        

        holder.productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ProductInfoActivity.class);
                // intent.putExtra("image", productModels.get(position).getImage());
                intent.putExtra("randomKey", productModels.get(position).getProductRandom());
                intent.putExtra("section", productModels.get(position).getSection());
                intent.putExtra("userId", productModels.get(position).getUserId());
                context.startActivity(intent);

            }
        });

    }


    private void removeItem(String r, String section) {

        DatabaseReference myRef= FirebaseDatabase.getInstance().getReference().child("My Listings");
        ref = FirebaseDatabase.getInstance().getReference()
                .child("All Listings").child(country).child(section);

        Query query = ref.orderByChild("productRandom").equalTo(r);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    ds.getRef().removeValue();
                }
            }

            @Override
            public void onCancelled(@NotNull DatabaseError databaseError) {
                // Log.e(TAG, "onCancelled", databaseError.toException());
            }
        });

        Query q = myRef.child(user.getUid()).orderByChild("productRandom").equalTo(r);

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
