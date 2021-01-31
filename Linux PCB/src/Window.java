import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Window extends JFrame {
    Window(){
        this.setTitle("进程调度模拟程序");
        this.setSize(720,960);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setAlwaysOnTop(true);
        this.setLayout(null);
        //放置背景图
        ImageIcon background = new ImageIcon("src/PCBbg.jpg");
        JLabel bkLabel = new JLabel(background);
        bkLabel.setBounds(0, 0,background.getIconWidth(), background.getIconHeight());
        this.setSize(background.getIconWidth(), background.getIconHeight());
        this.getLayeredPane().add(bkLabel,new Integer(Integer.MIN_VALUE));
        JPanel ctPanel = (JPanel)this.getContentPane();
        ctPanel.setOpaque(false);

//        this.setVisible(true);
    }
    private static JTextArea processInformation = new JTextArea();
    private static JTextArea display = new JTextArea();
    static JButton start = new JButton("开始");
    static String[] cy = {"最高优先数优先","先来先服务","时间片轮转","最高优先+时间片"};
    static JComboBox cate = new JComboBox(cy);
    public void draw(){
        //调度算法选择
        JLabel select = new JLabel("调度算法");
        select.setBounds(40,20,200,30);
        select.setFont(new  java.awt.Font("微软雅黑", Font.PLAIN,  22));
        this.add(select);

        cate.setBounds(140,18,130,40);
        cate.setBackground(Color.white);
        this.add(cate);

        //开始按钮
//        JButton start = new JButton("开始");
        this.add(start);
        start.setBounds(300,18,200,40);
        start.setCursor(new Cursor(Cursor.HAND_CURSOR));
        start.setForeground(Color.black);
        start.setBackground(new Color(179,207,229));
        start.setFont(new java.awt.Font("微软雅黑", Font.PLAIN,25));
        start.setBorder(BorderFactory.createEmptyBorder());
        //StartButtonListener sbl = new StartButtonListener();
        //start.addActionListener(sbl);
        //sbl.set(cate);

        //退出按钮
        JButton exit = new JButton("退出");
        this.add(exit);
        exit.setBounds(500,18,200,40);
        exit.setCursor(new Cursor(Cursor.HAND_CURSOR));
        exit.setForeground(Color.black);
        exit.setBackground(new Color(179,207,229));
        exit.setFont(new java.awt.Font("微软雅黑", Font.PLAIN,25));
        exit.setBorder(BorderFactory.createEmptyBorder());
        ExitButtonListener et = new ExitButtonListener();
        exit.addActionListener(et);

        //进程状态
        JLabel processStatus = new JLabel("进程信息");
        processStatus.setBounds(40,80,200,30);
        processStatus.setFont(new  java.awt.Font("微软雅黑", Font.PLAIN,  22));
        this.add(processStatus);

        processInformation.setBounds(90,123,540,170);
        processInformation.setText(" 进程名\t优先级\t到达时间\t需要运行时间    \t已用CPU时间    \t进程状态\n");
        //processInformation.setText("进程名\t优先级\t到达时间\t需要运行时间\t已用CPU时间\t进程状态\n");  //每次进程状态变化都变一次
        this.add(processInformation);

        //调度结果
        JLabel result = new JLabel("运行结果");
        result.setBounds(40,323,200,30);
        result.setFont(new  java.awt.Font("微软雅黑", Font.PLAIN,  22));
        this.add(result);

        display.setBounds(90,363,540,320);
        this.add(display);


        this.setVisible(true);
    }
    public void appendStatusJTextArea(String info) {
        processInformation.append(info+"\n");
        processInformation.paintImmediately(processInformation.getBounds());
    }
    public void appendResultJTextArea(String info) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                display.append(info);
            }
        });
        //display.append(info);
        //display.paintImmediately(display.getBounds());
    }
    public void refresh(){
        processInformation.setText(" 进程名\t优先级\t到达时间\t需要运行时间    \t已用CPU时间    \t进程状态\n");
    }

}
class ExitButtonListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        System.exit(0);
    }
}