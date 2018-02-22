package ru.prcy.app.gui.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.text.SimpleDateFormat;
import java.util.Date;

import ru.prcy.app.R;
import ru.prcy.app.data.AnalizeFieldInt;
import ru.prcy.app.data.AnalizeFieldString;
import ru.prcy.app.data.Common;
import ru.prcy.app.db.DomainData;
import ru.prcy.app.db.DomainDataHelper;
import ru.prcy.app.db.FavouriteDomainData;
import ru.prcy.app.gui.MainActivity;
import ru.prcy.app.gui.views.AnalizeResultFieldLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class AnalizeResultsFragment extends Fragment implements DomainDataProvider {

    private static final String ARG_DOMAIN_ID = "domainId";

    public static final String SCREENSHOT_BASE_PATH = "https://s3-eu-west-1.amazonaws.com/s3.pr-cy.ru/";
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private DomainData domainData;
    private OnFragmentInteractionListener mListener;
    private DomainDataHelper helper;

    public AnalizeResultsFragment() {
        // Required empty public constructor
    }

    public static AnalizeResultsFragment newInstance(long domainId) {
        AnalizeResultsFragment fragment = new AnalizeResultsFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_DOMAIN_ID, domainId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            long domainId = getArguments().getLong(ARG_DOMAIN_ID);
            helper = new DomainDataHelper(getContext());
            this.domainData = helper.getDomainFromHistory(domainId);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).getSupportActionBar().hide();
    }
    @Override
    public void onStop() {
        super.onStop();
        ((MainActivity)getActivity()).getSupportActionBar().show();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_analize_results, container, false);
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitle(domainData.getDomain());

        toolbar.findViewById(R.id.update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onAnalizeUpdateRequested(domainData.getDomain());
            }
        });

        final ImageView favButton = (ImageView) toolbar.findViewById(R.id.add_to_fav);
        invalidateAddToFavButton(favButton);

//        toolbar.inflateMenu(R.menu.menu_analize_results);

//        ((MainActivity)getActivity()).setSupportActionBar(toolbar);

//        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                int id = item.getItemId();
//                switch (id) {
//                    case R.id.action_refresh:
//
//                        break;
//                }
//                return true;
//            }
//        });
       // ((MainActivity) getActivity()).setSupportActionBar(toolbar);


        ((TextView) rootView.findViewById(R.id.analizeTime)).setText(Common.formatDate(domainData.getAnalized()));
        ((TextView) rootView.findViewById(R.id.updateTime)).setText(getString(R.string.analize_results_update_time, Common.formatDate(domainData.getUpdated())));


        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tabLayout);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        mViewPager = (ViewPager) rootView.findViewById(R.id.container);
        mViewPager.setAdapter(new AnalizeResultsFragment.TabsAdapter(getChildFragmentManager()));
        tabLayout.setupWithViewPager(mViewPager);

        if(domainData.getData().has("favicon")) {
            ImageView favicon = (ImageView) rootView.findViewById(R.id.favicon);
            Glide.with(this).load(domainData.getData().getAsJsonObject("favicon").get("faviconSrc").getAsString())
                    .apply(new RequestOptions().placeholder(R.drawable.ic_favicon))
                    .into(favicon);
        }

        if(domainData.getData().has("screenshotDesktop")) {
            ImageView screenshot = (ImageView) rootView.findViewById(R.id.screenshot);
            Glide.with(this).load(SCREENSHOT_BASE_PATH + domainData.getData().getAsJsonObject("screenshotDesktop").get("filePath").getAsString())
                    .into(screenshot);
        }

        //Получаем ТИЦ из результатов API
        if(domainData.getData().has("yandexCitation")) {
            int tic = domainData.getData().getAsJsonObject("yandexCitation").get("yandexCitation").getAsInt();
            AnalizeResultFieldLayout ticLayout = (AnalizeResultFieldLayout) rootView.findViewById(R.id.ticLayout);
            ticLayout.setData(new AnalizeFieldInt(getString(R.string.field_title_tic), tic));
        }

        if(domainData.getData().has("yandexCatalog")) {
            boolean catalog = domainData.getData().getAsJsonObject("yandexCatalog").get("yandexCatalog").getAsBoolean();
            AnalizeResultFieldLayout yakLayout = (AnalizeResultFieldLayout) rootView.findViewById(R.id.yakLayout);
            yakLayout.setData(new AnalizeFieldString(getString(R.string.field_title_yak),
                    (catalog) ? getString(R.string.yes) : getString(R.string.no)));
        }

        if(domainData.getData().has("yandexRank")) {
            int yaRanc = domainData.getData().getAsJsonObject("yandexRank").get("yandexRank").getAsInt();
            AnalizeResultFieldLayout yaRankLayout = (AnalizeResultFieldLayout) rootView.findViewById(R.id.yaRankLayout);
            yaRankLayout.setData(new AnalizeFieldInt(getString(R.string.field_title_ya_rank), yaRanc));
        }

        if(domainData.getData().has("yandexIndex")) {
            int yaIndex = domainData.getData().getAsJsonObject("yandexIndex").get("yandexIndex").getAsInt();
            AnalizeResultFieldLayout yaIndLayout = (AnalizeResultFieldLayout) rootView.findViewById(R.id.yaIndLayout);
            yaIndLayout.setData(new AnalizeFieldInt(getString(R.string.field_title_ya_index), yaIndex));
        }

        if(domainData.getData().has("googleIndex")) {
            long goIndex = domainData.getData().getAsJsonObject("googleIndex").get("googleIndex").getAsLong();
            AnalizeResultFieldLayout goIndLayout = (AnalizeResultFieldLayout) rootView.findViewById(R.id.goIndLayout);
            goIndLayout.setData(new AnalizeFieldInt(getString(R.string.field_title_go_index), goIndex));
        }

        if(domainData.getData().has("alexaRank")) {
            int alexaRank = domainData.getData().getAsJsonObject("alexaRank").get("alexaGlobalRank").getAsInt();
            AnalizeResultFieldLayout alexaLayout = (AnalizeResultFieldLayout) rootView.findViewById(R.id.alexaLayout);
            alexaLayout.setData(new AnalizeFieldInt(getString(R.string.field_title_alexa_index), alexaRank));
        }

        return rootView;
    }

    private void invalidateAddToFavButton(final ImageView favButton) {
        if(helper.isDomainFavourite(domainData.getDomain())) {
            favButton.setImageResource(R.drawable.ic_action_remove_from_fav);
            favButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    helper.deleteDomainFromFavourites(domainData.getDomain());
                    invalidateAddToFavButton(favButton);
                }
            });
        } else {
            favButton.setImageResource(R.drawable.ic_action_add_to_fav);
            favButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    helper.addDomainToFavourites(new FavouriteDomainData(domainData.getDomain(), domainData.getFavicon()));
                    invalidateAddToFavButton(favButton);
                }
            });
        }
    }

    @Override
    public DomainData getDomainData() {
        return this.domainData;
    }

    class TabsAdapter extends FragmentStatePagerAdapter {
        public TabsAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 6;
        }

        @Override
        public Fragment getItem(int i) {
            switch(i) {
                case 0: return AnalizeMainPageFragment.newInstance();
                case 1: return AnalizeYandexFragment.newInstance();
                case 2: return AnalizeGoogleFragment.newInstance();
                case 3: return AnalizeDomainFragment.newInstance();
                case 4: return AnalizeSocialFragment.newInstance();
                case 5: return AnalizeOptimizationFragment.newInstance();
            }
            return null;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch(position) {
                case 0: return "Главная страница";
                case 1: return "Яндекс";
                case 2: return "Google";
                case 3: return "Домен";
                case 4: return "Социальные сети";
                case 5: return "Оптимизация";
            }
            return "";
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AnalizeInProgressFragment.OnFragmentInteractionListener) {
            mListener = (AnalizeResultsFragment.OnFragmentInteractionListener) context;
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

    public interface OnFragmentInteractionListener {

        public void onAnalizeRequested(boolean offline, String site);
        public void onAnalizeUpdateRequested(String site);
    }

}
