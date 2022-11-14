public class Encryption {

    private static final int DEFAULT_ROTARY_NUM = 13;

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

    private static boolean isEncryptMode(String mode) {
        if ("encrypt".equals(mode))
            return true;

        if ("decrypt".equals(mode))
            return false;

        System.err.println("Invalid mode; expected encrypt or decrypt; got: " + mode);
        System.exit(-1);
        return false;
    }

    private static int getRotaryNum(String num) {
        if(!num.matches("rot\\d+")) {
            System.err.println("Unknown encryption algorithm: " + num);
            System.exit(-1);
        }
        return Integer.parseInt(num.substring(3));
    }

    public static String encrypt(int rotaryNum, String plainText) {
        return rotate(rotaryNum, plainText);
    }

    public static String decrypt(int rotaryNum, String encryptedText) {
        return rotate(-rotaryNum, encryptedText);
    }

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

    private static boolean isLetter(char chr) {
        return isLowercase(chr) || isUppercase(chr);
    }

    private static boolean isUppercase(char chr) {
        return (chr >= 'A' && chr <= 'Z');
    }

    private static boolean isLowercase(char chr) {
        return (chr >= 'a' && chr <='z');
    }

}