'use strict'

let pointToCheck
let strips = []

let graph = {
    points: [],
    edges: [],
    clear: function () {
        this.points = []
        this.edges = []
    },
    containsCrossingEdges: function () {
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
    makeRegular: function () {
        let binarySearch = (currentNode) => {
            let left = 0
            let right = statusEdges.length - 1
            while (right - left > 1) {
                let mid = Math.ceil((right + left) / 2)
                if (statusEdges[mid].pointRelativeX(currentNode) < 0) {
                    right = mid
                } else {
                    left = mid
                }
            }
            if (statusEdges[right].pointRelativeX(currentNode) > 0) {
                return right + 1
            }
            if (statusEdges[left].pointRelativeX(currentNode) < 0) {
                return left
            }
            return right
        }
        let currentNode

        let statusEdges = [...this.points[0].out]
        let statusNodes = new Array(statusEdges.length + 1).fill(this.points[0])

        for (let i = 1; i < this.points.length; i++) {
            currentNode = this.points[i]


            if (currentNode.in.length === 0) {
                if (statusEdges.length !== 0) {
                    let index = binarySearch(currentNode)
                    graph.edges.push(new Edge(currentNode, statusNodes[index]))
                    statusEdges.splice(index, 0, ...currentNode.out)
                    statusNodes.splice(index, 1, ...new Array(currentNode.out.length + 1).fill(currentNode))
                } else {
                    graph.edges.push(new Edge(currentNode, statusNodes[0]))
                    statusEdges = [...currentNode.out]
                    statusNodes = new Array(statusEdges.length + 1).fill(currentNode)
                }
            } else {
                let index = statusEdges.findIndex(item => item === currentNode.in[0])
                statusEdges.splice(index, currentNode.in.length, ...currentNode.out)
                statusNodes.splice(index, currentNode.in.length + 1, ...new Array(currentNode.out.length + 1).fill(currentNode))
                if (statusNodes.length === 0) {
                    statusNodes.push(currentNode)
                }
            }
        }

        statusEdges = [...this.points[this.points.length - 1].in]
        statusNodes = new Array(statusEdges.length + 1).fill(this.points[this.points.length - 1])

        for (let i = this.points.length - 2; i >= 0; i--) {
            currentNode = this.points[i]


            if (currentNode.out.length === 0) {
                if (statusEdges.length !== 0) {
                    let index = binarySearch(currentNode)
                    graph.edges.push(new Edge(currentNode, statusNodes[index]))
                    statusEdges.splice(index, 0, ...currentNode.in)
                    statusNodes.splice(index, 1, ...new Array(currentNode.in.length + 1).fill(currentNode))
                } else {
                    graph.edges.push(new Edge(currentNode, statusNodes[0]))
                    statusEdges = [...currentNode.in]
                    statusNodes = new Array(statusEdges.length + 1).fill(currentNode)
                }
            } else {
                let index = statusEdges.findIndex(item => item === currentNode.out[0])
                statusEdges.splice(index, currentNode.out.length, ...currentNode.in)
                statusNodes.splice(index, currentNode.out.length + 1, ...new Array(currentNode.in.length + 1).fill(currentNode))
                if (statusNodes.length === 0) {
                    statusNodes.push(currentNode)
                }
            }
        }
    }
}

function Strip(y) {
    return {
        y,
        segments: []
    }
}

function buildStrips(graph) {
    let strips = []
    graph.points.sort((point1, point2) => point2.y - point1.y)
    let status = []
    for (let i = 0; i < graph.points.length; i++) {
        let point = graph.points[i]
        let strip = new Strip(point.y)
        status = status.filter((item) => !point.in.includes(item))
        status.push(...graph.points[i].out)
        status.sort((edge1, edge2) => {
            let x1 = edge1.start.x + (point.y - edge1.start.y) * (edge1.end.x - edge1.start.x) / (edge1.end.y - edge1.start.y)
            let x2 = edge2.start.x + (point.y - edge2.start.y) * (edge2.end.x - edge2.start.x) / (edge2.end.y - edge2.start.y)
            return x1 - x2
        })
        strip.segments = [...status]
        strips.push(strip)
    }
    return strips
}

function locatePoint(point, strips) {
    let stripIndex = strips.findIndex(strip => strip.y < point.y) - 1
    let segmentIndex = strips[stripIndex].segments.findIndex(segment => segment.pointRelativeX(point) < 0) - 1

    return {
        stripIndex,
        segmentIndex
    }
}