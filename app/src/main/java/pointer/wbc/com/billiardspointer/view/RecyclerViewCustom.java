package pointer.wbc.com.billiardspointer.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import pointer.wbc.com.billiardspointer.R;
import pointer.wbc.com.billiardspointer.listener.LoadMoreListener;

/**
 * Created by Dalton on 15. 1. 25..
 */
public class RecyclerViewCustom extends RecyclerView {
    private final RecycleAdapterWrapper wrapper = new RecycleAdapterWrapper();
    private int[] positions = new int[2];
    private LoadMoreListener loadMoreListener;

    public RecyclerViewCustom(Context context) {
        this(context, null);
    }

    public RecyclerViewCustom(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecyclerViewCustom(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setAdapter(wrapper);
    }

    public void setLoadMoreListener(LoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (adapter instanceof RecycleAdapterWrapper) {
            super.setAdapter(adapter);
        } else {
            ((RecycleAdapterWrapper) getAdapter()).setInnerAdapter(adapter);
        }
    }

    public int getFirstVisiblePosition() {
        if (getLayoutManager() instanceof StaggeredGridLayoutManager) {
            ((StaggeredGridLayoutManager) getLayoutManager()).findFirstVisibleItemPositions(positions);
            return positions[0];
        }
        return 0;
    }

    public void setSelection(int position) {
        scrollToPosition(position);
    }

    final public static class HeaderFooterHolder extends RecyclerView.ViewHolder {

        private static final StaggeredGridLayoutManager.LayoutParams params = new StaggeredGridLayoutManager.LayoutParams(StaggeredGridLayoutManager.LayoutParams.MATCH_PARENT, StaggeredGridLayoutManager.LayoutParams.WRAP_CONTENT);

        public HeaderFooterHolder(View itemView) {
            super(itemView);
            StaggeredGridLayoutManager.LayoutParams newParams = new StaggeredGridLayoutManager.LayoutParams(HeaderFooterHolder.params);
            newParams.setFullSpan(true);
            itemView.setLayoutParams(newParams);
        }
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

    private class RecycleAdapterWrapper extends RecyclerView.Adapter {
        private static final String HEADER = "header_";
        private static final String FOOTER = "footer_";
        private RecyclerView.Adapter innerAdapter = new Adapter() {
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return null;
            }

            @Override
            public void onBindViewHolder(ViewHolder holder, int position) {
            }

            @Override
            public int getItemCount() {
                return 0;
            }
        };
        private final List<View> headerViews = new ArrayList<>();
        private final List<View> footerViews = new ArrayList<>();
        private int headerSize;
        private int itemCount;
        private int footerSize;

        public RecycleAdapterWrapper() {
        }

        public void setInnerAdapter(RecyclerView.Adapter innerAdapter) {
            this.innerAdapter = innerAdapter;
            this.innerAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                @Override
                public void onChanged() {
                    RecycleAdapterWrapper.this.notifyDataSetChanged();
                }

                @Override
                public void onItemRangeChanged(int positionStart, int itemCount) {
                    RecycleAdapterWrapper.this.notifyItemRangeChanged(positionStart, itemCount);
                }

                @Override
                public void onItemRangeInserted(int positionStart, int itemCount) {
                    RecycleAdapterWrapper.this.notifyItemRangeInserted(positionStart, itemCount);
                }

                @Override
                public void onItemRangeRemoved(int positionStart, int itemCount) {
                    RecycleAdapterWrapper.this.notifyItemRangeRemoved(positionStart, itemCount);
                }

                @Override
                public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                    RecycleAdapterWrapper.this.notifyDataSetChanged();
                }
            });
            innerAdapter.notifyDataSetChanged();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            for (View view : headerViews) {
                if (view.getTag(R.id.tag) != null && Integer.valueOf(view.getTag(R.id.tag).toString()) == viewType) {
                    return new HeaderFooterHolder(view);
                }
            }
            for (View view : footerViews) {
                if (view.getTag(R.id.tag) != null && Integer.valueOf(view.getTag(R.id.tag).toString()) == viewType) {
                    return new HeaderFooterHolder(view);
                }
            }
            return innerAdapter.onCreateViewHolder(parent, viewType);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (!(holder instanceof HeaderFooterHolder)) {
                position -= headerSize;
                innerAdapter.onBindViewHolder(holder, position);
            }
            if (loadMoreListener != null && !(holder instanceof HeaderFooterHolder) && position >= innerAdapter.getItemCount() - 1) {
                loadMoreListener.loadMoreIfNeed();
            }
        }

        @Override
        public int getItemViewType(int position) {
//            if (innerAdapter instanceof FeedAdapter && ((FeedAdapter) innerAdapter).hideHeaderFooter() && itemCount == 0 && position == 0) {
//                int hash = (FOOTER + position).hashCode();
//                footerViews.get(position).setTag(R.id.tag, hash);
//                return hash;
//            }
            if (position < headerSize) {
                int hash = (HEADER + position).hashCode();
                headerViews.get(position).setTag(R.id.tag, hash);
                return hash;
            } else if (position >= headerSize + itemCount) {
                position -= (headerSize + itemCount);
                int hash = (FOOTER + position).hashCode();
                footerViews.get(position).setTag(R.id.tag, hash);
                return hash;
            } else {
                position -= headerSize;
                return innerAdapter.getItemViewType(position);
            }
        }

        @Override
        public int getItemCount() {
            headerSize = headerViews.size();
            itemCount = innerAdapter.getItemCount();
            footerSize = footerViews.size();
//            if (innerAdapter instanceof FeedAdapter && ((FeedAdapter) innerAdapter).hideHeaderFooter() && itemCount == 0) {
//                return footerSize;
//            }
            return headerSize + itemCount + footerSize;
        }

        protected void addHeaderView(View v) {
            if (headerViews.indexOf(v) == -1) {
                headerViews.add(v);
                innerAdapter.notifyItemInserted(headerViews.size() - 1);
            }
        }

        protected void removeHeaderView(View v) {
            int position = headerViews.indexOf(v);
            if (position != -1) {
                headerViews.remove(v);
                innerAdapter.notifyItemRemoved(position);
            }
        }

        protected void addFooterView(View v) {
            if (footerViews.indexOf(v) == -1) {
                footerViews.add(v);
                innerAdapter.notifyItemInserted(headerSize + itemCount + footerViews.size() - 1);
            }
        }

        protected void removeFooterView(View v) {
            int position = footerViews.indexOf(v);
            if (position != -1) {
                footerViews.add(v);
                innerAdapter.notifyItemRemoved(headerSize + itemCount + position);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public boolean canScrollVertically(int direction) {
        if (getChildCount() == 0) {
            return false;
        }
        // check if scrolling up
        if (direction < 1) {
            boolean original = super.canScrollVertically(direction);
            return !original && getChildAt(0) != null && getChildAt(0).getTop() < getPaddingTop() || original;
        }
        return super.canScrollVertically(direction);
    }

    public void addHeaderView(View v) {
        wrapper.addHeaderView(v);
    }

    public void removeHeaderView(View v) {
        wrapper.removeHeaderView(v);
    }

    public void addFooterView(View v) {
        wrapper.addFooterView(v);
    }

    public void removeFooterView(View v) {
        wrapper.removeFooterView(v);
    }
}