
void f() {
    if (keyboard_listener.wait_for(0s) == std::future_status::ready) {
        _terminateUnfinished();
        _exitRun(start_ts);
        return TERMINATED;
    }
}