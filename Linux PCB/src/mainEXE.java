import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class mainEXE {
    public static void main(String[] args) {
        Window window = new Window();
        window.draw();

        ArrayList<PCB> pcb = new ArrayList<>();
        pcb.add(new PCB("p0",9,0,5,0,'W'));
        pcb.add(new PCB("p1",38,0,2,0,'W'));
        pcb.add(new PCB("p2",30,0,6,0,'W'));
        pcb.add(new PCB("p3",29,0,3,0,'W'));
        pcb.add(new PCB("p4",4,0,4,0,'W'));
        printStatus(window,pcb);
//        timeRotation(window,pcb);
        ExecutorService service = Executors.newCachedThreadPool(new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "output");
            }
        });
         class StartButtonListener implements ActionListener {
            private JComboBox cate;
            private void set(JComboBox cate){
                this.cate = cate;
            }
            public void actionPerformed(ActionEvent e) {
                int algorithm = cate.getSelectedIndex();
                if(algorithm==0){                   //最高优先数优先
                    service.submit(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                highestPriority(window,pcb);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        }
                    });
                }
                else if(algorithm == 1){            //先来先服务
                    service.submit(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                FIFO(window,pcb);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        }
                    });
                }
                else if(algorithm == 2){            //时间片轮转算法
                    service.submit(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                timeRotation(window,pcb);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        }
                    });
                }
                else if(algorithm == 3){
                    service.submit(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                HighestAndTimeRotation(window,pcb);
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        }
                    });
                }
            }

        }
        StartButtonListener sbl = new StartButtonListener();
        window.start.addActionListener(sbl);
        sbl.set(window.cate);

    }

    private static void printStatus(Window window, ArrayList<PCB> pcb){
        for (PCB value : pcb) {
            window.appendStatusJTextArea(" " + value.name + "\t" + value.priority_print + "\t" + value.arriveTime + "\t" + value.allTime + "\t      " + value.CPUTime + "\t      " + value.status);
        }
    }

    private static void timeRotation(Window window, ArrayList<PCB> pcb) throws InterruptedException {
        ArrayList<PCB> pcb_print = new ArrayList<>(pcb);
        for (PCB value : pcb) {
            value.priority = 35;
            value.priority_print = 35;
        }
        int time = 0;
        while (!isFinish(pcb)) {
            Collections.sort(pcb);
            PCB p = pcb.get(0);
            int index = 0;
            while(pcb_print.get(index)!=p){index++;}
            pcb_print.get(index).status='R';
            window.appendResultJTextArea("正在运行"+p.name);
            window.appendResultJTextArea("\t当前就绪队列:");
            for (int i = 1; i < pcb.size(); i++) {
                pcb.get(i).status='W';
                window.appendResultJTextArea(pcb.get(i).name + " ");
            }
            window.appendResultJTextArea("\n");
            window.refresh();
            printStatus(window, pcb_print);
            p.priority -= 3;
            p.CPUTime++;
            p.runTime--;
            Thread.sleep(1000);
            if (p.runTime == 0) {
                pcb_print.get(index).status='F';
                pcb.remove(0);
            }
        }
    }
    private static void highestPriority(Window window, ArrayList<PCB> pcb) throws InterruptedException {
        ArrayList<PCB> pcb_print = new ArrayList<>(pcb);
        while (!isFinish(pcb)) {
            Collections.sort(pcb);
            PCB p = pcb.get(0);
            int index = 0;
            while(pcb_print.get(index)!=p){index++;}
            pcb_print.get(index).status='R';
            window.appendResultJTextArea("正在运行"+p.name);
            window.appendResultJTextArea("\t当前就绪队列:");
            for (int i = 1; i < pcb.size(); i++) {
                pcb.get(i).status='W';
                window.appendResultJTextArea(pcb.get(i).name + " ");
            }
            window.appendResultJTextArea("\n");
            window.refresh();
            printStatus(window, pcb_print);
            p.CPUTime++;
            p.runTime--;
            Thread.sleep(1000);
            if (p.runTime == 0) {
                pcb_print.get(index).status='F';
                pcb.remove(0);
            }
        }
    }

    private static void FIFO(Window window,ArrayList<PCB> pcb) throws InterruptedException{
        ArrayList<PCB> pcb_print = new ArrayList<>();
        for(int i =0;i<pcb.size();i++){
            pcb_print.add(pcb.get(i));
            if(i>0) {
                pcb_print.get(i).arriveTime = i + 2;
            }
        }
        while (!isFinish(pcb)) {
            PCB p = pcb.get(0);
            int index = 0;
            while(pcb_print.get(index)!=p){index++;}
            pcb_print.get(index).status='R';
            window.appendResultJTextArea("正在运行"+p.name);
            window.appendResultJTextArea("\t当前就绪队列:");
            for (int i = 1; i < pcb.size(); i++) {
                pcb.get(i).status='W';
                window.appendResultJTextArea(pcb.get(i).name + " ");
            }
            window.appendResultJTextArea("\n");
            window.refresh();
            printStatus(window, pcb_print);
            p.CPUTime++;
            p.runTime--;
            Thread.sleep(1000);
            if (p.runTime == 0) {
                pcb_print.get(index).status='F';
                pcb.remove(0);
            }
        }
    }
    private static void HighestAndTimeRotation(Window window, ArrayList<PCB> pcb) throws InterruptedException {
        ArrayList<PCB> pcb_print = new ArrayList<>(pcb);
        while (!isFinish(pcb)) {
            Collections.sort(pcb);
            PCB p = pcb.get(0);
            int index = 0;
            while(pcb_print.get(index)!=p){index++;}
            pcb_print.get(index).status='R';
            window.appendResultJTextArea("正在运行"+p.name);
            window.appendResultJTextArea("\t当前就绪队列:");
            for (int i = 1; i < pcb.size(); i++) {
                pcb.get(i).status='W';
                window.appendResultJTextArea(pcb.get(i).name + " ");
            }
            window.appendResultJTextArea("\n");
            window.refresh();
            printStatus(window, pcb_print);
            p.priority -= 1;
            p.priority_print -=1;
            p.CPUTime++;
            p.runTime--;
            Thread.sleep(1000);
            if (p.runTime == 0) {
                pcb_print.get(index).status='F';
                pcb.remove(0);
            }
        }
    }

    private static boolean isFinish(ArrayList<PCB> pcb) {
        boolean flag = true;
        for (PCB p : pcb) {
            if (p.runTime != 0) {
                flag = false;
                break;
            }
        }
        return flag;
    }


}

