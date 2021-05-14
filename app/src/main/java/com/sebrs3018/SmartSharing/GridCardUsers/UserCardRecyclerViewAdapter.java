package com.sebrs3018.SmartSharing.GridCardUsers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sebrs3018.SmartSharing.GridCardBooks.BookCardViewHolder;
import com.sebrs3018.SmartSharing.R;
import com.sebrs3018.SmartSharing.TouchCardListener.OnUserListener;
import com.sebrs3018.SmartSharing.network.BookEntry;

import org.jetbrains.annotations.NotNull;

import java.util.List;

//TODO: finire logica del grid!
public class UserCardRecyclerViewAdapter extends RecyclerView.Adapter<UserCardViewHolder>{

    private List<User> registeredUserList;       //Questa lista sarà modificata dal filtraggio
    private OnUserListener onUserListener;

    public UserCardRecyclerViewAdapter(List<User> _registeredUserList, OnUserListener _onUserListener){
        registeredUserList = _registeredUserList;
        onUserListener = _onUserListener;
    }


    @NonNull
    @NotNull
    @Override
    public UserCardViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_card, parent, false);
        return new UserCardViewHolder(layoutView, onUserListener);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull UserCardViewHolder holder, int position) {
        if (registeredUserList != null && position < registeredUserList.size()) {
            User registeredUser = registeredUserList.get(position);
            holder.username.setText(registeredUser.username);
        }
    }

    @Override
    public int getItemCount() {
        return registeredUserList.size();
    }
}
