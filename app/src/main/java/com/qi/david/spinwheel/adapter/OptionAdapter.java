package com.qi.david.spinwheel.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.qi.david.spinwheel.R;

import java.util.ArrayList;

public class OptionAdapter extends RecyclerView.Adapter<OptionAdapter.MyViewHandler>{

    private Context mContext;

    private ArrayList<String> mOptions = new ArrayList<>();

    public OptionAdapter(Context context, ArrayList<String> options) {
        mContext = context;
        mOptions = options;
    }

    @NonNull
    @Override
    public MyViewHandler onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_items,viewGroup,false);
        return new MyViewHandler(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHandler myViewHandler, int i) {
        String key = mOptions.get(i);
        myViewHandler.option.setText(key);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return mOptions.size();
    }


    public class MyViewHandler extends RecyclerView.ViewHolder {
        public TextView option;

        public MyViewHandler(@NonNull View itemView) {
            super(itemView);
            option = itemView.findViewById(R.id.option);
        }
    }

}
