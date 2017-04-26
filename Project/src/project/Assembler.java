package project;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Assembler {

	public static void assemble(File input, File output, ArrayList<String> errors){
		ArrayList<String> code = new ArrayList<String>();
		ArrayList<String> data = new ArrayList<String>();
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
		}
		
		
		
	}
	
	/*public static void main(String[] args) {
		ArrayList<String> errors = new ArrayList<>();
		assemble(new File("in.pasm"), new File("out.pexe"), errors);
	}*/
}
