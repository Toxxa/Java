import java.io.*;

public class Main {
    private static final String ALPHABET = "абвгґдеєжзиіїйклмнопрстуфхцчшщьюя.,\":-!? ";

    public static void main(String[] args) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            while (true) {
                System.out.println("Оберіть дію:");
                System.out.println("1. Зашифрувати текст");
                System.out.println("2. Розшифрувати текст");
                System.out.println("3. Криптоаналіз методом brute force");
                System.out.println("4. Криптоаналіз методом статистичного аналізу");
                System.out.println("5. Вийти з програми");
                System.out.print("Ваш вибір: ");
                String choice = reader.readLine();
                System.out.println();

                switch (choice) {
                    case "1":
                        encryptText(reader);
                        break;
                    case "2":
                        decryptText(reader);
                        break;
                    case "3":
                        bruteForceAttack(reader);
                        break;
                    case "4":
                        statisticalAnalysisAttack(reader);
                        break;
                    case "5":
                        System.out.println("Дякую за використання програми!");
                        return;
                    default:
                        System.out.println("Невірний вибір. Спробуйте ще раз.");
                        break;
                }

                System.out.println();
            }
        } catch (IOException e) {
            System.out.println("Помилка при читанні вводу: " + e.getMessage());
        }
    }

    private static void encryptText(BufferedReader reader) throws IOException {
        System.out.print("Введіть шлях до файлу з вихідним текстом: ");
        String inputFile = reader.readLine().trim();
        System.out.print("Введіть шлях до файлу, куди зберегти зашифрований текст: ");
        String outputFile = reader.readLine().trim();
        System.out.print("Введіть ключ шифрування: ");
        int key = Integer.parseInt(reader.readLine().trim());

        try (BufferedReader fileReader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter fileWriter = new BufferedWriter(new FileWriter(outputFile))) {
            String line;
            while ((line = fileReader.readLine()) != null) {
                String encryptedLine = encryptDecryptText(line, key);
                fileWriter.write(encryptedLine);
                fileWriter.newLine();
            }
            System.out.println("Текст успішно зашифровано та збережено у файлі " + outputFile);
        } catch (IOException e) {
            System.out.println("Помилка при читанні або записі файлу: " + e.getMessage());
        }
    }

    private static void decryptText(BufferedReader reader) throws IOException {
        System.out.print("Введіть шлях до файлу з зашифрованим текстом: ");
        String inputFile = reader.readLine().trim();
        System.out.print("Введіть шлях до файлу, куди зберегти розшифрований текст: ");
        String outputFile = reader.readLine().trim();
        System.out.print("Введіть ключ розшифрування: ");
        int key = Integer.parseInt(reader.readLine().trim());

        try (BufferedReader fileReader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter fileWriter = new BufferedWriter(new FileWriter(outputFile))) {
            String line;
            while ((line = fileReader.readLine()) != null) {
                String decryptedLine = encryptDecryptText(line, -key);
                fileWriter.write(decryptedLine);
                fileWriter.newLine();
            }
            System.out.println("Текст успішно розшифровано та збережено у файлі " + outputFile);
        } catch (IOException e) {
            System.out.println("Помилка при читанні або записі файлу: " + e.getMessage());
        }
    }

    private static void bruteForceAttack(BufferedReader reader) throws IOException {
        System.out.print("Введіть шлях до файлу з зашифрованим текстом: ");
        String inputFile = reader.readLine().trim();
        System.out.print("Введіть шлях до файлу, куди зберегти розшифрований текст: ");
        String outputFile = reader.readLine().trim();

        try (BufferedReader fileReader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter fileWriter = new BufferedWriter(new FileWriter(outputFile))) {
            String line;
            while ((line = fileReader.readLine()) != null) {
                System.out.println("Зашифрований текст: " + line);
                System.out.println("Розшифрований текст (перебір ключів):");
                for (int key = 1; key <= ALPHABET.length(); key++) {
                    String decryptedLine = encryptDecryptText(line, -key);
                    System.out.println("Ключ " + key + ": " + decryptedLine);
                    fileWriter.write("Ключ " + key + ": " + decryptedLine);
                    fileWriter.newLine();
                }
                System.out.println();
            }
            System.out.println("Розшифровані тексти збережено у файлі " + outputFile);
        } catch (IOException e) {
            System.out.println("Помилка при читанні або записі файлу: " + e.getMessage());
        }
    }

    private static void statisticalAnalysisAttack(BufferedReader reader) throws IOException {
        System.out.print("Введіть шлях до файлу з зашифрованим текстом: ");
        String inputFile = reader.readLine().trim();
        System.out.print("Введіть шлях до файлу з текстом для статистичного аналізу: ");
        String analysisFile = reader.readLine().trim();
        System.out.print("Введіть шлях до файлу, куди зберегти розшифрований текст: ");
        String outputFile = reader.readLine().trim();

        try (BufferedReader cipherFileReader = new BufferedReader(new FileReader(inputFile));
             BufferedReader analysisFileReader = new BufferedReader(new FileReader(analysisFile));
             BufferedWriter fileWriter = new BufferedWriter(new FileWriter(outputFile))) {
            String cipherLine;
            String analysisLine;

            while ((cipherLine = cipherFileReader.readLine()) != null && (analysisLine = analysisFileReader.readLine()) != null) {
                System.out.println("Зашифрований текст: " + cipherLine);
                System.out.println("Статистичний аналіз тексту: " + analysisLine);
                System.out.println("Розшифрований текст (статистичний аналіз):");

                int key = findKeyByFrequency(cipherLine, analysisLine);
                String decryptedLine = encryptDecryptText(cipherLine, -key);
                System.out.println("Знайдений ключ: " + key);
                System.out.println("Розшифрований текст: " + decryptedLine);

                fileWriter.write("Знайдений ключ: " + key);
                fileWriter.newLine();
                fileWriter.write("Розшифрований текст: " + decryptedLine);
                fileWriter.newLine();
                fileWriter.newLine();
            }
            System.out.println("Розшифровані тексти збережено у файлі " + outputFile);
        } catch (IOException e) {
            System.out.println("Помилка при читанні або записі файлу: " + e.getMessage());
        }
    }

    private static String encryptDecryptText(String text, int key) {
        StringBuilder result = new StringBuilder();
        for (char c : text.toCharArray()) {
            int index = ALPHABET.indexOf(Character.toLowerCase(c));
            if (index != -1) {
                int newIndex = (index + key + ALPHABET.length()) % ALPHABET.length();
                result.append(ALPHABET.charAt(newIndex));
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    private static int findKeyByFrequency(String cipherText, String analysisText) {
        int maxMatches = 0;
        int bestKey = 0;

        for (int key = 1; key <= ALPHABET.length(); key++) {
            String decryptedText = encryptDecryptText(cipherText, -key);
            int matches = countCharacterMatches(decryptedText, analysisText);
            if (matches > maxMatches) {
                maxMatches = matches;
                bestKey = key;
            }
        }

        return bestKey;
    }

    private static int countCharacterMatches(String text1, String text2) {
        int matches = 0;
        for (int i = 0; i < text1.length(); i++) {
            if (text1.charAt(i) == text2.charAt(i)) {
                matches++;
            }
        }
        return matches;
    }
}
