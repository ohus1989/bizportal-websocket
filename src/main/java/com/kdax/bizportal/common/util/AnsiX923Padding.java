package com.kdax.bizportal.common.util;


public class AnsiX923Padding implements CryptoPadding {


    private String name = "ANSI-X.923-Padding";

    private static final byte PADDING_VALUE = (byte) 0x80;


    public byte[] addPadding(byte[] source, int blockSize) {
        int paddingCnt = source.length % blockSize;
        byte[] paddingResult = null;

        if (paddingCnt != 0) {
            paddingResult = new byte[source.length + (blockSize - paddingCnt)];

            System.arraycopy(source, 0, paddingResult, 0, source.length);

            int addPaddingCnt = blockSize - paddingCnt;
            for (int i = 0; i < addPaddingCnt; i++) {
                paddingResult[source.length + i] = PADDING_VALUE;
            }


            paddingResult[paddingResult.length - 1] = (byte) addPaddingCnt;
        } else {
            paddingResult = source;
        }

        return paddingResult;
    }

    public byte[] add80Padding(byte[] source, int blockSize) {
        int paddingCnt = source.length % blockSize;
        byte[] paddingResult = null;
        if (paddingCnt != 0) {
            paddingResult = new byte[source.length + (blockSize - paddingCnt)];

            System.arraycopy(source, 0, paddingResult, 0, source.length);


            int addPaddingCnt = blockSize - paddingCnt;
            for (int i = 0; i < addPaddingCnt; i++) {
                if (i == 0) {
                    paddingResult[source.length + i] = (byte) 0x80;
                } else {
                    paddingResult[source.length + i] = (byte) 0x00;
                }
            }
        } else {
            paddingResult = source;
        }

        return paddingResult;
    }

    public byte[] removePadding(byte[] source, int blockSize) {
        byte[] paddingResult = null;
        int paddingIdx = 0;
        if (source[source.length - 1] == (byte) 0x00 || source[source.length - 1] == (byte) 0x80) {
            //padding
            for (int i = source.length - 1; i >= 0; i--) {
                if (source[i] == (byte) 0x80) {
                    paddingIdx = i;
                    break;
                }
            }
            paddingResult = new byte[paddingIdx];
            System.arraycopy(source, 0, paddingResult, 0, paddingIdx);
            return paddingResult;

        } else {
            //not padding
            return source;
        }

    }

    public String getName() {
        return name;
    }

}
