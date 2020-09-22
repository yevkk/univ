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
    FILE *m_data_file = fopen(M_DATA_FILENAME, "rb");
    if (m_data_file == NULL) {
        fclose(m_data_file);
        m_data_file = fopen(M_DATA_FILENAME, "ab");
        unsigned int tmp = 0;
        fwrite(&tmp, sizeof(tmp), 1, m_data_file);
    }
    fclose(m_data_file);

    FILE *s_data_file = fopen(S_DATA_FILENAME, "rb");
    if (s_data_file == NULL) {
        fclose(s_data_file);
        s_data_file = fopen(S_DATA_FILENAME, "ab");
        unsigned int tmp = 0;
        fwrite(&tmp, sizeof(tmp), 1, s_data_file);
    }
    fclose(s_data_file);

    FILE *m_gc_file = fopen(M_GC_FILENAME, "ab");
    FILE *s_gc_file = fopen(S_GC_FILENAME, "ab");
    fclose(m_gc_file);
    fclose(s_gc_file);

    mIndex.size = 0;
    mIndex.capacity = INDEX_INITIAL_SIZE;
    mIndex.data = (struct IndexItem *) malloc(mIndex.capacity);

    FILE *m_index_file = fopen(M_INDEX_FILENAME, "rb");
    if (m_index_file == NULL) {
        fclose(m_index_file);
        m_index_file = fopen(M_INDEX_FILENAME, "ab");
        mIndex.size = 0;
        mIndex.max_id = 0;
        mIndex.capacity = INDEX_INITIAL_SIZE;
        mIndex.data = (struct IndexItem *) malloc(mIndex.capacity);
    } else {
        struct IndexMeta meta;
        fread(&meta, sizeof(struct IndexMeta), 1, m_index_file);

        mIndex.max_id = meta.max_id;
        mIndex.size = meta.size;
        int tmp;
        for (tmp = 2; tmp < meta.size; tmp *= 2);
        mIndex.capacity = (tmp > INDEX_INITIAL_SIZE) ? tmp : INDEX_INITIAL_SIZE;
        mIndex.data = (struct IndexItem *) malloc(mIndex.capacity);

        for (unsigned int i = 0; i < mIndex.size; i++) {
            fread(mIndex.data + i, sizeof(struct IndexItem), 1, m_index_file);
        }
    }
    fclose(m_index_file);
}

void insertM(struct Account *account_ptr) {
    if (!account_ptr) return;

    account_ptr->id = ++mIndex.max_id;
    unsigned int m_size = mIndex.size;

    FILE *m_data_file = fopen(M_DATA_FILENAME, "r+");

    fseek(m_data_file, 0, SEEK_END);
    fwrite(account_ptr, sizeof(struct Account), 1, m_data_file);

    struct IndexItem newIndexItem = {account_ptr->id, m_size};
    //TODO: capacity overflow
    mIndex.data[mIndex.size] = newIndexItem;
    mIndex.size++;
}
