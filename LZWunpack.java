import java.io.*;

public class LZWunpack {

    /* Declare Constants */
    static final int BYTE = 8; // number of bits in byte
    static final int INT = 32; // number of bits in integer
    static final int SHIFT = 24; // used to shift to either MSB or LSB
    static final int LSB_MASK = 0xff;
    static final int MSB_MASK = 0xff000000;

    /**
     * Performs bit packing for a list of phrase numbers.
     * For this to work correctly, phrase numbers must be
     * on a new line each.
     */
    public static void unpack() {

        /**
         * Declare other variables
         */
        int maxPhraseNumber = 16; // 16 phrases (0-F) in the dictionary
        int minBits = calculateMinBits(maxPhraseNumber);
        int totalBitsUnpacked = 0; // used to check the number of bits current stored
        int unpackingInt = 0;
        int output = 0;

        try {
            /* Declare IO stream */
            FileInputStream fis = new FileInputStream(new File("packed.pack"));
            BufferedInputStream bis = new BufferedInputStream(fis);
            FileOutputStream fos = new FileOutputStream(new File("unpacked.txt"));

            int packedByte = bis.read();

            while (packedByte != -1) {
                // calculate the number of bits needed

                // ensure that we get the first byte of userful data
                packedByte &= calculateMask(MSB_MASK);
                
                // every read is 8-bits
                totalBitsUnpacked += BYTE;
                unpackingInt |= packedByte << (INT - totalBitsUnpacked);

                unpackingInt &= calculateMask(totalBitsUnpacked);
                minBits = calculateMinBits(maxPhraseNumber);

                if (totalBitsUnpacked >= minBits) {
                    output = unpackingInt & calculateMask(minBits);
                    output >>>= (INT - minBits);

                    unpackingInt <<= minBits;
                    totalBitsUnpacked -= minBits;

                    System.out.println(output);
                    maxPhraseNumber++;
                }

                packedByte = bis.read();
            }

            output = unpackingInt & calculateMask(minBits);
            // output = unpackingInt & MSB_MASK;

            output >>>= (INT - minBits);

            unpackingInt <<= minBits;
            totalBitsUnpacked -= minBits;

            System.out.println(output);

            fos.flush();
            fos.close();
            bis.close();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    private static int calculateMask(int bits) {
        return (int) (Math.pow(2, bits) - 1) << (INT - bits);
    }

    private static int calculateMinBits(int k) {
        return (int) Math.ceil((Math.log(k) / Math.log(2)));
    }
}