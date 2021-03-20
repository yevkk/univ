'use strict'

class Point {
    #x
    #y
    #in = []
    #out = []

    constructor(x, y) {
        this.#x = x
        this.#y = y
    }

    get x() {
        return this.#x
    }

    get y() {
        return this.#y
    }

    get in() {
        return this.#in
    }

    get out() {
        return this.#out
    }
}
