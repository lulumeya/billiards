package pointer.wbc.com.billiardspointer.view.pressable;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * Created by Dalton on 2014. 8. 16..
 */
public class DefaultImageView extends ImageView {

    private boolean clickListenerRegistered = false;

    public DefaultImageView(Context context) {
        this(context, null);
    }

    public DefaultImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DefaultImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setClickable(true);
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        super.setOnClickListener(l);
        if (l != null) {
            clickListenerRegistered = true;
        } else {
            clickListenerRegistered = false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (clickListenerRegistered) {
            int action = event.getAction() & MotionEvent.ACTION_MASK;
            if (action == MotionEvent.ACTION_DOWN) {
                setAlpha(0.6f);
            } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_OUTSIDE || action == MotionEvent.ACTION_CANCEL) {
                setAlpha(1f);
            }
        }
        return super.onTouchEvent(event);
    }
}