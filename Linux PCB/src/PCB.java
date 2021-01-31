public class PCB implements Comparable<PCB>{
    String name;
    int priority;
    int priority_print;
    int arriveTime;
    int runTime;
    int allTime;
    int CPUTime;
    char status;
    public PCB(String name, int priority, int arriveTime, int runTime, int CPUTime, char status){
        super();
        this.name = name;
        this.priority = priority;
        this.priority_print = priority;
        this.arriveTime = arriveTime;
        this.runTime = runTime;
        this.allTime = runTime;
        this.CPUTime = CPUTime;
        this.status = status;
    }

    @Override
    public int compareTo(PCB o) {
        if (this.priority > o.priority) {
            return -1;
        } else if (this.priority < o.priority) {
            return 1;
        } else {
            return 0;
        }
    }

    public String toString() {

        return this.priority + "";
    }
}
