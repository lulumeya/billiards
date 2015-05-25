package pointer.wbc.com.billiardspointer.util;

import android.content.Context;
import android.content.Intent;

import pointer.wbc.com.billiardspointer.Const;
import pointer.wbc.com.billiardspointer.GameListActivity;
import pointer.wbc.com.billiardspointer.GameResultActivity;
import pointer.wbc.com.billiardspointer.model.Game;

/**
 * Created by Dalton on 15. 5. 19..
 */
public class Navigator {
    public static void goGameList(Context context) {
        Intent intent = new Intent(context, GameListActivity.class);
        context.startActivity(intent);
    }

    public static void goGameResult(Context context, Game game) {
        Intent intent = new Intent(context, GameResultActivity.class);
        intent.putExtra(Const.EXTRA_GAME_DATA, InstantObjectTransporter.put(game));
        context.startActivity(intent);
    }
}