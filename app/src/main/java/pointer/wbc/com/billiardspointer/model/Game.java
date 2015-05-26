package pointer.wbc.com.billiardspointer.model;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;

/**
 * Created by Haksu on 2015-05-17.
 */
public class Game extends RealmObject {
    @Ignore
    public final List<Byte> history = new ArrayList<>();
    @Ignore
    public static final int LOSE = 0;
    @Ignore
    public static final int WIN = 1;
    @Ignore
    public static final int NO_GAME = 2;

    private boolean deleteCandidate;
    private byte[] scores;
    private long createTime;
    private long lastScoreTime;
    private String name = "";
    private int inning;
    private int point;
    private int highrun;
    private float average;
    private int result;

    public Game() {
        createTime = System.currentTimeMillis();
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public byte[] getScores() {
        return scores;
    }

    public void setScores(byte[] scores) {
        this.scores = scores;
    }

    public long getCreateTime() {
        return createTime;
    }

    public long getLastScoreTime() {
        return lastScoreTime;
    }

    public void setLastScoreTime(long lastScoreTime) {
        this.lastScoreTime = lastScoreTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getInning() {
        return inning;
    }

    public void setInning(int inning) {
        this.inning = inning;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getHighrun() {
        return highrun;
    }

    public void setHighrun(int highrun) {
        this.highrun = highrun;
    }

    public float getAverage() {
        return average;
    }

    public void setAverage(float average) {
        this.average = average;
    }

    public List<Byte> getHistory() {
        return history;
    }

    public boolean isDeleteCandidate() {
        return deleteCandidate;
    }

    public void setDeleteCandidate(boolean deleteCandidate) {
        this.deleteCandidate = deleteCandidate;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}