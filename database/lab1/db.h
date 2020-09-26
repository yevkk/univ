#ifndef LAB_DB_H
#define LAB_DB_H

struct Account {
    unsigned id;
    char nickname[32];
    char fullname[32];
    char country[32];
};

struct Post {
    unsigned id;
    char title[32];
    float pulse;
};

struct IndexItem {
    unsigned id;
    unsigned record_no;
};

struct DataMeta  {
    unsigned size;
    unsigned max_id;
};

struct {
    struct IndexItem *data;
    unsigned int capacity;
    unsigned int size;
    unsigned int max_id;
} m_index;

void load();

//void insertM(const nickname[32], char fullname[32], char country[32]);

#endif //LAB_DB_H
