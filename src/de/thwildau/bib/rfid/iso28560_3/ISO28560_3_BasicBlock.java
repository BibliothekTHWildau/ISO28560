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
public class ISO28560_3_BasicBlock {

  private int ContentParameter = 0; // Version
  private int TypeOfUsage;
  private int partNumber;
  private int partsInItem;
  private String PrimaryItemIdentifier;
  private byte[] crc;
  private String ISIL;
  //private String CountryCode; //deprecated

  private boolean truncatedBasicBlock = false;

  // crc calculation 
  private byte[] crcbytes = new byte[]{0x00, 0x00};
  private boolean crcError = false;

  /**
   * constructor called when userdata on tag is present.
   * @param userdata 
   */
  public ISO28560_3_BasicBlock(byte[] userdata) {

    if (userdata.length <= 32) {
      truncatedBasicBlock = true;
    }

    // as from https://github.com/je4/Library-RFID-Tools/blob/master/src/org/objectspace/rfid/FinnishDataModel.java
    //Type of usage & Content Parameter
    ContentParameter = userdata[0] >> 4;//Integer.parseInt((char)tmp.charAt(0)+"");
    TypeOfUsage = userdata[0] & 0x0f;//Integer.parseInt((char)tmp.charAt(1)+"");

    //Set information
    partNumber = userdata[1];
    partsInItem = userdata[2];

    //PrimaryItemIdentifier:
    PrimaryItemIdentifier = new String(userdata, 3, 16).trim();

    // crc
    crc = new byte[]{userdata[19], userdata[20]};

    // compare crc with original crc
    // Binary encoding with the lsb stored at the lowest memory location 
    int crc = TagCRC(userdata);
    crcbytes = new byte[]{(byte) (crc & 0xFF), (byte) (crc >> 8 & 0xFF)};
    //System.out.println("Expected CRC: " + HexConvert.byteArrayToHexString(crcbytes));
    crcError = (getCrc()[0] != crcbytes[0] || getCrc()[1] != crcbytes[1]);

    //ISIL
    ISIL = truncatedBasicBlock ? new String(userdata, 21, 11).trim() : new String(userdata, 21, 13).trim();

  }
  
  /**
   * constructor for empty basic block
   * @param truncated if true basic block is limited to 32 bytes, else 34 bytes
   */
  public ISO28560_3_BasicBlock(boolean truncated){
    this.truncatedBasicBlock = truncated;
    
    // fix values
    ContentParameter = 1;
    TypeOfUsage = 1;
  }

  /**
   * 
   * @return 
   */
  public boolean isCrcError() {
    return crcError;
  }

  /**
   * creates crc checksum of data block (excluding byte 19/20)   *
   * @param data
   * @return
   */
  protected static int TagCRC(byte[] data) {
    // create a byte array without crc bytes
    byte[] crcdata = new byte[data.length];
    System.arraycopy(data, 0, crcdata, 0, 19);
    System.arraycopy(data, 21, crcdata, 19, data.length - 21);
    int p = 19 + data.length - 21;
    while (p < 32) {
      crcdata[p] = 0x00;
      p++;
    }

    return CRC16CCITT(crcdata, 0, 32);
  }

  /**
   * creates a crc16 checksum of a data block
   *
   * @param data the data block
   * @param start start byte
   * @param length number of bytes to use
   * @return
   */
  protected static int CRC16CCITT(byte[] data, int start, int length) {
    int crc = 0xFFFF; // initial value
    int polynomial = 0x1021; // 0001 0000 0010 0001 (0, 5, 12)

    // byte[] testBytes = "123456789".getBytes("ASCII");
    for (int p = start; p < start + length; p++) {
      for (int i = 0; i < 8; i++) {
        boolean bit = ((data[p] >> (7 - i) & 1) == 1);
        boolean c15 = ((crc >> 15 & 1) == 1);
        crc <<= 1;
        if (c15 ^ bit) {
          crc ^= polynomial;
        }
      }
    }

    crc &= 0xffff;
    return crc;
    // System.out.println("CRC16-CCITT = " + Integer.toHexString(crc));
  }

  public String getTypeOfUsageToString() {
    switch (getTypeOfUsage()) {
      case 0:
        return "Acquisition item";
      case 1:
        return "Item for circulation";
      case 2:
        return "Item not for circulation";
      case 3:
      case 4:
        return "For local use";
      case 5:
        return "For future use";
      case 6:
        return "No information about usage of the tag";
      case 7:
        return "Discarded item";
      case 8:
        return "Patron card";
      case 9:
        return "Library equipment";
      default:
        return "";
    }
  }

  /**
   * ToDo what if it is a truncated basic block
   * @param block
   * @return 
   */
  byte[] getBasicBlock() {
    
    int size = isTruncatedBasicBlock() ? 32 : 34;
    byte[] block = ISO28560_3.getEmptyBlock(size);    

    block[0] = (byte) ((getContentParameter() << 4) | getTypeOfUsage());
    block[1] = (byte) getPartsInItem();
    block[2] = (byte) getPartNumber();

    byte[] temp = getPrimaryItemIdentifier().getBytes();
    for (int i = 3; i <= 3 + 15; i++) {
      if (i - 3 < temp.length) {
        block[i] = temp[i - 3];
      }
    }
    /*temp = countryOfOwnerLib.getBytes();
		for (int i = 21; i <= 21 + 2; i++) {
			if (i - 21 < temp.length) {
				block[i] = temp[i - 21];
			}
		}
		temp = ISIL.getBytes();
		for (int i = 23; i < block.length; i++) {
			if (i - 23 < temp.length) {
				block[i] = temp[i - 23];
			}
		}*/
    temp = getISIL().getBytes();
    for (int i = 21; i < block.length; i++) {
      if (i - 21 < temp.length) {
        block[i] = temp[i - 21];
      }
    }

    // optional data blocks start here
    //int optStart = 21 + temp.length;

    int crc = TagCRC(block);
    // Binary encoding with the lsb stored at the lowest memory location
    byte[] crcbytes = new byte[]{(byte) (crc & 0xFF), (byte) (crc >> 8 & 0xFF)};
    block[19] = crcbytes[0];
    block[20] = crcbytes[1];
    return block;
  }
  
  //Getters
  public boolean isTruncatedBasicBlock() {
    return truncatedBasicBlock;
  }

  public String getPrimaryItemIdentifier() {
    return PrimaryItemIdentifier;
  }

  public int getContentParameter() {
    return ContentParameter;
  }

  public int getTypeOfUsage() {
    return TypeOfUsage;
  }

  public int getPartNumber() {
    return partNumber;
  }

  public int getPartsInItem() {
    return partsInItem;
  }

  public String getISIL() {
    return ISIL;
  }

  public byte[] getCrc() {
    return crc;
  }
  //Setter

  public void setCrc(byte[] crc) {
    this.crc = crc;
  }

  public void setPrimaryItemIdentifier(String primaryItemIdentifier) {
    PrimaryItemIdentifier = primaryItemIdentifier;
  }

  public void setISIL(String ISIL) {
    ISIL = ISIL.replaceFirst("-", "");
    this.ISIL = ISIL;
  }

  public void setPartNumber(int partNumber) {
    this.partNumber = partNumber;
  }

  public void setPartsInItem(int partsInItem) {
    this.partsInItem = partsInItem;
  }
  
  
}
