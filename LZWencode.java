import java.io.*;

/**
 * The encoder, with LZW algorithm:
 *  - Takes input as a stream of hex digits (chars) from stdin
 *  - Outputs phrase number, one phrase number per line
 *  - Utilise a multiway trie
 */
public class LZWencode {
    public static void main(String[] args) {       
        try {
            File file = new File("input.txt");
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);

            byte[] byteArray = null;
            
            byteArray = bis.readAllBytes();

             // Iterating over using for each loop
            for (byte b : byteArray) {
                String st = String.format("%02X", b);
                System.out.println((char)b + " = " + st);
            // Printing the content of byte array
            
        }

        bis.close();

        } catch (Exception e) {
            System.err.println(e);
        }
    }
}