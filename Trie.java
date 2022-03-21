
/**
 * Harmon Transfield and Caleb Pyne
 */

import java.util.ArrayList;

class Trie {
    // define alphabet size (0 - F)
    private static final int HEX_SET = 16; // 0, 1, 2, 3, 4, 5, 6, 7,8, 9, A, B, C, D, E, F
    private static TrieNode _root;
    private static int _totalNodes;
    ////////////////////////////////////////////////////////////////////////////////

    class TrieNode {
        private ArrayList<TrieNode> _children = new ArrayList<TrieNode>();
        private boolean _isLeaf;
        private int _phraseNumber;
        private int _phraseNibble;

        /**
         * Constructor
         * 
         * Used for instatiating root nodes of a Trie.
         */
        TrieNode() {
            _totalNodes++;
            _isLeaf = false;
        }

        /**
         * Overloaded Constructor.
         * 
         * Used to instatiate
         * 
         * @param hexNibble
         * @param phraseNumber
         */
        TrieNode(int hexNibble, int phraseNumber) {
            _totalNodes++;
            _isLeaf = false;
            _phraseNibble = hexNibble;
            _phraseNumber = phraseNumber;

        }

        public ArrayList<TrieNode> getChildren() {
            return _children;
        }

        public int getPhraseNum() {
            return _phraseNumber;
        }

        public int getPhraseHexDigit() {
            return _phraseNibble;
        }

        public boolean getIsLeaf() {
            return _isLeaf;
        }

        public void setChildren(int hexDigit) {
            _children.add(new TrieNode(hexDigit, _totalNodes));
            this._isLeaf = false;
        }
    }

    ////////////////////////////////////////////////////////////////////////////////

    /**
     * Constructor
     * 
     * Instatiates a new Trie with a root node.
     */
    public Trie() {
        _totalNodes = 0;
        _root = new TrieNode();

        // initialize trie with expected symbols
        for (int i = 0; i < HEX_SET; i++) {

            _root.setChildren(i);
        }
    }

    public TrieNode search(int nibble, TrieNode currNode) {

        System.out.println("Nibble = " + nibble);

        // search if one of its children has the key
        for (int i = 0; i < currNode.getChildren().size(); i++) {
            if (currNode.getChildren().get(i).getPhraseHexDigit() == nibble)
                return currNode.getChildren().get(i);
        }

        return null;
    }

    // prints out all current nodes and associated phrases
    public void print() {
        int testChar;
        int testNum;

        for (int i = 0; i < _root.getChildren().size(); i++) {
            testChar = _root.getChildren().get(i).getPhraseHexDigit();
            testNum = _root.getChildren().get(i).getPhraseNum();

            String stx = String.format("%2X", testChar);

            System.out.println(stx + " = " + testNum);
        }
    }

    public TrieNode getRoot() {
        return _root;
    }

    public void insert(int hexDigit, TrieNode parentNode) {
        parentNode.setChildren(hexDigit);
    }
}