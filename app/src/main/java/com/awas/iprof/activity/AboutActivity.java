package com.awas.iprof.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.awas.iprof.fragment.FragmentAbout;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentAbout about = new FragmentAbout();
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, about).commit();
    }
}
