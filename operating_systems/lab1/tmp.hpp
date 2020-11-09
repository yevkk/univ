
void f() {

    std::atomic<bool> done = false;

    bool prompt_enabled = true;
    auto next_prompt_ts = system_clock::now() + PROMPT_PERIOD;

    auto keyboard_listener = std::async(std::launch::async,
                                        [&done]() {
                                            while (true) {
                                                std::this_thread::sleep_for(50ms);
                                                if (done || (GetKeyState(VK_ESCAPE) & 0x8000)) {
                                                    break;
                                                }
                                            }
                                        }
    );


    if (keyboard_listener.wait_for(0s) == std::future_status::ready) {
        _terminateUnfinished();
        _exitRun(start_ts);
        return TERMINATED;
    }

    if (system_clock::now() > next_prompt_ts && prompt_enabled) {
        std::cout << "[TIME] " << duration_cast<seconds>(system_clock::now() - start_ts).count() << "s\n";
        std::cout << "Choose Option:\n \ta) continue\n \tb) continue without prompt\n \tc) stop\n";
        char input = ' ';
        while (input != 'a' && input != 'b' && input != 'c') {
            std::cin >> input;
            if (input == 'a') {
                std::cout << "[INFO] Continued\n";
            } else if (input == 'b') {
                std::cout << "[INFO] Prompt disabled\n";
                prompt_enabled = false;
            } else if (input == 'c') {
                done = true;
            }
        }
        next_prompt_ts = system_clock::now() + PROMPT_PERIOD;
    }

    done = true;
}