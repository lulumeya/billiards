package pointer.wbc.com.billiardspointer;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import pointer.wbc.com.billiardspointer.model.Game;
import pointer.wbc.com.billiardspointer.util.Util;
import pointer.wbc.com.billiardspointer.view.GameItemView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.swipe_recycler_base);
        ButterKnife.inject(this);

        recycler.setLayoutManager(new LinearLayoutManager(context));
        recycler.setAdapter(adapter);
        recycler.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                margin = Util.dpToPx(10);
                outRect.set(margin, margin, margin, 0);
            }
        });
        recycler.setPadding(0, 0, 0, Util.dpToPx(20));

        swipe.setOnRefreshListener(this);
        swipe.post(() -> swipe.setRefreshing(true));
        onRefresh();
    }

    @Override
    public void onRefresh() {
        Realm realm = Realm.getInstance(context);
        RealmQuery<Game> query = realm.where(Game.class);
        result = query.findAllSorted("createTime", false);
        swipe.post(() -> swipe.setRefreshing(false));
    }

    class Holder extends RecyclerView.ViewHolder {
        private final GameItemView view;

        public Holder(View itemView) {
            super(itemView);
            this.view = (GameItemView) itemView;
        }
    }

    private class GameAdapter extends RecyclerView.Adapter<Holder> {
        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new Holder(new GameItemView(context));
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