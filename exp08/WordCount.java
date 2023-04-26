import java.io.*;
import java.util.*;

public class WordCount {
    public static void main(String[] args) {
        List<WordCountThread> threads = new ArrayList<>();
        Map<String, Integer> wordCount = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader("input.txt"))) {
            String line;
            StringBuilder paragraph = new StringBuilder();
            while ((line = br.readLine()) != null) {
                if (!line.isEmpty()) {
                    // Add the line to the current paragraph

                    paragraph.append(line).append(" ");
                } else if (paragraph.length() > 0) {
                    // Create a WordCountThread for the current paragraph and start the thread
                    final String text = paragraph.toString();
                    WordCountThread thread = new WordCountThread(text);
                    threads.add(thread);
                    thread.start();
                    // Reset the paragraph
                    paragraph.setLength(0);
                }
            }
            if (paragraph.length() > 0) {
                // Create a WordCountThread for the last paragraph and start the thread
                final String text = paragraph.toString();
                WordCountThread thread = new WordCountThread(text);
                threads.add(thread);
                thread.start();
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(1);
        }
        // Wait for all the threads to finish and combine the word counts
        for (WordCountThread thread : threads) {
            try {
                thread.join();
                Map<String, Integer> partialWordCount = thread.getWordCount();
                for (Map.Entry<String, Integer> entry : partialWordCount.entrySet()) {
                    String word = entry.getKey();
                    int count = entry.getValue();
                    wordCount.put(word, wordCount.getOrDefault(word, 0) + count);
                }
            } catch (InterruptedException e) {
                System.err.println("Error: " + e.getMessage());
                System.exit(1);
            }
        }
        // Print the total number of words and word count
        System.out.println("Total words: " + wordCount.size());
        for (Map.Entry<String, Integer> entry : wordCount.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    private static class WordCountThread extends Thread {
        private String text;
        private Map<String, Integer> wordCount;

        public WordCountThread(String text) {
            this.text = text;
        }

        public void run() {
            wordCount = new HashMap<>();
            String[] words = text.split("\\s+");
            for (String word : words) {
                wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
            }
        }

        public Map<String, Integer> getWordCount() {
            return wordCount;
        }
    }
}