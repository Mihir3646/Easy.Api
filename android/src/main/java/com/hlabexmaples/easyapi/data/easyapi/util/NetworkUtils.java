package com.hlabexmaples.easyapi.data.easyapi.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.Gson;
import com.hlabexmaples.easyapi.R;
import com.hlabexmaples.easyapi.common.DialogUtils;
import com.hlabexmaples.easyapi.data.easyapi.main.model.ModelError;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.HttpException;


/**
 * Purpose of this Class is to check internet connection of phone and perform actions on user's input
 */
public class NetworkUtils {


    /**
     * Checks internet network connection.
     *
     * @param context    Activity context
     * @param message    if want to show connection message to user then true, false otherwise.
     * @param goSettings if want to go action setting for connection then true, otherwise only OK button.
     * @return if network connectivity exists or is in the process of being established, false otherwise.
     */
    public static boolean isOnline(final Activity context, boolean message, boolean goSettings) {
        if (context != null && !context.isFinishing()) {
            if (isNetworkOn(context)) {
                return true;
            }

            if (message) {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(context);

                dialog.setTitle(context.getString(R.string.app_name));
                dialog.setCancelable(false);
                dialog.setMessage(context.getString(R.string.alert_no_connection));

                if (goSettings) {
                    dialog.setPositiveButton(context.getString(R.string.settings), (dialog1, id) -> {
                        dialog1.dismiss();
                        context.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
                    });

                    dialog.setNegativeButton(context.getString(R.string.cancel), (dialog12, id) -> dialog12.dismiss());

                } else {
                    dialog.setNeutralButton(context.getString(R.string.ok), (dialog13, id) -> dialog13.dismiss());
                }
                dialog.show();
                return false;
            }
        }
        return false;
    }

    /**
     * Checks the Network availability.
     *
     * @return true if internet available, false otherwise
     */
    public static boolean isNetworkOn(Context context) {
        final ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetwork = cm != null ? cm.getActiveNetworkInfo() : null;

        return (activeNetwork != null && activeNetwork.isConnectedOrConnecting());
    }

    /**
     * Checks the Network availability
     *
     * @return observable to check internet available
     */
    public static Observable<Boolean> isNetworkAvailable(Context context) {
        return Observable.just(isNetworkOn(context));
    }

    /**
     * Show common api error
     */
    public static void showApiError(Context context) {
        DialogUtils.showToast(context, context.getString(R.string.alert_message_error));
    }

    /**
     * Handle network errors like PageNotFound, ServerError etc.
     *
     * @param e exception
     * @return error message to display
     */
    public static String handleErrorResponse(Throwable e) {
        if (e instanceof HttpException) {
            final ResponseBody errorBody = ((HttpException) e).response().errorBody();
            if (errorBody != null) {
                try {
                    final String errMsg = errorBody.string();
                    final ModelError modelError = new Gson().fromJson(errMsg, ModelError.class);
                    return modelError.getStatus().getMsg();
                } catch (Exception ignored) {
                }
            }
        }
        return "Something went wrong, Try again";
    }
}
