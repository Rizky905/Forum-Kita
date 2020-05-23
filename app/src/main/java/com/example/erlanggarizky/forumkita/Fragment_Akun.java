package com.example.erlanggarizky.forumkita;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import static android.app.Activity.RESULT_OK;
import static com.google.firebase.storage.FirebaseStorage.getInstance;

public class Fragment_Akun extends Fragment {

    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase database;
    DatabaseReference databaseReference;

    //storage
    StorageReference storageReference;

    //path image user
    String storagePath = "User_profile";

    //progress dialog
    ProgressDialog pd;

    //permission
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;
    private static final int IMAGE_PICK_GALERY_CODE = 300;
    private static final int IMAGE_PICK_CAMERA_CODE = 400;

    String cameraPermissions[];
    String storagePermissions[];

    //uri picked image
    Uri image_uri;

    //checking profile photo
    String profilePhoto;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_akun, null);

        //Button
        Button Logout = (Button) v.findViewById(R.id.logout);
        Button Teman = (Button) v.findViewById(R.id.teman);

        //INIT FIREBASE
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("USERS");
        storageReference = getInstance().getReference(); //firebase storage reference

        //INIT ARRAYS OF PERMISSION
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions= new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        //INIT VIEWS
        final ImageView imageview = (ImageView) v.findViewById(R.id.fotoprofile);
        final TextView nama = (TextView) v.findViewById(R.id.namaprofile);
        final TextView jurusan_id = (TextView) v.findViewById(R.id.jurusan);
        final TextView universitas_id = (TextView) v.findViewById(R.id.universitas);
        final FloatingActionButton edit = (FloatingActionButton) v.findViewById(R.id.edit_akun);

        //init progress dialog
        pd = new ProgressDialog(getActivity());

        Query query = databaseReference.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //set get data
                for (DataSnapshot ds: dataSnapshot.getChildren()){

                    //get data
                    String name = ""+ ds.child("name").getValue();
                    String jurusan = ""+ ds.child("jurusan").getValue();
                    String universitas = ""+ ds.child("universitas").getValue();
                    String image = ""+ ds.child("image").getValue();

                    //set data
                    nama.setText(name);
                    jurusan_id.setText(jurusan);
                    universitas_id.setText(universitas);

                    try {
                        //image received then set
                        Picasso.get().load(image).into(imageview);

                    }catch (Exception e){

                        Picasso.get().load(R.drawable.add_photo_ic).into(imageview);
                    }


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //edit Button
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditProfileDialog();
            }
        });

        //LOGOUT
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth = FirebaseAuth.getInstance();
                auth.signOut();
                Intent myIntent = new Intent(Fragment_Akun.this.getActivity(), Login.class);
                startActivity(myIntent);

            }
        });

        //Teman
        Teman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Intent = new Intent(Fragment_Akun.this.getActivity(), user_view.class);
                startActivity(Intent);
            }
        });



        return v;

    }


    private boolean checkStoragePermission(){
        boolean result = ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return  result;
    }
    private void  requestStoragePermission(){
        requestPermissions(storagePermissions, STORAGE_REQUEST_CODE);

    }

    private boolean checkCameraPermission(){

        boolean result = ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.CAMERA)
                == (PackageManager.PERMISSION_GRANTED);

        boolean result1 = ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return  result && result1;
    }

    private void  requestCameraPermission(){
        requestPermissions(storagePermissions, STORAGE_REQUEST_CODE);

    }

    private void showEditProfileDialog() {
        String options[] = {"Edit gambar profile", "Edit Nama", "Edit Jurusan", "Edit Universitas"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Pilih");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0){

                    pd.setMessage("update gambar profile");
                    profilePhoto="image";
                    showImagePicDialog();

                }else if(which ==1 ){
                    pd.setMessage("update nama");
                    showNameUpdateDialog("name");
                }
                else if(which ==2 ){
                    pd.setMessage("update jurusan");
                    showNameUpdateDialog("jurusan");
                }
                else if(which ==3 ){
                    pd.setMessage("update universitas");
                    showNameUpdateDialog("universitas");
                }
            }
        });

        builder.create().show();
    }

    private void showNameUpdateDialog(final String key) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Update"+key);
        //set layout
        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(10,10, 10 ,10);

        //edit text
        final EditText editText = new EditText(getActivity());
        editText.setHint("enter"+key);
        linearLayout.addView(editText);

        builder.setView(linearLayout);

        //add button in dialog to update
        builder.setPositiveButton("update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //input text
                String value = editText.getText().toString().trim();
                if(!TextUtils.isEmpty(value)){

                    pd.show();
                    HashMap<String , Object> result = new HashMap<>();
                    result.put(key, value);

                    databaseReference.child(user.getUid()).updateChildren(result)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    pd.dismiss();
                                    Toast.makeText(getActivity(), "Updated" , Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    pd.dismiss();
                                    Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                else{
                    Toast.makeText(getActivity(), "Please enter"+key, Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }


    private void showImagePicDialog() {
        String options[] = {"kamera", "Galeri"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Pilih");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0){

                    //camera click
                    if(!checkCameraPermission()){
                        requestCameraPermission();
                    }
                    else{
                        pickFromCamera();
                    }


                }else if(which ==1 ){
                    if(!checkStoragePermission()){
                        requestStoragePermission();
                    }
                    else {
                        pickFromGalery();
                    }
                }
            }
        });

        builder.create().show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case CAMERA_REQUEST_CODE:{
                if(grantResults.length>0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writestorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && writestorageAccepted){
                        //permission enable
                        pickFromCamera();
                    }
                    else{
                        //request denied
                        Toast.makeText(getActivity(),"Please enable camera & storage permission", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;

            case STORAGE_REQUEST_CODE:{
                if(grantResults.length>0){
                    boolean writeStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (writeStorageAccepted){
                        //permission enable
                        pickFromGalery();
                    }
                    else{
                        //request denied
                        Toast.makeText(getActivity(),"Please enable storage permission", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == RESULT_OK ){
            if(requestCode == IMAGE_PICK_GALERY_CODE){

                image_uri = data.getData();
                uploadProfile(image_uri);

            }
            if(requestCode == IMAGE_PICK_CAMERA_CODE){
                
                uploadProfile(image_uri);

            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void uploadProfile(Uri uri) {

        pd.show();
        String filePathAndName = storagePath +  "" + profilePhoto + ""+ user.getUid();
        StorageReference storageReference2nd = storageReference.child(filePathAndName);
        storageReference2nd.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        //image is uploaded to storage,
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful());
                        Uri downloadUri = uriTask.getResult();

                        if (uriTask.isSuccessful()){

                            //img uploaded
                            //add url in user database
                            HashMap<String, Object> results = new HashMap<>();
                            results.put(profilePhoto, downloadUri.toString());

                            databaseReference.child(user.getUid()).updateChildren(results)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            pd.dismiss();
                                            Toast.makeText(getActivity(),"Image updated", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    pd.dismiss();
                                    Toast.makeText(getActivity(), "error ", Toast.LENGTH_SHORT).show();
                                }
                            });


                        }else {

                            //error
                            pd.dismiss();
                            Toast.makeText(getActivity(), "ERROR", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void pickFromGalery() {

        //GALERY PICK IMAGE
        Intent galeryIntent = new Intent(Intent.ACTION_PICK);
        galeryIntent.setType("image");
        startActivityForResult(galeryIntent, IMAGE_PICK_GALERY_CODE);
    }

    private void pickFromCamera() {
        //PICK IMAGE FROM DEVICE
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"GAMBAR");
        values.put(MediaStore.Images.Media.DESCRIPTION,"DESKRIPSI");

        //PUT IMAGE URI
        image_uri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        //intent start camera
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);

    }
}
