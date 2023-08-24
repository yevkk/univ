import random
import math
import sys

import lab2 as utils


def str_hex(b_arr, lf_after = 16):
    length  = len(b_arr)
    counter = 0
    res = ''
    while counter < length:
        res += ' '.join(f'{b:02x}' for b in b_arr[counter : min(counter + lf_after, length)]) + '\n'
        counter += lf_after

    return res


def extended_euclid(a, b):
        if b == 0:
            return a, 1, 0
        d, x, y = extended_euclid(b, a % b)
        return d, y, x - (a // b) * y


class RSA:
    PUBLIC_KEY_FILENAME  = 'public.key'
    PRIVATE_KEY_FILENAME = 'private.key'
    KEY_CHUNK_BYTES      = 64
    PRIME_GEN_MIN        = 10 ** 50 


    @staticmethod
    def save_key(preffix, key, filename):
        with open(filename, 'wb') as f:
            key_bytes = preffix.to_bytes(RSA.KEY_CHUNK_BYTES) + key.to_bytes(RSA.KEY_CHUNK_BYTES)
            f.write(key_bytes)
            print(f'Saved key:\n{str_hex(key_bytes)}as "{filename}"\n')


    @staticmethod
    def get_key(filename):
        with open(filename, 'rb') as f:
            key_bytes = f.read()
        return key_bytes[:RSA.KEY_CHUNK_BYTES], key_bytes[RSA.KEY_CHUNK_BYTES:]


    @staticmethod
    def key_gen():
        primes_list = utils.primes_list(1000)

        p     = utils.gen_prime(RSA.PRIME_GEN_MIN, primes_list)
        q     = utils.gen_prime(RSA.PRIME_GEN_MIN, primes_list)
        n     = p * q
        phi_n = (p - 1) * (q - 1)

        e = 2
        while math.gcd(e, phi_n) != 1:
            e = random.choice(primes_list)
        
        d = extended_euclid(e, phi_n)[1] % phi_n

        RSA.save_key(e, n, RSA.PUBLIC_KEY_FILENAME)
        RSA.save_key(d, n, RSA.PRIVATE_KEY_FILENAME)


    @staticmethod
    def encrypt(in_file, out_file, key_file = PUBLIC_KEY_FILENAME):
        counter    = 0
        data       = bytes()
        key_bytes  = RSA.get_key(key_file)
        chunk_size = RSA.KEY_CHUNK_BYTES >> 2

        with open(in_file, 'r') as f: data = bytes(f.read(), 'utf-8')
        data_length = len(data)

        with open(out_file, 'wb') as f_out: 
            while counter < data_length:
                chunk = data[counter : min(counter + chunk_size, data_length)]
                enc   = pow(int.from_bytes(chunk), int.from_bytes(key_bytes[0]), int.from_bytes(key_bytes[1]))
                f_out.write(enc.to_bytes(RSA.KEY_CHUNK_BYTES))
                counter += chunk_size


    @staticmethod
    def decrypt(in_file, out_file, key_file = PRIVATE_KEY_FILENAME):
        counter   = 0
        data      = bytes()
        key_bytes = RSA.get_key(key_file)
        chunk_size = RSA.KEY_CHUNK_BYTES

        with open(in_file, 'rb') as f: data = f.read()
        data_length = len(data)

        with open(out_file, 'w') as f_out:
            while counter < data_length:
                enc   = int.from_bytes(data[counter : min(counter + chunk_size, data_length)])
                dec   = pow(enc, int.from_bytes(key_bytes[0]), int.from_bytes(key_bytes[1]))
                chunk = str(dec.to_bytes(RSA.KEY_CHUNK_BYTES), 'utf-8', 'replace').replace('\0', '')
                f_out.write(chunk)
                counter += chunk_size



if __name__ == '__main__':   
    mode = sys.argv[1]
    if mode == 'keygen':
        print('Generating keys...')
        RSA.key_gen()
    else:
        in_file  = sys.argv[2]
        out_file = sys.argv[3]

        if  mode == 'enc':
            print(f'Encrypting text from "{in_file}"...')   
            RSA.encrypt(in_file, out_file)
            print(f'Saved encrypted data into "{out_file}"') 
        elif  mode == 'dec':
            print(f'Decrypting data from "{in_file}"...')   
            RSA.decrypt(in_file, out_file)
            print(f'Saved decrypted text into "{out_file}"') 
        else:
            print('Invalid mode')

