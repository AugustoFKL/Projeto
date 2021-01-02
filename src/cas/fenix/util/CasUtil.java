package cas.fenix.util;

import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

public class CasUtil {
    /**
     * Faz a divisao de uma string por um delimitador
     * 
     * @param splitStr
     *            String original
     * @param delimiter
     *            Delimitador
     * @return Divisao da string original em um vetor de strings
     */
    public static String[] split(final String splitStr, final String delimiter) {
        final StringBuffer token = new StringBuffer();
        final Vector tokens = new Vector(32);
        // split
        final char[] chars = splitStr.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (delimiter.indexOf(chars[i]) != -1) {
                // we bumbed into a delimiter
                if (token.length() > 0) {
                    tokens.addElement(token.toString());
                    token.setLength(0);
                }
            } else {
                token.append(chars[i]);
            }
        }
        // don't forget the "tail"...
        if (token.length() > 0) {
            tokens.addElement(token.toString());
        }
        // convert the vector into an array
        final String[] splitArray = new String[tokens.size()];
        for (int i = 0; i < splitArray.length; i++) {
            splitArray[i] = (String) tokens.elementAt(i);
        }
        return splitArray;
    }
    
    public static String removeEnterFromTheEnd(String pASC) {
        final String enter = pASC.substring(pASC.length() - 2, pASC.length());
        
        if (CasUtil.contains(enter, "\r") && CasUtil.contains(enter, "\n")) {
            pASC = pASC.substring(0, pASC.length() - 2);
        } else {
            if (CasUtil.contains(enter, "\r")) {
                pASC = pASC.substring(0, pASC.length() - 1);
            }
            if (CasUtil.contains(enter, "\n")) {
                pASC = pASC.substring(0, pASC.length() - 1);
            }
        }
        return pASC;
    }
    
    /**
     * Faz a divisao de uma string por um delimitador
     * 
     * @param splitStr
     *            String original
     * @param delimiter
     *            Delimitador
     * @return Divisao da string original em um vetor de strings
     */
    public static String[] split2(final String splitStr, final String delimiter) {
        final StringBuffer token = new StringBuffer();
        final Vector tokens = new Vector(32);
        // split
        final char[] chars = splitStr.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (delimiter.indexOf(chars[i]) != -1) {
                // we bumbed into a delimiter
                if (token.length() > 0) {
                    tokens.addElement(token.toString());
                    token.setLength(0);
                } else {
                    tokens.addElement("");
                }
            } else {
                token.append(chars[i]);
            }
        }
        // don't forget the "tail"...
        if (token.length() > 0) {
            tokens.addElement(token.toString());
        }
        // convert the vector into an array
        final String[] splitArray = new String[tokens.size()];
        for (int i = 0; i < splitArray.length; i++) {
            splitArray[i] = (String) tokens.elementAt(i);
        }
        return splitArray;
    }
    
    /**
     * Converte um vetor de bytes em uma string com os valores dos bytes em hexadecimal.
     * 
     * Resultado da conversão não possui espaços entre os bytes.
     * 
     * @param b
     *            vetor de bytes
     * @return String em hexadecimal
     */
    public static String createHexString_NoWhiteSpace(final byte[] b) {
        return replaceAll(createHexString(b), " ", "");
    }
    
    /**
     * Converte um vetor de bytes em uma string com os valores dos bytes em hexadecimal
     * 
     * Resultado da conversão não possui espaços entre os bytes.
     * 
     * @param b
     *            vetor de bytes
     * @return String em hexadecimal
     */
    public static String createHexStringVector_NoWhiteSpace(final Vector v) {
        return replaceAll(createHexStringVector(v), " ", "");
    }
    
    /**
     * @param buffer
     * @return
     */
    public static String createHexStringVector(final Vector v) {
        byte[] b;
        String s = "";
        
        try {
            for (int i = 0; i < v.size(); i++) {
                b = (byte[]) v.elementAt(i);
                s += (createHexString(b));
            }
        } catch (final Exception e) {
        }
        
        return s;
    }
    
    /**
     * Converte um vetor de bytes em uma string com os valores dos bytes em hexadecimal
     * 
     * @param b
     *            vetor de bytes
     * @return String em hexadecimal
     */
    public static String createHexString(final byte[] b) {
        String out = "";
        
        for (int i = 0; i < b.length; i++) {
            String aux = Integer.toHexString(b[i]);
            
            if (aux.length() > 2) {
                aux = aux.substring(aux.length() - 2, aux.length());
                
            } else if (aux.length() < 2) {
                aux = "0" + aux;
            }
            
            out += aux + " ";
        }
        
        return out.toUpperCase();
    }
    
    /**
     * Converte um vetor de bytes em uma string com os valores dos bytes em hexadecimal, considerando o tamanho len do vetor
     * 
     * @param Vetor
     *            de bytes
     * @param Numero
     *            de bytes que deve ser considerado
     * @return String em hexadecimal
     */
    public static String createHexString(final byte[] b, final int len) {
        final byte[] aux = new byte[len];
        
        System.arraycopy(b, 0, aux, 0, len);
        
        return (createHexString(aux));
    }
    
    /**
     * Recebe uma String com um delimitador e preenche com "0"s a direita do numero depois do delimitador
     * <p>
     * <b>Exemplo:</b><br>
     * originalString = "0.0"<br>
     * qtdAfterComma = 2<br>
     * delimiter = "."<br>
     * return "0.00"
     * 
     * @param originalString
     *            String com delimiter que recebera os "0"s apos o delimiter
     * @param qtdAfterComma
     *            Quantidade de "0"s apos o delimiter
     * @param delimiter
     *            Delimitador que sera buscado na originalString. Exemplo: "," ou "."
     * @return
     */
    public static String IncludeZeroAfterComma(final String originalString, final int qtdAfterComma, final String delimiter) {
        final String s[] = CasUtil.split(originalString, delimiter);
        while (s[1].length() < qtdAfterComma) {
            s[1] += "0";
        }
        
        return s[0] + delimiter + s[1];
    }
    
    /**
     * Verifica se a String é maior que o size informado e retira os numeros a esquerda se necessario
     * 
     * @param originalString
     *            String analizada
     * @param size
     *            Quantidade de digitos a completar
     * @return String recortada para o size informado se necessario
     */
    public static String StringExactNumberOfDigits_AfterComma(final String originalString, final int size) {
        String stringAux = originalString;
        if (originalString.length() > size) {
            stringAux = originalString.substring((originalString.length() - size));
        }
        return stringAux;
    }
    
    /**
     * Completa a String com "0"s ate o size informado. Ou retorna uma substring com o tamanho de size com os ultimos digitos da String
     * 
     * @param s
     *            String analizada
     * @param size
     *            Quantidade de digitos a completar ou limitar
     * @return String com size informado e "0"s a esquerda se necessario
     */
    public static String StringExactNumberOfDigits(final String s, final int size) {
        String stringAux = "";
        if (s.length() <= size) {
            stringAux = CasUtil.StringLeftPadZeros(s, size);
        } else {
            stringAux = s.substring((s.length() - size));
        }
        return stringAux;
    }
    
    public static String StringLeftPadZeros(final String s, final int size) {
        String stringAux = "";
        final int numZeros = size - s.length();
        
        for (int i = 0; i < numZeros; i++) {
            stringAux += "0";
        }
        
        stringAux += s;
        
        return stringAux;
    }
    
    /**
     * Substitui todas as ocorrências de <b>searchString</b> por <b>replacementString</b> encontradas na <b>text</b> informada
     * 
     * @param text
     *            String original
     * @param searchString
     *            String que será procurada
     * @param replacementString
     *            String que será substituida
     * 
     * @return Retorna a String com os valores encontrados substituidos
     */
    public static String replaceAll(String text, final String searchString, final String replacementString) {
        final StringBuffer sBuffer = new StringBuffer();
        int pos = 0;
        while ((pos = text.indexOf(searchString)) != -1) {
            sBuffer.append(text.substring(0, pos) + replacementString);
            text = text.substring(pos + searchString.length());
        }
        sBuffer.append(text);
        return sBuffer.toString();
    }
    
    /**
     * Verifica se uma string esta contida em outra
     * 
     * @param src
     *            String para verificacao
     * @param lookingForString
     *            String que sera procurada na String src
     * @return true se lookingForString esta contida em src; false se não
     */
    public static boolean contains(final String src, final String lookingForString) {
        int j = 0;
        
        for (int i = 0; i < src.length(); i++) {
            if (src.charAt(i) == lookingForString.charAt(j)) {
                if (++j == lookingForString.length()) {
                    return true;
                }
            } else {
                j = 0;
            }
        }
        return false;
    }
    
    /**
     * Convete a data em segundo desde 01/01/2000 00:00:00 para o formato ddmmyyyyHHmmSS
     * 
     * @param dateInSeconds
     *            Data em segundos
     * @return String no formato ddmmyyyyHHmmSS
     */
    public static String convertDateSecondsSince2000ToString(final long dateInSeconds) {
        final Calendar aux2000 = Calendar.getInstance();
        aux2000.set(Calendar.YEAR, 2000);
        aux2000.set(Calendar.MONTH, Calendar.JANUARY);
        aux2000.set(Calendar.DAY_OF_MONTH, 1);
        aux2000.set(Calendar.HOUR_OF_DAY, 0);
        aux2000.set(Calendar.MINUTE, 0);
        aux2000.set(Calendar.SECOND, 0);
        return convertDateSecondsSinceGenericToString(dateInSeconds, aux2000);
    }
    
    public static String convertDateSecondsSince1970ToString(final long dateInSeconds) {
        final Calendar aux2000 = Calendar.getInstance();
        aux2000.set(Calendar.YEAR, 1970);
        aux2000.set(Calendar.MONTH, Calendar.JANUARY);
        aux2000.set(Calendar.DAY_OF_MONTH, 1);
        aux2000.set(Calendar.HOUR_OF_DAY, 0);
        aux2000.set(Calendar.MINUTE, 0);
        aux2000.set(Calendar.SECOND, 0);
        return convertDateSecondsSinceGenericToString(dateInSeconds, aux2000);
    }
    
    public static String convertDateSecondsSince1900ToString(final long dateInSeconds) {
        final Calendar aux2000 = Calendar.getInstance();
        aux2000.set(Calendar.YEAR, 1900);
        aux2000.set(Calendar.MONTH, Calendar.JANUARY);
        aux2000.set(Calendar.DAY_OF_MONTH,1);
        aux2000.set(Calendar.HOUR_OF_DAY, 0);
        aux2000.set(Calendar.MINUTE, 0);
        aux2000.set(Calendar.SECOND, 0);
        return convertDateSecondsSinceGenericToString(dateInSeconds, aux2000);
    }
    
    public static String convertDateSecondsSinceGenericToString(final long dateInSeconds, final Calendar aux2000) {
        final Calendar date = Calendar.getInstance();
        date.setTime(new Date((dateInSeconds * 1000) + aux2000.getTime().getTime()));
        
        String day = "" + date.get(Calendar.DAY_OF_MONTH);
        day = (day.length() > 1 ? day : "0" + day);
        
        String month = "" + (date.get(Calendar.MONTH) + 1);
        month = (month.length() > 1 ? month : "0" + month);
        
        final String year = "" + date.get(Calendar.YEAR);
        
        String hour = "" + date.get(Calendar.HOUR_OF_DAY);
        hour = (hour.length() > 1 ? hour : "0" + hour);
        
        String minute = "" + date.get(Calendar.MINUTE);
        minute = (minute.length() > 1 ? minute : "0" + minute);
        
        String second = "" + date.get(Calendar.SECOND);
        second = (second.length() > 1 ? second : "0" + second);
        
        return day + month + year + hour + minute + second;
    }
    
    /**
     * Convete a data em segundo desde 01/01/1970 00:00:00 para o formato ddmmyyyyHHmmSS
     * 
     * @param dateInSeconds
     *            Data em segundos
     * @return String no formato ddmmyyyyHHmmSS
     */
    public static String convertDateSecondsToString(final long dateInSeconds) {
        final Calendar date = Calendar.getInstance();
        date.setTime(new Date(dateInSeconds * 1000));
        
        String day = "" + date.get(Calendar.DAY_OF_MONTH);
        day = (day.length() > 1 ? day : "0" + day);
        
        String month = "" + (date.get(Calendar.MONTH) + 1);
        month = (month.length() > 1 ? month : "0" + month);
        
        final String year = "" + date.get(Calendar.YEAR);
        
        String hour = "" + date.get(Calendar.HOUR_OF_DAY);
        hour = (hour.length() > 1 ? hour : "0" + hour);
        
        String minute = "" + date.get(Calendar.MINUTE);
        minute = (minute.length() > 1 ? minute : "0" + minute);
        
        String second = "" + date.get(Calendar.SECOND);
        second = (second.length() > 1 ? second : "0" + second);
        
        return day + month + year + hour + minute + second;
    }
    
    /**
     * Convete a data em segundo desde 01/01/1970 00:00:00 para o formato ddmmyyyyHHmmSS
     * 
     * @param dateInSeconds
     *            Data em segundos
     * @return String no formato yymmddHHmmSS
     */
    public static String convertDateSecondsToString2(final long dateInSeconds) {
        final Calendar date = Calendar.getInstance();
        date.setTime(new Date(dateInSeconds * 1000));
        
        String day = "" + date.get(Calendar.DAY_OF_MONTH);
        day = (day.length() > 1 ? day : "0" + day);
        
        String month = "" + (date.get(Calendar.MONTH) + 1);
        month = (month.length() > 1 ? month : "0" + month);
        
        String year = "" + date.get(Calendar.YEAR);
        year = year.substring(2);
        
        String hour = "" + date.get(Calendar.HOUR_OF_DAY);
        hour = (hour.length() > 1 ? hour : "0" + hour);
        
        String minute = "" + date.get(Calendar.MINUTE);
        minute = (minute.length() > 1 ? minute : "0" + minute);
        
        String second = "" + date.get(Calendar.SECOND);
        second = (second.length() > 1 ? second : "0" + second);
        
        return year + month + day + hour + minute + second;
    }
    
    /**
     * Converte data no formato ddmmyyyyHHmmSS para segundos desde 01/01/1970 00:00:00
     * 
     * @param dateStr
     *            String da data no formato ddmmyyyyHHmmSS
     * @return Retorna a data em segundos
     */
    public static long convertDateStringToSeconds(final String dateStr) {
        final Calendar date = Calendar.getInstance();
        
        final int day = Integer.parseInt(dateStr.substring(0, 2));
        final int month = Integer.parseInt(dateStr.substring(2, 4)) - 1;
        final int year = Integer.parseInt(dateStr.substring(4, 8));
        final int hour = Integer.parseInt(dateStr.substring(8, 10));
        final int minute = Integer.parseInt(dateStr.substring(10, 12));
        final int second = Integer.parseInt(dateStr.substring(12, 14));
        
        date.set(Calendar.DAY_OF_MONTH, day);
        date.set(Calendar.MONTH, month);
        date.set(Calendar.YEAR, year);
        date.set(Calendar.HOUR_OF_DAY, hour);
        date.set(Calendar.MINUTE, minute);
        date.set(Calendar.SECOND, second);
        
        return (date.getTime().getTime() / 1000);
    }
    
    /**
     * Cria formato da data para ser enviado no AT+CCLK do modem EHS6
     * 
     * @param dateSeconds
     *            Data em segundos desde 01/01/1970 00:00:00
     * @return String para parametrizacao
     */
    public static String createEHS6DateFormat(final long dateSeconds) {
        final String dateStr = CasUtil.convertDateSecondsSince2000ToString(dateSeconds);
        // Cria string da data no formato de parametrizacao do modem
        // "yy/mm/dd,hh:mm:ss"
        final String modemDateFormat =
            "\"" + dateStr.substring(6, 8) + "/" + dateStr.substring(2, 4) + "/" + dateStr.substring(0, 2) + ","
                + dateStr.substring(8, 10) + ":" + dateStr.substring(10, 12) + ":" + dateStr.substring(12, 14) + "\"";
        
        return modemDateFormat;
    }
    
    public static byte[] reverseArray(final byte[] array) {
        int i = 0;
        int j = array.length - 1;
        byte tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
        
        return array;
    }
    
    public static byte reverseByte(byte b) {
        b = (byte) (((b & 0xF0) >> 4) | ((b & 0x0F) << 4));
        b = (byte) (((b & 0xCC) >> 2) | ((b & 0x33) << 2));
        b = (byte) (((b & 0xAA) >> 1) | ((b & 0x55) << 1));
        
        return b;
    }
    
    public static String reverseString(final String original) {
        String out = "";
        int i = original.length();
        
        while (--i >= 0) {
            out += original.charAt(i);
        }
        
        return out;
    }
    
    public static String getActualSecondsInHex1980() {
        final long t = (System.currentTimeMillis() / 1000) - 315532800L;
        return ByteArray.byteToHex(ByteArray.longToByteInv(t), "").substring(8, 16);
    }
    
    public static String BCDtoString(final byte[] bcd) {
        final StringBuffer sb = new StringBuffer();
        
        byte high = (byte) (bcd[0] & 0xf0);
        high >>>= (byte) 4;
        high = (byte) (high & 0x0f);
        final byte low = (byte) (bcd[0] & 0x0f);
        
        for (int i = 0; i < bcd.length; i++) {
            sb.append(BCDtoString(bcd[i]));
        }
        
        return sb.toString();
    }
    
    public static String BCDtoString(final byte bcd) {
        final StringBuffer sb = new StringBuffer();
        
        byte high = (byte) (bcd & 0xf0);
        high >>>= (byte) 4;
        high = (byte) (high & 0x0f);
        final byte low = (byte) (bcd & 0x0f);
        
        sb.append(high);
        sb.append(low);
        
        return sb.toString();
    }
    
    public static String asciiToDec(final byte[] n) {
        String result = "";
        
        for (int i = 0; i < n.length; i++) {
            result = result + (char) n[i];
        }
        return result;
    }
    
    public static boolean compareIPs(final String ip1, final String ip2) {
        final String[] ip1Parser = CasUtil.split(ip1, ".");
        final String[] ip2Parser = CasUtil.split(ip2, ".");
        try {
            if ((Integer.parseInt(ip1Parser[0]) == Integer.parseInt(ip2Parser[0]))
                && (Integer.parseInt(ip1Parser[1]) == Integer.parseInt(ip2Parser[1]))
                && (Integer.parseInt(ip1Parser[2]) == Integer.parseInt(ip2Parser[2]))
                && (Integer.parseInt(ip1Parser[3]) == Integer.parseInt(ip2Parser[3]))) {
                return true;
            }
            
        } catch (final Exception e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
}
