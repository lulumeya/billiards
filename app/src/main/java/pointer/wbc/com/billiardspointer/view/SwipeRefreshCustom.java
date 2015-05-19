package pointer.wbc.com.billiardspointer.view;

import android.content.Context;
import android.os.Build;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Dalton on 15. 1. 26..
 */
public class SwipeRefreshCustom extends SwipeRefreshLayout {
    public SwipeRefreshCustom(Context context) {
        this(context, null);
    }

    public SwipeRefreshCustom(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private float xDistance, yDistance, lastX, lastY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDistance = yDistance = 0f;
                lastX = ev.getX();
                lastY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float curX = ev.getX();
                final float curY = ev.getY();
                xDistance += Math.abs(curX - lastX);
                yDistance += Math.abs(curY - lastY);
                lastX = curX;
                lastY = curY;
                if (xDistance > yDistance)
                    return false;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean canChildScrollUp() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            if (getChildCount() > 0) {
                View recyclerView = getChildAt(0);
                boolean isRecyclerView = recyclerView instanceof RecyclerViewCustom;
                if (isRecyclerView) {
                    boolean hasChild = ((RecyclerViewCustom) recyclerView).getChildCount() > 0;
                    if (hasChild) {
                        int firstVisiblePosition = ((RecyclerViewCustom) recyclerView).getFirstVisiblePosition();
                        int top = ((RecyclerViewCustom) recyclerView).getChildAt(0).getTop();
                        boolean scrolledMoreThanOne = firstVisiblePosition > 0;
                        boolean scrolledMoreThanPadding = top < recyclerView.getPaddingTop();
                        return scrolledMoreThanOne || scrolledMoreThanPadding;
                    }
                }
            }
            return false;
        } else {
            return getChildAt(0).canScrollVertically(-1);
        }
    }
}