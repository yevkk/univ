#include "db.h"

#include <stdio.h>

int main() {
    insert_m("NICKNAME 1", "FULL NAME 1", "COUNTRY 1");
    insert_m("NICKNAME 2", "FULL NAME 2", "COUNTRY 2");
    insert_m("NICKNAME 3", "FULL NAME 3", "COUNTRY 3");
    insert_m("NICKNAME 4", "FULL NAME 4", "COUNTRY 4");
    insert_m("NICKNAME 5", "FULL NAME 5", "COUNTRY 5");

    insert_s(1, "TITLE 1", 11.1f);
    insert_s(1, "TITLE 2", 22.2f);
    insert_s(4, "TITLE 3", 33.3f);
    insert_s(3, "TITLE 4", 44.4f);
    insert_s(4, "TITLE 5", 55.5f);
    insert_s(4, "TITLE 6", 67.7f);

    printf("\n[[INITIAL INSERTION]]\n");
    ut_m(true);
    printf("=====\n");
    ut_s(true);
    printf("=====\n=====\n");

    del_m(1);
    del_s_of_m(4, 5);

    printf("\n[[REMOVAL]]\n");
    ut_m(true);
    printf("=====\n");
    ut_s(true);
    printf("=====\n=====\n");

    defragment_m();
    defragment_s();

    printf("\n[[DEFRAGMENTATION]]\n");
    ut_m(true);
    printf("=====\n");
    ut_s(true);
    printf("=====\n=====\n");

    insert_m("NICKNAME 6", "FULL NAME 6", "COUNTRY 6");
    insert_s(6, "TITLE 7", 77.7f);

    printf("\n[[INSERTION]]\n");
    ut_m(true);
    printf("=====\n");
    ut_s(true);
    printf("=====\n=====\n");

    update_m(2, "NEW NICKNAME 2", "NEW FULL NAME 2", "NEW COUNTRY 2");
    update_s_of_m(4, 6, "NEW TITLE 6", 88.8f);

    printf("\n[[UPDATE]]\n");
    ut_m(true);
    printf("=====\n");
    ut_s(true);
    printf("=====\n=====\n");

    return 0;
}
