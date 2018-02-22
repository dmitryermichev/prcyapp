package ru.prcy.app.gui.fragments;

import android.content.Context;
import android.nfc.cardemulation.OffHostApduService;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.Date;

import ru.prcy.app.R;
import ru.prcy.app.api.AnalizeResult;
import ru.prcy.app.api.ApiAsyncTask;
import ru.prcy.app.api.BasicAnalizeAsyncTask;
import ru.prcy.app.api.exceptions.AnalizeCommonException;
import ru.prcy.app.db.DomainData;
import ru.prcy.app.db.DomainDataHelper;
import ru.prcy.app.gui.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AnalizeInProgressFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AnalizeInProgressFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AnalizeInProgressFragment extends Fragment {
    private static final String ARG_DOMAIN = "domain";
    private static final String ARG_OFFLINE = "offline";
    private static final String ARG_FORCE_UPDATE = "forceUpdate";

    private String domain;

    private OnFragmentInteractionListener mListener;
    private  boolean offline;
    private boolean forceUpdate;

    BasicAnalizeAsyncTask analizeTask;

    public AnalizeInProgressFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param domain
     * @return A new instance of fragment AnalizeInProgressFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AnalizeInProgressFragment newInstance(String domain, boolean offline, boolean forceUpdate) {
        AnalizeInProgressFragment fragment = new AnalizeInProgressFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DOMAIN, domain);
        args.putBoolean(ARG_OFFLINE, offline);
        args.putBoolean(ARG_FORCE_UPDATE, forceUpdate);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            domain = getArguments().getString(ARG_DOMAIN);
            offline = getArguments().getBoolean(ARG_OFFLINE);
            forceUpdate = getArguments().getBoolean(ARG_FORCE_UPDATE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(getActivity() != null)
            ((MainActivity) getActivity()).getSupportActionBar().setTitle("");

        View rootView = inflater.inflate(R.layout.fragment_analize_in_progress, container, false);

        analize(offline, rootView);

        return rootView;
    }

    private void displayTryAgain(final View rootView, final boolean offlineFlag, Exception e) {
        View progressContainer = rootView.findViewById(R.id.progressContainer);
        View tryAgainContainer = rootView.findViewById(R.id.tryAgainContainer);

        final Button analizeButton = (Button) rootView.findViewById(R.id.analizeButton);
        TextView offline = (TextView) rootView.findViewById(R.id.offlineMessage);


        Log.d(this.getClass().getName(), "displayTryAgain offline = " + offlineFlag);

        if(offlineFlag) {
            offline.setVisibility(View.GONE);
            analizeButton.setText(R.string.analize_in_progress_fragment_try_online);

            ((TextView) rootView.findViewById(R.id.error)).setText(getString(R.string.analize_in_progress_fragment_error_prefix, e.getLocalizedMessage()));
            analizeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    analize(false, rootView);
                }
            });

        } else {
            analizeButton.setText(R.string.analize_in_progress_fragment_try_again);

            ((TextView) rootView.findViewById(R.id.error)).setText(getString(R.string.analize_in_progress_fragment_error_prefix, e.getLocalizedMessage()));

            DomainDataHelper helper = new DomainDataHelper(getContext());

            long domainId = helper.getDomainIdFromHistory(domain);

            if(domainId > 0) {
                offline.setVisibility(View.VISIBLE);
                offline.setText(getString(R.string.analize_in_progress_fragment_in_cache_message, domain));

                offline.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        analize(offlineFlag, rootView);
                    }
                });
            } else {
                offline.setVisibility(View.GONE);
            }

            analizeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    analize(offlineFlag, rootView);
                }
            });
        }

        progressContainer.setVisibility(View.GONE);
        tryAgainContainer.setVisibility(View.VISIBLE);
    }

    private void displayProgress(View rootView) {
        View progressContainer = rootView.findViewById(R.id.progressContainer);
        View tryAgainContainer = rootView.findViewById(R.id.tryAgainContainer);

        progressContainer.setVisibility(View.VISIBLE);
        tryAgainContainer.setVisibility(View.GONE);
    }

    private void analize(final boolean offline, final View rootView) {

        final DomainDataHelper helper = new DomainDataHelper(getContext());

        displayProgress(rootView);

        if(offline) {
            long lastDomainId = helper.getDomainIdFromHistory(domain);
            if(lastDomainId > 0) {
                mListener.onAnalizeFinished(lastDomainId);
                return;
            } else {
                displayTryAgain(rootView, offline, new AnalizeCommonException(getString(R.string.analize_in_progress_error_getting_domain_from_bd, domain)));
                return;
            }

        }

        analizeTask = new BasicAnalizeAsyncTask(getContext(), forceUpdate, new ApiAsyncTask.ResultListener() {
            @Override
            public void onResult(AnalizeResult result) {

                if(analizeTask.isCancelled())
                    return;

                if(result.isSucceed()) {
                    long domainId = analizeTask.saveDomainData(domain, result, new DomainDataHelper(getContext()));
                    mListener.onAnalizeFinished(domainId);
                } else {
                    displayTryAgain(rootView, offline, result.getException());
                }

            }
        });

        analizeTask.execute(domain);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).getSupportActionBar().hide();
    }
    @Override
    public void onStop() {
        super.onStop();
        if(analizeTask != null && !analizeTask.isCancelled())
            analizeTask.cancel(true);
        ((MainActivity)getActivity()).getSupportActionBar().show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {

        void onAnalizeFinished(long domainId);
    }
}
