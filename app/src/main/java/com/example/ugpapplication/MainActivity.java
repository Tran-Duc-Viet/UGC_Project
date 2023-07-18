package com.example.ugpapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ugpapplication.Database.AppDB;
import com.example.ugpapplication.Database.UGCDao;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final int PERMISSION_REQUEST_CODE = 100;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 123;
    private static final float DEFAULT_ZOOM = 30f;


    private FusedLocationProviderClient mFusedLocationProviderClient;

    SQLiteDatabase db;
    private boolean mLocationPermissonGranted = false;
    private GoogleMap myMap;
    String userChoosenTask;
    Button btnOpenUploadingDialog, btnCancel, btnUploading;
    ImageView uploadImage1, uploadImage2, uploadImage3;

    ImageInfo image1,image2,image3,imageInfo;

    EditText memo;

    double latitude,longitude;
    UGCModel ugcModel;

    Uri imageUri;

    String msg;

    private UgcViewModel ugcViewModel;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maps);
        mapFragment.getMapAsync(this);
        getLocationPermission();

        //Open upload content dialog
        btnOpenUploadingDialog = findViewById(R.id.buttonUploadingUGC);
        btnOpenUploadingDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenUploadScreen();

            }
        });
        ugcViewModel= new ViewModelProvider(this).get(UgcViewModel.class);
        ugcViewModel.getAllUGCs().observe(this, new Observer<List<UGCModel>>() {
            @Override
            public void onChanged(List<UGCModel> ugcModels) {

            }
        });



    }

    private void createData(){
        db.beginTransaction();
        try {
            db.execSQL("create table tblUGC(" +
                    "recID integer PRIMARY KEY autoincrement," +
                    "date text," +"lattitude real, "+"longtitude real, "+"image1 blob,"+"image2 blob,"+"image3 blob,"+"category text, "+
                    "memo text)");
            db.execSQL("insert into tblAMIGO(date, latitude, longtitude, image1, image2, image3, category, memo) values('2023/12/05 15:05','21.000344586052734','105.84257429703847'," +image1+", "+image2+", "+image3+", 'png', 'hi')");
            db.execSQL("insert into tblAMIGO(date, latitude, longtitude, image1, image2, image3, category, memo) values('2023/12/05 15:03','21.0','105.0'," +image1+", "+image2+", "+image3+", 'png', 'hi')");
            db.execSQL("insert into tblAMIGO(date, latitude, longtitude, image1, image2, image3, category, memo) values('2023/12/05 15:02','21.0','105.0'," +image1+", "+image2+", "+image3+", 'png', 'hi')");
            db.setTransactionSuccessful();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    private void getDeviceLocation() {
        mFusedLocationProviderClient=LocationServices.getFusedLocationProviderClient(this);
        try {
            Task location = mFusedLocationProviderClient.getLastLocation();
            location.addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        Log.d("Viet", "onComplete: found loaction");
                        Location currentLocation = (Location) task.getResult();
                        latitude=currentLocation.getLatitude();
                        longitude=currentLocation.getLongitude();

                        moveCamera(new LatLng(latitude, longitude), DEFAULT_ZOOM);


                    } else {
                        Log.d("Viet", "onComplete: current location is null");
                    }
                }
            });

        } catch (SecurityException e) {
            Log.e("Location", "getDeviceLocation: Security Execption: " + e.getMessage());
        }
    }

    private void moveCamera(LatLng latLng, float zoom) {
        Log.d("Viet","lat:"+latLng.latitude+",lng"+latLng.longitude);
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    private void putMarkerOnMap(LatLng latLng){
        MarkerOptions markerOptions=new MarkerOptions().position(latLng);
        myMap.addMarker(markerOptions).setTag(latLng.latitude+" "+latLng.longitude);

    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        myMap = googleMap;
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED&&checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED) {
            getDeviceLocation();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            myMap.setMyLocationEnabled(true);
            myMap.getUiSettings().setMyLocationButtonEnabled(false);

        }
    }

    public void OpenMenu(View view) {
        Intent intent=new Intent(this, Menu.class);
        startActivity(intent);
    }


    private void getLocationPermission(){
        String []permissions={Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED&&ContextCompat.checkSelfPermission(this.getApplicationContext(),Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this,"Permission Granted. You can use the API which requires permisson",Toast.LENGTH_LONG);
        }else if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)&&ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_COARSE_LOCATION)) {
            AlertDialog.Builder askBuilder=new AlertDialog.Builder(MainActivity.this);
            askBuilder.setMessage("このアプリは位置情報を利用します").setTitle("\"UGC Application\"に位置情報の利用を許可しますか？").setCancelable(false).setPositiveButton("許可しない",((dialog, which) -> {
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},LOCATION_PERMISSION_REQUEST_CODE);
                dialog.dismiss();
            })).setNegativeButton("許可する",((dialog, which) ->dialog.dismiss() ));

        }else {
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    //Open Upload Screen
    private void OpenUploadScreen(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View uploadContentView=getLayoutInflater().inflate(R.layout.upload_content,null);
        Spinner category=uploadContentView.findViewById(R.id.category);
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item,getResources().getStringArray(R.array.categoryList));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(arrayAdapter);
        uploadImage1=uploadContentView.findViewById(R.id.uploadImage1);
        uploadImage2=uploadContentView.findViewById(R.id.uploadImage2);
        uploadImage3=uploadContentView.findViewById(R.id.uploadImage3);
        image1=new ImageInfo(uploadImage1);
        image2=new ImageInfo(uploadImage2);
        image3=new ImageInfo(uploadImage3);
        memo=uploadContentView.findViewById(R.id.memo);
        memo.clearFocus();
        memo.requestFocus(EditText.FOCUS_DOWN);
        btnUploading=uploadContentView.findViewById(R.id.btnUploading);
        btnCancel = uploadContentView.findViewById(R.id.btnBackToMapscreen);
        builder.setView(uploadContentView);
        AlertDialog dialog=builder.create();
        uploadImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageInfo=image1;
                selectImage();
            }
        });
        uploadImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageInfo=image2;
                selectImage();
            }
        });
        uploadImage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageInfo=image3;
                selectImage();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnUploading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                Date date = new Date();
                ugcModel=new UGCModel( latitude,longitude,dateFormat.format(date), category.getSelectedItem().toString(), memo.getText().toString(), image1.getImageURI().toString(),image2.getImageURI().toString(), image3.getImageURI().toString());
                ugcViewModel.insert(ugcModel);
                Toast.makeText(getApplicationContext(),"success",Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });
        dialog.show();
        CheckRuntimePermission();
    }

    private void CheckRuntimePermission(){
        String []permissions={Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if(ActivityCompat.checkSelfPermission(MainActivity.this,permissions[0])==PackageManager.PERMISSION_GRANTED&&ActivityCompat.checkSelfPermission(MainActivity.this,permissions[1])==PackageManager.PERMISSION_GRANTED&&ActivityCompat.checkSelfPermission(MainActivity.this,permissions[2])==PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this,"Permission Granted. You can use the API which requires permisson",Toast.LENGTH_LONG);
        }else if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CAMERA)&&ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)&&ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
            AlertDialog.Builder askBuilder=new AlertDialog.Builder(MainActivity.this);
            askBuilder.setMessage("\"UGC Application\"にカメラと写真ライブラリの使用を許可しますか？").setTitle("Permission Required").setCancelable(false).setPositiveButton("OK",((dialog, which) -> {
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA , Manifest.permission.READ_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);
                dialog.dismiss();
            })).setNegativeButton("Cancel",((dialog, which) ->dialog.dismiss() ));
        }else {
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA , Manifest.permission.READ_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);

        }
    }

    private void selectImage() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {

                        userChoosenTask="Take Photo";
                        cameraIntent();
                }
                else if (options[item].equals("Choose from Gallery"))
                {
                        userChoosenTask="Choose from Gallery";
                        galleryIntent();
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                imageInfo.setImageURI(imageUri.toString());
                imageInfo.getImageView().setImageURI(imageUri);
            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                imageInfo.setImageURI(selectedImage.toString());
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                Log.w("path of image from gallery......******************.........", picturePath+"");
                imageInfo.getImageView().setImageBitmap(thumbnail);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mLocationPermissonGranted=false;

        switch (requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED&&grantResults[1]==PackageManager.PERMISSION_GRANTED){
//                    mLocationPermissonGranted=true;
                    Toast.makeText(this,"Permission Granted. You can use the API which requires permisson",Toast.LENGTH_LONG);
                }else if(!ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)&&!ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_COARSE_LOCATION)){
                    AlertDialog.Builder newBuilder=new AlertDialog.Builder(MainActivity.this);
                    newBuilder.setMessage("This feature is unavailable because this feature require features you denied.Please allow Camera permission from settings to proceed further").setTitle("Permission required").setCancelable(false).setNegativeButton("Cancel",((dialog, which) -> dialog.dismiss())).setPositiveButton("Settings", new DialogInterface.OnClickListener(){

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent= new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri=Uri.fromParts("pakage",getPackageName(),null);
                                    intent.setData(uri);
                                    startActivity(intent);

                                    dialog.dismiss();
                                }
                            }
                    );
                    newBuilder.show();
                }

            }
            case PERMISSION_REQUEST_CODE:{
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED&&grantResults[1]==PackageManager.PERMISSION_GRANTED&&grantResults[2]==PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this,"Permission Granted. You can use the API which requires permisson",Toast.LENGTH_LONG);

                }else if(!ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CAMERA)&&!ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)&&!ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                    AlertDialog.Builder goToBuilder=new AlertDialog.Builder(MainActivity.this);
                    goToBuilder.setMessage("This feature is unavailable because this feature require features you denied.Please allow Camera permission from settings to proceed further").setTitle("Permission required").setCancelable(false).setNegativeButton("Cancel",((dialog, which) -> dialog.dismiss())).setPositiveButton("Settings", new DialogInterface.OnClickListener(){

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent= new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri=Uri.fromParts("pakage",getPackageName(),null);
                                    intent.setData(uri);
                                    startActivity(intent);

                                    dialog.dismiss();
                                }
                            }
                    );
                    goToBuilder.show();
                }
            }
        }

    }

    private void galleryIntent() {
        Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 2);
    }

    private void cameraIntent() {
        ContentValues values=new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"new image");
        values.put(MediaStore.Images.Media.DESCRIPTION,"From the camera");
        imageUri=getContentResolver().insert(MediaStore.Images.Media. EXTERNAL_CONTENT_URI, values);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, 1);
    }

    public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }


}

