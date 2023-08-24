import sys
import math
import random

def res_fstr(desc, res):
    desc = desc + ':'
    return f'{desc:20}{res}'

# Sieve of Eratosthenes
def primes_list(max = 1000):
    bool_list = ([False] * 2) + ([True] * (max - 2))

    for i in range(2, math.ceil(math.sqrt(max))):
        if bool_list[i]:
            for j in range(i * i, max, i):
                bool_list[j] = False

    return [i for i, val in enumerate(bool_list) if val]


def gen_prime(min : int, primes_list = [2]):
    s = random.choice(primes_list[-20:])

    while s < min:
        lo, hi = (s + 1) >> 1, (s << 1) + 1
        while True:
            r = random.randint(lo, hi) << 1
            n = s * r + 1

            for p in primes_list:
                if not n % p:
                    continue

            while True:
                a = random.randint(2, n - 1)

                if pow(a, n - 1, n) != 1:
                    break

                d = math.gcd((pow(a, r, n) - 1) % n, n)
                if d != n:
                    if d == 1:
                        s = n
                    break

            if s == n:
                break
    
    return s

def fermat_test(num : int, retry = 1):
    a = random.randint(2, num - 1)

    for i in range(retry):
        if pow(a, num - 1, num) != 1:
            return False 

    return True 


if __name__ == '__main__':
    prime = gen_prime(10 ** 50, primes_list(1000))
    print(res_fstr('Generated number', prime))
    print(res_fstr('Fermat\'s lit. th.', fermat_test(prime, 5)))
