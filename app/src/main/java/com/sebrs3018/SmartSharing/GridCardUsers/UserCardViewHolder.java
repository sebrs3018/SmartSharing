package com.sebrs3018.SmartSharing.GridCardUsers;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sebrs3018.SmartSharing.R;
import com.sebrs3018.SmartSharing.TouchCardListener.OnUserListener;

import org.jetbrains.annotations.NotNull;

public class UserCardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


    public TextView username;
    OnUserListener onUserListener;

    public UserCardViewHolder(@NonNull @NotNull View itemView, OnUserListener _onUserListener) {
        super(itemView);
        username = itemView.findViewById(R.id.registeredUsername);
        onUserListener = _onUserListener;

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //E' qua che passo la posizione dell'elemento (View holder) appena toccato
        onUserListener.onUserClick(getBindingAdapterPosition());
    }
}
