package com.example.badhri.huddle.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.yelp.clientlib.entities.Category;
import com.yelp.clientlib.entities.Location;
import com.yelp.clientlib.entities.Review;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Business implements Parcelable {

    private String displayPhone;
    private Double distance;
    private String name;
    private ArrayList<Category> categories;
    private String url;
    private String imageUrl;
    private String id;
    private Boolean isClosed;
    private Location location;
    private String mobileUrl;
    private Double rating;
    private String ratingImgUrl;
    private int reviewCount;
    private ArrayList<Review> reviews;
    private String snippetText;

    public Business() {}

    private static Business fromJson(JSONObject jsonObject) {
        Business b = new Business();

        try {
            b.name = jsonObject.getString("name");
        } catch(Exception e) {}

        try {
            b.categories = (ArrayList<Category>) jsonObject.get("categories");
        } catch(Exception e){}

        try {
            b.displayPhone = jsonObject.getString("display_phone");
        }catch(Exception e){}


        try {
            b.distance = jsonObject.getDouble("distance");
        }catch(Exception e){}


        try {
            b.imageUrl = jsonObject.getString("image_url");
        }catch(Exception e){}

        try {
            b.id = jsonObject.getString("id");
        }catch(Exception e){}

        try {
            b.isClosed = jsonObject.getBoolean("is_closed");
        }catch(Exception e){}

        try {
            b.location = (Location) jsonObject.get("location");
        }catch(Exception e){}

        try {
            b.mobileUrl = jsonObject.getString("mobile_url");
        }catch(Exception e){}

        try {
            b.rating = jsonObject.getDouble("rating");
        }catch(Exception e){}

        try {
            b.ratingImgUrl = jsonObject.getString("rating_img_url");
        }catch(Exception e){}

        try {
            b.reviewCount = jsonObject.getInt("review_count");
        }catch(Exception e){}

        try {
            b.reviews = (ArrayList<Review>) jsonObject.get("reviews");
        }catch(Exception e){}

        try {
            b.snippetText = jsonObject.getString("snippet_text");
        }catch(Exception e){}

        try {
            b.url = jsonObject.getString("url");
        } catch (Exception e){}

        return b;
    }

    public static ArrayList<Business> fromJSONArray(JSONArray businessesJson) {
        ArrayList<Business> b = new ArrayList<>();
        int length = businessesJson.length();
        for (int i = 0; i < length; i++) {

            try {
                b.add(fromJson(businessesJson.getJSONObject(i)));
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

        }
        return b;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.displayPhone);
        dest.writeValue(this.distance);
        dest.writeString(this.name);
        dest.writeList(this.categories);
        dest.writeString(this.url);
        dest.writeString(this.imageUrl);
        dest.writeString(this.id);
        dest.writeValue(this.isClosed);
        dest.writeSerializable(this.location);
        dest.writeString(this.mobileUrl);
        dest.writeValue(this.rating);
        dest.writeString(this.ratingImgUrl);
        dest.writeInt(this.reviewCount);
        dest.writeList(this.reviews);
        dest.writeString(this.snippetText);
    }

    protected Business(Parcel in) {
        this.displayPhone = in.readString();
        this.distance = (Double) in.readValue(Double.class.getClassLoader());
        this.name = in.readString();
        this.categories = new ArrayList<Category>();
        in.readList(this.categories, Category.class.getClassLoader());
        this.url = in.readString();
        this.imageUrl = in.readString();
        this.id = in.readString();
        this.isClosed = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.location = (Location) in.readSerializable();
        this.mobileUrl = in.readString();
        this.rating = (Double) in.readValue(Double.class.getClassLoader());
        this.ratingImgUrl = in.readString();
        this.reviewCount = in.readInt();
        this.reviews = new ArrayList<Review>();
        in.readList(this.reviews, Review.class.getClassLoader());
        this.snippetText = in.readString();
    }

    public static final Parcelable.Creator<Business> CREATOR = new Parcelable.Creator<Business>() {
        @Override
        public Business createFromParcel(Parcel source) {
            return new Business(source);
        }

        @Override
        public Business[] newArray(int size) {
            return new Business[size];
        }
    };
}
