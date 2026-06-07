# PANDUAN LENGKAP MENJALANKAN APLIKASI
## HOTEL RESERVATION MANAGEMENT SYSTEM (JAVA CONSOLE & MYSQL)

Panduan ini akan menuntun Anda langkah demi langkah mulai dari persiapan database, konfigurasi project di NetBeans, hingga menjalankan program untuk keperluan demonstrasi atau presentasi UAS Pemrograman Berbasis Objek.

---

### LANGKAH 1: PERSIAPAN DATABASE MYSQL

Aplikasi ini membutuhkan database MySQL untuk menyimpan data kamar dan reservasi.

1. **Aktifkan MySQL Server di Laragon**:
   - Buka aplikasi **Laragon**.
   - Klik tombol **Start All** untuk mengaktifkan layanan Apache dan MySQL.

2. **Buat Database dan Tabel via Database Tool (Database / phpMyAdmin)**:
   - Klik tombol **Database** di Laragon untuk membuka pengelola database (biasanya menggunakan **HeidiSQL** secara default).
   - Atau, jika Anda menggunakan phpMyAdmin di Laragon, Anda bisa mengaksesnya melalui URL: `http://localhost/phpmyadmin/`.
   - Buat database baru bernama `db_hotel` (di HeidiSQL: klik kanan pada sesi koneksi $\rightarrow$ *Create new* $\rightarrow$ *Database* $\rightarrow$ beri nama `db_hotel`).
   - Buka file skrip SQL yang sudah disediakan di workspace: [db_setup.sql](file:///c:/Users/LENOVO/Desktop/PBO-UAS/db_setup.sql)
   - Salin (Copy) seluruh kode SQL dari file tersebut, kemudian jalankan query tersebut di HeidiSQL (tab *Query*) atau phpMyAdmin (tab *SQL*) untuk membuat tabel `kamar` & `reservasi` beserta data awalnya.


---

### LANGKAH 2: MEMBUAT PROJECT DI APACHE NETBEANS

Ikuti langkah berikut untuk memasukkan source code project ke dalam NetBeans:

1. Buka aplikasi **Apache NetBeans** Anda.
2. Klik menu **File** di pojok kiri atas, lalu pilih **New Project...**
3. Di jendela New Project:
   - Pilih **Categories**: `Java with Ant`
   - Pilih **Projects**: `Java Project with Existing Sources`
   - Klik **Next**.
4. Di jendela berikutnya:
   - **Project Name**: Masukkan nama project, contoh: `HotelReservationSystem`
   - **Project Folder**: Biarkan default atau sesuaikan dengan keinginan Anda.
   - Klik **Next**.
5. Di bagian **Existing Sources**:
   - Pada kolom **Source Package Folders**, klik tombol **Add Folder...**
   - Cari dan pilih folder `src` yang berada di workspace UAS Anda, yaitu:
     `c:\Users\LENOVO\Desktop\PBO-UAS\src`
    - Klik **Finish**. NetBeans akan memuat semua package dan kelas Java secara otomatis.
6. **PENTING (Catatan Sumber Daya/Properties File)**:
   - Supaya file `database.properties` dapat dibaca dengan baik oleh program Java saat di-*run*, pastikan file `database.properties` berada di dalam package `config`.
   - Di NetBeans, Anda akan melihat struktur package: `config`, `dao`, `entity`, `main`, dan `service`.
   - Jika Anda menemui error saat menjalankan aplikasi yang bertuliskan *"tidak ditemukan di classpath"*, pastikan file `database.properties` sudah berada tepat di bawah package `config` di editor NetBeans Anda. (Kelas `DatabaseConfig` di project ini sudah dilengkapi fallback pencarian ke direktori lokal `src/config/database.properties` secara cerdas untuk mencegah error ini).


---

### LANGKAH 3: MENAMBAHKAN MYSQL JDBC DRIVER (LIBRARIES)

Agar program Java dapat terhubung ke MySQL, Anda harus menambahkan library MySQL Connector ke dalam project.

1. Di panel bagian kiri NetBeans (tab **Projects**), cari nama project Anda (`HotelReservationSystem`).
2. Klik tanda `+` untuk mengekspansi project, lalu cari folder bernama **Libraries**.
3. **Klik kanan** pada folder **Libraries** tersebut, lalu pilih **Add JAR/Folder**.
4. Cari file MySQL Connector di komputer Anda:
   - Biasanya jika Anda menginstal NetBeans lengkap, filenya sudah tersedia.
   - Atau jika Anda belum memilikinya, Anda dapat mengunduh **MySQL Connector/J** (`.jar` file) terlebih dahulu.
   - Pilih file `.jar` tersebut (misalnya `mysql-connector-j-8.x.x.jar`), lalu klik **Open**.
   - Pastikan driver MySQL tersebut kini terdaftar di bawah folder **Libraries** project Anda.

---

### LANGKAH 4: MENYESUAIKAN KONFIGURASI DATABASE DI JAVA

Pastikan program Java menggunakan username dan password MySQL yang sesuai dengan komputer Anda.

1. Buka file konfigurasi database di NetBeans pada package `config`: [database.properties](file:///c:/Users/LENOVO/Desktop/PBO-UAS/src/config/database.properties)
2. Secara default konfigurasinya adalah:
   ```properties
   db.url=jdbc:mysql://localhost:3306/db_hotel?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
   db.user=root
   db.password=
   ```
3. Jika database MySQL Anda menggunakan password (misalnya di luar XAMPP default), silakan isi password tersebut pada baris `db.password=`. Jika tidak ada password, biarkan kosong setelah tanda sama dengan (`=`).
4. Simpan file (`Ctrl + S`).

---

### LANGKAH 5: MENJALANKAN APLIKASI (RUN FILE)

Sekarang project siap dijalankan:

1. Pada panel project di sebelah kiri NetBeans, buka package `main`.
2. Klik kanan pada file [MainApp.java](file:///c:/Users/LENOVO/Desktop/PBO-UAS/src/main/MainApp.java).
3. Pilih **Run File** (atau klik file tersebut dan tekan **Shift + F6** pada keyboard).
4. Menu aplikasi konsol interaktif bertema **Hotel Reservation Management System** akan muncul di tab **Output** NetBeans di bagian bawah.
5. Anda dapat mulai berinteraksi dengan memasukkan angka menu (1 untuk mengelola kamar, 2 untuk mengelola reservasi, dan 0 untuk keluar).

---

### FITUR VALIDASI BISNIS UNTUK DIUJI SAAT PRESENTASI:
Saat mempresentasikan tugas ini di depan Dosen, Anda dapat mendemonstrasikan fitur validasi bisnis otomatis berikut:
* **Validasi Kamar Unik**: Coba tambahkan kamar baru dengan nomor kamar yang sudah ada. Sistem akan menolak dan memunculkan pesan error.
* **Validasi Harga**: Coba masukkan harga kamar bernilai `0` atau minus. Sistem akan memunculkan pesan validasi error.
* **Validasi Check-Out**: Saat menambah reservasi, coba masukkan tanggal check-out yang mendahului tanggal check-in (contoh: check-in `2026-06-10`, check-out `2026-06-08`). Sistem akan menolaknya.
* **Validasi Status Kamar Terisi**: Kamar yang statusnya sedang `TERISI` atau `MAINTENANCE` tidak akan bisa direservasi oleh tamu lain hingga statusnya kembali `TERSEDIA`.
