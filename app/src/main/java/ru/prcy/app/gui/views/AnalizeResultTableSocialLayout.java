package ru.prcy.app.gui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import ru.prcy.app.R;
import ru.prcy.app.data.Common;

/**
 * Created by dmitry on 27.10.17.
 */

public class AnalizeResultTableSocialLayout extends CardView {

    String title;
    Drawable icon;
    private ViewGroup container;

    public AnalizeResultTableSocialLayout(Context context) {
        super(context);
    }

    public AnalizeResultTableSocialLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        loadAttributes(attrs);
        initializeViews(context);
    }

    public AnalizeResultTableSocialLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        loadAttributes(attrs);
        initializeViews(context);
    }

    private void initializeViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.analize_result_table_social_layout, this);

        container = (ViewGroup) findViewById(R.id.contentContainer);
        TextView titleTextView = (TextView) findViewById(R.id.title);
        titleTextView.setText(title);

        ImageView iconImageView = (ImageView) findViewById(R.id.headerIcon);
        iconImageView.setImageDrawable(icon);
    }

    private void loadAttributes(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.analizeResultTable);
        title = typedArray.getString(R.styleable.analizeResultTable_title);
        icon = typedArray.getDrawable(R.styleable.analizeResultTable_icon);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if(container == null) {
            super.addView(child, index, params);
        } else {
            container.addView(child, index, params);
        }
    }

    public void setData(String name, String link, String icon, String about) {
        TextView nameTextView = (TextView) findViewById(R.id.name);
        if(link != null)
            nameTextView.setText(Common.fromHtml("<a href='" + link + "'>" + name + "</a>"));
        else
            nameTextView.setText(Common.fromHtml(name));
        nameTextView.setMovementMethod(LinkMovementMethod.getInstance());

        ImageView iconImageView = (ImageView) findViewById(R.id.icon);
        Glide.with(getContext()).load(icon).into(iconImageView);
        TextView aboutTextView = (TextView) findViewById(R.id.about);
        aboutTextView.setMovementMethod(LinkMovementMethod.getInstance());
        aboutTextView.setText(Common.fromHtml(about));
    }


}
