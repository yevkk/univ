'use strict'

class Edge {
    #start
    #end
    #W = 0

    constructor(point1, point2) {
        if (point1.y > point2.y) {
            this.#start = point1
            this.#end = point2
        } else {
            this.#start = point2
            this.#end = point1
        }
        point1.addEdge(this)
        point2.addEdge(this)
    }

    get start() {
        return this.#start
    }

    get end() {
        return this.#end
    }

    get W() {
        return this.#W
    }

    set W(value) {
        this.#W = value
    }
}
