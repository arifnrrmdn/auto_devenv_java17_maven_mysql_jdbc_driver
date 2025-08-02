/*
 * ================================================
 * SetupJavaDevEnv.java
 * --------------------------------
 * Script installer otomatis untuk:
 *   - Java 17 (JDK)
 *   - Apache Maven
 *   - Lombok
 *   - MySQL (Server + Client)
 *   - JDBC Driver MySQL
 *
 * Author  : Arif N Ramadhan
 * GitHub  : https://github.com/arifnrrmdn
 * Created : 02-08-2025
 * License : Free to use with attribution
 * ================================================
 */

import java.io.*;
import java.util.Arrays;

public class SetupJavaDevEnv {

    public static void main(String[] args) {
        // Created by Arif N Ramadhan
        System.out.println("🚀 Memulai Setup Java Development Environment...");

        // Update sistem
        runCommand("sudo", "apt", "update");
        runCommand("sudo", "apt", "upgrade", "-y");

        // Install Java 17
        installPkg("java", "openjdk-17-jdk");

        // Install Maven
        installPkg("mvn", "maven");

        // Install Lombok
        installLombok();

        // Install MySQL
        installPkg("mysql", "mysql-server", "mysql-client");

        // Install JDBC MySQL Driver
        installJdbcDriver();

        System.out.println("🎉 Setup selesai! Java 17, Maven, Lombok, JDBC, dan MySQL siap digunakan.");
    }

    private static void installPkg(String checkCmd, String... pkgNames) {
        // Created by Arif N Ramadhan
        if (!isInstalled(checkCmd)) {
            System.out.println("🔧 Menginstal " + String.join(", ", pkgNames) + "...");
            runCommand("sudo", "apt", "install", "-y", String.join(" ", pkgNames));
        } else {
            System.out.println("✅ " + String.join(", ", pkgNames) + " sudah terinstal.");
        }
    }

    private static void installLombok() {
        // Created by Arif N Ramadhan
        String lombokJar = "lombok.jar";
        File file = new File(lombokJar);

        if (!file.exists()) {
            System.out.println("🔧 Mengunduh Lombok...");
            runCommand("wget", "https://projectlombok.org/downloads/lombok.jar", "-O", lombokJar);

            if (file.exists()) {
                System.out.println("✅ Lombok berhasil diunduh: " + file.getAbsolutePath());
                System.out.println("ℹ️ Untuk mengaktifkan Lombok di IDE, jalankan:");
                System.out.println("   java -jar lombok.jar");
            } else {
                System.out.println("⚠️ Gagal mengunduh Lombok.");
            }
        } else {
            System.out.println("✅ Lombok sudah ada di direktori.");
        }
    }

    private static void installJdbcDriver() {
        // Created by Arif N Ramadhan
        String jdbcJar = "mysql-connector-j.jar";
        File file = new File(jdbcJar);

        if (!file.exists()) {
            System.out.println("🔧 Mengunduh MySQL JDBC Driver...");
            runCommand("wget", "https://dev.mysql.com/get/Downloads/Connector-J/mysql-connector-j-8.3.0.tar.gz", "-O", "mysql-connector.tar.gz");
            runCommand("tar", "-xvzf", "mysql-connector.tar.gz");
            runCommand("bash", "-c", "find . -name \"mysql-connector-j-*.jar\" -exec cp {} " + jdbcJar + " \\;");
            System.out.println("✅ JDBC Driver berhasil diunduh sebagai " + jdbcJar);
        } else {
            System.out.println("✅ JDBC Driver sudah ada di direktori.");
        }
    }

    private static boolean isInstalled(String command) {
        // Created by Arif N Ramadhan
        try {
            ProcessBuilder builder = new ProcessBuilder("which", command);
            Process process = builder.start();
            return process.waitFor() == 0;
        } catch (IOException | InterruptedException e) {
            return false;
        }
    }

    private static void runCommand(String... command) {
        // Created by Arif N Ramadhan
        try {
            ProcessBuilder builder = new ProcessBuilder(command);
            builder.redirectErrorStream(true);
            Process process = builder.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.out.println("⚠️ Perintah gagal: " + Arrays.toString(command));
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
