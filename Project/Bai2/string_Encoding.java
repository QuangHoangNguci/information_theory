package Bai2;
import java.util.*;

public class string_Encoding
{
    static void fano_Shannon(List<Symbol> symbols, int beg, int end) {
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

        fano_Shannon(symbols, beg, y - 1);
        fano_Shannon(symbols, y, end);
    }

    static void huffman_Encoding(List<Symbol> symbols) {
        // Tạo hàng đợi ưu tiên từ danh sách các ký tự
        PriorityQueue<Symbol> pq = new PriorityQueue<>((a, b) -> Integer.compare(a.frequency, b.frequency));
        for (Symbol symbol : symbols) {
            pq.offer(symbol);
        }

        // Xây dựng cây Huffman bằng cách gộp các nút có tần số nhỏ nhất
        while (pq.size() > 1) {
            Symbol left = pq.poll();
            Symbol right = pq.poll();
            Symbol merged = new Symbol('\0', left.frequency + right.frequency);
            merged.code = "";
            merged.left = left;
            merged.right = right;
            pq.offer(merged);
        }

        // Gán mã hóa Huffman cho từng ký tự
        if (!pq.isEmpty()) {
            assignHuffmanCodes(pq.peek(), "");
        }
    }

    static void assignHuffmanCodes(Symbol symbol, String code) {
        if (symbol.left != null) {
            assignHuffmanCodes(symbol.left, code + "0");
        }
        if (symbol.right != null) {
            assignHuffmanCodes(symbol.right, code + "1");
        }
        if (symbol.left == null && symbol.right == null) {
            symbol.code = code;
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Nhập chuỗi: ");
        String input_String = sc.nextLine();

        Map<Character, Integer> frequency_table = new HashMap<>();
        for (char c : input_String.toCharArray()) {
            frequency_table.put(c, frequency_table.getOrDefault(c, 0) + 1);
        }

        List<Symbol> symbol_List = new ArrayList<>();
        for (Map.Entry<Character, Integer> entry : frequency_table.entrySet()) {
            symbol_List.add(new Symbol(entry.getKey(), entry.getValue()));
        }

        // Sắp xếp symbol_List theo tần số giảm dần
        Collections.sort(symbol_List, (a, b) -> Integer.compare(b.frequency, a.frequency));

        // Áp dụng mã hoá Shannon-Fano cho symbol_List
        fano_Shannon(symbol_List, 0, symbol_List.size() - 1);

        // In ký tự, tần số và mã hóa theo Shannon-Fano
        System.out.println("Ký tự, tần số và mã hóa theo Shannon-Fano:");
        for (Symbol symbol : symbol_List) {
            System.out.println(symbol.c + " : " + symbol.frequency + " : " + symbol.code);
        }

        // Áp dụng mã hoá Huffman cho symbol_List
        huffman_Encoding(symbol_List);

        // In ký tự, tần số và mã hóa theo Huffman
        System.out.println("Ký tự, tần số và mã hóa theo Huffman:");
        for (Symbol symbol : symbol_List) {
            System.out.println(symbol.c + " : " + symbol.frequency + " : " + symbol.code);
        }

        sc.close();
    }


}
