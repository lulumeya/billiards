package pointer.wbc.com.billiardspointer.view;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import pointer.wbc.com.billiardspointer.R;
import pointer.wbc.com.billiardspointer.model.Game;
import pointer.wbc.com.billiardspointer.util.Util;

/**
 * Created by Dalton on 15. 5. 19..
 */
public class HistoryGameItemView extends CardView {
    public static final SimpleDateFormat FULL_DATE = new SimpleDateFormat("yyyy.M.d a HH:mm");
    private static final ViewGroup.LayoutParams HIDE_PARAMS = new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, 0);
    private static final ViewGroup.LayoutParams WRAP_PARAMS = new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
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
    @InjectView(R.id.history)
    PrefixTextView history;
    @InjectView(R.id.won)
    TextView won;
    private Game data;
    private final SpannableStringBuilder builder = new SpannableStringBuilder();

    public HistoryGameItemView(Context context) {
        this(context, null);
    }

    public HistoryGameItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HistoryGameItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.history_game_item_view, this, true);
        ButterKnife.inject(this);
    }

    public void setData(Game data) {
        this.data = data;
        processHistory(history, data);
        startTime.setText(FULL_DATE.format(new Date(data.getCreateTime())));
        endTime.setText(FULL_DATE.format(new Date(data.getLastScoreTime())));
        points.setText(data.getPoint() + "점");
        inning.setText(data.getInning() + "이닝");
        highrun.setText(data.getHighrun() + "점 ");
        average.setText(String.format("%.3f", data.getAverage()));
        won.setText(Util.resultAsString(data.getResult()));
    }

    private int processHistory(TextView history, Game game) {
        builder.clear();
        int highrun = 0;
        int sum = 0;
        byte[] scores;
        if (game.getScores() != null) {
            scores = game.getScores();
        } else {
            scores = new byte[game.history.size()];
            int index = 0;
            for (Byte aByte : game.history) {
                scores[index] = aByte;
                index++;
            }
        }
        for (Byte score : scores) {
            highrun = highrun > score ? highrun : score;
            sum += score;
            String text = String.valueOf(score);
            int index = builder.length();
            int size = index + text.length();
            builder.append(text).append(" ");
            if (score > 2) {
                builder.setSpan(new ForegroundColorSpan(0xffff0000), index, size, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        history.setText(builder);
        this.highrun.setText(String.valueOf(highrun));
        return sum;
    }

    public Game getData() {
        return data;
    }
}