package com.guzic.myapplication.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.guzic.myapplication.R;
import com.guzic.myapplication.model.ClockModel;
import java.util.List;

/**
 * https://www.youtube.com/watch?v=4k1ZMpO9Zn0&ab_channel=CodeWithCal
 */
public class ClockAdapter extends ArrayAdapter<ClockModel> {

    public ClockAdapter(Context context, List<ClockModel> clocks) {

        super(context, 0, clocks);

    }

    /**
     * https://www.youtube.com/watch?v=4k1ZMpO9Zn0&ab_channel=CodeWithCal
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ClockModel clock = getItem(position);

        if(convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.clock_items, parent, false);
            TextView title = convertView.findViewById(R.id.clockTitle);
            TextView category = convertView.findViewById(R.id.clockCategory);
            TextView date = convertView.findViewById(R.id.clockDate);
            ImageView image = convertView.findViewById(R.id.clockImage);
            title.setText(clock.getTitle());
            category.setText(clock.getCategory());

            /**
             * https://stackoverflow.com/questions/454315/how-to-format-date-and-time-in-android
             */
            android.text.format.DateFormat df = new android.text.format.DateFormat();
            // df.format("yyyy-MM-dd hh:mm:ss a", new java.util.Date());
            date.setText(df.format( "dd.MM.yyyy", clock.getCreatedAt()));

            image.setImageBitmap(clock.getBitmap());
        }

        return convertView;

    }

}