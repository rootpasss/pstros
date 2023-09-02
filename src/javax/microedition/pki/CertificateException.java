// 
// Decompiled by Procyon v0.5.36
// 

package javax.microedition.pki;

import java.io.IOException;

public class CertificateException extends IOException
{
    public static final byte BAD_EXTENSIONS = 1;
    public static final byte CERTIFICATE_CHAIN_TOO_LONG = 2;
    public static final byte EXPIRED = 2;
    public static final byte UNAUTHORIZED_INTERMEDIATE_CA = 4;
    public static final byte MISSING_SIGNATURE = 5;
    public static final byte NOT_YET_VALID = 6;
    public static final byte SITENAME_MISMATCH = 7;
    public static final byte UNRECOGNIZED_ISSUER = 8;
    public static final byte UNSUPPORTED_SIGALG = 9;
    public static final byte INAPPROPRIATE_KEY_USAGE = 10;
    public static final byte BROKEN_CHAIN = 11;
    public static final byte ROOT_CA_EXPIRED = 12;
    public static final byte UNSUPPORTED_PUBLIC_KEY_TYPE = 13;
    public static final byte VERIFICATION_FAILED = 14;
    private Certificate cert;
    private byte reason;
    
    public CertificateException(final Certificate certificate, final byte status) {
        this.cert = certificate;
        this.reason = status;
    }
    
    public CertificateException(final String message, final Certificate certificate, final byte status) {
        super(message);
        this.cert = certificate;
        this.reason = status;
    }
    
    public Certificate getCertificate() {
        return this.cert;
    }
    
    public byte getReason() {
        return this.reason;
    }
}
