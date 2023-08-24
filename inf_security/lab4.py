import sys

import des as des_lib


ECB = des_lib.ECB
CBC = des_lib.CBC


class DES_wrapper:
    NULL_IV = b'\0\0\0\0\0\0\0\0'

    @staticmethod
    def encrypt(in_file, out_file, key, mode = ECB):
        des_inst = des_lib.des(bytes(key, 'utf-8'), mode, DES_wrapper.NULL_IV)
        
        data = ''
        with open(in_file, 'r') as f:
            data = f.read()
        
        with open(out_file, 'wb') as f:
            f.write(des_inst.encrypt(data))


    @staticmethod
    def decrypt(in_file, out_file, key, mode = ECB):
        des_inst = des_lib.des(bytes(key, 'utf-8'), mode, DES_wrapper.NULL_IV)
        
        enc = bytes()
        with open(in_file, 'rb') as f:
            enc = f.read()
        
        with open(out_file, 'w') as f:
            f.write(str(des_inst.decrypt(enc), 'utf-8'))


if __name__ == '__main__': 
    if len(sys.argv) < 5:
        print('No required args provided')  
    else:
        mode, in_file, out_file, key = sys.argv[1:5]

        if  mode == 'enc':
            print(f'Encrypting text from "{in_file}"...')   
            DES_wrapper.encrypt(in_file, out_file, key)
            print(f'Saved encrypted data into "{out_file}"') 

        elif  mode == 'dec':
            print(f'Decrypting data from "{in_file}"...')   
            DES_wrapper.decrypt(in_file, out_file, key)
            print(f'Saved decrypted text into "{out_file}"') 

        elif  mode == 'enc_cbc':
            print(f'Encrypting text from "{in_file}" in CBC mode...')   
            DES_wrapper.encrypt(in_file, out_file, key, CBC)
            print(f'Saved encrypted data into "{out_file}"') 

        elif  mode == 'dec_cbc':
            print(f'Decrypting data from "{in_file}" in CBC mode...')   
            DES_wrapper.decrypt(in_file, out_file, key, CBC)
            print(f'Saved decrypted text into "{out_file}"') 

        else:
            print('Invalid mode')
