package org.practice.chapter6;

public class Time{
    int hour;
    int minute;
    int second;

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public int getSecond() {
        return second;
    }

    public void setHour(int hour) {
        if(hour<0 || hour>23)return;
        this.hour = hour;
    }

    public void setMinute(int minute) {
        if(minute<0 || minute>59)return;
        this.minute = minute;
    }

    public void setSecond(int second) {
        if(second<0 || second>59)return;
        this.second = second;
    }

    public void difference(Time t) {
        int total1 = this.hour * 60 * 60 + this.minute * 60 + this.second;
        int total2 = t.hour * 60 * 60 + t.minute * 60 + t.second;

        System.out.println(total1+" "+total2);

        int total = Math.abs(total1 - total2);
        int result_h = total / (60 * 60);
        total = total % (60 * 60);
        int result_m = total / 60;
        total = total % 60;
        int result_s = total;

        System.out.printf("시간 차이는 %d시간 %d분 %d초 입니다.", result_h, result_m, result_s);

    }

    public static void main(String[] args) {
        Time t1 = new Time();
        Time t2 = new Time();

        t1.hour =12;
        t1.minute =49;
        t1.second = 0;

        t2.hour =13;
        t2.minute =0;
        t2.second = 0;

        t1.difference(t2);


    }
}
