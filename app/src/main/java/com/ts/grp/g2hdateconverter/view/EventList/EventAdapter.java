package com.ts.grp.g2hdateconverter.view.EventList;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ts.grp.g2hdateconverter.R;
import com.ts.grp.g2hdateconverter.databinding.ListItemEventBinding;
import com.ts.grp.g2hdateconverter.repository.model.Event;
import com.ts.grp.g2hdateconverter.view.EventDetailsActivity;

import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventHolder>{

    private final Event[]events;
    private final ArrayList<Event>selectedEvents;
    private boolean multiSelectModeOn=false;
    private static  MultiSelectItemsListener selectListener = null;
    public EventAdapter(@NonNull Event[] events,@NonNull MultiSelectItemsListener listener) {
        this.events = events;
        selectListener=listener;
        selectedEvents=new ArrayList<>();
    }

    @NonNull
    @Override
    public EventAdapter.EventHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater =
                LayoutInflater.from(parent.getContext());
        ListItemEventBinding itemBinding =
                ListItemEventBinding.inflate(layoutInflater, parent, false);
        return new EventAdapter.EventHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull EventAdapter.EventHolder holder, int position) {
        Event event=events[position];
        boolean isFirstOnDay=false;
        if(position==0 || !events[position-1].gregorianDate.getDate().equals(event.gregorianDate.getDate()))
            isFirstOnDay=true;

        holder.setEvent(event,isFirstOnDay);
    }

    @Override
    public int getItemCount() {
        return events.length;
    }

    public class EventHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {
        private final ListItemEventBinding mBinding;
        private boolean selected=false;
        public EventHolder(@NonNull ListItemEventBinding binding) {
            super(binding.getRoot());
            mBinding=binding;

        }
        public void setEvent(Event event, boolean isFirstOfDay){
            selected=false;
            mBinding.setEvent(event);
            //in case arabic take hijri
            mBinding.setDate(event.gregorianDate);
            mBinding.setIsFirstEventOfDay(isFirstOfDay);
            mBinding.buttonItem.setBackgroundTintList(ContextCompat.getColorStateList(itemView.getContext(),event.color));

            mBinding.buttonItem.setOnClickListener(this);
            mBinding.buttonItem.setOnLongClickListener(this);
            mBinding.executePendingBindings();
        }


        @Override
        public void onClick(View v) {
            if(multiSelectModeOn ) {
                if (selected)
                    deselectItem();
                else selectItem();
            }
            else {
            Intent intent=new Intent(v.getContext(), EventDetailsActivity.class);
            intent.putExtra(EventDetailsActivity.EXTRA_EVENT_ID,String.valueOf(mBinding.getEvent().id));
            v.getContext().startActivity(intent);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if(!multiSelectModeOn && !selected){
                //start multi select mode
                multiSelectModeOn=true;
                selectItem();
                return true;
            }
            return false;
        }

    private void selectItem(){
        selected=true;
        mBinding.getRoot().setBackgroundColor(mBinding.getRoot().getContext().getColor(R.color.colorSelectDelete));
        selectedEvents.add(mBinding.getEvent());
        selectListener.onMultiSelectEvent(selectedEvents);

    }
    private void deselectItem(){
        selected=false;
        mBinding.getRoot().setBackgroundColor(0);
        selectedEvents.removeIf(obj -> obj.id == mBinding.getEvent().id);
        selectListener.onMultiSelectEvent(selectedEvents);
        if(selectedEvents.isEmpty()) {
            multiSelectModeOn=false;
            selectListener.onMultiSelectDismissed();
        }

    }

    }
    public static interface MultiSelectItemsListener{
        public void onMultiSelectEvent(List<Event>selected);
        public void onMultiSelectDismissed();
    }
}
