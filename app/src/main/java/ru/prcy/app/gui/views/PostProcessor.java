package ru.prcy.app.gui.views;

import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import ru.prcy.app.R;
import ru.prcy.app.data.AnalizeField;
import ru.prcy.app.data.AnalizeFieldBoolean;
import ru.prcy.app.data.AnalizeFieldDate;
import ru.prcy.app.data.AnalizeFieldInt;
import ru.prcy.app.data.AnalizeFieldString;
import ru.prcy.app.data.Common;

/**
 * Created by dmitry on 02.11.17.
 */

public class PostProcessor {
    public void setTitle(TextView titleTextView, AnalizeField data) {
        if(data.title != null) {
            titleTextView.setText(Common.fromHtml(data.title));
            titleTextView.setVisibility(View.VISIBLE);
        }
    }

    public void setValue(TextView valueTextView, AnalizeField data) {

        if(data instanceof AnalizeFieldString) {
            String value = ((AnalizeFieldString) data).value;
            valueTextView.setText(value);
        } else if (data instanceof AnalizeFieldBoolean) {
            boolean value = ((AnalizeFieldBoolean) data).value;
            valueTextView.setText((value) ? valueTextView.getContext().getString(R.string.yes) : valueTextView.getContext().getString(R.string.no));
        } else if (data instanceof AnalizeFieldDate) {
            String value = ((AnalizeFieldDate) data).getValueString();
            valueTextView.setText(value);
        } else if (data instanceof AnalizeFieldInt) {
            AnalizeFieldInt intData = (AnalizeFieldInt) data;
            valueTextView.setText(intData.getValueString());
        }


    }


    public void setDelta(TextView deltaTextView, AnalizeField data) {

        if(data instanceof AnalizeFieldInt) {
            AnalizeFieldInt intData = (AnalizeFieldInt) data;
            if (intData.getDelta() == 0) {
                deltaTextView.setVisibility(View.GONE);
            } else if (intData.getDelta() < 0) {
                deltaTextView.setTextColor(ContextCompat.getColor(deltaTextView.getContext(), R.color.colorRed));
            } else {
                deltaTextView.setTextColor(ContextCompat.getColor(deltaTextView.getContext(), R.color.colorGreen));
            }
            deltaTextView.setText(intData.getDeltaString());
        }

    }

    public void setIcon(ImageView iconImageView, AnalizeField data) {

    }

    public static final PostProcessor BOOLEAN_INVERSE_PROCESSOR = new PostProcessor() {
        @Override
        public void setIcon(ImageView iconImageView, AnalizeField data) {
            AnalizeFieldBoolean booleanData = (AnalizeFieldBoolean) data;
            if(booleanData.value) {
                iconImageView.setImageResource(R.drawable.negative);
            } else
                iconImageView.setImageResource(R.drawable.positive);
        }
    };

    public static final PostProcessor BOOLEAN_POSITIVE_PROCESSOR = new PostProcessor() {
        @Override
        public void setIcon(ImageView iconImageView, AnalizeField data) {
            AnalizeFieldBoolean booleanData = (AnalizeFieldBoolean) data;
            if(booleanData.value) {
                iconImageView.setImageResource(R.drawable.positive);
            } else
                iconImageView.setImageResource(R.drawable.negative);
        }
    };

}
