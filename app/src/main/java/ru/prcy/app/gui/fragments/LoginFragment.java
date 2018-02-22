package ru.prcy.app.gui.fragments;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ru.prcy.app.R;
import ru.prcy.app.data.ApiKey;
import ru.prcy.app.gui.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoginFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    public static final String PARAM_CLOSE_ON_SAVE = "PARAM_CLOSE_ON_SAVE";

    private boolean closeOnSave;
    private ApiKey currentKey;
    private OnFragmentInteractionListener mListener;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(boolean closeOnSave) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putBoolean(PARAM_CLOSE_ON_SAVE, closeOnSave);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.closeOnSave = getArguments().getBoolean(PARAM_CLOSE_ON_SAVE);
        this.currentKey = ApiKey.load(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(getActivity() != null)
            ((MainActivity) getActivity()).getSupportActionBar().setTitle(R.string.login_fragment_title);

        final View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        TextView promptTextView = (TextView) rootView.findViewById(R.id.keyPromptTextView);
        promptTextView.setMovementMethod(LinkMovementMethod.getInstance());

        final EditText edit = (EditText) rootView.findViewById(R.id.keyEditText);

        final Button saveButton = (Button) rootView.findViewById(R.id.saveButton);
        final Button clearButton = (Button) rootView.findViewById(R.id.clearButton);

        saveButton.setEnabled(false);
        clearButton.setEnabled(false);

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit.setText("");
                currentKey.clear();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newKey = edit.getText().toString();
                if(ApiKey.isStringAKey(newKey)) {
                    ApiKey key = new ApiKey(getContext(), newKey);
                    key.save();
                    mListener.onLogin();
                    Log.d(LoginFragment.class.getName(), "closing fragment = " + closeOnSave);
                    if(closeOnSave) {
                        getActivity().getSupportFragmentManager().popBackStack();
                    }
                } else {
                    Snackbar.make(rootView.findViewById(R.id.coordinatorLayout), getString(R.string.login_fragment_key_save_bad_format), Snackbar.LENGTH_LONG).show();
                }
            }
        });

        edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //Активируем кнопку Сохранить если введеное значение подходит по формату и текущий сохраненный ключ с ним не совпадает.
                if(ApiKey.isStringAKey(s.toString()) && !s.toString().equals(currentKey.getKey())) {
                    saveButton.setEnabled(true);
                } else {
                    saveButton.setEnabled(false);
                }

                if(s.length() > 0)
                    clearButton.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (currentKey.isEmpty())
            tryToPasteFromClipboard();
        else {
            EditText edit = (EditText) getView().findViewById(R.id.keyEditText);
            edit.setText(currentKey.getKey());
        }

    }

    private void tryToPasteFromClipboard() {
        EditText edit = (EditText) getView().findViewById(R.id.keyEditText);
        ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);

        if(clipboard.hasPrimaryClip() && clipboard.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
            ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
            String key = item.getText().toString();

            //TODO: Перед вставкой проверяем только длину ключа, возможно можно проверять как-то поумнее
            if(ApiKey.isStringAKey(key))
                edit.setText(key);
        }

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
        void onLogin();
    }
}
