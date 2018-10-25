package com.qi.david.spinwheel.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OptionsStore {

    private static final String TAG = "OptionsStore";
    private static final String FILE_STORE_NAME = "options";
    private SharedPreferences sharedpreferences;

    public OptionsStore(final Context context) {
        sharedpreferences = context.getSharedPreferences(FILE_STORE_NAME, Context.MODE_PRIVATE);
    }

    public void addNewOptionItem(final String option) throws IllegalArgumentException {
        String existingOption = sharedpreferences.getString(option, null);
        if (existingOption != null) {
            throw new IllegalArgumentException("Option " + option + " already exists");
        }
        sharedpreferences.edit().putString(option, option).apply();
    }

    public void removeOption(final String option) {
        sharedpreferences.edit().remove(option).apply();
    }

    public int getOptionsCount() {
        return sharedpreferences.getAll().size();
    }

    public ArrayList<String> getOptions() {
        final ArrayList<String> result = new ArrayList<>();
        for (final Object o : this.sharedpreferences.getAll().entrySet()) {
            final Map.Entry entry = (Map.Entry) o;
            result.add(entry.getKey().toString());
        }
        return result;
    }
}
