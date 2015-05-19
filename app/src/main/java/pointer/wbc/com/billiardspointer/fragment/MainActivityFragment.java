package pointer.wbc.com.billiardspointer.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.kakao.KakaoLink;
import com.kakao.KakaoParameterException;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;
import io.realm.Realm;
import pointer.wbc.com.billiardspointer.R;
import pointer.wbc.com.billiardspointer.model.Game;
import pointer.wbc.com.billiardspointer.util.Util;
import pointer.wbc.com.billiardspointer.view.FlatGameItemView;
import pointer.wbc.com.billiardspointer.view.PrefixEditTextView;
import pointer.wbc.com.billiardspointer.view.PrefixTextView;


public class MainActivityFragment extends BaseFragment implements View.OnClickListener {

    Game game;

    @InjectView(R.id.title)
    TextView title;
    @InjectView(R.id.date)
    TextView date;
    @InjectView(R.id.history)
    PrefixEditTextView history;
    @InjectView(R.id.inning)
    PrefixTextView inning;
    @InjectView(R.id.point)
    PrefixTextView point;
    @InjectView(R.id.recent)
    PrefixTextView recent;
    @InjectView(R.id.highrun)
    PrefixTextView highrun;
    @InjectView(R.id.average)
    PrefixTextView average;
    @InjectView(R.id.btn_newgame)
    AppCompatButton btnNewgame;
    @InjectView(R.id.btn_save)
    AppCompatButton btnSave;
    @InjectView(R.id.btn_delete)
    AppCompatButton btnDelete;
    @InjectViews({R.id.btn_point0, R.id.btn_point1, R.id.btn_point2, R.id.btn_point3, R.id.btn_point4, R.id.btn_point5, R.id.btn_point_back, R.id.btn_point_more})
    List<View> buttons;
    private static final String[] numbers = new String[30];
    private Dialog gridDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.inject(this, view);

        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = String.valueOf(i + 5);
        }


        btnSave.setOnClickListener(this);
        btnNewgame.setOnClickListener(this);

        for (View button : buttons) {
            button.setOnClickListener(pointListener);
        }

        btnDelete.setVisibility(View.GONE);

        reset();

        return view;
    }

    private void reset() {
        game = new Game();
        applyHistory();
    }

    private View.OnClickListener pointListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getTag().toString()) {
                case "0":
                    game.history.add(game.history.size(), (byte) 0);
                    break;
                case "1":
                    game.history.add(game.history.size(), (byte) 1);
                    break;
                case "2":
                    game.history.add(game.history.size(), (byte) 2);
                    break;
                case "3":
                    game.history.add(game.history.size(), (byte) 3);
                    break;
                case "4":
                    game.history.add(game.history.size(), (byte) 4);
                    break;
                case "5":
                    game.history.add(game.history.size(), (byte) 5);
                    break;
                case "←":
                    if (game.history.size() > 0) {
                        game.history.remove(game.history.size() - 1);
                    }
                    break;
                case "▼":
                    showGrid();
                    break;
            }
            applyHistory();
        }
    };

    private void showGrid() {
        if (gridDialog == null) {
            gridDialog = new Dialog(context);
            gridDialog.setTitle("NICE SHOT!!");

            GridView grid = new GridView(context);
            grid.setNumColumns(3);
            GridAdapter adapter = new GridAdapter();
            grid.setAdapter(adapter);
            grid.setOnItemClickListener(adapter);

            gridDialog.setContentView(grid);
            gridDialog.setCancelable(true);
            gridDialog.setCanceledOnTouchOutside(true);
        }
        if (!gridDialog.isShowing()) {
            gridDialog.show();
        }
    }

    private void hideGrid() {
        if (gridDialog != null && gridDialog.isShowing()) {
            gridDialog.dismiss();
        }
    }

    class GridAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {

        private final int size;

        public GridAdapter() {
            size = Util.dpToPx(15);
        }

        @Override
        public int getCount() {
            return 31;
        }

        @Override
        public String getItem(int position) {
            return String.valueOf(position + 5);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView button = new TextView(context);
            button.setGravity(Gravity.CENTER);
            button.setText(getItem(position));
            button.setBackgroundResource(android.R.drawable.list_selector_background);
            button.setPadding(size, size, size, size);
            return button;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            game.history.add(game.history.size(), (byte) (position + 5));
            applyHistory();
            hideGrid();
        }
    }

    private final SpannableStringBuilder builder = new SpannableStringBuilder();

    private void applyHistory() {
        int sum = processHistory(this.history);
        if (game.history.size() > 0) {
            float average = (float) sum / (float) game.history.size();
            this.average.setText(String.format("%.3f", average));
            game.setAverage(average);
        } else {
            this.average.setText(String.format("%.3f", 0f));
            game.setAverage(0f);
        }
        this.inning.setText(String.valueOf(game.history.size()));
        game.setInning(game.history.size());
        if (game.history.size() > 0) {
            this.recent.setText(String.valueOf(game.history.get(game.history.size() - 1)));
        } else {
            this.recent.setText("");
        }
        this.point.setText(String.valueOf(sum));
        game.setPoint(sum);
        game.setLastScoreTime(System.currentTimeMillis());
    }

    private int processHistory(TextView history) {
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    boolean won = false;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_save:
                final View v = LayoutInflater.from(context).inflate(R.layout.finish_dialog, null);
                FlatGameItemView gameItemView = (FlatGameItemView) v.findViewById(R.id.game_item_view);
                gameItemView.getWon().setVisibility(View.GONE);
                PrefixEditTextView history = (PrefixEditTextView) v.findViewById(R.id.history);
                processHistory(history);
                MaterialBetterSpinner resultSelect = (MaterialBetterSpinner) v.findViewById(R.id.result_select);
                gameItemView.setData(game);
                resultSelect.setAdapter(new ArrayAdapter<>(context,
                        android.R.layout.simple_dropdown_item_1line,
                        new String[]{"승리", "패배"}));
                resultSelect.setOnItemClickListener((adapterView, view1, position, l) -> {
                    switch (position) {
                        case 0:
                            won = true;
                            break;
                        case 1:
                            won = false;
                            break;
                    }
                });
                new AlertDialog.Builder(context).setCustomTitle(v)
                        .setItems(new String[]{"저장", "취소", "공유"}, (dialogInterface, i) -> {
                            switch (i) {
                                case 0:
                                    save(won);
                                    break;
                                case 1:
                                    dialogInterface.dismiss();
                                    break;
                                case 2:
                                    try {
                                        game.setWon(won);
                                        KakaoLink kakaoLink = KakaoLink.getKakaoLink(context);
                                        kakaoLink.sendMessage(kakaoLink.createKakaoTalkLinkMessageBuilder()
                                                .addText(gameToString(game))
                                                .addAppButton("앱으로 가기").build(), context);
                                    } catch (KakaoParameterException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                            }
                        }).show();
                break;

            case R.id.btn_newgame:
                new AlertDialog.Builder(context).setMessage("정말로 초기화 할까요?\n저장안된 데이터는 삭제됩니다.").setPositiveButton("초기화", (dialog1, which) -> reset()).setNegativeButton("취소", null).show();
                break;
        }

    }

    private void save(boolean won) {
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
        saved.setWon(won);
        saved.setScores(game.getScores());
        saved.setAverage(game.getAverage());
        saved.setCreateTime(game.getCreateTime());
        saved.setLastScoreTime(game.getLastScoreTime());
        saved.setName(game.getName());
        saved.setInning(game.getInning());
        saved.setPoint(game.getPoint());
        saved.setHighrun(game.getHighrun());
        realm.commitTransaction();
        reset();
        Util.toast("저장 되었습니다. 초기화 합니다.");
    }

    public static final SimpleDateFormat FULL_DATE = new SimpleDateFormat("yyyy.M.d a HH:mm");

    public static String gameToString(Game game) {
        StringBuilder builder = new StringBuilder()
                .append("시작시간  ").append(FULL_DATE.format(game.getCreateTime())).append("\n")
                .append("종료시간  ").append(FULL_DATE.format((game.getLastScoreTime()))).append("\n")
                .append("에버리지  ").append(String.format("%.3f", (game.getAverage()))).append("\n")
                .append("총 득점  ").append((game.getPoint()) + "점").append("\n")
                .append("하이런  ").append((game.getHighrun()) + "점").append("\n")
                .append("승패  ").append((game.isWon()) ? "승리" : "패배").append("\n")
                .append("이닝  ").append((game.getInning()) + "이닝").append("\n")
                .append("이닝별득점  ");
        for (Byte aByte : (game.getHistory())) {
            builder.append(String.valueOf(aByte));
        }
        return builder.toString();
    }
}