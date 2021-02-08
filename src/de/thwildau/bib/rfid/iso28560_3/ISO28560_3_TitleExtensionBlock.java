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
public class ISO28560_3_TitleExtensionBlock extends ISO28560_3_ExtensionBlock {

  // case 1: return new String[]{ "Media format (19)","Primary item identifier (1) or alternative item identifier (22)","ISIL (3) or alternative owner institution (23)"};
  private String title = null;

  public ISO28560_3_TitleExtensionBlock(byte[] data) {

    super(data);

    if (this.validBlock) {
      parseExtensionBlockPayload(this.payload);
    }

  }
  
  public ISO28560_3_TitleExtensionBlock() {

    super(4);

  }

  public String getTitle() {
    return title;
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
        title = value;
        break;
      

    }
  }


  @Override
  byte[] getExtensionBlock() {
    int length = 1 + 2 + 1;

    length += title != null ? title.getBytes().length : 1;
    
    //System.out.println("Length of AcquisitionExtensionBlock: " + length);

    byte[] block = new byte[length];
    byte[] tmp;

    block[0] = (byte) length;
    block[1] = 4;
    block[2] = 0;

    int pos = 4;
    int truncatePos = 4;

    if (title != null) {
      tmp = title.getBytes();
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

  public void setTitle(String title) {
    this.title = title;
  }
  
  


}
