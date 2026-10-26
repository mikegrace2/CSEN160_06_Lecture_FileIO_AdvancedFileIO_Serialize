package scu.csen160;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class E_RandomAccessFileExample3 {
    public static void main(String[] args) {
        File file = new File("info.txt"); // place this file in project root (working directory)
        try {
            ensureContent(file);
            replaceTrailingWithEtc(file);
            System.out.println("Updated file: " + file.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
        }
    }

    // Create file with the original sentence if it does not exist
    private static void ensureContent(File file) throws IOException {
        if (!file.exists()) {
            String text = "Audio files can be stored in a variety of formats such as wav, wma, mp3, aiff, au and many others.";
            Files.write(file.toPath(), text.getBytes(StandardCharsets.UTF_8));
        }
    }

    // Locate "and many others." and replace it with "etc." using RandomAccessFile (seek, write, setLength)
    private static void replaceTrailingWithEtc(File file) throws IOException {
        String target = "and many others.";
        String replacement = "etc.";
        byte[] all = Files.readAllBytes(file.toPath());
        String content = new String(all, StandardCharsets.UTF_8);
        int idx = content.indexOf(target);
        if (idx == -1) {
            System.out.println("Target substring not found. No change made.");
            return;
        }
        try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
            raf.seek(idx);
            raf.write(replacement.getBytes(StandardCharsets.UTF_8));
            raf.setLength(idx + replacement.length());
        }
    }
}
