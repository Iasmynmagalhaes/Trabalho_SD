package server;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.google.protobuf.ByteString;
import Message.MessageOuterClass.Message;
//import com.google.protobuf.Message;

public class Despachante {
	public ByteString selecionaEqueleto(Message request) {

		Class<?> objRef = null;
		Method method = null;
		ByteString resposta = null;
		try {
			objRef = Class.forName("server." + request.getObjReference() + "Esqueleto");
			String methodName = request.getMethodId();
			//System.out.println("Executando: " + methodName);
			method = objRef.getMethod(methodName, ByteString.class);
			resposta = (ByteString) (method.invoke(objRef.newInstance(),request.getArguments()));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		return resposta;
	}
}