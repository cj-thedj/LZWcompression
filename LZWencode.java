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

    private static Trie trie;
    
    /**
     * Implementation of a multiway trie.
     * 
     * Trie is a tree that can have more than two children. It is used
     * by the encoder for the dictionary.
     */
    private class Trie {
        private static final int HEX_SET = 16; // define size of alphabet
        private TrieNode _root;
        private int _totalNodes; // used for tracking the phrase number

        
        
        /**
         * Holds information about the phrase number and the nibble. It can be retrieved
         * from the structure by traversing down the path of the trie
         */
        class TrieNode {

            /* Declare properties */
            private ArrayList<TrieNode> _children = new ArrayList<TrieNode>(); // tracks all the children a node may have
            private int _phraseNumber;
            private byte _phraseNibble;
    
            /**
             * Constructor
             * 
             * Used for instatiating root nodes of a Trie.
             */
            TrieNode() {
                _totalNodes++;
            }
    
            /**
             * Overloaded Constructor.
             * 
             * Used to instatiate a node storing information
             * 
             * @param phraseNibble
             * @param phraseNumber
             */
            TrieNode(byte phraseNibble, int phraseNumber) {
                _totalNodes++;
                _phraseNibble = phraseNibble;
                _phraseNumber = phraseNumber;
    
            }
    
            public ArrayList<TrieNode> getChildren() {
                return _children;
            }
    
            public int getPhraseNum() {
                return _phraseNumber;
            }
    
            public byte getPhraseNibble() {
                return _phraseNibble;
            }
    
            public void setChildren(byte nibble) {
                _children.add(new TrieNode(nibble, _totalNodes));
            }
        }
    
        /**
         * Constructor
         * 
         * Instatiates a new Trie with a root node.
         */
        public Trie() {
            _totalNodes = 0;
            _root = new TrieNode();
    
            // initialize trie with expected symbols
            for (int i = 0; i < HEX_SET; i++)
                _root.setChildren((byte) i);
        }
        
        /**
         * Adds a new trie node into the trie.
         * 
         * @param childNibble The value to be added to the child
         * @param parentNode The parent who the new node will be appended to
         */
        public void insert(byte childNibble, TrieNode parentNode) {
            parentNode.setChildren(childNibble);
        }

        public TrieNode search(int currNibble, TrieNode currNode) {
    
            // search if one of its children has the key
            for (int i = 0; i < currNode.getChildren().size(); i++) {
                if (currNode.getChildren().get(i).getPhraseNibble() == currNibble)
                    return currNode.getChildren().get(i);
            }
    
            return null;
        }
    
        public TrieNode getRoot() {
            return _root;
        }
    
    }

    public void encode() {
        try {
            File file = new File("input.txt");
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            ArrayList<Byte> nibbles = new ArrayList<Byte>(); // stores the split bytes

            byte[] bytes = bis.readAllBytes();

            // instantiate a new trie that has 1st level symbols
            trie = new Trie();
            Trie.TrieNode parentNode = trie.getRoot();
            Trie.TrieNode tempNode = parentNode;
            Trie.TrieNode childNode;

            for (byte b : bytes) {
                byte highNibble = (byte) ((b >> 4) & 0x0f);
                byte lowNibble = (byte) (b & 0x0f);

                nibbles.add(highNibble);
                nibbles.add(lowNibble);
            }

            byte tempNibble = 0;
            int phraseStart = 0;

            for (byte nibble : nibbles) { // check if each nibble is in the trie
                for (int i = phraseStart; i < nibbles.size(); i++) { // search for matching phrases

                    // save this nibble for later
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

            LZWpack.pack();
            LZWunpack.unpack();

            System.out.print("\n");
            bis.close();

        } catch (Exception e) {
            System.err.println(e);
        }
    }
    public static void main(String[] args) {
        LZWencode l = new LZWencode();
        l.encode();
    }
}