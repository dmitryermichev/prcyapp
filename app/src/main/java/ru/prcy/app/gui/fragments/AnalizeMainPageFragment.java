package ru.prcy.app.gui.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;
import java.util.Set;

import ru.prcy.app.R;
import ru.prcy.app.data.AnalizeField;
import ru.prcy.app.data.AnalizeFieldInt;
import ru.prcy.app.data.AnalizeFieldString;
import ru.prcy.app.data.Common;
import ru.prcy.app.db.DomainData;
import ru.prcy.app.gui.views.AnalizeResultTableLayout;
import ru.prcy.app.gui.views.AnalizeResultTableRowLayout;
import ru.prcy.app.gui.views.AnalizeResultTableSingleColumnLayout;
import ru.prcy.app.gui.views.AnalizeResultTableSocialLayout;
import ru.prcy.app.gui.views.PostProcessor;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AnalizeMainPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AnalizeMainPageFragment extends AnalizeDetailsFragment {

    DomainData domainData;


    public AnalizeMainPageFragment() {
        // Required empty public constructor
    }

    public static AnalizeMainPageFragment newInstance() {
        AnalizeMainPageFragment fragment = new AnalizeMainPageFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_analize_mainpage, container, false);

        if(getParentFragment() != null && getParentFragment() instanceof DomainDataProvider) {
            this.domainData = ((DomainDataProvider) getParentFragment()).getDomainData();
        }

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        JsonObject data = this.domainData.getData();

        if(data != null) {

            setIntValueFromAnalize(view, R.id.pageSize, data, "mainPagePageSize", "pageSize", new PostProcessor() {
                @Override
                public void setValue(TextView valueTextView, AnalizeField data) {
                    AnalizeFieldInt intData = (AnalizeFieldInt) data;
                    valueTextView.setText(Common.kBytesToString(intData.getValue()));
                }
            });

            setStringValueFromAnalize(view, R.id.pageEncoding, data, "mainPageEncoding", "encoding");
            setStringValueFromAnalize(view, R.id.pageSickness, data, "mainPageSickness", "sickness");
            setStringValueFromAnalize(view, R.id.pageTextLength, data, "mainPageTextLength", "textLength", new PostProcessor() {
                @Override
                public void setValue(TextView valueTextView, AnalizeField data) {
                    AnalizeFieldString stringData = (AnalizeFieldString) data;
                    valueTextView.setText(getString(R.string.analize_results_main_page_text_length_value, stringData.value));
                }
            });

            setIntValueFromAnalize(view, R.id.pageWordsCount, data, "mainPageWordsCount", "wordsCount");
            setBooleanValueFromAnalize(view, R.id.pageWwwRedirect, data, "wwwRedirect");

            if(data.has("mainPageTitle")) {
                boolean isTitleGood = data.getAsJsonObject("mainPageTitle").get("titleIsGood").getAsBoolean();
                String title = data.getAsJsonObject("mainPageTitle").get("title").getAsString();
                ((AnalizeResultTableSingleColumnLayout) view.findViewById(R.id.pageTitle)).setValue(title);
                int titleLength = data.getAsJsonObject("mainPageTitle").get("titleLength").getAsInt();
                AnalizeResultTableRowLayout rowLayout = (AnalizeResultTableRowLayout) view.findViewById(R.id.pageTitleLength);
                rowLayout.setValue("" + titleLength);
                if(isTitleGood)
                    rowLayout.setIcon(R.drawable.positive);
                else
                    rowLayout.setIcon(R.drawable.negative);
            }

            if(data.has("mainPageDescription")) {
                boolean isDescriptionGood = data.getAsJsonObject("mainPageDescription").get("descriptionIsGood").getAsBoolean();
                String desciption = data.getAsJsonObject("mainPageDescription").get("description").getAsString();
                AnalizeResultTableSingleColumnLayout descrLayout = (AnalizeResultTableSingleColumnLayout) view.findViewById(R.id.pageDescription);
                AnalizeResultTableRowLayout descrLengthLayout = (AnalizeResultTableRowLayout) view.findViewById(R.id.pageDescriptionLength);
                if(desciption.length() > 0) {
                    descrLayout.setValue(desciption);
                    int descriptionLength = data.getAsJsonObject("mainPageDescription").get("descriptionLength").getAsInt();

                    descrLengthLayout.setValue("" + descriptionLength);
                    if(isDescriptionGood)
                        descrLengthLayout.setIcon(R.drawable.positive);
                    else
                        descrLengthLayout.setIcon(R.drawable.negative);
                } else {
                    descrLayout.setValue(getString(R.string.analize_results_main_page_description_empty));
                    descrLengthLayout.setVisibility(View.GONE);
                }
            }

            if(data.has("loadTime")) {
                float time = data.getAsJsonObject("loadTime").get("loadTime").getAsFloat();
                int percent = data.getAsJsonObject("loadTime").get("percent").getAsInt();
                ((AnalizeResultTableSingleColumnLayout) view.findViewById(R.id.pageSpeed)).setValue(
                        getString(R.string.analize_results_main_page_html_speed, "" + Common.formatFloat(time), percent + "%"));

            }


            setIntValueFromAnalize(view, R.id.internalLinks, data, "mainPageInternalLinks", "internalCount");
            setIntValueFromAnalize(view, R.id.externalLinks, data, "mainPageExternalLinks", "externalCount");

            if(data.has("mainPageHeaders")) {
                JsonObject headers = data.getAsJsonObject("mainPageHeaders").getAsJsonObject("headersCount");
                ((AnalizeResultTableRowLayout) view.findViewById(R.id.pageH1)).setValue(headers.get("h1").getAsString());
                ((AnalizeResultTableRowLayout) view.findViewById(R.id.pageH2)).setValue(headers.get("h2").getAsString());
                ((AnalizeResultTableRowLayout) view.findViewById(R.id.pageH3)).setValue(headers.get("h3").getAsString());
                ((AnalizeResultTableRowLayout) view.findViewById(R.id.pageH4)).setValue(headers.get("h4").getAsString());
                ((AnalizeResultTableRowLayout) view.findViewById(R.id.pageH5)).setValue(headers.get("h5").getAsString());
                ((AnalizeResultTableRowLayout) view.findViewById(R.id.pageH6)).setValue(headers.get("h6").getAsString());
            }


            if(data.has("mainPageTechs") &&
                    data.getAsJsonObject("mainPageTechs").has("browserTechs")
                    ) {
                AnalizeResultTableLayout frameworksTable = (AnalizeResultTableLayout) view.findViewById(R.id.javascript_frameworks);
                JsonObject frameworksObject = data.getAsJsonObject("mainPageTechs").
                        getAsJsonObject("browserTechs").getAsJsonObject("javascript-frameworks");
                if(frameworksObject != null && frameworksObject.entrySet().size() > 0) {
                    for(Map.Entry<String, JsonElement> entry : frameworksObject.entrySet()) {
                        View child = getActivity().getLayoutInflater().inflate(R.layout.analize_result_single_column_row_layout, null);
                        AnalizeResultTableSingleColumnLayout framework = (AnalizeResultTableSingleColumnLayout) child.findViewById(R.id.row);
                        framework.setValue(entry.getValue().getAsString());
                        frameworksTable.addView(child);
                    }
                } else {
                    View child = getActivity().getLayoutInflater().inflate(R.layout.analize_result_single_column_row_layout, null);
                    ((AnalizeResultTableSingleColumnLayout) child.findViewById(R.id.row)).setValue(getString(R.string.not_found));
                    frameworksTable.addView(child);
                }

            }

            AnalizeResultTableSocialLayout openGraphLayout = (AnalizeResultTableSocialLayout) view.findViewById(R.id.openGraph);
            if(data.has("microdataOpenGraph") && data.getAsJsonObject("microdataOpenGraph").get("ogFound").getAsBoolean()) {

                String ogTitle = data.getAsJsonObject("microdataOpenGraph").get("ogTitle").getAsString();
                String ogDescription = data.getAsJsonObject("microdataOpenGraph").get("ogDescription").getAsString();
                String ogImage = data.getAsJsonObject("microdataOpenGraph").get("ogImage").getAsString();


                openGraphLayout.setData(ogTitle, null, ogImage, ogDescription);
            } else {
                openGraphLayout.setVisibility(View.GONE);
            }


            setBooleanValueFromAnalize(view, R.id.htmlValidatorStatus, data, "htmlValidator", "validity", PostProcessor.BOOLEAN_POSITIVE_PROCESSOR);
            setIntValueFromAnalize(view, R.id.htmlValidatorErrors, data, "htmlValidator", "errors");
            setIntValueFromAnalize(view, R.id.htmlValidatorWarnings, data, "htmlValidator", "warnings");

        }


    }
}
