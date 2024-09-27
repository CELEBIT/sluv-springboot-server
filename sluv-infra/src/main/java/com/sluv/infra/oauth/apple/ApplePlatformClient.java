package com.sluv.infra.oauth.apple;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sluv.common.jwt.JwtProvider;
import com.sluv.common.jwt.exception.ExpiredTokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class ApplePlatformClient {

    @Value("${apple.clientId}")
    private String CLIENT_ID;

    @Value("${apple.openKey}")
    private String APPLE_OPEN_KEY;

    @Value("${apple.iss}")
    private String ISS_URL;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final JwtProvider jwtProvider;

    /**
     * == identitiyToken이 유효한 토큰인지 확인 ==
     *
     * @param identityToken
     * @return 유효 여부
     * @throws Exception
     */
    public boolean verifyIdToken(String identityToken) throws Exception {
        String[] pieces = identityToken.split("\\.");
        if (pieces.length != 3) {
            return false;
        }
        String header = new String(Base64.getUrlDecoder().decode(pieces[0]));
        String payload = new String(Base64.getUrlDecoder().decode(pieces[1]));

        JsonNode headerNode = objectMapper.readTree(header);
        JsonNode payloadNode = objectMapper.readTree(payload);

        String algorithm = headerNode.get("alg").asText();

        String idKid = headerNode.get("kid").asText();

        if (!algorithm.equals("RS256")) {
            return false;
        }
        // 원래 처리해야하는데 왜 우리 토큰엔 없죠...? - JunKer
//        String nonce = payloadNode.get("nonce").asText();
//        if (!nonce.equals(this.nonce)) {
//            return false;
//        }
        String iss = payloadNode.get("iss").asText();
        if (!iss.equals(ISS_URL)) {
            return false;
        }

        String aud = payloadNode.get("aud").asText();
        if (!aud.equals(this.CLIENT_ID)) {
            return false;
        }

        long exp = payloadNode.get("exp").asLong();
        if (exp < System.currentTimeMillis() / 1000) {
            throw new ExpiredTokenException();
        }

        if (getPublicKeyFromPEM(identityToken, idKid)) {
            return false;
        }

        return true;

    }

    /**
     * == idToken이 검증된 토큰인지 확인 ==
     *
     * @param identityToken
     * @param identityKid
     * @return
     * @throws Exception
     */
    public Boolean getPublicKeyFromPEM(String identityToken, String identityKid) throws Exception {
        JsonNode correctKey = getApplePublicKey(identityKid);
        String tN = correctKey.get("n").asText();
        String tE = correctKey.get("e").asText();
        String kty = correctKey.get("kty").asText();

        byte[] nBytes = Base64.getUrlDecoder().decode(tN);
        byte[] eBytes = Base64.getUrlDecoder().decode(tE);

        BigInteger n = new BigInteger(1, nBytes);
        BigInteger e = new BigInteger(1, eBytes);

        RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(n, e);
        KeyFactory keyFactory = KeyFactory.getInstance(kty);
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

        return jwtProvider.checkClaimsJwsBodyIsNull(publicKey, identityToken);
    }

    /**
     * == Apple에게 공개키를 요청 ==
     *
     * @param identityKid
     * @return 알맞는 공개키의 JsonNode
     * @throws Exception
     */

    private JsonNode getApplePublicKey(String identityKid) throws Exception {
        URL url = new URL(APPLE_OPEN_KEY);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();

        JsonNode jsonNode = objectMapper.readTree(connection.getInputStream());
        JsonNode keysNode = jsonNode.get("keys");

        JsonNode correctKey = null;

        for (JsonNode keyNode : keysNode) {
            String kid = keyNode.get("kid").asText();
            if (kid.equals(identityKid)) {
                correctKey = keyNode;
                break;
            }
        }

        return correctKey;

    }

}
