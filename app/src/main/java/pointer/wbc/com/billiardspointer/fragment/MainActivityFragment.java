package pointer.wbc.com.billiardspointer.fragment;

import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import pointer.wbc.com.billiardspointer.R;
import pointer.wbc.com.billiardspointer.model.Game;
import pointer.wbc.com.billiardspointer.view.PrefixTextView;


public class MainActivityFragment extends BaseFragment implements View.OnClickListener {

    Game game;

    @InjectView(R.id.title)
    TextView title;
    @InjectView(R.id.date)
    TextView date;
    @InjectView(R.id.history)
    PrefixTextView history;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.inject(this, view);

        game = new Game();

        btnSave.setOnClickListener(this);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_save:
                break;
        }
    }
}