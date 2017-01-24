package com.zonkey.chorepocalypse.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
    private String mCurrentPhotoPath;

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

    Intent choreDetailsIntent;

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

        choreDetailsIntent = new Intent(getApplicationContext(), MainActivity.class);

        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        mChoreDatabaseReference = mFirebaseDatabase.getReference().child("chores");

        mChorePointsEditText.setInputType(InputType.TYPE_CLASS_NUMBER);

        mChorePic.setVisibility(View.INVISIBLE);
        mAddChorePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
//                dispatchChoosePickerIntent();
            }
        });

        mAddChoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addChore();

            }
        });
    }

    public void dispatchChoosePickerIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(Intent.createChooser(intent,
                "Complete action using"), RC_PHOTO_PICKER);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
//            Bundle extras = data.getExtras();
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap imageBitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, options);
            galleryAddPic();
            setPic();
            mChorePic.setVisibility(View.VISIBLE);
            mAddChorePhotoButton.setVisibility(View.GONE);
            MediaStore.Images.Media.insertImage(getContentResolver(), imageBitmap, null, null);

        }

    }

    private void dispatchTakePictureIntent() {
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
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.zonkey.chorepocalypse.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
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
        mCurrentPhotoPath = choreImage.getAbsolutePath();
        return choreImage;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private void setPic() {
        // Get the dimensions of the View
        int targetW = mChorePic.getWidth();
        int targetH = mChorePic.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        mChorePic.setImageBitmap(bitmap);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    public void addChore() {
        Chore newChore = new Chore(mChoreName, mChorePoints);
        getAndSetChoreName(newChore);
        getAndSetChorePoints(newChore);
        mChoreDatabaseReference.push().setValue(newChore);
        putChoreStringExtras();
        Toast.makeText(AddChoreActivity.this, mChoreName + " added to list", Toast.LENGTH_SHORT).show();
        //clear text after setting chore
        mChoreNameEditText.setText("");
        mChorePointsEditText.setText("");
    }

    public void getAndSetChoreName(Chore newChore) {
        mChoreName = mChoreNameEditText.getText().toString();
        newChore.setChoreName(mChoreName);
    }

    public void getAndSetChorePoints(Chore newChore) {
        mChorePoints = mChorePointsEditText.getText().toString();
        newChore.setChoreReward(mChorePoints);
    }

    private void putChoreStringExtras() {
        choreDetailsIntent.putExtra("chore_name", mChoreName)
                .putExtra("chore_points", mChorePoints);
    }

    @Override
    public void onBackPressed() {
        putChoreStringExtras();
        startActivity(choreDetailsIntent);
        super.onBackPressed();
    }
}
