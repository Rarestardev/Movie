package com.rarestardev.movie.activities;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.tabs.TabLayout;
import com.rarestardev.movie.R;
import com.rarestardev.movie.databinding.ActivityMainBinding;
import com.rarestardev.movie.tab_fragments.HomeFragment;
import com.rarestardev.movie.tab_fragments.SearchFragment;
import com.rarestardev.movie.tab_fragments.WatchlistFragment;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    private static final String[] TabsTitle = {"Home", "Watchlist", "Search"};
    private static final int[] TabsIcon = {R.drawable.baseline_home_24, R.drawable.baseline_bookmark_24, R.drawable.baseline_search_24};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        setupTabViewPagers();

    }

    private void setupTabViewPagers() {
        binding.tabs.addTab(binding.tabs.newTab().setIcon(TabsIcon[0]).setText(TabsTitle[0]));
        binding.tabs.addTab(binding.tabs.newTab().setIcon(TabsIcon[1]).setText(TabsTitle[1]));
        binding.tabs.addTab(binding.tabs.newTab().setIcon(TabsIcon[2]).setText(TabsTitle[2]));

        // Default Page
        replaceFragment(new HomeFragment());

        binding.tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment selectedFragment = null;
                switch (tab.getPosition()) {
                    case 0:
                        selectedFragment = new HomeFragment();
                        break;
                    case 1:
                        selectedFragment = new WatchlistFragment();
                        break;
                    case 2:
                        selectedFragment = new SearchFragment();
                        break;
                }
                if (selectedFragment != null) {
                    replaceFragment(selectedFragment);
                }

                Drawable icon = tab.getIcon();
                if (icon != null) {
                    icon.setTint(getColor(R.color.white));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Drawable icon = tab.getIcon();
                if (icon != null) {
                    icon.setTint(getColor(R.color.TabsIconUnselect));
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Drawable icon = tab.getIcon();
                if (icon != null) {
                    icon.setTint(getColor(R.color.white));
                }
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
}