package xyz.mmhasanovee.fnflocationtracker.Remote;

import android.app.DownloadManager;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import xyz.mmhasanovee.fnflocationtracker.Model.MyResponse;
import xyz.mmhasanovee.fnflocationtracker.Model.Request;

public interface IFCMService {

    @Headers({

        "Content-Type:application/json",
            "Authorization:key=AAAAYYmjsBg:APA91bH9e77tAKU4me1ycgsL6gHj0-PmaISn0_bF_WR1sknbiCxuBSjyviKfTDeoQsbEyktgtWsmHRJkSy9cJomlLia2Vu0V0Z6Eb-Scr9gg-VwJt-U0f4ARhf9KTEsxuCI-qyPZ0yfs"

    })

    @POST("fcm/send")
    Observable<MyResponse> sendFriendRequestToUser(@Body Request body);
}
