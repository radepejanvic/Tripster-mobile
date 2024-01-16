package com.example.tripster.model.view;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.tripster.model.enums.ReservationStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class UserReport implements Parcelable {
    private Long id;
    private String reason;
    private Long reporterId;
    private String reporterEmail;
    private Long reporteeId;
    private String reporteeEmail;
    protected UserReport(Parcel in) {

        reason = in.readString();
        reporterId = in.readLong();
        reporterEmail = in.readString();
        reporteeId = in.readLong();
        reporteeEmail = in.readString();
        this.id = in.readLong();

    }

    public static final Parcelable.Creator<UserReport> CREATOR = new Parcelable.Creator<UserReport>() {
        @Override
        public UserReport createFromParcel(Parcel in) {
            return new UserReport(in);
        }

        @Override
        public UserReport[] newArray(int size) {
            return new UserReport[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(reason);
        dest.writeLong(reporterId);
        dest.writeString(reporterEmail);
        dest.writeLong(reporteeId);
        dest.writeString(reporteeEmail);
        dest.writeLong(id);

    }
}
