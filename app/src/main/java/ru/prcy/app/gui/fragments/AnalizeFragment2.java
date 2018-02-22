package ru.prcy.app.gui.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.SearchSuggestionsAdapter;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import ru.prcy.app.R;
import ru.prcy.app.data.ApiKey;
import ru.prcy.app.data.DomainSuggestion;
import ru.prcy.app.db.DomainData;
import ru.prcy.app.db.DomainDataHelper;
import ru.prcy.app.gui.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AnalizeFragment2.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AnalizeFragment2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AnalizeFragment2 extends Fragment {

    private static final String PREFS_FILE = "analize.prefs";
    private static final String OFFLINE_PREFS_KEY = "offline";

    public static final int SUGGESTION_LIMIT = 5;

    private static final long ANIM_DURATION = 350;

    private View mHeaderView;
    private View mDimSearchViewBackground;
    private ColorDrawable mDimDrawable;
    private FloatingSearchView mSearchView;

    private CheckBox offlineCheckBox;

    private boolean mIsDarkSearchTheme = false;
    private DomainDataHelper helper;

    private String mLastQuery = "";


    private OnFragmentInteractionListener mListener;

    public AnalizeFragment2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AnalizeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AnalizeFragment2 newInstance() {
        AnalizeFragment2 fragment = new AnalizeFragment2();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helper = new DomainDataHelper(getContext());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.analize, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        if(getActivity() != null)
            ((MainActivity) getActivity()).getSupportActionBar().setTitle("");
        View rootView = inflater.inflate(R.layout.fragment_analize2, container, false);

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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSearchView = (FloatingSearchView) view.findViewById(R.id.floating_search_view);
        mHeaderView = view.findViewById(R.id.header_view);
        offlineCheckBox = (CheckBox) view.findViewById(R.id.offlineCheckBox);

        offlineCheckBox.setChecked(getOffline());
        offlineCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setOffline(isChecked);
            }
        });

        DomainData data = helper.getLastFromHistory();

        if(data != null)
            mSearchView.setSearchText(data.getDomain());

        mDimSearchViewBackground = view.findViewById(R.id.dim_background);
        mDimDrawable = new ColorDrawable(getContext().getResources().getColor(R.color.colorPrimary));
        mDimDrawable.setAlpha(0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mDimSearchViewBackground.setBackground(mDimDrawable);
        } else {
            mDimSearchViewBackground.setBackgroundDrawable(mDimDrawable);
        }

        Log.d(this.getClass().getName(), "onViewCreated called");

        TextView siteLink = (TextView) view.findViewById(R.id.siteLink);
        siteLink.setMovementMethod(LinkMovementMethod.getInstance());

        setupFloatingSearch();
    }

    private void setupFloatingSearch() {

        mSearchView.setDismissFocusOnItemSelection(true);

        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {

            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {

                if (!oldQuery.equals("") && newQuery.equals("")) {
                    mSearchView.clearSuggestions();
                } else {

                    //this shows the top left circular progress
                    //you can call it where ever you want, but
                    //it makes sense to do it when loading something in
                    //the background.
                    mSearchView.showProgress();

                    helper.search(getContext(), newQuery, SUGGESTION_LIMIT, new DomainDataHelper.OnFindSuggestionsListener() {
                        @Override
                        public void onResults(List<DomainSuggestion> results) {
                            if(results != null) {
                                mSearchView.swapSuggestions(results);
                            }
                            mSearchView.hideProgress();
                        }
                    });

                }

            }
        });

        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(final SearchSuggestion searchSuggestion) {

                mLastQuery = searchSuggestion.getBody();
                mListener.onAnalizeRequested(getOffline(), searchSuggestion.getBody());
            }

            @Override
            public void onSearchAction(String query) {
                Log.d(this.getClass().getName(), "search action query = " + query);
                mLastQuery = query;
                mListener.onAnalizeRequested(getOffline(), query);
            }
        });

        mSearchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {
                int headerHeight = getResources().getDimensionPixelOffset(R.dimen.sliding_search_view_header_height);

                // Calculate ActionBar height
                TypedValue tv = new TypedValue();
                int actionBarHeight = 100;
                if (getContext().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
                {
                    actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
                }

                ObjectAnimator anim = ObjectAnimator.ofFloat(mSearchView, "translationY",
                        headerHeight, actionBarHeight);
                anim.setDuration(350);
                fadeDimBackground(0, 150, null);
                anim.addListener(new AnimatorListenerAdapter() {

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        //show suggestions when search bar gains focus (typically history suggestions)
//                        mSearchView.swapSuggestions(DataHelper.getHistory(getActivity(), 3));
                        mSearchView.swapSuggestions(helper.suggest());
                        offlineCheckBox.setVisibility(View.GONE);
                    }
                });
                anim.start();
            }

            @Override
            public void onFocusCleared() {
                int headerHeight = getResources().getDimensionPixelOffset(R.dimen.sliding_search_view_header_height);

                // Calculate ActionBar height
                TypedValue tv = new TypedValue();
                int actionBarHeight = 100;
                if (getContext().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
                {
                    actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
                }

                ObjectAnimator anim = ObjectAnimator.ofFloat(mSearchView, "translationY",
                        actionBarHeight, headerHeight);
                anim.setDuration(350);
                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        offlineCheckBox.setVisibility(View.VISIBLE);
                    }
                });
                anim.start();
                fadeDimBackground(150, 0, null);

                //set the title of the bar so that when focus is returned a new query begins
                mSearchView.setSearchText(mLastQuery);

                //you can also set setSearchText(...) to make keep the query there when not focused and when focus returns
                //mSearchView.setSearchText(searchSuggestion.getBody());

            }
        });


        //handle menu clicks the same way as you would
        //in a regular activity
        mSearchView.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {
            @Override
            public void onActionMenuItemSelected(MenuItem item) {

//                if (item.getItemId() == R.id.action_favourites ) {
//
//                    mIsDarkSearchTheme = true;
//
//                    //demonstrate setting colors for items
//                    mSearchView.setBackgroundColor(Color.parseColor("#787878"));
//                    mSearchView.setViewTextColor(Color.parseColor("#e9e9e9"));
//                    mSearchView.setHintTextColor(Color.parseColor("#e9e9e9"));
//                    mSearchView.setActionMenuOverflowColor(Color.parseColor("#e9e9e9"));
//                    mSearchView.setMenuItemIconColor(Color.parseColor("#e9e9e9"));
//                    mSearchView.setLeftActionIconColor(Color.parseColor("#e9e9e9"));
//                    mSearchView.setClearBtnColor(Color.parseColor("#e9e9e9"));
//                    mSearchView.setDividerColor(Color.parseColor("#BEBEBE"));
//                    mSearchView.setLeftActionIconColor(Color.parseColor("#e9e9e9"));
//                } else {
//
//                    //just print action
//                    Toast.makeText(getActivity().getApplicationContext(), item.getTitle(),
//                            Toast.LENGTH_SHORT).show();
//                }

            }
        });

        //use this listener to listen to menu clicks when app:floatingSearch_leftAction="showHome"
        mSearchView.setOnHomeActionClickListener(new FloatingSearchView.OnHomeActionClickListener() {
            @Override
            public void onHomeClicked() {

            }
        });

        /*
         * Here you have access to the left icon and the text of a given suggestion
         * item after as it is bound to the suggestion list. You can utilize this
         * callback to change some properties of the left icon and the text. For example, you
         * can load the left icon images using your favorite image loading library, or change text color.
         *
         *
         * Important:
         * Keep in mind that the suggestion list is a RecyclerView, so views are reused for different
         * items in the list.
         */
        mSearchView.setOnBindSuggestionCallback(new SearchSuggestionsAdapter.OnBindSuggestionCallback() {
            @Override
            public void onBindSuggestion(View suggestionView, ImageView leftIcon,
                                         TextView textView, SearchSuggestion item, int itemPosition) {
                DomainSuggestion domainSuggestion = (DomainSuggestion) item;

                String textColor = mIsDarkSearchTheme ? "#ffffff" : "#000000";
                String textLight = mIsDarkSearchTheme ? "#bfbfbf" : "#787878";


                if(domainSuggestion.getDomainData().getFavicon() != null) {
                    Glide.with(getContext()).load(domainSuggestion.getDomainData().getFavicon())
                            .apply(new RequestOptions().placeholder(R.drawable.ic_favicon))
                            .into(leftIcon);
                }
//                leftIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(),
//                        R.drawable.ic_history_black_24dp, null));
                //Util.setIconColor(leftIcon, Color.parseColor(textColor));
              //  leftIcon.setAlpha(.36f);
//                if (colorSuggestion.getIsHistory()) {
//                    leftIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(),
//                            R.drawable.ic_history_black_24dp, null));
//
//                    Util.setIconColor(leftIcon, Color.parseColor(textColor));
//                    leftIcon.setAlpha(.36f);
//                } else {
//                    leftIcon.setAlpha(0.0f);
//                    leftIcon.setImageDrawable(null);
//                }

                textView.setTextColor(Color.parseColor(textColor));
                String text = domainSuggestion.getBody()
                        .replaceFirst(mSearchView.getQuery(),
                                "<font color=\"" + textLight + "\">" + mSearchView.getQuery() + "</font>");
                textView.setText(Html.fromHtml(text));
            }

        });

        /*
         * When the user types some text into the search field, a clear button (and 'x' to the
         * right) of the search text is shown.
         *
         * This listener provides a callback for when this button is clicked.
         */
        mSearchView.setOnClearSearchActionListener(new FloatingSearchView.OnClearSearchActionListener() {
            @Override
            public void onClearSearchClicked() {

            }
        });
    }


    private void fadeDimBackground(int from, int to, Animator.AnimatorListener listener) {
        ValueAnimator anim = ValueAnimator.ofInt(from, to);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                int value = (Integer) animation.getAnimatedValue();
                mDimDrawable.setAlpha(value);
            }
        });
        if (listener != null) {
            anim.addListener(listener);
        }
        anim.setDuration(ANIM_DURATION);
        anim.start();
    }

    private void setOffline(boolean offline) {
        SharedPreferences prefs = getContext().getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE);
        prefs.edit().putBoolean(OFFLINE_PREFS_KEY, offline).commit();
    }

    private boolean getOffline() {
        return getContext().getSharedPreferences(PREFS_FILE, Context.MODE_PRIVATE).getBoolean(OFFLINE_PREFS_KEY, false);
    }
}
