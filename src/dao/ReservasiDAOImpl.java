package dao;

import config.DatabaseConfig;
import entity.Kamar;
import entity.Reservasi;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservasiDAOImpl implements ReservasiDAO {

    @Override
    public void insert(Reservasi reservasi) {
        String sql = "INSERT INTO reservasi (nama_tamu, kamar_id, check_in, check_out, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, reservasi.getNamaTamu());
            ps.setInt(2, reservasi.getKamar().getId());
            ps.setDate(3, Date.valueOf(reservasi.getCheckIn()));
            ps.setDate(4, Date.valueOf(reservasi.getCheckOut()));
            ps.setString(5, reservasi.getStatus());

            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    reservasi.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Gagal menambahkan reservasi: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Reservasi> findAll() {
        List<Reservasi> list = new ArrayList<>();
        String sql = "SELECT r.id, r.nama_tamu, r.check_in, r.check_out, r.status AS r_status, " +
                     "k.id AS kamar_id, k.nomor_kamar, k.tipe_kamar, k.harga_per_malam, k.status AS k_status " +
                     "FROM reservasi r JOIN kamar k ON r.kamar_id = k.id";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Kamar kamar = new Kamar(
                    rs.getInt("kamar_id"),
                    rs.getString("nomor_kamar"),
                    rs.getString("tipe_kamar"),
                    rs.getBigDecimal("harga_per_malam"),
                    rs.getString("k_status")
                );

                Reservasi res = new Reservasi(
                    rs.getInt("id"),
                    rs.getString("nama_tamu"),
                    kamar,
                    rs.getDate("check_in").toLocalDate(),
                    rs.getDate("check_out").toLocalDate(),
                    rs.getString("r_status")
                );
                list.add(res);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Gagal mengambil data reservasi: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public Reservasi findById(int id) {
        String sql = "SELECT r.id, r.nama_tamu, r.check_in, r.check_out, r.status AS r_status, " +
                     "k.id AS kamar_id, k.nomor_kamar, k.tipe_kamar, k.harga_per_malam, k.status AS k_status " +
                     "FROM reservasi r JOIN kamar k ON r.kamar_id = k.id " +
                     "WHERE r.id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Kamar kamar = new Kamar(
                        rs.getInt("kamar_id"),
                        rs.getString("nomor_kamar"),
                        rs.getString("tipe_kamar"),
                        rs.getBigDecimal("harga_per_malam"),
                        rs.getString("k_status")
                    );

                    return new Reservasi(
                        rs.getInt("id"),
                        rs.getString("nama_tamu"),
                        kamar,
                        rs.getDate("check_in").toLocalDate(),
                        rs.getDate("check_out").toLocalDate(),
                        rs.getString("r_status")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Gagal mencari reservasi berdasarkan ID: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public List<Reservasi> findByNamaTamu(String namaTamu) {
        List<Reservasi> list = new ArrayList<>();
        String sql = "SELECT r.id, r.nama_tamu, r.check_in, r.check_out, r.status AS r_status, " +
                     "k.id AS kamar_id, k.nomor_kamar, k.tipe_kamar, k.harga_per_malam, k.status AS k_status " +
                     "FROM reservasi r JOIN kamar k ON r.kamar_id = k.id " +
                     "WHERE r.nama_tamu LIKE ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + namaTamu + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Kamar kamar = new Kamar(
                        rs.getInt("kamar_id"),
                        rs.getString("nomor_kamar"),
                        rs.getString("tipe_kamar"),
                        rs.getBigDecimal("harga_per_malam"),
                        rs.getString("k_status")
                    );

                    Reservasi res = new Reservasi(
                        rs.getInt("id"),
                        rs.getString("nama_tamu"),
                        kamar,
                        rs.getDate("check_in").toLocalDate(),
                        rs.getDate("check_out").toLocalDate(),
                        rs.getString("r_status")
                    );
                    list.add(res);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Gagal mencari reservasi berdasarkan nama tamu: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public void update(Reservasi reservasi) {
        String sql = "UPDATE reservasi SET nama_tamu = ?, kamar_id = ?, check_in = ?, check_out = ?, status = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, reservasi.getNamaTamu());
            ps.setInt(2, reservasi.getKamar().getId());
            ps.setDate(3, Date.valueOf(reservasi.getCheckIn()));
            ps.setDate(4, Date.valueOf(reservasi.getCheckOut()));
            ps.setString(5, reservasi.getStatus());
            ps.setInt(6, reservasi.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Gagal memperbarui reservasi: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM reservasi WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Gagal menghapus data reservasi: " + e.getMessage(), e);
        }
    }
}
