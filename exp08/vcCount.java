import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileProcessor extends Thread {
    private String filename;
    private int vowels = 0;
    private int consonants = 0;

    public FileProcessor(String filename) {
        this.filename = filename;
    }

    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;

            while ((line = reader.readLine()) != null) {
                for (char c : line.toCharArray()) {
                    if (Character.isLetter(c)) {
                        if ("AEIOUaeiou".indexOf(c) != -1) {
                            vowels++;
                        } else {
                            consonants++;
                        }
                    }
                }
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getVowels() {
        return vowels;
    }

    public int getConsonants() {
        return consonants;
    }

    public static void main(String[] args) throws InterruptedException {
        int numThreads = 3;
        FileProcessor[] processors = new FileProcessor[numThreads];

        // Create the threads
        for (int i = 0; i < numThreads; i++) {
            processors[i] = new FileProcessor("file" + (i + 1) + ".txt");
        }

        // Start the threads
        for (FileProcessor processor : processors) {
            processor.start();
        }

        // Wait for the threads to finish
        for (FileProcessor processor : processors) {
            processor.join();
        }

        // Aggregate the results
        int totalVowels = 0;
        int totalConsonants = 0;

        for (FileProcessor processor : processors) {
            totalVowels += processor.getVowels();
            totalConsonants += processor.getConsonants();
        }

        System.out.println("Total vowels: " + totalVowels);
        System.out.println("Total consonants: " + totalConsonants);
    }
}
