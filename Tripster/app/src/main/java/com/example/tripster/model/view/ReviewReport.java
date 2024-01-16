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
public class ReviewReport implements Parcelable {
    private Long id;

    private String reason;
    private int rate;
    private String comment;
    private String reporterEmail;
    private String name;
    protected ReviewReport(Parcel in) {
        id = in.readLong();
        reason = in.readString();
        reporterEmail = in.readString();
        name = in.readString();
        rate = in.readInt();
        comment = in.readString();
    }

    public static final Creator<ReviewReport> CREATOR = new Creator<ReviewReport>() {
        @Override
        public ReviewReport createFromParcel(Parcel in) {
            return new ReviewReport(in);
        }

        @Override
        public ReviewReport[] newArray(int size) {
            return new ReviewReport[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(reason);
        dest.writeString(reporterEmail);
        dest.writeString(name);
        dest.writeInt(rate);
        dest.writeDouble(rate);
        dest.writeString(comment);
    }
}
