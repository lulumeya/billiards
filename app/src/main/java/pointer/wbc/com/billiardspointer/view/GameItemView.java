package pointer.wbc.com.billiardspointer.view;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import pointer.wbc.com.billiardspointer.R;
import pointer.wbc.com.billiardspointer.model.Game;

/**
 * Created by Dalton on 15. 5. 19..
 */
public class GameItemView extends CardView {
    public static final SimpleDateFormat FULL_DATE = new SimpleDateFormat("yyyy.M.d a HH:mm");
    @InjectView(R.id.startTime)
    PrefixTextView startTime;
    @InjectView(R.id.endTime)
    PrefixTextView endTime;
    @InjectView(R.id.points)
    PrefixTextView points;
    @InjectView(R.id.inning)
    PrefixTextView inning;
    @InjectView(R.id.highrun)
    PrefixTextView highrun;
    @InjectView(R.id.average)
    PrefixTextView average;
    @InjectView(R.id.won)
    TextView won;
    private Game data;

    public GameItemView(Context context) {
        this(context, null);
    }

    public GameItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GameItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.game_item_view, this, true);
        ButterKnife.inject(this);
    }

    public void setData(Game data) {
        this.data = data;
        startTime.setText(FULL_DATE.format(new Date(data.getCreateTime())));
        endTime.setText(FULL_DATE.format(new Date(data.getLastScoreTime())));
        points.setText(data.getPoint() + "점");
        inning.setText(data.getInning() + "이닝");
        highrun.setText(data.getHighrun() + "점 ");
        average.setText(String.format("%.3f", data.getAverage()));
        won.setText(data.isWon() ? "Win!!" : "Loose..");
    }

    public Game getData() {
        return data;
    }
}