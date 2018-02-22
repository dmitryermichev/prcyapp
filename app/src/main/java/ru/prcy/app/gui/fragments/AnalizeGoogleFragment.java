package ru.prcy.app.gui.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;
import java.util.Set;

import ru.prcy.app.R;
import ru.prcy.app.db.DomainData;
import ru.prcy.app.gui.views.AnalizeResultTableLayout;
import ru.prcy.app.gui.views.AnalizeResultTableSingleColumnLayout;
import ru.prcy.app.gui.views.PostProcessor;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AnalizeGoogleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AnalizeGoogleFragment extends AnalizeDetailsFragment {

    private static final String YAC_BASE_PATH = "https://yandex.ru/yaca";

    DomainData domainData;


    public AnalizeGoogleFragment() {
        // Required empty public constructor
    }

    public static AnalizeGoogleFragment newInstance() {
        AnalizeGoogleFragment fragment = new AnalizeGoogleFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_analize_google, container, false);

        if(getParentFragment() != null && getParentFragment() instanceof DomainDataProvider) {
            this.domainData = ((DomainDataProvider) getParentFragment()).getDomainData();
        }

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        JsonObject data = this.domainData.getData();

        if(data != null) {

            setIntValueFromAnalize(view, R.id.goIndex, data, "googleIndex");

            setBooleanValueFromAnalize(view, R.id.goSafeBrowsing, data, "googleSafeBrowsing", PostProcessor.BOOLEAN_POSITIVE_PROCESSOR);

        }


    }
}
