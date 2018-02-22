package ru.prcy.app.gui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ru.prcy.app.R;
import ru.prcy.app.data.AnalizeField;
import ru.prcy.app.data.AnalizeFieldBoolean;
import ru.prcy.app.data.AnalizeFieldDate;
import ru.prcy.app.data.AnalizeFieldInt;
import ru.prcy.app.data.AnalizeFieldString;
import ru.prcy.app.data.Common;

/**
 * Created by dmitry on 27.10.17.
 */

public class AnalizeResultTableRowLayout extends CardView {

    private String title;
    private Drawable icon;
    private TextView titleTextView;
    private TextView valueTextView;
    private TextView deltaTextView;
    private ImageView iconImageView;

    public AnalizeResultTableRowLayout(Context context) {
        super(context);
    }

    public AnalizeResultTableRowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        loadAttributes(attrs);
        initializeViews(context);
    }

    public AnalizeResultTableRowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        loadAttributes(attrs);
        initializeViews(context);
    }

    private void initializeViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.analize_result_table_row_layout, this);

        titleTextView = (TextView) findViewById(R.id.title);
        valueTextView = (TextView) findViewById(R.id.value);
        deltaTextView = (TextView) findViewById(R.id.delta);
        iconImageView = (ImageView) findViewById(R.id.icon);
        if(this.title != null) {
            titleTextView.setText(title);
        } else {
            titleTextView.setVisibility(View.GONE);
        }


        ImageView iconImageView = (ImageView) findViewById(R.id.icon);
        if(icon != null)
            iconImageView.setImageDrawable(icon);
    }

    private void loadAttributes(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.analizeResultTableRow);
        title = typedArray.getString(R.styleable.analizeResultTableRow_title);
        icon = typedArray.getDrawable(R.styleable.analizeResultTableRow_icon);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    public void setData(AnalizeField data) {
        setData(data, new PostProcessor());
    }

    public void setData(AnalizeField data, PostProcessor processor) {
        processor.setTitle(titleTextView, data);
        processor.setValue(valueTextView, data);
        processor.setDelta(deltaTextView, data);
        processor.setIcon(iconImageView, data);

    }

    public void setTitle(String title) {
        titleTextView.setText(title);
    }

    public void setValue(String value) {
        valueTextView.setText(value);
    }

    public void setIcon(int resourceId) {
        iconImageView.setImageResource(resourceId);
    }

}
