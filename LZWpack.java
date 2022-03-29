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
    }

    public static void pack() {

        /**
         * Declare other variables
         */
        int maxPhraseNumber = 16; // 16 phrases (0-F) in the dictionary
        int minBits = 0;
        int packingInt = 0; // hold data read in
        int totalBitsPacked = 0; // used to check the number of bits current stored

        try {
            /**
             * Declare IO
             */
            FileInputStream fis = new FileInputStream(new File("output.txt"));
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            FileOutputStream fos = new FileOutputStream(new File("packed.pack"));

            // read the first phrase number
            String line = br.readLine();

            while (line != null) { // continue until the EOF has been reached
                int phraseNumber = Integer.parseInt(line);

                // calculate number of bits in phrase number
                minBits = (int) Math.ceil((Math.log(maxPhraseNumber) / Math.log(2)));
                System.out.println(phraseNumber + " : " + maxPhraseNumber + " bits: " + minBits);
                // track new bits added
                totalBitsPacked += minBits;

                // copy packed phrase number to the output buffer
                packingInt |= phraseNumber << (INT - totalBitsPacked);

                int mask = (int) (Math.pow(2, totalBitsPacked) - 1) << (INT -
                        totalBitsPacked);
                packingInt &= mask;

                maxPhraseNumber++;

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

        return (byte) outputInt;
    }
}