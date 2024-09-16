@file:Suppress("MagicNumber") // a lot of numbers related to the algorithm

package dev.fobo66.crypto

@Suppress("LargeClass", "TooManyFunctions") // algorithm requires to be like this
object DES {
    // initialization vector for CBC, CFB and OFB modes
    private val IV = byteArrayOf(220.toByte(), 190.toByte(), 106, 231.toByte(), 234.toByte(), 93, 92, 97)

    // initial permutation table
    private val IP =
        intArrayOf(
            58,
            50,
            42,
            34,
            26,
            18,
            10,
            2,
            60,
            52,
            44,
            36,
            28,
            20,
            12,
            4,
            62,
            54,
            46,
            38,
            30,
            22,
            14,
            6,
            64,
            56,
            48,
            40,
            32,
            24,
            16,
            8,
            57,
            49,
            41,
            33,
            25,
            17,
            9,
            1,
            59,
            51,
            43,
            35,
            27,
            19,
            11,
            3,
            61,
            53,
            45,
            37,
            29,
            21,
            13,
            5,
            63,
            55,
            47,
            39,
            31,
            23,
            15,
            7,
        )

    // inverse initial permutation
    private val invIP =
        intArrayOf(
            40,
            8,
            48,
            16,
            56,
            24,
            64,
            32,
            39,
            7,
            47,
            15,
            55,
            23,
            63,
            31,
            38,
            6,
            46,
            14,
            54,
            22,
            62,
            30,
            37,
            5,
            45,
            13,
            53,
            21,
            61,
            29,
            36,
            4,
            44,
            12,
            52,
            20,
            60,
            28,
            35,
            3,
            43,
            11,
            51,
            19,
            59,
            27,
            34,
            2,
            42,
            10,
            50,
            18,
            58,
            26,
            33,
            1,
            41,
            9,
            49,
            17,
            57,
            25,
        )

    // Permutation P (in f(Feistel) function)
    private val P =
        intArrayOf(
            16,
            7,
            20,
            21,
            29,
            12,
            28,
            17,
            1,
            15,
            23,
            26,
            5,
            18,
            31,
            10,
            2,
            8,
            24,
            14,
            32,
            27,
            3,
            9,
            19,
            13,
            30,
            6,
            22,
            11,
            4,
            25,
        )

    // initial key permutation 64 => 56 bit
    private val PC1 =
        intArrayOf(
            57,
            49,
            41,
            33,
            25,
            17,
            9,
            1,
            58,
            50,
            42,
            34,
            26,
            18,
            10,
            2,
            59,
            51,
            43,
            35,
            27,
            19,
            11,
            3,
            60,
            52,
            44,
            36,
            63,
            55,
            47,
            39,
            31,
            23,
            15,
            7,
            62,
            54,
            46,
            38,
            30,
            22,
            14,
            6,
            61,
            53,
            45,
            37,
            29,
            21,
            13,
            5,
            28,
            20,
            12,
            4,
        )

    // key permutation at round i 56 => 48
    private val PC2 =
        intArrayOf(
            14,
            17,
            11,
            24,
            1,
            5,
            3,
            28,
            15,
            6,
            21,
            10,
            23,
            19,
            12,
            4,
            26,
            8,
            16,
            7,
            27,
            20,
            13,
            2,
            41,
            52,
            31,
            37,
            47,
            55,
            30,
            40,
            51,
            45,
            33,
            48,
            44,
            49,
            39,
            56,
            34,
            53,
            46,
            42,
            50,
            36,
            29,
            32,
        )

    // key shift for each round
    private val keyShift = intArrayOf(1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1)

    // expansion permutation from function f
    private val expandTbl =
        intArrayOf(
            32,
            1,
            2,
            3,
            4,
            5,
            4,
            5,
            6,
            7,
            8,
            9,
            8,
            9,
            10,
            11,
            12,
            13,
            12,
            13,
            14,
            15,
            16,
            17,
            16,
            17,
            18,
            19,
            20,
            21,
            20,
            21,
            22,
            23,
            24,
            25,
            24,
            25,
            26,
            27,
            28,
            29,
            28,
            29,
            30,
            31,
            32,
            1,
        )

    // substitution boxes
    private val sboxes =
        arrayOf(
            arrayOf(
                intArrayOf(14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7),
                intArrayOf(0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8),
                intArrayOf(4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0),
                intArrayOf(15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13),
            ),
            arrayOf(
                intArrayOf(15, 1, 8, 14, 6, 11, 3, 2, 9, 7, 2, 13, 12, 0, 5, 10),
                intArrayOf(3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5),
                intArrayOf(0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15),
                intArrayOf(13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9),
            ),
            arrayOf(
                intArrayOf(10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8),
                intArrayOf(13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1),
                intArrayOf(13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7),
                intArrayOf(1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12),
            ),
            arrayOf(
                intArrayOf(7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15),
                intArrayOf(13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9),
                intArrayOf(10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4),
                intArrayOf(3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14),
            ),
            arrayOf(
                intArrayOf(2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9),
                intArrayOf(14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6),
                intArrayOf(4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14),
                intArrayOf(11, 8, 12, 7, 1, 14, 2, 12, 6, 15, 0, 9, 10, 4, 5, 3),
            ),
            arrayOf(
                intArrayOf(12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11),
                intArrayOf(10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8),
                intArrayOf(9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6),
                intArrayOf(4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13),
            ),
            arrayOf(
                intArrayOf(4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1),
                intArrayOf(13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6),
                intArrayOf(1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2),
                intArrayOf(6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12),
            ),
            arrayOf(
                intArrayOf(13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7),
                intArrayOf(1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2),
                intArrayOf(7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8),
                intArrayOf(2, 1, 14, 7, 4, 10, 18, 13, 15, 12, 9, 0, 3, 5, 6, 11),
            ),
        )

    private fun setBit(
        data: ByteArray,
        pos: Int,
        value: Int,
    ) {
        val posByte = pos / 8
        val posBit = pos % 8
        var tmpB = data[posByte]
        tmpB = (0xFF7F shr posBit and tmpB.toInt() and 0x00FF).toByte()
        val newByte = (value shl 8 - (posBit + 1) or tmpB.toInt()).toByte()
        data[posByte] = newByte
    }

    private fun extractBit(
        data: ByteArray,
        pos: Int,
    ): Int {
        val posByte = pos / 8
        val posBit = pos % 8
        val tmpB = data[posByte]
        return tmpB.toInt() shr 8 - (posBit + 1) and 0x0001
    }

    private fun rotateLeft(
        input: ByteArray,
        len: Int,
        pas: Int,
    ): ByteArray {
        val nrBytes = (len - 1) / 8 + 1
        val out = ByteArray(nrBytes)
        for (i in 0 until len) {
            val `val` = extractBit(input, (i + pas) % len)
            setBit(out, i, `val`)
        }
        return out
    }

    private fun extractBits(
        input: ByteArray,
        pos: Int,
        n: Int,
    ): ByteArray {
        val numOfBytes = (n - 1) / 8 + 1
        val out = ByteArray(numOfBytes)
        for (i in 0 until n) {
            val `val` = extractBit(input, pos + i)
            setBit(out, i, `val`)
        }
        return out
    }

    private fun permute(
        input: ByteArray,
        table: IntArray,
    ): ByteArray {
        val nrBytes = (table.size - 1) / 8 + 1
        val out = ByteArray(nrBytes)
        for (i in table.indices) {
            val `val` = extractBit(input, table[i] - 1)
            setBit(out, i, `val`)
        }
        return out
    }

    private fun xorBytes(
        a: ByteArray,
        b: ByteArray,
    ): ByteArray {
        val out = ByteArray(a.size)
        for (i in a.indices) {
            out[i] = (a[i].toInt() xor b[i].toInt()).toByte()
        }
        return out
    }

    private fun encrypt64Block(
        block: ByteArray,
        key: ByteArray,
    ): ByteArray {
        var right: ByteArray
        var left: ByteArray
        val subkeys = generateSubKeys(key)
        var result: ByteArray = permute(block, IP)
        left = extractBits(result, 0, IP.size / 2)
        right = extractBits(result, IP.size / 2, IP.size / 2)
        for (i in 0..15) {
            val tmpR = right
            right =
                fFunc(
                    right,
                    subkeys[i]!!,
                )
            right = xorBytes(left, right)
            left = tmpR
        }
        result = concatBits(right, IP.size / 2, left, IP.size / 2)
        result = permute(result, invIP)
        return result
    }

    private fun fFunc(
        r: ByteArray,
        k: ByteArray,
    ): ByteArray {
        var result: ByteArray = permute(r, expandTbl)
        result = xorBytes(result, k)
        result = sFunc(result)
        result = permute(result, P)
        return result
    }

    private fun sFunc(input: ByteArray): ByteArray {
        val processedInput = separateBytes(input, 6)
        val out = ByteArray(processedInput.size / 2)
        var halfByte = 0
        for (b in processedInput.indices) {
            val valByte = processedInput[b]
            val r = 2 * (valByte.toInt() shr 7 and 0x0001) + (valByte.toInt() shr 2 and 0x0001)
            val c = valByte.toInt() shr 3 and 0x000F
            val substitute = sboxes[b][r][c]
            if (b % 2 == 0) halfByte = substitute else out[b / 2] = (16 * halfByte + substitute).toByte()
        }
        return out
    }

    private fun separateBytes(
        input: ByteArray,
        length: Int,
    ): ByteArray {
        val numOfBytes = (8 * input.size - 1) / length + 1
        val out = ByteArray(numOfBytes)
        for (i in 0 until numOfBytes) {
            for (j in 0 until length) {
                val `val` = extractBit(input, length * i + j)
                setBit(out, 8 * i + j, `val`)
            }
        }
        return out
    }

    private fun concatBits(
        a: ByteArray,
        aLen: Int,
        b: ByteArray,
        bLen: Int,
    ): ByteArray {
        val numOfBytes = (aLen + bLen - 1) / 8 + 1
        val out = ByteArray(numOfBytes)
        var j = 0
        for (i in 0 until aLen) {
            val `val` = extractBit(a, i)
            setBit(out, j, `val`)
            j++
        }
        for (i in 0 until bLen) {
            val `val` = extractBit(b, i)
            setBit(out, j, `val`)
            j++
        }
        return out
    }

    private fun generateSubKeys(key: ByteArray): Array<ByteArray?> {
        val result = arrayOfNulls<ByteArray>(16)
        val tmpK = permute(key, PC1)
        var C = extractBits(tmpK, 0, PC1.size / 2)
        var D = extractBits(tmpK, PC1.size / 2, PC1.size / 2)
        for (i in 0..15) {
            C = rotateLeft(C, 28, keyShift[i])
            D = rotateLeft(D, 28, keyShift[i])
            val cd = concatBits(C, 28, D, 28)
            result[i] = permute(cd, PC2)
        }
        return result
    }

    fun encrypt(
        data: ByteArray,
        key: ByteArray,
        mode: DESMode,
    ): ByteArray {
        val length = 8 - data.size % 8
        val padding = ByteArray(length)
        var feedback = ByteArray(8)
        IV.copyInto(feedback, endIndex = feedback.size - 1)
        padding[0] = 0x80.toByte()
        padding.fill(0.toByte(), fromIndex = 1)
        val result = ByteArray(data.size + length)
        var block = ByteArray(8)
        var processedBlock = ByteArray(8)
        var count = 0
        var i = 0
        while (i < data.size + length) {
            if (i > 0 && i % 8 == 0) {
                when (mode) {
                    DESMode.ECB -> processedBlock = encrypt64Block(block, key)
                    DESMode.CBC -> {
                        processedBlock = xorBytes(block, feedback)
                        processedBlock = encrypt64Block(processedBlock, key)
                        feedback = processedBlock.copyOfRange(0, 8)
                    }

                    DESMode.CFB -> {
                        processedBlock = encrypt64Block(key, feedback)
                        feedback = processedBlock.copyOfRange(0, 8)
                        processedBlock = xorBytes(processedBlock, block)
                    }

                    DESMode.OFB -> {
                        processedBlock = encrypt64Block(feedback, key)
                        feedback = processedBlock.copyOfRange(0, 8)
                        processedBlock = xorBytes(processedBlock, block)
                    }

                    DESMode.CTR -> processedBlock = encrypt64Block(block, key)
                }
                System.arraycopy(processedBlock, 0, result, i - 8, block.size)
            }
            if (i < data.size) {
                block[i % 8] = data[i]
            } else {
                block[i % 8] = padding[count % 8]
                count++
            }
            i++
        }
        if (block.size == 8) {
            block = encrypt64Block(block, key)
            block.copyInto(result, destinationOffset = i - 8)
        }
        return result
    }
}
