'use strict'

let graph = {
    nodes: [],
    edges: [],
    clear: function () {
        this.nodes = []
        this.edges = []
    },
    containsCrossingEdges: function() {
        return !this.edges.every((item, index, arr) => {
            for (let i = index + 1; i < arr.length; i++) {
                if (item.start === arr[i].start || item.start === arr[i].end || item.end === arr[i].start || item.end === arr[i].end) {
                    continue
                }
                if (item.cross(arr[i])) {
                    return false
                }
            }
            return true
        })
    },
    makeRegular: function() {
        let statusNodes = []
        let statusEdges = []
    }
}
