package com.brighteststar.asiftamal.recipeapp;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class foodRecyclerAdapter extends RecyclerView.Adapter<foodRecyclerAdapter.foodViewholder> {
private Context mcontext;
private List<foodData> myFoofList;
private int lastposition=-1;

    public foodRecyclerAdapter(Context mcontext, List<foodData> myFoofList) {
        this.mcontext = mcontext;
        this.myFoofList = myFoofList;
    }

    @NonNull
    @Override
    public foodViewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater=LayoutInflater.from(viewGroup.getContext());
        View mview= inflater.inflate(R.layout.reclycler_row_recipeitem,viewGroup,false);
        return new foodViewholder(mview);
    }

    @Override
    public void onBindViewHolder(@NonNull final foodViewholder foodviewholder, final int i) {
//            foodViewholder.mitemImage.setImageResource(myFoofList.get(i).getItemImage());
        Picasso.with(mcontext).load(myFoofList.get(i).getItemImage()).networkPolicy(NetworkPolicy.OFFLINE).into(foodviewholder.mitemImage, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Picasso.with(mcontext).load(myFoofList.get(i).getItemImage()).into(foodviewholder.mitemImage);
            }
            });
        if(myFoofList.get(i).isItemIsfavorite()==false){
            foodviewholder.mtxtIsfav.setText("false");
            foodviewholder.mtxtIsfav.setVisibility(View.GONE);
        }
        foodviewholder.mtxttxtitemTitle.setText(myFoofList.get(i).getItemTitle());
        foodviewholder.mtxtitemCategory.setText(myFoofList.get(i).getItemCategory());
        foodviewholder.mtxtitemCookingSteps.setText(myFoofList.get(i).getItemCookingSteps());
        foodviewholder.mcardViewforRecycler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mcontext,RecipeDetailsActivity.class);
                intent.putExtra("image",myFoofList.get(foodviewholder.getAdapterPosition()).getItemImage());
                intent.putExtra("title",myFoofList.get(foodviewholder.getAdapterPosition()).getItemTitle());
                intent.putExtra("category",myFoofList.get(foodviewholder.getAdapterPosition()).getItemCategory());
                intent.putExtra("ingredient",myFoofList.get(foodviewholder.getAdapterPosition()).getItemIngredientQuantity());
                intent.putExtra("steps",myFoofList.get(foodviewholder.getAdapterPosition()).getItemCookingSteps());
                intent.putExtra("specialIns",myFoofList.get(foodviewholder.getAdapterPosition()).getItemSpecialInstruction());
                intent.putExtra("favorite",myFoofList.get(foodviewholder.getAdapterPosition()).isItemIsfavorite());
                mcontext.startActivity(intent);
            }
        });
        setAnimation(foodviewholder.itemView,i);

    }

    public void setAnimation(View viewToAnimate, int position) {
        if(position>lastposition){
            ScaleAnimation animation= new ScaleAnimation(0.0f,1.0f,0.0f,1.0f,
                    Animation.RELATIVE_TO_SELF,0.5f,
                    Animation.RELATIVE_TO_SELF,0.5f);
            animation.setDuration(1000);
            viewToAnimate.startAnimation(animation);
            lastposition=position;
        }
    }

    @Override
    public int getItemCount() {
        return myFoofList.size();
    }

    public void filterData(ArrayList<foodData> filteredList) {
        myFoofList=filteredList;
        notifyDataSetChanged();
    }

    class foodViewholder extends RecyclerView.ViewHolder{
        ImageView mitemImage;
        TextView mtxttxtitemTitle,mtxtitemCookingSteps,mtxtitemCategory,mtxtIsfav;
        CardView mcardViewforRecycler;
        public foodViewholder(@NonNull View itemView) {
            super(itemView);
            mitemImage=(ImageView)itemView.findViewById(R.id.itemImage);

             mtxttxtitemTitle=(TextView)itemView.findViewById(R.id.txtitemTitle);
            mtxtIsfav=(TextView)itemView.findViewById(R.id.txtIsFav);

                     mtxtitemCookingSteps=(TextView)itemView.findViewById(R.id.txtitemCookingSteps);

                     mtxtitemCategory=(TextView)itemView.findViewById(R.id.txtitemCategory);
             mcardViewforRecycler=(CardView)itemView.findViewById(R.id.cardViewforRecycler);



        }
    }
}
