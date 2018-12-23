package com.ladsuematsu.capstoneproject.core.adapter;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.ladsuematsu.capstoneproject.R;

public class DayAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static int VIEWTYPE_TEXT_FIELDS = 1;
    private static int VIEWTYPE_CHECKABLE = 2;
    private static int VIEWTYPE_WEEKDAY = 3;

    private static final int ITEM_COUNT = 11;

    private static final int TEXT_EDIT_FIELDS = 0;

    private static final int HOME_DELIVERY_CHECKBOX = 1;
    private static final int ANIMAL_FRIENDLY_CHECKBOX = 2;
    private static final int DISABLED_PEOPLE_FACILITIES_CHECKBOX = 3;

    private static final int SUNDAY = 4;
    private static final int MONDAY = 5;
    private static final int THURSDAY = 6;
    private static final int WEDNESDAY = 7;
    private static final int TUESDAY = 8;
    private static final int FRIDAY = 9;
    private static final int SATURDAY = 10;


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

    @Override
    public int getItemViewType(int position) {

        int viewType;

        if (position < HOME_DELIVERY_CHECKBOX) {
            viewType = VIEWTYPE_TEXT_FIELDS;
        } else if (position < SUNDAY) {
            viewType = VIEWTYPE_CHECKABLE;
        } else {
            viewType = VIEWTYPE_WEEKDAY;
        }

        return viewType;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        if (viewType == VIEWTYPE_TEXT_FIELDS) {

            View view = inflater.inflate(R.layout.item_edit_text_fieds, viewGroup, false);
            viewHolder = new TextFieldHolder(view);

        } else if (viewType == VIEWTYPE_CHECKABLE) {

            View view = inflater.inflate(R.layout.item_edit_checkable, viewGroup, false);
            viewHolder = new CheckableHolder(view);

        } else {

            View view = inflater.inflate(R.layout.day_card, viewGroup, false);
            viewHolder = new DayHolder(view);

        }

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (i < ANIMAL_FRIENDLY_CHECKBOX) {

        } else if (i < SUNDAY) {

        } else {

            listener.bindHolder(i, ((DayHolder) viewHolder).holderObserver);

        }
    }

    @Override
    public int getItemCount() {
        return ITEM_COUNT;
    }

    class TextFieldHolder extends RecyclerView.ViewHolder {
        public TextFieldHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    class CheckableHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView label;
        private final CheckBox checkBox;

        public CheckableHolder(@NonNull View itemView) {
            super(itemView);
            label = itemView.findViewById(R.id.labelText);
            checkBox = itemView.findViewById(R.id.checkbox);

//            label.setOnClickListener(this);
//            checkBox.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            checkBox.setChecked(!checkBox.isChecked());

            listener.setOnCheckToggle(checkBox.isChecked());
        }
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
