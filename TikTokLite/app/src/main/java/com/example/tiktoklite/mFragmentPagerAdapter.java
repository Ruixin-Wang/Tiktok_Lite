package com.example.tiktoklite;




import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class mFragmentPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> mlist;
        public mFragmentPagerAdapter(FragmentManager fm, List<Fragment> list) {
            super(fm);
            this.mlist = list;
        }
        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mlist.get(position);
        }
        @Override
        public int getCount() {
            return mlist.size();
        }

    public interface IOnItemClickListener {
        void onItemCLick();
    }
    }
