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
public class ISO28560_3_AcquisitionExtensionBlock extends ISO28560_3_ExtensionBlock {

  // case 1: return new String[]{ "Media format (19)","Primary item identifier (1) or alternative item identifier (22)","ISIL (3) or alternative owner institution (23)"};
  private String supplierIdentifier = null;
  private String productIdentifierLocal = null;
  private String orderNumber = null;
  private String supplierInvoiceNumber = null;
  private String gs1ProductIdentifier = null;
  private String supplyChainStage = null;

  /**
   * Standard constructor when tag with data is given
   *
   * @param data
   */
  public ISO28560_3_AcquisitionExtensionBlock(byte[] data) {

    super(data);

    if (this.validBlock) {
      parseExtensionBlockPayload(this.payload);
    }
  }

  /**
   * constructor when an empty block is generated
   *
   * @param type
   */
  public ISO28560_3_AcquisitionExtensionBlock() {

    super(2);
  }

  // getters
  public String getSupplierIdentifier() {
    return supplierIdentifier;
  }

  public String getProductIdentifierLocal() {
    return productIdentifierLocal;
  }

  public String getOrderNumber() {
    return orderNumber;
  }

  public String getSupplierInvoiceNumber() {
    return supplierInvoiceNumber;
  }

  public String getGs1ProductIdentifier() {
    return gs1ProductIdentifier;
  }

  public String getSupplyChainStage() {
    return supplyChainStage;
  }

  /**
   * extract object values from payload of extension block
   *
   * @param payload
   */
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
        supplierIdentifier = value;
        break;
      case 1:
        productIdentifierLocal = value;
        break;
      case 2:
        orderNumber = value;
        break;
      case 3:
        supplierInvoiceNumber = value;
        break;
      case 4:
        gs1ProductIdentifier = value;
        break;
      case 5:
        supplyChainStage = value;
        break;
    }
  }

  /**
   * returns extension block as byte array
   *
   * @return
   */
  @Override
  byte[] getExtensionBlock() {
    int length = 1 + 2 + 1;

    length += supplierIdentifier != null ? supplierIdentifier.getBytes().length + 1 : 1;
    length += productIdentifierLocal != null ? productIdentifierLocal.getBytes().length + 1 : 1;
    length += orderNumber != null ? orderNumber.getBytes().length : 1;
    length += supplierInvoiceNumber != null ? supplierInvoiceNumber.getBytes().length + 1 : 1;
    length += gs1ProductIdentifier != null ? gs1ProductIdentifier.getBytes().length + 1 : 1;
    length += supplyChainStage != null ? supplyChainStage.getBytes().length : 1;

    //System.out.println("Length of AcquisitionExtensionBlock: " + length);

    byte[] block = new byte[length];
    byte[] tmp;

    block[0] = (byte) length;
    block[1] = 2;
    block[2] = 0;

    int pos = 4;
    int truncatePos = 4;

    if (supplierIdentifier != null) {
      tmp = supplierIdentifier.getBytes();
      for (int i = 0; i < tmp.length; i++) {
        block[pos++] = tmp[i];
        truncatePos = pos;
      }
    }
    // append end block
    block[pos++] = (byte) 0x00;
    
    if (productIdentifierLocal != null) {
      tmp = productIdentifierLocal.getBytes();
      for (int i = 0; i < tmp.length; i++) {
        block[pos++] = tmp[i];
        truncatePos = pos;
      }
    }
    // append end block
    block[pos++] = (byte) 0x00;
    
    if (orderNumber != null) {
      tmp = orderNumber.getBytes();
      for (int i = 0; i < tmp.length; i++) {
        block[pos++] = tmp[i];
        truncatePos = pos;
      }
    }
    // append end block
    block[pos++] = (byte) 0x00;
    
    if (supplierInvoiceNumber != null) {
      tmp = supplierInvoiceNumber.getBytes();
      for (int i = 0; i < tmp.length; i++) {
        block[pos++] = tmp[i];
        truncatePos = pos;
      }
    }
    // append end block
    block[pos++] = (byte) 0x00;
    
    if (gs1ProductIdentifier != null) {
      tmp = gs1ProductIdentifier.getBytes();
      for (int i = 0; i < tmp.length; i++) {
        block[pos++] = tmp[i];
        truncatePos = pos;
      }
    }
    // append end block
    block[pos++] = (byte) 0x00;
    
    if (supplyChainStage != null) {
      tmp = supplyChainStage.getBytes();
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

  public void setSupplierIdentifier(String supplierIdentifier) {
    this.supplierIdentifier = supplierIdentifier;
  }

  public void setProductIdentifierLocal(String productIdentifierLocal) {
    this.productIdentifierLocal = productIdentifierLocal;
  }

  public void setOrderNumber(String orderNumber) {
    this.orderNumber = orderNumber;
  }

  public void setSupplierInvoiceNumber(String supplierInvoiceNumber) {
    this.supplierInvoiceNumber = supplierInvoiceNumber;
  }

  public void setGs1ProductIdentifier(String gs1ProductIdentifier) {
    this.gs1ProductIdentifier = gs1ProductIdentifier;
  }

  public void setSupplyChainStage(String supplyChainStage) {
    this.supplyChainStage = supplyChainStage;
  }

  private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

  public static String bytesToHex(byte[] bytes) {
    char[] hexChars = new char[bytes.length * 2];
    for (int j = 0; j < bytes.length; j++) {
      int v = bytes[j] & 0xFF;
      hexChars[j * 2] = HEX_ARRAY[v >>> 4];
      hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
    }
    return new String(hexChars);
  }

}
