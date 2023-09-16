import sys

def answer_fstr(op_str, res):
    op_str = op_str + ':'
    return f'{op_str:10}{res}'

def to_int(str):
    try:
        return int(str)
    except:
        print('Err: invalid input')
        exit()

if __name__ == '__main__':
    a, b = 0, 0

    if len(sys.argv) < 3:
        a = to_int(input('a = '))
        b = to_int(input('b = '))
    else:
        a, b = [to_int(n) for n in sys.argv[1:3]]

    print(answer_fstr('a + b', a + b))
    print(answer_fstr('a * b', a * b))
    print(answer_fstr('a ^ 2', a ** 2))
    print(answer_fstr('b ^ 2', b ** 2))
    print(answer_fstr('a mod b', a % b))
