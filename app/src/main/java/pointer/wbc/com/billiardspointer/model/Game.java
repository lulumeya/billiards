package pointer.wbc.com.billiardspointer.model;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Haksu on 2015-05-17.
 */
@Table(databaseName = Db.NAME)
public class Game extends BaseModel {
    public final List<Byte> history = new ArrayList<>();
    public static final int WIN = 0;
    public static final int LOSE = 1;
    public static final int NO_GAME = 2;

    @Column
    @PrimaryKey
    private long id;
    @Column
    private boolean deleteCandidate;
    @Column
    private byte[] scores;
    @Column
    private long createTime;
    @Column
    private long lastScoreTime;
    @Column
    private String name = "";
    @Column
    private int inning;
    @Column
    private int point;
    @Column
    private int highrun;
    @Column
    private float average;
    @Column
    private int result;

    @Column
    private int hit;

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

    public int getHit() {
        return hit;
    }

    public void setHit(int hit) {
        this.hit = hit;
    }
}