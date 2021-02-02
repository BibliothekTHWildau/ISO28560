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
public class ISO28560_3ExtensionBlockFactory {

  /**
   *
   * @param data
   * @return
   */
  public ISO28560_3ExtensionBlock getInstance(byte[] data) {

    int ebType = (int) data[1] + (int) data[2];
    switch (ebType) {
      case 1:
        return new ISO28560_3LibraryExtensionBlock(data);
      case 2:
        return new ISO28560_3AcquisitionExtensionBlock(data);
      case 3:
        return new ISO28560_3LibrarySupplementExtensionBlock(data);
      case 4:
        return new ISO28560_3TitleExtensionBlock(data);
      case 5:
        return new ISO28560_3ILLExtensionBlock(data);
      default:
        return null;
    }

  }

  public ISO28560_3ExtensionBlockFactory() {
  }
;
}
