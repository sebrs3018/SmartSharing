package com.sebrs3018.SmartSharing.GridCardUsers;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class UserGridItemDecoration extends RecyclerView.ItemDecoration{

    private int topBottomPadding;

    public UserGridItemDecoration(int _topBottomPadding){
        topBottomPadding = _topBottomPadding;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        outRect.top     = topBottomPadding;
        outRect.bottom  = topBottomPadding;
    }


}
