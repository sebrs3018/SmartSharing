package com.sebrs3018.SmartSharing.GridCardUsers;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sebrs3018.SmartSharing.R;

import org.jetbrains.annotations.NotNull;

public class UserCardViewHolder extends RecyclerView.ViewHolder{


    public TextView username;

    public UserCardViewHolder(@NonNull @NotNull View itemView) {
        super(itemView);
        username = itemView.findViewById(R.id.registeredUsername);
    }
}
