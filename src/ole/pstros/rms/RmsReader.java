// 
// Decompiled by Procyon v0.5.36
// 

package ole.pstros.rms;

import java.util.StringTokenizer;
import java.util.Vector;
import org.xml.sax.Attributes;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import ole.pstros.ConfigData;
import org.xml.sax.helpers.DefaultHandler;
import ole.pstros.DataHandler;

import javax.microedition.rms.*;

public class RmsReader extends DefaultHandler
{
    private RmsManager manager;
    
    public void readRms() {
        if (ConfigData.readOnly) {
            return;
        }
        this.manager = RmsManager.getInstance();
        final File file = new File(this.manager.getDataFilename());
        if (!file.exists()) {
            return;
        }
        try {
            final SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
            parser.parse(file,this);

            //System.exit(0);
        }
        catch (Exception e) {
            System.out.println("Error:RmsReader:" + e);
            e.printStackTrace();
        }
    }
    
    public void startDocument() {
    }
    
    public void endDocument() {
    }
    
    public void startElement(final String uri, final String localName, final String qName, final Attributes attributes) {
        final RmsRecord item = null;
        String appName = null;
        String name = null;
        String data = null;
        String auth = null;
        int dataCount = 0;
        Vector v = null;
        if (qName.equals("record")) {
            for (int size = attributes.getLength(), i = 0; i < size; ++i) {
                final String attrName = attributes.getQName(i);
                if (attrName.equals("application")) {
                    appName = attributes.getValue(i);
                    ++dataCount;
                }
                else if (attrName.equals("name")) {
                    name = attributes.getValue(i);
                    ++dataCount;
                }
                else if (attrName.equals("auth")) {
                    auth = attributes.getValue(i);
                }
                else if (attrName.equals("data")) {
                    data = attributes.getValue(i);
                    ++dataCount;
                }
                else if (attrName.startsWith("data")) {
                    if (v == null) {
                        v = new Vector();
                    }
                    v.add(attributes.getValue(i));
                }
            }
            if (dataCount == 3) {
                this.createRmsGroup(appName, name, data, auth);
            }
            else if (dataCount >= 2 && v != null) {
                this.createRmsGroup(appName, name, v, auth);
            }
        }
    }
    
    private void createRmsGroup(final String appName, final String name, final Object data, final String auth) {
        boolean publicAuth = false;
        if (auth != null && (auth.equals("public") || auth.equals("1"))) {
            publicAuth = true;
        }
        final RmsGroup result = this.manager.getRmsGroup(appName, name, publicAuth, true);
        if (result == null) {
            return;
        }
        if (data instanceof String) {
            final byte[] buff = this.getByteArray((String)data);
            result.addRecord(buff);
        }
        else if (data instanceof Vector) {
            final Vector v = (Vector)data;
            System.out.println("data is Vector "+v.size());
            for (int size = v.size(), i = 0; i < size; ++i) {
                final byte[] buff2 = this.getByteArray(v.get(i).toString());
                result.addRecord(buff2);
                System.out.println("vec: "+new String(buff2,0,buff2.length));
            }
          /*try {
            Vector v=(Vector)data;
            RecordStore rs=RecordStore.openRecordStore(name,true);
            for(int i=0;i<v.size();i++) {
              byte[]buff=getByteArray(v.get(i).toString());
              rs.addRecord(buff,0,buff.length);
            }
            rs.closeRecordStore();
          } catch(Exception e) {e.printStackTrace();}*/
        }
    }
    
    private byte[] getByteArray(final String data) {
        final StringTokenizer tokenizer = new StringTokenizer(data, " ");
        final int size = tokenizer.countTokens();
        final byte[] buff = new byte[size];
        //System.out.println("DATA PARSED FROM XML: "+data);
        for (int i = 0; i < size; ++i) {
            buff[i] = (byte)Integer.parseInt(tokenizer.nextToken());
        }
            //System.out.println("CONVERTED BYTE ARRAY TO RMS:"+new String(buff,0,buff.length));
        return buff;
    }
}
