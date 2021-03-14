package com.ts.grp.g2hdateconverter.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.ts.grp.g2hdateconverter.R;
import com.ts.grp.g2hdateconverter.ViewModel.CalendarViewModel;

import java.util.Date;
import java.util.List;

public class CalendarFragment extends Fragment implements Observer<List<com.ts.grp.g2hdateconverter.repository.model.Event>> {

    private CalendarViewModel mViewModel;
    private TextView mTextMonthYear;
    private CompactCalendarView mCalendarView;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(CalendarViewModel.class);
        View root = inflater.inflate(R.layout.fragment_calendar, container, false);
        mTextMonthYear=root.findViewById(R.id.text_month_year);
        mViewModel.getMonthTitle().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                mTextMonthYear.setText(s);
            }
        });
        mViewModel.setSelectedDate(new Date());
        mViewModel.getEvents().observe(getViewLifecycleOwner(),this);
        mCalendarView = root.findViewById(R.id.calendar_view);
        mCalendarView.shouldDrawIndicatorsBelowSelectedDays(false);
        mCalendarView.setEnabled(false);
        mCalendarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {

            //TODO show each event under the calendar

            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                mViewModel.setSelectedDate(firstDayOfNewMonth);
            }
        });
        return root;
    }

    @Override
    public void onChanged(List<com.ts.grp.g2hdateconverter.repository.model.Event> events) {
        mCalendarView.removeAllEvents();
        for(int i=0;i<events.size();i++){
            Event event=new Event(getResources().getColor(events.get(i).color),events.get(i).timestamp,events.get(i).name);
            mCalendarView.addEvent(event);
        }
    }
}