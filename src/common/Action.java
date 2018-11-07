package common;


/**
 * Represent the actions that are taking place during a phase
 */
public enum Action {
    None,
    Finish_Current_Phase, // TODO: remove later
    Allocate_Army,
    Invalid_Card_Exchange,
    Invalid_Move,
    Show_Next_Phase_Button
}
