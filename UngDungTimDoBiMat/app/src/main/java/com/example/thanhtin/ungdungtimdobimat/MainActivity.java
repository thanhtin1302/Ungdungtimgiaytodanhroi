package com.example.thanhtin.ungdungtimdobimat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.thanhtin.ungdungtimdobimat.fragments.HomeFragment;
import com.example.thanhtin.ungdungtimdobimat.fragments.NotifFragment;
import com.example.thanhtin.ungdungtimdobimat.fragments.PersonFragment;
import com.example.thanhtin.ungdungtimdobimat.fragments.PostNew;

public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;



    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //bottomnavi
        bottomNavigationView = findViewById(R.id.bottomNavBar);
        bottomNavigationView.setOnNavigationItemSelectedListener(listaner);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_content,new HomeFragment()).commit();

        //toolbar
        toolbar =(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        //toolbar.setSubtitle("welcome");

    }
    //toolbar


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tool_bar,menu);
        return true;

}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.add:

                startActivity(new Intent(MainActivity.this,PostNew.class));

                        break;
        }
        return super.onOptionsItemSelected(item);
    }
    //bottomNavi
    BottomNavigationView.OnNavigationItemSelectedListener listaner = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()){

                case R.id.home:
                    fragment = new HomeFragment();
                    loadFragment(fragment);
                    toolbar.setVisibility(View.VISIBLE);
                    return true;
//                case R.id.notif:
//                    fragment = new NotifFragment();
//                    loadFragment(fragment);
//                    toolbar.setVisibility(View.VISIBLE);
//                    return true;
                case R.id.person:
                    fragment = new PersonFragment();
                    loadFragment(fragment);
                    toolbar.setVisibility(View.GONE);
                    return true;
            }
            return false;

        }
    };

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_content,fragment);
        transaction.addToBackStack(null);
        transaction.commit();

    }
}
