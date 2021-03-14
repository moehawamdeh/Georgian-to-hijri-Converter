package com.ts.grp.g2hdateconverter.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ts.grp.g2hdateconverter.R;
import com.ts.grp.g2hdateconverter.ViewModel.EventsViewModel;
import com.ts.grp.g2hdateconverter.repository.apiclient.DataState;
import com.ts.grp.g2hdateconverter.repository.model.Event;
import com.ts.grp.g2hdateconverter.view.EventList.EventAdapter;

import java.util.List;

import at.markushi.ui.CircleButton;

public class EventListFragment extends Fragment implements EventAdapter.MultiSelectItemsListener,Observer<List<Event>> {
    private RecyclerView mRecyclerView;
    private EventsViewModel mEventsVM;
    private CircleButton mDeleteButton;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mEventsVM = new ViewModelProvider(this).get(EventsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_event_list, container, false);
        mRecyclerView=root.findViewById(R.id.recycler_events);
        LinearLayoutManager manager=new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false);
        mRecyclerView.setLayoutManager(manager);
        mDeleteButton=root.findViewById(R.id.button_delete);
        mEventsVM.getEvents().observe(getViewLifecycleOwner(), this);
        mDeleteButton.setOnClickListener(v -> deleteSelectedEvents());
        return root;
    }

    private void deleteSelectedEvents(){
            DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        mEventsVM.deleteSelected().observe(getViewLifecycleOwner(), new Observer<DataState<Void>>() {
                            @Override
                            public void onChanged(DataState<Void> voidDataState) {
                                if(voidDataState.getStatus().equals(DataState.Status.SUCCESS)){
                                    Toast.makeText(getContext(), getResources().getString(R.string.deleted),Toast.LENGTH_SHORT).show();
                                    mDeleteButton.setVisibility(View.INVISIBLE);
                                }else if(voidDataState.getStatus().equals(DataState.Status.ERROR))
                                    Toast.makeText(getContext(),getResources().getString(R.string.delete_failed),Toast.LENGTH_SHORT).show();
                                //in case loading state nothing needed
                            }
                        });
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:

                        break;
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(getString(R.string.are_u_sure)).setPositiveButton(R.string.yes, dialogClickListener)
                    .setNegativeButton(R.string.no, dialogClickListener).show();


    }
    @Override
    public void onMultiSelectEvent(List<Event> selected) {
        mEventsVM.setSelected(selected);
        mDeleteButton.setVisibility(View.VISIBLE); }

    @Override
    public void onMultiSelectDismissed()
    {
        mEventsVM.removeSelected();
        mDeleteButton.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onChanged(List<Event> events) {

        Event[] eventArr=new Event[events.size()];
        events.toArray(eventArr);
        EventAdapter adapter=new EventAdapter(eventArr, EventListFragment.this);
        mRecyclerView.setAdapter(adapter);
    }
}