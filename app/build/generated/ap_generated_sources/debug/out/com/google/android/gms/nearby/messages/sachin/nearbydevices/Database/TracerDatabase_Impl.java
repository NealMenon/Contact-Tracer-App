package com.google.android.gms.nearby.messages.sachin.nearbydevices.Database;

import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenHelper;
import androidx.room.RoomOpenHelper.Delegate;
import androidx.room.RoomOpenHelper.ValidationResult;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.room.util.TableInfo.Column;
import androidx.room.util.TableInfo.ForeignKey;
import androidx.room.util.TableInfo.Index;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Callback;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Configuration;
import com.example.contact_tracer_appv2.Database.DAO.EphSecretKeyDAO;
import com.example.contact_tracer_appv2.Database.DAO.EphSecretKeyDAO_Impl;
import com.example.contact_tracer_appv2.Database.DAO.InteractionDAO;
import com.example.contact_tracer_appv2.Database.DAO.InteractionDAO_Impl;
import com.example.contact_tracer_appv2.Database.DAO.SecretKeyDAO;
import com.example.contact_tracer_appv2.Database.DAO.SecretKeyDAO_Impl;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings({"unchecked", "deprecation"})
public final class TracerDatabase_Impl extends TracerDatabase {
  private volatile InteractionDAO _interactionDAO;

  private volatile SecretKeyDAO _secretKeyDAO;

  private volatile EphSecretKeyDAO _ephSecretKeyDAO;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `interactions_table` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `EphSK` TEXT NOT NULL, `Duration` INTEGER NOT NULL, `Proximity` INTEGER NOT NULL, `TimeStamp` TEXT NOT NULL)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `secretkeys_table` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `SecretKey` TEXT, `Date` TEXT NOT NULL)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `ephsecretkeys_table` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `EphSecretKey` TEXT NOT NULL)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '0e9bc7ff089bca706355950d13bf89a6')");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `interactions_table`");
        _db.execSQL("DROP TABLE IF EXISTS `secretkeys_table`");
        _db.execSQL("DROP TABLE IF EXISTS `ephsecretkeys_table`");
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onDestructiveMigration(_db);
          }
        }
      }

      @Override
      protected void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      public void onPreMigrate(SupportSQLiteDatabase _db) {
        DBUtil.dropFtsSyncTriggers(_db);
      }

      @Override
      public void onPostMigrate(SupportSQLiteDatabase _db) {
      }

      @Override
      protected RoomOpenHelper.ValidationResult onValidateSchema(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsInteractionsTable = new HashMap<String, TableInfo.Column>(5);
        _columnsInteractionsTable.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsInteractionsTable.put("EphSK", new TableInfo.Column("EphSK", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsInteractionsTable.put("Duration", new TableInfo.Column("Duration", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsInteractionsTable.put("Proximity", new TableInfo.Column("Proximity", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsInteractionsTable.put("TimeStamp", new TableInfo.Column("TimeStamp", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysInteractionsTable = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesInteractionsTable = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoInteractionsTable = new TableInfo("interactions_table", _columnsInteractionsTable, _foreignKeysInteractionsTable, _indicesInteractionsTable);
        final TableInfo _existingInteractionsTable = TableInfo.read(_db, "interactions_table");
        if (! _infoInteractionsTable.equals(_existingInteractionsTable)) {
          return new RoomOpenHelper.ValidationResult(false, "interactions_table(com.example.contact_tracer_appv2.Database.Model.Interaction).\n"
                  + " Expected:\n" + _infoInteractionsTable + "\n"
                  + " Found:\n" + _existingInteractionsTable);
        }
        final HashMap<String, TableInfo.Column> _columnsSecretkeysTable = new HashMap<String, TableInfo.Column>(3);
        _columnsSecretkeysTable.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSecretkeysTable.put("SecretKey", new TableInfo.Column("SecretKey", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSecretkeysTable.put("Date", new TableInfo.Column("Date", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSecretkeysTable = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSecretkeysTable = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSecretkeysTable = new TableInfo("secretkeys_table", _columnsSecretkeysTable, _foreignKeysSecretkeysTable, _indicesSecretkeysTable);
        final TableInfo _existingSecretkeysTable = TableInfo.read(_db, "secretkeys_table");
        if (! _infoSecretkeysTable.equals(_existingSecretkeysTable)) {
          return new RoomOpenHelper.ValidationResult(false, "secretkeys_table(com.example.contact_tracer_appv2.Database.Model.SecretKey).\n"
                  + " Expected:\n" + _infoSecretkeysTable + "\n"
                  + " Found:\n" + _existingSecretkeysTable);
        }
        final HashMap<String, TableInfo.Column> _columnsEphsecretkeysTable = new HashMap<String, TableInfo.Column>(2);
        _columnsEphsecretkeysTable.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsEphsecretkeysTable.put("EphSecretKey", new TableInfo.Column("EphSecretKey", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysEphsecretkeysTable = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesEphsecretkeysTable = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoEphsecretkeysTable = new TableInfo("ephsecretkeys_table", _columnsEphsecretkeysTable, _foreignKeysEphsecretkeysTable, _indicesEphsecretkeysTable);
        final TableInfo _existingEphsecretkeysTable = TableInfo.read(_db, "ephsecretkeys_table");
        if (! _infoEphsecretkeysTable.equals(_existingEphsecretkeysTable)) {
          return new RoomOpenHelper.ValidationResult(false, "ephsecretkeys_table(com.example.contact_tracer_appv2.Database.Model.EphSecretKey).\n"
                  + " Expected:\n" + _infoEphsecretkeysTable + "\n"
                  + " Found:\n" + _existingEphsecretkeysTable);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "0e9bc7ff089bca706355950d13bf89a6", "7b77173960b580fbd4da68c43d533cba");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "interactions_table","secretkeys_table","ephsecretkeys_table");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `interactions_table`");
      _db.execSQL("DELETE FROM `secretkeys_table`");
      _db.execSQL("DELETE FROM `ephsecretkeys_table`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  public InteractionDAO interactionDAO() {
    if (_interactionDAO != null) {
      return _interactionDAO;
    } else {
      synchronized(this) {
        if(_interactionDAO == null) {
          _interactionDAO = new InteractionDAO_Impl(this);
        }
        return _interactionDAO;
      }
    }
  }

  @Override
  public SecretKeyDAO secretKeyDAO() {
    if (_secretKeyDAO != null) {
      return _secretKeyDAO;
    } else {
      synchronized(this) {
        if(_secretKeyDAO == null) {
          _secretKeyDAO = new SecretKeyDAO_Impl(this);
        }
        return _secretKeyDAO;
      }
    }
  }

  @Override
  public EphSecretKeyDAO ephSecretKeyDAO() {
    if (_ephSecretKeyDAO != null) {
      return _ephSecretKeyDAO;
    } else {
      synchronized(this) {
        if(_ephSecretKeyDAO == null) {
          _ephSecretKeyDAO = new EphSecretKeyDAO_Impl(this);
        }
        return _ephSecretKeyDAO;
      }
    }
  }
}
