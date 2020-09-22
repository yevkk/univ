#ifndef LAB_DB_H
#define LAB_DB_H

struct Account {
    unsigned int id;
    char nickname[32];
    char full_name[32];
    char country[32];
};

struct Post {
    unsigned int id;
    char title[32];
    float pulse;
};

struct IndexItem {
    unsigned int id;
    unsigned int record_number;
};

struct IndexMeta  {
    unsigned int size;
    unsigned int max_id;
};

struct {
    struct IndexItem *data;
    unsigned int capacity;
    unsigned int size;
    unsigned int max_id;
} mIndex;

void load();

void insertM(struct Account* data);

#endif //LAB_DB_H
