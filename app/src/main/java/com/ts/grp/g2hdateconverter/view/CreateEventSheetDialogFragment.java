package com.ts.grp.g2hdateconverter.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.ts.grp.g2hdateconverter.R;
import com.ts.grp.g2hdateconverter.ViewModel.CreateEventsViewModel;
import com.ts.grp.g2hdateconverter.repository.apiclient.DataState;
import com.ts.grp.g2hdateconverter.repository.apiclient.pojo.DualCalendarDate;

import java.util.Random;

public class CreateEventSheetDialogFragment extends BottomSheetDialogFragment implements View.OnClickListener, Observer<DataState<Void>> {
    private CreateEventsViewModel mViewModel;
    private DualCalendarDate mDate;
    private Button mButton;
    private EditText mName,mDescription;
    private DialogCallback mCallBack;
    public static CreateEventSheetDialogFragment newInstance(DualCalendarDate date, DialogCallback callback) {
        if(date==null )
            return null;
        if(callback==null)
            return null;

        CreateEventSheetDialogFragment fragment=new CreateEventSheetDialogFragment();
        fragment.mDate=date;
        fragment.mCallBack=callback;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel  = new ViewModelProvider(this).get(CreateEventsViewModel.class);

        View view = inflater.inflate(R.layout.bottom_sheet_add_event, container,
                false);
        // get the views and attach the listener
        mButton=view.findViewById(R.id.button_create);
        mButton.setOnClickListener(this);
        mName=view.findViewById(R.id.text_title);
        mDescription=view.findViewById(R.id.text_description);
        return view;
    }

    @Override
    public void onClick(View v) {
        if(mName.getText().toString().isEmpty())
             mName.setError(getString(R.string.name_no_empty));
        else
        mViewModel.createEvent(mDate,mName.getText().toString(),mDescription.getText().toString(),getRandomColor())
                .observe(this,this);
    }

    private int getRandomColor() {
        int[]colors=new int[]{R.color.colorListItemGreen,
                R.color.colorListItemRed,
                R.color.colorListItemBlue,
                R.color.colorListItemCyan,
                R.color.colorListItemOrange,
                R.color.colorListItemPurple};
        return colors[new Random().nextInt(colors.length)];
    }

    @Override
    public void onChanged(DataState<Void> state) {
      if(state.getStatus().equals(DataState.Status.LOADING)){
          getView().setEnabled(false);
      }else if(state.getStatus().equals(DataState.Status.SUCCESS)){
          mCallBack.onSuccess(this);
      }else if(state.getStatus().equals(DataState.Status.ERROR)){
          mButton.setEnabled(true);
      }

    }
    public  interface DialogCallback{
        void onSuccess(CreateEventSheetDialogFragment fragment);
    }
}