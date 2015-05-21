package pointer.wbc.com.billiardspointer.view;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
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
public class HistoryGameItemView extends CardView {
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
    @InjectView(R.id.history)
    PrefixEditTextView history;
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
        won.setText(data.isWon() ? "Win!!" : "Loose..");
    }

    private int processHistory(TextView history, Game game) {
        byte[] bytes = new byte[game.history.size()];
        int index1 = 0;
        for (Byte aByte : game.history) {
            bytes[index1] = aByte;
            index1++;
        }
        game.setScores(bytes);

        builder.clear();
        int highrun = 0;
        int sum = 0;
        for (Byte score : game.history) {
            highrun = highrun > score ? highrun : score;
            sum += score;
            String text = String.valueOf(score);
            int index = builder.length();
            int size = index + text.length();
            builder.append(text).append(" ");
            if (score > 9) {
                builder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.tiffany_red)), index, size, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if (score > 4) {
                builder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.tiffany_yellow)), index, size, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if (score > 2) {
                builder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.tiffany_green)), index, size, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        history.setText(builder);
        this.highrun.setText(String.valueOf(highrun));
        game.setHighrun(highrun);
        return sum;
    }

    public Game getData() {
        return data;
    }
}