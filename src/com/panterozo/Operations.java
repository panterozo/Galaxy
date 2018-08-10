package com.panterozo;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class Operations {

	private HashMap<String, String> definitionNumbers = new HashMap<String, String>();
	private HashMap<String, String> definitionCalculated = new HashMap<String, String>();
	
	private String linesInFile = null;
	private String messageToReturn = "";
	

	private String[] line = null;
	
	
	public String getLinesInFile() {
		return linesInFile;
	}

	public void setLinesInFile(String linesInFile) {
		this.linesInFile = linesInFile;
	}

	
	public String[] getLine() {
		return line;
	}

	public void setLine(String[] line) {
		this.line = line;
	}
	public String getMessageToReturn() {
		return messageToReturn;
	}

	public void setMessageToReturn(String messageToReturn) {
		this.messageToReturn = messageToReturn;
	}

	public Operations(){
		
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

            int i =0;
            while((line = bufferedReader.readLine()) != null) {
            	if(i==0){
            		/*This is only for presentation*/
            		setLinesInFile(line);
            		i=1;
            	}else{
            		setLinesInFile(getLinesInFile() + "\n"+ line);
            	}
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
		String value = "";
		switch(checkWichDefinitionIs(line)){
			case 1:
				LoadFirstDefinition();
				//System.out.println("First Case\n");
				break;
			case 2:
				LoadSecondDefinition();
				//System.out.println("Second Case\n");
				break;
			case 3:
				value = LoadThirdDefinition();
				if(getMessageToReturn().equals("")){
					setMessageToReturn(value);
				}else{
					setMessageToReturn(getMessageToReturn()+"\n"+value);
				}
				break;
			case -1:
				/*Do anything with this*/
				value = "I have no idea what you are talking about2";
				if(getMessageToReturn().equals("")){
					setMessageToReturn(value);
				}else{
					setMessageToReturn(getMessageToReturn()+"\n"+value);
				}
				break;
		}
		
	}
	
	public void LoadFirstDefinition(){
		try{
			String[] Array = getLine();
			this.definitionNumbers.put(Array[0], Array[2]);
		}catch(Exception e){
			
		}
	}
	
	public int LoadSecondDefinition(){
		int retorno = 1;
		try{
			String[] Array = getLine();
			String romanNumber = "";
			for(int i = 0; i < Array.length - 4; i++){
				if(isInDefinition(Array[i])==true){
					/*Concatenate Letters in Roman Number*/
					romanNumber = romanNumber + this.definitionNumbers.get(Array[i]);
				}else{
					retorno = 0;
					break;
					/*The value isnt defined*/
				}
			}
			if(retorno == 1){
				boolean valid = romanNumber.matches("^M{0,4}(CM|CD|D?C{0,3})(XC|XL|L?X{0,3})(IX|IV|V?I{0,3})$");
				if(valid){
					int number = getNumberFromArabic(romanNumber);
					
					/*Big try & catch*/
					float numberToDivide = Integer.parseInt(Array[Array.length-2]);
					float valueCalculate = numberToDivide/number;
					this.definitionCalculated.put(Array[Array.length-4], valueCalculate+"");
					/*System.out.println("Number "+romanNumber+" => "+number);
					System.out.println("Number "+Array[Array.length-4]+" => "+valueCalculate);*/					
				}else{
					retorno = 0;
				}
			}
			//System.out.println("Number "+romanNumber+" => "+retorno);
		}catch(Exception e){
			retorno = 0;
		}
		return retorno;
	}
	
	public String LoadThirdDefinition(){
		String message = "";
		try{
			String[] Array = getLine();
			
			int init;
			if(Array[1].equals("much")){
				init = 3;
			}else{
				init = 4;
			}
			String romanNumber = "";
			float calculatedNumber = 1;
			for(int i = init; i < Array.length -1; i++){
				if(isInDefinition(Array[i])==true){
					/*Concatenate Letters in Roman Number*/
					romanNumber = romanNumber + this.definitionNumbers.get(Array[i]);
					
				}else if(isInDefinitionCalculated(Array[i])){
					/*Big try catch*/
					float valueToMultiply = Float.parseFloat(this.definitionCalculated.get(Array[i]));
					calculatedNumber = calculatedNumber*valueToMultiply;
				}else{
					return "I have no idea what you are talking about";
					/*unknown value*/
				}
				message = (i==init) ? Array[i] : message + " "+ Array[i];
			}
			boolean valid = romanNumber.matches("^M{0,4}(CM|CD|D?C{0,3})(XC|XL|L?X{0,3})(IX|IV|V?I{0,3})$");
			if(valid){
				int number = 1;
				if(!romanNumber.equals("")){
					number = getNumberFromArabic(romanNumber);	
				}					
				float mult = calculatedNumber*number;
				
				if(Array[1].equals("much")){
					return message+" is "+mult;
				}else{
					return message+" is "+mult+" Credits";
				}
				
				//System.out.println("Number: "+number+", Multi: "+mult);
			}else{
				/*don't know what are you talking about*/
				return "I have no idea what you are talking about";
			}				
			
		}catch(Exception e){
			return "I have no idea what you are talking about";
		}
	}
	
	public int getNumberFromArabic(String romanNumber){
		
		int sum = 0;
		int previous = 0;
		for(int i=romanNumber.length()-1; i>=0;i--){
			char letter = romanNumber.charAt(i);
			int valueLetter = 0;
			switch(letter){
				case 'I':
					valueLetter = 1;
					break;
				case 'V':
					valueLetter = 5;
					break;
				case 'X':
					valueLetter = 10;
					break;
				case 'L':
					valueLetter = 50;
					break;		
				/*To expand the solution only*/
				case 'C':
					valueLetter = 100;
					break;
				case 'D':
					valueLetter = 500;
					break;
				case 'M':
					valueLetter = 1000;
					break;
			}
			if(valueLetter >= previous){
				sum = valueLetter + sum;
			}else{
				sum = sum - valueLetter;
			}
			previous = valueLetter;
		}		
		return sum;
	}
	
	public boolean isInDefinition(String value){
		if(this.definitionNumbers.get(value)==null){
			return false;
		}else{
			return true;	
		}
	}
	
	public boolean isInDefinitionCalculated(String value){
		if(this.definitionCalculated.get(value)==null){
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
							setLine(Array);
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
