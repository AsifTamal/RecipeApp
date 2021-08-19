package com.brighteststar.asiftamal.recipeapp;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.WindowManager;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecipeAppMainActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    List<foodData> mfoodDataList;
    foodData mfoodData;
    foodRecyclerAdapter mfoodRecyclerAdapter;
    DatabaseReference databaseReference;
    ValueEventListener valueEventListener;
    ProgressDialog progressDialog;
    private static boolean initializedPicasso = false;
    String category;
    EditText search;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_app_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        if (!initializedPicasso) {
            Picasso.Builder builder=new  Picasso.Builder(this);
            builder.downloader(new OkHttpDownloader(this,Integer.MAX_VALUE));
            Picasso built=builder.build();
            built.setIndicatorsEnabled(false);
            built.setLoggingEnabled(true);
            Picasso.setSingletonInstance(built);
            initializedPicasso = true;
        }

        search=(EditText)findViewById(R.id.edtxtsearch);
        mRecyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading items....");
        progressDialog.show();
        GridLayoutManager gridLayoutManager=new GridLayoutManager(RecipeAppMainActivity.this,1);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mfoodDataList=new ArrayList<>();
        mfoodRecyclerAdapter=new foodRecyclerAdapter(RecipeAppMainActivity.this,mfoodDataList);
        mRecyclerView.setAdapter(mfoodRecyclerAdapter);

        databaseReference=FirebaseDatabase.getInstance().getReference("Recipe");
        databaseReference.keepSynced(true);
        Bundle extras = getIntent().getExtras();
        Intent intent = getIntent();
        if (extras != null) {
             category = intent.getExtras().getString("Category").toString();
        }
        valueEventListener=databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mfoodDataList.clear();
                for (DataSnapshot itemSnapshot:dataSnapshot.getChildren()){
                    mfoodData=itemSnapshot.getValue(foodData.class);
                   if(mfoodData.getItemCategory().equals(category)) {
                       mfoodDataList.add(mfoodData);
                   }else if(category.equals("")){
                       mfoodDataList.add(mfoodData);
                   }else if(category.equals("AllFavorite")&&mfoodData.isItemIsfavorite()==true){
                       mfoodDataList.add(mfoodData);
                   }




                }
                mfoodRecyclerAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            filter(s.toString());
            }
        });
    }

    private void filter(String s) {
        ArrayList<foodData> filteredList=new ArrayList<>();
        for(foodData item: mfoodDataList){
            if(item.getItemTitle().toLowerCase().contains(s.toLowerCase())){
                filteredList.add(item);
            }
        }
        mfoodRecyclerAdapter.filterData(filteredList);
    }
}
