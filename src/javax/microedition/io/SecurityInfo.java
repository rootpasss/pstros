// 
// Decompiled by Procyon v0.5.36
// 

package javax.microedition.io;

import javax.microedition.pki.Certificate;

public interface SecurityInfo
{
    String getCipherSuite();
    
    String getProtocolName();
    
    String getProtocolVersion();
    
    Certificate getServerCertificate();
}
