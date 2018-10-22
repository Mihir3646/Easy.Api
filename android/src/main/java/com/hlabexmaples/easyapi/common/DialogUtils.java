package com.hlabexmaples.easyapi.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import com.hlabexmaples.easyapi.R;

/**
 * Purpose of this Class is to display different dialog in application.
 */
public class DialogUtils {

  /**
   * Displays alert dialog to show common messages.
   *
   * @param message Message to be shown in the Dialog displayed
   * @param context Context of the Application, where the Dialog needs to be displayed
   */
  public static void displayDialog(final Context context, final String message) {

    final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
    alertDialog.setTitle(context.getString(R.string.app_name));
    alertDialog.setCancelable(false);

    alertDialog.setMessage(message);
    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, context.getString(android.R.string.ok),
        (dialog, which) -> dialog.dismiss());

    if (!((Activity) context).isFinishing()) {
      alertDialog.show();
    }
  }

  /**
   * Displays alert dialog to show common messages.
   *
   * @param message Message to be shown in the Dialog displayed
   * @param context Context of the Application, where the Dialog needs to be displayed
   */
  public static void displayDialogSettings(final Context context, final String message) {

    final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
    alertDialog.setTitle(context.getString(R.string.app_name));
    alertDialog.setCancelable(false);

    alertDialog.setMessage(message);
    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, context.getString(R.string.cancel),
        (dialog, which) -> {
          context.startActivity(new Intent(android.provider.Settings.ACTION_SECURITY_SETTINGS));
          dialog.dismiss();
        });
    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, context.getString(R.string.settings),
        (dialog, which) -> dialog.dismiss());

    if (!((Activity) context).isFinishing()) {
      alertDialog.show();
    }
  }

  /**
   * Displays toast message
   *
   * @param mContext requires to create Toast
   */
  public static void showToast(final Context mContext, final String message) {
    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
  }
}
