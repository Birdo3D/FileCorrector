package fr.birdo.filecorrector;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Stream;

public class FileCorrector {

    static File src_file = new File("C:\\Users\\dylan\\Documents\\Workspace Java\\FileCorrector\\tests\\file.csv");
    static File errors_file = new File("C:\\Users\\dylan\\Documents\\Workspace Java\\FileCorrector\\tests\\detected_errors.txt");
    static File dest_file = new File("C:\\Users\\dylan\\Documents\\Workspace Java\\FileCorrector\\tests\\file_corrected.txt");

    public static void main(String[] args) {
        if (src_file.exists()) {
            try {
                System.out.println("Votre fichier fait " + getLengt(src_file) + " lignes, voulez vous l'analiser ? (o/n)");
                Scanner scanner = new java.util.Scanner(System.in);
                if (scanner.next().equalsIgnoreCase("o")) {
                    List<Integer> errors = new ArrayList<>();
                    for (int i = 1; i <= getLengt(src_file); i++) {
                        System.out.println("ligne " + i);
                        try (Stream<String> lines = Files.lines(Paths.get(String.valueOf(src_file)))) {
                            String line = lines.skip(i - 1).findFirst().get();
                            System.out.println(line);
                            String[] values = line.split(",");
                            if (values.length != 57)
                                errors.add(i);
                        }
                    }
                    if (errors.size() != 0) {
                        FileWriter writer = new FileWriter(errors_file);
                        writer.write(errors.size());
                        writer.flush();
                        writer.close();
                        System.out.println("Votre fichier contient " + errors.size() + " erreurs (errors.txt), voulez vous les corriger automatiquement ? (o/n)");
                        if (scanner.next().equalsIgnoreCase("o")) {
                            InputStream is = null;
                            OutputStream os = null;
                            try {
                                is = new FileInputStream(src_file);
                                os = new FileOutputStream(dest_file);
                                byte[] buffer = new byte[1024];
                                int len;
                                while ((len = is.read(buffer)) > 0) {
                                    os.write(buffer, 0, len);
                                }
                                is.close();
                                os.close();
                                System.out.println("Le fichier à été correctement copié !");

                                //TODO repair file

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    } else
                        System.out.println("Votre fichier ne contient aucune erreur ! Enjoy :)");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Le fichier n'existe pas !");
        }
    }

    public static int getLengt(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        int lines = 0;
        while (reader.readLine() != null)
            lines++;
        reader.close();
        return lines;
    }
}