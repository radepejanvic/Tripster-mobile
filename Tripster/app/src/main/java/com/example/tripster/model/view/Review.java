package com.example.tripster.model.view;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Review implements Parcelable {

    private Long id;
    private String title;

    private String timeStamp;
    private double rate;
    private String comment;

    private Long reviewerId;
    private String reviewerName;
    private String reviewerSurname;

    private Long reviewedId;


    protected Review(Parcel in) {
        id = in.readLong();
        title = in.readString();
        reviewerName = in.readString();
        reviewerSurname = in.readString();
        timeStamp = in.readString();
        rate = in.readDouble();
        comment = in.readString();
    }

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(reviewerName);
        dest.writeString(reviewerSurname);
        dest.writeString(timeStamp);
        dest.writeDouble(rate);
        dest.writeString(comment);
    }
}
