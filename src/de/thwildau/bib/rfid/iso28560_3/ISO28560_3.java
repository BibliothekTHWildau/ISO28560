package de.thwildau.bib.rfid.iso28560_3;

import static de.thwildau.bib.rfid.iso28560_3.ISO28560_3_BasicBlock.TagCRC;
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
  private ArrayList<ISO28560_3_ExtensionBlock> extensionBlocks;

  //ISO28560 basic block data
  private ISO28560_3_BasicBlock basicBlock;

  private static ISO28560_3_ExtensionBlockFactory extensionBlockFactory = new ISO28560_3_ExtensionBlockFactory();

  private byte[] data;
  

  /**
   *
   * @param data
   *
   */
  public ISO28560_3(byte[] data) throws ISO28560_Exception {

    this.data = data;

    basicBlock = new ISO28560_3_BasicBlock(this.data);

    if (basicBlock.isCrcError()) {
      throw new ISO28560_Exception("CRC error in basic block.");
    }

    //System.out.println("├ Basic block: " + basicBlock.toJSON());    
    while (hasExtensionBlock()) {
      extractExtensionBlock();
    }
  }

  /**
   * 
   * @param truncated 
   */
  public ISO28560_3(boolean truncated){
    basicBlock = new ISO28560_3_BasicBlock(truncated);
    extensionBlocks = new ArrayList<>();
  }
  
  
  /**
   * checks for an existing extension block
   *
   * @return
   */
  private boolean hasExtensionBlock() {
    try {
      return (this.data[nextBlockStart] != 0x00);
    } catch (IndexOutOfBoundsException | NullPointerException iex) {
      return false;
    }

  }

  /**
   * extracts an extension block
   */
  private void extractExtensionBlock() throws ISO28560_Exception {

    if (extensionBlocks == null) {
      extensionBlocks = new ArrayList<>();
    }

    try {

      int actBlockStart = nextBlockStart;
      int length = this.data[nextBlockStart];
      //length of block + start of block

      nextBlockStart = actBlockStart + length;
      //System.out.println("│ Special block expected at " + actBlockStart + " (length: " + length + ")");
      ISO28560_3_ExtensionBlock block = extensionBlockFactory.getInstance(Arrays.copyOfRange(this.data, actBlockStart, nextBlockStart));
      //System.out.println("├ " + block.getName() + ": " +  block.toJSON());
      extensionBlocks.add(block);

    } catch (Exception e) {
      throw new ISO28560_Exception("Error in extractExtensionBlock.", e);
      //System.err.println("│ Error during parseExtensionBlock " + this.getUID());
      //System.out.println(e);
    }

  }

  public ArrayList<ISO28560_3_ExtensionBlock> getExtensionBlocks() {
    return extensionBlocks;
  }

  public ISO28560_3_BasicBlock getBasicBlock() {
    return basicBlock;
  }

  /**
   * creates an empty data block (zero values)
   *
   * @param size
   * @return data block with zero values
   */
  static public byte[] getEmptyBlock(int size) {
    byte[] block = new byte[size];
    Arrays.fill(block, (byte) 0);
    return block;
  }
  
  /**
   * creates and appends an empty extension block to data model
   * @param type extension block id
   * @return returns the created block 
   */
  public ISO28560_3_ExtensionBlock createExtensionBlock(int type){
    ISO28560_3_ExtensionBlock block = extensionBlockFactory.getInstance(type);
    extensionBlocks.add(block);
    return block;
  }
  
  public void removeExtensionBlock(){
    
  }
  
  /**
   * returns iso28560-3 datamodel as byte array
   * @param block
   * @return 
   */
  public byte[] getISO28560(int tagSize){
    
    byte[] block = basicBlock.getBasicBlock();
    int index = block.length;
    System.out.println("Basic block: " + bytesToHex(block));
     
    // each extension block
    for (ISO28560_3_ExtensionBlock entry : extensionBlocks) {
      
      byte[] eBlock = entry.getExtensionBlock();
      
      // extend block to new size
      block = Arrays.copyOf(block, block.length + eBlock.length);
      
      // copy extension block into block
      for (int i = 0; i < eBlock.length; i++){
        block[index + i] = eBlock[i];
      }
      System.out.println("ext block added: " + bytesToHex(block));
      index = block.length;
     
    }
    // mandatory end block
    block = Arrays.copyOf(block, block.length + 1);
    block[block.length - 1] = (byte) 0x00;
    
    return block;
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

  /*
  public byte[] getBytes(int size) throws Exception {
    if (size == 0) {
      size = 2048;
    }
    byte[] block = getEmptyBlock(size);
    block[0] = (byte) ((basicBlock.getContentParameter() << 4) | basicBlock.getTypeOfUsage());
    block[1] = (byte) basicBlock.getPartsInItem();
    block[2] = (byte) basicBlock.getPartNumber();

    byte[] temp = basicBlock.getPrimaryItemIdentifier().getBytes();
    for (int i = 3; i <= 3 + 15; i++) {
      if (i - 3 < temp.length) {
        block[i] = temp[i - 3];
      }
    }
    
    temp = basicBlock.getISIL().getBytes();
    for (int i = 21; i < block.length; i++) {
      if (i - 21 < temp.length) {
        block[i] = temp[i - 21];
      }
    }

    // optional data blocks start here
    int optStart = 23 + temp.length;

    int crc = TagCRC(block);
    // Binary encoding with the lsb stored at the lowest memory location
    byte[] crcbytes = new byte[]{(byte) (crc & 0xFF), (byte) (crc >> 8 & 0xFF)};
    block[19] = crcbytes[0];
    block[20] = crcbytes[1];

    if (extensionBlocks != null) {
      for (ISO28560_3ExtensionBlock entry : extensionBlocks) {
        byte[] d = entry.getData();
        long id = entry.getID();
        int s;
        if (id < 0xffff) {
          s = d.length + 4;
          if (optStart + s > size) {
            throw new Exception("blocksize too small");
          }
          block[optStart] = (byte) s;
          block[optStart + 1] = (byte) (id & 0xff);
          block[optStart + 2] = (byte) ((id >> 8) & 0xff);
          for (int i = 0; i < d.length; i++) {
            block[optStart + 3 + i] = d[i];
          }
        } else {
          s = d.length + 6;
          if (optStart + s > size) {
            throw new Exception("blocksize too small");
          }
          block[optStart] = (byte) s;
          block[optStart + 1] = (byte) (id & 0xff);
          block[optStart + 2] = (byte) (0xff);
          block[optStart + 3] = (byte) ((id >> 8) & 0xff);
          block[optStart + 4] = (byte) ((id >> 16) & 0xff);
          for (int i = 0; i < d.length; i++) {
            block[optStart + 5 + i] = d[i];
          }
        }
        byte xor = 0;
        for (int i = 0; i < s - 1; i++) {
          xor ^= block[optStart + i];
        }
        xor ^= 0;
        block[optStart + s - 1] = xor;
        optStart += s;
      }
    }

    // end block (size==0)
    block[optStart] = 0x00;

    data = Arrays.copyOfRange(block, 0, optStart + 1);
    return data;
  }*/

}
