package curso.rest.seguridad.tokens.jwt;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jwk.RsaJwkGenerator;
import org.jose4j.lang.JoseException;

public class RsaKeyProducer {

    private RsaKeyProducer() {
    }

    private static RsaJsonWebKey theOne;

    /**
     * TO-DO Concurrencia
     */
    public static RsaJsonWebKey produce() {
        if (theOne == null) {
            try {
                theOne = RsaJwkGenerator.generateJwk(2048);
            } catch (JoseException ex) {
                Logger.getLogger(RsaKeyProducer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        System.out.println("Llave RSA:" + theOne.hashCode());
        return theOne;
    }
}
