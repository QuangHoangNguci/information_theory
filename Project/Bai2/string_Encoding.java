package Bai2;
import java.util.*;

public class string_Encoding
{
    public static double calculate_Efficiency(List<Symbol> symbols) {
        double entropy = 0;
        int total_Frequency = 0;
        double avg_Length = 0;

        for (Symbol symbol : symbols) {
            total_Frequency += symbol.frequency;
        }

        for (Symbol symbol : symbols) {
            double probability = (double) symbol.frequency / total_Frequency;
            entropy += -probability * log2(probability);
            avg_Length += probability * symbol.code.length();
        }

        return entropy / avg_Length;
    }

    static double log2(double n) {
        return Math.log(n) / Math.log(2);
    }
    static void shannon_Fano(List<Symbol> symbols, int beg, int end) {
        if (beg == end)
            return;

        int y = beg;
        int z = end;
        int sum_left = 0;
        int sum_right = 0;

        while (y <= z) {
            if (sum_left <= sum_right) {
                sum_left += symbols.get(y).frequency;
                y++;
            } else {
                sum_right += symbols.get(z).frequency;
                z--;
            }
        }

        for (int h = beg; h < y; h++) {
            symbols.get(h).code += "0";
        }
        for (int h = y; h <= end; h++) {
            symbols.get(h).code += "1";
        }

        shannon_Fano(symbols, beg, y - 1);
        shannon_Fano(symbols, y, end);
    }

    static void huffman_Encoding(List<Symbol> symbols) {
        // Tạo hàng đợi ưu tiên từ danh sách các ký tự
        PriorityQueue<Symbol> priority_Queue = new PriorityQueue<>((a, b) -> Integer.compare(a.frequency, b.frequency));
        for (Symbol symbol : symbols) {
            priority_Queue.offer(symbol);
        }

        // Xây dựng cây Huffman
        while (priority_Queue.size() > 1) {
            Symbol left = priority_Queue.poll();
            Symbol right = priority_Queue.poll();
            Symbol merged = new Symbol('\0', left.frequency + right.frequency);
            merged.code = "";
            merged.left = left;
            merged.right = right;
            priority_Queue.offer(merged);
        }

        // Gán mã hóa Huffman
        if (!priority_Queue.isEmpty()) {
            assign_Huffman_Codes(priority_Queue.peek(), "");
        }
    }

    static void assign_Huffman_Codes(Symbol symbol, String code) {
        if (symbol.left != null) {
            assign_Huffman_Codes(symbol.left, code + "0");
        }
        if (symbol.right != null) {
            assign_Huffman_Codes(symbol.right, code + "1");
        }
        if (symbol.left == null && symbol.right == null) {
            symbol.code = code;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Nhập chuỗi: ");
        String input_String = sc.nextLine();
        input_String = input_String.toLowerCase();

        // Khởi tạo bảng tần số là một đối tượng HashMap
        Map<Character, Integer> frequency_table = new HashMap<>();
        // Chuyển chuỗi đầu vào thành một mảng các ký tự
        char[] characters = input_String.toCharArray();


        for (char c : characters) {
            // Kiểm tra xem ký tự đã có trong bảng tần số chưa
            if (frequency_table.containsKey(c)) {
                // Nếu ký tự đã có trong bảng tần số, tăng giá trị tần số lên 1
                int frequency = frequency_table.get(c);
                frequency_table.put(c, frequency + 1);
            } else {
                // Nếu ký tự chưa có trong bảng tần số, thêm ký tự vào bảng và gán giá trị tần số là 1
                frequency_table.put(c, 1);
            }
        }

        List<Symbol> symbol_List = new ArrayList<>();
        for (Map.Entry<Character, Integer> entry : frequency_table.entrySet()) {
            symbol_List.add(new Symbol(entry.getKey(), entry.getValue()));
        }

        // Sắp xếp symbol_List theo tần số giảm dần
        Collections.sort(symbol_List, (a, b) -> Integer.compare(b.frequency, a.frequency));

        // áp dụng mã hoá Huffman cho symbol_List
        huffman_Encoding(symbol_List);

        // in ký tự, tần số và mã hóa theo Huffman
        System.out.println("Ký tự, tần số và mã hóa theo Huffman:");
        for (Symbol symbol : symbol_List) {
            System.out.println(symbol.c + " : " + symbol.frequency + " : " + symbol.code);
        }
        // Tạo bảng mã hóa từ danh sách các ký tự
        Map<Character, String> encoding_Table_Huffman = new HashMap<>();
        for (Symbol symbol : symbol_List) {
            encoding_Table_Huffman.put(symbol.c, symbol.code);
        }

        // Mã hoá chuỗi đầu vào bằng cách thay thế mỗi ký tự bằng mã hóa tương ứng từ bảng mã hóa
        StringBuilder encoded_String_Huffman = new StringBuilder();
        for (char c : characters) {
            String code = encoding_Table_Huffman.get(c);
            encoded_String_Huffman.append(code);
        }

        // In chuỗi đã được mã hoá
        System.out.println("Chuỗi đã được mã hoá huffman:");
        System.out.println(encoded_String_Huffman.toString());

        // Áp dụng mã hoá Shannon-Fano cho symbol_List
        shannon_Fano(symbol_List, 0, symbol_List.size() - 1);

        // In ký tự, tần số và mã hóa theo Shannon-Fano
        System.out.println("Ký tự, tần số và mã hóa theo Shannon-Fano:");
        for (Symbol symbol : symbol_List) {
            System.out.println(symbol.c + " : " + symbol.frequency + " : " + symbol.code);
        }

        // Tạo bảng mã hóa từ danh sách các ký tự
        Map<Character, String> encoding_Table_Shannon = new HashMap<>();
        for (Symbol symbol : symbol_List) {
            encoding_Table_Shannon.put(symbol.c, symbol.code);
        }

        // Mã hoá chuỗi đầu vào bằng cách thay thế mỗi ký tự bằng mã hóa tương ứng từ bảng mã hóa
        StringBuilder encoded_String_Shannom = new StringBuilder();
        for (char c : characters) {
            String code = encoding_Table_Shannon.get(c);
            encoded_String_Shannom.append(code);
        }

        // In chuỗi đã được mã hoá
        System.out.println("Chuỗi đã được mã hoá shannon-Fano:");
        System.out.println(encoded_String_Shannom.toString());

        // Tính hiệu suất mã hoá Shannon-Fano
        double efficiency = calculate_Efficiency(symbol_List);
        double redundancy = 1 - efficiency;

        System.out.println("Hiệu suất mã hoá: η = " + efficiency);
        System.out.println("Tính dư thừa: R = " + redundancy);

        sc.close();
    }
}
