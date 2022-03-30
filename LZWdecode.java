//takes input file "encoded.txt" and performs LZW decompression
// encoded.txt must be formatted to have one codeword per line, with no whitespace

import java.io.*;

public class LZWdecode {
public static void main(String[] args) throws IOException {

FileReader filereader = new FileReader("encoded.txt");

BufferedReader reader = new BufferedReader(filereader);

int cw = 0; //current codeword
int old = 0; //old cw
String read = ""; // current line

//index set up
int min = 0;  
int max = 128;  //set dictionary size
int endval = max; //end of index for new entries

String[] idx = new String[1000]; //character index that can hold 1000 symbols

for (int i = min; i < max; i++) {  

            idx[i]= Character.toString((char) i);  //fill index with char up to 255
}

read = reader.readLine(); //initialize with first letter to avoid null in index entry
cw = Integer.parseInt(read); //
idx[endval] = idx[cw]; //
System.out.print(idx[cw]);//
endval++;//

while (read != null) { //checks for end of file (will not work if blank line in file)
    try{
    read = reader.readLine();//
    cw = Integer.parseInt(read); //get next codeword

    idx[endval-1] = idx[endval-1] + (idx[cw]).charAt(0); // new dictionary entry

    System.out.print(idx[cw]); //print current 

    idx[endval] = idx[cw]; //

    endval++; //increment end of index
    }

    catch (NumberFormatException nfe) {} //catch to stop java crying

}

}
}
