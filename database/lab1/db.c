#include "db.h"

#include <stdio.h>
#include <stdlib.h>
#include <stdbool.h>
#include <string.h>
#include <math.h>

const char M_INDEX_FILENAME[] = "index_m.ind";
const char M_DATA_FILENAME[] = "data_m.fl";
const char S_DATA_FILENAME[] = "data_s.fl";

void load() {
    struct DataMeta m_meta = {0, 0};
    struct DataMeta s_meta = {0, 0};

    FILE *m_data_file = fopen(M_DATA_FILENAME, "rb");
    if (m_data_file == NULL) {
        m_data_file = fopen(M_DATA_FILENAME, "ab");
        fwrite(&m_meta, sizeof(struct DataMeta), 1, m_data_file);
    } else {
        fread(&m_meta, sizeof(struct DataMeta), 1, m_data_file);
    }
    fclose(m_data_file);

    FILE *s_data_file = fopen(S_DATA_FILENAME, "rb");
    if (s_data_file == NULL) {
        s_data_file = fopen(S_DATA_FILENAME, "ab");
        fwrite(&s_meta, sizeof(s_meta), 1, s_data_file);
    }
    fclose(s_data_file);

    m_index.max_id = m_meta.max_id;
    m_index.size = m_meta.size;

    FILE *m_index_file = fopen("index_m.ind", "rb");
    if (m_index_file == NULL) {
        m_index_file = fopen("index_m.ind", "ab");
    } else {
        for (unsigned i = 0; i < m_index.size; i++) {
            fread(m_index.data + i, sizeof(struct IndexItem), 1, m_index_file);
        }
    }
    fclose(m_index_file);
}

void save_index() {
    FILE *m_index_file = fopen("index_m.ind", "wb");

    for (unsigned i = 0; i < m_index.size; i++) {
        fwrite(m_index.data + i, sizeof(struct IndexItem), 1, m_index_file);
    }

    fclose(m_index_file);
}

int get_m_record_no(unsigned id) {
    if (m_index.size == 0) {
        return -1;
    }

    unsigned left = 0;
    unsigned right = m_index.size - 1;

    while (left <= right) {
        unsigned mid = (left + right) / 2;
        if (m_index.data[mid].id > id) {
            right = mid - 1;
        } else if (m_index.data[mid].id < id) {
            left = mid + 1;
        } else {
            return (int) m_index.data[mid].record_no;
        }
    }

    return -1;
}

int get_s_record_no(unsigned id) {
    FILE *s_data_file = fopen(S_DATA_FILENAME, "rb");
    struct DataMeta s_meta;
    fread(&s_meta, sizeof(struct DataMeta), 1, s_data_file);

    if (id > s_meta.max_id) {
        return -1;
    }

    for (int i = 1; i <= s_meta.size; i++) {
        struct Post *post = malloc(sizeof(struct Post));
        bool valid;
        fread(post, sizeof(struct Post), 1, s_data_file);
        fread(&valid, sizeof(bool), 1, s_data_file);
        if (post->id == id) {
            return (valid) ? i : -1;
        }
        fseek(s_data_file, sizeof(int), SEEK_CUR);
    }

    fclose(s_data_file);

    return -1;
}

int get_s_of_m_record_no(unsigned m_id, unsigned id) {
    unsigned m_record_no = get_m_record_no(m_id);
    if (m_record_no == -1) {
        return -1;
    }

    FILE *m_data_file = fopen(M_DATA_FILENAME, "rb");
    FILE *s_data_file = fopen(S_DATA_FILENAME, "rb");

    struct Post *post = malloc(sizeof(struct Post));
    int s_record_no;

    fseek(m_data_file,
          sizeof(struct DataMeta) + (m_record_no) * (sizeof(struct Account) + sizeof(bool)) +
          (m_record_no - 1) * sizeof(int),
          SEEK_SET
    );
    fread(&s_record_no, sizeof(int), 1, m_data_file);

    while (s_record_no != -1) {
        fseek(s_data_file,
              sizeof(struct DataMeta) + (s_record_no - 1) * (sizeof(struct Post) + sizeof(bool) + sizeof(int)),
              SEEK_SET
        );
        fread(post, sizeof(struct Post), 1, s_data_file);

        if (post->id == id) {
            return s_record_no;
        }

        fseek(s_data_file, sizeof(bool), SEEK_CUR);
        fread(&s_record_no, sizeof(int), 1, s_data_file);
    }

    fclose(m_data_file);
    fclose(s_data_file);

    return -1;
}

struct Account *get_m(unsigned id) {
    int record_no = get_m_record_no(id);

    if (record_no != -1) {
        struct Account *account = malloc(sizeof(struct Account));
        FILE *m_data_file = fopen(M_DATA_FILENAME, "rb");
        fseek(m_data_file,
              sizeof(struct DataMeta) + (record_no - 1) * (sizeof(struct Account) + sizeof(bool) + sizeof(int)),
              SEEK_SET
        );
        fread(account, sizeof(struct Account), 1, m_data_file);
        return account;
    } else {
        return NULL;
    }
}

struct Post *get_s(unsigned id) {
    int record_no = get_s_record_no(id);

    if (record_no != -1) {
        struct Post *post = malloc(sizeof(struct Post));
        FILE *s_data_file = fopen(S_DATA_FILENAME, "rb");
        fseek(s_data_file,
              sizeof(struct DataMeta) + (record_no - 1) * (sizeof(struct Post) + sizeof(bool) + sizeof(int)),
              SEEK_SET
        );
        fread(post, sizeof(struct Account), 1, s_data_file);
        return post;
    } else {
        return NULL;
    }
}

struct Post *get_s_of_m(unsigned m_id, unsigned id) {
    int record_no = get_s_of_m_record_no(m_id, id);

    if (record_no != -1) {
        struct Post *post = malloc(sizeof(struct Post));
        FILE *s_data_file = fopen(S_DATA_FILENAME, "rb");
        fseek(s_data_file,
              sizeof(struct DataMeta) + (record_no - 1) * (sizeof(struct Post) + sizeof(bool) + sizeof(int)),
              SEEK_SET
        );
        fread(post, sizeof(struct Account), 1, s_data_file);
        return post;
    } else {
        return NULL;
    }
}

int insert_m(const char nickname[32], const char fullname[32], const char country[32]) {
    if (m_index.size == INDEX_MAX_SIZE) {
        return 1;
    }

    FILE *m_data_file = fopen(M_DATA_FILENAME, "rb+");

    struct DataMeta m_meta;
    fseek(m_data_file, 0, SEEK_SET);
    fread(&m_meta, sizeof(struct DataMeta), 1, m_data_file);
    m_meta.size++;
    m_index.max_id = ++m_meta.max_id;
    fseek(m_data_file, 0, SEEK_SET);
    fwrite(&m_meta, sizeof(struct DataMeta), 1, m_data_file);

    struct Account account;
    account.id = m_index.max_id;
    strcpy(account.nickname, nickname);
    strcpy(account.fullname, fullname);
    strcpy(account.country, country);

    fseek(m_data_file, 0, SEEK_END);
    fwrite(&account, sizeof(struct Account), 1, m_data_file);
    bool valid = true;
    fwrite(&valid, sizeof(bool), 1, m_data_file);
    int s_record_no = -1;
    fwrite(&s_record_no, sizeof(int), 1, m_data_file);

    fclose(m_data_file);

    struct IndexItem newIndexItem = {account.id, m_meta.size};
    m_index.data[m_index.size++] = newIndexItem;

    return 0;
}

int insert_s(unsigned m_id, const char title[32], float pulse) {
    unsigned m_record_no = get_m_record_no(m_id);
    if (m_record_no == -1) {
        return 1;
    }

    FILE *s_data_file = fopen(S_DATA_FILENAME, "rb+");

    struct DataMeta s_meta;
    fseek(s_data_file, 0, SEEK_SET);
    fread(&s_meta, sizeof(struct DataMeta), 1, s_data_file);
    s_meta.size++;
    s_meta.max_id++;
    fseek(s_data_file, 0, SEEK_SET);
    fwrite(&s_meta, sizeof(struct DataMeta), 1, s_data_file);

    struct Post post;
    post.id = s_meta.max_id;
    strcpy(post.title, title);
    post.pulse = pulse;

    fseek(s_data_file, 0, SEEK_END);
    fwrite(&post, sizeof(struct Post), 1, s_data_file);
    bool valid = true;
    fwrite(&valid, sizeof(bool), 1, s_data_file);
    int next_s_record_no = -1;
    fwrite(&next_s_record_no, sizeof(int), 1, s_data_file);


    FILE *m_data_file = fopen(M_DATA_FILENAME, "rb+");
    fseek(m_data_file,
          sizeof(struct DataMeta) + (m_record_no) * (sizeof(struct Account) + sizeof(bool)) +
          (m_record_no - 1) * sizeof(int),
          SEEK_SET
    );
    fread(&next_s_record_no, sizeof(int), 1, m_data_file);
    if (next_s_record_no == -1) {
        fseek(m_data_file, (long) -sizeof(int), SEEK_CUR);
        fwrite(&s_meta.size, sizeof(int), 1, m_data_file);
    } else {
        while (next_s_record_no != -1) {
            int s_record_no = next_s_record_no;
            fseek(s_data_file,
                  sizeof(struct DataMeta) + (s_record_no) * (sizeof(struct Post) + sizeof(bool)) +
                  (s_record_no - 1) * sizeof(int),
                  SEEK_SET
            );
            fread(&next_s_record_no, sizeof(int), 1, s_data_file);
        }
        fseek(s_data_file, (long) -sizeof(int), SEEK_CUR);
        fwrite(&s_meta.size, sizeof(int), 1, s_data_file);
    }

    fclose(m_data_file);
    fclose(s_data_file);

    return 0;
}

void ut_m(bool print_deleted) {
    FILE *m_data_file = fopen(M_DATA_FILENAME, "rb");

    struct DataMeta m_meta;
    fread(&m_meta, sizeof(struct DataMeta), 1, m_data_file);
    printf("SIZE (with removed): %d\n", m_meta.size);
    printf("MAX ID: %d \n", m_meta.max_id);

    for (unsigned i = 0; i < m_meta.size; i++) {
        struct Account account;
        int s_record_no;
        bool valid;
        fread(&account, sizeof(struct Account), 1, m_data_file);
        fread(&valid, sizeof(bool), 1, m_data_file);
        fread(&s_record_no, sizeof(int), 1, m_data_file);

        if (valid || print_deleted) {
            printf("%d)\n", i + 1);
            printf("\t id: %d \n", account.id);
            printf("\t nickname: %s \n", account.nickname);
            printf("\t full_name: %s \n", account.fullname);
            printf("\t country: %s \n", account.country);
            printf("\t [first s record no.: %d] \n", s_record_no);
            if (print_deleted) {
                printf("\t [state: %s] \n", (valid) ? "valid" : "deleted");
            }
        }
    }

    fclose(m_data_file);
}

void ut_s(bool print_deleted) {
    FILE *s_data_file = fopen(S_DATA_FILENAME, "rb");

    struct DataMeta s_meta;
    fread(&s_meta, sizeof(struct DataMeta), 1, s_data_file);
    printf("SIZE (with removed): %d\n", s_meta.size);
    printf("MAX ID: %d \n", s_meta.max_id);

    for (unsigned i = 0; i < s_meta.size; i++) {
        struct Post post;
        int next_s_record_no;
        bool valid;
        fread(&post, sizeof(struct Post), 1, s_data_file);
        fread(&valid, sizeof(bool), 1, s_data_file);
        fread(&next_s_record_no, sizeof(int), 1, s_data_file);

        if (valid || print_deleted) {
            printf("%d)\n", i + 1);
            printf("\t id: %d \n", post.id);
            printf("\t title: %s \n", post.title);
            printf("\t pulse: %f \n", post.pulse);
            printf("\t [next s record no.: %d] \n", next_s_record_no);
            if (print_deleted) {
                printf("\t [state: %s] \n", (valid) ? "valid" : "deleted");
            }
        }
    }

    fclose(s_data_file);
}
