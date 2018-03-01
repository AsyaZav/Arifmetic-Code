import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main extends JFrame {

    public static void createGUI() {
        JFrame frame = new JFrame("Arifmetic coding");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setSize(600,400);
        frame.add(new Avt());
        frame.validate();
        frame.repaint();

    }

    public static void main(String[] args) {
        createGUI();

    }
}

class Avt extends JPanel {

    Dimension sizeWindow = Toolkit.getDefaultToolkit().getScreenSize();

    private JLabel firstLabel = new JLabel("Введите кол-во символов: ");
    private JLabel secondLabel = new JLabel("Введите фразу для кодирования: ");
    private JLabel resCode = new JLabel();
    private JLabel thirdLabel = new JLabel("Введите код: ");
    private JLabel resDecode = new JLabel();
    private JLabel endSymbol = new JLabel("Заверш. символ: ");

    private JTextField firstText = new JTextField(5);
    private JTextField codedPhrase = new JTextField(30);
    private JTextField code = new JTextField(30);
    private JTextField endSymb = new JTextField(30);

    private JTable table;

    private JButton button;
    private JButton codingButton;
    private JButton decodingButton;

    Calculation coding;

    int n = 0;
    float p;

    public Avt() {
        setLayout(null);
        firstLabel.setBounds(10, 10, 170, 18);
        firstText.setBounds(180, 10, 20, 20);

        button = new JButton("Таблица: ");
        button.setBounds(10, 40, 100, 20);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                n = Integer.parseInt(firstText.getText());
                p = (float) 1 / (n + 1);
                System.out.println("n:"+n+" p:"+p);
                if (table!= null) remove(table);
                table = new JTable(n + 1,2);
                table.setValueAt("Символ", 0, 0);
                table.setValueAt("Вероятность", 0, 1);
                for (int i = 1; i < n + 1; i++)
                    table.setValueAt(String.valueOf(p), i, 1);
                table.setBounds(150,40,200, table.getRowHeight()* (n + 1));
                add(table);

                revalidate();
                repaint();


            }
        });

        endSymbol.setBounds(10,65,120,20);
        endSymb.setBounds(10,90,100,20);

        codingButton = new JButton("Закодировать");
        codingButton.setBounds(360, 115, 120, 20);
        codingButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String[][] data = new String[n + 1][2];
                for (int i = 0; i < n; i++){
                    data[i][0]=(String) table.getValueAt(i + 1,0);
                    data[i][1]=(String) table.getValueAt(i + 1,1);
                }
                data[n][0] = endSymb.getText();
                data[n][1] = data[n-1][1];
                coding = new Calculation(data,codedPhrase.getText());
                String result = coding.coding();
                resCode.setText("Интервал: " + result);
            }
        });

        secondLabel.setBounds(360, 40, 250, 20);
        codedPhrase.setBounds(360,70,250,20);
        resCode.setBounds(360,140,600,20);
        thirdLabel.setBounds(360,170,100,20);
        code.setBounds(360,195,200,20);
        resDecode.setBounds(360, 245, 120, 20);

        decodingButton = new JButton("Декодировать");
        decodingButton.setBounds(360, 220, 120, 20);
        decodingButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resDecode.setText(coding.decoding(code.getText()));
            }
        });

        add(firstLabel);
        add(firstText);
        add(button);
        add(endSymbol);
        add(endSymb);
        add(secondLabel);
        add(codedPhrase);
        add(codingButton);
        add(resCode);
        add(thirdLabel);
        add(code);
        add(decodingButton);
        add(resDecode);
        revalidate();
        repaint();

    }
}

class Calculation {

    String[][] matrix;
    String alfabet = "";
    String phrase;
    String[][] interval;
    String[] symbols;
    double a = 0,b = 1;
    String endSymbol = "";

    Calculation(String[][] m, String p){
        matrix = new String[m.length][m[0].length];
        interval = new String[m.length + 1][m[0].length];
        interval[0][1] = "0";
        for (int i = 0; i < m.length; i++){
            matrix[i][0] = m[i][0];
            matrix[i][1] = m[i][1];
            alfabet = alfabet + m[i][0];

            interval[i + 1][0] = m[i][0];
            interval[i + 1][1] = String.valueOf(Double.parseDouble(interval[i][1]) + Double.parseDouble(m[i][1]));

            System.out.println("3: " +interval[i + 1][0]+"-"+interval[i + 1][1]);
        }
        endSymbol = matrix[matrix.length - 1][0];
        System.out.println("end: " + endSymbol);
        phrase = new String(p);
        symbols = new String[phrase.length()];
        for(int i = 0; i < phrase.length(); i++)
            symbols[i] = String.valueOf(phrase.charAt(i));

    }
    public String coding(){

        for (int i = 0; i < symbols.length; i++)
            if (!alfabet.contains(symbols[i])) return "ERROR!";

        for(int i = 0; i < phrase.length(); i++)
            for (int j = 1; j < interval.length; j++){

                if (symbols[i].equals(interval[j][0])){
                    double a1 = a + (b - a) * Double.parseDouble(interval[j - 1][1]);
                    double b1 = a + (b - a) * Double.parseDouble(interval[j][1]);
                    b = b1;
                    a = a1;
                    System.out.println("a: " + a + " b: " + b);
                }
            }
        return "a: " + a + "; b: " + b;
    }

    public String decoding(String s){
        double x = 0, y = 1;
        double d = Double.parseDouble(s);
        String res = "";

        System.out.println("d: " + d);

        while (!(res.contains(endSymbol))){
            System.out.println("res: "+res);
            d = (d - x)/(y - x);

            for(int i = 1; i < interval.length; i++) {
                if (Double.parseDouble(interval[i - 1][1]) <= d && Double.parseDouble(interval[i][1]) >= d) {
                    res = res + interval[i][0];
                    x = Double.parseDouble(interval[i - 1][1]);
                    y = Double.parseDouble(interval[i][1]);
                }
            }
        }

        return "Phrase: " + res;
    }
}

