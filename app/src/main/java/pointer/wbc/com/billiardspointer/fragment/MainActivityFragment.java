package pointer.wbc.com.billiardspointer.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.InjectViews;
import pointer.wbc.com.billiardspointer.R;
import pointer.wbc.com.billiardspointer.log.Logger;
import pointer.wbc.com.billiardspointer.model.Game;
import pointer.wbc.com.billiardspointer.util.Navigator;
import pointer.wbc.com.billiardspointer.util.Util;
import pointer.wbc.com.billiardspointer.view.PrefixTextView;


public class MainActivityFragment extends BaseFragment implements View.OnClickListener {

    Game game;

    @InjectView(R.id.history)
    TextView history;
    @InjectView(R.id.inning)
    PrefixTextView inning;
    @InjectView(R.id.point)
    PrefixTextView point;
    @InjectView(R.id.average)
    PrefixTextView average;
    @InjectView(R.id.btn_newgame)
    TextView btnNewgame;
    @InjectView(R.id.btn_save)
    TextView btnSave;
    @InjectViews({R.id.btn_point0, R.id.btn_point1, R.id.btn_point2, R.id.btn_point3, R.id.btn_point4, R.id.btn_point5, R.id.btn_point_back, R.id.btn_point_more})
    List<View> buttons;
    private static final String[] numbers = new String[30];
    @InjectView(R.id.btn_exit)
    net.kianoni.fontloader.TextView btnExit;
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
        btnExit.setOnClickListener(this);

        for (View button : buttons) {
            button.setOnClickListener(pointListener);
        }

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
            grid.setNumColumns(4);
            grid.setPadding(0, Util.dpToPx(10), 0, Util.dpToPx(15));
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

    public void onBackPressed() {
        onClick(btnExit);
    }

    class GridAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {

        private final int size;
        private final SpannableStringBuilder builder = new SpannableStringBuilder();

        public GridAdapter() {
            size = Util.dpToPx(40);
        }

        @Override
        public int getCount() {
            return 24;
        }

        @Override
        public String getItem(int position) {
            return String.valueOf(position + 6);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView button = new TextView(context);
            if (position + 6 == 26) {
                builder.clear();
                builder.append("\n").append(getItem(position)).append("\n");
                int length = builder.length();
                builder.append("Korean Record");
                builder.setSpan(new AbsoluteSizeSpan(7, true), length, builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                button.setText(builder);
            } else if (position + 6 == 28) {
                builder.clear();
                builder.append("\n").append(getItem(position)).append("\n");
                int length = builder.length();
                builder.append("World Record");
                builder.setSpan(new AbsoluteSizeSpan(7, true), length, builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                button.setText(builder);
            } else {
                button.setText(getItem(position));
            }
            button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            button.setLineSpacing(Util.dpToPx(3), 1f);
            button.setGravity(Gravity.CENTER);
            button.setMinLines(3);
            return button;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            game.history.add(game.history.size(), (byte) (position + 6));
            applyHistory();
            hideGrid();
        }
    }

    private final SpannableStringBuilder builder = new SpannableStringBuilder();

    private void applyHistory() {
        int sum = processHistory(this.history, 20);
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
//        if (game.history.size() > 0) {
//            this.recent.setText(String.valueOf(game.history.get(game.history.size() - 1)));
//        } else {
//            this.recent.setText("");
//        }
        this.point.setText(String.valueOf(sum));
        game.setPoint(sum);
        game.setLastScoreTime(System.currentTimeMillis());
    }

    private int processHistory(TextView history, int itemsPerLine) {
        builder.clear();
        int highrun = 0;
        int sum = 0;
        int index = 0;
        int builderLength = 0;
        for (Byte score : game.history) {
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
                builder.setSpan(new ForegroundColorSpan(0xffff0000), builderLength, builderLength + text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            index++;
        }
        history.setText(builder);
        game.setHighrun(highrun);
        return sum;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    int winSelectedIndex = 2;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_save:
                if (game.history.size() == 0) {
                    Util.toast("득점 정보가 없습니다.");
                    return;
                }

                winSelectedIndex = 2;
                new AlertDialog.Builder(context).setSingleChoiceItems(new String[]{"WIN", "LOSE", "NO GAME"}, 2,
                        (dialogInterface, i) -> {
                            winSelectedIndex = i;
                            Logger.objs("select", i);
                        }).setPositiveButton("확인", (dialogInterface, i) -> {
                    game.setResult(winSelectedIndex);
                    Navigator.goGameResult(context, copy());
                    getActivity().finish();
                }).setNegativeButton("취소", null).show();

//                final View v = LayoutInflater.from(context).inflate(R.layout.finish_dialog, null);
//                FlatGameItemView gameItemView = (FlatGameItemView) v.findViewById(R.id.game_item_view);
//                gameItemView.getWon().setVisibility(View.GONE);
//                PrefixEditTextView history = (PrefixEditTextView) v.findViewById(R.id.history);
//                processHistory(history);
//                MaterialBetterSpinner resultSelect = (MaterialBetterSpinner) v.findViewById(R.id.result_select);
//                gameItemView.setData(game);
//                resultSelect.setAdapter(new ArrayAdapter<>(context,
//                        android.R.layout.simple_dropdown_item_1line,
//                        new String[]{getString(R.string.win), getString(R.string.defeat)}));
//                resultSelect.setOnItemClickListener((adapterView, view1, position, l) -> {
//                    switch (position) {
//                        case 0:
//                            won = true;
//                            break;
//                        case 1:
//                            won = false;
//                            break;
//                    }
//                });
//
//                String[] menu = {
//                        getString(R.string.action_save),
//                        getString(R.string.action_cancel),
//                        getString(R.string.action_share),
//                };
//                new AlertDialog.Builder(context).setCustomTitle(v)
//                        .setItems(menu, (dialogInterface, i) -> {
//                            switch (i) {
//                                case 0:
//                                    save(won);
//                                    break;
//                                case 1:
//                                    dialogInterface.dismiss();
//                                    break;
//                                case 2:
//                                    try {
//                                        game.setWon(won);
//                                        KakaoLink kakaoLink = KakaoLink.getKakaoLink(context);
//                                        kakaoLink.sendMessage(kakaoLink.createKakaoTalkLinkMessageBuilder()
//                                                .addText(gameToString(game))
//                                                .addAppButton("앱으로 가기").build(), context);
//                                    } catch (KakaoParameterException e) {
//                                        e.printStackTrace();
//                                    }
//                                    break;
//                            }
//                        }).show();
                break;

            case R.id.btn_newgame:
                if (game.history.size() == 0) {
                    return;
                }
                new AlertDialog.Builder(context)
                        .setMessage(getString(R.string.init_question))
                        .setPositiveButton(getString(R.string.init), (dialog1, which) -> reset())
                        .setNegativeButton(getString(R.string.action_cancel), null)
                        .show();
                break;

//            case R.id.btn_share:
//                HistoryGameItemView itemView = new HistoryGameItemView(context);
//                itemView.setData(game);
//                new AlertDialog.Builder(context)
//                        .setCustomTitle(itemView)
//                        .setMessage("위의 내용으로 이미지를 공유합니다.")
//                        .setPositiveButton("공유하기", (dialog1, which) -> Util.share((BaseActivity) getActivity(), itemView))
//                        .setNegativeButton(getString(R.string.action_cancel), null)
//                        .show();
//                break;

            case R.id.btn_exit:
                if (game.history.size() == 0) {
                    getActivity().finish();
                    return;
                }
                new AlertDialog.Builder(context)
                        .setMessage("게임을 취소하고 이전화면으로 돌아갑니다.")
                        .setPositiveButton("확인", (dialog1, which) -> getActivity().finish())
                        .setNegativeButton(getString(R.string.action_cancel), null)
                        .show();
                break;
        }
    }

    private Game copy() {
        byte[] bytes = new byte[game.history.size()];
        int index = 0;
        for (Byte aByte : game.history) {
            bytes[index] = aByte;
            index++;
        }
        game.setScores(bytes);
        Game saved = new Game();
        saved.setResult(game.getResult());
        saved.setScores(game.getScores());
        saved.setAverage(game.getAverage());
        saved.setCreateTime(game.getCreateTime());
        saved.setLastScoreTime(game.getLastScoreTime());
        saved.setName(game.getName());
        saved.setInning(game.getInning());
        saved.setPoint(game.getPoint());
        saved.setHighrun(game.getHighrun());
        return saved;
    }

    public static final SimpleDateFormat FULL_DATE = new SimpleDateFormat("yyyy.M.d a HH:mm");

    public String gameToString(Game game) {
        StringBuilder builder = new StringBuilder()
                .append(getString(R.string.start_time) + "  ")
                .append(FULL_DATE.format(game.getCreateTime()))
                .append("\n")
                .append(getString(R.string.end_time) + "  ")
                .append(FULL_DATE.format((game.getLastScoreTime())))
                .append("\n")
                .append(getString(R.string.average) + "  ")
                .append(String.format("%.3f", (game.getAverage())))
                .append("\n")
                .append(getString(R.string.total_point) + "  ")
                .append((game.getPoint()) + getString(R.string.point))
                .append("\n")
                .append(getString(R.string.high_run) + "  ")
                .append((game.getHighrun()) + getString(R.string.point))
                .append("\n")
                .append(getString(R.string.result) + "  ")
                .append(Util.resultAsString(game.getResult()))
                .append("\n")
                .append(getString(R.string.inning) + "  ")
                .append((game.getInning()) + getString(R.string.inning)).append("\n")
                .append(getString(R.string.point_per_inning) + "  ");

        for (Byte aByte : (game.getHistory())) {
            builder.append(String.valueOf(aByte));
        }
        return builder.toString();
    }
}