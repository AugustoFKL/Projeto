package Util;

import cas.fenix.util.ByteArray;

public class CRC16CAS {
    
    /**
     * Classe responsavel por gerar CRC 16, e conferi-lo. Esta forma de checagem foi implementada na linguagem utilizada pela Tigger, pelo PIC e em Java, e sua compatibilidade testada nestes tres
     * ambientes.
     * 
     * @author ecroys, larenzon, bcroys
     * */
    
    /**
     * Verifica se o CRC eh igual aos bytes informados
     * 
     * @param crc
     *            Numero CRC a ser checado
     * @param msbRec
     *            Byte mais significativo a ser comparado
     * @param lsbRec
     *            Byte menos significativo a ser comparado
     * @return Verdadeiro se o CRC for igual aos bytes informados, falso caso contrario.
     * */
    public static boolean check(final int crc, final byte msbRec, final byte lsbRec) {
        if ((getMSB(crc) == msbRec) && (getLSB(crc) == lsbRec)) {
            return true;
        }
        return false;
    }
    
    /**
     * Obter o byte mais significativo do CRC
     * 
     * @param crc
     *            CRC do qual se deseja obter o byte mais significativo
     * @return O byte mais significativo do CRC informado
     * */
    public static byte getMSB(final int crc) {
        // return (byte)((int)(crc >> 8) & 0xFF);
        return ByteArray.int2ToByte(crc)[1];
    }
    
    /**
     * Obter o byte menos significativo do CRC
     * 
     * @param crc
     *            CRC do qual se deseja obter o byte menos significativo
     * @return O byte menos significativo do CRC informado
     * */
    public static byte getLSB(final int crc) {
        // return (byte)((int)(crc) & 0xFF);
        return ByteArray.int2ToByte(crc)[0];
    }
    
    /**
     * Calcula o CRC 16, utilizando o array informado, considerando a posicao inicial e o tamanho solicitados. !!! UTILIZADO NO CTBRIDGE !!!
     * 
     * @param arr
     *            Array para gerar o CRC
     * @param ini
     *            Posicao inicial para comecar a gerar o CRC
     * @param tam
     *            Tamanho da mensagem a ser considerada na geracao do CRC
     * @return Retorna um numero inteiro com o CRC 16, com o qual pode-se usar as funcoes getMSB e getLSB para pegar os bytes relativos a cada octeto do CRC.
     * */
    public static int calculate(final byte[] arr, final int ini, final int tam) {
        int mascPar, paridade;
        int oldCrc, tempCrc, crc;
        crc = 0;
        for (int i = ini; i < (ini + tam); i++) {
            oldCrc = crc;
            tempCrc = 0xFF & (oldCrc ^ (ByteArray.byteToUnsignedInt(arr[i])));
            for (paridade = 0, mascPar = 1; mascPar <= 0x80; mascPar <<= 1) {
                if ((mascPar & tempCrc) != 0) {
                    paridade++;
                }
            }
            oldCrc = oldCrc >> 8;
            if ((paridade % 2) == 0) {
                oldCrc = oldCrc ^ 0x0C001;
            }
            tempCrc <<= 7;
            oldCrc = oldCrc ^ tempCrc;
            tempCrc >>= 1;
            oldCrc = oldCrc ^ tempCrc;
            crc = oldCrc;
        }
        return crc;
    }
    
    // ////////////////////
    /**
     * Calcula o CRC 16, utilizando o array informado, considerando a posicao inicial e o tamanho solicitados. !!! UTILIZADO NO PROTOCOLO ABNT COM ENDERECO - POTHOS !!!
     * 
     * @param buffer
     *            buffer com a mensagem a ser calculada
     * @param offset
     *            Posicao inicial para comecar a gerar o CRC
     * @param size
     *            Tamanho da mensagem a ser considerada na geracao do CRC
     * @return Retorna um numero inteiro com o CRC 16, com o qual pode-se usar as funcoes getMSB e getLSB para pegar os bytes relativos a cada octeto do CRC.
     * */
    public static int generateMultPointCrc(final byte[] buffer, final int offset, final int size) {
        int mCrc = 0xFFFF;
        int chr;
        for (int i = offset; i < size; i++) {
            chr = ByteArray.byteToUnsignedInt(buffer[i]);
            mCrc = mCrc ^ chr;
            for (int j = 0; j < 8; j++) {
                if ((mCrc & 0x01) == 0) {
                    mCrc = mCrc >> 1;
                } else {
                    mCrc = ((mCrc >> 1) ^ 0xA001);
                }
            }
        }
        return mCrc;
    }
    
    // ///////////////////
    /**
     * Calcula o CRC 16, utilizando o array informado, considerando a posicao inicial e o tamanho solicitados. !!! UTILIZADO NA SUM !!!
     * 
     * @param arr
     *            Array para gerar o CRC
     * @param ini
     *            Posicao inicial para comecar a gerar o CRC
     * @param tam
     *            Tamanho da mensagem a ser considerada na geracao do CRC
     * @return Retorna um numero inteiro com o CRC 16, com o qual pode-se usar as funcoes getMSB e getLSB para pegar os bytes relativos a cada octeto do CRC.
     * */
    public static int SUMCalc(final byte[] arr, final int ini, final int tam) {
        // byte x, vaux, cont, i, msb, lsb
        // long oldcrc, tempcrc, lcrc
        int x, vaux, cont, i, oldcrc, tempcrc, lcrc;
        // lcrc=0
        lcrc = 0;
        // for x=0 to len(aux$)-1
        for (x = ini; x < tam; x++) {
            // oldcrc = lcrc
            oldcrc = lcrc;
            // tempcrc = 0FFh bitand (oldcrc bitxor nfroms(aux$,x,1))
            tempcrc = 0x0FF & (oldcrc ^ ByteArray.byteToUnsignedInt(arr[x]));
            // vaux = 1
            vaux = 1;
            // cont = 0
            cont = 0;
            // for i=0 to 7
            for (i = 0; i <= 7; i++) {
                // if(vaux bitand tempcrc) = 1 then
                if ((vaux & tempcrc) == 1) {
                    // cont = cont+1
                    cont++;
                    // endif
                }
                // vaux = vaux SHL 1
                vaux <<= 1;
                // next
            }
            // oldcrc = oldcrc SHR 8
            oldcrc >>>= 8;
            // if mod(cont,2) = 0 then
            if ((cont % 2) == 0) {
                // oldcrc = oldcrc bitxor 0C001h
                oldcrc = oldcrc ^ 0x0C001;
                // endif
            }
            // tempcrc = tempcrc SHL 7
            tempcrc <<= 7;
            // oldcrc = oldcrc bitxor tempcrc
            oldcrc ^= tempcrc;
            // tempcrc = tempcrc SHR 1
            tempcrc >>= 1;
            // oldcrc = oldcrc bitxor tempcrc
            oldcrc ^= tempcrc;
            // lcrc = oldcrc
            lcrc = oldcrc;
            // next
        }
        // crclow = lcrc bitand 0FFh
        // int crclow = lcrc & 0x0FF;
        // crchigh = (lcrc SHR 8) bitand 0FFh
        // int crchigh = (lcrc >>> 8) & 0x0FF;
        return lcrc;
    }
    
    /*
     * public static int calculate(byte[] arr, int ini, int tam) { int mascPar, paridade; int oldCrc, tempCrc, crc; crc=0; for(int i=ini; i<(ini+tam); i++){ oldCrc = crc; tempCrc = 0xFF & (oldCrc ^
     * arr[i]); for( paridade=0, mascPar=1; mascPar<=0x80; mascPar<<=1 ) { if( (mascPar & tempCrc) != 0){ paridade++; } } oldCrc = oldCrc >> 8; if(paridade % 2 == 0) { oldCrc = oldCrc ^ 0x0C001; }
     * tempCrc <<= 7; oldCrc = oldCrc ^ tempCrc; tempCrc >>= 1; oldCrc = oldCrc ^ tempCrc; crc = oldCrc; } return crc; }
     */
    // ******************CRC DO GARNET
    public static final int byteToUnsignedInt(final byte b) {
        final int value = b;
        return (value >= 0 ? value : value + 256);
    }
    
    public static int generate(final byte[] buffer, final int offSet, final int intSize) {
        int lcrc = 0x00;
        for (int i = offSet; i < intSize; i++) {
            lcrc = crc16(byteToUnsignedInt(buffer[i]), lcrc);
        }
        return lcrc;
    }
    
    public static int parity(int data, final int size) {
        int bit = 0;
        for (int i = 0; i < size; i++) {
            if ((data & 1) == 1) {
                bit++;
            }
            data = data >> 1;
        }
        return bit % 2;
    }
    
    public static int crc16(final int newData, int lcrc) {
        int oldcrc, tempcrc;
        
        oldcrc = lcrc;
        tempcrc = 0xFF & (oldcrc ^ newData);
        oldcrc >>= 8;
        if (parity(tempcrc, 16) == 1) {
            oldcrc ^= 0xC001;
        }
        tempcrc <<= 7;
        oldcrc ^= tempcrc;
        tempcrc >>= 1;
        oldcrc ^= tempcrc;
        lcrc = oldcrc;
        return lcrc;
    }
    
    public static int garnetCalc(final byte[] msg) {
        final byte[] vet = new byte[msg.length + 1];
        System.arraycopy(msg, 0, vet, 0, msg.length);
        
        return generate(vet, 0, vet.length);
    }
    
    
    
    public static int calculaCRC16Eletra(byte[] inbuf, int tam) {
		int i, j;
		int CRC_calc,bitbang;
		// ---------------------------------------Inicializa com o valor para
		// 0x02 que � o SOT.
		CRC_calc = 0xC181;
		for (i = 0; i < tam; i++) {
			CRC_calc ^= inbuf[i] & 0x00FF;
			for (j = 0; j < 8; j++) {
				bitbang = CRC_calc ;
				CRC_calc >>= 1;
				if ((bitbang & 0x01) == 1) {
					CRC_calc ^= 0xA001;
				}
			}
		}
		// ---------------------------------------retorna o CRC calculado.
		return CRC_calc;	
	}
}