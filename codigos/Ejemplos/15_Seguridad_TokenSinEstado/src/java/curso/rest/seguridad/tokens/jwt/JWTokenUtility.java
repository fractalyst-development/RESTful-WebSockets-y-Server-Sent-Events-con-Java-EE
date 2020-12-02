package curso.rest.seguridad.tokens.jwt;

import curso.rest.seguridad.tokens.filtros.JWTAuthFilter;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.lang.JoseException;

public class JWTokenUtility {

    public static String buildJWT(String subject) {
        RsaJsonWebKey rsaJsonWebKey = RsaKeyProducer.produce();
        System.out.println("RSA hashcode: " + rsaJsonWebKey.hashCode());
        System.out.println("RSA llave:" + rsaJsonWebKey.getPrivateKey());
        System.out.println("RSA llave.format" + rsaJsonWebKey.getPrivateKey().getFormat());
        System.out.println("RSA llave.json" + rsaJsonWebKey.toJson());
        System.out.println("RSA llave.publica:" + rsaJsonWebKey.getRSAPublicKey().toString());
        System.out.println("RSA llave.publica.typo:" + rsaJsonWebKey.getKeyType());
        System.out.println("RSA llave.publica:" + rsaJsonWebKey.getPublicKey().toString());
        JwtClaims claims = new JwtClaims();
        claims.setSubject(subject); // El token es a partir del Principal

        JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());
        jws.setKey(rsaJsonWebKey.getPrivateKey());
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);

        String jwt = null;
        try {
            jwt = jws.getCompactSerialization();
        } catch (JoseException ex) {
            Logger.getLogger(JWTAuthFilter.class.getName()).log(Level.SEVERE, null, ex);
        }

        System.out.println("Claims:\n" + claims);
        System.out.println("JWS:\n" + jws);
        System.out.println("JWT:\n" + jwt);

        return jwt;
    }
}
