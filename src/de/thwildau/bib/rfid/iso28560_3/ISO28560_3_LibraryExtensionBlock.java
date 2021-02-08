package de.thwildau.bib.rfid.iso28560_3;

import java.util.Arrays;

/**
 *
 * @author jan
 */
public class ISO28560_3_LibraryExtensionBlock extends ISO28560_3_ExtensionBlock {

  private Integer mediaFormat;
  private String[] mediaFormatValues = {"Undefined", "Book", "CD/DVD", "Magnetic tape", "Other", "Other, careful handling is required", "Very small item, special handling is required"};
  private String itemIdentifier = null; // optional variable followed by 00hex
  private String ownerInstitution = null;

  /**
   *
   * @param data
   */
  public ISO28560_3_LibraryExtensionBlock(byte[] data) {

    super(data);

    if (this.validBlock) {
      parseExtensionBlockPayload(this.payload);
    } else {
      System.err.println("invalid payload");
    }

  }

  /**
   *
   * @param type
   */
  public ISO28560_3_LibraryExtensionBlock() {
    super(1);
  }

  /**
   *
   * @param payload
   */
  @Override
  void parseExtensionBlockPayload(byte[] payload) {
    
    int pos = 1;
    int key = 1;
    String value = null;
   
    // fixed length field
    mediaFormat = payload[0] != 0x00 ? (int) payload[0] : null;
    
    // payload: 01 
    // payload: 01 61626300 44452D313233
    // payload: 01 00       44452D313233
    // payload: 01 61626300 00
    // payload: 01 00 00
    
    while (pos < payload.length) {
      
      // field ends
      if (payload[pos] == 0x00) {

        setKeyValue(key, value);

        // prepare next key and value
        key++;
        value = null;

      } else {

        if (value == null) {
          value = "";
        }

        value += (char) payload[pos];
        
        // last round?
        if (pos == payload.length - 1) {
          setKeyValue(key, value);
        }
      }

      pos++;
    }

  }

  private void setKeyValue(int key, String value) {
    switch (key) {
      case 1:
        itemIdentifier = value;
        break;
      case 2:
        ownerInstitution = value;
        break;
    }
  }

  /**
   *
   * @return
   */
  @Override
  byte[] getExtensionBlock() {

    // fields: length=1, id=2 and checksum=1 = 4
    int length = 4;
    // mediaFormat always 1 as it is a fixed length field 
    length += 1;
    // variable length fields + 1 end block length
    length += itemIdentifier != null ? itemIdentifier.getBytes().length + 1 : 1;
    length += ownerInstitution != null ? ownerInstitution.getBytes().length : 1;

    byte[] block = new byte[length];
    byte[] tmp;

    block[0] = (byte) length;
    // library extension block
    block[1] = 1;
    block[2] = 0;

    int pos = 4;
    int truncatePos = 4;

    if (mediaFormat != null) {
      block[pos++] = mediaFormat.byteValue();
      
      truncatePos = pos;
    } else {
      // empty
      block[pos++] = (byte) 0x00;
    }

    // fixed length value is not followed by 0x00
    if (itemIdentifier != null) {
      tmp = itemIdentifier.getBytes();
      for (int i = 0; i < tmp.length; i++) {
        block[pos++] = tmp[i];
        
      }
      truncatePos = pos;
    }
    block[pos++] = (byte) 0x00;

    if (ownerInstitution != null) {
      tmp = ownerInstitution.getBytes();
      for (int i = 0; i < tmp.length; i++) {
        block[pos++] = ownerInstitution.getBytes()[i];
        
      }
      truncatePos = pos;
    }

    // truncate that extension block as values after truncatePos are null
    if (truncatePos != pos) {
      
      block[0] = (byte) truncatePos;
      block = Arrays.copyOf(block, truncatePos);
    }

    // generate checksum
    byte xor = 0x00;
    for (int i = 0; i < block.length; i++) {
      xor ^= block[i];
    }
    xor ^= 0x00;
    block[3] = xor;

    return block;
  }

  public void setMediaFormat(int mediaFormat) {
    this.mediaFormat = mediaFormat;
  }

  public void setItemIdentifier(String itemIdentifier) {
    this.itemIdentifier = itemIdentifier;
  }

  public void setOwnerInstitution(String ownerInstitution) {
    this.ownerInstitution = ownerInstitution;
  }

  public Integer getMediaFormat() {
    return mediaFormat;
  }

  public String getItemIdentifier() {
    return itemIdentifier;
  }

  public String getOwnerInstitution() {
    return ownerInstitution;
  }

  public String getMediaFormatString(){
    return mediaFormatValues[mediaFormat];
  }
}
