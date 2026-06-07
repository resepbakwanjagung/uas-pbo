import dao.MahasiswaDAOImpl;
import entity.Mahasiswa;
import service.MahasiswaService;

import java.util.List;
import java.util.Scanner;

public class MainAppService {

    private static final Scanner scanner = new Scanner(System.in);
    private static final MahasiswaService service = new MahasiswaService(new MahasiswaDAOImpl());

    public static void main(String[] args) {
        int pilihan;

        do {
            tampilkanMenu();
            pilihan = bacaInt("Pilih menu: ");

            switch (pilihan) {
                case 1 -> tampilkanSemua();
                case 2 -> cariById();
                case 3 -> tambahMahasiswa();
                case 4 -> ubahMahasiswa();
                case 5 -> hapusMahasiswa();
                case 0 -> System.out.println("\nSampai jumpa!");
                default -> System.out.println("[!] Pilihan tidak valid.\n");
            }
        } while (pilihan != 0);

        scanner.close();
    }

    // ---------------------------------------------------------------

    private static void tampilkanMenu() {
        System.out.println("========================================");
        System.out.println("       APLIKASI DATA MAHASISWA");
        System.out.println("========================================");
        System.out.println("  1. Tampilkan Semua Mahasiswa");
        System.out.println("  2. Cari Mahasiswa by ID");
        System.out.println("  3. Tambah Mahasiswa");
        System.out.println("  4. Ubah Data Mahasiswa");
        System.out.println("  5. Hapus Mahasiswa");
        System.out.println("  0. Keluar");
        System.out.println("----------------------------------------");
    }

    private static void tampilkanSemua() {
        System.out.println("\n--- Daftar Semua Mahasiswa ---");
        List<Mahasiswa> list = service.semuaMahasiswa();

        if (list.isEmpty()) {
            System.out.println("(Belum ada data)\n");
            return;
        }

        System.out.printf("%-5s %-20s %-12s %-20s%n", "ID", "Nama", "NIM", "Jurusan");
        System.out.println("-".repeat(60));
        for (Mahasiswa m : list) {
            System.out.printf("%-5d %-20s %-12s %-20s%n",
                    m.getId(), m.getNama(), m.getNim(), m.getJurusan());
        }
        System.out.println();
    }

    private static void cariById() {
        System.out.println("\n--- Cari Mahasiswa by ID ---");
        int id = bacaInt("Masukkan ID: ");

        try {
            Mahasiswa m = service.cariMahasiswa(id);
            System.out.println("\nData ditemukan:");
            System.out.printf("  ID      : %d%n", m.getId());
            System.out.printf("  Nama    : %s%n", m.getNama());
            System.out.printf("  NIM     : %s%n", m.getNim());
            System.out.printf("  Jurusan : %s%n%n", m.getJurusan());
        } catch (IllegalArgumentException e) {
            System.out.println("[!] " + e.getMessage() + "\n");
        }
    }

    private static void tambahMahasiswa() {
        System.out.println("\n--- Tambah Mahasiswa Baru ---");
        String nama    = bacaString("Nama    : ");
        String nim     = bacaString("NIM     : ");
        String jurusan = bacaString("Jurusan : ");

        try {
            Mahasiswa m = new Mahasiswa(nama, nim, jurusan);
            service.tambahMahasiswa(m);
            System.out.println("[OK] Data berhasil ditambahkan. ID = " + m.getId() + "\n");
        } catch (IllegalArgumentException e) {
            System.out.println("[!] Validasi gagal: " + e.getMessage() + "\n");
        } catch (RuntimeException e) {
            System.out.println("[!] Gagal menyimpan ke database: " + e.getMessage() + "\n");
        }
    }

    private static void ubahMahasiswa() {
        System.out.println("\n--- Ubah Data Mahasiswa ---");
        int id = bacaInt("Masukkan ID yang ingin diubah: ");

        Mahasiswa existing;
        try {
            existing = service.cariMahasiswa(id);
        } catch (IllegalArgumentException e) {
            System.out.println("[!] " + e.getMessage() + "\n");
            return;
        }

        System.out.println("Data saat ini: " + existing);
        System.out.println("(Tekan Enter untuk mempertahankan nilai lama)");

        String nama    = bacaStringOpsional("Nama baru    [" + existing.getNama() + "]: ");
        String nim     = bacaStringOpsional("NIM baru     [" + existing.getNim() + "]: ");
        String jurusan = bacaStringOpsional("Jurusan baru [" + existing.getJurusan() + "]: ");

        if (!nama.isEmpty())    existing.setNama(nama);
        if (!nim.isEmpty())     existing.setNim(nim);
        if (!jurusan.isEmpty()) existing.setJurusan(jurusan);

        try {
            service.ubahMahasiswa(existing);
            System.out.println("[OK] Data berhasil diperbarui.\n");
        } catch (IllegalArgumentException e) {
            System.out.println("[!] Validasi gagal: " + e.getMessage() + "\n");
        } catch (RuntimeException e) {
            System.out.println("[!] Gagal update ke database: " + e.getMessage() + "\n");
        }
    }

    private static void hapusMahasiswa() {
        System.out.println("\n--- Hapus Mahasiswa ---");
        int id = bacaInt("Masukkan ID yang ingin dihapus: ");

        try {
            Mahasiswa m = service.cariMahasiswa(id);
            System.out.println("Data yang akan dihapus: " + m);
            String konfirmasi = bacaString("Yakin ingin menghapus? (y/n): ");

            if (konfirmasi.equalsIgnoreCase("y")) {
                service.hapusMahasiswa(id);
                System.out.println("[OK] Data berhasil dihapus.\n");
            } else {
                System.out.println("[i] Penghapusan dibatalkan.\n");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("[!] " + e.getMessage() + "\n");
        } catch (RuntimeException e) {
            System.out.println("[!] Gagal menghapus dari database: " + e.getMessage() + "\n");
        }
    }

    // ---------------------------------------------------------------

    private static int bacaInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("[!] Harus berupa angka. Coba lagi.");
            }
        }
    }

    private static String bacaString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static String bacaStringOpsional(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
}
