package util;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public final class Claves {
    private static final int ITERACIONES=600000;
    private Claves() { }
    public static String hash(char[] clave) {
        byte[] sal=new byte[16]; new SecureRandom().nextBytes(sal);
        return "pbkdf2$"+ITERACIONES+"$"+Base64.getEncoder().encodeToString(sal)+"$"+
                Base64.getEncoder().encodeToString(derivar(clave,sal,ITERACIONES));
    }
    public static boolean verificar(char[] clave,String almacenada) {
        if(almacenada==null) return false;
        if(!almacenada.startsWith("pbkdf2$"))
            return MessageDigest.isEqual(new String(clave).getBytes(StandardCharsets.UTF_8),almacenada.getBytes(StandardCharsets.UTF_8));
        try {
            String[] partes=almacenada.split("\\$");
            int vueltas=Integer.parseInt(partes[1]);
            if(vueltas<10000 || vueltas>2000000) return false;
            return MessageDigest.isEqual(derivar(clave,Base64.getDecoder().decode(partes[2]),vueltas),Base64.getDecoder().decode(partes[3]));
        } catch(RuntimeException ex) { return false; }
    }
    private static byte[] derivar(char[] clave,byte[] sal,int vueltas) {
        PBEKeySpec spec=new PBEKeySpec(clave,sal,vueltas,256);
        try { return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded(); }
        catch(Exception ex) { throw new IllegalStateException("No se pudo proteger la contraseña.",ex); }
        finally { spec.clearPassword(); }
    }
}
