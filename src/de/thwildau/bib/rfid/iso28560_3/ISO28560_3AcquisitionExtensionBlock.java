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
public class ISO28560_3AcquisitionExtensionBlock extends ISO28560_3ExtensionBlock {

  // case 1: return new String[]{ "Media format (19)","Primary item identifier (1) or alternative item identifier (22)","ISIL (3) or alternative owner institution (23)"};
  private String supplierIdentifier = null;
  private String productIdentifierLocal = null;
  private String orderNumber = null;
  private String supplierInvoiceNumber = null;
  private String gs1ProductIdentifier = null;
  private String supplyChainStage = null;

  //private String[] keys = {"supplierIdentifier", "productIdentifierLocal", "orderNumber", "supplierInvoiceNumber", "gs1ProductIdentifier", "supplyChainStage"};
  //private Object[] values = {null, null, null, null, null, null};

  public ISO28560_3AcquisitionExtensionBlock(byte[] data) {

    super(data);
    
    //System.out.println(" ISO28560AcquisitionExtensionBlock:");
    if (this.isValidBlock()) {
      //System.out.println(" -payload:" + HexConvert.byteArrayToHexString(this.payload));
      parseExtensionBlockPayload(this.payload);
    }

  }

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

  @Override
  void parseExtensionBlockPayload(byte[] payload) {

    int pos = 0;
    int key = 0;
    String value = null;
    // itemIdentifier 
    while (pos < payload.length) {

      if (payload[pos] == 0x00) {
        //data.set(key, value);

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
        key++;
        value = null;
      } else {
        if (value == null) {
          value = "";
        }

        value += (char) payload[pos];
      }

      pos++;
    }

  }


}
