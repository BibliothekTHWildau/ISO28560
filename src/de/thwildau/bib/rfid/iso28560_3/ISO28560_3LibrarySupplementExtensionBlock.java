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
public class ISO28560_3LibrarySupplementExtensionBlock extends ISO28560_3ExtensionBlock {

  private String shelfLocation = null;
  private String marcMediaFormat = null;
  private String onixMediaFormat = null;
  private String subsidiaryOfAnOwnerInstitution = null;

  public ISO28560_3LibrarySupplementExtensionBlock(byte[] data) {

    super(data);

    if (this.isValidBlock()) {
      parseExtensionBlockPayload(this.payload);
    }

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


}
