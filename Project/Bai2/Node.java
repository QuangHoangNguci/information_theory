package Bai2;

public class Node {
    char character; //Ký tự
    int frequency; //Tần số
    Node left, right; // Các node con trái, phải

    public Node(char character, int frequency) {
        this.character = character;
        this.frequency = frequency;
    }

    // Kiểm tra xem nút hiện tại có phải là lá hay không
    public boolean isLeaf() {
        // Nếu cả hai con trái và con phải của nút đều là null
        if (left == null && right == null) {
            // Nút là lá
            return true;
        } else {
            // Nút không phải là lá
            return false;
        }
    }

}
