#include "db.h"

#include <stdio.h>
#include <stdlib.h>
#include <math.h>

const char M_INDEX_FILENAME[] = "index_m.ind";
const char M_DATA_FILENAME[] = "data_m.fl";
const char S_DATA_FILENAME[] = "data_s.fl";
const char M_GC_FILENAME[] = "gc_m.fl";
const char S_GC_FILENAME[] = "gc_s.fl";
const unsigned int INDEX_INITIAL_SIZE = 32;

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

    FILE *m_gc_file = fopen(M_GC_FILENAME, "ab");
    FILE *s_gc_file = fopen(S_GC_FILENAME, "ab");
    fclose(m_gc_file);
    fclose(s_gc_file);

    m_index.max_id = m_meta.max_id;
    m_index.size = m_meta.size;
    int tmp;
    for (tmp = 2; tmp < m_meta.size; tmp *= 2);
    m_index.capacity = (tmp > INDEX_INITIAL_SIZE) ? tmp : INDEX_INITIAL_SIZE;
    m_index.data = (struct IndexItem *) malloc(m_index.capacity);

    FILE *m_index_file = fopen(M_INDEX_FILENAME, "rb");
    if (m_index_file == NULL) {
        m_index_file = fopen(M_INDEX_FILENAME, "ab");
    } else {
        for (unsigned int i = 0; i < m_index.size; i++) {
            fread(m_index.data + i, sizeof(struct IndexItem), 1, m_index_file);
        }
    }
    fclose(m_index_file);
}

//void insertM(const char nickname[32], const char full_name[32], const char country[32]) {
//    struct Account account = {};
//    account.id = ++mIndex.max_id;
//    *account.nickname = *nickname;
//    *account.full_name = *full_name;
//    *account.country = *country;
//
//    FILE *m_data_file = fopen(M_DATA_FILENAME, "r+");
//    fseek(m_data_file, 0, SEEK_END);
//    fwrite(&account, sizeof(struct Account), 1, m_data_file);
//    int tmp  = -1;
//    fwrite(&tmp, sizeof(tmp), 1, m_data_file);
//
//    unsigned int m_size;
//    //TODO: read meta from m_data
//    struct IndexItem newIndexItem = {account.id, m_size};
//    //TODO: capacity overflow
//    mIndex.data[mIndex.size] = newIndexItem;
//    mIndex.size++;
//
//    fseek(m_data_file, 0, SEEK_SET);
//    struct DataMeta meta = {m_size + 1, mIndex.max_id};
//    fwrite(&meta, sizeof(struct DataMeta), 1, m_data_file);
//
//    fclose(m_data_file);
//}
