package com.ladsuematsu.capstoneproject.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

public class UiUtils {

    public static void hideStatusBar(Dialog dialog) {
        if (dialog != null) {
            hideStatusBar(dialog.getWindow());
        }
    }

    public static void hideStatusBar(Window window) {
        if (Build.VERSION.SDK_INT < 16) {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            View decorView = window.getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    public static void hideSoftInput(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static Snackbar showSnackbar(View view, String message, String actionLabel, int duration, View.OnClickListener listener) {
        final Snackbar snackbar = Snackbar.make(view, message, duration);

        TextView snackbarText = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text);
        snackbarText.setMaxLines(999);

        if (actionLabel != null) {
            if (listener == null) {
                listener = new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                    }
                };
            }

            snackbar.setAction(actionLabel, listener);
        }

        snackbar.show();

        return snackbar;
    }

    public static void triggerSwipeRefreshLayout(final SwipeRefreshLayout view, final SwipeRefreshLayout.OnRefreshListener listener) {
        view.post(new Runnable() {
            @Override
            public void run() {
                view.setRefreshing(true);
                if (listener != null) {
                    listener.onRefresh();
                }
            }
        });
    }

    public static String generatePluralsText(int count, int pluralsResource, int emptyResource, Resources resources) {
        String text;
        if (count > 0 || emptyResource == 0) {
            text = String.format(resources.getQuantityText(pluralsResource, count).toString(), count);
        } else {
            text = resources.getString(emptyResource);
        }

        return text;
    }

    public static DisplayMetrics getDisplayMetrics(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);

        return dm;
    }

    public static Spanned convertFromHtml(String htmlString) {
        Spanned result;
        if (Build.VERSION.SDK_INT < 24) {
            result = Html.fromHtml(htmlString);
        } else {
            result = Html.fromHtml(htmlString, Html.FROM_HTML_MODE_LEGACY);
        }

        return result;
    }

    public static void setBottomSheetDialogPeekHeight(View bottomSheetInternal, int scale) {
        if (bottomSheetInternal == null
            || scale < 1) {
            return;
        }

        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheetInternal);

        if (behavior == null) { return; }

        WindowManager windowManager = (WindowManager) bottomSheetInternal.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);


        behavior.setPeekHeight(displayMetrics.heightPixels / scale);
    }

    public static void expandDialog(View bottomSheetInternal) {
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheetInternal);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private static void cancelDialogDrag(View bottomSheetInternal) {
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheetInternal);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING || newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

    public static DialogInterface.OnShowListener getExpandBottomSheetDialogOnShowListener() {
        return new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialogInterface;
                View bottomSheetInternal = bottomSheetDialog.findViewById(android.support.design.R.id.design_bottom_sheet);
                expandDialog(bottomSheetInternal);
                cancelDialogDrag(bottomSheetInternal);
            }
        };
    }
}
