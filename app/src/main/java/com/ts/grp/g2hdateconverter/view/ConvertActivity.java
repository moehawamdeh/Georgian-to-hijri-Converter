package com.ts.grp.g2hdateconverter.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.ts.grp.g2hdateconverter.R;
import com.ts.grp.g2hdateconverter.ViewModel.ConvertViewModel;
import com.ts.grp.g2hdateconverter.repository.apiclient.DataState;
import com.ts.grp.g2hdateconverter.repository.apiclient.pojo.DualCalendarDate;

import java.util.Calendar;
import java.util.Date;

public class ConvertActivity extends AppCompatActivity implements Observer<DataState<DualCalendarDate>> {
    private ConvertViewModel mVM;
    SingleDateAndTimePicker mPicker;
    private Button mMakeEventButton;
    private Button mConvertDateButton;
    private TextView mHeader;
    private TextView mSubHeader;
    private ProgressBar mProgressBar;
    private DualCalendarDate mDualFormatDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);//Disabled Night mode, TODO: Add support for it
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        setContentView(R.layout.activity_convert);

        mVM=  new ViewModelProvider(this).get(ConvertViewModel.class);


        Button mEventsButton = findViewById(R.id.button_events);
        mConvertDateButton=findViewById(R.id.button_convert);
        mMakeEventButton=findViewById(R.id.button_appBar_action);
        mHeader=findViewById(R.id.text_app_bar_header);
        mSubHeader=findViewById(R.id.text_app_bar_sub_header);
        mEventsButton.setOnClickListener(v -> {
            Intent intent=new Intent(ConvertActivity.this, DisplayEventsActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        });
        mProgressBar=findViewById(R.id.progress_inside);
        //Date picker and its change listener

        mPicker = findViewById(R.id.date_picker);
        mPicker.addOnDateChangedListener((displayed, date) -> {

            if(!date.equals(mVM.getDate()))
            {
                mVM.setDate(date);
                //make event button gone if change was real
                mMakeEventButton.setVisibility(View.GONE);
                mHeader.setText(R.string.convert_hint);
                mSubHeader.setText(R.string.convert_to_add_hint);
            }

        });
        Calendar calendar=Calendar.getInstance();
        //if first run, show today else show VM date
        if(mVM.getDate()==null)
            mVM.setDate(new Date());

        calendar.setTime(mVM.getDate());
        mPicker.setDefaultDate(mVM.getDate());
        mMakeEventButton.setOnClickListener(v -> {
            final CreateEventSheetDialogFragment bottomCreateDialog =
                    CreateEventSheetDialogFragment.newInstance(mDualFormatDate, new CreateEventSheetDialogFragment.DialogCallback() {
                        @Override
                        public void onSuccess(CreateEventSheetDialogFragment fragment) {
                            fragment.dismiss();
                            //let user know that its done
                            Toast.makeText(ConvertActivity.this,getString(R.string.created_event),Toast.LENGTH_SHORT).show();
                        }
                    });
            bottomCreateDialog.show(getSupportFragmentManager(),
                    "add_photo_dialog_fragment");
        });

        mVM.getConvertedDate().observe(this, this);

        //Convert button
        mConvertDateButton.setOnClickListener(v -> {
            mVM.getConvertedDate();
        });



    }


    @Override
    public void onChanged(DataState<DualCalendarDate> stateData) {

        if(stateData.getStatus().equals(DataState.Status.LOADING)) {
            //show progress bar
            //show touch prevention
            mConvertDateButton.setEnabled(false);
            mProgressBar.setVisibility(View.VISIBLE);


        }

        else if(stateData.getStatus().equals(DataState.Status.SUCCESS)) {
            //TODO: Check language before selecting
            mDualFormatDate =stateData.getData();
            mHeader.setText(stateData.getData().getHijri().getDate());
            String builder = stateData.getData().getHijri().getWeekday().getEn()
                    + " - "
                    + stateData.getData().getHijri().getMonth().getEn();
            mSubHeader.setText(builder);
            mMakeEventButton.setVisibility(View.VISIBLE);
            mConvertDateButton.setEnabled(true);
            mProgressBar.setVisibility(View.INVISIBLE);

        }
        else if(stateData.getStatus().equals(DataState.Status.ERROR)) {
            mConvertDateButton.setEnabled(true);
            mProgressBar.setVisibility(View.INVISIBLE);
            if (stateData.getError().equals(DataState.Errors.API_NO_RESPONSE))
                Toast.makeText(this, getString(R.string.api_down), Toast.LENGTH_LONG).show();
            else if (stateData.getError().equals(DataState.Errors.NETWORK_ERROR))
                Toast.makeText(this, getString(R.string.internet_error), Toast.LENGTH_LONG).show();
        }

    }

}