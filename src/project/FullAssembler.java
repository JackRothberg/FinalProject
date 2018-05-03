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
		int lineNumber = 0;
		int numData = 0;
		try(Scanner sc = new Scanner(new File(inputFileName))){
			while(sc.hasNext()) {
				String line = sc.nextLine();
				//System.out.println(line);
				if(line == "DATA") {
					bool = true;
					numData++;
					sc.nextLine();
				}
				if(numData > 0) {
					
				}
				//System.out.println(line);
				if(bool == false) {
					codeLines.add(line);
				}
				else {
					dataLines.add(line);
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
