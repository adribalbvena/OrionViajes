package ar.edu.ort.orionviajes.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class UserDto(
    @field:SerializedName("email")
    val email: String,
    @field:SerializedName("password")
    val password: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(email)
        parcel.writeString(password)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserDto> {
        override fun createFromParcel(parcel: Parcel): UserDto {
            return UserDto(parcel)
        }

        override fun newArray(size: Int): Array<UserDto?> {
            return arrayOfNulls(size)
        }
    }
}