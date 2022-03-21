import java.io.File;
import java.io.FileInputStream;
import java.io.BufferedInputStream;
import java.util.ArrayList;

/**
 * The encoder, with LZW algorithm:
 * - Takes input as a stream of hex digits (chars) from stdin
 * - Outputs phrase number, one phrase number per line
 * - Utilise a multiway trie
 */
public class LZWencode {
    public static void main(String[] args) {
        try {
            File file = new File("input.txt");
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            ArrayList<Integer> nibbles = new ArrayList<Integer>();

            byte[] byteArray = bis.readAllBytes();

            // instantiate a new trie that has 1st level symbols
            Trie t = new Trie();
            Trie.TrieNode parentNode = t.getRoot();
            Trie.TrieNode childNode, tempNode;

            t.print();
            System.out.println("---Phrase Numbers---");

            // extract each 'nibble' or hexadecimal digit
            for (byte b : byteArray) {
                int highNibble = (b >> 4) & 0x0f;
                nibbles.add(highNibble);

                int lowNibble = b & 0x0f;
                nibbles.add(lowNibble);
            }

            tempNode = parentNode;
            int tempNibble = 0;
            int phraseStart = 0;

            // check if each nibble is in the trie
            for (int nibble : nibbles) {

                // search for matching phrases
                for (int i = phraseStart; i < nibbles.size(); i++) {

                    tempNibble = nibbles.get(i);

                    // save current state of parent
                    tempNode = parentNode;

                    // search if parent's children have nibble
                    childNode = t.search(tempNibble, parentNode);

                    // if not null, continue to the next nibble
                    if (childNode != null) {

                        // if child has children, it becomes new parent
                        parentNode = childNode;
                        continue;

                    } else
                        break;
                }

                System.out.println(parentNode.getPhraseNum());
                t.insert(nibble, parentNode);

                // start back at the root
                parentNode = t.getRoot();
                phraseStart++;
            }

            System.out.print("\n");
            bis.close();

        } catch (Exception e) {
            System.err.println(e);
        }
    }
}