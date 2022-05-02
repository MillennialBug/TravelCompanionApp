package com.example.travelcompanionapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GalleryActivity extends AppCompatActivity implements RecyclerViewInterface {

    private RecyclerView mRecyclerView;
    private PlaceOfInterestImageAdapter mAdapter;
    private PlaceOfInterestGalleryViewModel placeOfInterestGalleryViewModel;
    private int mPoiId;
    private Button mGalleryButton;
    private Button mCameraButton;
    private Uri mImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        // Get intent
        Intent intent = getIntent();
        if (intent.hasExtra(PlaceOfInterestActivity.POI_ID)) {
            mPoiId = intent.getIntExtra(PlaceOfInterestActivity.POI_ID, 0);
        }

        // Get a handle to the RecyclerView.
        mRecyclerView = findViewById(R.id.galleryrecyclerview);
        // Create an adapter and supply the data to be displayed.
        mAdapter = new PlaceOfInterestImageAdapter(this, this);
        // Connect the adapter with the RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
        // Give the RecyclerView a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mGalleryButton = findViewById(R.id.button_gallery);
        mGalleryButton.setOnClickListener(view -> startGallery());

        mCameraButton = findViewById(R.id.button_camera);
        mCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    mImageUri = FileProvider.getUriForFile(GalleryActivity.this, GalleryActivity.this.getApplicationContext().getPackageName() + ".provider", createImageFile());
                    File file = new File(mImageUri.getPath());
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                    takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    captureImageResultLauncher.launch(Intent.createChooser(takePictureIntent, "Take Picture"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        // Can't figure out why the returned image isn't working. Run out of time for now.
        mCameraButton.setVisibility(View.GONE);

        // Set up database and feed recycler view.
        ViewModelProvider mViewModelProvider = new ViewModelProvider(this);
        placeOfInterestGalleryViewModel = mViewModelProvider.get(PlaceOfInterestGalleryViewModel.class);
        placeOfInterestGalleryViewModel.setUp(getApplication(), mPoiId);
        placeOfInterestGalleryViewModel.getGallery().observe(this, gallery -> {
            // Update the cached copy of the objects in the adapter.
            mAdapter.setImages(gallery);
        });

        ActivityCompat.requestPermissions( this,
                new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.MANAGE_EXTERNAL_STORAGE
                }, 1
        );
    }

    private void startGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        pickImageResultLauncher.launch(Intent.createChooser(intent, "Select Picture"));
    }

    ActivityResultLauncher<Intent> pickImageResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Intent intent = result.getData();
                    if (intent != null) {
                        if (result.getResultCode() == RESULT_OK) {
                            InputStream inputStream = null;
                            try {
                                inputStream = getApplicationContext().getContentResolver().openInputStream(intent.getData());
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                            Bitmap bmp = BitmapFactory.decodeStream(inputStream);
                            storeImage(bmp);
                        }
                    }
                }
            });

    ActivityResultLauncher<Intent> captureImageResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Intent intent = result.getData();
                    if (intent != null) {
                        if (result.getResultCode() == RESULT_OK) {
                            File file = new File(mImageUri.getPath());
                            Bitmap bmp = BitmapFactory.decodeFile(file.getPath());
                            storeImage(bmp);
                        }
                    }
                }
            }
    );

    private void storeImage(Bitmap bmp) {
        PlaceOfInterestGallery item = new PlaceOfInterestGallery();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG,10,stream);
        item.setPoiId(mPoiId);
        item.setImage(stream.toByteArray());
        placeOfInterestGalleryViewModel.insert(item);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onItemLongClick(int position) {
        AlertDialog.Builder myAlertBuilder = new
                AlertDialog.Builder(GalleryActivity.this);
        // Set the dialog title and message.
        myAlertBuilder.setTitle("Please confirm");
        myAlertBuilder.setMessage("Do you want to delete this image?");
        // Add the dialog buttons.
        myAlertBuilder.setPositiveButton("YES", (dialog, which) -> {
            // User clicked YES button.
            placeOfInterestGalleryViewModel.delete(mAdapter.getImageAtPosition(position));
            mAdapter.notifyItemRemoved(position);
        });
        myAlertBuilder.setNegativeButton("NO", (dialog, which) -> {
            // User clicked NO button.
        });

        myAlertBuilder.show();

    }

    private File createImageFile() throws IOException {
        System.out.println(Environment.getExternalStorageDirectory().getPath());
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        return image;
    }

}