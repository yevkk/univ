
'use strict'

class Point {
    #x
    #y
    #in = []
    #out = []
    #value = 0

    constructor(x, y, value) {
        this.#x = x
        this.#y = y
        this.#value = value
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

    get value() {
        return this.#value
    }

    set value(val) {
        this.#value = val
    }

    edges() {
        return this.#in.concat(this.#out)
    }

    adj() {
        return this.#in.map(e => e.start).concat(this.#out.map(e => e.end))
    }

    adj_values() {
        return this.#in.map(e => e.value).concat(this.#out.map(e => e.value))
    }

    addEdge(edge) {
        if (edge.start === this) {
            this.#out.push(edge)
        } else if (edge.end === this) {
            this.#in.push(edge)
        }
    }

}