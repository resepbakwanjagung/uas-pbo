import java.sql.Connection;
import java.sql.SQLException;

import config.DatabaseConfig;
import dao.MahasiswaDAO;
import dao.MahasiswaDAOImpl;
import entity.Mahasiswa;

public class MainApp {

    public static void main(String[] args) {

        try (Connection conn = DatabaseConfig.getConnection()) {
            if (conn != null) {
                System.out.println("✅ Koneksi berhasil terhubung ke MySQL Server!");
            }
        } catch (SQLException e) {
            System.err.println("❌ Terjadi kegagalan koneksi: " + e.getMessage());
        }

        MahasiswaDAO dao = new MahasiswaDAOImpl();

        // INSERT
        Mahasiswa m = new Mahasiswa("Royan", "2026001", "Informatika");
        dao.insert(m);
        System.out.println("INSERT -> ID yang di-generate: " + m.getId());

        Mahasiswa m2 = new Mahasiswa("Siti", "2026002", "Sistem Informasi");
        dao.insert(m2);
        System.out.println("INSERT -> ID yang di-generate: " + m2.getId());

        // FIND ALL
        System.out.println("\n--- findAll() ---");
        dao.findAll().forEach(System.out::println);

        // FIND BY ID
        System.out.println("\n--- findById(" + m.getId() + ") ---");
        Mahasiswa found = dao.findById(m.getId());
        System.out.println(found);

        // UPDATE
        m.setNama("Royan Updated");
        m.setJurusan("Teknik Informatika");
        dao.update(m);
        System.out.println("\n--- Setelah update ---");
        dao.findAll().forEach(System.out::println);

        // DELETE
        dao.delete(m.getId());
        System.out.println("\n--- Setelah delete id=" + m.getId() + " ---");
        dao.findAll().forEach(System.out::println);
    }
}
