package ru.prcy.app.gui.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;

import ru.prcy.app.R;
import ru.prcy.app.data.AnalizeField;
import ru.prcy.app.data.AnalizeFieldBoolean;
import ru.prcy.app.data.AnalizeFieldObject;
import ru.prcy.app.data.Common;
import ru.prcy.app.db.DomainData;
import ru.prcy.app.gui.views.AnalizeResultTableLayout;
import ru.prcy.app.gui.views.AnalizeResultTableRowLayout;
import ru.prcy.app.gui.views.AnalizeResultTableSingleColumnLayout;
import ru.prcy.app.gui.views.PostProcessor;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AnalizeOptimizationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AnalizeOptimizationFragment extends AnalizeDetailsFragment {

    private static final String YAC_BASE_PATH = "https://yandex.ru/yaca";

    DomainData domainData;

    public AnalizeOptimizationFragment() {
        // Required empty public constructor
    }

    public static AnalizeOptimizationFragment newInstance() {
        AnalizeOptimizationFragment fragment = new AnalizeOptimizationFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_analize_optimization, container, false);

        if(getParentFragment() != null && getParentFragment() instanceof DomainDataProvider) {
            this.domainData = ((DomainDataProvider) getParentFragment()).getDomainData();
        }

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        JsonObject data = this.domainData.getData();

        if(data != null) {

            setBooleanValueFromAnalize(view, R.id.optimizationAvoidInterstitials, data, "pageSpeedAvoidInterstitials", new PostProcessor() {
                @Override
                public void setIcon(ImageView iconImageView, AnalizeField data) {
                    AnalizeFieldBoolean booleanData =(AnalizeFieldBoolean) data;
                    if(booleanData.value)
                        iconImageView.setImageResource(R.drawable.positive);
                    else
                        iconImageView.setImageResource(R.drawable.negative);
                }
            });
            setBooleanValueFromAnalize(view, R.id.optimizationAvoidLandingPageRedirects, data, "pageSpeedAvoidLandingPageRedirects", new PostProcessor() {
                @Override
                public void setIcon(ImageView iconImageView, AnalizeField data) {
                    AnalizeFieldBoolean booleanData =(AnalizeFieldBoolean) data;
                    if(booleanData.value)
                        iconImageView.setImageResource(R.drawable.positive);
                    else
                        iconImageView.setImageResource(R.drawable.negative);
                }
            });

            AnalizeResultTableRowLayout pluginsRow = (AnalizeResultTableRowLayout) view.findViewById(R.id.optimizationAvoidPlugins);
            if(data.has("pageSpeedAvoidPlugins")) {
                JsonObject images = data.getAsJsonObject("pageSpeedAvoidPlugins");

                if(images.get("pageSpeedAvoidPlugins").isJsonObject()) {
                    JsonObject avoidPlugins = images.getAsJsonObject("pageSpeedAvoidPlugins");
                    pluginsRow.setIcon(R.drawable.negative);
                } else {
                    boolean isEnabled = images.get("pageSpeedAvoidPlugins").getAsBoolean();
                    if(isEnabled) {
                        pluginsRow.setValue(getString(R.string.yes));
                        pluginsRow.setIcon(R.drawable.positive);
                    } else {
                        pluginsRow.setValue(getString(R.string.no));
                        pluginsRow.setIcon(R.drawable.negative);
                    }
                }
            }

//            setObjectValueFromAnalize(view, R.id.optimizationAvoidPlugins, data, "pageSpeedAvoidPlugins", new PostProcessor() {
//                @Override
//                public void setIcon(ImageView iconImageView, AnalizeField data) {
//                    AnalizeFieldObject objectData = (AnalizeFieldObject) data;
//                    if(objectData.object.isJsonPrimitive()) {
//                        boolean value = objectData.object.getAsBoolean();
//                        if(value)
//                            iconImageView.setImageResource(R.drawable.positive);
//                        else
//                            iconImageView.setImageResource(R.drawable.negative);
//                    } else {
//                        iconImageView.setImageResource(R.drawable.negative);
//                    }
//                }
//
//                @Override
//                public void setValue(TextView valueTextView, AnalizeField data) {
//                    AnalizeFieldObject objectData = (AnalizeFieldObject) data;
//                    if(objectData.object.isJsonPrimitive()) {
//                        boolean value = objectData.object.getAsBoolean();
//                        if(value)
//                            valueTextView.setText(R.string.yes);
//                        else
//                            valueTextView.setText(R.string.no);
//                    } else {
//                        valueTextView.setText(R.string.no);
//                    }
//                }
//            });

//            setBooleanValueFromAnalize(view, R.id.optimizationAvoidPlugins, data, "pageSpeedAvoidPlugins", new PostProcessor() {
//                @Override
//                public void setIcon(ImageView iconImageView, AnalizeField data) {
//                    AnalizeFieldBoolean booleanData =(AnalizeFieldBoolean) data;
//                    if(booleanData.value)
//                        iconImageView.setImageResource(R.drawable.positive);
//                    else
//                        iconImageView.setImageResource(R.drawable.negative);
//                }
//            });
            setBooleanValueFromAnalize(view, R.id.optimizationConfigureViewport, data, "pageSpeedConfigureViewport", new PostProcessor() {
                @Override
                public void setIcon(ImageView iconImageView, AnalizeField data) {
                    AnalizeFieldBoolean booleanData =(AnalizeFieldBoolean) data;
                    if(booleanData.value)
                        iconImageView.setImageResource(R.drawable.positive);
                    else
                        iconImageView.setImageResource(R.drawable.negative);
                }
            });
            setBooleanValueFromAnalize(view, R.id.optimizationAvoidLeverageBrowserCaching, data, "pageSpeedLeverageBrowserCaching", new PostProcessor() {
                @Override
                public void setIcon(ImageView iconImageView, AnalizeField data) {
                    AnalizeFieldBoolean booleanData =(AnalizeFieldBoolean) data;
                    if(booleanData.value)
                        iconImageView.setImageResource(R.drawable.positive);
                    else
                        iconImageView.setImageResource(R.drawable.negative);
                }
            });

            setBooleanValueFromAnalize(view, R.id.optimizationPrioritizeVisibleContent, data, "pageSpeedPrioritizeVisibleContent", new PostProcessor() {
                @Override
                public void setIcon(ImageView iconImageView, AnalizeField data) {
                    AnalizeFieldBoolean booleanData =(AnalizeFieldBoolean) data;
                    if(booleanData.value)
                        iconImageView.setImageResource(R.drawable.positive);
                    else
                        iconImageView.setImageResource(R.drawable.negative);
                }
            });
            setBooleanValueFromAnalize(view, R.id.optimizationServerResponseTime, data, "pageSpeedServerResponseTime", new PostProcessor() {
                @Override
                public void setIcon(ImageView iconImageView, AnalizeField data) {
                    AnalizeFieldBoolean booleanData =(AnalizeFieldBoolean) data;
                    if(booleanData.value)
                        iconImageView.setImageResource(R.drawable.positive);
                    else
                        iconImageView.setImageResource(R.drawable.negative);
                }
            });
            setBooleanValueFromAnalize(view, R.id.optimizationSizeContentToViewport, data, "pageSpeedSizeContentToViewport", new PostProcessor() {
                @Override
                public void setIcon(ImageView iconImageView, AnalizeField data) {
                    AnalizeFieldBoolean booleanData =(AnalizeFieldBoolean) data;
                    if(booleanData.value)
                        iconImageView.setImageResource(R.drawable.positive);
                    else
                        iconImageView.setImageResource(R.drawable.negative);
                }
            });
            setBooleanValueFromAnalize(view, R.id.optimizationSizeTapTargetsAppropriately, data, "pageSpeedSizeTapTargetsAppropriately", new PostProcessor() {

                @Override
                public void setIcon(ImageView iconImageView, AnalizeField data) {
                    AnalizeFieldBoolean booleanData =(AnalizeFieldBoolean) data;
                    if(booleanData.value)
                        iconImageView.setImageResource(R.drawable.positive);
                    else
                        iconImageView.setImageResource(R.drawable.negative);
                }
            });
            setBooleanValueFromAnalize(view, R.id.optimizationUseLegibleFontSizes, data, "pageSpeedUseLegibleFontSizes", new PostProcessor() {
                @Override
                public void setIcon(ImageView iconImageView, AnalizeField data) {
                    AnalizeFieldBoolean booleanData =(AnalizeFieldBoolean) data;
                    if(booleanData.value)
                        iconImageView.setImageResource(R.drawable.positive);
                    else
                        iconImageView.setImageResource(R.drawable.negative);
                }
            });

            AnalizeResultTableSingleColumnLayout gzipRow = (AnalizeResultTableSingleColumnLayout) view.findViewById(R.id.optimizationEnableGzipCompression);
            if(data.has("pageSpeedEnableGzipCompression")) {
                JsonObject gzip = data.getAsJsonObject("pageSpeedEnableGzipCompression");

                if(gzip.get("pageSpeedEnableGzipCompression").isJsonObject()) {
                    JsonObject gzipEnabled = gzip.getAsJsonObject("pageSpeedEnableGzipCompression");
                    int bytes = gzipEnabled.get("bytes").getAsInt();
                    int percents = gzipEnabled.get("percentage").getAsInt();
                    String result = getString(R.string.analizer_results_optimization_gzip_not_enabled, Common.bytesToString(bytes), percents + "%");
                    gzipRow.setValue(result);
                    gzipRow.setIcon(R.drawable.negative);
                } else {
                    boolean isEnabled = gzip.get("pageSpeedEnableGzipCompression").getAsBoolean();
                    if(isEnabled) {
                        gzipRow.setValue(getString(R.string.enabled));
                        gzipRow.setIcon(R.drawable.positive);
                    } else {
                        gzipRow.setValue(getString(R.string.disabled));
                        gzipRow.setIcon(R.drawable.negative);
                    }
                }
            }

            AnalizeResultTableLayout blockingTable = (AnalizeResultTableLayout) view.findViewById(R.id.blockingTable);
            AnalizeResultTableSingleColumnLayout blockingRow = (AnalizeResultTableSingleColumnLayout) blockingTable.findViewById(R.id.optimizationMinimizeRenderBlockingResources);

            if(data.has("pageSpeedMinimizeRenderBlockingResources")) {
                JsonObject blocking = data.getAsJsonObject("pageSpeedMinimizeRenderBlockingResources");

                if(blocking.get("pageSpeedMinimizeRenderBlockingResources").isJsonObject()) {
                    JsonObject blockingObject = blocking.getAsJsonObject("pageSpeedMinimizeRenderBlockingResources");
                    int numJs = 0;
                    if(blockingObject.has("num_js"))
                        numJs = blockingObject.get("num_js").getAsInt();
                    int numCss = 0;
                    if(blockingObject.has("num_css"))
                        numCss = blockingObject.get("num_css").getAsInt();
                    blockingRow.setValue(getString(R.string.analize_results_optimization_decrease_resources));
                    blockingRow.setIcon(R.drawable.negative);

                    if(numCss > 0) {
                        View cssChild = getActivity().getLayoutInflater().inflate(R.layout.analize_result_row_layout_instance, null);
                        AnalizeResultTableRowLayout css = (AnalizeResultTableRowLayout) cssChild.findViewById(R.id.row);
                        css.setTitle(getString(R.string.css_files));
                        css.setValue("" + numCss);
                        blockingTable.addView(cssChild);
                    }

                    if(numJs > 0) {
                        View jsChild = getActivity().getLayoutInflater().inflate(R.layout.analize_result_row_layout_instance, null);
                        AnalizeResultTableRowLayout js = (AnalizeResultTableRowLayout) jsChild.findViewById(R.id.row);
                        js.setTitle(getString(R.string.js_files));
                        js.setValue("" + numJs);
                        blockingTable.addView(jsChild);
                    }

                } else {
                    boolean isBlocking = blocking.get("pageSpeedMinimizeRenderBlockingResources").getAsBoolean();
                    if(isBlocking) {
                        blockingRow.setValue(getString(R.string.found));
                        blockingRow.setIcon(R.drawable.negative);
                    } else {
                        blockingRow.setValue(getString(R.string.not_found));
                        blockingRow.setIcon(R.drawable.positive);
                    }
                }
            }

            AnalizeResultTableSingleColumnLayout imagesRow = (AnalizeResultTableSingleColumnLayout) view.findViewById(R.id.optimizationOptimizeImages);
            if(data.has("pageSpeedOptimizeImages")) {
                JsonObject images = data.getAsJsonObject("pageSpeedOptimizeImages");

                if(images.get("pageSpeedOptimizeImages").isJsonObject()) {
                    JsonObject imagesOptimized = images.getAsJsonObject("pageSpeedOptimizeImages");
                    int bytes = imagesOptimized.get("bytes").getAsInt();
                    int percents = imagesOptimized.get("percentage").getAsInt();
                    String result = getString(R.string.analizer_results_optimization_images_not_enabled, Common.bytesToString(bytes), percents + "%");
                    imagesRow.setValue(result);
                    imagesRow.setIcon(R.drawable.negative);
                } else {
                    boolean isEnabled = images.get("pageSpeedOptimizeImages").getAsBoolean();
                    if(isEnabled) {
                        imagesRow.setValue(getString(R.string.enabled));
                        imagesRow.setIcon(R.drawable.positive);
                    } else {
                        imagesRow.setValue(getString(R.string.disabled));
                        imagesRow.setIcon(R.drawable.negative);
                    }
                }
            }
        }


    }
}
