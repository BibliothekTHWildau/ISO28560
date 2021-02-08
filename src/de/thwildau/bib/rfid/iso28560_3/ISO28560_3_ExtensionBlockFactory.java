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
public class ISO28560_3_ExtensionBlockFactory {

  /**
   *
   * @param data
   * @return
   */
  public ISO28560_3_ExtensionBlock getInstance(byte[] data) {

    int ebType = (int) data[1] + (int) data[2];
    switch (ebType) {
      case 1:
        return new ISO28560_3_LibraryExtensionBlock(data);
      case 2:
        return new ISO28560_3_AcquisitionExtensionBlock(data);
      case 3:
        return new ISO28560_3_LibrarySupplementExtensionBlock(data);
      case 4:
        return new ISO28560_3_TitleExtensionBlock(data);
      case 5:
        return new ISO28560_3_ILLExtensionBlock(data);
      default:
        return null;
    }

  }
  
  public ISO28560_3_ExtensionBlock getInstance(int ebType) {
    System.out.println("ebType" + ebType);
    switch (ebType) {
      case 1:
        return new ISO28560_3_LibraryExtensionBlock();
      case 2:
        return new ISO28560_3_AcquisitionExtensionBlock();
      case 3:
        return new ISO28560_3_LibrarySupplementExtensionBlock();
      case 4:
        return new ISO28560_3_TitleExtensionBlock();
      case 5:
        return new ISO28560_3_ILLExtensionBlock();
      default:
        return null;
    }

  }

  public ISO28560_3_ExtensionBlockFactory() {
  }
;
}
