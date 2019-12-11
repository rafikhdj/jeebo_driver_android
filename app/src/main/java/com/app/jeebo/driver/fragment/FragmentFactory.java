package com.app.jeebo.driver.fragment;

import com.app.jeebo.driver.base.BaseFragment;
import com.app.jeebo.driver.enums.EScreenType;
import com.app.jeebo.driver.modules.home.fragment.OrdersProcessFragment;
import com.app.jeebo.driver.modules.home.fragment.PendingOrdersFragment;
import com.app.jeebo.driver.modules.home.fragment.TerminatedOrdersFragment;

public class FragmentFactory {

    private final String TAG = FragmentFactory.class.getSimpleName();
    private static FragmentFactory fragmentFactory;
    private PendingOrdersFragment pendingOrdersFragment;
    private OrdersProcessFragment ordersProcessFragment;
    private TerminatedOrdersFragment terminatedOrdersFragment;

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
        switch (eScreenType) {

            case PENDING_ORDERS_SCREEN:
               // if (pendingOrdersFragment == null)
                    pendingOrdersFragment = new PendingOrdersFragment();
                baseFragment = pendingOrdersFragment;
                break;

            case ORDERS_PROCESSED_SCREEN:
               // if (ordersProcessFragment == null)
                  ordersProcessFragment = new OrdersProcessFragment();
                baseFragment = ordersProcessFragment;
                break;
            case TERMINATED_ORDERS_SCREEN:
                //if (terminatedOrdersFragment== null)
                 terminatedOrdersFragment= new TerminatedOrdersFragment();
                baseFragment = terminatedOrdersFragment;
                break;
        }

        return baseFragment;
    }


    public BaseFragment getCurrentFragment() {
        return baseFragment;
    }

    public void setCurrentFragment(BaseFragment baseFragment) {
        this.baseFragment = baseFragment;
    }


}
