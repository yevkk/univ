package main

type Graph struct {
	data [][]int
	size int
}

func (g *Graph) addNode() {
	g.size++
	g.data = append(g.data, make([]int, g.size))
	for i := 0; i < g.size; i++ {
		g.data[i] = append(g.data[i], 0)
	}
}

func (g *Graph) delNode(index int) {
	g.size--
	g.data[index] = g.data[g.size]
	g.data = g.data[:g.size]
	for i := 0; i < g.size; i++ {
		g.data[i][index] = g.data[i][g.size]
		g.data[i] = g.data[i][:g.size]
	}
}

//can be used for adding edged
func (g *Graph) updateEdge(start, end, weight int) {
	g.data[start][end] = weight
	g.data[end][start] = weight
}

type Path struct {
	nodes     []int
	sumWeight int
}

var paths []Path

func (g *Graph) findPaths(start, end int) []Path {
	paths = make([]Path, 0)

	visited := make([]bool, g.size)
	path := make([]int, g.size)
	pathIndex := 0

	for i := range visited {
		visited[i] = false
	}

	g.findPathsUtil(start, end, visited, path, pathIndex)

	return paths
}

func (g *Graph) findPathsUtil(start, end int, visited []bool, path []int, pathIndex int) {
	visited[start] = true
	path[pathIndex] = start
	pathIndex++

	if start == end {
		sumWeight := 0
		resPath := make([]int, pathIndex)
		copy(resPath, path[:pathIndex])
		for i := 0; i < pathIndex-1; i++ {
			sumWeight += g.data[path[i]][path[i+1]]
		}
		paths = append(paths, Path{resPath, sumWeight})
	} else {
		for i := 0; i < g.size; i++ {
			if i != start && g.data[start][i] > 0 && !visited[i] {
				g.findPathsUtil(i, end, visited, path, pathIndex)
			}
		}
	}

	pathIndex--
	visited[start] = false
}
