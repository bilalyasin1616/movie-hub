package com.eg.moviehub;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;

import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.eg.moviehub.Fragments.HomeFragment;
import com.eg.moviehub.Fragments.SettingsFragment;
import com.eg.moviehub.Fragments.UploadFragment;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Fragment fragment1;
    Fragment fragment2;
    Fragment fragment3,fragment4;

    FragmentManager fragmentManager=getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView=findViewById(R.id.bottom_nav_bar);
        BottomNavigationViewHelper.removeShiftMode(bottomNavigationView);

        fragment1= new HomeFragment();
        fragment2= new HomeFragment();
        fragment3= new SettingsFragment();
        fragment4= new UploadFragment();
        fragmentManager=getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.frame_layout_main,fragment1,"fragment1");
        fragmentTransaction.show(fragment1);
        fragmentTransaction.commit();
        setFragmentsInBottomNavBar();

//        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
//        getSupportActionBar().setDisplayShowCustomEnabled(true);
//        getSupportActionBar().setCustomView(R.layout.custom_action_bar);
//        getSupportActionBar().setElevation(0);
//        View view = getSupportActionBar().getCustomView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        999);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            // Permission has already been granted
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_search,menu);
        MenuItem menuItem=menu.findItem(R.id.actionBar_searchMenu);
        SearchView searchView=(SearchView)menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.i("Submited Query",query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i("Search Query",newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);

    }
    public  void setFragmentsInBottomNavBar(){
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    //hide show approach
                    case R.id.nav_1:
                    {
                        // setFragment(fragment1);
                        FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.hide(fragment2);
                        fragmentTransaction.hide(fragment4);
                        fragmentTransaction.hide(fragment3);
                        fragmentTransaction.show(fragment1);
                        fragmentTransaction.commit();
                        return true;
                    }
                    case R.id.nav_2:
                    {
//                        setFragment(fragment2);

                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        if(fragmentManager.findFragmentByTag("fragment2")==null) {
                            fragmentTransaction.add(R.id.frame_layout_main, fragment2, "fragment2");

                        }
                        fragmentTransaction.hide(fragment4);
                        fragmentTransaction.hide(fragment1);
                        fragmentTransaction.hide(fragment3);
                        fragmentTransaction.show(fragment2);
                        fragmentTransaction.commit();


                        return true;
                    }
                    case R.id.nav_3:
                    {
                        // setFragment(fragment4);
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        if(fragmentManager.findFragmentByTag("fragment4")==null) {
                            fragmentTransaction.add(R.id.frame_layout_main, fragment4, "fragment4");
                        }
                        fragmentTransaction.hide(fragment2);
                        fragmentTransaction.hide(fragment1);
                        fragmentTransaction.hide(fragment3);
                        fragmentTransaction.show(fragment4);
                        fragmentTransaction.commit();
                        return true;
                    }
                    case R.id.nav_4:{
//                        setFragment(fragment3);
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        if(fragmentManager.findFragmentByTag("fragment3")==null) {
                            fragmentTransaction.add(R.id.frame_layout_main, fragment3, "fragment3");

                        }
                        fragmentTransaction.hide(fragment4);
                        fragmentTransaction.hide(fragment2);
                        fragmentTransaction.hide(fragment1);
                        fragmentTransaction.show(fragment3);
                        fragmentTransaction.commit();
                        break;
                    }
                }
                return true;
            }
        });

    }


}
