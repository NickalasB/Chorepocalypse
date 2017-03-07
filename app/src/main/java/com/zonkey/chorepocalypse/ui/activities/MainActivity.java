package com.zonkey.chorepocalypse.ui.activities;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.zonkey.chorepocalypse.R;
import com.zonkey.chorepocalypse.models.Chore;
import com.zonkey.chorepocalypse.ui.adapters.BaseChoreListAdapter;
import com.zonkey.chorepocalypse.ui.fragments.ChoreDetailFragment;
import com.zonkey.chorepocalypse.ui.fragments.ChoreListFragment;
import com.zonkey.chorepocalypse.ui.widget.ChoreWidgetProvider;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity implements BaseChoreListAdapter.ChoreListAdapterInterface {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.fab)
    FloatingActionButton mFab;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addChoreIntent = new Intent(getApplicationContext(), AddChoreActivity.class);
                startActivity(addChoreIntent);
            }
        });
    }

    //Wrapping the Activity Context for custom font
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListChoreSelected(Chore chore) {
        ChoreDetailFragment choreDetailFragment = (ChoreDetailFragment)getSupportFragmentManager()
                .findFragmentById(R.id.chore_detail_fragment);
        choreDetailFragment.updateChoreBasedOnListSelection(chore);
    }

    @Override
    public void onChorePointsTotaled(int totalChorePoints) {
        ChoreDetailFragment choreDetailFragment = (ChoreDetailFragment)getSupportFragmentManager()
                .findFragmentById(R.id.chore_detail_fragment);
        choreDetailFragment.onChorePointsTotaled(totalChorePoints);
    }

    @Override
    public void onItemCountChange(int itemCount) {
        ChoreListFragment choreListFragment = (ChoreListFragment)getSupportFragmentManager()
                .findFragmentById(R.id.chore_list_fragment);
        choreListFragment.onItemCountChange(itemCount);
        ComponentName name = new ComponentName(this, ChoreWidgetProvider.class);
        int[] ids = AppWidgetManager.getInstance(this).getAppWidgetIds(name);
        Intent intent = new Intent(this, ChoreWidgetProvider.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        intent.putExtra(AppWidgetManager.ACTION_APPWIDGET_UPDATE, ids);
        sendBroadcast(intent);
    }
}