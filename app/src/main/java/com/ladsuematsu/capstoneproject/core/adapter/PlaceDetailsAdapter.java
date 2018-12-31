package com.ladsuematsu.capstoneproject.core.adapter;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ladsuematsu.capstoneproject.R;

import java.util.Locale;

public class PlaceDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static int VIEWTYPE_HEADER= 1;
    private static int VIEWTYPE_TEXT_FIELDS = 2;
    private static int VIEWTYPE_CHECKABLE = 3;
    private static int VIEWTYPE_WEEKDAY = 4;

    private final String labelAmenityDisabledPeople;
    private final String labelAmenityAnimalFriendly;
    private final String labelAmenityDoorDelivery;
    private final String labelSunday;
    private final String labelMonday;
    private final String labelThursday;
    private final String labelWednesday;
    private final String labelTuesday;
    private final String labelFriday;
    private final String labelSaturday;

    private final LayoutInflater inflater;
    private final DayListenerObserver.HolderListener listener;
    private final String openCloseHourFormat;
    private final Drawable checkedDrawable;
    private final Drawable notCheckedDrawable;
    private final String labelHeaderCheckfields;
    private final String labelHeaderWeekdayTimes;

    public PlaceDetailsAdapter(LayoutInflater inflater, DayListenerObserver.HolderListener listener) {
        this.inflater = inflater;
        this.listener = listener;
        Resources resources = inflater.getContext().getResources();

        labelHeaderCheckfields = resources.getString(R.string.place_detail_header_check_fields);
        labelHeaderWeekdayTimes = resources.getString(R.string.place_detail_header_weekday_times_fields);

        labelAmenityAnimalFriendly = resources.getString(R.string.amenity_animal_friendly);
        labelAmenityDisabledPeople = resources.getString(R.string.amenity_disabled_people);
        labelAmenityDoorDelivery = resources.getString(R.string.amenity_door_delivery);

        labelSunday = resources.getString(R.string.label_sunday);
        labelMonday = resources.getString(R.string.label_monday);
        labelThursday = resources.getString(R.string.label_thursday);
        labelWednesday = resources.getString(R.string.label_wednesday);
        labelTuesday = resources.getString(R.string.label_tuesday);
        labelFriday = resources.getString(R.string.label_friday);
        labelSaturday = resources.getString(R.string.label_saturday);
        
        openCloseHourFormat = resources.getString(R.string.open_close_hour_weekday_format);

        checkedDrawable = ResourcesCompat.getDrawable(resources, R.drawable.ic_check_circle_black_24dp, null);
        notCheckedDrawable = ResourcesCompat.getDrawable(resources, R.drawable.ic_do_not_disturb_on_black_24dp, null);
    }

    @Override
    public int getItemViewType(int position) {

        int viewType;
        switch(position) {
            case DayListenerObserver.TEXT_EDIT_FIELDS:
                viewType = VIEWTYPE_TEXT_FIELDS;
                break;

            case DayListenerObserver.HOME_DELIVERY_CHECKBOX:
            case DayListenerObserver.ANIMAL_FRIENDLY_CHECKBOX:
            case DayListenerObserver.DISABLED_PEOPLE_FACILITIES_CHECKBOX:
                viewType = VIEWTYPE_CHECKABLE;
                break;

            case DayListenerObserver.SUNDAY:
            case DayListenerObserver.MONDAY:
            case DayListenerObserver.THURSDAY:
            case DayListenerObserver.WEDNESDAY:
            case DayListenerObserver.TUESDAY:
            case DayListenerObserver.FRIDAY:
            case DayListenerObserver.SATURDAY:
                viewType = VIEWTYPE_WEEKDAY;
                break;

            case DayListenerObserver.HEADER_CHECK_ITEMS:
            case DayListenerObserver.HEADER_WEEKDAY_TIMES:
            default:
                viewType = VIEWTYPE_HEADER;
                break;
        }

        return viewType;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        if (viewType == VIEWTYPE_HEADER) {

            View view = inflater.inflate(R.layout.item_header, viewGroup, false);
            viewHolder = new HeaderHolder(view);

        } else if (viewType == VIEWTYPE_TEXT_FIELDS) {

            View view = inflater.inflate(R.layout.item_place_read_only_text_fieds, viewGroup, false);
            viewHolder = new TextFieldHolder(view);

        } else if (viewType == VIEWTYPE_CHECKABLE) {

            View view = inflater.inflate(R.layout.item_place_read_only_checkable, viewGroup, false);
            viewHolder = new CheckableHolder(view);

        } else {

            View view = inflater.inflate(R.layout.day_card, viewGroup, false);
            viewHolder = new DayHolder(view);

        }

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        if (viewHolder instanceof HeaderHolder) {

            HeaderHolder holder = (HeaderHolder) viewHolder;

            String headerText;
            switch (i) {

                case DayListenerObserver.HEADER_CHECK_ITEMS:

                    headerText = labelHeaderCheckfields;
                    break;

                case DayListenerObserver.HEADER_WEEKDAY_TIMES:
                    headerText = labelHeaderWeekdayTimes;
                    break;

                default:
                    headerText = "";
            }


            holder.headerText.setText(headerText);

        } else if (viewHolder instanceof TextFieldHolder) {

            TextFieldHolder textFieldHolder = (TextFieldHolder) viewHolder;

            listener.bindHolder(i, textFieldHolder.observer);

        } else if (viewHolder instanceof CheckableHolder) {

            CheckableHolder checkableHolder = (CheckableHolder) viewHolder;

            String label;
            switch (i) {

                case DayListenerObserver.HOME_DELIVERY_CHECKBOX:
                    label = labelAmenityDoorDelivery;
                    break;

                case DayListenerObserver.ANIMAL_FRIENDLY_CHECKBOX:
                    label = labelAmenityAnimalFriendly;
                    break;

                case DayListenerObserver.DISABLED_PEOPLE_FACILITIES_CHECKBOX:
                    label = labelAmenityDisabledPeople;
                    break;

                default:
                    label = "";

            }

            checkableHolder.observer.setLabel(label);
            listener.bindHolder(i, checkableHolder.observer);

        } else if (viewHolder instanceof  DayHolder) {

            DayHolder dayHolder = (DayHolder) viewHolder;
            listener.bindHolder(i, dayHolder.holderObserver);

        }

    }

    @Override
    public int getItemCount() {
        return DayListenerObserver.ITEM_COUNT;
    }

    class HeaderHolder extends RecyclerView.ViewHolder {

        private final TextView headerText;

        public HeaderHolder(View itemView) {
            super(itemView);
            headerText = itemView.findViewById(R.id.item_header);
        }
    }


    class TextFieldHolder extends RecyclerView.ViewHolder {

        private final TextView placeName;
        private final TextView placeAddress;
        private final TextView placePhoneNumber;

        TextFieldHolder(@NonNull View itemView) {
            super(itemView);

            placeName = itemView.findViewById(R.id.name);
            placeAddress = itemView.findViewById(R.id.address);
            placePhoneNumber = itemView.findViewById(R.id.telephone);

        }

        final DayListenerObserver.TextfieldObserver observer = new DayListenerObserver.TextfieldObserver() {

            @Override
            public void setPlaceName(String name) {
                placeName.setText(name);
            }

            @Override
            public void setAddress(String address) {
                placeAddress.setText(address);
            }

            @Override
            public void setPhoneNumber(String phoneNumber) {
                placePhoneNumber.setText(phoneNumber);
            }

        };
    }

    class CheckableHolder extends RecyclerView.ViewHolder {
        private final TextView label;
        private final ImageView checkBox;
        private int code;

        CheckableHolder(@NonNull View itemView) {
            super(itemView);
            label = itemView.findViewById(R.id.labelText);
            checkBox = itemView.findViewById(R.id.checkbox);

        }

        final DayListenerObserver.CheckableObserver observer = new DayListenerObserver.CheckableObserver() {

            @Override
            public void setLabel(String labelText) {
                label.setText(labelText);
            }

            @Override
            public void setCheckable(int code, boolean checked) {
                CheckableHolder.this.code = code;

                checkBox.setImageDrawable(checked ? checkedDrawable : notCheckedDrawable);
            }
        };
    }

    class DayHolder extends RecyclerView.ViewHolder {

        private int weekCode;
        private final TextView weekDay;
        private final TextView openClosed;
        private final Button edit;

        public DayHolder(@NonNull View itemView) {
            super(itemView);

            weekDay = itemView.findViewById(R.id.week_day);
            openClosed = itemView.findViewById(R.id.open_closed);
            edit = itemView.findViewById(R.id.edit);
            edit.setVisibility(View.GONE);
        }

        private final View.OnClickListener onButtonClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (listener == null) { return; }

                listener.onWeekEdit(getAdapterPosition(), weekCode);
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
                        case DayListenerObserver.SUNDAY:
                            weekDayLabel = labelSunday;
                            break;

                        case DayListenerObserver.MONDAY:
                            weekDayLabel = labelMonday;
                            break;

                        case DayListenerObserver.THURSDAY:
                            weekDayLabel = labelThursday;
                            break;

                        case DayListenerObserver.WEDNESDAY:
                            weekDayLabel = labelWednesday;
                            break;

                        case DayListenerObserver.TUESDAY:
                            weekDayLabel = labelTuesday;
                            break;

                        case DayListenerObserver.FRIDAY:
                            weekDayLabel = labelFriday;
                            break;

                        case DayListenerObserver.SATURDAY:
                            weekDayLabel = labelSaturday;
                            break;

                        default:
                            weekDayLabel = "";

                    }
                }

                DayHolder.this.weekDay.setText(weekDayLabel);
            }

            @Override
            public void fillHours(int weekCode, String hourStart, String hourEnd) {
                DayHolder.this.weekCode = weekCode;


                String displayText = TextUtils.isEmpty(hourStart) || TextUtils.isEmpty(hourEnd)
                                        ? ""
                                        : String.format(Locale.ROOT, openCloseHourFormat, hourStart, hourEnd);
                
                DayHolder.this.openClosed.setText(displayText);
            }
        };

    }

}
