package com.acharya.habba2k18.Events;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.acharya.habba2k18.Events.adapter.CarPagerAdapter;
import com.acharya.habba2k18.MainMenu.MainActivity;
import com.acharya.habba2k18.R;
import com.acharya.habba2k18.Test.Test;

public class Event extends AppCompatActivity {

    ViewPager mViewPager;
    CarPagerAdapter carPagerAdapter;
    public int currentposition;
    Button first,three_prev,middle,three_next,last;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        getSupportActionBar().hide();

        Intent mIntent = getIntent();
        Bundle bundle = mIntent.getExtras();
        if (bundle != null) {
            currentposition = bundle.getInt("currentposition");
        }

        mViewPager = (ViewPager) findViewById(R.id.view_pager);
        mViewPager.setOffscreenPageLimit(2);
        carPagerAdapter = new CarPagerAdapter(getSupportFragmentManager());
//        mViewPager.setPageMargin((int) (getResources().getDisplayMetrics().widthPixels * -0.28));
//
//        mViewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
//            @Override public void transformPage(View page, float position) {
//                page.setScaleX(0.9f - Math.abs(position * 0.3f));
//                page.setScaleY(0.9f - Math.abs(position * 0.1f));
//                page.setAlpha(1.0f - Math.abs(position * 0.5f));
//            }
//        });
//        mViewPager.setAdapter(carPagerAdapter);
//        mViewPager.setCurrentItem(currentposition,true);

        mViewPager.setPageTransformer(true, new CardStackTransformer());

        mViewPager.setOffscreenPageLimit(5);

        mViewPager.setAdapter(carPagerAdapter);

        mViewPager.setCurrentItem(currentposition,true);

        first = (Button)findViewById(R.id.first);
        three_prev = (Button)findViewById(R.id.back3);
        middle = (Button)findViewById(R.id.middle);
        three_next = (Button)findViewById(R.id.jump3);
        last = (Button)findViewById(R.id.last);

        first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(0,true);
            }
        });

        three_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = mViewPager.getCurrentItem();
                if(position-3 <= 0)
                mViewPager.setCurrentItem(0,true);
                else  mViewPager.setCurrentItem(position-3,true);
            }
        });
        middle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem((Test.eventList.size()-1)/2,true);
            }
        });
        three_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = mViewPager.getCurrentItem();
                if(position+3 >= Test.eventList.size()-1)
                    mViewPager.setCurrentItem(Test.eventList.size()-1,true);
                else  mViewPager.setCurrentItem(position+3,true);
            }
        });
        last.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(Test.eventList.size()-1,true);
            }
        });

    }


    private class CardStackTransformer implements ViewPager.PageTransformer
    {
        @Override
        public void transformPage(View page, float position)
        {
            if(position>=0)
            {
                page.setScaleX(1f - 0.02f * position);

                page.setScaleY(1f);

                page.setTranslationX(- page.getWidth()*position);

                page.setTranslationY(30 * position);

                page.setAlpha(1.0f - Math.abs(position * 0.2f));
            }

        }
    }


    @Override
    public void onBackPressed() {
        Intent i8 = new Intent(Event.this, MainActivity.class);
        startActivity(i8);
        finish();
    }
}