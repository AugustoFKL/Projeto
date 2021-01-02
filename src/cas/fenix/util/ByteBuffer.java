package cas.fenix.util;

public class ByteBuffer {
    private int    current_position;
    private byte[] byteArray;
    private int    sizeOfBuffer;
    
    public ByteBuffer(final int initialSize) {
        current_position = 0;
        byteArray = new byte[initialSize];
        sizeOfBuffer = initialSize;
    }
    
    public ByteBuffer() {
        this(100);
    }
    
    public void add(final byte[] b) {
        for (int c = 0; c < b.length; c++) {
            add(b[c]);
        }
    }
    
    public void add(final int offset, final int length, final byte[] b) {
        for (int i = offset; (i < length) && (i < b.length); i++) {
            add(b[i]);
        }
    }
    
    public void add(final byte b) {
        verify();
        byteArray[current_position++] = b;
    }
    
    public void replace(final byte b, final int pos) {
        byteArray[pos] = b;
    }
    
    // DESAFIO
    // [0,1,5,2]
    public void insert(final byte b, final int pos) {
        verifySwift();
        for (int i = byteArray.length - 1; i > pos; i--) {
            byteArray[i] = byteArray[i - 1];
        }
        byteArray[pos] = b;
    }
    
    public byte[] getBytes() {
        final byte[] result = new byte[current_position];
        System.arraycopy(byteArray, 0, result, 0, current_position);
        return result;
    }
    
    private void verifySwift() {
        if (current_position >= (byteArray.length - 1)) {
            final byte[] newByteArray = new byte[(byteArray.length + 1) * 2];
            System.arraycopy(byteArray, 0, newByteArray, 0, byteArray.length);
            byteArray = newByteArray;
        }
    }
    
    private void verify() {
        if (current_position >= byteArray.length) {
            final byte[] newByteArray = new byte[(byteArray.length + 1) * 2];
            System.arraycopy(byteArray, 0, newByteArray, 0, byteArray.length);
            byteArray = newByteArray;
        }
    }
    
    public int getCurrentPosition() {
        return current_position;
    }
    
    public void clear() {
        current_position = 0;
        byteArray = new byte[sizeOfBuffer];
        ;
    }
    
    public int remaining() {
        return sizeOfBuffer - current_position;
    }
}
