package com.warbargic.school.Home.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.warbargic.school.R;

/**
 * Created by kippe_000 on 2017-04-21.
 */

public class fragment_class3 extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_viewpager_image, container, false);


        ImageView imageView = (ImageView)view.findViewById(R.id.school_home);
        imageView.setImageResource(R.drawable.school_home3);


        return view;
    }
}