package pointer.wbc.com.billiardspointer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextPaint;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.github.codefalling.recyclerviewswipedismiss.SwipeDismissRecyclerViewTouchListener;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import pointer.wbc.com.billiardspointer.model.Game;
import pointer.wbc.com.billiardspointer.util.Util;
import pointer.wbc.com.billiardspointer.view.HistoryGameItemView;
import pointer.wbc.com.billiardspointer.view.SwipeRefreshCustom;

/**
 * Created by Dalton on 15. 5. 19..
 */
public class GameListActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    @InjectView(R.id.recycler)
    RecyclerView recycler;
    @InjectView(R.id.swipe)
    SwipeRefreshCustom swipe;
    private RealmResults<Game> result;
    private final GameAdapter adapter = new GameAdapter();
    private int margin;
    private String ga = "loading";
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        realm = Realm.getInstance(context);

        setContentView(R.layout.activity_game_list);
        ButterKnife.inject(this);

        SwipeDismissRecyclerViewTouchListener listener = new SwipeDismissRecyclerViewTouchListener.Builder(
                recycler,
                new SwipeDismissRecyclerViewTouchListener.DismissCallbacks() {
                    @Override
                    public boolean canDismiss(int position) {
                        return true;
                    }

                    @Override
                    public void onDismiss(View view) {
                        // Do what you want when dismiss
                        if (result == null || adapter == null) return;
                        int position = recycler.getChildAdapterPosition(view);
                        Realm realm = Realm.getInstance(context);
                        realm.beginTransaction();
                        result.get(position).setDeleteCandidate(true);
                        realm.commitTransaction();
                        adapter.notifyDataSetChanged();

                        Toast toast = new Toast(context);
                        View v = LayoutInflater.from(context).inflate(R.layout.delete_confirm_toast, null);
                        v.findViewById(R.id.undo).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                realm.beginTransaction();
                                result.get(position).setDeleteCandidate(false);
                                realm.commitTransaction();
                                adapter.notifyDataSetChanged();
                            }
                        });
                        toast.setView(v);
                        toast.show();
                    }
                })
                .setIsVertical(false)
//                .setItemTouchCallback(
//                        new SwipeDismissRecyclerViewTouchListener.OnItemTouchCallBack() {
//                            @Override
//                            public void onTouch(int index) {
//                                // Do what you want when item be touched
//                            }
//                        })
                .create();
        recycler.setOnTouchListener(listener);

        final Rect r = new Rect(0, 0, getResources().getDisplayMetrics().widthPixels, Util.dpToPx(50));
        final Paint p = new Paint();
        p.setColor(0x20000000);
        p.setStyle(Paint.Style.FILL);
        p.setAntiAlias(true);
        p.setDither(true);

        final Paint tp = new TextPaint();
        tp.setColor(0xffffffff);
        tp.setStyle(Paint.Style.FILL);
        tp.setAntiAlias(true);
        tp.setDither(true);
        tp.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));

        recycler.setLayoutManager(new LinearLayoutManager(context));
        recycler.setAdapter(adapter);
        recycler.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
                c.drawRect(r, p);
                String text = "GrandAverage  " + ga;
                float width = tp.measureText(text) / 2f;
                c.drawText(text, r.centerX() - width, r.centerY() + tp.getTextSize() / 2f, tp);
            }

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                margin = Util.dpToPx(10);
                outRect.set(margin, margin, margin, 0);
            }
        });
        recycler.setPadding(0, Util.dpToPx(50), 0, Util.dpToPx(20));

        swipe.setOnRefreshListener(this);
        swipe.post(() -> swipe.setRefreshing(true));
        onRefresh();
    }

    @Override
    public void onRefresh() {
        RealmQuery<Game> query = realm.where(Game.class);
        ga = String.format("%.3f", query.equalTo("deleteCandidate", false).averageFloat("average"));

        query = realm.where(Game.class);
        result = query.findAllSorted("createTime", false);
        swipe.post(() -> swipe.setRefreshing(false));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RealmResults<Game> deleteCandidates = realm.where(Game.class).equalTo("deleteCandidate", true).findAll();
        if (deleteCandidates.size() > 0) {
            realm.beginTransaction();
            deleteCandidates.clear();
            realm.commitTransaction();
        }
    }

    class Holder extends RecyclerView.ViewHolder {
        private final HistoryGameItemView view;

        public Holder(View itemView) {
            super(itemView);
            this.view = (HistoryGameItemView) itemView;
        }
    }

    private class GameAdapter extends RecyclerView.Adapter<Holder> {
        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(new HistoryGameItemView(context));
        }

        @Override
        public void onBindViewHolder(Holder holder, int position) {
            holder.view.setData(result.get(position));
        }

        @Override
        public int getItemCount() {
            if (result != null) {
                return result.size();
            }
            return 0;
        }
    }
}