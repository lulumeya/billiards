package pointer.wbc.com.billiardspointer.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.TypedValue;

import net.kianoni.fontloader.TextView;

import pointer.wbc.com.billiardspointer.R;

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
        prefixColor = a.getColor(R.styleable.PrefixTextView_prefixColor, 0xff303030);
        prefixSize = Math.round(a.getDimension(R.styleable.PrefixTextView_prefixSize, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 13, context.getResources().getDisplayMetrics())));
        a.recycle();

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
            builder.setSpan(new AbsoluteSizeSpan(Math.round(getTextSize()), false), length, builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            super.setText(builder, BufferType.SPANNABLE);
        } else {
            super.setText(text, type);
        }
    }
}