package com.vlad.lesson6_maskaikin;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class Fragment3ImageAndText extends Fragment {

    static final String ARGUMENT_IMAGE = "argument_image";
    static final String ARGUMENT_TEXT = "argument_text";

    private GetStringFromClickedImage getStringFromClickedImage;
    private int img;
    private String text;


    public Fragment3ImageAndText() {

    }

    public static Fragment3ImageAndText getInstance(Banner banner) {
        Fragment3ImageAndText fragment3ImageAndText = new Fragment3ImageAndText();
        Bundle arguments = new Bundle();
        arguments.putInt(ARGUMENT_IMAGE, banner.getImageBanner());
        arguments.putString(ARGUMENT_TEXT, banner.getStringBanner());
        fragment3ImageAndText.setArguments(arguments);
        return fragment3ImageAndText;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        getStringFromClickedImage = (GetStringFromClickedImage) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().getInt(ARGUMENT_IMAGE) != 0 || getArguments().getString(ARGUMENT_TEXT) != null) {
            img = getArguments().getInt(ARGUMENT_IMAGE);
            text = getArguments().getString(ARGUMENT_TEXT);
        } else {
            img = R.drawable.ic_launcher_foreground;
            text = getString(R.string.error_image);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_3_image_and_text, container, false);

        ImageView imageView = view.findViewById(R.id.imageViewFragment3);
        imageView.setImageResource(img);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        TextView textView = view.findViewById(R.id.textViewFragment3);
        textView.setText(text);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getStringFromClickedImage.getStringFromClickedImage(text);
            }
        });

        return view;
    }

}
