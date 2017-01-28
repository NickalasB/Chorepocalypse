package com.zonkey.chorepocalypse.ui.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.zonkey.chorepocalypse.R;
import com.zonkey.chorepocalypse.models.Chore;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AddChoreActivity extends AppCompatActivity {

    static final int REQUEST_TAKE_PHOTO = 1;
    private static final int RC_PHOTO_PICKER = 2;
    private DatabaseReference mChoreDatabaseReference;

    private String mChoreName;
    private String mChorePoints;
    //    private String mSelectedImageUri;
    private String mSelectedImageUri;

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mStorageReference;

    @BindView(R.id.add_chore_name)
    EditText mChoreNameEditText;

    @BindView(R.id.add_chore_pic)
    ImageView mChorePic;

    @BindView(R.id.chore_due_date_button)
    Button mChoreDueDateButton;

    @BindView(R.id.chore_status_textview)
    TextView mChoreStatusTextView;

    @BindView(R.id.chore_checkbox)
    CheckBox mChoreCheckbox;

    @BindView(R.id.current_chore_points)
    EditText mChorePointsEditText;

    @BindView(R.id.add_chore_button)
    Button mAddChoreButton;

    @BindView(R.id.add_chore_photo_picker_button)
    ImageButton mAddChorePhotoButton;

    Intent mChoreDetailsIntent;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //defining custom default font
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/GloriaHallelujah.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
        setContentView(R.layout.activity_add_chore);
        ButterKnife.bind(this);

        mChoreDetailsIntent = new Intent(getApplicationContext(), MainActivity.class);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mChoreDatabaseReference = mFirebaseDatabase.getReference().child("chores");
        mFirebaseStorage = FirebaseStorage.getInstance();
        mStorageReference = mFirebaseStorage.getReference();


        mChorePointsEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        mChorePic.setVisibility(View.INVISIBLE);
        mAddChorePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                launchCameraPhotoPicker();
                launchImageChooser();
            }
        });

        mAddChoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addChore();
            }
        });
    }

    public void launchImageChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), RC_PHOTO_PICKER);
    }

    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            mSelectedImageUri = imageUri.toString();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                mChorePic.setImageBitmap(bitmap);
                mChorePic.setVisibility(View.VISIBLE);
                mAddChorePhotoButton.setVisibility(View.INVISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void launchCameraPhotoPicker() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast.makeText(this, "Error creating photo file", Toast.LENGTH_SHORT).show();
            }
            if (photoFile != null) {
                Uri photoUri = FileProvider.getUriForFile(this,
                        "com.zonkey.chorepocalypse.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
                mSelectedImageUri = photoUri.toString();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), photoUri);
                    mChorePic.setImageBitmap(bitmap);
                    mChorePic.setVisibility(View.VISIBLE);
                    mAddChorePhotoButton.setVisibility(View.INVISIBLE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File choreImage = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        mSelectedImageUri = choreImage.getAbsolutePath();
        return choreImage;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mSelectedImageUri);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }


    public void addChore() {
        final Chore newChore = new Chore(mChoreName, mChorePoints, mSelectedImageUri);
        if (mChoreNameEditText.getText().length() == 0) {
            Toast.makeText(this, "Gotta give yer chore a name!", Toast.LENGTH_SHORT).show();
        } else {
            pushPhotoToFirebase(newChore);
            setChoreValues(newChore);
            putChoreStringExtras();
            mChoreDatabaseReference.push().setValue(newChore);
        }
    }

    public void pushPhotoToFirebase(final Chore newChore) {
        if (mSelectedImageUri != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading Chore");
            progressDialog.show();
            StorageReference chorePhotosReference = mStorageReference.child("chore_photos/" + mChoreNameEditText.getText());
            chorePhotosReference.putFile(Uri.parse(mSelectedImageUri))
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            getAndSetChorePhotoUrl(newChore);
                            //clear text and views after setting chore
                            mChoreNameEditText.setText("");
                            mChorePointsEditText.setText("");
                            mChorePic.setVisibility(View.INVISIBLE);
                            mAddChorePhotoButton.setVisibility(View.VISIBLE);
                            Toast.makeText(AddChoreActivity.this, mChoreName + " added to list!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            progressDialog.dismiss();
                            Toast.makeText(AddChoreActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        } else {
            Toast.makeText(this, "Oops something went wrong!!", Toast.LENGTH_SHORT).show();
        }
    }

    public void setChoreValues(Chore newChore) {
        getAndSetChorePhotoUrl(newChore);
        getAndSetChoreName(newChore);
        getAndSetChorePoints(newChore);
    }

    public void getAndSetChoreName(Chore newChore) {
        mChoreName = mChoreNameEditText.getText().toString();
        newChore.setChoreName(mChoreName);
    }

    public void getAndSetChorePoints(Chore newChore) {
        mChorePoints = mChorePointsEditText.getText().toString();
        newChore.setChoreReward(mChorePoints);
    }

    public void getAndSetChorePhotoUrl(Chore newChore) {
        newChore.setChorePhotoUrl(mSelectedImageUri);
    }

    private void putChoreStringExtras() {
        mChoreDetailsIntent.putExtra("chore_name", mChoreName)
                .putExtra("chore_points", mChorePoints);
    }

    @Override
    public void onBackPressed() {
        putChoreStringExtras();
        startActivity(mChoreDetailsIntent);
        super.onBackPressed();
    }
}
