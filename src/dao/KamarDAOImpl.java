package dao;

import config.DatabaseConfig;
import entity.Kamar;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KamarDAOImpl implements KamarDAO {

    @Override
    public void insert(Kamar kamar) {
        String sql = "INSERT INTO kamar (nomor_kamar, tipe_kamar, harga_per_malam, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, kamar.getNomorKamar());
            ps.setString(2, kamar.getTipeKamar());
            ps.setBigDecimal(3, kamar.getHargaPerMalam());
            ps.setString(4, kamar.getStatus());

            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    kamar.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Gagal menambahkan data kamar: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Kamar> findAll() {
        List<Kamar> list = new ArrayList<>();
        String sql = "SELECT id, nomor_kamar, tipe_kamar, harga_per_malam, status FROM kamar";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Kamar kamar = new Kamar();
                kamar.setId(rs.getInt("id"));
                kamar.setNomorKamar(rs.getString("nomor_kamar"));
                kamar.setTipeKamar(rs.getString("tipe_kamar"));
                kamar.setHargaPerMalam(rs.getBigDecimal("harga_per_malam"));
                kamar.setStatus(rs.getString("status"));
                list.add(kamar);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Gagal mengambil data kamar: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public Kamar findById(int id) {
        String sql = "SELECT id, nomor_kamar, tipe_kamar, harga_per_malam, status FROM kamar WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Kamar kamar = new Kamar();
                    kamar.setId(rs.getInt("id"));
                    kamar.setNomorKamar(rs.getString("nomor_kamar"));
                    kamar.setTipeKamar(rs.getString("tipe_kamar"));
                    kamar.setHargaPerMalam(rs.getBigDecimal("harga_per_malam"));
                    kamar.setStatus(rs.getString("status"));
                    return kamar;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Gagal mencari kamar berdasarkan ID: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public Kamar findByNomorKamar(String nomorKamar) {
        String sql = "SELECT id, nomor_kamar, tipe_kamar, harga_per_malam, status FROM kamar WHERE nomor_kamar = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nomorKamar);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Kamar kamar = new Kamar();
                    kamar.setId(rs.getInt("id"));
                    kamar.setNomorKamar(rs.getString("nomor_kamar"));
                    kamar.setTipeKamar(rs.getString("tipe_kamar"));
                    kamar.setHargaPerMalam(rs.getBigDecimal("harga_per_malam"));
                    kamar.setStatus(rs.getString("status"));
                    return kamar;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Gagal mencari kamar berdasarkan nomor: " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    public void update(Kamar kamar) {
        String sql = "UPDATE kamar SET nomor_kamar = ?, tipe_kamar = ?, harga_per_malam = ?, status = ? WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, kamar.getNomorKamar());
            ps.setString(2, kamar.getTipeKamar());
            ps.setBigDecimal(3, kamar.getHargaPerMalam());
            ps.setString(4, kamar.getStatus());
            ps.setInt(5, kamar.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Gagal memperbarui data kamar: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM kamar WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Gagal menghapus data kamar: " + e.getMessage(), e);
        }
    }
}
