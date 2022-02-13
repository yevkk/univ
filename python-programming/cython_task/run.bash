#! /bin/bash

pip install cython
python3 setup.py build_ext -i

echo ""
python3 lab.py
