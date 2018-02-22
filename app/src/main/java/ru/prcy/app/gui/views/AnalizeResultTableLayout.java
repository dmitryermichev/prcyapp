package ru.prcy.app.gui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ru.prcy.app.R;

/**
 * Created by dmitry on 27.10.17.
 */

public class AnalizeResultTableLayout extends CardView {

    String title;
    Drawable icon;
    private ViewGroup container;

    public AnalizeResultTableLayout(Context context) {
        super(context);
    }

    public AnalizeResultTableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        loadAttributes(attrs);
        initializeViews(context);
    }

    public AnalizeResultTableLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        loadAttributes(attrs);
        initializeViews(context);
    }

    private void initializeViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.analize_result_table_layout, this);

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
}
