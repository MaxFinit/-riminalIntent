package com.maxfin.criminalintent;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrimePagerActivity extends AppCompatActivity {
    private static final String EXTRA_CRIME_ID =
            "com.maxfin.criminalintent.crime_id";

    private ViewPager mViewPager;
    private List<Crime> mCrimes;
    private Button mStartButton;
    private Button mEndButton;
    private ImageButton mDeleteCrimeButton;

    public static Intent newIntent(Context packageContext, UUID crimeId) {
        Intent intent = new Intent(packageContext, CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);

        UUID crimeId = (UUID) getIntent()
                .getSerializableExtra(EXTRA_CRIME_ID);
        mViewPager = findViewById(R.id.crime_view_pager);


        mCrimes = CrimeLab.get(this).getListCrimes();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Crime crime = mCrimes.get(position);
                return CrimeFragment.newInstance(crime.getId());
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });

        for (int i = 0; i < mCrimes.size(); i++) {
            if (mCrimes.get(i).getId().equals(crimeId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }

        mStartButton = findViewById(R.id.start_button);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(0);
            }
        });
        mEndButton = findViewById(R.id.end_button);
        mEndButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(mCrimes.size());
            }
        });
        mDeleteCrimeButton = findViewById(R.id.delete_crime_button);
        mDeleteCrimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CrimeLab.get(getApplicationContext()).removeCrime(mCrimes.get(mViewPager.getCurrentItem()));
                finish();
            }
        });

    }
}
