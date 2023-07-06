package Bai2;

import java.util.*;

public class string_Encoding
{
    /*
    // Mã hoá Huffman
    public static Map<Character, String> huffman_Encode(String input) {
        // Tạo bảng tần số
        Map<Character, Integer> frequency_Table = new HashMap<>();
        for (int i = 0; i < input.length(); i++) {
            // Kiểm tra xem ký tự đã có trong bảng tần số chưa
            // Nếu đã có, tăng giá trị tần số lên 1
            // Nếu chưa có, đặt giá trị tần số là 1
            char c = input.charAt(i);
            frequency_Table.put(c, frequency_Table.getOrDefault(c, 0) + 1);
        }

        // Xây dựng cây Huffman
        Node root = build_Huffman_Tree(frequency_Table);

        // Tạo bảng mã huffman
        Map<Character, String> huffman_Table = new HashMap<>();
        build_Huffman_Table(root, "", huffman_Table);

        return huffman_Table;
    }

    //Xây dựng cây huffman từ bảng tần số
    private static Node build_Huffman_Tree(Map<Character, Integer> frequency_Table) {
        //Tạo hàng đợi ưu tiên, xắp xếp theo tần số theo giá trị tăng dần
        PriorityQueue<Node> priority_Queue = new PriorityQueue<>((a, b) -> a.frequency - b.frequency);

        // Tạo nút lá cho mỗi ký tự và thêm vào hàng đợi ưu tiên
        for (char c : frequency_Table.keySet()) {
            int frequency = frequency_Table.get(c);

            // Tạo 1 node lá với giá trị tần số tương ứng
            Node leaf_Node = new Node(c, frequency);

            // Thêm node lá vào hàng đợi ưu tiên
            priority_Queue.offer(leaf_Node);
        }

        // Xây dựng cây Huffman
        while (priority_Queue.size() > 1) {
            // Lấy 2 node lá có tần số thấp nhất
            Node left = priority_Queue.poll();
            Node right = priority_Queue.poll();

            // Tạo 1 node cha mới với ký tự trống và tần số là tổng của 2 node con
            Node parent = new Node('\0', left.frequency + right.frequency);

            // Gán 'left' và 'right' là con trái và con phải của parent vừa tạo
            parent.left = left;
            parent.right = right;

            // thêm parent vào priority queue
            priority_Queue.offer(parent);
        }
        // Trả về nút gốc của cây Huffman
        return priority_Queue.poll();
    }

    // Xây dựng bảng mã Huffman từ cây Huffman
    private static void build_Huffman_Table(Node root, String code, Map<Character, String> huffman_Table)
    {
        if (root.isLeaf()) {
            // Nếu là node lá: ghi mã huffman của node lá voà bảng mã
            huffman_Table.put(root.character, code);
        } else {

            // Gọi đệ quy cho cây con bên trái, thêm "0" vào mã Huffman hiện tại
            build_Huffman_Table(root.left, code + "0", huffman_Table);

            // Gọi đệ quy cho cây con bên phải, thêm "1" vào mã Huffman hiện tại
            build_Huffman_Table(root.right, code + "1", huffman_Table);
        }

    }
     */

    // Mã hoá Shannon-Fano
    public static Map<Character, String> shannon_Fano_Encode(String input) {
        // Tạo bảng tần số
        Map<Character, Integer> frequency_Table = new HashMap<>();
        for (int i = 0; i < input.length(); i++) {
            // Kiểm tra xem ký tự đã có trong bảng tần số chưa
            // Nếu đã có, tăng giá trị tần số lên 1
            // Nếu chưa có, đặt giá trị tần số là 1
            char c = input.charAt(i);
            frequency_Table.put(c, frequency_Table.getOrDefault(c, 0) + 1);
        }

        // Sắp xếp bảng tần số theo thứ tự giảm dần
        List<Map.Entry<Character, Integer>> sorted_Frequency_List = new ArrayList<>(frequency_Table.entrySet());
        sorted_Frequency_List.sort((a, b) -> b.getValue() - a.getValue()) ;

        // Xây dựng cây Shannon Fano
        Node root = build_shannon_Fano_Tree(sorted_Frequency_List, 0, sorted_Frequency_List.size() - 1);

        // Tạo bảng mã Shannon Fano
        Map<Character, String> shannon_Fano_Table = new HashMap<>();
        build_Shannon_Fano_Table(root, "", shannon_Fano_Table);

        return shannon_Fano_Table;
    }

    // Xây dựng cây Shannon-Fano từ bảng tần số đã xắp xếp
    private static Node build_shannon_Fano_Tree(List<Map.Entry<Character, Integer>> sorted_Frequency_List, int start, int end)
    {
        if (start > end){
            return null;
        }
        // TH chỉ có 1 phần tử trong danh sách
        if (start == end){
            // Tạo node lá với ký tự và tần số tương ứng
            return new Node(sorted_Frequency_List.get(start).getKey(), sorted_Frequency_List.get(start).getValue());

        }

        // Tính tổng tần số của các phần tử trong danh sách
        int total_Frequency =0;
        for (int i = start; i<= end; i++)
        {
            total_Frequency += sorted_Frequency_List.get(i).getValue();
        }

        // Tìm chỉ số mid để chia danh sách thành2 phần sao cho hiệu tần số gần nhau nhất
        int mid = start;
        int current_Frequency = sorted_Frequency_List.get(mid).getValue();
        int diff = Math.abs(total_Frequency - 2 * current_Frequency );

        while (mid < end && Math.abs(total_Frequency - 2 * current_Frequency) <= diff)
        {
            mid++;
            current_Frequency += sorted_Frequency_List.get(mid).getValue();
            diff = Math.abs(total_Frequency - 2* current_Frequency);
        }

        // Xây dựng cây Shannon-Fano từ 2 phần của danh sách
        Node root = new Node('\0', total_Frequency);
        root.left = build_shannon_Fano_Tree(sorted_Frequency_List, start, mid);
        root.right = build_shannon_Fano_Tree(sorted_Frequency_List, mid + 1, end);

        return root;
    }

    private static void build_Shannon_Fano_Table(Node root, String code, Map<Character, String> shannon_Fano_Table) {
        // Trường hợp cơ sở: Nếu đến nút lá, thêm cặp ký tự và mã vào bảng mã
        if (root.isLeaf()) {
            shannon_Fano_Table.put(root.character, code);
        } else {
            // Xây dựng bảng mã cho cây con bên trái với thêm bit '0' vào mã hiện tại
            build_Shannon_Fano_Table(root.left, code + "0", shannon_Fano_Table);

            // Xây dựng bảng mã cho cây con bên phải với thêm bit '1' vào mã hiện tại
            build_Shannon_Fano_Table(root.right, code + "1", shannon_Fano_Table);
        }
    }

    // Tính hiệu suất mã hóa (compression ratio)
    public static double cal_Compression_Ratio(String input, Map<Character, String> encoding_Table) {
        int original_Bits = input.length() * 16; // Số bit ban đầu cho mỗi ký tự là 16
        int encoded_Bits = 0;

        for (char c : input.toCharArray()) {
            String encoded_Code = encoding_Table.get(c);
            encoded_Bits += encoded_Code.length(); // Đếm số bit đã mã hóa cho ký tự hiện tại
        }

        return (double) original_Bits / encoded_Bits;
    }

    // Tính dư thừa (redundancy)
    public static double cal_Redundancy(Map<Character, String> encoding_Table) {
        int total_Bits = 0;

        for (String code : encoding_Table.values()) {
            total_Bits += code.length(); // Đếm tổng số bit đã mã hóa cho tất cả các ký tự
        }

        int min_Bits = encoding_Table.size() - 1; // Số bit tối thiểu cần thiết để biểu diễn các ký tự khác nhau
        return (double) (total_Bits - min_Bits) / total_Bits;
    }

    // test
        public static void main(String[] args) {
            Scanner sc = new Scanner(System.in);
            System.out.print("Nhập chuỗi ký tự không dấu: ");
            String input = sc.nextLine();
            sc.close();

            /*
            // Mã hoá huffman
            Map<Character, String> huffman_Table = huffman_Encode(input);
            System.out.println("Bảng mã huffman:");
            for (Map.Entry<Character, String> entry : huffman_Table.entrySet()) {
                char key = entry.getKey();
                String value = entry.getValue();
                System.out.println(key + ":" + value);
            }

             */

            // Mã hóa Shannon-Fano
            Map<Character, String> shannon_Fano_Table = shannon_Fano_Encode(input);
            System.out.println("Bảng mã Shannon-Fano:");
            for (Map.Entry<Character, String> entry : shannon_Fano_Table.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }

        }


}
