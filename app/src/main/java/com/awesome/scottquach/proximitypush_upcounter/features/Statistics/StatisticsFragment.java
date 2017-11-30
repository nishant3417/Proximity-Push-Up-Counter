package com.awesome.scottquach.proximitypush_upcounter.features.Statistics;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.awesome.scottquach.proximitypush_upcounter.R;


/**
 * Handles displaying statistical information about the users sessions
 */
public class StatisticsFragment extends Fragment implements StatisticsContract.View{

    private StatisticsFragmentInterface listener;
    private StatisticsPresenter presenter;

    public StatisticsFragment() {}

    public static StatisticsFragment newInstance() {
        StatisticsFragment fragment = new StatisticsFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        presenter = new StatisticsPresenter(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (presenter != null) {
            presenter.loadData();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return  inflater.inflate(R.layout.fragment_statistics, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof StatisticsFragmentInterface) {
            listener = (StatisticsFragmentInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement StatisticsFragmentInterface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.onViewDestroyed();
    }

    @Override
    public void setSessionHighScore(int highscore) {
        TextView view = (TextView) getView().findViewById(R.id.text_high_score);
        view.setText(String.valueOf(highscore));
    }

    @Override
    public void setDayHighScore(int highscore) {
        TextView view = (TextView) getView().findViewById(R.id.text_day_high_score);
        view.setText(String.valueOf(highscore));
    }

    @Override
    public void setTotalPushups(int totalPushups) {
        TextView view = (TextView) getView().findViewById(R.id.text_total_pushups);
        view.setText(String.valueOf(totalPushups));
    }

    @Override
    public void setTimesGoalReached(int times) {
        TextView view = (TextView) getView().findViewById(R.id.text_goal_reached);
        view.setText(String.valueOf(times));
    }

    @Override
    public void setTimesGoalFailed(int times) {
        TextView view = (TextView) getView().findViewById(R.id.text_goal_failed);
        view.setText(String.valueOf(times));
    }

    public interface StatisticsFragmentInterface {

    }
}