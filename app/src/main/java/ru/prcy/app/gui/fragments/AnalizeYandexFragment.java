package ru.prcy.app.gui.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;
import java.util.Set;

import ru.prcy.app.R;
import ru.prcy.app.data.AnalizeField;
import ru.prcy.app.data.AnalizeFieldBoolean;
import ru.prcy.app.db.DomainData;
import ru.prcy.app.gui.views.AnalizeResultTableSingleColumnLayout;
import ru.prcy.app.gui.views.AnalizeResultTableLayout;
import ru.prcy.app.gui.views.PostProcessor;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AnalizeYandexFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AnalizeYandexFragment extends AnalizeDetailsFragment {

    private static final String YAC_BASE_PATH = "https://yandex.ru/yaca";

    DomainData domainData;


    public AnalizeYandexFragment() {
        // Required empty public constructor
    }

    public static AnalizeYandexFragment newInstance() {
        AnalizeYandexFragment fragment = new AnalizeYandexFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_analize_yandex, container, false);

        if(getParentFragment() != null && getParentFragment() instanceof DomainDataProvider) {
            this.domainData = ((DomainDataProvider) getParentFragment()).getDomainData();
        }

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        JsonObject data = this.domainData.getData();

        if(data != null) {

            setIntValueFromAnalize(view, R.id.yaCitation, data, "yandexCitation");
            setIntValueFromAnalize(view, R.id.yaIndex, data, "yandexIndex");
            setIntValueFromAnalize(view, R.id.yaRank, data, "yandexRank");
            setBooleanValueFromAnalize(view, R.id.yaAgs, data, "yandexAgs", PostProcessor.BOOLEAN_INVERSE_PROCESSOR);
            setBooleanValueFromAnalize(view, R.id.yaGlue, data, "yandexGlue",  PostProcessor.BOOLEAN_INVERSE_PROCESSOR);
            setBooleanValueFromAnalize(view, R.id.yaSafeBrowsing, data, "yandexSafeBrowsing",  PostProcessor.BOOLEAN_POSITIVE_PROCESSOR);

            if(data.has("yandexCatalog")) {
                JsonObject catalog = data.getAsJsonObject("yandexCatalog");
                boolean found = catalog.get("yandexCatalog").getAsBoolean();
                AnalizeResultTableSingleColumnLayout header = (AnalizeResultTableSingleColumnLayout) view.findViewById(R.id.yacHeader);
                AnalizeResultTableLayout yacLayout = (AnalizeResultTableLayout) view.findViewById(R.id.yacTable);
                if(found) {

                    Set<Map.Entry<String, JsonElement>> categories = catalog.getAsJsonObject("yandexCategories").entrySet();
                    header.setValue(getString(R.string.analize_results_yandex_yac_header_found, categories.size()));

                    for(Map.Entry<String, JsonElement> entry: categories) {
                        Log.d(this.getClass().getName(), "category link " + entry.getKey() + " name " + entry.getValue().getAsString());
                        View child = getActivity().getLayoutInflater().inflate(R.layout.analize_result_single_column_row_layout, null);
                        AnalizeResultTableSingleColumnLayout category = (AnalizeResultTableSingleColumnLayout) child.findViewById(R.id.row);
                        category.setValue("<a href='" + entry.getKey() + "'>" + entry.getValue().getAsString() + "</a>");
                        yacLayout.addView(child);
                    }

                } else {
                    header.setValue(getString(R.string.analize_results_yandex_yac_header_not_found));
                }
            }

        }


    }
}
