package com.brighteststar.asiftamal.recipeapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class StartActivity extends AppCompatActivity {
    private static final int STORAGE_REQUEST_CODE = 100;
    Spinner SspnrforCategory;
    ImageView mimageViewUpload;
    private Uri uri;
    String storagePermission[];
    ScrollView mscrollView,mScrollviewforGrid;
    EditText mEDtxtRecipeTitleForUpload,mEDtxtIngregientQnty,mEDtxtStepsforUpload,mEDtxtSpInsforUpload;
    Button mbtnSubmit;
    private String imageUrl;
    foodData fooddata;
     ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private UploadTask uploadTask;
    private StorageReference storageReference;
    GridLayout gridLayout;
    Intent intent;
    private boolean initializedPicasso= false;
    DatabaseReference databaseReference;
    boolean Favorite=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_start);
        progressDialog=new ProgressDialog(this);
        mimageViewUpload=(ImageView)findViewById(R.id.imageForUpload) ;
        mscrollView=(ScrollView)findViewById(R.id.scrolbar);
        mScrollviewforGrid=(ScrollView)findViewById(R.id.ScrollviewforGrid);
        mEDtxtRecipeTitleForUpload=(EditText)findViewById(R.id.txtRecipeTitleForUpload);
        mEDtxtIngregientQnty=(EditText)findViewById(R.id.txtIngregientQnty);
        mEDtxtStepsforUpload=(EditText)findViewById(R.id.txtStepsforUpload);
        mEDtxtSpInsforUpload=(EditText)findViewById(R.id.txtSpInsforUpload);
        storagePermission=new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        LoaddataforSpinner();


        gridLayout=(GridLayout)findViewById(R.id.mainGrid);

        setSingleEvent(gridLayout);

        try{
            progressDialog.setMessage("Data Loading");
            progressDialog.show();
            databaseReference=FirebaseDatabase.getInstance().getReference("Recipe");
            databaseReference.keepSynced(true);
            progressDialog.dismiss();
        }catch (Exception e){
            Toast.makeText(StartActivity.this,"Problem loading new data. Check your internet connection",Toast.LENGTH_SHORT).show();
        }
    }
    public void clearData(){
        mimageViewUpload.setImageURI(null);
        mEDtxtRecipeTitleForUpload.setText("");
        mEDtxtSpInsforUpload.setText("");;
        mEDtxtStepsforUpload.setText("");;
        mEDtxtIngregientQnty.setText("");
    }
    private void setSingleEvent(GridLayout gridLayout) {
        for(int i = 0; i<gridLayout.getChildCount();i++){
            CardView cardView=(CardView)gridLayout.getChildAt(i);
            final int finalI= i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(StartActivity.this,"Clicked at index "+ finalI,
                            Toast.LENGTH_SHORT).show();
                    intent = new Intent(StartActivity.this,RecipeAppMainActivity.class);
                    switch (finalI){
                        case 0:
                        intent.putExtra("Category", "Biriyani Cuisine");
                        break;
                        case 1:
                            intent.putExtra("Category", "Beef Curry");
                            break;
                        case 2:
                            intent.putExtra("Category", "Chicken Curry");
                            break;
                        case 3:
                            intent.putExtra("Category", "Vegetables Curry");
                            break;
                        case 4:
                            intent.putExtra("Category", "Fish Curry");
                            break;
                        case 5:
                            intent.putExtra("Category", "Mutton Curry");
                            break;
                        case 6:
                            intent.putExtra("Category", "Chinese");
                            break;
                        case 7:
                            intent.putExtra("Category", "Khichuri");
                            break;
                        case 8:
                            intent.putExtra("Category", "Pickles");
                            break;
                        case 10:
                            intent.putExtra("Category", "AllFavorite");
                            break;
                        case 11:
                            LoadPrivacy();
                            break;
                        default:
                            intent.putExtra("Category", "");
                            break;
                    }
                    if(intent.getStringExtra("Category")!=null){

                        startActivity(intent);
                    }



                }
            });
        }



}
public void LoadPrivacy(){
    final AlertDialog.Builder alert = new AlertDialog.Builder(StartActivity.this,R.style.AlertDialogTheme);
    alert.setTitle("Privacy Policy");
    alert.setMessage(getString(R.string.privacy));

    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int whichButton) {

        }
    });

    alert.create().show();


}


    public void btnUploadecipe(View view) {
        UploadImage();

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
                        Toast.makeText(StartActivity.this, "Image Upload Successful ", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }




                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(StartActivity.this, "No imge uploaded because : " + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
           /* uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isComplete()) {
                                Uri urlImage = uriTask.getResult();
                                imageUrl = urlImage.toString();
                                uploadrecipe();
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(StartActivity.this, "No imge uploaded because : " + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });*/
        }
    }


    public void uploadrecipe(){


        fooddata=new foodData(mEDtxtRecipeTitleForUpload.getText().toString(),
                SspnrforCategory.getSelectedItem().toString(),mEDtxtIngregientQnty.getText().toString(),
                mEDtxtStepsforUpload.getText().toString(),
                mEDtxtSpInsforUpload.getText().toString(),imageUrl,Favorite);
        String myCurrentDatetime= DateFormat.getDateTimeInstance()
                .format(Calendar.getInstance().getTime());
        FirebaseDatabase.getInstance().getReference("Recipe")
                .child(myCurrentDatetime).setValue(fooddata).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(StartActivity.this, "Upload Recipe Successful", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    mscrollView.setVisibility(View.GONE);
                    mScrollviewforGrid.setVisibility(View.VISIBLE);
                    clearData();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(StartActivity.this,"Upload failed",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                mscrollView.setVisibility(View.GONE);
            }
        });

    }
    private void LoaddataforSpinner() {
        SspnrforCategory=(Spinner)findViewById(R.id.spnrCategory);
        List<String> list = new ArrayList<String>();
        String[] allDistricts=getResources().getStringArray(R.array.Languages);
        list.add("Please Select Food Category");
        for(int i=0;i<=allDistricts.length-1;i++){
            list.add(allDistricts[i]);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SspnrforCategory.setAdapter(dataAdapter);
        SspnrforCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    public void btnselectImage(View view) {
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
            mimageViewUpload.setImageURI(uri);
        }else Toast.makeText(StartActivity.this,"No image",Toast.LENGTH_SHORT).show();
    }


    public void btnOpenUploder(View view) {
        mscrollView.setVisibility(View.VISIBLE);
        mScrollviewforGrid.setVisibility(View.GONE);
    }


    public void btnCancelcipe(View view) {
        mscrollView.setVisibility(View.GONE);
        mScrollviewforGrid.setVisibility(View.VISIBLE);
    }
}


/*
<ScrollView
        android:id="@+id/scrolbar"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:visibility="gone">
<LinearLayout
            android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:orientation="vertical"
                    android:padding="20sp">
<ImageView
                android:id="@+id/imageForUpload"
                        android:layout_width="match_parent"
                        android:layout_height="200dp"
                        android:scaleType="centerCrop"
                        android:src="@drawable/noimage"/>
<EditText
                android:id="@+id/txtRecipeTitleForUpload"
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:hint="Title"/>
<EditText
                android:id="@+id/txtIngregientQnty"
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:hint="Ingredient and Quantity"
                        android:inputType="text|textMultiLine"/>
<EditText
                android:id="@+id/txtStepsforUpload"
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:hint="Cooking Process"
                        android:inputType="text|textMultiLine"/>
<EditText
                android:id="@+id/txtSpInsforUpload"
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:hint="Special Instruction (Optional)"
                        android:inputType="text|textMultiLine"/>
<TextView

                android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:layout_marginTop="10dp"
                        android:padding="20sp"
                        android:text="Select Category "
                        android:textSize="15sp"/>
<Spinner
                android:id="@+id/spnrCategory"
                        android:layout_width="match_parent"
                        android:layout_height="match_parent"
                        style="@style/Widget.AppCompat.Light.Spinner.DropDown.ActionBar"
                        android:layout_marginBottom="15dp"></Spinner>
<Button
                android:backgroundTint="@color/colorPrimary"
                        android:textColor="@android:color/white"
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:text="Select Image"
                        android:textSize="22sp"
                        android:onClick="btnselectImage"/>
<Button
                android:id="@+id/btnSubmit"
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:text="Upload Recipe"
                        android:textSize="22sp"
                        android:onClick="btnUploadecipe"
                        android:backgroundTint="@color/colorPrimary"
                        android:textColor="@android:color/white"/>
<Button
                android:id="@+id/btnCancel"
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:text="Cancel"
                        android:textSize="22sp"
                        android:onClick="btnCancelcipe"
                        android:backgroundTint="@color/colorPrimary"
                        android:textColor="@android:color/white"/>

</LinearLayout>
</ScrollView>*/
