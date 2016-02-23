package com.delta.pragyan16;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by HP on 23-02-2016.
 */
public class fTextView extends TextView {


        public fTextView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            init();
        }

        public fTextView(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        public fTextView(Context context) {
            super(context);
            init();
        }

        private void init() {
            if (!isInEditMode()) {
                Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Cabin.ttf");
                setTypeface(tf);
            }


    }

}
