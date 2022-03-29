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
            ArrayList<Byte> nibbles = new ArrayList<Byte>(); // stores the split bytes

            byte[] bytes = bis.readAllBytes();

            // instantiate a new trie that has 1st level symbols
            Trie trie = new Trie();
            Trie.TrieNode parentNode = trie.getRoot();
            Trie.TrieNode childNode, tempNode;

            for (byte b : bytes) {
                byte highNibble = (byte) ((b >> 4) & 0x0f);
                byte lowNibble = (byte) (b & 0x0f);

                nibbles.add(highNibble);
                nibbles.add(lowNibble);
            }

            tempNode = parentNode;
            byte tempNibble = 0;
            int phraseStart = 0;

            for (byte nibble : nibbles) { // check if each nibble is in the trie
                for (int i = phraseStart; i < nibbles.size(); i++) { // search for matching phrases

                    tempNibble = nibbles.get(i);

                    // save current state of parent
                    tempNode = parentNode;

                    // search if parent's children have nibble
                    childNode = trie.search(tempNibble, parentNode);

                    if (childNode != null) { // if not null, continue to the next nibble

                        // if child has children, it becomes new parent
                        parentNode = childNode;
                        continue;

                    } else
                        break;
                }

                System.out.println(parentNode.getPhraseNum());
                trie.insert(nibble, parentNode);

                // start back at the root
                parentNode = trie.getRoot();
                phraseStart++;
            }

            System.out.print("\n");
            bis.close();

        } catch (Exception e) {
            System.err.println(e);
        }
    }
}