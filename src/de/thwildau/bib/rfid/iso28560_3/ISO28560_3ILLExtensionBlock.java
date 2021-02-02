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
public class ISO28560_3ILLExtensionBlock extends ISO28560_3ExtensionBlock {

  // case 1: return new String[]{ "Media format (19)","Primary item identifier (1) or alternative item identifier (22)","ISIL (3) or alternative owner institution (23)"};
  private String illBorrowingInstitution = null;
  private String illBorrowingTransactionNumber = null;
  private String alternativeILLBorrowingInstitution = null;
  
  
  public ISO28560_3ILLExtensionBlock(byte[] data) {

    super(data);
    
    //System.out.println(" ISO28560AcquisitionExtensionBlock:");
    if (this.isValidBlock()) {
      //System.out.println(" -payload:" + HexConvert.byteArrayToHexString(this.payload));
      parseExtensionBlockPayload(this.payload);
    }

  }

  public String getIllBorrowingInstitution() {
    return illBorrowingInstitution;
  }

  public String getIllBorrowingTransactionNumber() {
    return illBorrowingTransactionNumber;
  }

  public String getAlternativeILLBorrowingInstitution() {
    return alternativeILLBorrowingInstitution;
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
            illBorrowingInstitution = value;
            break;
          case 1:
            illBorrowingTransactionNumber = value;
            break;
          case 2:
            alternativeILLBorrowingInstitution = value;
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
