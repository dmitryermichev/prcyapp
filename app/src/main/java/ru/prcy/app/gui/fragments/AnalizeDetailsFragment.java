package ru.prcy.app.gui.fragments;

import android.support.v4.app.Fragment;
import android.view.View;

import com.google.gson.JsonObject;

import ru.prcy.app.data.AnalizeFieldBoolean;
import ru.prcy.app.data.AnalizeFieldDate;
import ru.prcy.app.data.AnalizeFieldInt;
import ru.prcy.app.data.AnalizeFieldObject;
import ru.prcy.app.data.AnalizeFieldString;
import ru.prcy.app.gui.views.AnalizeResultTableRowLayout;
import ru.prcy.app.gui.views.PostProcessor;

/**
 * Created by dmitry on 01.11.17.
 */

public class AnalizeDetailsFragment extends Fragment{



    protected void setDateValueFromAnalize(View rootView, int layoutResourceId, JsonObject data, String key) {
        setDateValueFromAnalize(rootView, layoutResourceId, data, key, key);

    }


    protected void setDateValueFromAnalize(View rootView, int layoutResourceId, JsonObject data, String key, String subKey) {
        if(data.has(key)) {
            AnalizeResultTableRowLayout rowLayout = (AnalizeResultTableRowLayout) rootView.findViewById(layoutResourceId);
            rowLayout.setData(AnalizeFieldDate.getValueFromAnalize(data, key, subKey));
        }

    }

    protected void setStringValueFromAnalize(View rootView, int layoutResourceId, JsonObject data, String key) {
        setStringValueFromAnalize(rootView, layoutResourceId, data, key, key);

    }


    protected void setStringValueFromAnalize(View rootView, int layoutResourceId, JsonObject data, String key, String subKey) {
        if(data.has(key)) {
            AnalizeResultTableRowLayout rowLayout = (AnalizeResultTableRowLayout) rootView.findViewById(layoutResourceId);
            rowLayout.setData(AnalizeFieldString.getValueFromAnalize(data, key, subKey));
        }

    }

    protected void setStringValueFromAnalize(View rootView, int layoutResourceId, JsonObject data, String key, PostProcessor processor) {
        setStringValueFromAnalize(rootView, layoutResourceId, data, key, key, processor);

    }


    protected void setStringValueFromAnalize(View rootView, int layoutResourceId, JsonObject data, String key, String subKey, PostProcessor processor) {
        if(data.has(key)) {
            AnalizeResultTableRowLayout rowLayout = (AnalizeResultTableRowLayout) rootView.findViewById(layoutResourceId);
            AnalizeFieldString result = AnalizeFieldString.getValueFromAnalize(data, key, subKey);
            rowLayout.setData(result, processor);
        }

    }


    protected void setIntValueFromAnalize(View rootView, int layoutResourceId, JsonObject data, String key) {
        setIntValueFromAnalize(rootView, layoutResourceId, data, key, key);

    }


    protected void setIntValueFromAnalize(View rootView, int layoutResourceId, JsonObject data, String key, String subKey) {
        if(data.has(key)) {
            AnalizeResultTableRowLayout rowLayout = (AnalizeResultTableRowLayout) rootView.findViewById(layoutResourceId);
            rowLayout.setData(AnalizeFieldInt.getValueFromAnalize(data, key, subKey));
        }

    }


    protected void setIntValueFromAnalize(View rootView, int layoutResourceId, JsonObject data, String key, PostProcessor processor) {
        setIntValueFromAnalize(rootView, layoutResourceId, data, key, key, processor);

    }


    protected void setIntValueFromAnalize(View rootView, int layoutResourceId, JsonObject data, String key, String subKey, PostProcessor processor) {
        if(data.has(key)) {
            AnalizeResultTableRowLayout rowLayout = (AnalizeResultTableRowLayout) rootView.findViewById(layoutResourceId);
            AnalizeFieldInt result = AnalizeFieldInt.getValueFromAnalize(data, key, subKey);
            rowLayout.setData(result, processor);
        }

    }


    protected void setBooleanValueFromAnalize(View rootView, int layoutResourceId, JsonObject data, String key) {
        setBooleanValueFromAnalize(rootView, layoutResourceId, data, key, key);

    }


    protected void setBooleanValueFromAnalize(View rootView, int layoutResourceId, JsonObject data, String key, String subKey) {
        if(data.has(key)) {
            AnalizeResultTableRowLayout rowLayout = (AnalizeResultTableRowLayout) rootView.findViewById(layoutResourceId);

            rowLayout.setData(AnalizeFieldBoolean.getValueFromAnalize(data, key, subKey));
        }

    }

    protected void setBooleanValueFromAnalize(View rootView, int layoutResourceId, JsonObject data, String key, PostProcessor processor) {
        setBooleanValueFromAnalize(rootView, layoutResourceId, data, key, key, processor);

    }


    protected void setBooleanValueFromAnalize(View rootView, int layoutResourceId, JsonObject data, String key, String subKey, PostProcessor processor) {
        if(data.has(key)) {
            AnalizeResultTableRowLayout rowLayout = (AnalizeResultTableRowLayout) rootView.findViewById(layoutResourceId);
            AnalizeFieldBoolean result = AnalizeFieldBoolean.getValueFromAnalize(data, key, subKey);
            rowLayout.setData(result, processor);
        }

    }

    //Сохранение объекта


    protected void setObjectValueFromAnalize(View rootView, int layoutResourceId, JsonObject data, String key, PostProcessor processor) {
        setObjectValueFromAnalize(rootView, layoutResourceId, data, key, key, processor);
    }


    protected void setObjectValueFromAnalize(View rootView, int layoutResourceId, JsonObject data, String key, String subKey, PostProcessor processor) {
        if(data.has(key)) {
            AnalizeResultTableRowLayout rowLayout = (AnalizeResultTableRowLayout) rootView.findViewById(layoutResourceId);
            AnalizeFieldObject result = AnalizeFieldObject.getValueFromAnalize(data, key, subKey);
            rowLayout.setData(result, processor);
        }

    }


}
