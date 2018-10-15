package common;

public class Message {

    STATE state;
    Object obj;

    public Message(STATE state, Object obj){
        this.state = state;
        this.obj = obj;
    }
}
