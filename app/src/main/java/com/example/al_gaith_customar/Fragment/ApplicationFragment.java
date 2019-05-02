package com.example.al_gaith_customar.Fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.al_gaith_customar.Data.AppData;
import com.example.al_gaith_customar.Data.Application;
import com.example.al_gaith_customar.R;
import com.example.al_gaith_customar.Utility.GeneralUtility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ApplicationFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    ArrayList<Application> dataList = new ArrayList<>();
    MyApplicationRecyclerViewAdapter myApplicationRecyclerViewAdapter;

    RadioGroup sortRadioGroup;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ApplicationFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ApplicationFragment newInstance(int columnCount) {
        ApplicationFragment fragment = new ApplicationFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_application_list, container, false);
        View recycleView = view.findViewById(R.id.list);
        sortRadioGroup = view.findViewById(R.id.sort_app_rg);
        // Set the adapter
        if (recycleView instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) recycleView;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            myApplicationRecyclerViewAdapter = new MyApplicationRecyclerViewAdapter(dataList, mListener);
            recyclerView.setAdapter(myApplicationRecyclerViewAdapter);
        }

        loadMyApplication loadMyApplication = new loadMyApplication();
        loadMyApplication.execute();
        sortRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int index = group.indexOfChild(group.findViewById(checkedId));
                if (index == 0) {
                    Collections.sort(dataList, new Comparator<Application>() {
                        @Override
                        public int compare(Application o1, Application o2) {
                            return o1.app_name.compareTo(o2.app_name);
                        }
                    });

                    notifyDataChange();
                } else {
                    Collections.sort(dataList, new Comparator<Application>() {
                        @Override
                        public int compare(Application o1, Application o2) {
                            return o1.getDate().compareTo(o2.getDate());
                        }
                    });
                    notifyDataChange();
                }
            }
        });
        return view;
    }


    void notifyDataChange() {
        if (myApplicationRecyclerViewAdapter != null) {
            myApplicationRecyclerViewAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Application item);
    }

    class loadMyApplication extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {

            return GeneralUtility.getMyApplication(getContext(), AppData.authType + AppData.userToken);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            for (Application application : GeneralUtility.parseApplication(s)) {
                dataList.add(application);
            }
            notifyDataChange();
        }
    }
}
