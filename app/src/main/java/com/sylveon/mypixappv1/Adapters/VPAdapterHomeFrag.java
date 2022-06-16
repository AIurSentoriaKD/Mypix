package com.sylveon.mypixappv1.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.sylveon.mypixappv1.ui.home.TabsHome.HomeFragmentMain;
import com.sylveon.mypixappv1.ui.home.TabsHome.HomeFragmentNewest;

public class VPAdapterHomeFrag extends FragmentStateAdapter {

    public VPAdapterHomeFrag(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1){
            return new HomeFragmentNewest();
        }
        return new HomeFragmentMain();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
