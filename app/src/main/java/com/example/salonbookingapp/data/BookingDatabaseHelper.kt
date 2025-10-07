package com.example.salonbookingapp.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class BookingDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "SalonBookings.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "bookings"
        private const val COLUMN_ID = "id"
        private const val COLUMN_SERVICE_NAME = "serviceName"
        private const val COLUMN_SERVICE_PRICE = "servicePrice"
        private const val COLUMN_DATE = "date"
        private const val COLUMN_TIME = "time"
        private const val COLUMN_PAYMENT_METHOD = "paymentMethod"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_SERVICE_NAME TEXT,
                $COLUMN_SERVICE_PRICE REAL,
                $COLUMN_DATE TEXT,
                $COLUMN_TIME TEXT,
                $COLUMN_PAYMENT_METHOD TEXT
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun addBooking(booking: Booking): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_SERVICE_NAME, booking.serviceName)
            put(COLUMN_SERVICE_PRICE, booking.servicePrice)
            put(COLUMN_DATE, booking.date)
            put(COLUMN_TIME, booking.time)
            put(COLUMN_PAYMENT_METHOD, booking.paymentMethod)
        }
        return db.insert(TABLE_NAME, null, values)
    }

    fun updateBooking(booking: Booking): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_SERVICE_NAME, booking.serviceName)
            put(COLUMN_SERVICE_PRICE, booking.servicePrice)
            put(COLUMN_DATE, booking.date)
            put(COLUMN_TIME, booking.time)
            put(COLUMN_PAYMENT_METHOD, booking.paymentMethod)
        }
        return db.update(
            TABLE_NAME,
            values,
            "$COLUMN_ID=?",
            arrayOf(booking.id.toString())
        )
    }

    fun deleteBooking(id: Int): Int {
        val db = writableDatabase
        return db.delete(TABLE_NAME, "$COLUMN_ID=?", arrayOf(id.toString()))
    }

    fun getAllBookings(): List<Booking> {
        val bookings = mutableListOf<Booking>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        cursor.use {
            if (it.moveToFirst()) {
                do {
                    bookings.add(
                        Booking(
                            id = it.getInt(it.getColumnIndexOrThrow(COLUMN_ID)),
                            serviceName = it.getString(it.getColumnIndexOrThrow(COLUMN_SERVICE_NAME)),
                            servicePrice = it.getDouble(it.getColumnIndexOrThrow(COLUMN_SERVICE_PRICE)),
                            date = it.getString(it.getColumnIndexOrThrow(COLUMN_DATE)),
                            time = it.getString(it.getColumnIndexOrThrow(COLUMN_TIME)),
                            paymentMethod = it.getString(it.getColumnIndexOrThrow(COLUMN_PAYMENT_METHOD))
                        )
                    )
                } while (it.moveToNext())
            }
        }
        return bookings
    }

    fun getBookingById(id: Int): Booking? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID=?", arrayOf(id.toString()))
        cursor.use {
            if (it.moveToFirst()) {
                return Booking(
                    id = it.getInt(it.getColumnIndexOrThrow(COLUMN_ID)),
                    serviceName = it.getString(it.getColumnIndexOrThrow(COLUMN_SERVICE_NAME)),
                    servicePrice = it.getDouble(it.getColumnIndexOrThrow(COLUMN_SERVICE_PRICE)),
                    date = it.getString(it.getColumnIndexOrThrow(COLUMN_DATE)),
                    time = it.getString(it.getColumnIndexOrThrow(COLUMN_TIME)),
                    paymentMethod = it.getString(it.getColumnIndexOrThrow(COLUMN_PAYMENT_METHOD))
                )
            }
        }
        return null
    }
}
