package com.qi.david.spinwheel;

import android.content.Intent;
import android.graphics.Canvas;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.qi.david.spinwheel.adapter.OptionAdapter;
import com.qi.david.spinwheel.model.OptionsStore;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText mOptionInput;
    private ImageButton mBtnAddOption;
    private Button mBtnDone;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView mOptionsRecyclerView;
    private SwipeController mSwipeController;
    private OptionsStore mOptionStore;
    private ArrayList<String> mOptionsList = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mOptionsRecyclerView = findViewById(R.id.list_view);
        mOptionInput = findViewById(R.id.option_input);
        mBtnAddOption = findViewById(R.id.add_option);
        mBtnDone = findViewById(R.id.doneBtn);

        setAdapterData();

        setUpButtons();

        mSwipeController = new SwipeController(new SwipeControllerAction() {
            @Override
            public void onRightClicked(int position) {
                deleteOption(position);
            }

            @Override
            public void onLeftClicked(int position) {
                super.onLeftClicked(position);
            }
        });

        mAdapter = new OptionAdapter(this, mOptionsList);
        mOptionsRecyclerView.setAdapter(mAdapter);
        mOptionsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(mSwipeController);
        itemTouchhelper.attachToRecyclerView(mOptionsRecyclerView);


        mOptionsRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                mSwipeController.onDraw(c);
            }
        });
    }

    private void setUpButtons() {

        mBtnAddOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String newOption = mOptionInput.getText().toString();

                if (newOption != null && !newOption.isEmpty() && newOption != "") {

                    try {
                        mOptionStore.addNewOptionItem(newOption);
                        mOptionsList.add(newOption);
                        mAdapter.notifyDataSetChanged();
                        mOptionInput.getText().clear();
                    } catch (IllegalArgumentException e) {
                        createToast(e.getMessage());
                    }

                } else {
                    createToast("The option cannot be empty!");
                }
            }
        });

        mBtnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mOptionsList.size() > 1) {

                    Intent intent = new Intent(getApplicationContext(), SpinWheelActivity.class);
                    intent.putStringArrayListExtra("options", mOptionsList);
                    startActivity(intent);

                } else {
                    createToast("More than 1 options are needed!");
                }
            }
        });

    }

    private void setAdapterData() {
        mOptionStore = new OptionsStore(this);
        mOptionsList = mOptionStore.getOptions();
    }

    RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            // Could hide open views here if you wanted. //
        }
    };

    private void createToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    private void deleteOption(int i) {
        String optionDeleted = mOptionsList.get(i);
        mOptionStore.removeOption(optionDeleted);
        mOptionsList.remove(i);
        mAdapter.notifyItemRemoved(i);
    }
}
