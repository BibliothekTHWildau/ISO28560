/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.thwildau.bib.rfid.iso28560_3;

/**
 *
 * @author jan
 */
public abstract class ISO28560_3ExtensionBlock {

  private String name;

  private int length;
  
  private int Data_block_ID;
  
  int XOR_checkusm;
  
  private boolean validBlock;

  byte[] payload;
  
  public int getLength() {
    return length;
  }

  public void setLength(int length) {
    this.length = length;
  }

  public int getData_block_ID() {
    return Data_block_ID;
  }

  public void setData_block_ID(int data_block_ID) {
    Data_block_ID = data_block_ID;
  }

  /*public int getXOR_checkusm() {
    return XOR_checkusm;
  }*/

  public void setXOR_checkusm(int XOR_checkusm) {
    this.XOR_checkusm = XOR_checkusm;
  }  

  public String getName() {
    return name;
  }

  public boolean isValidBlock() {
    return validBlock;
  }

  /*public byte[] getPayload() {
    return payload;
  }*/
  

  public ISO28560_3ExtensionBlock(byte[] data) {
    
    //System.out.println("data length: " + data.length + " : " + HexConvert.byteArrayToHexString(data));

    length = data[0];

    Data_block_ID = (int) data[1] + (int) data[2];

    name = getExtensionBlockType(Data_block_ID);
    
    int generatedChecksum = data[0];
    //blockstart +1 bcause start is already in crc
    for (int i = 1; i < length; i++) {
      generatedChecksum ^= data[i];
    }
    if (generatedChecksum == 0x00) {
      validBlock = true;
      payload = new byte[length - 4];
      int payloadPos = 0;
      //System.out.println("checksum valid building payload:");
      for (int i = 4; i < length; i++) {
        //System.out.println(i + " : " + payloadPos+ " " + data[i]);
        payload[payloadPos++] = (byte) data[i];
      }
      
    } else {
      validBlock = false;
      System.out.println("invalid checksum on extension block");
    }

  }

  private String getExtensionBlockType(int type) {
    switch (type) {
      case 1:
        return "Library extension block";
      case 2:
        return "Acquisition extension block";
      case 3:
        return "Library supplement block";
      case 4:
        return "Title block";
      case 5:
        return "ILL block";
      case 6:
      case 100:
        return "Other structured extension blocks (for future use)";
      default:
        return "unknown block type: " + type;
    }
  }

  
  abstract void parseExtensionBlockPayload(byte[] payload);

  
}
