package com.kdax.bizportal.common.util;


public interface CryptoPadding {
    public byte[] addPadding(byte[] source, int blockSize);

    public byte[] add80Padding(byte[] source, int blockSize);

    public byte[] removePadding(byte[] source, int blockSize);
}
