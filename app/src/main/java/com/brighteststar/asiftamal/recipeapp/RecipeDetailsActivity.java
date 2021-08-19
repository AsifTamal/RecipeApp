package com.brighteststar.asiftamal.recipeapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RecipeDetailsActivity extends AppCompatActivity  {
    private static final int STORAGE_REQUEST_CODE = 100;
    Spinner SspnrCategoryUpdate;
    ImageView mimageForUpdate;
    private Uri uri;
    String storagePermission[];
    ScrollView mscrolbarUpdate;
    EditText mEDtxtRecipeTitleForUpdate,mEDtxtIngregientQntyUpdate,mEDtxtStepsforUpdate,mEDtxtSpInsforUpdate;
    textZoom textzoom;
    private String imageUrl;
    foodData fooddata;
    ProgressDialog progressDialog;
    public static TextView mtxtIngredientDetails,mtxtCookingStepsDetails,mtxtSpecialInstructionDetails;
    private UploadTask uploadTask;
    private DatabaseReference DatabaseReference;
    ValueEventListener valueEventListener;
    FloatingActionButton btnFav;
    String key;
    boolean IsFab=false,IsFavBtnClick=false;
    ScrollView mRootL;
    ImageView mImageDetails;
    Bundle bundle;
    TextView mtxtCategoryDetails,mtxtTitleDetails;
    private foodData mfoodData;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        mImageDetails=(ImageView)findViewById(R.id.imageDetails);
        mtxtTitleDetails=(TextView)findViewById(R.id.txtTitleDetails);
        mtxtCategoryDetails=(TextView)findViewById(R.id.txtCategoryDetails);
        mtxtIngredientDetails=(TextView)findViewById(R.id.txtIngredientDetails);
        mtxtCookingStepsDetails=(TextView)findViewById(R.id.txtCookingStepsDetails);
        mtxtSpecialInstructionDetails=(TextView)findViewById(R.id.txtSpecialInstructionDetails);
        mRootL=(ScrollView)findViewById(R.id.rootDetails);
        btnFav=(FloatingActionButton)findViewById(R.id.btnFavDetails) ;
            loadRecipe();


        mimageForUpdate=(ImageView)findViewById(R.id.imageForUpdate) ;
        mscrolbarUpdate=(ScrollView)findViewById(R.id.scrolbarUpdate);

        mEDtxtRecipeTitleForUpdate=(EditText)findViewById(R.id.txtRecipeTitleForUpdate);
        mEDtxtIngregientQntyUpdate=(EditText)findViewById(R.id.txtIngregientQntyUpdate);
        mEDtxtStepsforUpdate=(EditText)findViewById(R.id.txtStepsforUpdate);
        mEDtxtSpInsforUpdate=(EditText)findViewById(R.id.txtSpInsforUpdate);
        storagePermission=new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        LoaddataforSpinner();
        progressDialog=new ProgressDialog(this);

//        progressDialog.setMessage("Loading items....");
//        progressDialog.show();
//        progressDialog.dismiss();

    }

    public void btnFvrtUpdater(View view) {
        if(IsFavBtnClick==false){
            IsFavBtnClick=true;
        }
        DatabaseReference=FirebaseDatabase.getInstance().getReference("Recipe");
        final Query userQuery = DatabaseReference.orderByChild("itemTitle").equalTo(bundle.getString("title"));

        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {

                    mfoodData=ds.getValue(foodData.class);
                    if(mfoodData.getItemTitle().equals(bundle.getString("title"))) {
                        key = ds.getKey();

                      if( IsFab==false&&IsFavBtnClick==true){

                          FirebaseDatabase.getInstance().getReference("Recipe")
                                  .child(key).child("itemIsfavorite").setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                              @Override
                              public void onComplete(@NonNull Task<Void> task) {
                                  if(task.isSuccessful()) {
                                      Toast.makeText(RecipeDetailsActivity.this, "Successfully added to favorite", Toast.LENGTH_SHORT).show();
                                      progressDialog.dismiss();
                                      mscrolbarUpdate.setVisibility(View.GONE);
                                      IsFavBtnClick=false;
                                      IsFab=true;
                                      Drawable myFabSrc = getResources().getDrawable(R.drawable.ic_fav_true);

//set it to your fab button initialized before
                                      btnFav.setImageDrawable(myFabSrc);
                                  }
                              }
                          }).addOnFailureListener(new OnFailureListener() {
                              @Override
                              public void onFailure(@NonNull Exception e) {
                                  Toast.makeText(RecipeDetailsActivity.this,"Added to favorite failed",Toast.LENGTH_SHORT).show();
                                  progressDialog.dismiss();
                                  mscrolbarUpdate.setVisibility(View.GONE);
                              }
                          });
                      }else if(IsFab==true&&IsFavBtnClick==true) {
                          FirebaseDatabase.getInstance().getReference("Recipe")
                                  .child(key).child("itemIsfavorite").setValue(false).addOnCompleteListener(new OnCompleteListener<Void>() {
                              @Override
                              public void onComplete(@NonNull Task<Void> task) {
                                  if(task.isSuccessful()) {
                                      Toast.makeText(RecipeDetailsActivity.this, "Removed from favorite", Toast.LENGTH_SHORT).show();
                                      progressDialog.dismiss();
                                      mscrolbarUpdate.setVisibility(View.GONE);
                                      IsFavBtnClick=false;
                                      IsFab=false;
                                      Drawable myFabSrc = getResources().getDrawable(R.drawable.ic_fav_false);
//copy it in a new one

//set it to your fab button initialized before
                                      btnFav.setImageDrawable(myFabSrc);
                                  }
                              }
                          }).addOnFailureListener(new OnFailureListener() {
                              @Override
                              public void onFailure(@NonNull Exception e) {
                                  Toast.makeText(RecipeDetailsActivity.this,"Failed to remove",Toast.LENGTH_SHORT).show();
                                  progressDialog.dismiss();
                                  mscrolbarUpdate.setVisibility(View.GONE);
                              }
                          });
                      }

                        loadRecipe();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Log.d(TAG, databaseError.getMessage());
            }
        });


    }

    private void LoaddataforSpinner() {
        SspnrCategoryUpdate=(Spinner)findViewById(R.id.spnrCategoryUpdate);
        List<String> list = new ArrayList<String>();
        String[] allCategory=getResources().getStringArray(R.array.Languages);
        list.add("Please Select Food Category");
        for(int i=0;i<=allCategory.length-1;i++){
            list.add(allCategory[i]);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SspnrCategoryUpdate.setAdapter(dataAdapter);
        SspnrCategoryUpdate.setSelection(dataAdapter.getPosition(bundle.getString("category")));
        SspnrCategoryUpdate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void loadRecipe() {
        bundle=getIntent().getExtras();
        if(bundle!=null){
            mtxtTitleDetails.setText(bundle.getString("title"));
            mtxtCategoryDetails.setText("Category: "+bundle.getString("category"));
            mtxtIngredientDetails.setText(bundle.getString("ingredient"));
            mtxtCookingStepsDetails.setText(bundle.getString("steps"));
            mtxtSpecialInstructionDetails.setText(bundle.getString("specialIns"));
            if(bundle.getBoolean("favorite")==true ){

                Drawable myFabSrc = getResources().getDrawable(R.drawable.ic_fav_true);

//set it to your fab button initialized before
                btnFav.setImageDrawable(myFabSrc);
                IsFab=true;
            }
            else {
                IsFab=false;
            }

            Picasso.with(this).load(bundle.getString("image")).networkPolicy(NetworkPolicy.OFFLINE).into(mImageDetails, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(RecipeDetailsActivity.this).load(bundle.getString("image")).into(mImageDetails);
                }
            });
        }

    }

    public void btnCancelRecipe(View view) {
        mscrolbarUpdate.setVisibility(View.GONE);
        clearData();
    }

    public void btnUpdateRecipe(View view) {
        DatabaseReference=FirebaseDatabase.getInstance().getReference("Recipe");
        final Query userQuery = DatabaseReference.orderByChild("itemTitle").equalTo(bundle.getString("title"));
        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {

                    mfoodData=ds.getValue(foodData.class);
                    if(mfoodData.getItemTitle().equals(bundle.getString("title"))) {
                        key = ds.getKey();
                        UploadImage();

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
               // Log.d(TAG, databaseError.getMessage());
            }
        });
        //userQuery.addListenerForSingleValueEvent(valueEventListener);
        /*DatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/


        /*valueEventListener=DatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot itemSnapshot:dataSnapshot.getChildren()){
                    mfoodData=itemSnapshot.getValue(foodData.class);
                    if(mfoodData.getItemTitle().equals(bundle.getString("title"))) {
                        UploadImage();
                    }
                   *//* else if(category.equals("")){

                    }*//*




                }

                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialog.dismiss();
            }
        });*/
    }
    public  void UploadImage(){
        if(uri != null) {
            storageReference = FirebaseStorage.getInstance()
                    .getReference().child("RecipeImages").child(uri.getLastPathSegment());
            progressDialog.setMessage("Data Loading");
            progressDialog.show();
            uploadTask = storageReference.putFile(uri);

            Task<Uri> urlTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    Task<Uri> uriTask = storageReference.getDownloadUrl();

                    return uriTask;
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri urlImage = task.getResult();
                        imageUrl = urlImage.toString();
                        uploadrecipe();
                        Toast.makeText(RecipeDetailsActivity.this, "Image Upload Successful ", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }




                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(RecipeDetailsActivity.this, "No imge uploaded because : " + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });

        }
    }

    private void uploadrecipe() {


        fooddata=new foodData(mEDtxtRecipeTitleForUpdate.getText().toString(),
                SspnrCategoryUpdate.getSelectedItem().toString(),mEDtxtIngregientQntyUpdate.getText().toString(),
                mEDtxtStepsforUpdate.getText().toString(),
                mEDtxtSpInsforUpdate.getText().toString(),imageUrl,IsFab);

        FirebaseDatabase.getInstance().getReference("Recipe")
                .child(key).setValue(fooddata).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(RecipeDetailsActivity.this, "Upload Recipe Successful", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    mscrolbarUpdate.setVisibility(View.GONE);
                    clearData();
                    Picasso.with(RecipeDetailsActivity.this).load(imageUrl).into(mImageDetails);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RecipeDetailsActivity.this,"Upload failed",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                mscrolbarUpdate.setVisibility(View.GONE);
            }
        });

    }

    public void clearData(){
        mimageForUpdate.setImageURI(null);

        mEDtxtRecipeTitleForUpdate.setText("");
        mEDtxtIngregientQntyUpdate.setText("");;
        mEDtxtStepsforUpdate.setText("");;
        mEDtxtSpInsforUpdate.setText("");
    }

    public void btnselectImageUpdate(View view) {
        if(!checkStoregePermission()){
            requestGalleryPermission();
        }
        else {
            pickGallery();
        }
    }

    private void requestGalleryPermission() {
        ActivityCompat.requestPermissions(this,storagePermission,STORAGE_REQUEST_CODE);
    }

    private boolean checkStoregePermission() {

        boolean result=ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);
        return result;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){

            case STORAGE_REQUEST_CODE:
                if(grantResults.length>0){
                    //  boolean cameraAccepted=grantResults[0]== PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted=grantResults[0]== PackageManager.PERMISSION_GRANTED;
                    if(writeStorageAccepted){
                        pickGallery();
                    }
                    else {
                        Toast.makeText(this,"Permission Denied",Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    private void pickGallery() {
        Intent photoPicker= new Intent(Intent.ACTION_PICK);
        photoPicker.setType("image/*");
        startActivityForResult(photoPicker,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            uri = data.getData();
            mimageForUpdate.setImageURI(uri);
        }else Toast.makeText(RecipeDetailsActivity.this,"No image",Toast.LENGTH_SHORT).show();
    }




    public void btnOpenUpdater(View view) {
        mscrolbarUpdate.setVisibility(View.VISIBLE);
        mEDtxtRecipeTitleForUpdate.setEnabled(false);
        mEDtxtRecipeTitleForUpdate.setText(bundle.getString("title"));
        //mtxtCategoryDetails.setText(bundle.getString("category"));
        mEDtxtIngregientQntyUpdate.setText(bundle.getString("ingredient"));
        mEDtxtStepsforUpdate.setText(bundle.getString("steps"));
        mEDtxtSpInsforUpdate.setText(bundle.getString("specialIns"));

        Picasso.with(this).load(bundle.getString("image")).networkPolicy(NetworkPolicy.OFFLINE).into(mimageForUpdate, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Picasso.with(RecipeDetailsActivity.this).load(bundle.getString("image")).into(mimageForUpdate);
            }
        });
        
    }



}
