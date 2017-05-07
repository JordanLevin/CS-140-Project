package project;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Assembler2 {

	public static void assemble(File input, File output, ArrayList<String> errors){
		ArrayList<String> code = new ArrayList<String>();
		ArrayList<String> data = new ArrayList<String>();
		ArrayList<String> inText = new ArrayList<String>();
		ArrayList<String> outText = new ArrayList<String>();
		try {
			Scanner scan = new Scanner(input);
			while(scan.hasNextLine()){
				inText.add(scan.nextLine());
			}
			scan.close();
		} catch (FileNotFoundException e) {
			errors.add("Input file does not exist");
			return;
		}
		boolean firstSep = true;
		for(int index = 0;index<inText.size()-1;index++){
			if(index < inText.size()-1 && inText.get(index).trim().length() == 0 && inText.get(index+1).trim().length() > 0){
				errors.add("Error: line " + (index+1) + ". Illegal blank line in source file");
			}
			if(inText.get(index).length() == 0 || inText.get(index).trim().length() == 0){
				continue;
			}
			if(inText.get(index).charAt(0) == ' ' || inText.get(index).charAt(0) ==  '\t'){
				errors.add("Error: line " + (index+1) + " starts with white space");
			}
			if(inText.get(index).trim().toUpperCase().startsWith("--")){
				if(inText.get(index).trim().replace("-","").length()!=0){
					errors.add("Error: line " + (index+1) + " has a badly formatted data separator");
				}
				else if(!firstSep){
					errors.add("Error: line " + (index+1) + " has a duplicate data separator");
				}
				firstSep = false;
			}
			
//			if(errors.size() > 0){
//				return;
//			}
			
		}
		
		boolean sep = false;
		for(int index = 0;index<inText.size();index++){
			if(inText.get(index).startsWith("--") && !sep){
				data.add("-1");
				sep = true;
				continue;
			}
			if(!sep){
				code.add(inText.get(index));
			}
			else if(sep){
				data.add(inText.get(index));
			}
		}
		
		for(int index = 0; index<code.size();index++){
			int indirLvl = 0;
			if(code.get(index).trim().length() == 0){
				continue;
			}
			String[] parts = code.get(index).trim().split("\\s+");
			if(!InstructionMap.sourceCodes.contains(parts[0]) && !InstructionMap.sourceCodes.contains(parts[0].toUpperCase())){
				errors.add("Error: line " + (index+1) + " illegal mnemonic: " + parts[0]);
				continue;
			}
			if(InstructionMap.sourceCodes.contains(parts[0].toUpperCase()) &&  !InstructionMap.sourceCodes.contains(parts[0])){
				errors.add("Error: line " + (index+1) + " does not have the instruction mnemonic in upper case");
			}
			else if(InstructionMap.noArgument.contains(parts[0]) && parts.length != 1){
				errors.add("Error: line " + (index+1) + " this mnemonic cannot take arguments");
			}
			else if(!InstructionMap.noArgument.contains(parts[0]) && parts.length == 1){
				errors.add("Error: line " + (index+1) + " is missing an argument");
			}
			else if(parts.length > 2){
				errors.add("Error: line " + (index+1) + " has more than one argument");
			}
			else if(parts.length == 2){
				indirLvl = 1;
				if(parts[1].startsWith("[")){
					if(!InstructionMap.indirectOK.contains(parts[0])){
						errors.add("Error: line " + (index+1) + " tries to use indirect mode with an instruction that cant do that");
					}
					else if(!parts[1].endsWith("]")){
						errors.add("Error: line " + (index+1) + " is missing a closing bracket");
					}
					else{
						parts[1] = parts[1].substring(1, parts[1].length()-1);
						indirLvl = 2;
					}
				}
				else{
				int arg = 0; 
					try {
						arg = Integer.parseInt(parts[1],16);
					} catch (NumberFormatException e) {
						errors.add("Error: line " + (index+1) 
							+ " argument is not a hex number");
					}
				}
			}
			if(parts[0].endsWith("I")){
				indirLvl = 0;
			}
			else if(parts[0].endsWith("A")){
				indirLvl = 3;
			}
			if(parts[0].length() == 0){
				continue;
			}
			if(parts[0].trim().length() == 0){
				continue;
			}
			System.out.println(parts[0]);
			if(InstructionMap.sourceCodes.contains(parts[0])){
				int opcode = InstructionMap.opcode.get(parts[0]);
				
				if(parts.length == 1){
					outText.add(Integer.toHexString(opcode).toUpperCase() + " 0 0");
				}
				else if(parts.length == 2){
					outText.add(Integer.toHexString(opcode).toUpperCase() + " " + indirLvl + " " + parts[1]);
					System.out.println(outText.get(outText.size()-1));
				}
			}
		}
		//System.out.println("START OF DATA -----------------------");
		for(int index = 0;index < data.size();index++){
			if(data.get(index).equals("-1")){
				outText.add("-1");
				continue;
			}
			//System.out.println(data.get(index));
			if(data.get(index).trim().length() == 0){
				continue;
			}
			String[] parts = data.get(index).trim().split("\\s+");
			//System.out.println(parts.length);
			if(parts.length != 2){
				errors.add("Error: line " + (index+1+code.size()) + " does not consist of 2 numbers");
			}
			else
			{
				int arg = 0; 
				int arg0 = 0;
				boolean err = false;
				String dataLine = "";
				try {
					arg0 = Integer.parseInt(parts[0],16);
					dataLine += arg0 + " ";
				} catch (NumberFormatException e) {
					err = true;
					errors.add("Error: line " + (index+1+code.size()) 
						+ " data address is not a hex number");
				}
				try {
					arg = Integer.parseInt(parts[1],16);
					dataLine += arg;
				} catch (NumberFormatException e) {
					err = true;
					errors.add("Error: line " + (index+1+code.size()) 
						+ " data value is not a hex number");
				}
				if(!err){
					outText.add(dataLine);
				}
			}
			
		}
		if(errors.size() > 0){
			return;
		}
		try (PrintWriter out = new PrintWriter(output)){
			for(String s : outText) out.println(s);
		} catch (FileNotFoundException e) {
			errors.add("Cannot create output file");
		}
		
		
		
	}
	
//	public static void main(String[] args) {
//		ArrayList<String> errors = new ArrayList<>();
//		assemble(new File("in.pasm"), new File("out.pexe"), errors);
//	}
}
