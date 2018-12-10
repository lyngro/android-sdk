package com.lyngro.android.sdk.widgets;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by j.naim on 10-Jun-16.
 */
public class WidgetDataHandler {

//    public Widget getWidget(String widgetUrl) {
//        Widget widget = new Widget();
//        String stream = null;
//        //String urlString = strings[0];
//
//        HTTPDataHandler httpDataHandler = new HTTPDataHandler();
//        stream = httpDataHandler.ReceiveHTTPData(widgetUrl);
//
//        if (stream != null) {
//            try {
//                // Get the full HTTP Data as JSONObject
//                JSONObject reader = new JSONObject(stream);
//
//                widget.setWidgetId(reader.getString("WidgetId"));
//                widget.setWidgetType(reader.getString("WidgetType"));
//
//                JSONArray posts = reader.getJSONArray("Posts");
//                for (int i = 0; i < posts.length(); i++) {
//
//                    JSONObject post = posts.getJSONObject(i);
//
//                    widget.getPosts().add(widget.new Post(
//                            post.getString("PostType"),
//                            post.getString("PostId"),
//                            post.getString("Title"),
//                            post.getString("Url"),
//                            post.getString("Thumbnail"),
//                            widget.getWidgetId())
//                    );
//
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        return widget;
//    }

    public Widget getWidget(String widgetUrl) {
        Widget widget = new Widget();
        String stream = null;
        //String urlString = strings[0];
        Log.d("widget.widgetUrl" ,widgetUrl + "");
        HTTPDataHandler httpDataHandler = new HTTPDataHandler();
        stream = httpDataHandler.ReceiveHTTPData(widgetUrl);

        if (stream != null) {
            try {
                // Get the full HTTP Data as JSONObject
                JSONObject reader = new JSONObject(stream);
                JSONArray data = reader.getJSONArray("data");

                Log.d("widget.data" ,data + "");
                String id = reader.getString("id");
                String widgetType = reader.getString("key");

                widget.setWidgetId(id);
                widget.setWidgetType(widgetType);

                for (int i = 0; i < data.length(); i++) {
                    JSONObject post = (JSONObject) data.get(i);//((JSONObject) data.get(i)).getJSONObject("Post");
                    widget.getPosts().add(
                        new WidgetPost(
                            post.getString("postid"),
                            post.getString("title"),
                            post.getString("url"),
                            post.getString("kryptonz_thumb"),
                            post.getInt("index"),
                            post.getDouble("rank")
                        )
                    );
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return widget;
    }
}

//public class WidgetDataHandler extends AsyncTask<String, Void, Widget> {
//    @Override
//    protected Widget doInBackground(String... strings) {
//        Widget widget = new Widget();
//        String stream = null;
//        String urlString = strings[0];
//
//        HTTPDataHandler httpDataHandler = new HTTPDataHandler();
//        stream = httpDataHandler.ReceiveHTTPData(urlString);
//
//        if (stream != null) {
//            try {
//                // Get the full HTTP Data as JSONObject
//                JSONObject reader = new JSONObject(stream);
//
//                widget.setWidgetId(reader.getString("WidgetId"));
//                widget.setWidgetType(reader.getString("WidgetType"));
//
//                JSONArray posts = reader.getJSONArray("Posts");
//                for (int i = 0; i < posts.length(); i++) {
//
//                    JSONObject post = posts.getJSONObject(i);
//
//                    widget.getPosts().add(widget.new Post(
//                            post.getString("PostType"),
//                            post.getString("PostId"),
//                            post.getString("Title"),
//                            post.getString("Url"),
//                            post.getString("Thumbnail"),
//                            widget.getWidgetId())
//                    );
//
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        return widget;
//    }
//}
