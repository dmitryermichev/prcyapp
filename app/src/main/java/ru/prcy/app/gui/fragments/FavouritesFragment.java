package ru.prcy.app.gui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.prcy.app.R;
import ru.prcy.app.db.DomainData;
import ru.prcy.app.db.DomainDataHelper;
import ru.prcy.app.db.FavouriteDomainData;
import ru.prcy.app.gui.MainActivity;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class FavouritesFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String ARG_ALLOW_DELETE = "allowDelete";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private boolean allowDelete = true;
    private OnListFragmentInteractionListener mListener;
    FavouritesRecyclerViewAdapter adapter;
    private List<FavouriteDomainData> favourites;
    private DomainDataHelper helper;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FavouritesFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static FavouritesFragment newInstance(int columnCount, boolean allowDelete) {
        FavouritesFragment fragment = new FavouritesFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        args.putBoolean(ARG_ALLOW_DELETE, allowDelete);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            allowDelete = getArguments().getBoolean(ARG_ALLOW_DELETE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.favourites_fragment_list, container, false);

        if(getActivity() != null)
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.action_favourites);

        helper = new DomainDataHelper(getContext());

        this.favourites = helper.getFavourites();

        View list = view.findViewById(R.id.list);

        Context context = list.getContext();
        final RecyclerView recyclerView = (RecyclerView) list;
        if (mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }

        final View finalView = view;

        adapter = new FavouritesRecyclerViewAdapter(getContext(), favourites, allowDelete, new OnListFragmentInteractionListener() {
            @Override
            public void onDomainSelected(FavouriteDomainData data) {
                mListener.onDomainSelected(data);
            }

            @Override
            public void onDomainDeleted(String domain) {
                helper.deleteDomainFromFavourites(domain);
                favourites = helper.getFavourites();
                adapter.setValues(favourites);
                invalidateFavourites(finalView);
            }
        });

        recyclerView.setAdapter(adapter);

        invalidateFavourites(view);

        return view;
    }

    public void invalidateFavourites(View rootView) {
        if(favourites.size() > 0) {

            rootView.findViewById(R.id.list).setVisibility(View.VISIBLE);
            rootView.findViewById(R.id.noFavourites).setVisibility(View.GONE);
        } else {
            rootView.findViewById(R.id.list).setVisibility(View.GONE);
            rootView.findViewById(R.id.noFavourites).setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        void onDomainSelected(FavouriteDomainData data);
        void onDomainDeleted(String domain);
    }
}
