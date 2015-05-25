package pointer.wbc.com.billiardspointer;

import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import net.kianoni.fontloader.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.realm.Realm;
import pointer.wbc.com.billiardspointer.model.Game;
import pointer.wbc.com.billiardspointer.util.InstantObjectTransporter;
import pointer.wbc.com.billiardspointer.util.Util;
import pointer.wbc.com.billiardspointer.view.FontIconButton;
import pointer.wbc.com.billiardspointer.view.PrefixTextView;

/**
 * Created by Haksu on 2015-05-25.
 */
public class GameResultActivity extends BaseActivity implements View.OnClickListener {
    @InjectView(R.id.btn_exit)
    FontIconButton btnExit;
    @InjectView(R.id.btn_share_image)
    FontIconButton btnShareImage;
    @InjectView(R.id.btn_share_text)
    FontIconButton btnShareText;
    @InjectView(R.id.top_menu)
    LinearLayout topMenu;
    @InjectView(R.id.divider1)
    View divider1;
    @InjectView(R.id.game_time)
    TextView gameTime;
    @InjectView(R.id.divider2)
    View divider2;
    @InjectView(R.id.average)
    PrefixTextView average;
    @InjectView(R.id.inning)
    PrefixTextView inning;
    @InjectView(R.id.cue)
    PrefixTextView cue;
    @InjectView(R.id.run)
    PrefixTextView run;
    @InjectView(R.id.highrun)
    PrefixTextView highrun;
    @InjectView(R.id.statistics)
    LinearLayout statistics;
    @InjectView(R.id.history)
    TextView history;
    @InjectView(R.id.run_inning)
    PrefixTextView runInning;
    @InjectView(R.id.no_run_inning)
    PrefixTextView noRunInning;
    @InjectView(R.id.run_rate)
    PrefixTextView runRate;
    @InjectView(R.id.divider3)
    View divider3;
    @InjectView(R.id.grand_average)
    PrefixTextView grandAverage;
    @InjectView(R.id.performance)
    PrefixTextView performance;
    @InjectView(R.id.result_circle)
    TextView resultCircle;
    @InjectView(R.id.capture_area)
    RelativeLayout captureArea;

    private Game game;

    private static final SimpleDateFormat FULL_FORMAT = new SimpleDateFormat("yyyy.MM.dd a HH:mm");
    private final SpannableStringBuilder builder = new SpannableStringBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!getIntent().getExtras().containsKey(Const.EXTRA_GAME_DATA)) {
            Util.toast("잘못된 접근입니다.");
            finish();
            return;
        } else {
            game = InstantObjectTransporter.consume(getIntent().getStringExtra(Const.EXTRA_GAME_DATA));
        }

        setContentView(R.layout.activity_game_result);
        ButterKnife.inject(this);

        btnShareImage.setOnClickListener(this);
        btnShareText.setOnClickListener(this);
        btnExit.setOnClickListener(this);

        applyStatistics();
    }

    private void applyStatistics() {
        if (game != null) {
            builder.clear();
            builder.append(FULL_FORMAT.format(new Date(game.getCreateTime())));
            int gameMin = Math.round((game.getLastScoreTime() - game.getCreateTime()) / (1000 * 60));
            builder.append(" (").append(String.valueOf(gameMin)).append("min)");
            gameTime.setText(builder);

            average.setText(String.format("%.3f", game.getAverage()));
            inning.setText(String.valueOf(game.getInning()));

            int totalCue = 0;
            int runInningCount = 0;
            int noRunInningCount = 0;
            for (byte point : game.getScores()) {
                totalCue += Math.max(1, point);
                if (point > 0) {
                    runInningCount++;
                } else {
                    noRunInningCount++;
                }
            }
            cue.setText(String.valueOf(totalCue));
            run.setText(String.valueOf(game.getPoint()));
            highrun.setText(String.valueOf(game.getHighrun()));

            processHistory(history, 30);

            runInning.setText(String.valueOf(runInningCount));
            noRunInning.setText(String.valueOf(noRunInningCount));
            if (runInningCount + noRunInningCount > 0) {
                runRate.setText(String.format("%d%%", Math.round((float) runInningCount / (float) (runInningCount + noRunInningCount) * 100f)));
            }

            Realm realm = Realm.getInstance(context);
            double currentAverage = realm.where(Game.class).equalTo("deleteCandidate", false).averageFloat("average");
            save();
            double newAverage = realm.where(Game.class).equalTo("deleteCandidate", false).averageFloat("average");
            builder.clear();
            builder.append(String.format("%.3f\n", newAverage));
            int length = builder.length();
            double diff = newAverage - currentAverage;
            if (diff > 0) {
                builder.append("(+").append(String.format("%.3f", diff)).append(")");
                builder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.tiffany_red)), length, builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if (diff == 0) {
                builder.append("(").append(String.format("%.3f", diff)).append(")");
            } else {
                builder.append("(").append(String.format("%.3f", diff)).append(")");
                builder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.tiffany_blue)), length, builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            builder.setSpan(new AbsoluteSizeSpan(11, true), length, builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            grandAverage.setText(builder);

            double performanceVal = newAverage * 25;
            int round = (int) Math.floor(performanceVal);
            double tail = performanceVal - round;
            builder.clear();
            builder.append(String.valueOf(round));
            length = builder.length();
            String str = String.format("%.2f", tail);
            if (str.startsWith("0.")) {
                str = str.substring(1);
            }
            builder.append(str);
            builder.setSpan(new AbsoluteSizeSpan(13, true), length, builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            performance.setText(builder);

            if (game.isWon()) {
                resultCircle.setBackgroundResource(R.drawable.red_circle);
                resultCircle.setText("WIN");
            } else {
                resultCircle.setBackgroundResource(R.drawable.blue_circle);
                resultCircle.setText("LOSE");
            }
        }
    }

    private void save() {
        byte[] bytes = new byte[game.history.size()];
        int index = 0;
        for (Byte aByte : game.history) {
            bytes[index] = aByte;
            index++;
        }
        game.setScores(bytes);
        Realm realm = Realm.getInstance(context);
        realm.beginTransaction();
        Game saved = realm.createObject(Game.class);
        saved.setWon(game.isWon());
        saved.setScores(game.getScores());
        saved.setAverage(game.getAverage());
        saved.setCreateTime(game.getCreateTime());
        saved.setLastScoreTime(game.getLastScoreTime());
        saved.setName(game.getName());
        saved.setInning(game.getInning());
        saved.setPoint(game.getPoint());
        saved.setHighrun(game.getHighrun());
        realm.commitTransaction();
    }

    private int processHistory(TextView history, int itemsPerLine) {
        if (game == null || history == null) {
            return 0;
        }
        builder.clear();
        int highrun = 0;
        int sum = 0;
        int index = 0;
        int builderLength = 0;
        for (Byte score : game.getScores()) {
            highrun = highrun > score ? highrun : score;
            sum += score;
            String text = String.valueOf(score);
            boolean isSplit = index % itemsPerLine == 0;
            if (index != 0 && isSplit) {
                builder.append("\n");
            }
            if (index % 5 == 0 && !isSplit) {
                builder.append(" ");
            }
            builderLength = builder.length();
            builder.append(text);
            if (text.length() > 1) {
                builder.setSpan(new RelativeSizeSpan(0.5f), builderLength, builderLength + text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if (score > 2) {
                builder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.tiffany_red)), builderLength, builderLength + text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            index++;
        }
        history.setText(builder);
        return sum;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.reset(this);
    }

    @Override
    public void onClick(View view) {

    }
}