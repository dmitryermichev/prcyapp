package ru.prcy.app.gui.views;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ru.prcy.app.R;
import ru.prcy.app.data.AnalizeField;
import ru.prcy.app.data.AnalizeFieldBoolean;
import ru.prcy.app.data.AnalizeFieldDate;
import ru.prcy.app.data.AnalizeFieldInt;
import ru.prcy.app.data.AnalizeFieldString;

/**
 * Created by dmitry on 27.10.17.
 */

public class AnalizeResultFieldLayout extends RelativeLayout {

    AnalizeFieldString data;

    public AnalizeResultFieldLayout(Context context) {
        super(context);
    }

    public AnalizeResultFieldLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context);
    }

    public AnalizeResultFieldLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initializeViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.analize_result_field_layout, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    public void setData(AnalizeField data) {
        ((TextView) findViewById(R.id.field_title)).setText(data.title);

        TextView valueTextView = (TextView) findViewById(R.id.field_value);
        TextView deltaTextView = (TextView) findViewById(R.id.field_delta);

        if(data instanceof AnalizeFieldString) {
            String value = ((AnalizeFieldString) data).value;
            valueTextView.setText("" + value);
        } else if (data instanceof AnalizeFieldBoolean) {
            boolean value = ((AnalizeFieldBoolean) data).value;
            valueTextView.setText((value) ? getContext().getString(R.string.yes) : getContext().getString(R.string.no));
        } else if (data instanceof AnalizeFieldDate) {
            String value = ((AnalizeFieldDate) data).getValueString();
            valueTextView.setText(value);
        } else if (data instanceof AnalizeFieldInt) {
            AnalizeFieldInt intData = (AnalizeFieldInt) data;
            valueTextView.setText(intData.getValueString());
            if(intData.getDelta() == 0) {
                deltaTextView.setVisibility(View.GONE);
            } else if(intData.getDelta() > 0) {
                deltaTextView.setVisibility(View.VISIBLE);
                deltaTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorGreen));
            } else {
                deltaTextView.setVisibility(View.VISIBLE);
                deltaTextView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorRed));
            }
            deltaTextView.setText(intData.getDeltaString());
        }

    }
}
