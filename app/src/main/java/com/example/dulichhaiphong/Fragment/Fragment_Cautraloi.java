package com.example.dulichhaiphong.Fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.example.dulichhaiphong.R;

import org.w3c.dom.Text;

public class Fragment_Cautraloi extends DialogFragment {
    TextView txtCautraloi;
    ImageView back;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cautraloi,container,false);
        txtCautraloi = (TextView) view.findViewById(R.id.txtTraloi);
        back = (ImageView) view.findViewById(R.id.imgClose);
        Bundle bundle = getArguments();
        if(bundle!=null){
            String txtTraloi = bundle.getString("cauTraloi");
            txtCautraloi.setText(android.text.Html.fromHtml(txtTraloi));
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
    }

}
