
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

    edges() {
        return this.#in.concat(this.#out)
    }

    addEdge(edge) {
        if (edge.start === this) {
            this.#out.push(edge)
        } else if (edge.end === this) {
            this.#in.push(edge)
        }
    }

}