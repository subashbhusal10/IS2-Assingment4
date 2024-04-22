package com.hidemessage.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SteganographyUtils {

    public static void applyMessageAndSaveFile(MultipartFile file, MultipartFile message, Integer l, int l1, int l2, int indexOfStartingBit) throws IOException {
        // Convert MultipartFile to byte arrays
        byte[] fileBytes = file.getBytes();
        byte[] messageBytes = message.getBytes();

        if (fileBytes.length * 8 < messageBytes.length * 8) {
            throw new IOException("Carrier file is too small to hide the message");
        }

        // If l1 is provided, use it as l
        if (l1 > 0) {
            l = l1;
        }

        applyMessage(fileBytes, messageBytes, l, l2, indexOfStartingBit);

        // Save the modified file
        Path path = Paths.get("data/posts/" + file.getOriginalFilename());
        Files.write(path, fileBytes);
    }

    private static void applyMessage(byte[] fileBytes, byte[] messageBytes, int l, int l2, int indexOfStartingBit) {
        int secretBitIndex = 0;
        int frequencyCounter = 0;

        for (int i = indexOfStartingBit; i <
                // Converting bytes to bits
                fileBytes.length * 8 &&
                // Converting bytes to bits
                secretBitIndex < messageBytes.length * 8;
             i++) {

            // Have we reached the frequency?
            if (frequencyCounter % l == 0) {
                // If yes, replace the bit in the main file
                replaceBitInMainFile(i, fileBytes, messageBytes, secretBitIndex);
                // Increment the secret bit index
                secretBitIndex++;
            }

            // Handle the case where l2 is specified so that we can reset the frequency counter
            if (l2 > 0 &&
                    // We reset only after skipping the starting bit
                    (i - indexOfStartingBit) % l2 == 0
            ) {
                frequencyCounter = 0;
            } else {
                frequencyCounter++;
            }
        }
    }

    private static void replaceBitInMainFile(int i, byte[] fileBytes, byte[] messageBytes, int secretBitIndex) {
        fileBytes[i / 8] = (byte) ((fileBytes[i / 8] & ~(1 << (7 - i % 8))) | (getBitFromMessage(messageBytes, secretBitIndex) << (7 - i % 8)));
    }

    private static int getBitFromMessage(byte[] messageBytes, int bitIndex) {
        return (byte) ((messageBytes[bitIndex / 8] >> (7 - bitIndex % 8)) & 1);
    }
}
