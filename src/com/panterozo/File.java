package com.panterozo;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class File {

	private HashMap<String, String> romanNumbers = new HashMap<>();
	private String[] line = null;
	public String[] getLine() {
		return line;
	}

	public void setLine(String[] line) {
		this.line = line;
	}

	public File(){
		
	}
	
	public int readFile(String fileName){
		String line = null;
		try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = 
                new FileReader(fileName);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = 
                new BufferedReader(fileReader);

            while((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
                parseLine(line);
            }   

            // Always close files.
            bufferedReader.close();         
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                fileName + "'");                
            return 0;
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" 
                + fileName + "'");                  
            // Or we could just do this: 
            // ex.printStackTrace();
            return 0;
        }
		 
		return 1;
	}
	
	public void parseLine(String line){
		/*Three types of inputs
		 * 
		 * 1) Definition
		 * String of three elements. The Second must be 'is', without quotation
		 * 2) Calculation of Credits
		 * This String ends with 'Credits'. The previous value is the Value To be Calculated
		 * 3) Asks about an amount
		 * This String start with how & ends with '?'. The value to be calculated is after 'is' word and before '?' char.
		 *
		 * */
		switch(checkWichDefinitionIs(line)){
			case 1:
				LoadFirstDefinition();
				System.out.println("First Case\n");
				break;
			case 2:
				LoadSecondDefinition();
				System.out.println("Second Case\n");
				break;
			case 3:
				System.out.println("Third Case\n");
				break;
			case -1:
				/*Do anything with this*/
				System.out.println("Dont Know Case Structure\n");
				break;
		}
		
	}
	
	public void LoadFirstDefinition(){
		try{
			String[] Array = getLine();
			this.romanNumbers.put(Array[0], Array[2]);
		}catch(Exception e){
			
		}
	}
	
	public void LoadSecondDefinition(){
		try{
			String[] Array = getLine();
			for(int i = 0; i < Array.length; i++){
				if(isInDefinition(Array[0])==true){
					
				}else{
					/*De value isnt defined*/
				}
			}
			this.romanNumbers.put( Array[0], Array[2]);
		}catch(Exception e){
			
		}
	}
	
	public boolean isInDefinition(String value){
		if(this.romanNumbers.get(value)==null){
			return false;
		}else{
			return true;	
		}
	}
	
	public int checkWichDefinitionIs(String value){
		String[] Array = null;
		
		try {
			Array = value.split("\\s+");
			//System.out.println("=> "+Array.length);
			/* First Case */
			if(Array.length==3){
				if(Array[1].equals("is")){
					if(Array[2].equals("I") || Array[2].equals("V") || Array[2].equals("X") || Array[2].equals("L")){
						setLine(Array);
						return 1;	
					}else{
						/*It must be at least one of the letter above*/
						return -1;
					}
				}else{
					/*I don't know what are you talking about*/
					return -1;
				}
			}		
			if(Array.length>3){
				if(Array[Array.length-1].equals("Credits") && Array[Array.length-3].equals("is")){
					/*The last element is 'Credits AND two previous is 'is''*/
					try{
						/*Check if is a Integer number*/
						Integer.parseInt(Array[Array.length-2]);
						setLine(Array);
						return 2;
					}
					catch(Exception e){return -1;}
				}
				if(Array[Array.length-1].equals("?")){
					/*The last element is '?'*/
					if(Array[0].equals("how")){
						if(Array[1].equals("much") && Array[2].equals("is")
								&&  !Array[3].equals("?")){
							return 3;
						}else if(Array[1].equals("many")){
							if(Array[2].equals("Credits") && Array[3].equals("is") 
									&& !Array[4].equals("?")){
								setLine(Array);
								return 3;
							}else{
								return -1;
							}
						}else{
							return -1;
						}
					}else{
						return -1;
					}
				}
			}
		} catch (Exception ex) {
			/*This exception could occur if an undefined element in an array is evaluated*/
		    return -1;	
		}
		return -1;
	}
	
}
