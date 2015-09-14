package com.mynotes.prajyot.moonshot;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class DrawerFragment extends Fragment {

    private ActionBarDrawerToggle mToggle;
    private DrawerLayout mDrawerLayout;

    public DrawerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // View drawer = inflater.inflate(R.layout.fragment_drawer, container, false);
        // TextView area = (TextView)drawer.findViewById(R.id.selected_area);
        //area.setText(AreaActivity.area+", "+AreaActivity.city);
        return inflater.inflate(R.layout.fragment_drawer, container, false);
        //return area;
    }

    public void setUp(DrawerLayout drawerLayout,Toolbar toolbar){
        mDrawerLayout = drawerLayout;
        mToggle = new ActionBarDrawerToggle(getActivity(),drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawerLayout.setDrawerListener(mToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mToggle.syncState();
            }
        });
    }

}
