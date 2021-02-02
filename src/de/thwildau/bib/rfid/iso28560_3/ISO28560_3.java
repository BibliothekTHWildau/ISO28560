package de.thwildau.bib.rfid.iso28560_3;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author jan
 */
public class ISO28560_3 {

  //global byte
    
  //Extension blocks
  //internal pointer to the next extension block
  private int nextBlockStart = 34;
  //List of Extension Block objects
  private ArrayList<ISO28560_3ExtensionBlock> extensionBlocks;

  //ISO28560 basic block data
  private ISO28560_3BasicBlock basicBlock; 
  
  private ISO28560_3ExtensionBlockFactory extensionBlockFactory = new ISO28560_3ExtensionBlockFactory();

  private byte[] data;  
  private String uid;
  
  /**
   *
   * @param data
   * 
   */
  public ISO28560_3(byte[] data, String uid) throws ISO28560_Exception {

    this.data = data;
    this.uid = uid;

    basicBlock = new ISO28560_3BasicBlock(this.data);
    
    if (basicBlock.isCrcError()){
      throw new ISO28560_Exception("CRC error in basic block: " + this.uid);
     
    }
    
    //System.out.println("├ Basic block: " + basicBlock.toJSON());    

    while (hasExtensionBlock()) {
      parseExtensionBlock();
    }
  }
  
 
  
  /**
   * checks for an existing extension block  
   * @return 
   */
  public boolean hasExtensionBlock() {
    try {
      return (this.data[nextBlockStart] != 0x00);
    } catch (IndexOutOfBoundsException | NullPointerException iex) {
      return false;
    }

  }

  /**
   * parses an extension block 
   */
  public void parseExtensionBlock() throws ISO28560_Exception {

    if (extensionBlocks == null) {
      extensionBlocks = new ArrayList<>();
    }

    try {
      
      int actBlockStart = nextBlockStart;
      int length = this.data[nextBlockStart];
      //length of block + start of block

      nextBlockStart = actBlockStart + length;
      //System.out.println("│ Special block expected at " + actBlockStart + " (length: " + length + ")");
      ISO28560_3ExtensionBlock block = extensionBlockFactory.getInstance(Arrays.copyOfRange(this.data, actBlockStart, nextBlockStart));
      //System.out.println("├ " + block.getName() + ": " +  block.toJSON());
      extensionBlocks.add(block);
      
    } catch (Exception e) {
      throw new ISO28560_Exception("Error in parseExtensionBlock: " + this.uid,e);
      //System.err.println("│ Error during parseExtensionBlock " + this.getUID());
      //System.out.println(e);
    }

  }
  
  /*public byte[] getData() {
    return data;
  }*/
  
  /*public String getUID() {
    return uid;
  }*/

  public ArrayList<ISO28560_3ExtensionBlock> getExtensionBlocks() {
    return extensionBlocks;
  }

  public ISO28560_3BasicBlock getBasicBlock() {
    return basicBlock;
  }
  
}
