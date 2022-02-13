#! /bin/bash

pip install cython
python3 setup.py build_ext -i

echo ""
echo "Input data: {2: 10, 3: 7, 5: 3}"
python3 lab.py
