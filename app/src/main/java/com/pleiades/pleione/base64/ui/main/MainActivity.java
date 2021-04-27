package com.pleiades.pleione.base64.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.pleiades.pleione.base64.R;
import com.pleiades.pleione.base64.ui.SettingsActivity;

import org.apache.commons.codec.binary.Base64;
import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {
    ViewPager viewPager;

    String externalInput;
    boolean isExternalInputBase64;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View appbar = findViewById(R.id.appbar_main);
        Toolbar toolbar = appbar.findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.layout_tab);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int index = viewPager.getCurrentItem();
                PlaceholderFragment fragment = (PlaceholderFragment) sectionsPagerAdapter.instantiateItem(viewPager, index);

                fragment.convert();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // initialize intent
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.getAction().equals(Intent.ACTION_PROCESS_TEXT) && intent.hasExtra(Intent.EXTRA_PROCESS_TEXT)) {
                externalInput = intent.getStringExtra(Intent.EXTRA_PROCESS_TEXT);
                isExternalInputBase64 = Base64.isBase64(externalInput);
                viewPager.setCurrentItem(isExternalInputBase64 ? 1 : 0);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    class SectionsPagerAdapter extends FragmentPagerAdapter {

        // constructor
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @NotNull
        @Override
        public Fragment getItem(int position) {
            if (externalInput != null)
                if ((position == 0 && !isExternalInputBase64) || position == 1 && isExternalInputBase64) {
                    Fragment fragment = PlaceholderFragment.newInstance(position, externalInput);
                    externalInput = null;
                    return fragment;
                }
            return PlaceholderFragment.newInstance(position, null);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return position == 0 ? "ENCODE" : "DECODE";
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}