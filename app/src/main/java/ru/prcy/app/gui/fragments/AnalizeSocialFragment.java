package ru.prcy.app.gui.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.JsonObject;

import ru.prcy.app.R;
import ru.prcy.app.data.AnalizeFieldString;
import ru.prcy.app.db.DomainData;
import ru.prcy.app.gui.views.AnalizeResultTableRowLayout;
import ru.prcy.app.gui.views.AnalizeResultTableSocialLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AnalizeSocialFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AnalizeSocialFragment extends AnalizeDetailsFragment {

    DomainData domainData;


    public AnalizeSocialFragment() {
        // Required empty public constructor
    }

    public static AnalizeSocialFragment newInstance() {
        AnalizeSocialFragment fragment = new AnalizeSocialFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_analize_social, container, false);

        if(getParentFragment() != null && getParentFragment() instanceof DomainDataProvider) {
            this.domainData = ((DomainDataProvider) getParentFragment()).getDomainData();
        }

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        JsonObject data = this.domainData.getData();

        if(data != null) {

            setIntValueFromAnalize(view, R.id.vkSocialCounter, data, "socialCounters", "vkontakteShareCount");
            setIntValueFromAnalize(view, R.id.fbSocialCounter, data, "socialCounters", "facebookLinkShareCount");
            setIntValueFromAnalize(view, R.id.gpSocialCounter, data, "socialCounters", "googlePlusCount");

            if(data.has("socialCounters")) {

//                setIntValueFromAnalize(view, R.id.vkSocialCounter, data, "socialCounters", "vkontakteShareCount");
//                setIntValueFromAnalize(view, R.id.fbSocialCounter, data, "socialCounters", "facebookLinkShareCount");
//                setIntValueFromAnalize(view, R.id.gpSocialCounter, data, "socialCounters", "googlePlusCount");
//
//                JsonObject sc = data.get("socialCounters").getAsJsonObject();
//                int vkontakteCounter = sc.get("vkontakteShareCount").getAsInt();
//                int facebookCounter = sc.get("facebookLinkShareCount").getAsInt();
//                int googlePlusCounter = sc.get("googlePlusCount").getAsInt();
//
//                ((AnalizeResultTableRowLayout) view.findViewById(R.id.vkSocialCounter)).setData("" + vkontakteCounter);
//                ((AnalizeResultTableRowLayout) view.findViewById(R.id.fbSocialCounter)).setData("" + facebookCounter);
//                ((AnalizeResultTableRowLayout) view.findViewById(R.id.gpSocialCounter)).setData("" + googlePlusCounter);

            }

            AnalizeResultTableSocialLayout facebookSocial = (AnalizeResultTableSocialLayout) view.findViewById(R.id.facebookSocial);
            AnalizeResultTableSocialLayout vkontakteSocial = (AnalizeResultTableSocialLayout) view.findViewById(R.id.vkontakteSocial);
            AnalizeResultTableSocialLayout twitterSocial = (AnalizeResultTableSocialLayout) view.findViewById(R.id.twitterSocial);
            AnalizeResultTableSocialLayout googlePlusSocial = (AnalizeResultTableSocialLayout) view.findViewById(R.id.googlePlusSocial);

            if(data.has("facebookSocial") && !data.getAsJsonObject("facebookSocial").get("link").getAsString().equals("false")) {
                JsonObject fs = data.getAsJsonObject("facebookSocial");

                String name = fs.get("groupName").getAsString();
                String link = fs.get("link").getAsString();
                String icon = fs.get("picture").getAsString();
                String about = fs.get("groupAbout").getAsString();

                facebookSocial.setData(name, link, icon, about);

                setIntValueFromAnalize(view, R.id.fbLikes, data, "facebookSocial", "likes");
                setIntValueFromAnalize(view, R.id.fbTalking, data, "facebookSocial", "talking");

//
//                String likes = fs.get("likes").getAsString();
//                String talking = fs.get("talking").getAsString();
//
//                ((AnalizeResultTableRowLayout) view.findViewById(R.id.fbLikes)).setData(likes);
//                ((AnalizeResultTableRowLayout) view.findViewById(R.id.fbTalking)).setData(talking);

            } else {
                facebookSocial.setVisibility(View.GONE);
            }

            if(data.has("vkontakteSocial") && !data.getAsJsonObject("vkontakteSocial").get("link").getAsString().equals("false")) {
                JsonObject vs = data.getAsJsonObject("vkontakteSocial");

                String name = vs.get("groupName").getAsString();
                String link = vs.get("link").getAsString();
                String icon = vs.get("groupPhoto").getAsString();
                String about = vs.get("groupStatus").getAsString();

                vkontakteSocial.setData(name, link, icon, about);

                setIntValueFromAnalize(view, R.id.vkMembers, data, "vkontakteSocial", "groupMembersCount");

//                String members = vs.get("groupMembersCount").getAsString();
//
//                ((AnalizeResultTableRowLayout) view.findViewById(R.id.vkMembers)).setData(members);
            } else {
                vkontakteSocial.setVisibility(View.GONE);
            }

            if(data.has("twitterSocial") && !data.getAsJsonObject("twitterSocial").get("link").getAsString().equals("false")) {
                JsonObject tw = data.getAsJsonObject("twitterSocial");

                String name = tw.get("profileName").getAsString();
                String link = tw.get("link").getAsString();
                String icon = tw.get("profileImageUrl").getAsString();
                String about = tw.get("profileDescription").getAsString();

                twitterSocial.setData(name, link, icon, about);

                setIntValueFromAnalize(view, R.id.twTweets, data, "twitterSocial", "tweets");
                setIntValueFromAnalize(view, R.id.twFollowers, data, "twitterSocial", "followers");
                setIntValueFromAnalize(view, R.id.twFollowing, data, "twitterSocial", "following");

//                String tweets = tw.get("tweets").getAsString();
//                String followers = tw.get("followers").getAsString();
//                String following = tw.get("following").getAsString();
//
//                ((AnalizeResultTableRowLayout) view.findViewById(R.id.twTweets)).setData(tweets);
//                ((AnalizeResultTableRowLayout) view.findViewById(R.id.twFollowers)).setData(followers);
//                ((AnalizeResultTableRowLayout) view.findViewById(R.id.twFollowing)).setData(following);

            } else {
                twitterSocial.setVisibility(View.GONE);
            }



            if(data.has("googlePlusSocial") && !data.getAsJsonObject("googlePlusSocial").get("link").getAsString().equals("false")) {
                JsonObject gp = data.getAsJsonObject("googlePlusSocial");

                String name = gp.get("displayName").getAsString();
                String link = gp.get("link").getAsString();
                String icon = gp.get("picture").getAsString();
                String about = gp.get("aboutMe").getAsString();

                googlePlusSocial.setData(name, link, icon, about);

                setIntValueFromAnalize(view, R.id.gpCircled, data, "googlePlusSocial", "circledByCount");

                String plusOneCount;
                if(!gp.get("plusOneCount").getAsString().equals("false"))
                    setIntValueFromAnalize(view, R.id.gpPlusOne, data, "googlePlusSocial", "plusOneCount");
                else
                    ((AnalizeResultTableRowLayout) view.findViewById(R.id.gpPlusOne)).setData(new AnalizeFieldString(getString(R.string.na)));



            } else {
                googlePlusSocial.setVisibility(View.GONE);
            }
        }


    }
}
