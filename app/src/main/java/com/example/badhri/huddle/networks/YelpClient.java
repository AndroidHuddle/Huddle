package com.example.badhri.huddle.networks;

/** Return the Call<SearchResponse> so that user can implement the Async / Sync approach */

import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.SearchResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;

public class YelpClient {

    public static void main(String args[]) {
//        YelpClient a = new YelpClient();
//
//        Callback<SearchResponse> callback = new Callback<SearchResponse>() {
//
//            @Override
//            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
//                SearchResponse searchResponse = response.body();
//                ArrayList<Business> businesses = searchResponse.businesses();
//                ArrayList<com.example.badhri.huddle.models.Business> b = com.example.badhri.huddle.models.Business.fromSearchResponse(businesses);
//                Log.d("DEBUG", b.toString());
//            }
//
//            @Override
//            public void onFailure(Call<SearchResponse> call, Throwable t) {
//                Log.d("DEBUG","Failing");
//            }
//        };
//        try {
//            Call<SearchResponse> call = a.getRestaurants("37.7867703362929,-122.399958372115");
//            call.enqueue(callback);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public static final String CONSUMER_KEY = "4O9FjQaJoNOiCLdycjyxTg";
    public static final String CONSUMER_SECRET = "B6nbe5j3eYwxwcoN0vSJVIChX3I";
    public static final String TOKEN = "4kKf-hgQYMgxCwKq-TtrhFntfRK_fXio";
    public static final String TOKEN_SECRET = "VYVKNfFgFmalOkCd8ZWba4ZDTEk";

    private YelpAPIFactory apiFactory;
    private YelpAPI yelpAPI;

    public YelpClient() {
        apiFactory = new YelpAPIFactory(CONSUMER_KEY, CONSUMER_SECRET, TOKEN, TOKEN_SECRET);
        yelpAPI = apiFactory.createAPI();
    }


    // https://api.yelp.com/v2/search?term=food&location=San+Francisco
    // location can be passed in as so cll=latitude,longitude
    private Call<SearchResponse> getRestaurants(String coordinates) throws IOException {
        Map<String, String> params = new HashMap<>();
        params.put("term", "restaurant");
        if (coordinates.length() > 0) {
            params.put("cll", coordinates);
        } else {
            params.put("cll", "37.7867703362929,-122.399958372115");
        }
        params.put("limit", "3");
        params.put("lang", "en");
        Call<SearchResponse> call = yelpAPI.search("San Francisco", params);
        return call;
    }
}

