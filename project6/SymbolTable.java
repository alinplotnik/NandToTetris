import java.util.HashMap;

public class SymbolTable {
    private HashMap<String, Integer> map;
    
    public SymbolTable(){
        map = new HashMap<>();

        for(int i=0; i<16 ; i++){
            map.put("R" + i, i);
        }
        map.put("SCREEN",16384);
        map.put("KBD",24576);
        map.put("SP", 0);
        map.put("LCL", 1);
        map.put("ARG", 2);
        map.put("THIS", 3);
        map.put("THAT", 4);
        map.put("LOOP", 4);
        map.put("STOP", 18);
        map.put("i", 16);
        map.put("sum", 17);
    }

    public void addEntry(String symbol, int address) {
        map.put(symbol, address);
    }

    // Optional: Method to check if the symbol exists
    public boolean contains(String symbol) {
        return map.containsKey(symbol);
    }

    // Optional: Method to get the address of a symbol
    public int getAddress(String symbol) {
        return map.getOrDefault(symbol, -1);  //return -1 if the symbol is missing
    }


    


}