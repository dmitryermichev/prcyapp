package ru.prcy.app.gui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ru.prcy.app.R;
import ru.prcy.app.data.Common;

/**
 * Created by dmitry on 27.10.17.
 */

public class AnalizeResultTableSingleColumnLayout extends CardView {

    String title;
    Drawable icon;

    String titleGravity;

    private TextView titleTextView;
    private ImageView iconImageView;

    public AnalizeResultTableSingleColumnLayout(Context context) {
        super(context);
    }

    public AnalizeResultTableSingleColumnLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        loadAttributes(attrs);
        initializeViews(context);
    }

    public AnalizeResultTableSingleColumnLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        loadAttributes(attrs);
        initializeViews(context);
    }

    private void initializeViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.analize_result_table_single_column_layout, this);

        titleTextView = (TextView) findViewById(R.id.title);
        titleTextView.setMovementMethod(LinkMovementMethod.getInstance());

        Log.d(this.getClass().getName(), "gravity = " + titleGravity + " title = " + title);

        if(titleGravity != null) {
            if (titleGravity.equals("center"))
                titleTextView.setGravity(Gravity.CENTER);
            else if (titleGravity.equals("left"))
                titleTextView.setGravity(Gravity.LEFT);
            else if (titleGravity.equals("right"))
                titleTextView.setGravity(Gravity.RIGHT);
        }

        if(this.title != null) {
            titleTextView.setText(Common.fromHtml(title));
        } else {
            titleTextView.setVisibility(View.GONE);
        }


        iconImageView = (ImageView) findViewById(R.id.icon);
        if(icon != null)
            iconImageView.setImageDrawable(icon);
    }

    private void loadAttributes(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.analizeResultTableRow);
        title = typedArray.getString(R.styleable.analizeResultTableRow_title);
        icon = typedArray.getDrawable(R.styleable.analizeResultTableRow_icon);
        titleGravity = typedArray.getString(R.styleable.analizeResultTableRow_titleGravity);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    public void setValue(String title) {

       titleTextView.setText(Common.fromHtml(title));

       titleTextView.setVisibility(View.VISIBLE);
    }

    public void setIcon(int resourceId) {
        iconImageView.setVisibility(View.VISIBLE);
        iconImageView.setImageResource(resourceId);
    }

}
