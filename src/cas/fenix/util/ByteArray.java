package cas.fenix.util;

/**
 * A finalidade desta classe é trabalhar com vetor de bytes, unindo, quebrando, formatando e convertendo os valores.
 * 
 * @author ecroys
 */
public class ByteArray {
    
    /**
     * Determina que a forma de trabalho é para ser realizada como caracter, ou seja, na entrada de dados, estes são lidos como caracteres, e na saída, é criada uma string simples.
     */
    public static final int CHAR      = 0;
    
    /**
     * Determina que a forma de trabalho deve ser realizada em decimal, ou seja, na entrada de dados, os números serão convertidos e armazenados, e na saída, será impresso o valor decimal de cada
     * byte.
     */
    public static final int DEC       = 1;
    
    /**
     * Determina que a forma de trabalho deve ser realizada em hexadecimal, ou seja, na entrada de dados, os números serão convertidos de hexadecimal para byte, e na saída, será impreso o valor
     * hexadecimal de cada byte.
     */
    public static final int HEX       = 3;
    
    /**
     * Determina que a forma de trabalho deve ser realizada em decimal, ou seja, na entrada de dados, os números serão convertidos e armazenados, e na saída, será impresso o valor decimal de cada
     * byte. Este forma trabalha posicionalmente, assumindo que cada posição possua 3 bytes.
     */
    public static final int DEC_POS   = 4;
    
    /**
     * Determina que a forma de trabalho deve ser realizada em decimal, ou seja, na entrada de dados, os números serão convertidos e armazenados, e na saída, será impresso o valor decimal de cada
     * byte. Este forma trabalha posicionalmente, assumindo que cada posição possua 2 bytes.
     */
    public static final int DEC_POS_2 = 6;
    
    /**
     * Determina que a forma de trabalho deve ser realizada em hexadecimal, ou seja, na entrada de dados, os números serão convertidos de hexadecimal para byte, e na saída, será impreso o valor
     * hexadecimal de cada byte. Este forma trabalha posicionalmente, assumindo que cada posição possua 2 bytes.
     */
    public static final int HEX_POS   = 5;
    
    /**
     * Vetor de bytes a ser manipulado.
     */
    private byte[]          arr       = new byte[0];
    
    /**
     * Cria o array vazio.
     */
    public ByteArray() {
    }
    
    /**
     * Cria o array com os dados da string informada, convertendo o valor do tipo que foi especificado para byte.
     * 
     * @param str
     *            String com os valores para iniciar o array
     * @param type
     *            Tipo de entrada (vide parâmetros)
     */
    public ByteArray(final String str, final int type) {
        this.append(str, type);
    }
    
    /**
     * Cria o array com os dados da string informada, convertendo o valor do tipo que foi especificado para byte.
     * 
     * @param str
     *            String com os valores para iniciar o array
     * @param type
     *            Tipo de entrada (vide parâmetros)
     * @param offset
     *            Posição inicial a ser considerada para pegar os dados
     * @param tam
     *            Quantidade de dados a serem lidos de dentro da string
     */
    public ByteArray(final String str, final int type, final int offset, final int tam) {
        this.append(str, type, offset, tam);
    }
    
    /**
     * Cria o array com os bytes informados.
     * 
     * @param bts
     *            Bytes para iniciar o array.
     */
    public ByteArray(final byte[] bts) {
        this.append(bts);
    }
    
    /**
     * Cria o array com os bytes informados, iniciando da posição inicial informada, percorrendo o número de bytes do tamanho especificado.
     * 
     * @param bts
     *            Bytes para iniciar o array
     * @param posIni
     *            Posição inicial para começar a utilizar
     * @param tam
     *            Quantidade de bytes para utilizar na inicialização do array.
     */
    public ByteArray(final byte[] bts, final int posIni, final int tam) {
        this.append(bts, posIni, tam);
    }
    
    /**
     * Retorna o tamanho atual do array.
     * 
     * @return Retorna o tamanho atual do array.
     */
    public int size() {
        return arr.length;
    }
    
    /**
     * Retorna o tamanho atual do array.
     * 
     * @return Retorna o tamanho atual do array.
     */
    public int lenght() {
        return arr.length;
    }
    
    /**
     * Altera o array para ser limpo e iniciado novamente com o valor requisitado.
     * 
     * @param bts
     *            Bytes para reiniciar o array.
     */
    public void set(final byte[] bts) {
        arr = bts;
    }
    
    /**
     * Altera o array para ser limpo e iniciado novamente com o valor requisitado, no tipo de formatação informado.
     * 
     * @param str
     *            Dados para iniciar o array.
     * @param mode
     *            Modo de trabalho em que o array deve traduzir o valor solicitado.
     */
    public void set(final String str, final int mode) {
        arr = parseArray(str, mode);
    }
    
    /**
     * Aumenta o array existem com o byte requisitado, colocando-o após os dados que já existem.
     * 
     * @param bts
     *            Byte para aumentar o array.
     */
    public void append(final byte bts) {
        arr = union(arr, new byte[] { bts });
    }
    
    /**
     * Aumenta o array existem com o array requisitado, colocando-o após os dados que já existem.
     * 
     * @param bts
     *            Array para aumentar o array existente.
     */
    public void append(final byte[] bts) {
        arr = union(arr, bts);
    }
    
    /**
     * Aumenta o array existem com o array requisitado, colocando-o após os dados que já existem, iniciando a coleta da informação na posição inicial informada, e utilizando a quantidade de bytes
     * solicitada.
     * 
     * @param bts
     *            Array para aumentar o array existente.
     * @param posIni
     *            Posição inicial do array informado para iniciar a contagem de coleta de informação.
     * @param tam
     *            Quantidade de bytes que deve ser utilizado para aumentar o array existente.
     */
    public void append(final byte[] bts, final int posIni, final int tam) {
        arr = union(arr, 0, arr.length, bts, posIni, tam);
    }
    
    /**
     * Aumenta o array existente com os dados informados, traduzindo para byte segundo o modo informado.
     * 
     * @param str
     *            Dados para aumentar o array
     * @param mode
     *            Modo de tradução para gerar os bytes a serem incrementados
     */
    public void append(final String str, final int mode) {
        arr = union(arr, parseArray(str, mode));
    }
    
    /**
     * Aumenta o array existente com os dados informados, traduzindo para byte segundo o modo informado.
     * 
     * @param str
     *            Dados para aumentar o array
     * @param mode
     *            Modo de tradução para gerar os bytes a serem incrementados
     * @param offset
     *            Início dos dados a serem considerados
     * @param tam
     *            Quantidade de dados para serem considerados
     */
    public void append(final String str, final int mode, final int offset, final int tam) {
        arr = union(arr, parseArray(str, mode, offset, tam));
    }
    
    /**
     * Retorna um sub array, com os dados do array atual iniciando da posição solicitada, indo até o final do array.
     * 
     * @param offset
     *            Posição inicial para construir o sub-array.
     */
    public void subarray(final int offset) {
        arr = subarray(arr, offset);
    }
    
    /**
     * Retorna um sub array, com os dados do array atual iniciando da posição solicitada, com o tamanho informado.
     * 
     * @param offset
     *            Posição inicial do array para iniciar a contagem.
     * @param tam
     *            Tamanho final do array a ser gerado.
     */
    public void subarray(final int offset, final int tam) {
        arr = subarray(arr, offset, tam);
    }
    
    /**
     * Retorna o array utilizado atualmente.
     * 
     * @return Retorna o array utilizado atualmente.
     */
    public byte[] getArray() {
        return arr;
    }
    
    /**
     * Retorna o texto formatado segundo o tipo informado.
     * 
     * @param tipo
     *            Tipo de retorno para o texto.
     * @return Retorna o texto formatado segundo o tipo informado.
     */
    public String getText(final int tipo) {
        return this.getText(tipo, 0);
    }
    
    /**
     * Retorna o texto formatado segundo o tipo informado, a partir da posição inicial do array que foi informada.
     * 
     * @param tipo
     *            Tipo de retorno para o texto.
     * @param offset
     *            Posição inicial a ser considerada.
     * @return Retorna o texto formatado segundo o tipo informado.
     */
    public String getText(final int tipo, final int offset) {
        return this.getText(tipo, offset, lenght() - offset);
    }
    
    /**
     * Retorna o texto formatado segundo o tipo informado, a partir da posição inicial do array que foi informada, considerando a quantidade de bytes requisitada.
     * 
     * @param tipo
     *            Tipo de retorno para o texto.
     * @param offset
     *            Posição inicial a ser considerada.
     * @param tam
     *            Quantidade de bytes a ser utilizada.
     * @return Retorna o texto formatado segundo o tipo informado.
     */
    public String getText(final int tipo, final int offset, final int tam) {
        switch (tipo) {
            case HEX:
                return byteToHex(arr, offset, tam);
            case DEC:
                return byteToDec(arr, offset, tam);
            case HEX_POS:
                return byteToHex(arr, offset, tam, "");
            case DEC_POS:
                return byteToDec(arr, offset, tam, "", 3);
            case DEC_POS_2:
                return byteToDec(arr, offset, tam, "", 2);
            default:
                return new String(arr, offset, tam);
        }
    }
    
    /**
     * Retorna o byte formatado em hexadecimal.
     * 
     * @param b
     *            Byte a ser formatado
     * @return Retorna o byte formatado em hexadecimal.
     */
    public static String byteToHex(final byte b) {
        return byteToHex(new byte[] { b });
    }
    
    /**
     * Retorna o byte formatado em hexadecimal.
     * 
     * @param b
     *            Byte a ser formatado
     * @param separador
     *            Separador para a formatação do texto
     * @return Retorna o byte formatado em hexadecimal.
     */
    public static String byteToHex(final byte b, final String separador) {
        return byteToHex(new byte[] { b }, separador);
    }
    
    /**
     * Retorna o array de bytes formatado em hexadecimal, separado por espaço em branco.
     * 
     * @param b
     *            Array de byte a ser formatado
     * @return Retorna o array de bytes formatado em hexadecimal.
     */
    public static String byteToHex(final byte[] b) {
        return byteToHex(b, 0);
    }
    
    /**
     * Retorna o array de bytes formatado em hexadecimal, separado por espaço em branco, a partir da posição inicial solicitada.
     * 
     * @param b
     *            Array de byte a ser formatado
     * @param offset
     *            Posição inicial para iniciar a contar para o retorno.
     * @return Retorna o array de bytes formatado em hexadecimal.
     */
    public static String byteToHex(final byte[] b, final int offset) {
        return byteToHex(b, offset, b.length - offset);
    }
    
    /**
     * Retorna o array de bytes formatado em hexadecimal, separado por espaço em branco, a partir da posição inicial solicitada.
     * 
     * @param b
     *            Array de byte a ser formatado
     * @param separador
     *            Separador para a formatação do texto
     * @return Retorna o array de bytes formatado em hexadecimal.
     */
    public static String byteToHex(final byte[] b, final String separador) {
        return byteToHex(b, 0, b.length, separador);
    }
    
    /**
     * Retorna o array de bytes formatado em hexadecimal, separado por espaço em branco, a partir da posição inicial solicitada, na quantidade informada.
     * 
     * @param b
     *            Array de byte a ser formatado
     * @param offset
     *            Posição inicial para iniciar a contar para o retorno.
     * @param tam
     *            Número de bytes a ser formatado.
     * @return Retorna o array de bytes formatado em hexadecimal.
     */
    public static String byteToHex(final byte[] b, final int offset, final int tam) {
        return byteToHex(b, offset, tam, " ");
    }
    
    /**
     * Retorna o array de bytes formatado em hexadecimal, separado pelo valor escolhido, a partir da posição inicial solicitada, na quantidade informada.
     * 
     * @param b
     *            Array de byte a ser formatado
     * @param offset
     *            Posição inicial para iniciar a contar para o retorno.
     * @param tam
     *            Número de bytes a ser formatado.
     * @param separador
     *            Separador para a formatação do texto
     * @return Retorna o array de bytes formatado em hexadecimal.
     */
    public static String byteToHex(final byte[] b, final int offset, final int tam, final String separador) {
        final StringBuffer ret = new StringBuffer();
        for (int i = offset; i < (offset + tam); i++) {
            String aux = Integer.toHexString(byteToUnsignedInt(b[i]) & 0x0FF);
            if (aux.length() < 2) {
                aux = "0" + aux;
            }
            if (aux.length() < 2) {
                aux = "0" + aux;
            }
            if (i > offset) {
                ret.append(separador);
            }
            ret.append(aux.toUpperCase());
        }
        return ret.toString();
    }
    
    /**
     * Retorna o array de bytes formatado em decimal, separado por espaço em branco, a partir da posição inicial solicitada, na quantidade informada.
     * 
     * @param b
     *            Array de byte a ser formatado
     * @param offset
     *            Posição inicial para iniciar a contar para o retorno.
     * @param tam
     *            Número de bytes a ser formatado.
     * @return Retorna o array de bytes formatado em decimal.
     */
    public static String byteToDec(final byte[] b, final int offset, final int tam) {
        return byteToDec(b, offset, tam, " ", 3);
    }
    
    /**
     * Retorna o array de bytes formatado em decimal, separado por espaço em branco, a partir da posição inicial solicitada, na quantidade informada. Offset = 0
     * 
     * @param b
     *            Array de byte a ser formatado
     * @param tam
     *            Número de bytes a ser formatado.
     * @return Retorna o array de bytes formatado em decimal.
     */
    public static String byteToDec(final byte[] b, final int tam) {
        return byteToDec(b, 0, tam, " ", 3);
    }
    
    /**
     * Retorna o array de bytes formatado em decimal, separado pelo valor escolhido, a partir da posição inicial solicitada, na quantidade informada.
     * 
     * @param b
     *            Array de byte a ser formatado
     * @param offset
     *            Posição inicial para iniciar a contar para o retorno.
     * @param tam
     *            Número de bytes a ser formatado.
     * @param separador
     *            Separador para a formatação do texto
     * @param printSize
     *            Quantidade de caracteres que um byte deve possuir
     * @return Retorna o array de bytes formatado em decimal.
     */
    public static String byteToDec(final byte[] b, final int offset, final int tam, final String separador,
        final int printSize) {
        boolean first = true;
        final StringBuffer ret = new StringBuffer();
        String aux = "";
        for (int i = offset; i < (offset + tam); i++) {
            aux = "" + (b[i] & 0x0FF);
            if (aux.length() < printSize) {
                aux = "0" + aux;
            }
            if (aux.length() < printSize) {
                aux = "0" + aux;
            }
            if (aux.length() < printSize) {
                aux = "0" + aux;
            }
            
            if (first) {
                first = false;
            } else {
                ret.append(separador);
            }
            
            ret.append(aux);
        }
        return ret.toString();
    }
    
    /**
     * Retorna um sub-array do array informado, iniciando na posição inicial solicitada, indo até o final do mesmo.
     * 
     * @param arr
     *            Array para adquirir o sub-array
     * @param offset
     *            Posição inicial para iniciar o novo array.
     * @return Retorna um sub-array do array informado.
     */
    public static byte[] subarray(final byte[] arr, final int offset) {
        return subarray(arr, offset, arr.length - offset);
    }
    
    /**
     * Retorna um sub-array do array informado, iniciando na posição inicial solicitada, contando a quantidade de bytes até o tamanho especificado.
     * 
     * @param arr
     *            Array para adquirir o sub-array
     * @param offset
     *            Posição inicial para iniciar o novo array.
     * @param tam
     *            Tamanho para o novo array a ser formado.
     * @return Retorna um sub-array do array informado.
     */
    public static byte[] subarray(final byte[] arr, final int offset, final int tam) {
        final byte[] nova = new byte[tam];
        for (int i = offset; i < (tam + offset); i++) {
            nova[i - offset] = arr[i];
        }
        return nova;
    }
    
    /**
     * Uni dois arrays, formando um com os dados dos outros dois.
     * 
     * @param arr1
     *            Primeiro array para ser unido.
     * @param arr2
     *            Segundo array para ser unido
     * @return Um array, com o primeiro seguido do segundo.
     */
    public static byte[] union(final byte[] arr1, final byte[] arr2) {
        final byte[] arr = new byte[arr1.length + arr2.length];
        for (int i = 0; i < arr1.length; i++) {
            arr[i] = arr1[i];
        }
        for (int i = arr1.length; i < (arr1.length + arr2.length); i++) {
            arr[i] = arr2[i - arr1.length];
        }
        return arr;
    }
    
    public static byte[] unionOptmized(final byte[] arr1, final byte[] arr2){
        final byte[] arr = new byte[arr1.length + arr2.length];
        System.arraycopy(arr1, 0, arr, 0, arr1.length);
        System.arraycopy(arr2, 0, arr, arr1.length, arr2.length);
        return arr;
    }
    
    /**
     * Uni dois arrays, formando um com os dados dos outros dois, considerando as posições inicias de ambos, e a quantidade que será utilizada de cada qual.
     * 
     * @param arr1
     *            Primeiro array para ser unido.
     * @param posIni1
     *            Posição incial do primeiro array.
     * @param tam1
     *            Quantidade de bytes do primeiro array que deve ser utilizada.
     * @param arr2
     *            Segundo array para ser unido
     * @return Um array, com os dados do primeiro seguido dos dados do segundo.
     */
    public static byte[] union(final byte[] arr1, final int posIni1, final int tam1, final byte[] arr2, final int posIni2,
        final int tam2) {
        final byte[] arr = new byte[tam1 + tam2];
        int i = 0;
        for (; i < tam1; i++) {
            arr[i] = arr1[posIni1 + i];
        }
        for (; i < (tam1 + tam2); i++) {
            arr[i] = arr2[(posIni2 + i) - tam1];
        }
        return arr;
    }
    
    /**
     * Transforma a string solicidade em um array de bytes, realizando o parse segundo o tipo informado.
     * 
     * @param s
     *            String que irá sofrer o parse.
     * @param tipo
     *            Tipo de formatação.
     * @return Retorna um array de byte com os dados da string.
     */
    public static byte[] parseArray(final String s, final int tipo) {
        return parseArray(s, tipo, 0, s.length());
    }
    
    /**
     * Transforma a string solicidade em um array de bytes, realizando o parse segundo o tipo informado.
     * 
     * @param s
     *            String que irá sofrer o parse.
     * @param tipo
     *            Tipo de formatação.
     * @param offset
     *            Início da string a ser considerada
     * @param tam
     *            Quantidade de dados a serem lidas da string
     * @return Retorna um array de byte com os dados da string.
     */
    public static byte[] parseArray(final String s, final int tipo, final int offset, final int tam) {
        byte[] ret = null;
        if (tipo == HEX) {
            final String pureBytes[] = CasUtil.split(s.substring(offset, offset + tam), " ");
            ret = new byte[pureBytes.length];
            parseNumericArray(16, ret, 0, pureBytes, 0, pureBytes.length);
            
        } else if (tipo == DEC) {
            final String pureBytes[] = CasUtil.split(s.substring(offset, offset + tam), " ");
            ret = new byte[pureBytes.length];
            parseNumericArray(10, ret, 0, pureBytes, 0, pureBytes.length);
            
        } else if (tipo == HEX_POS) {
            ret = new byte[tam / 2];
            parseNumericPosicional(16, ret, 0, s, 2, offset, tam / 2);
            
        } else if (tipo == DEC_POS) {
            ret = new byte[tam / 3];
            parseNumericPosicional(10, ret, 0, s, 3, offset, tam / 3);
            
        } else if (tipo == DEC_POS_2) {
            ret = new byte[tam / 2];
            parseNumericPosicional(10, ret, 0, s, 2, offset, tam / 2);
            
        } else {
            ret = new byte[s.length()];
            parseString(ret, 0, s, offset, tam);
        }
        return ret;
    }
    
    /**
     * Transforma uma string em um vetor de bytes.
     * 
     * @param buffer
     *            Buffer que irá receber os bytes formatados
     * @param posIniBuffer
     *            Posição inicial do buffer a ser preenchido
     * @param s
     *            String a ser transformada
     * @param offset
     *            Posição inicial da string a ser considerada
     * @param tam
     *            Quantidades de bytes a ser transformada
     */
    public static void parseString(final byte[] buffer, final int posIniBuffer, final String s, final int offset,
        final int tam) {
        for (int i = offset; i < (offset + tam); i++) {
            buffer[(posIniBuffer + i) - offset] = (byte) s.charAt(i);
        }
    }
    
    /**
     * Transforma um array de strings com valores hexadecimais em um array de bytes
     * 
     * @param base
     *            Base para transformação do número
     * @param buffer
     *            Buffer que irá receber os bytes formatados
     * @param posIniBuffer
     *            Posição inicial do buffer a ser preenchido
     * @param offset
     *            Primeira posição para iniciar a transformação
     * @param tam
     *            Quantidade de posições para serem transformadas
     */
    public static void parseNumericArray(final int base, final byte[] buffer, final int posIniBuffer,
        final String[] pureBytes, final int offset, final int tam) {
        for (int i = offset; i < (offset + tam); i++) {
            buffer[(posIniBuffer + i) - offset] = (byte) Integer.parseInt((pureBytes[i]), base); // (byte) (
            // Integer.parseInt(
            // (hexBytes[i]), 16 ) &
            // 255 );
        }
    }
    
    /**
     * Transforma uma string com dados numéricos em um array de byte, considerando que cada byte possui um número definido de caracteres.
     * 
     * @param base
     *            Base para transformação do número
     * @param buffer
     *            Buffer que irá receber os bytes formatados
     * @param posIniBuffer
     *            Posição inicial do buffer a ser preenchido
     * @param data
     *            String com os valores a serem transformados
     * @param posSize
     *            Tamanho que cada posição possuir
     * @param offset
     *            Primeira posição para iniciar a transformação
     * @param tam
     *            Quantidade de posições para serem transformadas
     */
    public static void parseNumericPosicional(final int base, final byte[] buffer, final int posIniBuffer,
        final String data, final int posSize, final int offset, final int tam) {
        for (int i = 0; i < tam; i++) {
            buffer[posIniBuffer + i] =
                (byte) Integer.parseInt(data.substring(offset + (i * posSize), offset + ((i + 1) * posSize)).trim(), base);
        }
    }
    
    /**
     * Transforma um byte em um número inteiro positivo.
     * 
     * @param b
     *            Byte a ser transformado.
     * @return Inteiro positivo com o valor do byte.
     */
    public static final int byteToUnsignedInt(final byte b) {
        final int value = b;
        return (value >= 0 ? value : value + 256);
    }
    
    /**
     * Transforma um byte em um número inteiro positivo.
     * 
     * @param b
     *            Byte a ser transformado.
     * @return Inteiro positivo com o valor do byte.
     */
    public static final long byteToUnsignedLong(final byte b) {
        final long value = b;
        return (value >= 0 ? value : value + 256);
    }
    
    /**
     * Transforma um inteiro em um vetor de dois bytes, seguindo da primeira à última posição do array com os bytes menos significativos para o mais significativo.
     * 
     * @param val
     *            Inteiro a ser transformado.
     * @return Array de byte com o valor inteiro.
     */
    public static byte[] int2ToByte(final int val) {
        final byte[] ret = new byte[2];
        ret[0] = (byte) ((val) & 255);
        ret[1] = (byte) ((val >>> 8) & 255);
        return ret;
    }
    
    public static byte[] int2ToByteInv(final int val) {
        final byte ret[] = new byte[2];
        ret[1] = (byte) ((val) & 255);
        ret[0] = (byte) ((val >>> 8) & 255);
        return ret;
    }
    
    public static byte[] longToByte(final long val, final int length) {
        final byte ret[] = new byte[length];
        for(int i = 0, shifter = 0; i < length ; i++, shifter+=8){
        	ret[i] = (byte) ((val >>> shifter) & 255);
        }
        return ret;
    }
    
    public static int byteToInt3(final byte[] bts, final int offset) {
        int ret;
        ret = (byteToUnsignedInt(bts[offset]) << 16);
        ret |= (byteToUnsignedInt(bts[offset + 1]) << 8);
        ret |= byteToUnsignedInt(bts[offset + 2]);
        return ret;
    }
    
    public static int byteToInt3(final byte[] bts, int offset, final boolean hilo) {
        int ret;
        if (hilo) {
            ret = (byteToUnsignedInt(bts[offset++]) << 16);
            ret |= (byteToUnsignedInt(bts[offset++]) << 8);
            ret |= (byteToUnsignedInt(bts[offset++]));
        } else {
            ret = byteToUnsignedInt(bts[offset++]);
            ret |= (byteToUnsignedInt(bts[offset++]) << 8);
            ret |= (byteToUnsignedInt(bts[offset++]) << 16);
        }
        return ret;
    }
    
    /**
     * Cria um inteiro a partir de dois bytes de um vetor de bytes, iniciando a contagem a partir da posição inicial requisitada, tendo o primeiro byte como menos significativo, até o último byte, que
     * deve ser o mais significativo.
     * 
     * @param bts
     *            Vetor de bytes com o número a ser transformado.
     * @param offset
     *            Posição inicial para pegar o número.
     * @return Inteiro de dois bytes.
     */
    public static int byteToInt2(final byte[] bts, final int offset) {
        return byteToInt2(bts, offset, false);
    }
    
    public static int byteToInt2(final byte[] bts, int offset, final boolean hilo) {
        int ret = 0;
        if (hilo) {
            ret = (byteToUnsignedInt(bts[offset++]) << 8);
            ret |= byteToUnsignedInt(bts[offset++]);
        } else {
            ret = byteToUnsignedInt(bts[offset++]);
            ret |= (byteToUnsignedInt(bts[offset++]) << 8);
        }
        return ret;
    }
    
    /**
     * Transforma um inteiro em um vetor de quatro bytes, seguindo da primeira à última posição do array com os bytes menos significativos para o mais significativo.
     * 
     * @param val
     *            Inteiro a ser transformado.
     * @return Array de byte com o valor inteiro.
     */
    public static byte[] int4ToByte(final int val) {
        final byte[] ret = new byte[4];
        ret[0] = (byte) ((val) & 255);
        ret[1] = (byte) ((val >>> 8) & 255);
        ret[2] = (byte) ((val >>> 16) & 255);
        ret[3] = (byte) ((val >>> 24) & 255);
        return ret;
    }
    
    /**
     * Transforma um inteiro em um vetor de quatro bytes, seguindo da primeira à última posição do array com os bytes menos significativos para o mais significativo.
     * 
     * @param val
     *            Inteiro a ser transformado.
     * @return Array de byte com o valor inteiro.
     */
    public static byte[] int4ToByte(final int val, final boolean inv) {
        final byte[] ret = new byte[4];
        
        if (!inv) {
            ret[0] = (byte) ((val) & 255);
            ret[1] = (byte) ((val >>> 8) & 255);
            ret[2] = (byte) ((val >>> 16) & 255);
            ret[3] = (byte) ((val >>> 24) & 255);
        } else {
            ret[3] = (byte) ((val) & 255);
            ret[2] = (byte) ((val >>> 8) & 255);
            ret[1] = (byte) ((val >>> 16) & 255);
            ret[0] = (byte) ((val >>> 24) & 255);
            
        }
        
        return ret;
    }
    
    public static byte[] intToByte(int val) {
        final byte[] bytes = new byte[4];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) (val & 255);
            val >>>= 8;
        }
        return bytes;
    }
    
    /**
     * Cria um inteiro a partir de quatro bytes de um vetor de bytes, iniciando a contagem a partir da posição inicial requisitada, tendo o primeiro byte como menos significativo, até o último byte,
     * que deve ser o mais significativo.
     * 
     * @param bts
     *            Vetor de bytes com o número a ser transformado.
     * @param offset
     *            Posição inicial para pegar o número.
     * @return Inteiro de quatro bytes.
     */
    public static int byteToInt4(final byte[] bts, final int offset) {
        return byteToInt4(bts, offset, false);
    }
    
    public static int byteToInt4(final byte[] bts, int offset, final boolean hilo) {
        int ret = 0;
        if (hilo) {
            ret = byteToUnsignedInt(bts[offset++]) << 24;
            ret |= (byteToUnsignedInt(bts[offset++]) << 16);
            ret |= (byteToUnsignedInt(bts[offset++]) << 8);
            ret |= (byteToUnsignedInt(bts[offset++]));
        } else {
            ret = byteToUnsignedInt(bts[offset++]);
            ret |= (byteToUnsignedInt(bts[offset++]) << 8);
            ret |= (byteToUnsignedInt(bts[offset++]) << 16);
            ret |= (byteToUnsignedInt(bts[offset++]) << 24);
            
        }
        return ret;
    }
    
    /**
     * Transforma um inteiro em um vetor de oito bytes, seguindo da primeira à última posição do array com os bytes menos significativos para o mais significativo..
     * 
     * @param val
     *            Inteiro a ser transformado.
     * @return Array de byte com o valor inteiro.
     */
    public static byte[] longToByte(final long val) {
        final byte[] ret = new byte[8];
        ret[0] = (byte) ((val) & 255);
        ret[1] = (byte) ((val >>> 8) & 255);
        ret[2] = (byte) ((val >>> 16) & 255);
        ret[3] = (byte) ((val >>> 24) & 255);
        ret[4] = (byte) ((val >>> 32) & 255);
        ret[5] = (byte) ((val >>> 40) & 255);
        ret[6] = (byte) ((val >>> 48) & 255);
        ret[7] = (byte) ((val >>> 56) & 255);
        return ret;
    }
    
    public static byte[] longToByteInv(final long val) {
        final byte[] ret = new byte[8];
        ret[7] = (byte) ((val) & 255);
        ret[6] = (byte) ((val >>> 8) & 255);
        ret[5] = (byte) ((val >>> 16) & 255);
        ret[4] = (byte) ((val >>> 24) & 255);
        ret[3] = (byte) ((val >>> 32) & 255);
        ret[2] = (byte) ((val >>> 40) & 255);
        ret[1] = (byte) ((val >>> 48) & 255);
        ret[0] = (byte) ((val >>> 56) & 255);
        return ret;
    }
    
    /**
     * Cria um inteiro a partir de oito bytes de um vetor de bytes, iniciando a contagem a partir da posição inicial requisitada, tendo o primeiro byte como menos significativo, até o último byte, que
     * deve ser o mais significativo.
     * 
     * @param bts
     *            Vetor de bytes com o número a ser transformado.
     * @param offset
     *            Posição inicial para pegar o número.
     * @return Inteiro de oito bytes.
     */
    public static long byteToLong(final byte[] bts, int offset) {
        long ret = 0;
        ret = byteToUnsignedLong(bts[offset++]);
        ret |= (byteToUnsignedLong(bts[offset++]) << 8);
        ret |= (byteToUnsignedLong(bts[offset++]) << 16);
        ret |= (byteToUnsignedLong(bts[offset++]) << 24);
        ret |= (byteToUnsignedLong(bts[offset++]) << 32);
        ret |= (byteToUnsignedLong(bts[offset++]) << 40);
        ret |= (byteToUnsignedLong(bts[offset++]) << 48);
        ret |= (byteToUnsignedLong(bts[offset++]) << 56);
        return ret;
    }
    
    public static long byteToLong4(final byte[] bts, int offset) {
        long ret = 0;
        ret = byteToUnsignedLong(bts[offset++]);
        ret |= (byteToUnsignedLong(bts[offset++]) << 8);
        ret |= (byteToUnsignedLong(bts[offset++]) << 16);
        ret |= (byteToUnsignedLong(bts[offset++]) << 24);
        return ret;
    }
    
    public static long byteToLong4(final byte[] bts, int offset, final boolean hilo) {
        long ret = 0;
        if (hilo) {
            ret = byteToUnsignedLong(bts[offset++]) << 24;
            ret |= (byteToUnsignedLong(bts[offset++]) << 16);
            ret |= (byteToUnsignedLong(bts[offset++]) << 8);
            ret |= (byteToUnsignedLong(bts[offset++]));
        } else {
            ret = byteToUnsignedLong(bts[offset++]);
            ret |= (byteToUnsignedLong(bts[offset++]) << 8);
            ret |= (byteToUnsignedLong(bts[offset++]) << 16);
            ret |= (byteToUnsignedLong(bts[offset++]) << 24);
        }
        return ret;
    }
    
    public static long byteToLongN(final byte[] bts, final int offset, final int n) {
        long ret = 0;
        for (int i = offset; i < (offset + n); i++) {
            ret |= (byteToUnsignedLong(bts[i]) << (i * 8));
        }
        return ret;
    }
    
    public static long byteToLongN(final byte[] bts, final int offset, final int n, final boolean hilo) {
        long ret = 0;
        if (hilo) {
            for (int i = 0; i < n; i++) {
                ret |= byteToUnsignedLong(bts[i + offset]) << ((n - i - 1) * 8);
            }
        } else {
            ret = byteToLongN(bts, offset, n);
        }
        return ret;
    }
    
    public static boolean equals(final byte[] x, final byte[] y) {
        if (x.length == y.length) {
            for (int i = 0; i < y.length; i++) {
                if (x[i] != y[i]) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
    
    /**
     * @author lucas.domingues
     * @param packet
     * 			 Pacote onde se encontram os btyes a serem tratados.
     * @param offset 
     * 			 Inicio do campo ASCIIBCD.
     * @param length 
     * 			 Quantidade de bytes ASCIIBCD.
     * @return
     *			 Exemplo de entrada: 00 00 12 13. 
     *			 Exemplo de Saída: 00001213
     * 
     */
    public static String formatASCIIBCD(final byte[] packet, int offset, int length){
    	//Separo a parte da array que eu preciso
    	final byte[] auxArray = ByteArray.subarray(packet, offset, length);
    	//Transformo essa array em uma HexString. Exemplo: 00 00 12 13 será 00001213
    	return ByteArray.byteToHex(auxArray,"");
    }
}
