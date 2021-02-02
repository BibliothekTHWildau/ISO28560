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
public class ISO28560_3LibraryExtensionBlock extends ISO28560_3ExtensionBlock {

  //            case 1: return new String[]{ "Media format (19)","Primary item identifier (1) or alternative item identifier (22)","ISIL (3) or alternative owner institution (23)"};
  private String mediaFormat;
  private String[] mediaFormatValues = {"Undefined", "Book", "CD/DVD", "Magnetic tape", "Other", "Other, careful handling is required", "Very small item, special handling is required"};
  private String itemIdentifier; // optional variable followed by 00hex
  private String ownerInstitution;

  public String getMediaFormat() {
    return mediaFormat;
  }

  private String[] identifiers = {"mediaFormat", "itemIdentifier", "ownerInstitution"};

  public ISO28560_3LibraryExtensionBlock(byte[] data) {

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
            mediaFormat = mediaFormatValues[Integer.parseInt(value)];
            break;
          case 1:
            itemIdentifier = value;
            break;
          case 2:
            ownerInstitution = value;
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
