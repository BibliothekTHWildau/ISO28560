/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.thwildau.bib.rfid.iso28560_3;

import java.util.Arrays;

/**
 *
 * @author jan
 */
public class ISO28560_3_LibrarySupplementExtensionBlock extends ISO28560_3_ExtensionBlock {

  private String shelfLocation = null;
  private String marcMediaFormat = null;
  private String onixMediaFormat = null;
  private String subsidiaryOfAnOwnerInstitution = null;

  public ISO28560_3_LibrarySupplementExtensionBlock(byte[] data) {

    super(data);

    if (this.validBlock) {
      parseExtensionBlockPayload(this.payload);
    }

  }

  public ISO28560_3_LibrarySupplementExtensionBlock() {
   
    super(3);
    
  }

  @Override
  void parseExtensionBlockPayload(byte[] payload) {

    int pos = 0;
    int key = 0;
    String value = null;

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
      case 0:
        shelfLocation = value;
        break;
      case 1:
        marcMediaFormat = value;
        break;
      case 2:
        onixMediaFormat = value;
        break;
      case 3:
        subsidiaryOfAnOwnerInstitution = value;
        break;

    }
  }

  public String getShelfLocation() {
    return shelfLocation;
  }

  public String getMarcMediaFormat() {
    return marcMediaFormat;
  }

  public String getOnixMediaFormat() {
    return onixMediaFormat;
  }

  public String getSubsidiaryOfAnOwnerInstitution() {
    return subsidiaryOfAnOwnerInstitution;
  }

  @Override
  byte[] getExtensionBlock() {
     int length = 1 + 2 + 1;
    length += shelfLocation != null ? shelfLocation.getBytes().length + 1 : 1;
    length += marcMediaFormat != null ? marcMediaFormat.getBytes().length + 1 : 1;
    length += onixMediaFormat != null ? onixMediaFormat.getBytes().length +1 : 1;
    length += subsidiaryOfAnOwnerInstitution != null ? subsidiaryOfAnOwnerInstitution.getBytes().length : 1;
    
    byte[] block = new byte[length];
    byte[] tmp;

    block[0] = (byte) length;
    block[1] = 3;
    block[2] = 0;

    int pos = 4;
    int truncatePos = 4;

    if (shelfLocation != null) {
      tmp = shelfLocation.getBytes();
      for (int i = 0; i < tmp.length; i++) {
        block[pos++] = tmp[i];
        truncatePos = pos;
      }
    }
    // append end block
    block[pos++] = (byte) 0x00;
    
    if (marcMediaFormat != null) {
      tmp = marcMediaFormat.getBytes();
      for (int i = 0; i < tmp.length; i++) {
        block[pos++] = tmp[i];
        truncatePos = pos;
      }
    }
    // append end block
    block[pos++] = (byte) 0x00;
    
    if (onixMediaFormat != null) {
      tmp = onixMediaFormat.getBytes();
      for (int i = 0; i < tmp.length; i++) {
        block[pos++] = tmp[i];
        truncatePos = pos;
      }
    }
    // append end block
    block[pos++] = (byte) 0x00;
    
    if (subsidiaryOfAnOwnerInstitution != null) {
      tmp = subsidiaryOfAnOwnerInstitution.getBytes();
      for (int i = 0; i < tmp.length; i++) {
        block[pos++] = tmp[i];
        truncatePos = pos;
      }
    }

    if (truncatePos != pos) {
      block[0] = (byte) truncatePos;
      block = Arrays.copyOf(block, truncatePos);
    }

    byte xor = 0x00;
    for (int i = 0; i < block.length; i++) {
      xor ^= block[i];
    }
    xor ^= 0x00;

    block[3] = xor;

    return block;
  }

  public void setShelfLocation(String shelfLocation) {
    this.shelfLocation = shelfLocation;
  }

  public void setMarcMediaFormat(String marcMediaFormat) {
    this.marcMediaFormat = marcMediaFormat;
  }

  public void setOnixMediaFormat(String onixMediaFormat) {
    this.onixMediaFormat = onixMediaFormat;
  }

  public void setSubsidiaryOfAnOwnerInstitution(String subsidiaryOfAnOwnerInstitution) {
    this.subsidiaryOfAnOwnerInstitution = subsidiaryOfAnOwnerInstitution;
  }

  
  
}
