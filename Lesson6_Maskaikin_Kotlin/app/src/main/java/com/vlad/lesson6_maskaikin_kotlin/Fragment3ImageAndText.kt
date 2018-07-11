package com.vlad.lesson6_maskaikin_kotlin


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView


class Fragment3ImageAndText : Fragment() {

    private lateinit var getStringFromClickedImage: GetStringFromClickedImage
    private var img: Int = 0
    private lateinit var text: String

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        getStringFromClickedImage = context as GetStringFromClickedImage
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments!!.getInt(ARGUMENT_IMAGE) != 0 || arguments!!.getString(ARGUMENT_TEXT) != null) {
            img = arguments!!.getInt(ARGUMENT_IMAGE)
            text = arguments!!.getString(ARGUMENT_TEXT)
        } else {
            img = R.drawable.ic_launcher_foreground
            text = getString(R.string.error_image)
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_3_image_and_text, container, false)

        val imageView = view.findViewById<ImageView>(R.id.imageViewFragment3)
        imageView.setImageResource(img)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        val textView = view.findViewById<TextView>(R.id.textViewFragment3)
        textView.text = text
        imageView.setOnClickListener { getStringFromClickedImage.getStringFromClickedImage(text) }

        return view
    }

    companion object {

        internal const val ARGUMENT_IMAGE = "argument_image"
        internal const val ARGUMENT_TEXT = "argument_text"

        fun getInstance(banner: Banner): Fragment3ImageAndText {
            val fragment3ImageAndText = Fragment3ImageAndText()
            val arguments = Bundle()
            arguments.putInt(ARGUMENT_IMAGE, banner.imageBanner)
            arguments.putString(ARGUMENT_TEXT, banner.stringBanner)
            fragment3ImageAndText.arguments = arguments
            return fragment3ImageAndText
        }
    }

}
