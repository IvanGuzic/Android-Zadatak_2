package com.guzic.myapplication.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import com.guzic.myapplication.model.ClockModel;
import com.guzic.myapplication.presenter.ClockPresenter;
import com.guzic.myapplication.R;
import com.guzic.myapplication.model.SQLiteManager;

/**
 * https://www.youtube.com/watch?v=4k1ZMpO9Zn0&ab_channel=CodeWithCal
 */
public class MainActivity extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWidgets();
        loadFromDBToMemory();
        setClockAdapter();
        setOnClickListener();

    }

    private void initWidgets() {

        listView = findViewById(R.id.listView);

    }

    private void loadFromDBToMemory() {

        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
        sqLiteManager.populateClockListArray();

    }

    private void setClockAdapter() {

        ClockAdapter clockAdapter = new ClockAdapter(getApplicationContext(), ClockModel.remainingClocks());
        listView.setAdapter(clockAdapter);

    }

    private void setOnClickListener() {

        listView.setOnItemClickListener((adapterView, view, position, l) -> {

            ClockModel selectedClock = (ClockModel)listView.getItemAtPosition(position);
            Intent intent = new Intent(getApplicationContext(), ClockPresenter.class);
            intent.putExtra(ClockModel.CLOCK_EDIT, selectedClock.getId());
            startActivity(intent);

        });

    }

    public void newClock(View view) {

        Intent newClockIntent = new Intent(this, ClockPresenter.class);
        startActivity(newClockIntent);

    }

    @Override
    protected void onResume() {

        super.onResume();
        setClockAdapter();

    }

}