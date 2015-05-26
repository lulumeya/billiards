package pointer.wbc.com.billiardspointer.model;

import io.realm.Realm;
import io.realm.RealmMigration;
import io.realm.internal.ColumnType;
import io.realm.internal.Table;

/**
 * Created by Haksu on 2015-05-22.
 */
public class Migration implements RealmMigration {
    @Override
    public long execute(Realm realm, long version) {
        if (version == 0) {
            // add deleteCandidate column (0 -> 1)
            Table gameTable = realm.getTable(Game.class);
            long index = gameTable.addColumn(ColumnType.BOOLEAN, "deleteCandidate");
            for (int i = 0; i < gameTable.size(); i++) {
                gameTable.setBoolean(index, i, false);
            }
            version++;
        }
        if (version == 1) {
            // change won(boolean) to result(int)
            Table gameTable = realm.getTable(Game.class);
            long oldTypeIndex = getIndexForProperty(gameTable, "won");
            long typeIndex = gameTable.addColumn(ColumnType.INTEGER, "result");
            for (int i = 0; i < gameTable.size(); i++) {
                boolean won = gameTable.getBoolean(oldTypeIndex, i);
                gameTable.setLong(typeIndex, i, won ? Game.WIN : Game.LOSE);
            }
            gameTable.removeColumn(oldTypeIndex);
            version++;
        }
        return version;
    }

    public static long getIndexForProperty(Table table, String name) {
        for (int i = 0; i < table.getColumnCount(); i++) {
            if (table.getColumnName(i).equals(name)) {
                return i;
            }
        }
        return -1;
    }
}