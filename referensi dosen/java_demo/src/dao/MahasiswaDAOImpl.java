package dao;

import config.DatabaseConfig;
import entity.Mahasiswa;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MahasiswaDAOImpl implements MahasiswaDAO {

    @Override
    public void insert(Mahasiswa m) {

        String sql = "INSERT INTO mahasiswa (nama, nim, jurusan) " +
                "VALUES (?, ?, ?)";

        try (
                Connection conn = DatabaseConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, m.getNama());
            ps.setString(2, m.getNim());
            ps.setString(3, m.getJurusan());

            ps.executeUpdate();

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    m.setId(generatedKeys.getInt(1));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Gagal menambahkan data mahasiswa",
                    e);
        }
    }

    @Override
    public List<Mahasiswa> findAll() {

        List<Mahasiswa> list = new ArrayList<>();

        String sql = "SELECT id, nama, nim, jurusan " +
                "FROM mahasiswa";

        try (
                Connection conn = DatabaseConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Mahasiswa m = new Mahasiswa();

                m.setId(rs.getInt("id"));
                m.setNama(rs.getString("nama"));
                m.setNim(rs.getString("nim"));
                m.setJurusan(rs.getString("jurusan"));

                list.add(m);
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Gagal mengambil data mahasiswa",
                    e);
        }

        return list;
    }

    @Override
    public void update(Mahasiswa m) {

        String sql = "UPDATE mahasiswa " +
                "SET nama = ?, nim = ?, jurusan = ? " +
                "WHERE id = ?";

        try (
                Connection conn = DatabaseConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, m.getNama());
            ps.setString(2, m.getNim());
            ps.setString(3, m.getJurusan());
            ps.setInt(4, m.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Gagal mengubah data mahasiswa",
                    e);
        }
    }

    @Override
    public void delete(int id) {

        String sql = "DELETE FROM mahasiswa " +
                "WHERE id = ?";

        try (
                Connection conn = DatabaseConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Gagal menghapus data mahasiswa",
                    e);
        }
    }

    @Override
    public Mahasiswa findById(int id) {

        String sql = "SELECT id, nama, nim, jurusan " +
                "FROM mahasiswa " +
                "WHERE id = ?";

        try (
                Connection conn = DatabaseConfig.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    Mahasiswa m = new Mahasiswa();

                    m.setId(rs.getInt("id"));
                    m.setNama(rs.getString("nama"));
                    m.setNim(rs.getString("nim"));
                    m.setJurusan(rs.getString("jurusan"));

                    return m;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Gagal mencari mahasiswa",
                    e);
        }

        return null;
    }
}