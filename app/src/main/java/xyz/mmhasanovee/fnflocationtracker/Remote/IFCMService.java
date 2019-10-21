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
            "Authorization:key="

    })

    @POST("fcm/send")
    Observable<MyResponse> sendFriendRequestToUser(@Body Request body);
}
