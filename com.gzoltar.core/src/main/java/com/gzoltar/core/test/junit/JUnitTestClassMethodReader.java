package com.gzoltar.core.test.junit;

import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;

public class JUnitTestClassMethodReader {
  String directory;
  String fileName;
  String absolutePath;
  String fileSeperator = "/"; //on windows must be "\\"
  public JUnitTestClassMethodReader() {
	  this.directory = System.getProperty("user.home");
	  this.fileName = "testAttributes.txt";
	  this.absolutePath = this.directory + this.fileSeperator + this.fileName;
  }
  
  public void writeClassMethod(String clazzName, String methodName) {
	  File fOld = new File(this.absolutePath);
	  fOld.delete(); //delete the old file
	  
	  File fNew = new File(this.absolutePath);
	  try(FileWriter fileWriter = new FileWriter(fNew)){
		  String fileContent = clazzName + " " + methodName;
		  fileWriter.write(fileContent);
		  fileWriter.close();
	  }
	  catch (IOException e){
		  e.printStackTrace();
	  }
  }
  
  public String[] readClassMethod() {
	  File f = new File(this.absolutePath);
	  String[] stringArray = new String[2];
	  stringArray[0] = "";
	  stringArray[1] = "";
	  int i = 0;
	  try(FileReader fileReader = new FileReader(f)){
		  BufferedReader br = new BufferedReader(fileReader);
		  int ch = 0;
		  while((ch = br.read()) != -1) {
			  char character = (char) ch;
			  if(character == ' ') {
				  i++;
			  }
			  else {
				  stringArray[i] = stringArray[i] + character;
			  }
		  }
		  br.close();
	  }
	  catch(IOException e){
		  e.printStackTrace();
	  }
	  
	  return stringArray;
  }
}