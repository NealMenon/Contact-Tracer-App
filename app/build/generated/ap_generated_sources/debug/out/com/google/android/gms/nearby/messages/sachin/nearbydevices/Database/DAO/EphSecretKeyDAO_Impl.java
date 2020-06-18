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
import com.google.android.gms.nearby.messages.sachin.nearbydevices.Database.Model.EphSecretKey;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class EphSecretKeyDAO_Impl implements EphSecretKeyDAO {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<EphSecretKey> __insertionAdapterOfEphSecretKey;

  private final EntityDeletionOrUpdateAdapter<EphSecretKey> __deletionAdapterOfEphSecretKey;

  private final EntityDeletionOrUpdateAdapter<EphSecretKey> __updateAdapterOfEphSecretKey;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllEphSecretKeys;

  private final SharedSQLiteStatement __preparedStmtOfDeleteEphSecretKeyByValue;

  public EphSecretKeyDAO_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfEphSecretKey = new EntityInsertionAdapter<EphSecretKey>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `ephsecretkeys_table` (`id`,`EphSecretKey`) VALUES (nullif(?, 0),?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, EphSecretKey value) {
        stmt.bindLong(1, value.getId());
        if (value.getSecretKey() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getSecretKey());
        }
      }
    };
    this.__deletionAdapterOfEphSecretKey = new EntityDeletionOrUpdateAdapter<EphSecretKey>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `ephsecretkeys_table` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, EphSecretKey value) {
        stmt.bindLong(1, value.getId());
      }
    };
    this.__updateAdapterOfEphSecretKey = new EntityDeletionOrUpdateAdapter<EphSecretKey>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `ephsecretkeys_table` SET `id` = ?,`EphSecretKey` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, EphSecretKey value) {
        stmt.bindLong(1, value.getId());
        if (value.getSecretKey() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getSecretKey());
        }
        stmt.bindLong(3, value.getId());
      }
    };
    this.__preparedStmtOfDeleteAllEphSecretKeys = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM ephsecretkeys_table";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteEphSecretKeyByValue = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM ephsecretkeys_table WHERE EphSecretKey=?";
        return _query;
      }
    };
  }

  @Override
  public void insertEphSecretKey(final EphSecretKey... ephSecretKeys) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfEphSecretKey.insert(ephSecretKeys);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteEphSecretKey(final EphSecretKey ephSecretKey) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfEphSecretKey.handle(ephSecretKey);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateEphSecretKeys(final EphSecretKey... ephSecretKeys) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfEphSecretKey.handleMultiple(ephSecretKeys);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteAllEphSecretKeys() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllEphSecretKeys.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteAllEphSecretKeys.release(_stmt);
    }
  }

  @Override
  public void deleteEphSecretKeyByValue(final String value) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteEphSecretKeyByValue.acquire();
    int _argIndex = 1;
    if (value == null) {
      _stmt.bindNull(_argIndex);
    } else {
      _stmt.bindString(_argIndex, value);
    }
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteEphSecretKeyByValue.release(_stmt);
    }
  }

  @Override
  public String getEphSecretKeyById(final int ephSKID) {
    final String _sql = "SELECT EphSecretKey  FROM ephsecretkeys_table WHERE id=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, ephSKID);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final String _result;
      if(_cursor.moveToFirst()) {
        _result = _cursor.getString(0);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public List<EphSecretKey> getAllEphSecretKeys() {
    final String _sql = "SELECT * FROM ephsecretkeys_table";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfSecretKey = CursorUtil.getColumnIndexOrThrow(_cursor, "EphSecretKey");
      final List<EphSecretKey> _result = new ArrayList<EphSecretKey>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final EphSecretKey _item;
        final String _tmpSecretKey;
        _tmpSecretKey = _cursor.getString(_cursorIndexOfSecretKey);
        _item = new EphSecretKey(_tmpSecretKey);
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
  public String getRandomEphSK() {
    final String _sql = "SELECT EphSecretKey FROM ephsecretkeys_table WHERE id=(SELECT MAX(id) from ephsecretkeys_table)";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final String _result;
      if(_cursor.moveToFirst()) {
        _result = _cursor.getString(0);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
