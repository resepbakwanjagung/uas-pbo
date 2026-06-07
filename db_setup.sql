-- ====================================================================
-- SQL Script: db_setup.sql
-- Tema: HOTEL RESERVATION MANAGEMENT SYSTEM
-- Deskripsi: Skrip inisialisasi database db_hotel beserta tabel 
--            kamar dan reservasi untuk UAS PBO.
-- ====================================================================

-- 1. Membuat Database jika belum ada
CREATE DATABASE IF NOT EXISTS db_hotel;
USE db_hotel;

-- 2. Menghapus tabel lama jika ada untuk menghindari konflik (Urutan foreign key harus diperhatikan)
DROP TABLE IF EXISTS reservasi;
DROP TABLE IF EXISTS kamar;

-- 3. Membuat Tabel Kamar
CREATE TABLE kamar (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nomor_kamar VARCHAR(10) NOT NULL UNIQUE,
    tipe_kamar VARCHAR(50) NOT NULL,
    harga_per_malam DECIMAL(10,2) NOT NULL,
    status VARCHAR(20) NOT NULL
);

-- 4. Membuat Tabel Reservasi
CREATE TABLE reservasi (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nama_tamu VARCHAR(100) NOT NULL,
    kamar_id INT NOT NULL,
    check_in DATE NOT NULL,
    check_out DATE NOT NULL,
    status VARCHAR(20) NOT NULL,
    CONSTRAINT fk_reservasi_kamar 
        FOREIGN KEY (kamar_id) REFERENCES kamar(id)
        ON DELETE RESTRICT ON UPDATE CASCADE
);

-- 5. Data Awal (Dummy Data) Kamar untuk Kemudahan Uji Coba
INSERT INTO kamar (nomor_kamar, tipe_kamar, harga_per_malam, status) VALUES
('101', 'Standard', 350000.00, 'TERSEDIA'),
('102', 'Standard', 350000.00, 'TERSEDIA'),
('201', 'Deluxe', 550000.00, 'TERSEDIA'),
('202', 'Deluxe', 550000.00, 'MAINTENANCE'),
('301', 'Suite', 950000.00, 'TERSEDIA');
