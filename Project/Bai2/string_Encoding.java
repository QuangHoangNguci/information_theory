package Bai2;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;

public class string_Encoding
{
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

    // test
        public static void main(String[] args) {
            Scanner sc = new Scanner(System.in);
            System.out.print("Nhập chuỗi ký tự không dấu: ");
            String input = sc.nextLine();
            sc.close();

            // Mã hoá huffman
            Map<Character, String> huffman_Table = huffman_Encode(input);
            System.out.println("Bảng mã huffman:");
            for (Map.Entry<Character, String> entry : huffman_Table.entrySet()) {
                char key = entry.getKey();
                String value = entry.getValue();
                System.out.println(key + ":" + value);
            }

        }
}
