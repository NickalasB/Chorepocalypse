package com.zonkey.chorepocalypse.ui.activities;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.format.DateUtils;
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
import com.zonkey.chorepocalypse.receivers.AlarmReceiver;
import com.zonkey.chorepocalypse.services.PhotoUploadIntentService;
import com.zonkey.chorepocalypse.ui.fragments.TimePickerFragment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AddChoreActivity extends AppCompatActivity implements TimePickerFragment.OnDueDateSelectedListener {

    static final int REQUEST_TAKE_PHOTO = 1;
    private static final int RC_PHOTO_PICKER = 2;

    private String mChoreName;
    private String mChorePoints;
    private String mSelectedImageUri;
    private long mChoreTime;
    private boolean mchoreApprovalRequired = true;
    private boolean mchoreApproved = true;


    private DatabaseReference mChoreDatabaseReference;

    private boolean mDueDateSelected;
    private boolean mChoreNameSelected;


    @BindView(R.id.add_chore_name)
    EditText mChoreNameEditText;

    @BindView(R.id.add_chore_pic)
    ImageView mChorePic;

    @BindView(R.id.chore_due_date_button)
    Button mChoreDueDateButton;

    @BindView(R.id.add_chore_due_date_textview)
    TextView mChoreDueDateTextview;

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

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        mChoreDatabaseReference = firebaseDatabase.getReference().child("chores");

        mChorePointsEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        mChorePic.setVisibility(View.INVISIBLE);
        setChorePhotoClickListener();
        setAddChoreClickListener();

        mChoreDueDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timePickerFragment = new TimePickerFragment();
                timePickerFragment.show(getSupportFragmentManager(), "timePicker");
            }
        });
    }

    private void setAddChoreClickListener() {
        mAddChoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addChore();
                //clear text and views after setting chore
                if (mDueDateSelected && mChoreNameSelected) {
                    mChoreNameEditText.setText("");
                    mChorePointsEditText.setText("");
                    mChoreDueDateButton.setText(getString(R.string.add_chore_due_button_text));
                    mChoreDueDateTextview.setText(getText(R.string.add_chore_due_date_text));
                    mChorePic.setVisibility(View.INVISIBLE);
                    mAddChorePhotoButton.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void setChorePhotoClickListener() {
        mAddChorePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence imageSources[] = new CharSequence[]{
                        getString(R.string.pic_picker_camera),
                        getString(R.string.pic_picker_gallery)
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(AddChoreActivity.this);
                builder.setTitle("Choose image using...");
                builder.setItems(imageSources, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i) {
                            case 0:
                                launchCameraPhotoPicker();
                                break;
                            case 1:
                                launchImageChooser();
                                break;
                        }
                    }
                });
                builder.show();
            }
        });
    }

    //    handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            mSelectedImageUri = imageUri.toString();
            Uri.parse(mSelectedImageUri);
            try {
                setChorePhoto(imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            Uri imageUri = Uri.parse(mSelectedImageUri);
            try {
                setChorePhoto(imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setChorePhoto(Uri imageUri) throws IOException {
        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
        mChorePic.setImageBitmap(bitmap);
        mChorePic.setVisibility(View.VISIBLE);
        mAddChorePhotoButton.setVisibility(View.INVISIBLE);
    }

    public void launchImageChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), RC_PHOTO_PICKER);
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
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
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

    public void addChore() {
        final Chore newChore = new Chore(mChoreName, mChorePoints, mSelectedImageUri, mChoreTime, null, mchoreApprovalRequired, mchoreApproved);
        if (mChoreNameEditText.getText().length() == 0) {
            Toast.makeText(this, R.string.add_chore_blank_chore_toast, Toast.LENGTH_SHORT).show();
        } else if (!mDueDateSelected) {
            Toast.makeText(this, R.string.add_chore_no_due_date, Toast.LENGTH_SHORT).show();
        } else {
            setChoreValues(newChore);
            mChoreNameSelected = true;
            mDueDateSelected = true;
            Toast.makeText(AddChoreActivity.this, getString(R.string.toast_saving_chore) + " " + mChoreName, Toast.LENGTH_SHORT).show();
            DatabaseReference newChoreReference = mChoreDatabaseReference.push();
            newChore.setChoreKey(newChoreReference.getKey());
            newChoreReference.setValue(newChore);
            pushPhotoToFirebase(newChore);
            setAlarm(newChore);
        }

    }

    public void pushPhotoToFirebase(final Chore newChore) {
        PhotoUploadIntentService.uploadPhoto(this, newChore, mSelectedImageUri);
    }

    public void setChoreValues(Chore newChore) {
        getAndSetChoreName(newChore);
        getAndSetChorePoints(newChore);
        getAndSetChoreCheckBoxStatus(newChore);
    }

    public void getAndSetChoreName(Chore newChore) {
        mChoreName = mChoreNameEditText.getText().toString();
        newChore.setChoreName(mChoreName);
    }
    public void getAndSetChorePoints(Chore newChore) {
        mChorePoints = mChorePointsEditText.getText().toString();
        if (mChorePoints.length() == 0){
            mChorePoints = String.valueOf(0);
        }
        newChore.setChoreReward(mChorePoints);
    }

    public void getAndSetChoreCheckBoxStatus(Chore newChore) {
        if (mChoreCheckbox.isChecked()) {
            newChore.setChoreApprovalRequired(mchoreApprovalRequired);
            mchoreApprovalRequired = true;
            newChore.setChoreApproved(!mchoreApproved);
            mchoreApproved = false;
        } else {
            newChore.setChoreApprovalRequired(!mchoreApprovalRequired);
            mchoreApprovalRequired = false;
            newChore.setChoreApproved(mchoreApproved);
            mchoreApproved = true;
        }
    }

    @Override
    public void onDueDateSelected(long timeInMillis) {
        Chore chore = new Chore();
        Calendar currentTime = Calendar.getInstance();
        mChoreTime = timeInMillis;
        int timeFlag = DateUtils.FORMAT_SHOW_TIME;
        int dateFlag = DateUtils.FORMAT_SHOW_DATE;
        String timeString = DateUtils.formatDateTime(this, mChoreTime, timeFlag);
        String dateString = DateUtils.formatDateTime(this, mChoreTime, dateFlag);
        if (currentTime.getTimeInMillis() >= mChoreTime) {
            Toast.makeText(this, R.string.add_chore_past_time_text, Toast.LENGTH_SHORT).show();
        } else {
            chore.setChoreTime(mChoreTime);
            mChoreDueDateTextview.setText(dateString + getString(R.string.add_chore_at_string) + timeString);
            mChoreDueDateButton.setText(R.string.add_chore_change_date_text);
            mDueDateSelected = true;
        }
    }

    public void setAlarm(Chore chore) {
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("choreKey", chore.getChoreKey());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, chore.getChoreKey().hashCode(), intent, 0);
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, mChoreTime, pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, mChoreTime, pendingIntent);
        }
    }


}
