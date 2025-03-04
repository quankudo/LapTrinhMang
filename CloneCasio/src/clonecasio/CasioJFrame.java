/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package clonecasio;

import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import javax.swing.SwingUtilities;

/**
 *
 * @author HP
 */
public class CasioJFrame extends javax.swing.JFrame {

    private StringBuilder text= new StringBuilder("");
    private boolean flagMode = false;
    private int flagChoose = 0;
    /**
     * Creates new form CasioJFrame
     */
    public CasioJFrame() {
        initComponents();
        tArea.setLineWrap(true);
        tArea.setWrapStyleWord(true);
        tArea.setPreferredSize(new Dimension(300, 150));
        tArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_TAB) { // Nếu nhấn phím Tab
                    e.consume(); // Ngăn JTextArea thêm ký tự Tab mặc định
                    int caretPos = tArea.getCaretPosition(); // Lấy vị trí con trỏ hiện tại
                    String text = tArea.getText();
                    tArea.setText(tArea.getText()+" ");
                    // Tìm vị trí tiếp theo của dấu ',' hoặc ':'
                    int nextPos = -1;
                    for (int i = caretPos; i < text.length()+10; i++) {
                        if (text.charAt(i) == ':') {
                            nextPos = i + 2; // Di chuyển con trỏ đến sau dấu ',' hoặc ':'
                            break;
                        }
                    }

                    // Nếu tìm thấy vị trí hợp lệ, di chuyển con trỏ đến đó
                    if (nextPos != -1 && nextPos < text.length()) {
                        tArea.setCaretPosition(nextPos);
                    }
                }
            }
        });
    }

    private void AddText(String str){
        if(flagChoose!=0){
            try {
                int vitri = tArea.getCaretPosition();
                String prev = tArea.getText(vitri-1, 1);
                tArea.replaceRange(" "+prev+str, vitri-1, vitri);
                SwingUtilities.invokeLater(() -> {
                tArea.requestFocusInWindow();
                });
            } catch (Exception e) {
                    
            }
        }
        else{
            if(text.length()==1 && text.toString().equals("0")){
            text.deleteCharAt(0);
            }
            text.append(str);
            tArea.setText(text.toString());
        }
    }
    
    public static double evaluate(StringBuilder expression) {
        ArrayList<Double> numbers = new ArrayList<>();  
        ArrayList<Character> operators = new ArrayList<>();  

        int i = 0;
        while (i < expression.length()) {
            char currentChar = expression.charAt(i);

            // Nếu là số (có thể là phần của một số nhiều chữ số)
            if (Character.isDigit(currentChar) || currentChar == '.') {
                StringBuilder number = new StringBuilder();
                // Duyệt qua số nguyên hoặc phần thập phân
                while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    number.append(expression.charAt(i));
                    i++;
                }
                i--; 
                numbers.add(Double.parseDouble(number.toString())); 
                if(operators.size()>0){
                    if(operators.get(operators.size()-1) =='*' || operators.get(operators.size()-1)=='/'){
                        numbers.add(applyOperator(operators.remove(operators.size() - 1), numbers.remove(numbers.size() - 1), numbers.remove(numbers.size() - 1)));
                    }
                }
            }
            else if (currentChar == 's' || currentChar == 'c' || currentChar == 't' || currentChar == 'f' || currentChar == 'g') {
                StringBuilder function = new StringBuilder();
                // Lấy tên hàm (sin, cos, tan, sqrt, factorial)
                while (i < expression.length() && Character.isLetter(expression.charAt(i))) {
                    function.append(expression.charAt(i));
                    i++;
                }
                i--; // Giảm i vì vòng lặp tăng quá mức
                double result = 0;
                if (function.toString().equals("sin")) {
                    i+=2; 
                    result = Math.sin(Math.toRadians(parseExpressionInsideParentheses(expression, i)));
                    i++;
                } else if (function.toString().equals("cos")) {
                    i+=2; 
                    result = Math.cos(Math.toRadians(parseExpressionInsideParentheses(expression, i)));
                    i++;
                } else if (function.toString().equals("tan")) {
                    i+=2; 
                    result = Math.tan(Math.toRadians(parseExpressionInsideParentheses(expression, i)));
                    i++;
                } else if (function.toString().equals("sqrt")) {
                    i+=2; 
                    result = Math.sqrt(parseExpressionInsideParentheses(expression, i));
                    i++;
                } else if (function.toString().equals("factorial")) {
                    i+=2; 
                    result = factorial(parseExpressionInsideParentheses(expression, i));
                    i++;
                }
                numbers.add(result); 
            }
            else if (currentChar == '+' || currentChar == '-' || currentChar == '*' || currentChar == '/') {
                
                // Xử lý toán tử khi có toán tử có độ ưu tiên cao hơn
//                while (!operators.isEmpty() && hasPrecedence(currentChar, operators.get(operators.size() - 1))) {
//                    System.out.println(operators.toString());
//                    System.out.println(numbers.toString());
//                    numbers.add(applyOperator(operators.remove(operators.size() - 1), numbers.remove(numbers.size() - 1), numbers.remove(numbers.size() - 1)));
//                }
                operators.add(currentChar); 
            }
            i++;  
        }

        while (!operators.isEmpty()) {
            System.out.println("--------------------");
            System.out.println(operators.toString());
            System.out.println(numbers.toString());
//            numbers.add(applyOperator(operators.remove(operators.size() - 1), numbers.remove(numbers.size() - 1), numbers.remove(numbers.size() - 1)));
            numbers.addFirst(applyOperator(operators.remove(0), numbers.remove(0), numbers.remove(0)));

        }

        return numbers.get(0);  
    }

    // Hàm giải quyết biểu thức trong dấu ngoặc (có thể chứa một số, toán tử, hoặc biểu thức phức tạp)
    private static double parseExpressionInsideParentheses(StringBuilder expression, int index) {
        StringBuilder number = new StringBuilder();
        while (index < expression.length() && (Character.isDigit(expression.charAt(index)) || expression.charAt(index) == '.')) {
            number.append(expression.charAt(index));
            index++;
        }

        if (number.length() == 0) {
            throw new IllegalArgumentException("Không tìm thấy giá trị trong dấu ngoặc.");
        }

        return Double.parseDouble(number.toString());
    }

    public static boolean hasPrecedence(char currentOp, char topOp) {
        if (topOp == '+' || topOp == '-') {
            return currentOp == '*' || currentOp == '/';
        }
        return false;
    }

    public static double applyOperator(char operator, double a, double b) {
        switch (operator) {
            case '+': return a + b;
            case '-': return a - b;
            case '*': return a * b;
            case '/': return a / b;
        }
        return 0;
    }

    // Hàm tính giai thừa
    public static double factorial(double n) {
        if (n == 0 || n == 1) return 1;
        double result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }
    
    public String DienTichHinhTron(double r){
        return String.format("Diện tích hình tròn: S = %.2f%n", Math.PI*r*r);
    }
    
    public String DienTichHinhCN(double dai, double rong){
        return String.format("Diện tích hình CN: S = %.2f%n", dai*rong);
    }
    
    public String DienTichHinhVuong(double a){
        return String.format("Diện tích hình vuông: S = %.2f%n", a*a);
    }
    
    public String DienTichHinhTamGiac(double a, double b, double c){
        if (a + b > c && a + c > b && b + c > a) {
            double s = (a + b + c) / 2;

            // Tính diện tích theo công thức Heron
            double area = Math.sqrt(s * (s - a) * (s - b) * (s - c));

            return String.format("Diện tích hình tam giác: S = %.2f%n", area);
        } else {
            return "Ba cạnh không tạo thành một tam giác hợp lệ.";
        }
    }
    
    public String GiaiPTBac2(double a, double b, double c){
        if (a == 0) {
            return "Không phải phương trình bậc 2.";
        } else {
            double delta = b * b - 4 * a * c;

            if (delta > 0) {
                double x1 = (-b + Math.sqrt(delta)) / (2 * a);
                double x2 = (-b - Math.sqrt(delta)) / (2 * a);
                return String.format("Phương trình có 2 nghiệm phân biệt:\nx1 = %.2f, x2 = %.2f%n", x1, x2);
            } else if (delta == 0) {
                // 1 nghiệm kép
                double x = -b / (2 * a);
                return String.format("Phương trình có nghiệm kép: x = %.2f%n", x);
            } else {
                return "Phương trình vô nghiệm.";
            }
        }
    }
    
    public void Choose1() {
        flagMode=false;
        flagChoose=1;
        if(text.length()>0)
            text.delete(0, text.length());
        text.append("Nhập bán kính R: ");
        tArea.setText("Nhập bán kính R: ");
        SwingUtilities.invokeLater(() -> {
            tArea.setCaretPosition(17);  // Đặt con trỏ sau "Nhập cạnh a: "
            tArea.requestFocusInWindow(); // Yêu cầu focus vào JTextArea
        });
    }
    
    public void Choose2() {
        flagMode=false;
        flagChoose=2;
        if(text.length()>0)
            text.delete(0, text.length());
        text.append("Nhập chiều dài: ,\nNhập chiều rộng: ");
        tArea.setText("Nhập chiều dài: ,\nNhập chiều rộng: ");
        SwingUtilities.invokeLater(() -> {
            tArea.setCaretPosition(16);  // Đặt con trỏ sau "Nhập cạnh a: "
            tArea.requestFocusInWindow(); // Yêu cầu focus vào JTextArea
        });
    }
    
    public void Choose3() {
        flagMode=false;
        flagChoose=3;
        if(text.length()>0)
            text.delete(0, text.length());
        
        text.append("Nhập cạnh a: ,\nNhập cạnh b: ,\nNhập cạnh c: ");
        tArea.setText("Nhập cạnh a: ,\nNhập cạnh b: ,\nNhập cạnh c: ");
        SwingUtilities.invokeLater(() -> {
            tArea.setCaretPosition(13);  // Đặt con trỏ sau "Nhập cạnh a: "
            tArea.requestFocusInWindow(); // Yêu cầu focus vào JTextArea
        });
    }
    
    public void Choose4() {
        flagMode=false;
        flagChoose=4;
        if(text.length()>0)
            text.delete(0, text.length());
        text.append("Nhập cạnh a: ");
        tArea.setText("Nhập cạnh a: ");
        SwingUtilities.invokeLater(() -> {
            tArea.setCaretPosition(13);  // Đặt con trỏ sau "Nhập cạnh a: "
            tArea.requestFocusInWindow(); // Yêu cầu focus vào JTextArea
        });
    }
    
    public void Choose5() {
        flagMode=false;
        flagChoose=5;
        if(text.length()>0)
            text.delete(0, text.length());
        text.append("Nhập hệ số a: ,\nNhập hệ số b: ,\nNhập hệ số c: ");
        tArea.setText("Nhập hệ số a: ,\nNhập hệ số b: ,\nNhập hệ số c: ");
        SwingUtilities.invokeLater(() -> {
            tArea.setCaretPosition(14);  // Đặt con trỏ sau "Nhập cạnh a: "
            tArea.requestFocusInWindow(); // Yêu cầu focus vào JTextArea
        });
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnSin = new javax.swing.JButton();
        btnCos = new javax.swing.JButton();
        btnTan = new javax.swing.JButton();
        btnLog = new javax.swing.JButton();
        btnLn = new javax.swing.JButton();
        btnGt = new javax.swing.JButton();
        btnBp = new javax.swing.JButton();
        btnCan = new javax.swing.JButton();
        btnPs = new javax.swing.JButton();
        btnChia = new javax.swing.JButton();
        btn7 = new javax.swing.JButton();
        btn8 = new javax.swing.JButton();
        btn9 = new javax.swing.JButton();
        btnNhan = new javax.swing.JButton();
        btn4 = new javax.swing.JButton();
        btn5 = new javax.swing.JButton();
        btn6 = new javax.swing.JButton();
        btnTru = new javax.swing.JButton();
        btn1 = new javax.swing.JButton();
        btn2 = new javax.swing.JButton();
        btn3 = new javax.swing.JButton();
        btnCong = new javax.swing.JButton();
        btnPi = new javax.swing.JButton();
        btn0 = new javax.swing.JButton();
        btnDot = new javax.swing.JButton();
        btnBang = new javax.swing.JButton();
        btnAc = new javax.swing.JButton();
        btnC = new javax.swing.JButton();
        btnOpen = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tArea = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        btnSin.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnSin.setForeground(new java.awt.Color(255, 51, 51));
        btnSin.setText("sin");
        btnSin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HandleBtnSin(evt);
            }
        });

        btnCos.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnCos.setForeground(new java.awt.Color(255, 51, 51));
        btnCos.setText("cos");
        btnCos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HandleBtnCos(evt);
            }
        });

        btnTan.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnTan.setForeground(new java.awt.Color(255, 51, 51));
        btnTan.setText("tan");
        btnTan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HandleBtnTan(evt);
            }
        });

        btnLog.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnLog.setForeground(new java.awt.Color(255, 51, 51));
        btnLog.setText("tab");
        btnLog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HandleTab(evt);
            }
        });

        btnLn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnLn.setForeground(new java.awt.Color(255, 51, 51));
        btnLn.setText("mode");
        btnLn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HandleMode(evt);
            }
        });

        btnGt.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnGt.setForeground(new java.awt.Color(255, 51, 51));
        btnGt.setText("x!");
        btnGt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HandleBtnGt(evt);
            }
        });

        btnBp.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnBp.setForeground(new java.awt.Color(255, 51, 51));
        btnBp.setText("x^2");
        btnBp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HandlePow(evt);
            }
        });

        btnCan.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnCan.setForeground(new java.awt.Color(255, 51, 51));
        btnCan.setText("sqrt");
        btnCan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HandleBtnCan(evt);
            }
        });

        btnPs.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnPs.setForeground(new java.awt.Color(255, 51, 51));
        btnPs.setText("1/x");

        btnChia.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnChia.setForeground(new java.awt.Color(255, 51, 51));
        btnChia.setText("/");
        btnChia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HandleBtnChia(evt);
            }
        });

        btn7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn7.setText("7");
        btn7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HandleBtn7(evt);
            }
        });

        btn8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn8.setText("8");
        btn8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HandleBtn8(evt);
            }
        });

        btn9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn9.setText("9");
        btn9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HandleBtn9(evt);
            }
        });

        btnNhan.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnNhan.setForeground(new java.awt.Color(255, 51, 51));
        btnNhan.setText("x");
        btnNhan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HandleBtnNhan(evt);
            }
        });

        btn4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn4.setText("4");
        btn4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HandleBtn4(evt);
            }
        });

        btn5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn5.setText("5");
        btn5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HandleBtn5(evt);
            }
        });

        btn6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn6.setText("6");
        btn6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HandleBtn6(evt);
            }
        });

        btnTru.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnTru.setForeground(new java.awt.Color(255, 51, 51));
        btnTru.setText("-");
        btnTru.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HandleBtnTru(evt);
            }
        });

        btn1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn1.setText("1");
        btn1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HandleBtn1(evt);
            }
        });

        btn2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn2.setText("2");
        btn2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HandleBtn2(evt);
            }
        });

        btn3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn3.setText("3");
        btn3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HandleBtn3(evt);
            }
        });

        btnCong.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnCong.setForeground(new java.awt.Color(255, 0, 0));
        btnCong.setText("+");
        btnCong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HandleBtnCong(evt);
            }
        });

        btnPi.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnPi.setText("PI");

        btn0.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btn0.setText("0");
        btn0.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HandleBtn0(evt);
            }
        });

        btnDot.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnDot.setText(".");
        btnDot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HandleBtnDot(evt);
            }
        });

        btnBang.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnBang.setForeground(new java.awt.Color(255, 0, 51));
        btnBang.setText("=");
        btnBang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HandleEval(evt);
            }
        });

        btnAc.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnAc.setForeground(new java.awt.Color(255, 51, 51));
        btnAc.setText("AC");
        btnAc.setName(""); // NOI18N
        btnAc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CloseAll(evt);
            }
        });

        btnC.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnC.setForeground(new java.awt.Color(255, 51, 51));
        btnC.setText("C");
        btnC.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RemoveLastItem(evt);
            }
        });

        btnOpen.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnOpen.setForeground(new java.awt.Color(255, 51, 51));
        btnOpen.setText("(");
        btnOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HandleBtnOpen(evt);
            }
        });

        btnClose.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnClose.setForeground(new java.awt.Color(255, 51, 51));
        btnClose.setText(")");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HandleBtnClose(evt);
            }
        });

        tArea.setColumns(20);
        tArea.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        tArea.setRows(5);
        jScrollPane2.setViewportView(tArea);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(btnAc, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnC, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnOpen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnSin, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCos, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnTan, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnLog, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnLn, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnGt, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBp, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCan, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPs, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnChia, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnPi, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn0, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDot, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(btn7, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btn8, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btn9, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE))
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(btn4, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(btn5, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(btn1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(btn2, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(btn3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btn6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnBang, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
                    .addComponent(btnCong, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnNhan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnTru, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addComponent(jScrollPane2)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAc, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnC, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnOpen, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSin, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCos, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTan, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLog, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLn, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGt, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBp, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCan, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPs, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnChia, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn7, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn8, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn9, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNhan, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn4, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn5, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn6, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTru, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn1, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn2, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn3, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCong, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnPi, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn0, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDot, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBang, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 6, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void CloseAll(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CloseAll
        // TODO add your handling code here:
        tArea.setText("");
        text.delete(0, text.length());
    }//GEN-LAST:event_CloseAll

    private void RemoveLastItem(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RemoveLastItem
        // TODO add your handling code here:
        text.deleteCharAt(text.length()-1);
        tArea.setText(text.toString());
    }//GEN-LAST:event_RemoveLastItem

    private void HandleBtn1(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HandleBtn1
        if(flagMode){
            Choose1();
        }
        else
            AddText("1");
    }//GEN-LAST:event_HandleBtn1

    private void HandleBtn2(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HandleBtn2
        if(flagMode){
            Choose2();
        }
        else
            AddText("2");
    }//GEN-LAST:event_HandleBtn2

    private void HandleBtn3(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HandleBtn3
        if(flagMode){
            Choose3();
        }
        else
            AddText("3");
    }//GEN-LAST:event_HandleBtn3

    private void HandleBtn4(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HandleBtn4
        if(flagMode){
            Choose4();
        }
        else
            AddText("4");
    }//GEN-LAST:event_HandleBtn4

    private void HandleBtn5(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HandleBtn5
        if(flagMode){
            Choose5();
        }
        else
            AddText("5");
    }//GEN-LAST:event_HandleBtn5

    private void HandleBtn6(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HandleBtn6
        AddText("6");
    }//GEN-LAST:event_HandleBtn6

    private void HandleBtn7(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HandleBtn7
        AddText("7");
    }//GEN-LAST:event_HandleBtn7

    private void HandleBtn8(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HandleBtn8
        AddText("8");
    }//GEN-LAST:event_HandleBtn8

    private void HandleBtn9(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HandleBtn9
        AddText("9");
    }//GEN-LAST:event_HandleBtn9

    private void HandleBtnNhan(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HandleBtnNhan
        text.append("*");
        tArea.setText(text.toString());
    }//GEN-LAST:event_HandleBtnNhan

    private void HandleBtnTru(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HandleBtnTru
        if(flagChoose!=0){
            try {
                int vitri = tArea.getCaretPosition();
                String prev = tArea.getText(vitri-1, 1);
                tArea.replaceRange(" "+prev+"-", vitri-1, vitri);
                SwingUtilities.invokeLater(() -> {
                tArea.requestFocusInWindow();
                });
            } catch (Exception e) {
                    
            }
             // Yêu cầu focus vào JTextArea
        
        }
        else{
            text.append("-");
            tArea.setText(text.toString());
        }
    }//GEN-LAST:event_HandleBtnTru

    private void HandleBtnCong(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HandleBtnCong
        text.append("+");
        tArea.setText(text.toString());
    }//GEN-LAST:event_HandleBtnCong

    private void HandleBtnDot(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HandleBtnDot
        text.append(".");
        tArea.setText(text.toString());
    }//GEN-LAST:event_HandleBtnDot

    private void HandleBtn0(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HandleBtn0
        if(flagChoose!=0){
            try {
                int vitri = tArea.getCaretPosition();
                String prev = tArea.getText(vitri-1, 1);
                tArea.replaceRange(" "+prev+"0", vitri-1, vitri);
                SwingUtilities.invokeLater(() -> {
                tArea.requestFocusInWindow();
                });
            } catch (Exception e) {
                    
            }
             // Yêu cầu focus vào JTextArea
        
        }
        else{
            text.append("0");
            tArea.setText(text.toString());
        }
    }//GEN-LAST:event_HandleBtn0

    private void HandleEval(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HandleEval
        if(flagChoose!=0){
            String text;
            if(flagChoose==1){
                text = tArea.getText().replace("Nhập bán kính R: ", "");
                double a = Double.parseDouble(text);
                tArea.setText(DienTichHinhTron(a));
            }
            else if(flagChoose==2){
                text = tArea.getText().replace("Nhập chiều dài: ", "");
                text = text.replace("\nNhập chiều rộng: ", "");
                String[] arr = text.split(",");
                tArea.setText(DienTichHinhCN(Double.parseDouble(arr[0]),Double.parseDouble(arr[1])));
            }
            else if(flagChoose==3){
                text = tArea.getText().replace("Nhập cạnh a: ", "");
                text = text.replace("\nNhập cạnh b: ", "");
                text = text.replace("\nNhập cạnh c: ", "");
                String[] arr = text.split(",");
                tArea.setText(DienTichHinhTamGiac(Double.parseDouble(arr[0]),Double.parseDouble(arr[1]),Double.parseDouble(arr[2])));
            }
            else if(flagChoose==4){
                text = tArea.getText().replace("Nhập cạnh a: ", "");
                double a = Double.parseDouble(text);
                tArea.setText(DienTichHinhVuong(a));
            }
            else if(flagChoose==5){
                text = tArea.getText().replace("Nhập hệ số a: ", "");
                text = text.replace("\nNhập hệ số b: ", "");
                text = text.replace("\nNhập hệ số c: ", "");
                String[] arr = text.split(",");
                tArea.setText(GiaiPTBac2(Double.parseDouble(arr[0]),Double.parseDouble(arr[1]),Double.parseDouble(arr[2])));
            }
            else {
                
            }
            flagChoose=0;
            if(this.text.length()>0)
                this.text.delete(0, this.text.length());
        }
        else {
            double result = evaluate(text);
            text.delete(0, text.length());
            tArea.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
            if(String.valueOf(result).length()>10)
                tArea.setText("\n\n\n\t\t     "+result+"");
            else
                tArea.setText("\n\n\n\t\t\t\t"+result+"");
            System.out.println("");
            tArea.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        }
    }//GEN-LAST:event_HandleEval

    private void HandleBtnChia(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HandleBtnChia
        text.append("/");
        tArea.setText(text.toString());
    }//GEN-LAST:event_HandleBtnChia

    private void HandleBtnSin(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HandleBtnSin
        text.append("sin(");
        tArea.setText(text.toString());
    }//GEN-LAST:event_HandleBtnSin

    private void HandleBtnCos(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HandleBtnCos
        text.append("cos(");
        tArea.setText(text.toString());
    }//GEN-LAST:event_HandleBtnCos

    private void HandleBtnTan(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HandleBtnTan
        text.append("tan(");
        tArea.setText(text.toString());
    }//GEN-LAST:event_HandleBtnTan

    private void HandleBtnOpen(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HandleBtnOpen
        text.append("(");
        tArea.setText(text.toString());
    }//GEN-LAST:event_HandleBtnOpen

    private void HandleBtnClose(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HandleBtnClose
        text.append(")");
        tArea.setText(text.toString());
    }//GEN-LAST:event_HandleBtnClose

    private void HandleBtnGt(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HandleBtnGt
        text.append("factorial(");
        tArea.setText(text.toString());
    }//GEN-LAST:event_HandleBtnGt

    private void HandleBtnCan(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HandleBtnCan
        text.append("sqrt(");
        tArea.setText(text.toString());
    }//GEN-LAST:event_HandleBtnCan

    private void HandleMode(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HandleMode
        flagMode = !flagMode;
        if(flagMode)
        {
            tArea.setText(String.format("%-30s %-30s\n%-30s %-30s\n%-30s",
            "1.Tính S hình tròn", "2.Tính S hình chữ nhật",
            "3.Tính S hình tam giác", "4.Tính S hình vuông",
            "5.Tính PT bậc 2"));
        }
        else {
            tArea.setText("");
        }
    }//GEN-LAST:event_HandleMode

    private void HandlePow(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HandlePow
        text.append("^2");
        tArea.setText(text.toString());
    }//GEN-LAST:event_HandlePow

    private void HandleTab(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HandleTab
        int caretPos = tArea.getCaretPosition(); // Lấy vị trí con trỏ hiện tại
                    String text = tArea.getText();
                    tArea.setText(tArea.getText()+" ");
                    // Tìm vị trí tiếp theo của dấu ',' hoặc ':'
                    int nextPos = -1;
                    for (int i = caretPos; i < text.length()+10; i++) {
                        if (text.charAt(i) == ':') {
                            nextPos = i + 2; // Di chuyển con trỏ đến sau dấu ',' hoặc ':'
                            break;
                        }
                    }

                    // Nếu tìm thấy vị trí hợp lệ, di chuyển con trỏ đến đó
                    if (nextPos != -1 && nextPos < text.length()) {
                        tArea.setCaretPosition(nextPos);
                    }
    }//GEN-LAST:event_HandleTab

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CasioJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CasioJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CasioJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CasioJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CasioJFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn0;
    private javax.swing.JButton btn1;
    private javax.swing.JButton btn2;
    private javax.swing.JButton btn3;
    private javax.swing.JButton btn4;
    private javax.swing.JButton btn5;
    private javax.swing.JButton btn6;
    private javax.swing.JButton btn7;
    private javax.swing.JButton btn8;
    private javax.swing.JButton btn9;
    private javax.swing.JButton btnAc;
    private javax.swing.JButton btnBang;
    private javax.swing.JButton btnBp;
    private javax.swing.JButton btnC;
    private javax.swing.JButton btnCan;
    private javax.swing.JButton btnChia;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnCong;
    private javax.swing.JButton btnCos;
    private javax.swing.JButton btnDot;
    private javax.swing.JButton btnGt;
    private javax.swing.JButton btnLn;
    private javax.swing.JButton btnLog;
    private javax.swing.JButton btnNhan;
    private javax.swing.JButton btnOpen;
    private javax.swing.JButton btnPi;
    private javax.swing.JButton btnPs;
    private javax.swing.JButton btnSin;
    private javax.swing.JButton btnTan;
    private javax.swing.JButton btnTru;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea tArea;
    // End of variables declaration//GEN-END:variables
}
