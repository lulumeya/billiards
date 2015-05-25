package pointer.wbc.com.billiardspointer.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.TypedValue;

import net.kianoni.fontloader.FontLoader;
import net.kianoni.fontloader.TextView;

import pointer.wbc.com.billiardspointer.Const;
import pointer.wbc.com.billiardspointer.R;
import pointer.wbc.com.billiardspointer.preference.Pref;

/**
 * Created by Haksu on 2015-04-25.
 */
public class PrefixTextView extends TextView {
    private int prefixColor;
    private int prefixSize;
    private String prefix;

    public PrefixTextView(Context context) {
        this(context, null);
    }

    public PrefixTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PrefixTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PrefixTextView);
        prefix = a.getString(R.styleable.PrefixTextView_prefix);

        boolean isLight = false;
        if (!isInEditMode()) {
            isLight = Pref.getBoolean(Const.KEY_THEME, false);
        }
        if (isLight) {
            prefixColor = a.getColor(R.styleable.PrefixTextView_prefixColor, 0xffc1c1c1);
        } else {
            prefixColor = a.getColor(R.styleable.PrefixTextView_prefixColor, 0xff474747);
        }
        prefixSize = Math.round(a.getDimension(R.styleable.PrefixTextView_prefixSize, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 13, context.getResources().getDisplayMetrics())));
        a.recycle();

        readAttributes(context, attrs);

        setText(getText());
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        if (prefix != null) {
            SpannableStringBuilder builder = new SpannableStringBuilder(prefix);
            int length = builder.length();
            builder.setSpan(new ForegroundColorSpan(prefixColor), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setSpan(new AbsoluteSizeSpan(prefixSize, false), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.append(text);
            //builder.setSpan(new AbsoluteSizeSpan(Math.round(getTextSize()), false), length, builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            super.setText(builder, BufferType.SPANNABLE);
        } else {
            super.setText(text, type);
        }
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