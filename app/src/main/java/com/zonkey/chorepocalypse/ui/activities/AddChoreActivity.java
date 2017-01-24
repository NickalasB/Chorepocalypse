package com.zonkey.chorepocalypse.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.zonkey.chorepocalypse.R;
import com.zonkey.chorepocalypse.data.Chore;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AddChoreActivity extends AppCompatActivity {

    private DatabaseReference mChoreDatabaseReference;

    private String mChoreName;
    private String mChorePoints;

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

        choreDetailsIntent = new Intent(getApplicationContext(),MainActivity.class);

        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
        mChoreDatabaseReference = mFirebaseDatabase.getReference().child("chores");

        mChorePointsEditText.setInputType(InputType.TYPE_CLASS_NUMBER);

        mAddChoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addChore();

            }
        });
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
