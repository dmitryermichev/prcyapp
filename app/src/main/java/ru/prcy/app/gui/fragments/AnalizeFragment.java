package ru.prcy.app.gui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import ru.prcy.app.R;
import ru.prcy.app.data.ApiKey;
import ru.prcy.app.db.DomainData;
import ru.prcy.app.db.DomainDataHelper;
import ru.prcy.app.gui.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AnalizeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AnalizeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AnalizeFragment extends Fragment {


    private OnFragmentInteractionListener mListener;

    public AnalizeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AnalizeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AnalizeFragment newInstance() {
        AnalizeFragment fragment = new AnalizeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(getActivity() != null)
            ((MainActivity) getActivity()).getSupportActionBar().setTitle("");
        View rootView = inflater.inflate(R.layout.fragment_analize, container, false);

        DomainDataHelper helper = new DomainDataHelper(getContext());
        DomainData data = helper.getLastFromHistory();

        final EditText domainEditText = (EditText) rootView.findViewById(R.id.domainEditText);

        if(data != null)
            domainEditText.setText(data.getDomain());

        final Button analizeButton = (Button) rootView.findViewById(R.id.analizeButton);

        analizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String domain = domainEditText.getText().toString();

                if(isDomain(domain)) {
                    Log.d(this.getClass().getName(), "it's correct domain");
                    domainEditText.setError(null);

                    mListener.onAnalizeRequested(false, domain);

                } else {
                    domainEditText.setError(getString(R.string.analize_fragment_error_bad_domain));
                    Log.d(this.getClass().getName(), "it's not a domain");
                }

                InputMethodManager imm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(analizeButton.getWindowToken(), 0);


            }
        });

        return rootView;
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
    public void onResume() {
        super.onResume();

        ApiKey apiKey = ApiKey.load(getContext());

        if(apiKey.isEmpty())
            mListener.onApiKeyIsEmpty();

        ((MainActivity)getActivity()).getSupportActionBar().show();
    }

    private boolean isDomain(String domain) {
        return domain.matches("^([a-z0-9]+(-[a-z0-9]+)*\\.)+[a-z]{2,}$");
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
        // TODO: Update argument type and name
        void onAnalizeRequested(boolean offline, String site);
        void onApiKeyIsEmpty();
    }
}
