package dev.fobo66.crypto;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class DES {

    // initialization vector for CBC, CFB and OFB modes
    private static final byte[] IV = {(byte) 220, (byte) 190, 106, (byte) 231, (byte) 234, 93, 92, 97};

    // initial permutation table
    private static final int[] IP = {58, 50, 42, 34, 26, 18, 10, 2, 60, 52, 44, 36, 28, 20, 12, 4, 62, 54, 46, 38, 30, 22,
            14, 6, 64, 56, 48, 40, 32, 24, 16, 8, 57, 49, 41, 33, 25, 17, 9, 1, 59, 51, 43, 35, 27, 19, 11, 3, 61, 53,
            45, 37, 29, 21, 13, 5, 63, 55, 47, 39, 31, 23, 15, 7};
    // inverse initial permutation
    private static final int[] invIP = {40, 8, 48, 16, 56, 24, 64, 32, 39, 7, 47, 15, 55, 23, 63, 31, 38, 6, 46, 14, 54, 22,
            62, 30, 37, 5, 45, 13, 53, 21, 61, 29, 36, 4, 44, 12, 52, 20, 60, 28, 35, 3, 43, 11, 51, 19, 59, 27, 34, 2,
            42, 10, 50, 18, 58, 26, 33, 1, 41, 9, 49, 17, 57, 25};
    // Permutation P (in f(Feistel) function)
    private static final int[] P = {16, 7, 20, 21, 29, 12, 28, 17, 1, 15, 23, 26, 5, 18, 31, 10, 2, 8, 24, 14, 32, 27, 3, 9,
            19, 13, 30, 6, 22, 11, 4, 25

    };
    // initial key permutation 64 => 56 bit
    private static final int[] PC1 = {57, 49, 41, 33, 25, 17, 9, 1, 58, 50, 42, 34, 26, 18, 10, 2, 59, 51, 43, 35, 27, 19,
            11, 3, 60, 52, 44, 36, 63, 55, 47, 39, 31, 23, 15, 7, 62, 54, 46, 38, 30, 22, 14, 6, 61, 53, 45, 37, 29, 21,
            13, 5, 28, 20, 12, 4};
    // key permutation at round i 56 => 48
    private static final int[] PC2 = {14, 17, 11, 24, 1, 5, 3, 28, 15, 6, 21, 10, 23, 19, 12, 4, 26, 8, 16, 7, 27, 20, 13, 2,
            41, 52, 31, 37, 47, 55, 30, 40, 51, 45, 33, 48, 44, 49, 39, 56, 34, 53, 46, 42, 50, 36, 29, 32};
    // key shift for each round
    private static final int[] keyShift = {1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1};
    // expansion permutation from function f
    private static final int[] expandTbl = {32, 1, 2, 3, 4, 5, 4, 5, 6, 7, 8, 9, 8, 9, 10, 11, 12, 13, 12, 13, 14, 15, 16,
            17, 16, 17, 18, 19, 20, 21, 20, 21, 22, 23, 24, 25, 24, 25, 26, 27, 28, 29, 28, 29, 30, 31, 32, 1};
    // substitution boxes
    private static final int[][][] sboxes = {
            {{14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7},
                    {0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8},
                    {4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0},
                    {15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13}},
            {{15, 1, 8, 14, 6, 11, 3, 2, 9, 7, 2, 13, 12, 0, 5, 10},
                    {3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5},
                    {0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15},
                    {13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9}},
            {{10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8},
                    {13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1},
                    {13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7},
                    {1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12}},
            {{7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15},
                    {13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9},
                    {10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4},
                    {3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14}},
            {{2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9},
                    {14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6},
                    {4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14},
                    {11, 8, 12, 7, 1, 14, 2, 12, 6, 15, 0, 9, 10, 4, 5, 3}},
            {{12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11},
                    {10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8},
                    {9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6},
                    {4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13}

            },
            {{4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1},
                    {13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6},
                    {1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2},
                    {6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12}

            },
            {{13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7},
                    {1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2},
                    {7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8},
                    {2, 1, 14, 7, 4, 10, 18, 13, 15, 12, 9, 0, 3, 5, 6, 11}

            }};

    private static void setBit(byte @NotNull [] data, int pos, int val) {
        int posByte = pos / 8;
        int posBit = pos % 8;
        byte tmpB = data[posByte];
        tmpB = (byte) (((0xFF7F >> posBit) & tmpB) & 0x00FF);
        byte newByte = (byte) ((val << (8 - (posBit + 1))) | tmpB);
        data[posByte] = newByte;
    }

    private static int extractBit(byte @NotNull [] data, int pos) {
        int posByte = pos / 8;
        int posBit = pos % 8;
        byte tmpB = data[posByte];
        int bit = tmpB >> (8 - (posBit + 1)) & 0x0001;
        return bit;
    }

    private static byte @NotNull [] rotateLeft(byte[] input, int len, int pas) {
        int nrBytes = (len - 1) / 8 + 1;
        byte[] out = new byte[nrBytes];
        for (int i = 0; i < len; i++) {
            int val = extractBit(input, (i + pas) % len);
            setBit(out, i, val);
        }
        return out;
    }

    private static byte @NotNull [] extractBits(byte[] input, int pos, int n) {
        int numOfBytes = (n - 1) / 8 + 1;
        byte[] out = new byte[numOfBytes];
        for (int i = 0; i < n; i++) {
            int val = extractBit(input, pos + i);
            setBit(out, i, val);
        }
        return out;

    }

    private static byte @NotNull [] permute(byte @NotNull [] input, int @NotNull [] table) {
        int nrBytes = (table.length - 1) / 8 + 1;
        byte[] out = new byte[nrBytes];
        for (int i = 0; i < table.length; i++) {
            int val = extractBit(input, table[i] - 1);
            setBit(out, i, val);
        }
        return out;

    }

    private static byte @NotNull [] xorBytes(byte @NotNull [] a, byte @NotNull [] b) {
        byte[] out = new byte[a.length];
        for (int i = 0; i < a.length; i++) {
            out[i] = (byte) (a[i] ^ b[i]);
        }
        return out;

    }

    private static byte @NotNull [] encrypt64Block(byte @NotNull [] block, byte @NotNull [] key, boolean isDecrypt) {
        byte[] result = new byte[block.length];
        byte[] R = new byte[block.length / 2];
        byte[] L = new byte[block.length / 2];
        byte[][] subkeys = generateSubKeys(key);

        result = permute(block, IP);

        L = extractBits(result, 0, IP.length / 2);
        R = extractBits(result, IP.length / 2, IP.length / 2);

        for (int i = 0; i < 16; i++) {
            byte[] tmpR = R;
            if (isDecrypt)
                R = f_func(R, subkeys[15 - i]);
            else
                R = f_func(R, subkeys[i]);
            R = xorBytes(L, R);
            L = tmpR;
        }

        result = concatBits(R, IP.length / 2, L, IP.length / 2);

        result = permute(result, invIP);
        return result;
    }

    private static byte @NotNull [] f_func(byte @NotNull [] R, byte @NotNull [] K) {
        byte[] result;
        result = permute(R, expandTbl);
        result = xorBytes(result, K);
        result = s_func(result);
        result = permute(result, P);
        return result;
    }

    private static byte @NotNull [] s_func(byte @NotNull [] in) {
        in = separateBytes(in, 6);
        byte[] out = new byte[in.length / 2];
        int halfByte = 0;
        for (int b = 0; b < in.length; b++) {
            byte valByte = in[b];
            int r = 2 * (valByte >> 7 & 0x0001) + (valByte >> 2 & 0x0001);
            int c = valByte >> 3 & 0x000F;
            int val = sboxes[b][r][c];
            if (b % 2 == 0)
                halfByte = val;
            else
                out[b / 2] = (byte) (16 * halfByte + val);
        }
        return out;
    }

    private static byte @NotNull [] separateBytes(byte @NotNull [] in, int length) {
        int numOfBytes = (8 * in.length - 1) / length + 1;
        byte[] out = new byte[numOfBytes];
        for (int i = 0; i < numOfBytes; i++) {
            for (int j = 0; j < length; j++) {
                int val = extractBit(in, length * i + j);
                setBit(out, 8 * i + j, val);
            }
        }
        return out;
    }

    private static byte @NotNull [] concatBits(byte @NotNull [] a, int aLen, byte @NotNull [] b, int bLen) {
        int numOfBytes = (aLen + bLen - 1) / 8 + 1;
        byte[] out = new byte[numOfBytes];
        int j = 0;
        for (int i = 0; i < aLen; i++) {
            int val = extractBit(a, i);
            setBit(out, j, val);
            j++;
        }
        for (int i = 0; i < bLen; i++) {
            int val = extractBit(b, i);
            setBit(out, j, val);
            j++;
        }
        return out;
    }

    private static byte @NotNull [] deletePadding(byte[] input) {
        int count = 0;

        int i = input.length - 1;
        while (input[i] == 0) {
            count++;
            i--;
        }

        byte[] result = new byte[input.length - count - 1];
        System.arraycopy(input, 0, result, 0, result.length);
        return result;
    }

    private static byte[] @NotNull [] generateSubKeys(byte @NotNull [] key) {
        byte[][] result = new byte[16][];
        byte[] tmpK = permute(key, PC1);

        byte[] C = extractBits(tmpK, 0, PC1.length / 2);
        byte[] D = extractBits(tmpK, PC1.length / 2, PC1.length / 2);

        for (int i = 0; i < 16; i++) {

            C = rotateLeft(C, 28, keyShift[i]);
            D = rotateLeft(D, 28, keyShift[i]);

            byte[] cd = concatBits(C, 28, D, 28);

            result[i] = permute(cd, PC2);
        }

        return result;
    }

    public static byte @NotNull [] encrypt(byte @NotNull [] data, byte @NotNull [] key, DESMode mode) {
        int i;
        int length = 8 - data.length % 8;
        byte[] padding = new byte[length];
        byte[] feedback = new byte[8];

        System.arraycopy(IV, 0, feedback, 0, feedback.length);

        padding[0] = (byte) 0x80;
        Arrays.fill(padding, 1, length, (byte) 0);

        byte[] result = new byte[data.length + length];
        byte[] block = new byte[8];
        byte[] processedBlock = new byte[8];

        int count = 0;

        for (i = 0; i < data.length + length; i++) {
            if (i > 0 && i % 8 == 0) {
                switch (mode) {
                    case ECB:
                        processedBlock = encrypt64Block(block, key, false);
                        break;
                    case CBC:
                        processedBlock = xorBytes(block, feedback);
                        processedBlock = encrypt64Block(processedBlock, key, false);
                        feedback = Arrays.copyOfRange(processedBlock, 0, 8);
                        break;
                    case CFB:
                        processedBlock = encrypt64Block(key, feedback, false);
                        feedback = Arrays.copyOfRange(processedBlock, 0, 8);
                        processedBlock = xorBytes(processedBlock, block);
                        break;
                    case OFB:
                        processedBlock = encrypt64Block(feedback, key, false);
                        feedback = Arrays.copyOfRange(processedBlock, 0, 8);
                        processedBlock = xorBytes(processedBlock, block);
                        break;
                    case CTR:
                        processedBlock = encrypt64Block(block, key, false);
                        break;
                }
                System.arraycopy(processedBlock, 0, result, i - 8, block.length);
            }
            if (i < data.length)
                block[i % 8] = data[i];
            else {
                block[i % 8] = padding[count % 8];
                count++;
            }
        }
        if (block.length == 8) {
            block = encrypt64Block(block, key, false);
            System.arraycopy(block, 0, result, i - 8, block.length);
        }
        return result;
    }
}
