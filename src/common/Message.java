package common;

/**
 * Message class
 * the message transformation between model and view
 */
public class Message {

    public STATE state;
    public Object obj;

    /**
     * ctor for Message class
     * @param state The STATE of next step
     * @param obj The possible information
     */
    public Message(STATE state, Object obj){
        this.state = state;
        this.obj = obj;
    }
}
