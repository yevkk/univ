from setuptools import setup
from Cython.Build import cythonize

setup(
    name='Lab 2',
    ext_modules=cythonize("cython_wrapper.pyx"),
    zip_safe=False,
)