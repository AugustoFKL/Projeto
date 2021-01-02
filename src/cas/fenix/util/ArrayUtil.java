package cas.fenix.util;

public class ArrayUtil {
    
    public static byte[] getArrayFrom(final byte[] arr1, final int offset1, final int qtd) {
        final byte[] arr2 = new byte[qtd];
        for (int i = 0; i < qtd; i++) {
            arr2[i] = arr1[offset1 + i];
        }
        return arr2;
    }
    
    public static void putArrayTo(final byte[] arr1, final byte[] arr2) {
        putArrayTo(0, arr1, 0, arr2);
    }
    
    public static void putArrayTo(final int offset1, final byte[] arr1, final int offset2, final byte[] arr2) {
        for (int i = offset1; i < arr1.length; i++) {
            arr2[(i + offset2) - offset1] = arr1[i];
        }
    }
    
    public static void
        putArrayTo(final int offset1, final byte[] arr1, final int offset2, final byte[] arr2, final int size) {
        for (int i = offset1; i < size; i++) {
            arr2[(i + offset2) - offset1] = arr1[i];
        }
    }
    
    /*
     * Funcao didática que transforma um array de 4 bytes em um float32 de single precision
     */
    // public static float getFloat(byte[] data, int offset) {
    // long l, l1, l2, l3, l4;
    // float result = 0;
    // l1 = ByteArray.byteToUnsignedInt(data[offset + 3]);
    // l2 = ByteArray.byteToUnsignedInt(data[offset + 2]);
    // l3 = ByteArray.byteToUnsignedInt(data[offset + 1]);
    // l4 = ByteArray.byteToUnsignedInt(data[offset]);
    // l = (l1 << 24) | (l2 << 16) | (l3 << 8) | l4;
    // int s = ((l >> 31) == 0) ? 1 : -1;
    // int e = (int) ((l >> 23) & 0xff) - 127;
    // int m = (int) (l & 0x7fffff);
    // double num = 0;
    // for (int i = 0; i < 24; i++) {
    // if ((m & 0x800000) > 0) {
    // if (i == 0)
    // num = 0.5;
    // num += (CasMath.pow(0.5, i));
    // }
    // m <<= 1;
    // }
    // num += 1;
    // result = (float) (s * CasMath.pow(2, e) * num);
    // return result;
    // }
    
}
