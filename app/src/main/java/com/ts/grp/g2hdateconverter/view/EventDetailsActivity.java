package com.ts.grp.g2hdateconverter.view;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.ts.grp.g2hdateconverter.R;
import com.ts.grp.g2hdateconverter.ViewModel.ConvertViewModel;
import com.ts.grp.g2hdateconverter.ViewModel.EventDetailsViewModel;
import com.ts.grp.g2hdateconverter.ViewModel.EventDetailsViewModel.TransactionType;
import com.ts.grp.g2hdateconverter.ViewModel.EventsViewModel;
import com.ts.grp.g2hdateconverter.databinding.ActivityEventDetailsBinding;
import com.ts.grp.g2hdateconverter.databinding.ListItemEventBinding;
import com.ts.grp.g2hdateconverter.repository.apiclient.DataState;
import com.ts.grp.g2hdateconverter.repository.model.Event;

public class EventDetailsActivity extends AppCompatActivity implements Observer<DataState<TransactionType>>, TextWatcher {
    private EventDetailsViewModel mVM;
    public static final String EXTRA_EVENT_ID = "event" ;
    ActivityEventDetailsBinding mBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!getIntent().hasExtra(EXTRA_EVENT_ID))
            finish();
        mBinding= DataBindingUtil.setContentView(this, R.layout.activity_event_details);
        mVM=  new ViewModelProvider(this).get(EventDetailsViewModel.class);
        String id=getIntent().getStringExtra(EXTRA_EVENT_ID);
        mVM.getEventById(id).observe(this, event -> {
            mBinding.setEvent(event);
            mBinding.buttonSave.setVisibility(View.GONE);
        });
        mVM.getTransactionState().observe(this, this);

        mBinding.buttonDelete.setOnClickListener(v -> deleteEvent());
        mBinding.buttonSave.setOnClickListener(v -> {
            if(mBinding.textTitle.getText().toString().isEmpty())
                mBinding.textTitle.setError(getString(R.string.name_no_empty));
            else {
                mBinding.getEvent().name=mBinding.textTitle.getText().toString();
                mBinding.getEvent().description=mBinding.textDescription.getText().toString();
                mVM.updateEvent(mBinding.getEvent());
            }
        });
        mBinding.textTitle.addTextChangedListener(this);
        mBinding.textDescription.addTextChangedListener(this);

    }

    private void deleteEvent() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        mVM.deleteEvent();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:

                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(EventDetailsActivity.this);
        builder.setMessage(getString(R.string.are_u_sure)).setPositiveButton(R.string.yes, dialogClickListener)
                .setNegativeButton(R.string.no, dialogClickListener).show();
    }

    @Override
    public void onChanged(DataState<TransactionType> transactionTypeDataState) {
            if(transactionTypeDataState.getStatus().equals(DataState.Status.LOADING)) {
                mBinding.progressInside.setVisibility(View.VISIBLE);
                mBinding.buttonSave.setEnabled(false);
            }
            else if(transactionTypeDataState.getStatus().equals(DataState.Status.SUCCESS))
            {
                if(transactionTypeDataState.getData().equals(TransactionType.TRANSACTION_DELETE))
                    this.finish();
                else mBinding.progressInside.setVisibility(View.GONE);
                mBinding.buttonSave.setEnabled(true);
            }
            else if(transactionTypeDataState.getStatus().equals(DataState.Status.ERROR))
            {
                Toast.makeText(this,getString(R.string.transaction_failed),Toast.LENGTH_LONG).show();
                mBinding.progressInside.setVisibility(View.INVISIBLE);
                mBinding.buttonSave.setEnabled(true);
            }

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        mBinding.buttonSave.setVisibility(View.VISIBLE);
    }
}