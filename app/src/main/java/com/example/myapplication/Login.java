package com.example.myapplication;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class Login extends FragmentActivity {

    ViewPager2 viewPager;
    private FragmentStateAdapter pagerAdapter;
    private final int totalTabs = 2;
    TabLayout tabLayout;
    private String [] name = {"Login", "Sign up"};

    EditText mEmail, mPassword;
    Button mLoginBtn;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swiping_fragment);


        // Instantiate ViewPager2 using id and PagerAdapter below
        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.pager);

        pagerAdapter = new LoginAdapter(this);
        viewPager.setAdapter(pagerAdapter);


        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText((name[position]))
        ).attach();
    }

    public class LoginAdapter extends FragmentStateAdapter {

        public LoginAdapter(FragmentActivity fm) {
            super(fm);
        }

        // Supply instances of loginFragment as new pages
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new loginFragment();
                case 1:
                    return new signupFragment();
                default:
                    return null;
            }
        }

        // return the no. of page the adapter will create
        @Override
        public int getItemCount() {
            return totalTabs;
        }


    }
}