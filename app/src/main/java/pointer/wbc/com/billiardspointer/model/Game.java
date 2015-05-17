package pointer.wbc.com.billiardspointer.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Haksu on 2015-05-17.
 */
public class Game {
    public final List<Integer> scores = new ArrayList<>();
    public long createTime;
    public long lastScoreTime;
    public String name;
    public int inning;
    public int point;
    public int recent;
    public int highrun;
    public float average;

    public Game() {
        createTime = System.currentTimeMillis();
    }
}