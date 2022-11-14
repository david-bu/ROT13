public class Encryption {

    private static final int DEFAULT_ROTARY_NUM = 13;

    /**
     * receives the command line arguments and extracts the given values:
     *  - [mode] [text] (if mode == decrypt the rotaryNum is guessed)
     *  - [mode] [rotationMode] [text]
     * @param args command line arguments
     */
    public static void main(String[] args) {
        boolean inEncryptMode = true;
        int rotaryNum = DEFAULT_ROTARY_NUM;
        String text = "";

        switch (args.length) {
            case 2:
                inEncryptMode = isEncryptMode(args[0]);
                text = args[1];
                if (!inEncryptMode) {
                    rotaryNum = guessDecryptRotaryNum(text);
                    System.out.println("Using rot" + rotaryNum + " for decryption");
                }
                break;
            case 3:
                inEncryptMode = isEncryptMode(args[0]);
                rotaryNum = getRotaryNum(args[1]);
                text = args[2];
                break;
            default:
                System.err.println("Invalid number of input arguments; expected 2 or 3; got " + args.length);
                System.exit(-1);
        }

        if (inEncryptMode)
            System.out.println(encrypt(rotaryNum, text));
        else System.out.println(decrypt(rotaryNum, text));
    }

    /**
     * checks which mode is chosen
     * exits if mode is invalid
     * @param mode input string; value is checked for 'encrypt' or 'decrypt'
     * @return true if encrypt mode chosen, false if decrypt mode chosen
     */
    private static boolean isEncryptMode(String mode) {
        if ("encrypt".equals(mode))
            return true;

        if ("decrypt".equals(mode))
            return false;

        System.err.println("Invalid mode; expected encrypt or decrypt; got: " + mode);
        System.exit(-1);
        return false;
    }

    /**
     * extracts the rotary num out of a string of type 'rot[num]'
     * @param num the string containing the rotary num; should be 'rot[num]'
     * @return rotary number which is chosen
     */
    private static int getRotaryNum(String num) {
        if(!num.matches("rot\\d+")) {
            System.err.println("Unknown encryption algorithm: " + num);
            System.exit(-1);
        }
        return Integer.parseInt(num.substring(3));
    }

    /**
     * rotates the plain text by rotaryNum
     * @param rotaryNum the value to rotate each letter
     * @param plainText the text where the letters should be rotated
     * @return the rotated text
     */
    public static String encrypt(int rotaryNum, String plainText) {
        return rotate(rotaryNum, plainText);
    }

    /**
     * rotates the encrypted text by -rotaryNum
     * @param rotaryNum the value to rotate each letter
     * @param encryptedText the text where the letters should be rotated
     * @return the rotated text
     */
    public static String decrypt(int rotaryNum, String encryptedText) {
        return rotate(-rotaryNum, encryptedText);
    }

    /**
     * adds the rotaryNum (in the range 'A' to 'Z' or 'a' to 'z' if it's uppercase or lowercase) to each letter
     * in the text and returns the rotated text
     * @param rotaryNum the value to add to each letter
     * @param text the text where the rotaryNum should be added
     * @return a copy of text with the rotated letters
     */
    private static String rotate(int rotaryNum, String text) {
        char[] chars = text.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char chr = chars[i];
            if (!isLetter(chr))
                continue;

            int alphabetStart = isUppercase(chr)? 'A' : 'a';
            chr += rotaryNum;
            while (chr < alphabetStart)
                chr += 26;
            chr -= alphabetStart;
            chr %= 26;
            chr += alphabetStart;
            chars[i] = chr;
        }

        return String.copyValueOf(chars);
    }

    /**
     * tries to get the rotary num of an encrypted string by looking for the most used chr and interpreting it as an 'e'
     * @param encryptedText the encrypted text where the rotary num should be guessed
     * @return the rotary number
     */
    public static int guessDecryptRotaryNum(String encryptedText) {
        int[] letterCount = new int[26];
        char[] chars = encryptedText.toCharArray();
        for (char chr : chars) {
            if (!isLetter(chr))
                continue;

            int alphabetStart = isUppercase(chr) ? 'A' : 'a';
            chr -= alphabetStart;
            letterCount[chr] = letterCount[chr] + 1;
        }

        int eIndex = 4;
        int decryptedEPos = 0;
        for (int i = 0; i < letterCount.length; i++) {
            if (letterCount[decryptedEPos] < letterCount[i])
                decryptedEPos = i;
        }
        while (eIndex > decryptedEPos)
            decryptedEPos += 26;
        decryptedEPos -= eIndex;

        return decryptedEPos;
    }

    /**
     * checks if the char is a lowercase or uppercase letter
     * @param chr char to check if it's a letter
     * @return true if chr is a letter
     */
    private static boolean isLetter(char chr) {
        return isLowercase(chr) || isUppercase(chr);
    }

    /**
     * checks if the char is an uppercase letter
     * @param chr char to check if it's uppercase
     * @return true if chr is uppercase
     */
    private static boolean isUppercase(char chr) {
        return (chr >= 'A' && chr <= 'Z');
    }

    /**
     * checks if the char is a lowercase letter
     * @param chr char to check if it's lowercase
     * @return true if chr is lowercase
     */
    private static boolean isLowercase(char chr) {
        return (chr >= 'a' && chr <='z');
    }

}