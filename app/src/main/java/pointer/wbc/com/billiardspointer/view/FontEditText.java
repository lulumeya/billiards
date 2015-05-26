package pointer.wbc.com.billiardspointer.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.EditText;

import net.kianoni.fontloader.FontLoader;

/**
 * Created by Haksu on 2015-05-27.
 */
public class FontEditText extends EditText {
    public FontEditText(Context context) {
        super(context);
    }

    public FontEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.readAttributes(context, attrs);
    }

    public FontEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.readAttributes(context, attrs);
    }

    @TargetApi(21)
    public FontEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.readAttributes(context, attrs);
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