package com.sebrs3018.SmartSharing.GridCardUsers;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sebrs3018.SmartSharing.R;
import com.sebrs3018.SmartSharing.TouchCardListener.OnTouchedItemListener;

import org.jetbrains.annotations.NotNull;

public class UserCardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private static final String TAG = "UserCardViewHolder";
    public TextView username;
    OnTouchedItemListener onTouchedItemListener;
    private String from;

    public UserCardViewHolder(@NonNull @NotNull View itemView, OnTouchedItemListener _onTouchedItemListener, String _from) {
        super(itemView);
        username = itemView.findViewById(R.id.registeredUsername);
        onTouchedItemListener = _onTouchedItemListener;
        from = _from;

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //E' qua che passo la posizione dell'elemento (View holder) appena toccato
        onTouchedItemListener.onItemTouched(getBindingAdapterPosition(), from);
    }
}
