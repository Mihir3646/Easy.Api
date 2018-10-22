package com.hlabexmaples.easyapi.data.webservice;

import android.content.Context;
import android.support.annotation.NonNull;
import com.hlabexmaples.easyapi.BuildConfig;
import java.io.File;
import java.util.concurrent.TimeUnit;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Webservice utility class.
 */

public class ApiFactory {

  private static ApiRepository wsCallback;

  /**
   * Static method to to get api client instance
   *
   * @return ApiCallback instance
   */
  public static ApiRepository getInstance() {

    try {
      if (wsCallback == null) {

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(Constants.WS_CONNECTION_TIMEOUT, TimeUnit.SECONDS);
        httpClient.readTimeout(Constants.WS_READ_TIMEOUT, TimeUnit.SECONDS);

        if (BuildConfig.DEBUG) {
          HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
          logging.setLevel(HttpLoggingInterceptor.Level.BODY);
          httpClient.addInterceptor(logging);
        }

        Retrofit client = new Retrofit.Builder()
            .baseUrl(Constants.WS_BASE_URL)
            .client(httpClient.build())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();
        wsCallback = client.create(ApiRepository.class);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return wsCallback;
  }

  @NonNull
  public static MultipartBody.Part getFileMultiPart(final Context context, final String partName,
      final File file) {

    // create RequestBody instance from file
    RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);

    // MultipartBody.Part is used to send also the actual file name
    return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
  }

  public static void resetClient() {
    wsCallback = null;
  }
}