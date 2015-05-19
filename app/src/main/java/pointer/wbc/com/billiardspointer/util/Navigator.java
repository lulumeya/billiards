package pointer.wbc.com.billiardspointer.util;

import android.content.Context;
import android.content.Intent;

import pointer.wbc.com.billiardspointer.GameListActivity;

/**
 * Created by Dalton on 15. 5. 19..
 */
public class Navigator {
    public static void goGameList(Context context) {
        Intent intent = new Intent(context, GameListActivity.class);
        context.startActivity(intent);
    }
}