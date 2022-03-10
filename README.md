# file-ingestor
The primary objective of this application is to maximize the usage of CPU while ingesting records from a file, for further processing. When we do that, things get faster! 
And that is the secondary objective!

Reading a file sequentially, in a single thread of processing, parsing it for record and field separators and assembling the fields into an usable object wastes a lot of
  CPU cycles. This utility changes all of that! The idea is to view the input file as a sequence of bytes, slicing it into small, manageable segments, map these segments to a **java.nio.MappedByteBuffer**, processing them in parallel.  
  
  
  > ### Please see documents in the 'Analysis & Design' folder for details
  
