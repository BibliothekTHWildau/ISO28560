# ISO28560 Datamodel for libraries

Create a raw ISO28560-3 model with all extensions blocks set

```java
boolean truncated = false;
int mediaFormat = 2;
    
ISO28560_3 model = new ISO28560_3(truncated);
    
model.getBasicBlock().setPrimaryItemIdentifier("1000000136");
model.getBasicBlock().setISIL("DK-718500");
model.getBasicBlock().setPartsInItem(1);
model.getBasicBlock().setPartNumber(1);
    
ISO28560_3_LibraryExtensionBlock libBlock = (ISO28560_3_LibraryExtensionBlock) model.createExtensionBlock(1);
libBlock.setMediaFormat(mediaFormat);
//libBlock.setItemIdentifier("abc");
libBlock.setOwnerInstitution("DE-123");
     
ISO28560_3_AcquisitionExtensionBlock acqBlock = (ISO28560_3_AcquisitionExtensionBlock) model.createExtensionBlock(2);
acqBlock.setSupplierIdentifier("Bogvognen");
acqBlock.setProductIdentifierLocal("1234567890");
acqBlock.setSupplierInvoiceNumber("a789656c");
  
ISO28560_3_LibrarySupplementExtensionBlock supBlock = (ISO28560_3_LibrarySupplementExtensionBlock) model.createExtensionBlock(3);
supBlock.setMarcMediaFormat("marcMediaFormat");
supBlock.setOnixMediaFormat("onixMediaFormat");
supBlock.setShelfLocation("shelfLocation");
supBlock.setSubsidiaryOfAnOwnerInstitution("subsidiaryOfAnOwnerInstitution");
    
ISO28560_3_TitleExtensionBlock titleBlock = (ISO28560_3_TitleExtensionBlock) model.createExtensionBlock(4);
titleBlock.setTitle("title");
    
ISO28560_3_ILLExtensionBlock illBlock = (ISO28560_3_ILLExtensionBlock) model.createExtensionBlock(5);
illBlock.setAlternativeILLBorrowingInstitution("alternativeILLBorrowingInstitution");
illBlock.setIllBorrowingInstitution("illBorrowingInstitution");
illBlock.setIllBorrowingTransactionNumber("illBorrowingTransactionNumber");
    
// retrieve byte array to write to a tag
byte [] data = model.getISO28560(80);


// build iso model from data
ISO28560_3 iso = new ISO28560_3(data);
```
