package project;

import java.util.Map;
import java.util.TreeMap;

public class MachineModel{
	public final Map<Integer, Instruction> IMAP = new TreeMap<Integer, Instruction>();

	private CPU cpu = new CPU();
	private Memory memory = new Memory();
	private HaltCallback callback;

	public MachineModel(HaltCallback callback){
		this.callback = callback;
		
		//INSTRUCTION MAP entry for "NOP"
		IMAP.put(0x0, (arg, level) ->{
			cpu.incrPC();
		});
		
		//INSTRUCTION MAP entry for "LOD"
		IMAP.put(0x1, (arg, level) -> {
			if(level < 0 || level > 2) {
				throw new IllegalArgumentException(
					"Illegal indirection level in LOD instruction");
			}
			if(level > 0) {
				
			} else {
				cpu.setAccum(arg);
			}
			cpu.incrPC();
		});
		
		//INSTRUCTION MAP entry for "STO"
		IMAP.put(0x2, (arg, level) ->{
			//TODO
		});
		
		//INSTRUCTION MAP entry for "ADD"
		IMAP.put(0x3, (arg, level) -> {
			if(level < 0 || level > 2) {
				throw new IllegalArgumentException(
					"Illegal indirection level in ADD instruction");
			}
			if(level > 0) {
				IMAP.get(0x3).execute(memory.getData(cpu.getMemBase()+arg), level-1);
			} else {
				cpu.setAccum(cpu.getAccum() + arg);
				cpu.incrPC();
			}
		});
		//INSTRUCTION MAP entry for "SUB"
		IMAP.put(0x4, (arg, level) -> {
			if(level < 0 || level > 2) {
				throw new IllegalArgumentException(
					"Illegal indirection level in SUB instruction");
			}
			if(level > 0) {
				IMAP.get(0x4).execute(memory.getData(cpu.getMemBase()-arg), level-1);
			} else {
				cpu.setAccum(cpu.getAccum() - arg);
				cpu.incrPC();
			}
		});
		//INSTRUCTION MAP entry for "MUL"
		IMAP.put(0x5, (arg, level) -> {
			if(level < 0 || level > 2) {
				throw new IllegalArgumentException(
					"Illegal indirection level in MUL instruction");
			}
			if(level > 0) {
				IMAP.get(0x5).execute(memory.getData(cpu.getMemBase()*arg), level-1);
			} else {
				cpu.setAccum(cpu.getAccum() * arg);
				cpu.incrPC();
			}
		});
		//INSTRUCTION MAP entry for "DIV"
		IMAP.put(0x6, (arg, level) -> {
			if(arg == 0){
				throw new DivideByZeroException();
			}
			if(level < 0 || level > 2) {
				throw new IllegalArgumentException(
					"Illegal indirection level in MUL instruction");
			}
			if(level > 0) {
				IMAP.get(0x6).execute(memory.getData(cpu.getMemBase()*arg), level-1);
			} else {
				cpu.setAccum(cpu.getAccum() * arg);
				cpu.incrPC();
			}
		});
		
		//INSTRUCTION MAP entry for "AND"
		IMAP.put(0x7, (arg, level) ->{
			//TODO
		});
		
		//INSTRUCTION MAP entry for "NOT"
		IMAP.put(0x8, (arg, level) ->{
			//TODO
		});
		
		//INSTRUCTION MAP entry for "CMPL"
		IMAP.put(0x9, (arg, level) ->{
			//TODO
		});
		
		//INSTRUCTION MAP entry for "CMPZ"
		IMAP.put(0xA, (arg, level) ->{
			//TODO
		});
		
		//INSTRUCTION MAP entry for "JMPZ"
		IMAP.put(0xB, (arg, level) ->{
			//TODO
		});
		
		//INSTRUCTION MAP entry for "HALT"
		IMAP.put(0xF, (arg, level) ->{
			callback.halt();
		});
		
		
		
		
	}
	public MachineModel(){
		this(() -> System.exit(0));
	}
	public int getAccum() {
		return cpu.getAccum();
	}
	public int getPCounter() {
		return cpu.getPCounter();
	}
	public int getMemBase() {
		return cpu.getMemBase();
	}
	public void setAccum(int accum) {
		cpu.setAccum(accum);
	}
	public void setPCounter(int pCounter) {
		cpu.setPCounter(pCounter);
	}
	public void setMemBase(int memBase) {
		cpu.setMemBase(memBase);
	}
	public void incrPC() {
		cpu.incrPC();
	}
	public int getData(int index) {
		return memory.getData(index);
	}
	public void setData(int index, int value) {
		memory.setData(index, value);
	}
	
	

}