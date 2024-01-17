package com.example.tripster.model.view;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.tripster.model.enums.NotificationStatus;
import com.example.tripster.model.enums.NotificationType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Notification implements Parcelable {

    private Long id;

    private String title;

    private String text;

    private String type;

    private String status;

    private String timeStamp;

    private Long userId;

    protected Notification(Parcel in) {
        id = in.readLong();
        title = in.readString();
        text = in.readString();
        type = in.readString();
        status = in.readString();
        timeStamp = in.readString();
        userId = in.readLong();
    }

    public NotificationStatus getStatus() {
        return NotificationStatus.valueOf(status);
    }

    public NotificationType getType() {
        return NotificationType.valueOf(type);
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
        dest.writeString(text);
        dest.writeString(type);
        dest.writeString(status);
        dest.writeString(timeStamp);
        dest.writeLong(userId);
    }

}
