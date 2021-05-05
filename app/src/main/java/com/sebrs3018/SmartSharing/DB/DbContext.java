package com.sebrs3018.SmartSharing.DB;

import android.content.Context;

public class DbContext {
    private Context context;

    public DbContext(Context _context){
        context = _context;
    }

    public Context getContext() {
        return context;
    }
}
