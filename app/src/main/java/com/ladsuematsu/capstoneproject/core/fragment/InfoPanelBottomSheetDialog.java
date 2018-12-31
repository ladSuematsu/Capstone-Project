package com.ladsuematsu.capstoneproject.core.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ladsuematsu.capstoneproject.R;
import com.ladsuematsu.capstoneproject.util.UiUtils;

public abstract class InfoPanelBottomSheetDialog extends BottomSheetDialogFragment {

    private final View.OnClickListener dismissButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dismiss();
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_info_panel, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView panelTitle = view.findViewById(R.id.panel_title);
        ImageButton panelButtonDismiss = view.findViewById(R.id.panel_button_dismiss);

        getDialog().setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                View dialogView = ((BottomSheetDialog) dialog).findViewById(android.support.design.R.id.design_bottom_sheet);

                UiUtils.setBottomSheetDialogPeekHeight(dialogView, 2);
            }
        });

        setPanelTitle(panelTitle);
        panelButtonDismiss.setOnClickListener(dismissButtonClickListener);

        FrameLayout content = view.findViewById(R.id.content);
        inflateContent(content, getLayoutInflater());
    }

    protected abstract void inflateContent(ViewGroup content, LayoutInflater inflater);

    protected abstract void setPanelTitle(TextView title);
}
