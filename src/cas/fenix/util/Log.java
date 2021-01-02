package cas.fenix.util;

public final class Log {
	private static final String CLASS_SEPARATOR = " - ";
	private static final String METHOD_SEPARATOR = " : ";
	
	public final static void log(final Object ob, final String method, final String message){
		String[] splitedClassName = CasUtil.split(ob.getClass().getName(),".");
		print(splitedClassName[splitedClassName.length-1] + CLASS_SEPARATOR + method + METHOD_SEPARATOR + message);
	}
	
	public final static void log(final Class classe, final String method,final String message){
		String[] splitedClassName = CasUtil.split(classe.getName(),".");
		print(splitedClassName[splitedClassName.length-1] + CLASS_SEPARATOR + method + METHOD_SEPARATOR + message);
	}	
	
	public final static void logSpecific(final String message){
		print(message);
	}
	
	private static final void print(String msg){
		System.out.println(msg);
	}


}
