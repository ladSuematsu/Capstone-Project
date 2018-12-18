package com.ladsuematsu.capstoneproject.core.adapter;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ladsuematsu.capstoneproject.R;

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.DayHolder> {
    private static final int ITEM_COUNT = 7;

    private static final int SUNDAY = 0;
    private static final int MONDAY = 1;
    private static final int THURSDAY = 2;
    private static final int WEDNESDAY = 3;
    private static final int TUESDAY = 4;
    private static final int FRIDAY = 5;
    private static final int SATURDAY = 6;

    private final String labelSunday;
    private final String labelMonday;
    private final String labelThursday;
    private final String labelWednesday;
    private final String labelTuesday;
    private final String labelFriday;
    private final String labelSaturday;

    private final LayoutInflater inflater;
    private final DayListenerObserver.HolderListener listener;

    public DayAdapter(LayoutInflater inflater, DayListenerObserver.HolderListener listener) {
        this.inflater = inflater;
        this.listener = listener;
        Resources resources = inflater.getContext().getResources();

        labelSunday = resources.getString(R.string.label_sunday);
        labelMonday = resources.getString(R.string.label_monday);
        labelThursday = resources.getString(R.string.label_thursday);
        labelWednesday = resources.getString(R.string.label_wednesday);
        labelTuesday = resources.getString(R.string.label_tuesday);
        labelFriday = resources.getString(R.string.label_friday);
        labelSaturday = resources.getString(R.string.label_saturday);
    }


    @NonNull
    @Override
    public DayHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = inflater.inflate(R.layout.day_card, viewGroup, false);

        return new DayHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull DayHolder dayViewholder, int i) {
        listener.bindHolder(i, dayViewholder.holderObserver);
    }

    @Override
    public int getItemCount() {
        return ITEM_COUNT;
    }

    class DayHolder extends RecyclerView.ViewHolder {

        private final TextView weekDay;
        private final TextView openClosed;
        private final Button edit;

        public DayHolder(@NonNull View itemView) {
            super(itemView);

            weekDay = itemView.findViewById(R.id.week_day);
            openClosed = itemView.findViewById(R.id.open_closed);
            edit = itemView.findViewById(R.id.edit);
            edit.setOnClickListener(onButtonClickListener);
        }

        private final View.OnClickListener onButtonClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (listener == null) { return; }

                listener.onWeekEdit(getAdapterPosition());
            }
        };

        private final DayListenerObserver.HolderObserver holderObserver = new DayListenerObserver.HolderObserver() {

            @Override
            public void fillWeekDay(String weekDay) {
                String weekDayLabel = weekDay;
                if (weekDay != null) {
                    weekDayLabel = weekDay;
                } else {
                    switch(getAdapterPosition()) {
                        case SUNDAY:
                            weekDayLabel = labelSunday;
                            break;

                        case MONDAY:
                            weekDayLabel = labelMonday;
                            break;

                        case THURSDAY:
                            weekDayLabel = labelThursday;
                            break;

                        case WEDNESDAY:
                            weekDayLabel = labelWednesday;
                            break;

                        case TUESDAY:
                            weekDayLabel = labelTuesday;
                            break;

                        case FRIDAY:
                            weekDayLabel = labelFriday;
                            break;

                        case SATURDAY:
                            weekDayLabel = labelSaturday;
                            break;

                        default:
                            weekDayLabel = "";

                    }
                }

                DayHolder.this.weekDay.setText(weekDayLabel);
            }

            @Override
            public void fillHours(String hourStart, String hourEnd) {
                DayHolder.this.openClosed.setText(hourStart + hourEnd);
            }
        };

    }
}
