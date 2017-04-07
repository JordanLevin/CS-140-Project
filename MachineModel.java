package project;

public class MachineModel{
	public final Map IMAP = new TreeMap();

	private CPU cpu = new CPU();
	private Memory memory = new Memory();
	private HaltCallback callback;

	public MachineModel(){
		this(() -> System.exit(0));
	}

}