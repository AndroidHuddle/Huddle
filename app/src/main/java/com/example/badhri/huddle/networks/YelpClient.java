package com.example.badhri.huddle.networks;

/** Return the Call<SearchResponse> so that user can implement the Async / Sync approach */

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;
import org.scribe.model.Token;

public class YelpClient extends OAuthBaseClient{

    public static final Class<? extends Api> REST_API_CLASS = YelpApi2.class;
    public static final String REST_URL = "https://api.yelp.com/v2";
    public static final String CONSUMER_KEY = "4O9FjQaJoNOiCLdycjyxTg";
    public static final String CONSUMER_SECRET = "B6nbe5j3eYwxwcoN0vSJVIChX3I";
    public static final String TOKEN = "HbwgSPLuaA4ng1GRYXj5oBAe0Z0NKclN";
    public static final String TOKEN_SECRET = "ht4m9xuvDa3wF6iVqshIIMdga6w";
    public static final String REST_CALLBACK_URL = "oauth://hdyelp"; // Change this (here and in manifest)


    public YelpClient(Context context) {
        super(context, REST_API_CLASS, REST_URL, CONSUMER_KEY, CONSUMER_SECRET, REST_CALLBACK_URL);
        this.client.setAccessToken(new Token(TOKEN, TOKEN_SECRET));
    }

    public void search(String term, String location, AsyncHttpResponseHandler handler) {
        // http://api.yelp.com/v2/search?term=food&location=San+Francisco
        String apiUrl = getApiUrl("search");
        RequestParams params = new RequestParams();
        params.put("term", term);
        // need to get cll from phone
        params.put("cll", "37.7867703362929,-122.399958372115");
        params.put("location", location);
        // need to update this limit
        params.put("limit", "20");
        params.put("lang", "en");
        client.get(apiUrl, params, handler);
    }
}