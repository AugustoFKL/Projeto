package ABNTCommands;

import Util.ByteArrayUtils;
import Util.CRC16CAS;

public class CRCCommands {

    public boolean checkCRC(final byte[] reading) {
        String crc = ByteArrayUtils.byteToHex(reading[reading.length - 1]) + ByteArrayUtils.byteToHex(reading[reading.length - 2]);
        int crcInt = Integer.parseInt(crc, 16);
        return CRC16CAS.check(crcInt, reading[257], reading[256]);
    }

    public byte[] generateCRC(byte[] commandArray, int start, int size) {
        int crcInteger = CRC16CAS.calculate(commandArray, start, size);
        byte msb = CRC16CAS.getMSB(crcInteger);
        byte lsb = CRC16CAS.getLSB(crcInteger);

        byte[] crcByte = new byte[] {lsb,msb};


        return crcByte;
    }
}
