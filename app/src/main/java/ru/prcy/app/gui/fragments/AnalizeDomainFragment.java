package ru.prcy.app.gui.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;

import ru.prcy.app.R;
import ru.prcy.app.data.AnalizeFieldString;
import ru.prcy.app.db.DomainData;
import ru.prcy.app.gui.views.AnalizeResultTableLayout;
import ru.prcy.app.gui.views.AnalizeResultTableRowLayout;
import ru.prcy.app.gui.views.AnalizeResultTableSingleColumnLayout;
import ru.prcy.app.gui.views.PostProcessor;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AnalizeDomainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AnalizeDomainFragment extends AnalizeDetailsFragment {

    private static final String YAC_BASE_PATH = "https://yandex.ru/yaca";

    DomainData domainData;


    public AnalizeDomainFragment() {
        // Required empty public constructor
    }

    public static AnalizeDomainFragment newInstance() {
        AnalizeDomainFragment fragment = new AnalizeDomainFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_analize_domain, container, false);

        if(getParentFragment() != null && getParentFragment() instanceof DomainDataProvider) {
            this.domainData = ((DomainDataProvider) getParentFragment()).getDomainData();
        }

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        JsonObject data = this.domainData.getData();

        if(data != null) {

            setStringValueFromAnalize(view, R.id.domainIp, data, "ip");
            setStringValueFromAnalize(view, R.id.domainIpIsp, data, "ipIsp", "ipIspName");
            setDateValueFromAnalize(view, R.id.domainWhoisCreationDate, data, "whoisCreationDate");
            setDateValueFromAnalize(view, R.id.domainWhoisExpirationDate, data, "whoisExpirationDate");
            setBooleanValueFromAnalize(view, R.id.domainRoskomnadzor, data, "roskomnadzor", "roskomnadzorDomainForbidden", PostProcessor.BOOLEAN_INVERSE_PROCESSOR);
            setBooleanValueFromAnalize(view, R.id.domainSsl, data, "ssl", "sslAccess");
            setBooleanValueFromAnalize(view, R.id.domainRobots, data, "robotsTxt", "robotsFileExists");

            if(data.has("mainPageTechs") &&
                    data.getAsJsonObject("mainPageTechs").has("browserTechs") &&
                    data.getAsJsonObject("mainPageTechs").getAsJsonObject("browserTechs").has("web-servers")

              ) {
                JsonObject webServersObject = data.getAsJsonObject("mainPageTechs").
                        getAsJsonObject("browserTechs").getAsJsonObject("web-servers");
                String webServersString = "";
                for(Map.Entry<String, JsonElement> entry : webServersObject.entrySet()) {
                    webServersString += entry.getValue().getAsString() + ", ";
                }
                webServersString = webServersString.substring(0, webServersString.length() - 2);

                ((AnalizeResultTableRowLayout) view.findViewById(R.id.domainWebServer)).setData(new AnalizeFieldString(webServersString));
            }

            if(data.has("publicStatistics")) {
                String source = data.getAsJsonObject("publicStatistics").get("publicStatisticsSourceType").getAsString();
                ((AnalizeResultTableSingleColumnLayout) view.findViewById(R.id.publicStatisticsSource))
                        .setValue(getString(R.string.analize_results_domain_public_statistics_source, source));
            }

            setIntValueFromAnalize(view, R.id.visitsPerDay, data, "publicStatistics", "publicStatisticsVisitsDaily");
            setIntValueFromAnalize(view, R.id.visitsPerWeek, data, "publicStatistics", "publicStatisticsVisitsWeekly");
            setIntValueFromAnalize(view, R.id.visitsPerMonth, data, "publicStatistics", "publicStatisticsVisitsMonthly");
            setIntValueFromAnalize(view, R.id.pagesPerDay, data, "publicStatistics", "publicStatisticsPageViewsDaily");
            setIntValueFromAnalize(view, R.id.pagesPerWeek, data, "publicStatistics", "publicStatisticsPageViewsWeekly");
            setIntValueFromAnalize(view, R.id.pagesPerMonth, data, "publicStatistics", "publicStatisticsPageViewsMonthly");

            if(data.has("statisticsSystems") &&
                    data.getAsJsonObject("statisticsSystems").has("statisticsSystems")
                    ) {
                AnalizeResultTableLayout systemsTable = (AnalizeResultTableLayout) view.findViewById(R.id.statisticsSystems);
                JsonObject systemsObject = data.getAsJsonObject("statisticsSystems").
                        getAsJsonObject("statisticsSystems");
                for(Map.Entry<String, JsonElement> entry : systemsObject.entrySet()) {
                    View child = getActivity().getLayoutInflater().inflate(R.layout.analize_result_single_column_row_layout, null);
                    AnalizeResultTableSingleColumnLayout system = (AnalizeResultTableSingleColumnLayout) child.findViewById(R.id.row);
                    system.setValue(entry.getValue().getAsString());
                    systemsTable.addView(child);
                }
            }

        }


    }
}
