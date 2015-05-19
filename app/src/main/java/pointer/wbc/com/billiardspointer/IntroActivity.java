package pointer.wbc.com.billiardspointer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.kakao.Session;
import com.kakao.SessionCallback;
import com.kakao.exception.KakaoException;
import com.kakao.widget.LoginButton;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Dalton on 15. 5. 19..
 */
public class IntroActivity extends BaseActivity {
    @InjectView(R.id.com_kakao_login)
    LoginButton loginButton;
    private Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        ButterKnife.inject(this);

        session = Session.getCurrentSession();
        session.addCallback(sessionCallback);
    }

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
        }
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
            final Intent intent = new Intent(context, MainActivity.class);
            startActivity(intent);
            finish();
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
}