package com.google.android.gms.nearby.messages.sachin.nearbydevices.Database.DAO;

import android.database.Cursor;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.google.android.gms.nearby.messages.sachin.nearbydevices.Database.Model.Interaction;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class InteractionDAO_Impl implements InteractionDAO {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Interaction> __insertionAdapterOfInteraction;

  private final EntityDeletionOrUpdateAdapter<Interaction> __deletionAdapterOfInteraction;

  private final EntityDeletionOrUpdateAdapter<Interaction> __updateAdapterOfInteraction;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllInteractions;

  public InteractionDAO_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfInteraction = new EntityInsertionAdapter<Interaction>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR IGNORE INTO `interactions_table` (`id`,`EphSK`,`Duration`,`Proximity`,`TimeStamp`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Interaction value) {
        stmt.bindLong(1, value.getId());
        if (value.getEphSK() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getEphSK());
        }
        stmt.bindLong(3, value.getDuration());
        stmt.bindLong(4, value.getProximity());
        if (value.getTime() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getTime());
        }
      }
    };
    this.__deletionAdapterOfInteraction = new EntityDeletionOrUpdateAdapter<Interaction>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `interactions_table` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Interaction value) {
        stmt.bindLong(1, value.getId());
      }
    };
    this.__updateAdapterOfInteraction = new EntityDeletionOrUpdateAdapter<Interaction>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `interactions_table` SET `id` = ?,`EphSK` = ?,`Duration` = ?,`Proximity` = ?,`TimeStamp` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Interaction value) {
        stmt.bindLong(1, value.getId());
        if (value.getEphSK() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getEphSK());
        }
        stmt.bindLong(3, value.getDuration());
        stmt.bindLong(4, value.getProximity());
        if (value.getTime() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getTime());
        }
        stmt.bindLong(6, value.getId());
      }
    };
    this.__preparedStmtOfDeleteAllInteractions = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM interactions_table";
        return _query;
      }
    };
  }

  @Override
  public void insertInteraction(final Interaction interaction) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfInteraction.insert(interaction);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteInteraction(final Interaction interaction) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfInteraction.handle(interaction);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateInteraction(final Interaction interaction) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfInteraction.handle(interaction);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteAllInteractions() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllInteractions.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteAllInteractions.release(_stmt);
    }
  }

  @Override
  public List<Interaction> getAllInteractions() {
    final String _sql = "SELECT * from interactions_table";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfEphSK = CursorUtil.getColumnIndexOrThrow(_cursor, "EphSK");
      final int _cursorIndexOfDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "Duration");
      final int _cursorIndexOfProximity = CursorUtil.getColumnIndexOrThrow(_cursor, "Proximity");
      final int _cursorIndexOfTime = CursorUtil.getColumnIndexOrThrow(_cursor, "TimeStamp");
      final List<Interaction> _result = new ArrayList<Interaction>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Interaction _item;
        final String _tmpEphSK;
        _tmpEphSK = _cursor.getString(_cursorIndexOfEphSK);
        final int _tmpDuration;
        _tmpDuration = _cursor.getInt(_cursorIndexOfDuration);
        final int _tmpProximity;
        _tmpProximity = _cursor.getInt(_cursorIndexOfProximity);
        final String _tmpTime;
        _tmpTime = _cursor.getString(_cursorIndexOfTime);
        _item = new Interaction(_tmpEphSK,_tmpDuration,_tmpProximity,_tmpTime);
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<Interaction> getInteractionByEphSK(final String ephSK) {
    final String _sql = "SELECT * FROM interactions_table WHERE EphSK=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (ephSK == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, ephSK);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfEphSK = CursorUtil.getColumnIndexOrThrow(_cursor, "EphSK");
      final int _cursorIndexOfDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "Duration");
      final int _cursorIndexOfProximity = CursorUtil.getColumnIndexOrThrow(_cursor, "Proximity");
      final int _cursorIndexOfTime = CursorUtil.getColumnIndexOrThrow(_cursor, "TimeStamp");
      final List<Interaction> _result = new ArrayList<Interaction>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Interaction _item;
        final String _tmpEphSK;
        _tmpEphSK = _cursor.getString(_cursorIndexOfEphSK);
        final int _tmpDuration;
        _tmpDuration = _cursor.getInt(_cursorIndexOfDuration);
        final int _tmpProximity;
        _tmpProximity = _cursor.getInt(_cursorIndexOfProximity);
        final String _tmpTime;
        _tmpTime = _cursor.getString(_cursorIndexOfTime);
        _item = new Interaction(_tmpEphSK,_tmpDuration,_tmpProximity,_tmpTime);
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<String> getAllInteractionsKeys() {
    final String _sql = "SELECT EphSK FROM interactions_table";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final List<String> _result = new ArrayList<String>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final String _item;
        _item = _cursor.getString(0);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
