from cython_wrapper import compute

test_data = {2: 10, 3: 7, 5: 3}
print("Test data:", test_data)
print(compute(test_data))
