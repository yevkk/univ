#include "db.h"

int main() {
    load_db();
    console_interface();
    onclose_db();

    return 0;
}
