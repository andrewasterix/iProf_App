package com.awas.iprof;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.awas.iprof.activity.AboutActivity;
import com.awas.iprof.activity.LoginActivity;
import com.awas.iprof.fragment.FragmentAbout;
import com.awas.iprof.fragment.FragmentClass;
import com.awas.iprof.fragment.FragmentMap;
import com.awas.iprof.fragment.FragmentProfile;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    // Tag LOG
    private String TAG = "MainActivity";

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private int[] tabIcons = {
            R.drawable.baseline_person_black_36,
            R.drawable.baseline_class_black_36,
            R.drawable.baseline_map_black_36,
    };

    /*  Deprecated 0.3 Version
        private Button logout;
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        // FireBase Session Control
        if(mAuth.getCurrentUser() == null){
            // Back To LoginActivity if User isn't logged
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }

        // Setup Material Element and Tabbed Layout
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tabs);

        // Set up ViewPage with Fragment
        setupViewPage(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        // Set Icon instead of Tab's Name
        setTabIcons();

        /* Deprecated 0.3 Version

        logout = findViewById(R.id.logout_button);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();

                //Toast.makeText(getApplicationContext(), "Logout OK", Toast.LENGTH_LONG).show();;
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });
        */
    }

    // Update 0.4.1 - Add menu options
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    // Update 0.4.1 - Add menu options with AboutDialog and Logout
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_about:
                /* Deprecated version 0.4.1 - Alert Dialog for About
                new AlertDialog.Builder(this).setTitle("iProf App").setMessage("Version: 0.4.1").setPositiveButton("Ok", null).show();
                */

                // Version 0.4.5 - About Activity and Fragment
                startActivity(new Intent(MainActivity.this, AboutActivity.class));

                // Toast.makeText(getApplicationContext(), "about", Toast.LENGTH_LONG).show();
                return true;

            case R.id.menu_logout:
                // Toast.makeText(getApplicationContext(), "LogOut OK", Toast.LENGTH_LONG).show();
                mAuth.signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Set up ViewPage with fragment Adapter
    private void setupViewPage (ViewPager viewPager){

        ViewPageAdapter viewPagerAdapter = new ViewPageAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new FragmentProfile(), getString(R.string.tab_profile));
        viewPagerAdapter.addFragment(new FragmentClass(), getString(R.string.tab_class));
        viewPagerAdapter.addFragment(new FragmentMap(), getString(R.string.tab_map));

        viewPager.setAdapter(viewPagerAdapter);
    }

    // Set up Icons
    private void setTabIcons (){
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }

    // Fragment Adapter
    class ViewPageAdapter extends FragmentPagerAdapter {

        // Fragment Array
        private ArrayList<Fragment> fragmentArrayList = new ArrayList<>();
        // Fragment Title Array
        private ArrayList<String> fragmentStringArrayList = new ArrayList<>();

        // Constructor
        public ViewPageAdapter(FragmentManager manager) {
            super(manager);
        }

        // Get Position of Fragment in Array Adapter
        @Override
        public Fragment getItem(int position) {
            return fragmentArrayList.get(position);
        }

        // Get Size of Fragment Array Adapter
        @Override
        public int getCount() {
            return fragmentArrayList.size();
        }

        // Add a Fragment in Array using ArrayList.add
        public void addFragment(Fragment fragment, String title){
            fragmentArrayList.add(fragment);
            fragmentStringArrayList.add(title);
        }

        // Get Title Position in Array
        @Override
        public CharSequence getPageTitle(int position){
            // Get Title of Fragment
            // return fragmentStringArrayList.get(position);

            // No Title to return, so it shows only Icons
            return null;
        }
    }
}
