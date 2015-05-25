package pointer.wbc.com.billiardspointer.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.phillipcalvin.iconbutton.IconButton;

import net.kianoni.fontloader.FontLoader;

/**
 * Created by Haksu on 2015-05-13.
 */
public class FontIconButton extends IconButton {
    public FontIconButton(Context context) {
        super(context);
    }

    public FontIconButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        readAttributes(context, attrs);
    }

    public FontIconButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        readAttributes(context, attrs);
    }

    private void readAttributes(Context context, AttributeSet attrs) {
        TypedArray styledAttributes = context.obtainStyledAttributes(attrs, net.kianoni.fontloader.R.styleable.TextView);
        String fontFile = null;
        String fontFamily = null;
        String fontVariant = null;
        String fontFilePattern = null;
        int N = styledAttributes.getIndexCount();

        for (int i = 0; i < N; ++i) {
            int attr = styledAttributes.getIndex(i);
            if (net.kianoni.fontloader.R.styleable.TextView_fontFile == attr) {
                fontFile = styledAttributes.getString(attr);
            }

            if (net.kianoni.fontloader.R.styleable.TextView_fontFamily == attr) {
                fontFamily = styledAttributes.getString(attr);
            }

            if (net.kianoni.fontloader.R.styleable.TextView_fontVariant == attr) {
                fontVariant = styledAttributes.getString(attr);
            }

            if (net.kianoni.fontloader.R.styleable.TextView_fontFilePattern == attr) {
                fontFilePattern = styledAttributes.getString(attr);
            }
        }

        styledAttributes.recycle();
        if (fontFamily != null && fontVariant != null && fontFilePattern != null) {
            if (fontFile != null) {
                throw new RuntimeException("Attempting to set fontFile together with fontFilePattern");
            }

            this.setTypeface(FontLoader.getInstance().getTypeFace(context, fontFamily, fontVariant, fontFilePattern));
        }

        if (fontFile != null) {
            this.setTypeface(FontLoader.getInstance().getTypeFace(context, fontFile));
        }
    }
}