package se.svenne.assignment2a;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends FragmentActivity {
    public static final String POSITION = "POSITION";
    final String[] menuTitle = {"Random", "BMI", "Display Color", "Alarm Clock", "Countries"};

    final String[] fragments = {
            "se.svenne.assignment2a.RandomString",
            "se.svenne.assignment2a.BmiActivity",
            "se.svenne.assignment2a.RGB",
            "se.svenne.assignment2a.Alarm",
            "se.svenne.assignment2a.CountriesList"};

    private int currentPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayAdapter<String> mainAdapter = new ArrayAdapter<String>(
                getActionBar().getThemedContext(),
                android.R.layout.simple_list_item_1, menuTitle);


        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ListView navList = (ListView) findViewById(R.id.drawer);
        navList.setAdapter(mainAdapter);

        navList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                currentPos = position;
                getActionBar().setTitle(menuTitle[position]);
                drawer.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
                    @Override
                    public void onDrawerClosed(View drawerView) {
                        super.onDrawerClosed(drawerView);
                        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
                        tx.replace(R.id.main, Fragment.instantiate(MainActivity.this, fragments[position]));
                        tx.commit();
                    }
                });
                drawer.closeDrawer(navList);
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        currentPos = getSharedPreferences("hello", MODE_PRIVATE).getInt(POSITION, 0);
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.main, Fragment.instantiate(MainActivity.this, fragments[currentPos]));
        tx.commit();
    }

    @Override
    protected void onPause(){
        super.onPause();
        getSharedPreferences("hello", MODE_PRIVATE).edit().putInt(POSITION, currentPos).commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //No call for super(). Bug on API Level > 11.
    }

}

