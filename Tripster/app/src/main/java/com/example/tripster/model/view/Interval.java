package com.example.tripster.model.view;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.tripster.adapters.LocalDateAdapter;
import com.example.tripster.util.DateTimeUtil;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Interval implements Parcelable{

    @JsonAdapter(LocalDateAdapter.class)
    @SerializedName("start")
    private LocalDate start;

    @JsonAdapter(LocalDateAdapter.class)
    @SerializedName("end")
    private LocalDate end;

    private double price;

    protected Interval(Parcel in) {
        start = DateTimeUtil.toLocalDate(in.readString());
        end = DateTimeUtil.toLocalDate(in.readString());
        price = in.readDouble();
    }

    public String getStart() {
        return DateTimeUtil.toString(start);
    }

    public String getEnd() {
        return DateTimeUtil.toString(end);
    }

    public String getPrice() {
        return String.valueOf(price);
    }

    public static final Parcelable.Creator<Interval> CREATOR = new Parcelable.Creator<Interval>() {
        @Override
        public Interval createFromParcel(Parcel in) {
            return new Interval(in);
        }

        @Override
        public Interval[] newArray(int size) {
            return new Interval[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(DateTimeUtil.toString(start));
        dest.writeString(DateTimeUtil.toString(end));
        dest.writeDouble(price);
    }
    
}
