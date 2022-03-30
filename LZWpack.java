import java.io.*;

public class LZWpack {

    /**
     * Declare Constant Variables
     */
    static final int BYTE = 8; // number of bits in byte
    static final int INT = 32; // number of bits in integer
    static final int SHIFT = 24; // used for shifting to either MSB or LSB
    static final int OUTPUT_MASK = 0xff000000;
    static final int LSB_MASK = 0xff;

    /**
     * Main method for running bit packing
     * 
     * @param args
     */
    public static void main(String[] args) {
        LZWpack.pack();
        System.out.print("------------------------------------------------\n");
        LZWunpack.unpack();
    }

    /**
     * Performs bit packing
     */
    public static void pack() {
        
        /* Declare method variables */
        int maxPhraseNumber = 16; // 16 phrases (0-F) in the dictionary
        int packingInt = 0; // hold data read in
        int totalBitsPacked = 0; // used to check the number of bits current stored

        try {

            /* Declare IO streams */
            FileInputStream fis = new FileInputStream(new File("output.txt"));
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            FileOutputStream fos = new FileOutputStream(new File("packed.pack"));

            // read the first phrase number
            String line = br.readLine();

            while (line != null) { // continue until the EOF has been reached
                
                // get the integral value of the read phrase number
                int phraseNumber = Integer.parseInt(line);
                
                // track new bits added
                totalBitsPacked += calculateMinBits(++maxPhraseNumber);

                // copy packed phrase number to the output buffer
                packingInt |= phraseNumber << (INT - totalBitsPacked);

                // 
                packingInt &= calculateMask(totalBitsPacked);
                
                while (totalBitsPacked >= BYTE) { // there is a byte to output
                    
                    fos.write(outputByte(packingInt));
                    
                    // shift the next byte to most significant position
                    packingInt <<= BYTE;
                    totalBitsPacked -= BYTE;
                }
                line = br.readLine();
            }

            while (totalBitsPacked > -1) { // there are still bits left after reading all

                fos.write(outputByte(packingInt));

                packingInt <<= BYTE;
                totalBitsPacked -= BYTE;
            }

            fos.flush();
            fos.close();
            br.close();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    private static byte outputByte(int packingInt) {
        int outputInt = 0;

        // isolate the byte at most significant position
        outputInt = packingInt & OUTPUT_MASK;

        // move byte to least significant position
        outputInt >>>= SHIFT;

        //System.out.println(Integer.toBinaryString(outputInt));

        return (byte) outputInt;
    }

    private static int calculateMinBits(int k) {
        return (int) Math.ceil((Math.log(k) / Math.log(2)));
    }

    private static int calculateMask(int n) {
        return (int) (Math.pow(2, n) - 1) << (INT - n);
    }
}