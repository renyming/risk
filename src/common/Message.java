package common;

public class Message {

    public STATE state;
    public Object obj;

    public Message(STATE state, Object obj){
        this.state = state;
        this.obj = obj;
    }
}
