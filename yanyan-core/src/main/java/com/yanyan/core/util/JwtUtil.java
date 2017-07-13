package com.yanyan.core.util;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.*;
import io.jsonwebtoken.impl.compression.DefaultCompressionCodecResolver;
import io.jsonwebtoken.impl.crypto.DefaultJwtSignatureValidator;
import io.jsonwebtoken.impl.crypto.DefaultJwtSigner;
import io.jsonwebtoken.impl.crypto.JwtSignatureValidator;
import io.jsonwebtoken.impl.crypto.JwtSigner;
import io.jsonwebtoken.lang.Assert;
import io.jsonwebtoken.lang.Collections;
import io.jsonwebtoken.lang.Objects;
import io.jsonwebtoken.lang.Strings;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Jwt工具类
 * User: Saintcy
 * Date: 2017/4/21
 * Time: 12:20
 */
public class JwtUtil {
    /**
     * 由字符串生成加密key
     *
     * @return
     */
    private static SecretKey generalKey(String keyStr) {
        String stringKey = "yanyan" + keyStr;
        byte[] encodedKey = Base64.decodeBase64(stringKey);
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        return key;
    }

    /**
     * 创建jwt
     *
     * @param gson
     * @param keyStr    秘钥
     * @param id
     * @param subject 主体
     * @param ttlMillis 有效期
     * @return
     * @throws Exception
     */
    public static String createJWT(Gson gson, String keyStr, String id, String subject, long ttlMillis) throws Exception {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        SecretKey key = generalKey(keyStr);
        JwtBuilder builder = Jwts.builder(gson)
                .signWith(signatureAlgorithm, key)
                .setId(id)
                .setIssuer("YanYan")
                .setSubject(subject)
                .setIssuedAt(now);
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }
        return builder.compact();
    }

    /**
     * 解密jwt
     *
     * @param gson
     * @param keyStr 秘钥
     * @param jwt
     * @return
     * @throws Exception
     */
    public static Claims parseJWT(Gson gson, String keyStr, String jwt) throws Exception {
        SecretKey key = generalKey(keyStr);
        Claims claims = Jwts.parser(gson)
                .setSigningKey(key)
                .parseClaimsJws(jwt)
                .getBody();
        return claims;
    }

    private static final class Jwts {
        public static Header header() {
            return new DefaultHeader();
        }

        public static Header header(Map<String, Object> header) {
            return new DefaultHeader(header);
        }

        public static JwsHeader jwsHeader() {
            return new DefaultJwsHeader();
        }

        public static JwsHeader jwsHeader(Map<String, Object> header) {
            return new DefaultJwsHeader(header);
        }

        public static Claims claims() {
            return new DefaultClaims();
        }

        public static Claims claims(Map<String, Object> claims) {
            return new DefaultClaims(claims);
        }

        public static JwtParser parser(Gson gson) {
            return new GsonJwtParser(gson);
        }

        public static JwtBuilder builder(Gson gson) {
            return new GsonJwtBuilder(gson);
        }
    }


    private static final class GsonJwtBuilder implements JwtBuilder {
        private Gson gson;
        private Header header;
        private Claims claims;
        private String payload;

        private SignatureAlgorithm algorithm;
        private Key key;
        private byte[] keyBytes;

        private CompressionCodec compressionCodec;

        public GsonJwtBuilder(Gson gson) {
            this.gson = gson;
        }

        public JwtBuilder setHeader(Header header) {
            this.header = header;
            return this;
        }

        public JwtBuilder setHeader(Map<String, Object> header) {
            this.header = new DefaultHeader(header);
            return this;
        }

        public JwtBuilder setHeaderParams(Map<String, Object> params) {
            if (!Collections.isEmpty(params)) {

                Header header = ensureHeader();

                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    header.put(entry.getKey(), entry.getValue());
                }
            }
            return this;
        }

        protected Header ensureHeader() {
            if (this.header == null) {
                this.header = new DefaultHeader();
            }
            return this.header;
        }

        public JwtBuilder setHeaderParam(String name, Object value) {
            ensureHeader().put(name, value);
            return this;
        }

        public JwtBuilder signWith(SignatureAlgorithm alg, byte[] secretKey) {
            Assert.notNull(alg, "SignatureAlgorithm cannot be null.");
            Assert.notEmpty(secretKey, "secret key byte array cannot be null or empty.");
            Assert.isTrue(alg.isHmac(), "Key bytes may only be specified for HMAC signatures.  If using RSA or Elliptic Curve, use the signWith(SignatureAlgorithm, Key) method instead.");
            this.algorithm = alg;
            this.keyBytes = secretKey;
            return this;
        }

        public JwtBuilder signWith(SignatureAlgorithm alg, String base64EncodedSecretKey) {
            Assert.hasText(base64EncodedSecretKey, "base64-encoded secret key cannot be null or empty.");
            Assert.isTrue(alg.isHmac(), "Base64-encoded key bytes may only be specified for HMAC signatures.  If using RSA or Elliptic Curve, use the signWith(SignatureAlgorithm, Key) method instead.");
            byte[] bytes = TextCodec.BASE64.decode(base64EncodedSecretKey);
            return signWith(alg, bytes);
        }

        public JwtBuilder signWith(SignatureAlgorithm alg, Key key) {
            Assert.notNull(alg, "SignatureAlgorithm cannot be null.");
            Assert.notNull(key, "Key argument cannot be null.");
            this.algorithm = alg;
            this.key = key;
            return this;
        }

        public JwtBuilder compressWith(CompressionCodec compressionCodec) {
            Assert.notNull(compressionCodec, "compressionCodec cannot be null");
            this.compressionCodec = compressionCodec;
            return this;
        }

        public JwtBuilder setPayload(String payload) {
            this.payload = payload;
            return this;
        }

        protected Claims ensureClaims() {
            if (this.claims == null) {
                this.claims = new DefaultClaims();
            }
            return this.claims;
        }

        public JwtBuilder setClaims(Claims claims) {
            this.claims = claims;
            return this;
        }

        public JwtBuilder setClaims(Map<String, Object> claims) {
            this.claims = Jwts.claims(claims);
            return this;
        }

        public JwtBuilder setIssuer(String iss) {
            if (Strings.hasText(iss)) {
                ensureClaims().setIssuer(iss);
            } else {
                if (this.claims != null) {
                    claims.setIssuer(iss);
                }
            }
            return this;
        }

        public JwtBuilder setSubject(String sub) {
            if (Strings.hasText(sub)) {
                ensureClaims().setSubject(sub);
            } else {
                if (this.claims != null) {
                    claims.setSubject(sub);
                }
            }
            return this;
        }

        public JwtBuilder setAudience(String aud) {
            if (Strings.hasText(aud)) {
                ensureClaims().setAudience(aud);
            } else {
                if (this.claims != null) {
                    claims.setAudience(aud);
                }
            }
            return this;
        }

        public JwtBuilder setExpiration(Date exp) {
            if (exp != null) {
                ensureClaims().setExpiration(exp);
            } else {
                if (this.claims != null) {
                    //noinspection ConstantConditions
                    this.claims.setExpiration(exp);
                }
            }
            return this;
        }

        public JwtBuilder setNotBefore(Date nbf) {
            if (nbf != null) {
                ensureClaims().setNotBefore(nbf);
            } else {
                if (this.claims != null) {
                    //noinspection ConstantConditions
                    this.claims.setNotBefore(nbf);
                }
            }
            return this;
        }

        public JwtBuilder setIssuedAt(Date iat) {
            if (iat != null) {
                ensureClaims().setIssuedAt(iat);
            } else {
                if (this.claims != null) {
                    //noinspection ConstantConditions
                    this.claims.setIssuedAt(iat);
                }
            }
            return this;
        }

        public JwtBuilder setId(String jti) {
            if (Strings.hasText(jti)) {
                ensureClaims().setId(jti);
            } else {
                if (this.claims != null) {
                    claims.setId(jti);
                }
            }
            return this;
        }

        public JwtBuilder claim(String name, Object value) {
            Assert.hasText(name, "Claim property name cannot be null or empty.");
            if (this.claims == null) {
                if (value != null) {
                    ensureClaims().put(name, value);
                }
            } else {
                if (value == null) {
                    this.claims.remove(name);
                } else {
                    this.claims.put(name, value);
                }
            }

            return this;
        }

        public String compact() {
            if (payload == null && Collections.isEmpty(claims)) {
                throw new IllegalStateException("Either 'payload' or 'claims' must be specified.");
            }

            if (payload != null && !Collections.isEmpty(claims)) {
                throw new IllegalStateException("Both 'payload' and 'claims' cannot both be specified. Choose either one.");
            }

            if (key != null && keyBytes != null) {
                throw new IllegalStateException("A key object and key bytes cannot both be specified. Choose either one.");
            }

            Header header = ensureHeader();

            Key key = this.key;
            if (key == null && !Objects.isEmpty(keyBytes)) {
                key = new SecretKeySpec(keyBytes, algorithm.getJcaName());
            }

            JwsHeader jwsHeader;

            if (header instanceof JwsHeader) {
                jwsHeader = (JwsHeader) header;
            } else {
                jwsHeader = new DefaultJwsHeader(header);
            }

            if (key != null) {
                jwsHeader.setAlgorithm(algorithm.getValue());
            } else {
                //no signature - plaintext JWT:
                jwsHeader.setAlgorithm(SignatureAlgorithm.NONE.getValue());
            }

            if (compressionCodec != null) {
                jwsHeader.setCompressionAlgorithm(compressionCodec.getAlgorithmName());
            }

            String base64UrlEncodedHeader = base64UrlEncode(jwsHeader, "Unable to serialize header to json.");

            String base64UrlEncodedBody;

            if (compressionCodec != null) {

                byte[] bytes;
                try {
                    bytes = this.payload != null ? payload.getBytes(Strings.UTF_8) : toJson(claims);
                } catch (JsonIOException e) {
                    throw new IllegalArgumentException("Unable to serialize claims object to json.");
                }

                base64UrlEncodedBody = TextCodec.BASE64URL.encode(compressionCodec.compress(bytes));

            } else {
                base64UrlEncodedBody = this.payload != null ?
                        TextCodec.BASE64URL.encode(this.payload) :
                        base64UrlEncode(claims, "Unable to serialize claims object to json.");
            }

            String jwt = base64UrlEncodedHeader + JwtParser.SEPARATOR_CHAR + base64UrlEncodedBody;

            if (key != null) { //jwt must be signed:

                JwtSigner signer = createSigner(algorithm, key);

                String base64UrlSignature = signer.sign(jwt);

                jwt += JwtParser.SEPARATOR_CHAR + base64UrlSignature;
            } else {
                // no signature (plaintext), but must terminate w/ a period, see
                // https://tools.ietf.org/html/draft-ietf-oauth-json-web-token-25#section-6.1
                jwt += JwtParser.SEPARATOR_CHAR;
            }

            return jwt;
        }

        /*
         * @since 0.5 mostly to allow testing overrides
         */
        protected JwtSigner createSigner(SignatureAlgorithm alg, Key key) {
            return new DefaultJwtSigner(alg, key);
        }

        protected String base64UrlEncode(Object o, String errMsg) {
            byte[] bytes;
            try {
                bytes = toJson(o);
            } catch (JsonIOException e) {
                throw new IllegalStateException(errMsg, e);
            }

            return TextCodec.BASE64URL.encode(bytes);
        }


        protected byte[] toJson(Object object) {
            return gson.toJson(object).getBytes();
        }
    }

    public static final class GsonJwtParser implements JwtParser {

        //don't need millis since JWT date fields are only second granularity:
        private static final String ISO_8601_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
        private static final int MILLISECONDS_PER_SECOND = 1000;

        private Gson gson;

        private byte[] keyBytes;

        private Key key;

        private SigningKeyResolver signingKeyResolver;

        private CompressionCodecResolver compressionCodecResolver = new DefaultCompressionCodecResolver();

        Claims expectedClaims = new DefaultClaims();

        private Clock clock = DefaultClock.INSTANCE;

        private long allowedClockSkewMillis = 0;

        public GsonJwtParser(Gson gson) {
            this.gson = gson;
        }

        public JwtParser requireIssuedAt(Date issuedAt) {
            expectedClaims.setIssuedAt(issuedAt);
            return this;
        }

        public JwtParser requireIssuer(String issuer) {
            expectedClaims.setIssuer(issuer);
            return this;
        }

        public JwtParser requireAudience(String audience) {
            expectedClaims.setAudience(audience);
            return this;
        }

        public JwtParser requireSubject(String subject) {
            expectedClaims.setSubject(subject);
            return this;
        }

        public JwtParser requireId(String id) {
            expectedClaims.setId(id);
            return this;
        }

        public JwtParser requireExpiration(Date expiration) {
            expectedClaims.setExpiration(expiration);
            return this;
        }

        public JwtParser requireNotBefore(Date notBefore) {
            expectedClaims.setNotBefore(notBefore);
            return this;
        }

        public JwtParser require(String claimName, Object value) {
            Assert.hasText(claimName, "claim name cannot be null or empty.");
            Assert.notNull(value, "The value cannot be null for claim name: " + claimName);
            expectedClaims.put(claimName, value);
            return this;
        }

        public JwtParser setClock(Clock clock) {
            Assert.notNull(clock, "Clock instance cannot be null.");
            this.clock = clock;
            return this;
        }

        public JwtParser setAllowedClockSkewSeconds(long seconds) {
            this.allowedClockSkewMillis = Math.max(0, seconds * MILLISECONDS_PER_SECOND);
            return this;
        }

        public JwtParser setSigningKey(byte[] key) {
            Assert.notEmpty(key, "signing key cannot be null or empty.");
            this.keyBytes = key;
            return this;
        }

        public JwtParser setSigningKey(String base64EncodedKeyBytes) {
            Assert.hasText(base64EncodedKeyBytes, "signing key cannot be null or empty.");
            this.keyBytes = TextCodec.BASE64.decode(base64EncodedKeyBytes);
            return this;
        }

        public JwtParser setSigningKey(Key key) {
            Assert.notNull(key, "signing key cannot be null.");
            this.key = key;
            return this;
        }

        public JwtParser setSigningKeyResolver(SigningKeyResolver signingKeyResolver) {
            Assert.notNull(signingKeyResolver, "SigningKeyResolver cannot be null.");
            this.signingKeyResolver = signingKeyResolver;
            return this;
        }

        public JwtParser setCompressionCodecResolver(CompressionCodecResolver compressionCodecResolver) {
            Assert.notNull(compressionCodecResolver, "compressionCodecResolver cannot be null.");
            this.compressionCodecResolver = compressionCodecResolver;
            return this;
        }

        public boolean isSigned(String jwt) {

            if (jwt == null) {
                return false;
            }

            int delimiterCount = 0;

            for (int i = 0; i < jwt.length(); i++) {
                char c = jwt.charAt(i);

                if (delimiterCount == 2) {
                    return !Character.isWhitespace(c) && c != SEPARATOR_CHAR;
                }

                if (c == SEPARATOR_CHAR) {
                    delimiterCount++;
                }
            }

            return false;
        }

        public Jwt parse(String jwt) throws ExpiredJwtException, MalformedJwtException, SignatureException {

            Assert.hasText(jwt, "JWT String argument cannot be null or empty.");

            String base64UrlEncodedHeader = null;
            String base64UrlEncodedPayload = null;
            String base64UrlEncodedDigest = null;

            int delimiterCount = 0;

            StringBuilder sb = new StringBuilder(128);

            for (char c : jwt.toCharArray()) {

                if (c == SEPARATOR_CHAR) {

                    CharSequence tokenSeq = Strings.clean(sb);
                    String token = tokenSeq != null ? tokenSeq.toString() : null;

                    if (delimiterCount == 0) {
                        base64UrlEncodedHeader = token;
                    } else if (delimiterCount == 1) {
                        base64UrlEncodedPayload = token;
                    }

                    delimiterCount++;
                    sb.setLength(0);
                } else {
                    sb.append(c);
                }
            }

            if (delimiterCount != 2) {
                String msg = "JWT strings must contain exactly 2 period characters. Found: " + delimiterCount;
                throw new MalformedJwtException(msg);
            }
            if (sb.length() > 0) {
                base64UrlEncodedDigest = sb.toString();
            }

            if (base64UrlEncodedPayload == null) {
                throw new MalformedJwtException("JWT string '" + jwt + "' is missing a body/payload.");
            }

            // =============== Header =================
            Header header = null;

            CompressionCodec compressionCodec = null;

            if (base64UrlEncodedHeader != null) {
                String origValue = TextCodec.BASE64URL.decodeToString(base64UrlEncodedHeader);
                Map<String, Object> m = readValue(origValue);

                if (base64UrlEncodedDigest != null) {
                    header = new DefaultJwsHeader(m);
                } else {
                    header = new DefaultHeader(m);
                }

                compressionCodec = compressionCodecResolver.resolveCompressionCodec(header);
            }

            // =============== Body =================
            String payload;
            if (compressionCodec != null) {
                byte[] decompressed = compressionCodec.decompress(TextCodec.BASE64URL.decode(base64UrlEncodedPayload));
                payload = new String(decompressed, Strings.UTF_8);
            } else {
                payload = TextCodec.BASE64URL.decodeToString(base64UrlEncodedPayload);
            }

            Claims claims = null;

            if (payload.charAt(0) == '{' && payload.charAt(payload.length() - 1) == '}') { //likely to be json, parse it:
                Map<String, Object> claimsMap = readValue(payload);
                claims = new DefaultClaims(claimsMap);
            }

            // =============== Signature =================
            if (base64UrlEncodedDigest != null) { //it is signed - validate the signature

                JwsHeader jwsHeader = (JwsHeader) header;

                SignatureAlgorithm algorithm = null;

                if (header != null) {
                    String alg = jwsHeader.getAlgorithm();
                    if (Strings.hasText(alg)) {
                        algorithm = SignatureAlgorithm.forName(alg);
                    }
                }

                if (algorithm == null || algorithm == SignatureAlgorithm.NONE) {
                    //it is plaintext, but it has a signature.  This is invalid:
                    String msg = "JWT string has a digest/signature, but the header does not reference a valid signature " +
                            "algorithm.";
                    throw new MalformedJwtException(msg);
                }

                if (key != null && keyBytes != null) {
                    throw new IllegalStateException("A key object and key bytes cannot both be specified. Choose either.");
                } else if ((key != null || keyBytes != null) && signingKeyResolver != null) {
                    String object = key != null ? "a key object" : "key bytes";
                    throw new IllegalStateException("A signing key resolver and " + object + " cannot both be specified. Choose either.");
                }

                //digitally signed, let's assert the signature:
                Key key = this.key;

                if (key == null) { //fall back to keyBytes

                    byte[] keyBytes = this.keyBytes;

                    if (Objects.isEmpty(keyBytes) && signingKeyResolver != null) { //use the signingKeyResolver
                        if (claims != null) {
                            key = signingKeyResolver.resolveSigningKey(jwsHeader, claims);
                        } else {
                            key = signingKeyResolver.resolveSigningKey(jwsHeader, payload);
                        }
                    }

                    if (!Objects.isEmpty(keyBytes)) {

                        Assert.isTrue(algorithm.isHmac(),
                                "Key bytes can only be specified for HMAC signatures. Please specify a PublicKey or PrivateKey instance.");

                        key = new SecretKeySpec(keyBytes, algorithm.getJcaName());
                    }
                }

                Assert.notNull(key, "A signing key must be specified if the specified JWT is digitally signed.");

                //re-create the jwt part without the signature.  This is what needs to be signed for verification:
                String jwtWithoutSignature = base64UrlEncodedHeader + SEPARATOR_CHAR + base64UrlEncodedPayload;

                JwtSignatureValidator validator;
                try {
                    validator = createSignatureValidator(algorithm, key);
                } catch (IllegalArgumentException e) {
                    String algName = algorithm.getValue();
                    String msg = "The parsed JWT indicates it was signed with the " + algName + " signature " +
                            "algorithm, but the specified signing key of type " + key.getClass().getName() +
                            " may not be used to validate " + algName + " signatures.  Because the specified " +
                            "signing key reflects a specific and expected algorithm, and the JWT does not reflect " +
                            "this algorithm, it is likely that the JWT was not expected and therefore should not be " +
                            "trusted.  Another possibility is that the parser was configured with the incorrect " +
                            "signing key, but this cannot be assumed for security reasons.";
                    throw new UnsupportedJwtException(msg, e);
                }

                if (!validator.isValid(jwtWithoutSignature, base64UrlEncodedDigest)) {
                    String msg = "JWT signature does not match locally computed signature. JWT validity cannot be " +
                            "asserted and should not be trusted.";
                    throw new SignatureException(msg);
                }
            }

            final boolean allowSkew = this.allowedClockSkewMillis > 0;

            //since 0.3:
            if (claims != null) {

                SimpleDateFormat sdf;

                final Date now = this.clock.now();
                long nowTime = now.getTime();

                //https://tools.ietf.org/html/draft-ietf-oauth-json-web-token-30#section-4.1.4
                //token MUST NOT be accepted on or after any specified exp time:
                Date exp = claims.getExpiration();
                if (exp != null) {

                    long maxTime = nowTime - this.allowedClockSkewMillis;
                    Date max = allowSkew ? new Date(maxTime) : now;
                    if (max.after(exp)) {
                        sdf = new SimpleDateFormat(ISO_8601_FORMAT);
                        String expVal = sdf.format(exp);
                        String nowVal = sdf.format(now);

                        long differenceMillis = maxTime - exp.getTime();

                        String msg = "JWT expired at " + expVal + ". Current time: " + nowVal + ", a difference of " +
                                differenceMillis + " milliseconds.  Allowed clock skew: " +
                                this.allowedClockSkewMillis + " milliseconds.";
                        throw new ExpiredJwtException(header, claims, msg);
                    }
                }

                //https://tools.ietf.org/html/draft-ietf-oauth-json-web-token-30#section-4.1.5
                //token MUST NOT be accepted before any specified nbf time:
                Date nbf = claims.getNotBefore();
                if (nbf != null) {

                    long minTime = nowTime + this.allowedClockSkewMillis;
                    Date min = allowSkew ? new Date(minTime) : now;
                    if (min.before(nbf)) {
                        sdf = new SimpleDateFormat(ISO_8601_FORMAT);
                        String nbfVal = sdf.format(nbf);
                        String nowVal = sdf.format(now);

                        long differenceMillis = nbf.getTime() - minTime;

                        String msg = "JWT must not be accepted before " + nbfVal + ". Current time: " + nowVal +
                                ", a difference of " +
                                differenceMillis + " milliseconds.  Allowed clock skew: " +
                                this.allowedClockSkewMillis + " milliseconds.";
                        throw new PrematureJwtException(header, claims, msg);
                    }
                }

                validateExpectedClaims(header, claims);
            }

            Object body = claims != null ? claims : payload;

            if (base64UrlEncodedDigest != null) {
                return new DefaultJws<Object>((JwsHeader) header, body, base64UrlEncodedDigest);
            } else {
                return new DefaultJwt<Object>(header, body);
            }
        }

        private void validateExpectedClaims(Header header, Claims claims) {
            for (String expectedClaimName : expectedClaims.keySet()) {

                Object expectedClaimValue = expectedClaims.get(expectedClaimName);
                Object actualClaimValue = claims.get(expectedClaimName);

                if (
                        Claims.ISSUED_AT.equals(expectedClaimName) ||
                                Claims.EXPIRATION.equals(expectedClaimName) ||
                                Claims.NOT_BEFORE.equals(expectedClaimName)
                        ) {
                    expectedClaimValue = expectedClaims.get(expectedClaimName, Date.class);
                    actualClaimValue = claims.get(expectedClaimName, Date.class);
                } else if (
                        expectedClaimValue instanceof Date &&
                                actualClaimValue != null &&
                                actualClaimValue instanceof Long
                        ) {
                    actualClaimValue = new Date((Long) actualClaimValue);
                }

                InvalidClaimException invalidClaimException = null;

                if (actualClaimValue == null) {
                    String msg = String.format(
                            ClaimJwtException.MISSING_EXPECTED_CLAIM_MESSAGE_TEMPLATE,
                            expectedClaimName, expectedClaimValue
                    );
                    invalidClaimException = new MissingClaimException(header, claims, msg);
                } else if (!expectedClaimValue.equals(actualClaimValue)) {
                    String msg = String.format(
                            ClaimJwtException.INCORRECT_EXPECTED_CLAIM_MESSAGE_TEMPLATE,
                            expectedClaimName, expectedClaimValue, actualClaimValue
                    );
                    invalidClaimException = new IncorrectClaimException(header, claims, msg);
                }

                if (invalidClaimException != null) {
                    invalidClaimException.setClaimName(expectedClaimName);
                    invalidClaimException.setClaimValue(expectedClaimValue);
                    throw invalidClaimException;
                }
            }
        }

        /*
         * @since 0.5 mostly to allow testing overrides
         */
        protected JwtSignatureValidator createSignatureValidator(SignatureAlgorithm alg, Key key) {
            return new DefaultJwtSignatureValidator(alg, key);
        }

        public <T> T parse(String compact, JwtHandler<T> handler)
                throws ExpiredJwtException, MalformedJwtException, SignatureException {
            Assert.notNull(handler, "JwtHandler argument cannot be null.");
            Assert.hasText(compact, "JWT String argument cannot be null or empty.");

            Jwt jwt = parse(compact);

            if (jwt instanceof Jws) {
                Jws jws = (Jws) jwt;
                Object body = jws.getBody();
                if (body instanceof Claims) {
                    return handler.onClaimsJws((Jws<Claims>) jws);
                } else {
                    return handler.onPlaintextJws((Jws<String>) jws);
                }
            } else {
                Object body = jwt.getBody();
                if (body instanceof Claims) {
                    return handler.onClaimsJwt((Jwt<Header, Claims>) jwt);
                } else {
                    return handler.onPlaintextJwt((Jwt<Header, String>) jwt);
                }
            }
        }

        public Jwt<Header, String> parsePlaintextJwt(String plaintextJwt) {
            return parse(plaintextJwt, new JwtHandlerAdapter<Jwt<Header, String>>() {
                @Override
                public Jwt<Header, String> onPlaintextJwt(Jwt<Header, String> jwt) {
                    return jwt;
                }
            });
        }

        public Jwt<Header, Claims> parseClaimsJwt(String claimsJwt) {
            try {
                return parse(claimsJwt, new JwtHandlerAdapter<Jwt<Header, Claims>>() {
                    @Override
                    public Jwt<Header, Claims> onClaimsJwt(Jwt<Header, Claims> jwt) {
                        return jwt;
                    }
                });
            } catch (IllegalArgumentException iae) {
                throw new UnsupportedJwtException("Signed JWSs are not supported.", iae);
            }
        }

        public Jws<String> parsePlaintextJws(String plaintextJws) {
            try {
                return parse(plaintextJws, new JwtHandlerAdapter<Jws<String>>() {
                    @Override
                    public Jws<String> onPlaintextJws(Jws<String> jws) {
                        return jws;
                    }
                });
            } catch (IllegalArgumentException iae) {
                throw new UnsupportedJwtException("Signed JWSs are not supported.", iae);
            }
        }

        public Jws<Claims> parseClaimsJws(String claimsJws) {
            return parse(claimsJws, new JwtHandlerAdapter<Jws<Claims>>() {
                @Override
                public Jws<Claims> onClaimsJws(Jws<Claims> jws) {
                    return jws;
                }
            });
        }

        @SuppressWarnings("unchecked")
        protected Map<String, Object> readValue(String val) {
            try {
                return gson.fromJson(val, Map.class);
            } catch (JsonSyntaxException e) {
                throw new MalformedJwtException("Unable to read JSON value: " + val, e);
            }
        }
    }
}
