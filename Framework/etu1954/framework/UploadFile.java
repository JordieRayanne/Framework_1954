package etu1954.framework;

public class UploadFile {
    String name;
    byte[] bytes;
    
    public UploadFile(String name, byte[] bytes) {
        this.setName(name);
        this.setBytes(bytes);
    }
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public byte[] getBytes() {
        return bytes;
    }
    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }
     
}
