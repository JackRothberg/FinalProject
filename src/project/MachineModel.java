package project;

import java.util.Map;

import java.util.TreeMap;

import projectview.States;

public class MachineModel {
	public final Map<Integer, Instruction> INSTRUCTIONS = new TreeMap<Integer, Instruction>();
	private CPU cpu = new CPU();
	private Memory memory = new Memory();
	private HaltCallback callback;
	private boolean withGUI;
	private Job currentJob;
	Job[] jobs = new Job[2];
	public MachineModel(boolean param, HaltCallback callbackValue) {
		withGUI = param;
		callback = callbackValue;
		//INSTRUCTION_MAP entry for "NOP"
		INSTRUCTIONS.put(0x0, arg -> {
			cpu.incrementIP(1);
		});
		//INSTRUCTION_MAP entry for "LODI"
		INSTRUCTIONS.put(0x1, arg -> {
			cpu.accumulator = arg;
			cpu.incrementIP(1);
		});
		//INSTRUCTION_MAP entry for "LOD"
		INSTRUCTIONS.put(0x2, arg -> {
			int arg1 = memory.getData(cpu.memoryBase + arg);
			cpu.accumulator = arg1;
			cpu.incrementIP(1);
		});
		//INSTRUCTION_MAP entry for "LODN"
		INSTRUCTIONS.put(0x3, arg -> {
			int arg1 = memory.getData(cpu.memoryBase+arg);
            int arg2 = memory.getData(cpu.memoryBase+arg1);
            cpu.accumulator = arg2;
            cpu.incrementIP(1);
		});
		//INSTRUCTION_MAP entry for "STO"
		INSTRUCTIONS.put(0x4, arg -> {
			memory.setData(cpu.memoryBase + arg, cpu.accumulator);
			cpu.incrementIP(1);
		});
		//INSTRUCTION_MAP entry for "STON"
		INSTRUCTIONS.put(0x5, arg -> {
			int arg1 = memory.getData(cpu.memoryBase + arg);
			memory.setData(cpu.memoryBase + arg1, cpu.accumulator);
			cpu.incrementIP(1);
		});
		//INSTRUCTION_MAP entry for "JMPR"
		INSTRUCTIONS.put(0x6, arg -> {
			cpu.incrementIP(arg);
		});
		//INSTRUCTION_MAP entry for "JUMP"
		INSTRUCTIONS.put(0x7, arg -> {
			int val = memory.getData(cpu.memoryBase + arg);
			cpu.incrementIP(val);
		});
		//INSTRUCTION_MAP entry for "JUMPI"
		INSTRUCTIONS.put(0x8, arg -> {
			cpu.instructionPointer = arg;
		});
		//INSTRUCTION_MAP entry for "JMPZR"
		INSTRUCTIONS.put(0x9, arg -> {
			if(cpu.accumulator == 0) {
				cpu.incrementIP(arg);
			}
			else {
				cpu.incrementIP(1);
			}
		});
		//INSTRUCTION_MAP entry for "JMPZ"
		INSTRUCTIONS.put(0xA, arg -> {
			if(cpu.accumulator == 0) {
				int val = memory.getData(cpu.memoryBase + arg);
				cpu.incrementIP(val);
			}
			else {
				cpu.incrementIP(1);
			}
		});
		//INSTRUCTION_MAP entry for "JMPZI"
		INSTRUCTIONS.put(0xB, arg -> {
			if(cpu.accumulator == 0) {
				cpu.instructionPointer = arg;
			}
			else {
				cpu.incrementIP(1);
			}
		});
		 //INSTRUCTION_MAP entry for "ADDI"
        INSTRUCTIONS.put(0xC, arg -> {
            cpu.accumulator += arg;
            cpu.incrementIP(1);
        });

        //INSTRUCTION_MAP entry for "ADD"
        INSTRUCTIONS.put(0xD, arg -> {
            int arg1 = memory.getData(cpu.memoryBase+arg);
            cpu.accumulator += arg1;
            cpu.incrementIP(1);
        });

        //INSTRUCTION_MAP entry for "ADDN"
        INSTRUCTIONS.put(0xE, arg -> {
            int arg1 = memory.getData(cpu.memoryBase+arg);
            int arg2 = memory.getData(cpu.memoryBase+arg1);
            cpu.accumulator += arg2;
            cpu.incrementIP(1);
        });
      //INSTRUCTION_MAP entry for "SUBI"
        INSTRUCTIONS.put(0xF, arg -> {
        	cpu.accumulator -= arg;
            cpu.incrementIP(1);
        });
      //INSTRUCTION_MAP entry for "SUB"
        INSTRUCTIONS.put(0x10, arg -> {
        	int arg1 = memory.getData(cpu.memoryBase+arg);
            cpu.accumulator -= arg1;
            cpu.incrementIP(1);
        });
        //INSTRUCTION_MAP entry for "SUBN"
        INSTRUCTIONS.put(0x11, arg -> {
        	int arg1 = memory.getData(cpu.memoryBase+arg);
            int arg2 = memory.getData(cpu.memoryBase+arg1);
            cpu.accumulator -= arg2;
            cpu.incrementIP(1);
        });
      //INSTRUCTION_MAP entry for "MULI"
        INSTRUCTIONS.put(0x12, arg -> {
            cpu.accumulator *= arg;
            cpu.incrementIP(1);
        });
      //INSTRUCTION_MAP entry for "MUL"
        INSTRUCTIONS.put(0x13, arg -> {
            int arg1 = memory.getData(cpu.memoryBase+arg);
            cpu.accumulator *= arg1;
            cpu.incrementIP(1);
        });
      //INSTRUCTION_MAP entry for "MULN"
        INSTRUCTIONS.put(0x14, arg -> {
            int arg1 = memory.getData(cpu.memoryBase+arg);
            int arg2 = memory.getData(cpu.memoryBase+arg1);
            cpu.accumulator *= arg2;
            cpu.incrementIP(1);
        });
      //INSTRUCTION_MAP entry for "DIVI"
        INSTRUCTIONS.put(0x15, arg -> {
        	if(arg == 0) {
        		throw new DivideByZeroException("Cannot divide by zero");
        	}
        	else {
        		 cpu.accumulator /= arg;
                 cpu.incrementIP(1);
        	}
        });
      //INSTRUCTION_MAP entry for "DIV"
        INSTRUCTIONS.put(0x16, arg -> {
        	int arg1 = memory.getData(cpu.memoryBase+arg);
        	if(arg1 == 0) {
        		throw new DivideByZeroException("Cannot divide by zero");
        	}
        	else {
                cpu.accumulator /= arg1;
                cpu.incrementIP(1);
        	}
        });
      //INSTRUCTION_MAP entry for "DIVN"
        INSTRUCTIONS.put(0x17, arg -> {
        	int arg1 = memory.getData(cpu.memoryBase+arg);
            int arg2 = memory.getData(cpu.memoryBase+arg1);
        	if(arg2 == 0) {
        		throw new DivideByZeroException("Cannot divide by zero");
        	}
        	else {
                cpu.accumulator /= arg2;
                cpu.incrementIP(1);
        	}
        });
      //INSTRUCTION_MAP entry for "ANDI"
        INSTRUCTIONS.put(0x18, arg -> {
        	if(cpu.accumulator != 0 && arg != 0) {
        		cpu.accumulator = 1;
        		cpu.incrementIP(1);
        	}
        	else {
        		cpu.accumulator = 0;
        		cpu.incrementIP(1);
        	}
        });
      //INSTRUCTION_MAP entry for "AND"
        INSTRUCTIONS.put(0x19, arg -> {
        	int val = memory.getData(cpu.memoryBase + arg);
        	if(cpu.accumulator != 0 && val != 0) {
        		cpu.accumulator = 1;
        		cpu.incrementIP(1);
        	}
        	else {
        		cpu.accumulator = 0;
        		cpu.incrementIP(1);
        	}
        });
        //INSTRUCTION_MAP entry for "NOT"
        INSTRUCTIONS.put(0x1A, arg -> {
        	if(cpu.accumulator != 0) {
        		cpu.accumulator = 0;
        	}
        	else if(cpu.accumulator == 0) {
        		cpu.accumulator = 1;
        	}
        	cpu.incrementIP(1);
        });
        //INSTRUCTION_MAP entry for "CMPL"
        INSTRUCTIONS.put(0x1B, arg -> {
        	int val = memory.getData(cpu.memoryBase + arg);
        	if(val < 0) {
        		cpu.accumulator = 1;
        	}
        	else {
        		cpu.accumulator = 0;
        	}
        	cpu.incrementIP(1);
        });
      //INSTRUCTION_MAP entry for "CMPZ"
        INSTRUCTIONS.put(0x1C, arg -> {
        	int val = memory.getData(cpu.memoryBase + arg);
        	if(val == 0) {
        		cpu.accumulator = 1;
        	}
        	else {
        		cpu.accumulator =0;
        	}
        	cpu.incrementIP(1);
        });
      //INSTRUCTION_MAP entry for "HALT"
        INSTRUCTIONS.put(0x1F, arg -> {
        	callback.halt();			
        });
      //INSTRUCTION_MAP entry for "JUMPN"
        INSTRUCTIONS.put(0x1D, arg -> {
        	int arg1 = memory.getData(cpu.memoryBase+arg);
        	cpu.instructionPointer = currentJob.getStartcodeIndex() + arg1;
        });
        jobs[0] = new Job();
        jobs[1] = new Job();
        currentJob = jobs[0];
        jobs[0].setStartcodeIndex(0);
        jobs[0].setStartmemoryIndex(0);
        jobs[1].setStartcodeIndex(Memory.CODE_MAX/4);
        jobs[1].setStartmemoryIndex(Memory.DATA_SIZE/2);
        jobs[0].setCurrentState(States.NOTHING_LOADED);
        jobs[1].setCurrentState(States.NOTHING_LOADED);
        
	}
	
	public int getChangedIndex() {
		return memory.getChangedIndex();
	}
	
	public MachineModel() {
		this(false, null);
	}
	
	public int getData(int index) {
		return memory.getData(index);
	}
	public int[] getData() {
		return memory.getData();
	}
	public void setData(int index, int value) {
		memory.setData(index, value);
	}
	public int getAccumulator() {
		return cpu.accumulator;
	}
	public void setAccumulator(int val) {
		cpu.accumulator = val;
	}
	public int getInstructionPointer() {
		return cpu.instructionPointer;
	}
	public void setInstructionPointer(int val) {
		cpu.instructionPointer = val;
	}
	public int getMemoryBase() {
		return cpu.memoryBase;
	}
	public void setMemoryBase(int val) {
		cpu.memoryBase = val;
	}
	public Instruction get(int index) {
		 return INSTRUCTIONS.get(index);
	 }
	public int[] getCode() {
		return memory.getCode();
	}
	
	public int getOp(int i) {
		return memory.getOp(i);
	}
	
	public int getArg(int i) {
		return memory.getArg(i);
	}
	
	public void setCode(int index, int op, int arg) {
		memory.setCode(index, op, arg);
	}
	
	public Job getCurrentJob() {
		return currentJob;
	}
	
	public States getCurrentState() {
		return currentJob.getCurrentState();
	}

	public void setCurrentState(States currentState) {
		currentJob.setCurrentState(currentState);
	}
	public void clearJob() {
		memory.clearData(currentJob.getStartmemoryIndex(), currentJob.getStartmemoryIndex() + memory.DATA_SIZE/2);
		memory.clearCode(currentJob.getStartcodeIndex(), currentJob.getStartcodeIndex() + currentJob.getCodeSize());
		cpu.accumulator = 0;
		cpu.instructionPointer = currentJob.getStartcodeIndex();
		currentJob.reset();
	}
	public void step() {
		try {
			int ip = cpu.instructionPointer;
			if(ip >= currentJob.getStartcodeIndex() || ip < currentJob.getStartcodeIndex() + currentJob.getCodeSize()) {
				throw new CodeAccessException("IP not between StartcodeIndex and StartcodeIndex + CodeSize");
			}
			int opcode = getOp(ip);
			int arg = getArg(ip);
			get(opcode).execute(arg);
		}
		catch(Exception e) {
			callback.halt();
			throw e;
		}
	}
	public void setJob(int i) {
		if(i != 0 || i != 1) {
			throw new IllegalArgumentException("Parameter must be 0 or 1");
		}
		else {
			currentJob.setCurrentAcc(cpu.accumulator);
			currentJob.setCurrentIP(cpu.instructionPointer);
			currentJob = jobs[i];
			cpu.accumulator = currentJob.getCurrentIP();
			cpu.instructionPointer = currentJob.getCurrentIP();
			cpu.memoryBase = currentJob.getStartmemoryIndex();
		}
	}
	public String getHex(int i) {
		return memory.getHex(i);
	}
	
	public String getDecimal(int i) {
		return memory.getDecimal(i);
	}
	private class CPU{
		private int accumulator;
		private int instructionPointer;
		private int memoryBase;
		public void incrementIP(int val){
			instructionPointer = instructionPointer + val;
		}
	}
}