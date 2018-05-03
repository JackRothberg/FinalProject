package project;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class FullAssembler implements Assembler {

	@Override
	public int assemble(String inputFileName, String outputFileName, StringBuilder error) {
		ArrayList<String> codeLines = new ArrayList<>();
		ArrayList<String> dataLines = new ArrayList<>();
		boolean bool = false;
		boolean blankFound = false;
		int lineBlankFound = 0;
		int lineNumber = 0;
		int numData = 0;
		try(Scanner sc = new Scanner(new File(inputFileName))){
			while(sc.hasNext()) {
				String line = sc.nextLine();
				lineNumber++;
				//System.out.println(line);
				if(line.trim().length() == 0) {
					if(!blankFound) {
						blankFound = true;
						lineBlankFound = lineNumber;
					}
					continue;
				}
				if(line.length() != 0 && (line.startsWith(" ") || line.startsWith("\t"))) {
					error.append("Error at line "+lineNumber+" Illegal White Space");
				}
				if(line.trim().length() != 0 && blankFound) {
					error.append("Error at line "+lineBlankFound+" Illegal Blank Line");
				}
				if(line.trim().toUpperCase().equals("DATA")) {
					bool = true;
					if(!(line == "DATA")) {
						error.append("Error at line "+lineNumber+" invalid Data delimiter");
					}
					if(numData > 0) {
						error.append("Error at line "+lineNumber+" Duplicate Data delimiter");
					}
					numData++;
					continue;
				}
				//System.out.println(line);
				if(bool == false) {
					codeLines.add(line.trim());
				}
				else {
					dataLines.add(line.trim());
				}
				System.out.println(codeLines);
				System.out.println(dataLines);
			}
		}
		catch(FileNotFoundException e) {
			System.out.println("Couldn't find "+inputFileName);
		}
		
		
		return 0;
	}
	public static void main(String[] args) {
		StringBuilder Hi = new StringBuilder();
		FullAssembler asem = new FullAssembler();
		asem.assemble("21e.pasm", "Hi", Hi);
	}
}