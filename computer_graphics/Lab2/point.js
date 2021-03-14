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

    get Win() {
        return this.in.reduce((sum, item) => sum + item.W, 0)
    }

    get Wout() {
        return this.out.reduce((sum, item) => sum + item.W, 0)
    }

    addEdge(edge) {
        if (edge.start === this) {
            let ctg = e => (e.start.x - e.end.x) / (e.start.y - e.end.y)
            let index = this.#out.findIndex(item => ctg(item) < ctg(edge))
            if (index === -1) {
                this.#out.push(edge)
            } else {
                this.#out.splice(index, 0, edge)
            }
        } else if (edge.end === this) {
            let ctg = e => (e.end.x - e.start.x) / (e.start.y - e.end.y)
            let index = this.#in.findIndex(item => ctg(item) < ctg(edge))
            if (index === -1) {
                this.#in.push(edge)
            } else {
                this.#in.splice(index, 0, edge)
            }
        }
    }

}
