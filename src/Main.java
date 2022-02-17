import java.io.*;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    private static final int TOTAL_GUESSES = 8;
    private static final String PATH = "/Users/nhulston/IdeaProjects/6wordle/src/6letterWordle.txt";
    private static final File FILE = new File(PATH);
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final String[] GUESSES = new String[TOTAL_GUESSES];
    private static String WORD = "";

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";

    public static void main(String[] args) throws IOException {
        WORD = getRandomWord();
        System.out.println("The word is " + WORD.toUpperCase());

        for (int i = 0; i < TOTAL_GUESSES; i++) {
            printBoard();
            if (takeGuess(i)) {
                if (i <= 1) {
                    System.out.println("GENIUS");
                } else if (i <= 3) {
                    System.out.println("SUPER SMART");
                } else if (i <= 5) {
                    System.out.println("AMAZING");
                } else {
                    System.out.println("GOOD JOB");
                }
                break;
            }
        }
        printBoard();
        System.out.println("The word was " + ANSI_GREEN + WORD.toUpperCase());
    }

    private static String getRandomWord() throws IOException {
        // TODO get by date
        final RandomAccessFile f = new RandomAccessFile(FILE, "r");
        final long randomLocation = (long) (Math.random() * f.length());
        f.seek(randomLocation);
        f.readLine();
        String randomLine = f.readLine();
        f.close();
        return randomLine;
    }

    private static void printBoard() {
        for (String s : GUESSES) {
            System.out.println(Objects.requireNonNullElse(s, "_ _ _ _ _ _"));
        }
    }

    private static boolean takeGuess(int index) throws IOException {
        while (true) {
            System.out.println("\nEnter a guess:");
            String guess = SCANNER.nextLine();
            if (guess.length() != 6) {
                System.out.println("Guess must be a 6 letter word. Try again");
            } else if (!guess.matches("[a-zA-Z]+")) {
                System.out.println("Guess must only contain letters. Try again.");
            } else if (!guessIsAWord(guess)) {
                System.out.println("Word not found in our list. Try again.");
            } else {
                updateArray(index, guess);
                if (WORD.equalsIgnoreCase(guess)) {
                    return true;
                } else {
                    return false;
                }
            }
        }
    }

    private static boolean guessIsAWord(String guess) throws IOException {
        String line;
        FileReader fReader = new FileReader(FILE);
        BufferedReader fileBuff = new BufferedReader(fReader);
        while ((line = fileBuff.readLine()) != null) {
            if (line.equalsIgnoreCase(guess)) {
                fileBuff.close();
                return true;
            }
        }
        fileBuff.close();

        return false;
    }

    private static void updateArray(int index, String guess) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            char guessedChar = guess.charAt(i);
            sb.append(getColor(guessedChar, i)).append(Character.toUpperCase(guessedChar)).append(" ").append(ANSI_RESET);
        }

        GUESSES[index] = sb.toString();
    }

    private static String getColor(char guessedChar, int index) {
        guessedChar = Character.toLowerCase(guessedChar);
        char actualChar = WORD.charAt(index);
        if (guessedChar == actualChar) {
            return ANSI_GREEN;
        } else if (WORD.contains(String.valueOf(guessedChar))) {
            return ANSI_YELLOW;
        } else {
            return ANSI_BLACK;
        }
    }
}
