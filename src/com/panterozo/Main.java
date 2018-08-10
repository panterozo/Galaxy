package com.panterozo;

public class Main {

	public static void main(String[] args) {

		for(int i=1;i<6;i++){
			String fileName = "test/test_0"+i+".txt";
	        Operations file = new Operations();
	        file.readFile(fileName);	
	        System.out.println("FILE: "+fileName+"\n");
	        System.out.println("## Input \n"+file.getLinesInFile()+"\n");
	        System.out.println("## Output \n"+file.getMessageToReturn()+"\n");
	        System.out.println("\n");
		}    
	}

}
