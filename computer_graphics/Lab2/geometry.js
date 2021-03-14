'use strict'

let pointToCheck
let chains = []

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
    },
    weightBalance: function () {
        this.edges.forEach((edge) => edge.W = 1)
        for (let i = 1; i < this.points.length - 1; i++) {
            let currentNode = this.points[i]
            if (currentNode.Win > currentNode.Wout) {
                currentNode.out[0].W = currentNode.Win - currentNode.Wout + 1
            }
        }
        for (let i = this.points.length - 2; i > 0; i--) {
            let currentNode = this.points[i]
            if (currentNode.Wout > currentNode.Win) {
                currentNode.in[0].W = currentNode.Wout - currentNode.Win + currentNode.in[0].W
            }
        }
    },
    buildChains: function() {
      let chains = []
      while(this.points[0].Wout > 0) {
          let chain = new Chain()
          let currentPoint = this.points[0]
          while (currentPoint !== this.points[this.points.length - 1]) {
              let edge =  currentPoint.out.find(item => item.W > 0)
              edge.W--
              chain.edges.push(edge)
              currentPoint = edge.end
          }
          chains.push(chain)
      }
      return chains
    }
}
