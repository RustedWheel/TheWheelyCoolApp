package com.qi.david.spinwheel;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.qi.david.spinwheel.views.SpinWheel;

import java.util.ArrayList;

public class SpinWheelActivity extends AppCompatActivity {

    private SpinWheel mWheel;
    private Button mSpinWheelBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spin_wheel);

        mWheel = findViewById(R.id.custom_view);

        ArrayList<String> options = new ArrayList<>();
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            finish();
        } else {
            options = extras.getStringArrayList("options");
        }
        mWheel.setOptions(options);

        mSpinWheelBtn = findViewById(R.id.spinBtn);
        mSpinWheelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWheel.spinWheel(10000);
            }
        });
    }

}
