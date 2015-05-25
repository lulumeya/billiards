package pointer.wbc.com.billiardspointer.view.pressable;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Dalton on 2014. 8. 16..
 */
public class DefaultView extends View {

    public DefaultView(Context context) {
        this(context, null);
    }

    public DefaultView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DefaultView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setClickable(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isPressed() && getAlpha() == 1) {
            setAlpha(0.6f);
        } else if (!isPressed() && getAlpha() != 1) {
            setAlpha(1.0f);
        }
        super.onDraw(canvas);
    }
}