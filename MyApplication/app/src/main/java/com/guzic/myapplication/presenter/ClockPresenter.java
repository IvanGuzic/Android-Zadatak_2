package com.guzic.myapplication.presenter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.guzic.myapplication.R;
import com.guzic.myapplication.model.ClockModel;
import com.guzic.myapplication.model.SQLiteManager;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ClockPresenter extends AppCompatActivity {

    /**
     * https://www.youtube.com/watch?v=4k1ZMpO9Zn0&ab_channel=CodeWithCal
     */
    private static final int SELECT_PICTURE = 200;
    private EditText titleEditText;
    private EditText categoryEditText;
    private Button datePickerButton;
    private TextView datePickerText;
    private DatePickerDialog datePickerDialog;
    private Button deleteButton;
    private ClockModel selectedClock;
    private Button imagePickerButton;
    private ImageView IVPreviewImage;
    private Date dateSelected;
    private Bitmap bitmapSelected;

    /**
     * Validators
     */
    private boolean titleValid;
    private boolean categoryValid;
    private boolean dateValid;
    private boolean imageValid;

    /**
     * https://www.youtube.com/watch?v=4k1ZMpO9Zn0&ab_channel=CodeWithCal
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock);
        initWidgets();
        checkForEdit();
        dateSelected = new Date();

    }


    /**
     * https://www.youtube.com/watch?v=4k1ZMpO9Zn0&ab_channel=CodeWithCal
     */
    private void initWidgets() {

        titleEditText = findViewById(R.id.titleEditText);
        categoryEditText = findViewById(R.id.categoryEditText);
        deleteButton = findViewById(R.id.deleteClockButton);
        datePickerButton = findViewById(R.id.datePickerButton);
        datePickerText = findViewById(R.id.datePickerText);
        imagePickerButton = findViewById(R.id.imagePickerButton);
        IVPreviewImage = findViewById(R.id.imagePreviewView);

        /**
         * Validators
         */
        titleValid = false;
        categoryValid = false;
        dateValid = false;
        imageValid = false;

        /**
         * https://abhiandroid.com/ui/datepicker
         */
        datePickerButton.setOnClickListener(v -> {
            // calender class's instance and get current date , month and year from calender
            final Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR); // current year
            int mMonth = c.get(Calendar.MONTH); // current month
            int mDay = c.get(Calendar.DAY_OF_MONTH); // current day

            datePickerDialog = new DatePickerDialog(ClockPresenter.this, AlertDialog.THEME_DEVICE_DEFAULT_DARK, (view, year, monthOfYear, dayOfMonth) -> {
                // set day of month , month and year value in the edit text
                datePickerText.setText(dayOfMonth + "." + (monthOfYear + 1) + "." + year + ".");

                /**
                 * https://www.youtube.com/watch?v=4k1ZMpO9Zn0&ab_channel=CodeWithCal
                 */
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-dd-MM");
                String formatted_string = year + "-" + (dayOfMonth > 9 ? "" : "0") + dayOfMonth + "-" + ((monthOfYear + 1) > 9 ? "" : "0") + (monthOfYear + 1);

                try {
                    dateSelected = formatter.parse(formatted_string);
                    this.dateValid = true;
                }
                catch(ParseException e) {
                    e.printStackTrace();
                    this.dateValid = false;
                }

            }, mYear, mMonth, mDay);

            datePickerDialog.show();
        });

        /**
         * https://stackoverflow.com/questions/32431723/read-external-storage-permission-for-android
         */
        imagePickerButton.setOnClickListener(v -> ActivityCompat.requestPermissions(ClockPresenter.this,
                new String[] {
                    Manifest.permission.READ_EXTERNAL_STORAGE
        }, SELECT_PICTURE));

    }

    /**
     * https://gist.github.com/crazy-diya/a4576c271ae01370ceca630078315976
     * A method dealing with permissions on accessing device storage
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == SELECT_PICTURE) {

            if(grantResults.length > 0 &&  grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("image/*");
                startActivityForResult(intent, SELECT_PICTURE);
            }
            else {
                Toast.makeText(this, "You don't have a permission!", Toast.LENGTH_SHORT).show();
            }

            return;

        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    /**
     * https://gist.github.com/crazy-diya/a4576c271ae01370ceca630078315976
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == SELECT_PICTURE && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                this.bitmapSelected = BitmapFactory.decodeStream(inputStream);
                IVPreviewImage.setImageBitmap(this.bitmapSelected);

                this.imageValid = true;//**
            }
            catch(FileNotFoundException e) {
                e.printStackTrace();

                this.imageValid = false;//**
            }

        }

        super.onActivityResult(requestCode, resultCode, data);

    }

    /**
     * https://www.youtube.com/watch?v=4k1ZMpO9Zn0&ab_channel=CodeWithCal
     */
    private void checkForEdit() {

        Intent previousIntent = getIntent();
        int clockID = previousIntent.getIntExtra(ClockModel.CLOCK_EDIT, -1);
        selectedClock = ClockModel.getClock(clockID);

        if(selectedClock != null) {
            titleEditText.setText(selectedClock.getTitle());
            categoryEditText.setText(selectedClock.getCategory());

            /**
             * https://stackoverflow.com/questions/454315/how-to-format-date-and-time-in-android
             */
            android.text.format.DateFormat df = new android.text.format.DateFormat();
            datePickerText.setText(df.format( "dd.MM.yyyy", selectedClock.getCreatedAt()));

            IVPreviewImage.setImageBitmap(selectedClock.getBitmap());


            this.dateValid = true;
            this.dateSelected = selectedClock.getCreatedAt();

            this.imageValid = true;
            this.bitmapSelected = selectedClock.getBitmap();
        }
        else {
            deleteButton.setVisibility(View.INVISIBLE);
        }

    }

    /**
     * https://www.youtube.com/watch?v=4k1ZMpO9Zn0&ab_channel=CodeWithCal
     * @param view
     */
    public void saveClock(View view) {

        if(titleEditText.getText().length() > 0) {
            this.titleValid = true;
        }
        else {
            this.titleValid = false;
        }

        if(categoryEditText.getText().length() > 0) {
            this.categoryValid = true;
        }
        else {
            this.categoryValid = false;
        }

        if(this.titleValid && this.categoryValid && this.dateValid && this.imageValid) { // validate
            SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
            String title = String.valueOf(titleEditText.getText());
            String category = String.valueOf(categoryEditText.getText());

            if(selectedClock == null) {
                int id = ClockModel.clockArrayList.size();
                ClockModel newClock = new ClockModel(id, title, category, dateSelected, bitmapSelected);
                ClockModel.clockArrayList.add(newClock);
                sqLiteManager.addClockToDatabase(newClock);
            }
            else {
                selectedClock.setTitle(title);
                selectedClock.setCategory(category);
                selectedClock.setBitmap(bitmapSelected);
                selectedClock.setCreatedAt(dateSelected);
                sqLiteManager.updateClockInDB(selectedClock);
            }

            Toast.makeText(this, "You have successfully inserted a new clock", Toast.LENGTH_SHORT).show();
            finish();
        }
        else {
            ArrayList<String> wrong = new ArrayList<String>();

            if(!this.titleValid) {
                wrong.add("Title not valid");
            }

            if(!this.categoryValid) {
                wrong.add("Category not valid");
            }

            if(!this.dateValid) {
                wrong.add("Date not valid");
            }

            if(!this.imageValid) {
                wrong.add("Image not valid");
            }

            Toast.makeText(this, "Something is not right: " + String.join(", ", wrong), Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * https://www.youtube.com/watch?v=4k1ZMpO9Zn0&ab_channel=CodeWithCal
     * @param view
     */
    public void deleteClock(View view) {

        selectedClock.setDeleted(true);
        SQLiteManager sqLiteManager = SQLiteManager.instanceOfDatabase(this);
        sqLiteManager.updateClockInDB(selectedClock);
        finish();

    }

}