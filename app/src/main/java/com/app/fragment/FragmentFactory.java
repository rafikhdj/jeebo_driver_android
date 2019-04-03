package com.app.fragment;

import com.app.base.BaseFragment;
import com.app.enums.EScreenType;

public class FragmentFactory {

    private final String TAG = FragmentFactory.class.getSimpleName();
    private static FragmentFactory fragmentFactory;

    //fragments to be launched on authActivity
    private BaseFragment baseFragment;


    public static FragmentFactory getInstance() {

        if (fragmentFactory == null) {
            fragmentFactory = new FragmentFactory();
        }
        return fragmentFactory;
    }

    /**
     * method to get fragment
     *
     * @param eScreenType
     * @return
     */
    public BaseFragment getFragment(EScreenType eScreenType) {
//        switch (eScreenType) {
//
//            case SONG_LIST_SCREEN:
//                if (songsFragment == null)
//                    songsFragment = new SongsFragment();
//                baseFragment = songsFragment;
//                break;
//        }

        return baseFragment;
    }


    public BaseFragment getCurrentFragment() {
        return baseFragment;
    }

    public void setCurrentFragment(BaseFragment baseFragment) {
        this.baseFragment = baseFragment;
    }


}
