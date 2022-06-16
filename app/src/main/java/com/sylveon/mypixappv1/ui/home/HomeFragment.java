package com.sylveon.mypixappv1.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.sylveon.mypixappv1.Adapters.VPAdapterHomeFrag;
import com.sylveon.mypixappv1.MainActivity;
import com.sylveon.mypixappv1.R;
import com.sylveon.mypixappv1.databinding.FragmentHomeBinding;
import com.sylveon.mypixappv1.ui.home.TabsHome.HomeFragmentMain;
import com.sylveon.mypixappv1.ui.home.TabsHome.HomeFragmentNewest;

public class HomeFragment extends Fragment{

    private FragmentHomeBinding binding;

    private View root;
    private TabLayout tabLayoutHome;
    private ViewPager2 viewPagerHome;
    protected static VPAdapterHomeFrag adapterHomeFragments;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        root = binding.getRoot();
        //root = inflater.inflate(R.layout.fragment_home, container, false);
        loadTabViewFragments();
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Log.d("TAG", "back presionado");
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
        return root;
        //return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).getCheckedItem();
        // esta linea segun recarga los elementos de los fragments dentro del tabview
        // tal vez me sirva mas adelante
        //loadTabViewFragments();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private void loadTabViewFragments(){
        FragmentManager fragmentManagerHome = getChildFragmentManager();
        adapterHomeFragments = new VPAdapterHomeFrag(fragmentManagerHome, getLifecycle());
        viewPagerHome = root.findViewById(R.id.viewPager2Home);
        viewPagerHome.setUserInputEnabled(false);
        tabLayoutHome = root.findViewById(R.id.tabLayoutHome);

        viewPagerHome.setAdapter(adapterHomeFragments);
        tabLayoutHome.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPagerHome.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPagerHome.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayoutHome.selectTab(tabLayoutHome.getTabAt(position));
            }
        });

    }

}