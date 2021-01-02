package cas.fenix.util;

import java.io.IOException;

public abstract class CasATCommand {
    protected boolean DEBUG     = false;
    public static int ATCounter = 0;
    
    /**
     * - Cria uma instancia do ATCommand passando o valor do parametro csdSupport.<br>
     * - Envia o comando AT passado no parametro ATCmd<br>
     * - Libera a instancia do ATCommand com o metodo release()
     * 
     * @param ATCmd
     *            Comando AT que será enviado
     * @return Retorna resposta do metodo send() de ATCommand.
     * @throws IllegalStateException
     * @throws ATCommandFailedException
     * @throws IOException
     */
    // * @param csdSupport
    // * Flag que indica se a instancia da classe {@link ATCommand #ATCommand(csdSupport)} deve ter suporte a CSD ou não.<br>
    public abstract String send(final String ATCmd) throws Exception;
    
    /**
     * Libera memoria de objetos utilizados
     * 
     * @throws IOException
     * @throws IllegalStateException
     */
    public abstract void releaseAll() throws Exception;
    
    public abstract void cancelAT() throws Exception;
}
