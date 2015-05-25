package pointer.wbc.com.billiardspointer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.kakao.APIErrorResult;
import com.kakao.MeResponseCallback;
import com.kakao.Session;
import com.kakao.SessionCallback;
import com.kakao.UserManagement;
import com.kakao.UserProfile;
import com.kakao.exception.KakaoException;
import com.kakao.widget.LoginButton;
import com.makeramen.roundedimageview.RoundedImageView;

import net.kianoni.fontloader.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.realm.Realm;
import pointer.wbc.com.billiardspointer.model.Game;
import pointer.wbc.com.billiardspointer.preference.Pref;
import pointer.wbc.com.billiardspointer.util.Navigator;
import pointer.wbc.com.billiardspointer.util.Util;
import pointer.wbc.com.billiardspointer.view.pressable.DefaultImageView;

/**
 * Created by Dalton on 15. 5. 19..
 */
public class IntroActivity extends BaseActivity implements View.OnClickListener {
    @InjectView(R.id.com_kakao_login)
    LoginButton loginButton;
    @InjectView(R.id.menu)
    DefaultImageView menu;
    @InjectView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @InjectView(R.id.profile_image)
    RoundedImageView profileImage;
    @InjectView(R.id.nickname)
    TextView nickname;
    @InjectView(R.id.club)
    TextView club;
    @InjectView(R.id.btn_play_1p)
    LinearLayout btnPlay1p;
    @InjectView(R.id.btn_play_2p)
    LinearLayout btnPlay2p;
    @InjectView(R.id.average)
    TextView average;
    @InjectView(R.id.win_rate)
    TextView winRate;
    @InjectView(R.id.highrun)
    TextView highrun;
    @InjectView(R.id.total_games)
    TextView totalGames;
    @InjectView(R.id.total_wins)
    TextView totalWins;
    @InjectView(R.id.highrun_date)
    TextView highrunDate;
    @InjectView(R.id.btn_theme)
    TextView btnTheme;
    @InjectView(R.id.go_list)
    TextView goList;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_intro);
        ButterKnife.inject(this);

        btnPlay1p.setOnClickListener(this);
        btnPlay2p.setOnClickListener(this);
        btnTheme.setOnClickListener(this);
        goList.setOnClickListener(this);

        menu.setOnClickListener((view) -> {
            if (drawerLayout.isDrawerOpen(Gravity.START)) {
                drawerLayout.closeDrawer(Gravity.START);
            } else {
                drawerLayout.openDrawer(Gravity.START);
            }
        });

        session = Session.getCurrentSession();
        session.addCallback(sessionCallback);
    }

    @Override
    protected void onStart() {
        super.onStart();
        applyUser();
        applyGameStatistics();
    }

    private void applyGameStatistics() {
        Realm realm = Realm.getInstance(context);
        if (realm.where(Game.class).count() == 0) {
            this.average.setText(getString(R.string.not_avail));
            this.winRate.setText(getString(R.string.not_avail));
            this.highrun.setText(getString(R.string.not_avail));
            this.totalGames.setText(getString(R.string.blank));
            this.totalWins.setText(getString(R.string.blank));
            this.highrunDate.setText(getString(R.string.blank));

            return;
        }
        double average = realm.where(Game.class).equalTo("deleteCandidate", false).averageFloat("average");
        long winCount = realm.where(Game.class).equalTo("deleteCandidate", false).equalTo("won", true).count();
        long totalCount = realm.where(Game.class).equalTo("deleteCandidate", false).count();
        Game highrunGame = realm.where(Game.class).equalTo("deleteCandidate", false).findAllSorted("highrun", false).get(0);

        this.average.setText(String.format("%.3f", average));
        this.winRate.setText(String.format("%d%%", winCount / totalCount * 100));
        this.highrun.setText(String.valueOf(highrunGame.getHighrun()));
        this.totalGames.setText(String.format("%d games", totalCount));
        this.totalWins.setText(String.format("%d games", winCount));
        this.highrunDate.setText(DATE_SIMPLE.format(new Date(highrunGame.getCreateTime())));
    }

    private static final SimpleDateFormat DATE_SIMPLE = new SimpleDateFormat("yyyy.MM.dd");

    @Override
    protected void onResume() {
        super.onResume();
        // 세션이 완전 종료된 상태로 갱신도 할 수 없으므로 명시적 오픈을 위한 로그인 버튼을 보여준다.
        if (session.isClosed()) {
            loginButton.setVisibility(View.VISIBLE);
        }
        // 세션을 가지고 있거나, 갱신할 수 있는 상태로 명시적 오픈을 위한 로그인 버튼을 보여주지 않는다.
        else {
            loginButton.setVisibility(View.GONE);

            // 갱신이 가능한 상태라면 갱신을 시켜준다.
            if (session.isOpenable()) {
                session.implicitOpen();
            }
            getUserInfo();
        }
    }

    private void getUserInfo() {
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            protected void onSuccess(final UserProfile userProfile) {
                // 성공.
                dismissProgress();
                App.setUser(userProfile);
                Pref.saveObject(Const.KEY_PROFILE, userProfile);
                applyUser();
            }

            @Override
            protected void onNotSignedUp() {
                // 가입 페이지로 이동
            }

            @Override
            protected void onSessionClosedFailure(final APIErrorResult errorResult) {
                // 다시 로그인 시도
            }

            @Override
            protected void onFailure(final APIErrorResult errorResult) {
                // 실패
            }
        });
    }

    private void applyUser() {
        UserProfile user = App.getUser();
        if (user != null) {
            Glide.with(context).load(user.getProfileImagePath()).into(profileImage);
            nickname.setText(user.getNickname());
        }
    }

    private void goGameScreen() {
        Intent intent = new Intent(context, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private SessionCallback sessionCallback = new SessionCallback() {
        @Override
        public void onSessionOpened() {
            // 프로그레스바 종료
            dismissProgress();

            // 세션 오픈후 보일 페이지로 이동
            getUserInfo();
        }

        @Override
        public void onSessionClosed(KakaoException e) {
            // 프로그레스바 종료
            dismissProgress();

            // 세션 오픈을 못했으니 다시 로그인 버튼 노출.
            loginButton.setVisibility(View.VISIBLE);
        }

        @Override
        public void onSessionOpening() {
            //프로그레스바 시작
            showProgress("카카오 로그인 중입니다..");
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.reset(this);
        session.removeCallback(sessionCallback);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_play_1p:
                goGameScreen();
                break;

            case R.id.btn_play_2p:
                Util.toast("준비중입니다.");
                break;

            case R.id.btn_theme:
                boolean changedValue = !Pref.getBoolean(Const.KEY_THEME, false);
                Pref.putBoolean(Const.KEY_THEME, changedValue);
                Intent intent = new Intent(this, this.getClass());
                startActivity(intent);
                finish();
                break;

            case R.id.go_list:
                Navigator.goGameList(context);
                break;
        }
    }
}