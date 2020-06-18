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
import com.google.android.gms.nearby.messages.sachin.nearbydevices.Database.Model.SecretKey;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class SecretKeyDAO_Impl implements SecretKeyDAO {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<SecretKey> __insertionAdapterOfSecretKey;

  private final EntityDeletionOrUpdateAdapter<SecretKey> __deletionAdapterOfSecretKey;

  private final EntityDeletionOrUpdateAdapter<SecretKey> __updateAdapterOfSecretKey;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAllSecretKeys;

  public SecretKeyDAO_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSecretKey = new EntityInsertionAdapter<SecretKey>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `secretkeys_table` (`id`,`SecretKey`,`Date`) VALUES (nullif(?, 0),?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, SecretKey value) {
        stmt.bindLong(1, value.getId());
        if (value.getSecretKey() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getSecretKey());
        }
        if (value.getDateString() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getDateString());
        }
      }
    };
    this.__deletionAdapterOfSecretKey = new EntityDeletionOrUpdateAdapter<SecretKey>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `secretkeys_table` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, SecretKey value) {
        stmt.bindLong(1, value.getId());
      }
    };
    this.__updateAdapterOfSecretKey = new EntityDeletionOrUpdateAdapter<SecretKey>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `secretkeys_table` SET `id` = ?,`SecretKey` = ?,`Date` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, SecretKey value) {
        stmt.bindLong(1, value.getId());
        if (value.getSecretKey() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getSecretKey());
        }
        if (value.getDateString() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getDateString());
        }
        stmt.bindLong(4, value.getId());
      }
    };
    this.__preparedStmtOfDeleteAllSecretKeys = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM secretkeys_table";
        return _query;
      }
    };
  }

  @Override
  public void insertSecretKey(final SecretKey secretKey) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfSecretKey.insert(secretKey);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteSecretKey(final SecretKey secretKey) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfSecretKey.handle(secretKey);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateSecretKey(final SecretKey secretKey) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfSecretKey.handle(secretKey);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteAllSecretKeys() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAllSecretKeys.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteAllSecretKeys.release(_stmt);
    }
  }

  @Override
  public SecretKey getSecretKeyByDate(final String secretKeyDate) {
    final String _sql = "SELECT * FROM secretkeys_table WHERE Date=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (secretKeyDate == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, secretKeyDate);
    }
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfSecretKey = CursorUtil.getColumnIndexOrThrow(_cursor, "SecretKey");
      final int _cursorIndexOfDateString = CursorUtil.getColumnIndexOrThrow(_cursor, "Date");
      final SecretKey _result;
      if(_cursor.moveToFirst()) {
        _result = new SecretKey();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _result.setId(_tmpId);
        final String _tmpSecretKey;
        _tmpSecretKey = _cursor.getString(_cursorIndexOfSecretKey);
        _result.setSecretKey(_tmpSecretKey);
        final String _tmpDateString;
        _tmpDateString = _cursor.getString(_cursorIndexOfDateString);
        _result.setDateString(_tmpDateString);
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
  public List<SecretKey> getAllSecretKeys() {
    final String _sql = "SELECT * FROM secretkeys_table";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfSecretKey = CursorUtil.getColumnIndexOrThrow(_cursor, "SecretKey");
      final int _cursorIndexOfDateString = CursorUtil.getColumnIndexOrThrow(_cursor, "Date");
      final List<SecretKey> _result = new ArrayList<SecretKey>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final SecretKey _item;
        _item = new SecretKey();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _item.setId(_tmpId);
        final String _tmpSecretKey;
        _tmpSecretKey = _cursor.getString(_cursorIndexOfSecretKey);
        _item.setSecretKey(_tmpSecretKey);
        final String _tmpDateString;
        _tmpDateString = _cursor.getString(_cursorIndexOfDateString);
        _item.setDateString(_tmpDateString);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public SecretKey getLastSecretKey() {
    final String _sql = "SELECT * FROM secretkeys_table WHERE id=(SELECT MAX(id) from secretkeys_table)";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfSecretKey = CursorUtil.getColumnIndexOrThrow(_cursor, "SecretKey");
      final int _cursorIndexOfDateString = CursorUtil.getColumnIndexOrThrow(_cursor, "Date");
      final SecretKey _result;
      if(_cursor.moveToFirst()) {
        _result = new SecretKey();
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        _result.setId(_tmpId);
        final String _tmpSecretKey;
        _tmpSecretKey = _cursor.getString(_cursorIndexOfSecretKey);
        _result.setSecretKey(_tmpSecretKey);
        final String _tmpDateString;
        _tmpDateString = _cursor.getString(_cursorIndexOfDateString);
        _result.setDateString(_tmpDateString);
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
