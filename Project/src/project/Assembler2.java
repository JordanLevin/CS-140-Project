/*package project;

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
			if(inText.get(index).trim().length() == 0 && inText.get(index+1).trim().length() > 0){
				errors.add("Error: line " + index + " is a blank line");
			}
			if(inText.get(index).charAt(0) == ' ' || inText.get(index).charAt(0) ==  '\t'){
				errors.add("Error: line " + index + " starts with white space");
			}
			if(inText.get(index).trim().toUpperCase().startsWith("--")){
				if(!firstSep){
					errors.add("Error: line " + index + " has a duplicate data separator");
				}
				else if(inText.get(index).trim().replace("-","").length()==0){
					errors.add("Error: line " + index + " has a badly formatted data separator");
				}
			}
			String[] parts = inText.get(index).trim().split("\\s+");
			if(InstructionMap.sourceCodes.contains(parts[0].toUpperCase()) && !InstructionMap.sourceCodes.contains(parts[0])){
				errors.add("Error: line " + index + " does not have the instruction mnemonic in upper case");
			}
			if(InstructionMap.noArgument.contains(parts[0])){
				if(parts.length != 1){
					errors.add("Error: line " + index + " has an illegal argument");
				}
			}
			else{
				if(parts.length == 1){
					errors.add("Error: line " + index + " is missing an argument");
				}
				else if(parts.length >= 3){
					errors.add("Error: line " + index + " has more than one argument");
				}
				else if(parts.length == 2){
					int arg = 0; 
					try {
						arg = Integer.parseInt(parts[1],16);
					} catch (NumberFormatException e) {
						errors.add("Error: line " + index 
							+ " does not have a numeric argument");
					}
					
					if(parts[0].startsWith("[")){
						if(!InstructionMap.indirectOK.contains(parts[0])){
							errors.add("Error: line " + index + " has extra opening bracket");
						}
						else{
							if(!parts[0].endsWith("]")){
								errors.add("Error: line " + index + " is missing closing bracket");
							}
							//TODO Remove the braces but record that the indirLvl is 2 as was done in the assemble code if Assembler.??????
						}
						
					}
				}
			}
			
			
		}
		
		try {
			Scanner scan = new Scanner(input);
			while(scan.hasNextLine()){
				String line = scan.nextLine();
				if(line.trim().startsWith("--")){
					while(scan.hasNextLine()){
						data.add(scan.nextLine());
					}
				}
				else{
					code.add(line);
				}
			}
			scan.close();
		} catch (FileNotFoundException e) {
			errors.add("Input file does not exist");
			return;
		}
		ArrayList<String> outText = new ArrayList<String>();
		for(String line: code){
			int indirLvl = 0;
			String[] parts = line.trim().split("\\s+");
			if(parts.length == 2){
				indirLvl = 1;
				if(parts[1].startsWith("[")){
					parts[1] = parts[1].substring(1, parts[1].length()-1);
					indirLvl = 2;
				}
			}

			if(parts[0].endsWith("I")){
				indirLvl = 0;
			}
			else if(parts[0].endsWith("A")){
				indirLvl = 3;
			}
			int opcode = InstructionMap.opcode.get(parts[0]);
			
			if(parts.length == 1){
				outText.add(Integer.toHexString(opcode).toUpperCase() + " 0 0");
			}
			else if(parts.length == 2){
				outText.add(Integer.toHexString(opcode).toUpperCase() + " " + indirLvl + " " + parts[1]);
				System.out.println(outText.get(outText.size()-1));
			}
		}
		outText.add("-1");
		outText.addAll(data);
		if(errors.size() > 0)
			return;
		try (PrintWriter out = new PrintWriter(output)){
			for(String s : outText) out.println(s);
		} catch (FileNotFoundException e) {
			errors.add("Cannot create output file");
		}
		
		
		
	}

}
*/

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
			if(inText.get(index).trim().length() == 0 && inText.get(index+1).trim().length() > 0){
				errors.add("Error: line " + index + " is a blank line");
			}
			if(inText.get(index).length() == 0){
				continue;
			}
			//System.out.println(inText.get(index));
			if(inText.get(index).charAt(0) == ' ' || inText.get(index).charAt(0) ==  '\t'){
				errors.add("Error: line " + index + " starts with white space");
			}
			if(inText.get(index).trim().toUpperCase().startsWith("--")){
				if(!firstSep){
					errors.add("Error: line " + index + " has a duplicate data separator");
				}
				else if(inText.get(index).trim().replace("-","").length()==0){
					firstSep = false;
					errors.add("Error: line " + index + " has a badly formatted data separator");
				}
			}
			
			if(errors.size() > 0){
				return;
			}
			
		}
		
		boolean sep = false;
		for(int index = 0;index<inText.size()-1;index++){
			if(inText.get(index).startsWith("--")){
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
			String[] parts = code.get(index).trim().split("\\s+");
			if(InstructionMap.sourceCodes.contains(parts[0].toUpperCase()) &&  !InstructionMap.sourceCodes.contains(parts[0])){
				errors.add("Error: line " + index + " does not have the instruction mnemonic in upper case");
			}
			else if(InstructionMap.noArgument.contains(parts[0]) && parts.length != 1){
				errors.add("Error: line " + index + " has an illegal argument");
			}
			else if(InstructionMap.noArgument.contains(parts[0]) && parts.length == 1){
				errors.add("Error: line " + index + " is missing an argument");
			}
			else if(parts.length > 2){
				errors.add("Error: line " + index + " has more than one argument");
			}
			if(parts.length == 2){
				indirLvl = 1;
				if(parts[1].startsWith("[")){
					if(!InstructionMap.indirectOK.contains(parts[0])){
						errors.add("Error: line " + index + "tries to use indirect mode with an instruction that cant do that");
					}
					else if(!parts[1].endsWith("]")){
						errors.add("Error: line " + index + "is missing a closing bracket");
					}
					else{
						parts[1] = parts[1].substring(1, parts[1].length()-1);
						indirLvl = 2;
					}
				}
				int arg = 0; 
				try {
					arg = Integer.parseInt(parts[1],16);
				} catch (NumberFormatException e) {
					errors.add("Error: line " + index 
						+ " does not have a numeric argument");
				}
			}
			if(parts[0].endsWith("I")){
				indirLvl = 0;
			}
			else if(parts[0].endsWith("A")){
				indirLvl = 3;
			}
			int opcode = InstructionMap.opcode.get(parts[0]);
			
			if(parts.length == 1){
				outText.add(Integer.toHexString(opcode).toUpperCase() + " 0 0");
			}
			else if(parts.length == 2){
				outText.add(Integer.toHexString(opcode).toUpperCase() + " " + indirLvl + " " + parts[1]);
				System.out.println(outText.get(outText.size()-1));
			}
		}
		
		for(int index = 0;index < data.size();index++){
			String[] parts = data.get(index).trim().split("\\s+");
			int arg = 0; 
			int arg0 = 0;
			try {
				arg = Integer.parseInt(parts[1],16);
				arg0 = Integer.parseInt(parts[0],16);
			} catch (NumberFormatException e) {
				errors.add("Error: line " + index 
					+ " does not have a numeric argument");
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
		
		//----------------------------------------------------------------------------------------------
		/*
		try {
			Scanner scan = new Scanner(input);
			while(scan.hasNextLine()){
				String line = scan.nextLine();
				if(line.trim().startsWith("--")){
					while(scan.hasNextLine()){
						data.add(scan.nextLine());
					}
				}
				else{
					code.add(line);
				}
			}
			scan.close();
		} catch (FileNotFoundException e) {
			errors.add("Input file does not exist");
			return;
		}
		ArrayList<String> outText = new ArrayList<String>();
		for(String line: code){
			int indirLvl = 0;
			String[] parts = line.trim().split("\\s+");
			if(parts.length == 2){
				indirLvl = 1;
				if(parts[1].startsWith("[")){
					parts[1] = parts[1].substring(1, parts[1].length()-1);
					indirLvl = 2;
				}
			}

			if(parts[0].endsWith("I")){
				indirLvl = 0;
			}
			else if(parts[0].endsWith("A")){
				indirLvl = 3;
			}
			int opcode = InstructionMap.opcode.get(parts[0]);
			
			if(parts.length == 1){
				outText.add(Integer.toHexString(opcode).toUpperCase() + " 0 0");
			}
			else if(parts.length == 2){
				outText.add(Integer.toHexString(opcode).toUpperCase() + " " + indirLvl + " " + parts[1]);
				System.out.println(outText.get(outText.size()-1));
			}
		}
		outText.add("-1");
		outText.addAll(data);
		
		try (PrintWriter out = new PrintWriter(output)){
			for(String s : outText) out.println(s);
		} catch (FileNotFoundException e) {
			errors.add("Cannot create output file");
		}*/
		
		
		
	}
	
	public static void main(String[] args) {
		ArrayList<String> errors = new ArrayList<>();
		assemble(new File("in.pasm"), new File("out.pexe"), errors);
	}
}
