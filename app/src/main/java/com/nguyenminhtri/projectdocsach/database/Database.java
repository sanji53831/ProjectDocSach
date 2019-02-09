package com.nguyenminhtri.projectdocsach.database;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.nguyenminhtri.projectdocsach.model.OjbectClass.ChiTietSachDaDoc;
import com.nguyenminhtri.projectdocsach.model.OjbectClass.ChiTietSachYeuThich;
import com.nguyenminhtri.projectdocsach.model.OjbectClass.Sach;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Database extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "docsach.sqlite";
    public static final String TABLE_NAME = "user";
    public static final String ID_USER = "userid";
    public static final String THOIGIAN_DOCSACH = "thoigiandocsach";

    public static final String TABLE_SACH = "sach";
    public static final String ID_SACH = "masach";
    public static final String TEN_SACH = "tensach";
    public static final String NOIDUNG_SACH = "noidung";
    public static final String MOTA_SACH = "mota";
    public static final String NGAYXUATBAN_SACH = "ngayxuatban";
    public static final String MATHELOAI_SACH = "matheloai";
    public static final String TENTHELOAI_SACH = "tentheloai";
    public static final String MANHAXUATBAN_SACH = "manhaxuatban";
    public static final String TENNHAXUATBAN_SACH = "tennhaxuatban";
    public static final String MATACGIA_SACH = "matacgia";
    public static final String TENTACGIA_SACH = "tentacgia";
    public static final String GIA_SACH = "gia";
    public static final String TRANGTHAI_SACH = "trangthai";
    public static final String HINHANH_SACH = "hinhanh";
    public static final String TONGSOTRANG_SACH = "tongsotrang";

    public static final String TABLE_SACHYEUTHICH = "sachyeuthich";
    public static final String ID_USER_SACHYEUTHICH = "iduser";
    public static final String ID_BOOK_SACHYEUTHICH = "idbook";

    public static final String TABLE_SACHDADOC = "sachdadoc";
    public static final String ID_USER_SACHDADOC = "iduser";
    public static final String ID_BOOK_SACHDADOC = "idbook";
    public static final String PAGE_CURRENT = "pagecurrent";

    public static final String TABLE_SACHDAMUA = "sachdamua";
    public static final String ID_USER_SACHDAMUA = "iduser";
    public static final String ID_BOOK_SACHDAMUA = "idbook";

    private static Database database;

    private Database(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    public static Database getDatabase(Application context) {
        if (database == null) {
            database = new Database(context);
        }
        return database;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String tableUser = "CREATE TABLE " + TABLE_NAME +
                "(" + ID_USER + " TEXT PRIMARY KEY" +
                "," + THOIGIAN_DOCSACH + " TEXT)";

        String tableSach = "CREATE TABLE " + TABLE_SACH +
                "(" + ID_SACH + " TEXT PRIMARY KEY" +
                "," + TEN_SACH + " TEXT" +
                "," + NOIDUNG_SACH + " TEXT" +
                "," + MOTA_SACH + " TEXT" +
                "," + NGAYXUATBAN_SACH + " TEXT" +
                "," + GIA_SACH + " TEXT" +
                "," + TRANGTHAI_SACH + " TEXT" +
                "," + HINHANH_SACH + " BLOB" +
                "," + MATHELOAI_SACH + " TEXT" +
                "," + TENTHELOAI_SACH + " TEXT" +
                "," + MANHAXUATBAN_SACH + " TEXT" +
                "," + TENNHAXUATBAN_SACH + " TEXT" +
                "," + MATACGIA_SACH + " TEXT" +
                "," + TENTACGIA_SACH + " TEXT" +
                "," + TONGSOTRANG_SACH + " TEXT)";

        String tableSachYeuThich = "CREATE TABLE " + TABLE_SACHYEUTHICH +
                "(" + ID_USER_SACHYEUTHICH + " TEXT" +
                "," + ID_BOOK_SACHYEUTHICH + " TEXT" +
                ", PRIMARY KEY(" + ID_USER_SACHYEUTHICH +
                "," + ID_BOOK_SACHYEUTHICH + "))";

        String tableSachDaDoc = "CREATE TABLE " + TABLE_SACHDADOC +
                "(" + ID_USER_SACHDADOC + " TEXT" +
                "," + ID_BOOK_SACHDADOC + " TEXT" +
                "," + PAGE_CURRENT + " INTEGER DEFAULT 0" +
                ", PRIMARY KEY(" + ID_USER_SACHDADOC +
                "," + ID_BOOK_SACHDADOC + "))";

        String tableSachDaMua = "CREATE TABLE " + TABLE_SACHDAMUA +
                "(" + ID_USER_SACHDAMUA + " TEXT" +
                "," + ID_BOOK_SACHDAMUA + " TEXT" +
                ",PRIMARY KEY(" + ID_USER_SACHDAMUA +
                "," + ID_BOOK_SACHDAMUA + "))";

        db.execSQL(tableUser);
        db.execSQL(tableSach);
        db.execSQL(tableSachYeuThich);
        db.execSQL(tableSachDaDoc);
        db.execSQL(tableSachDaMua);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public int getTotalSach() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM sach", null);
        cursor.moveToNext();
        int total = cursor.getInt(0);
        cursor.close();
        return total;
    }


    public int addUser(String id, String time) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID_USER, id);
        values.put(THOIGIAN_DOCSACH, time);
        if (db.insert(TABLE_NAME, null, values) != -1) {
            db.close();
            return 1;
        }
        db.close();
        return -1;
    }

    public Boolean checkUser(String id) {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[]{ID_USER}, ID_USER + "=?",
                new String[]{id}, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.close();
            db.close();
            return true;
        }

        cursor.close();
        db.close();
        return false;
    }


    public int updateThoiGianDocSach(String id, String time) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(THOIGIAN_DOCSACH, time);
        if (db.update(TABLE_NAME, values, ID_USER + "=?", new String[]{id}) != -1) {
            db.close();
            return 1;
        }
        db.close();
        return -1;
    }

    public String getThoiGianDocSach(String id) {
        String time = "0";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, null, ID_USER + "=?",
                new String[]{id}, null, null, null);
        while (cursor.moveToNext()) {
            time = cursor.getString(cursor.getColumnIndex(THOIGIAN_DOCSACH));
        }
        cursor.close();
        db.close();
        return time;
    }

    public int addBook(Sach sach, Bitmap bitmap) {
        SQLiteDatabase db = getWritableDatabase();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bytes = stream.toByteArray();
        ContentValues values = new ContentValues();
        values.put(ID_SACH, sach.getId());
        values.put(TEN_SACH, sach.getTenSach());
        values.put(NOIDUNG_SACH, sach.getNoiDung());
        values.put(NGAYXUATBAN_SACH, sach.getNgayXuatBan());
        values.put(MOTA_SACH, sach.getMoTa());
        values.put(GIA_SACH, sach.getGia());
        values.put(TRANGTHAI_SACH, sach.getTrangthai());
        values.put(MATHELOAI_SACH, sach.getMaTheLoai());
        values.put(TENTHELOAI_SACH, sach.getTenTheLoai());
        values.put(MATACGIA_SACH, sach.getMaTacGia());
        values.put(TENTACGIA_SACH, sach.getTenTacGia());
        values.put(MANHAXUATBAN_SACH, sach.getMaNhaXuatBan());
        values.put(TENNHAXUATBAN_SACH, sach.getTenNhaXuatBan());
        values.put(TONGSOTRANG_SACH, sach.getTongsotrang());
        values.put(HINHANH_SACH, bytes);
        if (db.insert(TABLE_SACH, null, values) != -1) {
            return 1;
        }
        return -1;
    }

    public Boolean checkSach(String maSach) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_SACH, new String[]{ID_SACH}, ID_SACH + "=?"
                , new String[]{maSach}, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    public int addBookDaDoc(String idUser, String idBook) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID_USER_SACHDADOC, idUser);
        values.put(ID_BOOK_SACHDADOC, idBook);
        if (db.insert(TABLE_SACHDADOC, null, values) != -1) {
            return 1;
        }
        return -1;
    }

    public Boolean checkSachDaDoc(String userId, String maSach) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_SACHDADOC, null,
                ID_USER_SACHDADOC + "=? AND " + ID_BOOK_SACHDADOC + "=?"
                , new String[]{userId, maSach}, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    public void addListChiTietSachDaDoc(ArrayList<ChiTietSachDaDoc> list) {
        for (ChiTietSachDaDoc book : list) {
            addBookDaDoc(book.getMaKhachHang(), book.getMaSach());
        }
    }

    public int deleteAllChiTietSachDaDoc(String idUser) {
        SQLiteDatabase db = getWritableDatabase();
        if (db.delete(TABLE_SACHDADOC, ID_USER_SACHDADOC + "= ?", new String[]{idUser}) != -1) {
            return 1;
        }
        return -1;
    }

    public void addListChiTietSachYeuThich(ArrayList<ChiTietSachYeuThich> list) {
        for (ChiTietSachYeuThich book : list) {
            addBookYeuThich(book.getMaKhachHang(), book.getMaSach());
        }
    }

    public int deleteAllChiTietSachYeuThich(String idUser) {
        SQLiteDatabase db = getWritableDatabase();
        if (db.delete(TABLE_SACHYEUTHICH, ID_USER_SACHYEUTHICH + "= ?", new String[]{idUser}) != -1) {
            return 1;
        }
        return -1;
    }

    public int addBookDaMua(String idUser, String idBook) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID_USER_SACHDAMUA, idUser);
        values.put(ID_BOOK_SACHDAMUA, idBook);
        if (db.insert(TABLE_SACHDAMUA, null, values) != -1) {
            return 1;
        }
        return -1;
    }

    public int addBookYeuThich(String idUser, String idBook) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID_USER_SACHYEUTHICH, idUser);
        values.put(ID_BOOK_SACHYEUTHICH, idBook);
        if (db.insert(TABLE_SACHYEUTHICH, null, values) != -1) {
            return 1;
        }
        return -1;
    }

    public int deleteBookYeuThich(String idUser, String idBook) {
        SQLiteDatabase db = getWritableDatabase();
        if (db.delete(TABLE_SACHYEUTHICH, ID_USER_SACHYEUTHICH + "= ? AND "
                + ID_BOOK_SACHYEUTHICH + " = ?", new String[]{idUser, idBook}) != -1) {
            return 1;
        }
        return -1;
    }

    public int getPagerCurrentSachDaDoc(String idUser, String idBook) {
        int page = 0;
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TABLE_SACHDADOC, new String[]{PAGE_CURRENT},
                ID_USER_SACHDADOC + "=? AND " + ID_BOOK_SACHDADOC + "=?",
                new String[]{idUser, idBook}, null, null, null);
        while (cursor.moveToNext()) {
            page = cursor.getInt(0);
        }
        cursor.close();
        return page;
    }

    public int savePagerCurrentSachDaDoc(String idUser, String idBook, int page) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PAGE_CURRENT, page);
        if (db.update(TABLE_SACHDADOC, values, ID_BOOK_SACHDADOC + "=? AND " + ID_USER_SACHDADOC + "=?"
                , new String[]{idBook, idUser}) != -1) {
            return 1;
        }
        return -1;
    }

    public ArrayList<ChiTietSachDaDoc> getListChiTietSachDaDoc(String userid) {
        ArrayList<ChiTietSachDaDoc> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_SACHDADOC, null,
                ID_USER_SACHDADOC + "=?", new String[]{userid}, null, null, null);
        while (cursor.moveToNext()) {
            String userId = cursor.getString(0);
            String booId = cursor.getString(1);
            ChiTietSachDaDoc chiTietSachDaDoc = new ChiTietSachDaDoc(booId, userId);
            list.add(chiTietSachDaDoc);
        }
        cursor.close();
        return list;
    }

    public ArrayList<ChiTietSachYeuThich> getListChiTietSachYeuThich(final String userid) {
        ArrayList<ChiTietSachYeuThich> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_SACHYEUTHICH, null,
                ID_USER_SACHYEUTHICH + "=?", new String[]{userid}, null, null, null);
        while (cursor.moveToNext()) {
            String userId = cursor.getString(0);
            String booId = cursor.getString(1);
            ChiTietSachYeuThich chiTietSachYeuThich = new ChiTietSachYeuThich(booId, userId);
            list.add(chiTietSachYeuThich);
        }
        cursor.close();
        return list;
    }

    public ArrayList<Sach> getListSachDaDoc(String userId) {
        ArrayList<Sach> list = new ArrayList<>();
        String query = "SELECT sach.masach,sach.tensach,sach.gia,sach.hinhanh,sach.noidung,sach.mota," +
                "sach.ngayxuatban, sach.trangthai, sach.matheloai, sach.manhaxuatban, sach.matacgia," +
                "sach.tentheloai, sach.tennhaxuatban, sach.tentacgia, sach.tongsotrang " +
                "FROM sach " +
                "INNER JOIN sachdadoc ON sach.masach = sachdadoc.idbook " +
                "WHERE sachdadoc.iduser = '" + userId + "'";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            byte[] hinhanh = cursor.getBlob(3);
            Bitmap bitmap = BitmapFactory.decodeByteArray(hinhanh, 0, hinhanh.length);
            String masach = cursor.getString(0);
            String tensach = cursor.getString(1);
            String gia = cursor.getString(2);
            String noidung = cursor.getString(4);
            String mota = cursor.getString(5);
            String ngayxuatban = cursor.getString(6);
            String trangthai = cursor.getString(7);
            String matheloai = cursor.getString(8);
            String manhaxuatban = cursor.getString(9);
            String matacgia = cursor.getString(10);
            String tentheloai = cursor.getString(11);
            String tennhaxuatban = cursor.getString(12);
            String tentacgia = cursor.getString(13);
            String tongsotrang = cursor.getString(14);
            Sach sach = new Sach(masach, tensach, bitmap, noidung, mota, ngayxuatban, tentheloai, tennhaxuatban, tentacgia
                    , gia, trangthai, matheloai, manhaxuatban, matacgia, tongsotrang);
            list.add(sach);
        }
        cursor.close();
        return list;
    }

    public ArrayList<Sach> getListSachYeuThich(String userId) {
        ArrayList<Sach> list = new ArrayList<>();
        String query = "SELECT sach.masach,sach.tensach,sach.gia,sach.hinhanh,sach.noidung,sach.mota," +
                "sach.ngayxuatban, sach.trangthai, sach.matheloai, sach.manhaxuatban, sach.matacgia," +
                "sach.tentheloai, sach.tennhaxuatban, sach.tentacgia,sach.tongsotrang " +
                "FROM sach " +
                "INNER JOIN sachyeuthich ON sach.masach = sachyeuthich.idbook " +
                "WHERE sachyeuthich.iduser = '" + userId + "'";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            byte[] hinhanh = cursor.getBlob(3);
            Bitmap bitmap = BitmapFactory.decodeByteArray(hinhanh, 0, hinhanh.length);
            String masach = cursor.getString(0);
            String tensach = cursor.getString(1);
            String gia = cursor.getString(2);
            String noidung = cursor.getString(4);
            String mota = cursor.getString(5);
            String ngayxuatban = cursor.getString(6);
            String trangthai = cursor.getString(7);
            String matheloai = cursor.getString(8);
            String manhaxuatban = cursor.getString(9);
            String matacgia = cursor.getString(10);
            String tentheloai = cursor.getString(11);
            String tennhaxuatban = cursor.getString(12);
            String tentacgia = cursor.getString(13);
            String tongsotrang = cursor.getString(14);
            Sach sach = new Sach(masach, tensach, bitmap, noidung, mota, ngayxuatban, tentheloai, tennhaxuatban, tentacgia
                    , gia, trangthai, matheloai, manhaxuatban, matacgia, tongsotrang);
            list.add(sach);
        }
        cursor.close();
        return list;
    }

    public ArrayList<Sach> getListSachDaMua(String userId) {
        ArrayList<Sach> list = new ArrayList<>();
        String query = "SELECT sach.masach,sach.tensach,sach.gia,sach.hinhanh,sach.noidung,sach.mota," +
                "sach.ngayxuatban, sach.trangthai, sach.matheloai, sach.manhaxuatban, sach.matacgia," +
                "sach.tentheloai, sach.tennhaxuatban, sach.tentacgia,sach.tongsotrang  " +
                "FROM sach " +
                "INNER JOIN sachdamua ON sach.masach = sachdamua.idbook " +
                "WHERE sachdamua.iduser = '" + userId + "'";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            byte[] hinhanh = cursor.getBlob(3);
            Bitmap bitmap = BitmapFactory.decodeByteArray(hinhanh, 0, hinhanh.length);
            String masach = cursor.getString(0);
            String tensach = cursor.getString(1);
            String gia = cursor.getString(2);
            String noidung = cursor.getString(4);
            String mota = cursor.getString(5);
            String ngayxuatban = cursor.getString(6);
            String trangthai = cursor.getString(7);
            String matheloai = cursor.getString(8);
            String manhaxuatban = cursor.getString(9);
            String matacgia = cursor.getString(10);
            String tentheloai = cursor.getString(11);
            String tennhaxuatban = cursor.getString(12);
            String tentacgia = cursor.getString(13);
            String tongsotrang = cursor.getString(14);
            Sach sach = new Sach(masach, tensach, bitmap, noidung, mota, ngayxuatban, tentheloai, tennhaxuatban, tentacgia
                    , gia, trangthai, matheloai, manhaxuatban, matacgia, tongsotrang);
            list.add(sach);
        }
        cursor.close();
        return list;
    }

}
