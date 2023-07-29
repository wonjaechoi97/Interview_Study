package org.practice.ch6_done;

public class TvTest {
    public static void main(String[] args) {
        Tv t = new Tv();
        t.channel = 7;
        t.channelDown();
        System.out.println(" 현재 채널은 "+t.channel+" 입니다.");

    }
}

class Tv {
    // TV의 속성(멤버 변수)
    String color;
    boolean power;
    int channel;

    //Tv의 기능(멤버 메서드)
    void power() {
        power = !power;
    }

    void channelUP() {
        ++channel;
    }

    void channelDown()

    {
        --channel;
    }
}





